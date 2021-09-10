import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.omg.CORBA.portable.OutputStream;

public class FinalServer {
	
	int port;
	ServerSocket server = null;
	FileOutputStream log = null;
	BufferedReader cin = null;
	PrintStream cout = null;
	DataInputStream in = null;
	private static final String UPLOAD_FOLDER = "C:\\temp\\";
	
	public static void main(String argv[]) {
		FinalServer serverobj = new FinalServer();
		while(true) {
			try {
				serverobj.run();
			} catch (IOException e) {
				System.out.println("Error in receiving file");
			}
		}
	}
	
	private FinalServer(){
	}
	
	public void run() throws IOException{
		try {
			server = new ServerSocket(5520);
		}catch(IOException e) {
			
		}
		
		
		while( true ) {
			System.out.println("Waiting...");
			Socket socket = server.accept();
			Date today = new Date();
			System.out.println("Got a connection: "+ today);
            System.out.println("Accepted connection : " + socket); 
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            
            //CREATE FILE
            File folder = new File("Juan Cruz"); 
    		folder.mkdir(); 
    		FileOutputStream fos = null;
            long z = 0;
            int first = 0;
            String name = "";
            long size = 0;
            while(true) {
            	int length = 0;
            	if(z == 0 || z > 1024) {
            		length = 1024;
            	}else{
            		length = (int)z;
            	}
                byte[] message = new byte[length];
                dIn.readFully(message, 0, message.length);
                
                if(first == 0) {
                	name = findName(message);
                	size  = findLength(message);
                	int find = find(message);
                	int boundary = 1024-find;
                	byte[] newmessage = newMessage(message, find, boundary);
                	
                	File file = new File(folder, name);
            		file.createNewFile();
            		FileWriter writer = new FileWriter(file);
            		fos = new FileOutputStream(file);
                	fos.write(newmessage);
                	first++;
                	z = size - 1024+find;
                }else {
                	z = z - 1024;
                	fos.write(message);
                }
                if(z <= 0) {
                	fos.close();
            		break;
            	}
            }
            System.out.println("received");
            System.out.println("File Name:  " + name);
            System.out.println("File Size: " + size);
            String s = "@";
            byte[] poop = s.getBytes();
            dout.write(poop,0,1);
            socket.close();
		}
		
	}
	
	private byte[] newMessage(byte[] message, int size, int boundary) {
		byte[] newmessage = new byte[boundary];
		for(int i = size; i < 1024; i++) {
			newmessage[i-size] = message[i];
		}
		return newmessage;
	}
	
	private int find(byte[] byteData) {
		int length = 0;
		int it = 0;
		for(int i = 0; i <  1024; i++) {
			if(byteData[i] == 0 && it == 1) {
				return length+1;
			}
			if(byteData[i] == 0 && it == 0) {
				it = 1;
			}
			length++;
		}
		return length + 1;
	}
	
	private String findName(byte[] byteData) {
		//NAME
		int namelength = 0;
		for(int i = 0; i <  1024; i++) {
			if(byteData[i] == 0) {
				break;
			}
			namelength++;
		}
		StringBuilder sb = new StringBuilder(namelength);
	    for (int i = 0; i < namelength; ++ i) {
	        if (byteData[i] < 0) throw new IllegalArgumentException();
	        sb.append((char) byteData[i]);
	    }
	    String name = sb.toString();
		return name;
	}
	private long findLength(byte[] byteData) {
		//LENGTH
		int start = 0;
		for(int c  = 0; c < 1024; c++) {
			if(byteData[c] == 0) {
				start  = c+1;
				break;
			}
		}
		int length  = 0;
		for(int c  = start; c < 1024; c++) {
			if(byteData[c] == 0) {
				length  = c;
				break;
			}
		}
		StringBuilder lb = new StringBuilder(length);
	    for (int i = start; i < length; ++ i) {
	        if (byteData[i] < 0) throw new IllegalArgumentException();
	        lb.append((char) byteData[i]);
	    }
	    String len = lb.toString();
	    long size = Long.parseLong(len);
	    return size;
	}

	
	private void begin() {
		
		PrintWriter logfile;
		
		Scanner sc = new Scanner(System.in);
		int id;
		String s = null;
		Date dateOne = new Date();
	}
}

