import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

class Server {
	
	int port;
	Socket client = null;
	ServerSocket server = null;
	ExecutorService pool = null;
	FileOutputStream log = null;
	
	int clientcount = 0;
	
	public static void main(String argv[]) {
		Server serverobj = new Server(5520);
		try {
			serverobj.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Server(int port){
		this.port = port;
		pool = Executors.newFixedThreadPool(10);
	}
	
	public void run() throws IOException{
		server = new ServerSocket(5520);
		
		
		File folder = new File("Juan Cruz"); 
		folder.mkdir(); // create a folder in your current work space
		File file = new File(folder, "prog1b.log.txt"); // put the file inside the folder
		file.createNewFile();
		
		log = new FileOutputStream(file);
		
		while( true ) {
			Socket sock = server.accept(); // listens for connections
			ServerThread servThread = new ServerThread(sock, clientcount, log);
			servThread.start();
			clientcount++;
			//pool.execute(servThread);
		}
	}
}

class ServerThread extends Thread{
	Socket client = null;
	BufferedReader cin;
	PrintStream cout;
	FileOutputStream log;
	PrintWriter logfile;
	
	Scanner sc = new Scanner(System.in);
	int id;
	String s;
	
	
	
	ServerThread(Socket client, int count, FileOutputStream log) throws IOException{
		this.client = client;
		this.id = count;
		this.log = log;
		Date dateOne = new Date();
		
		s = "Got a Connection: "+dateOne.toString()+" "+client.getInetAddress() + " Port : " + client.getPort() +"\n";
		byte[] byteData = s.getBytes();
		for(int i = 0;  i < byteData.length; i++) {
			try {
				log.write(byteData[i]);
			}catch (IOException t) {
				t.printStackTrace();
				return;
			}
		}
		
		cin = new BufferedReader(new InputStreamReader(client.getInputStream()));
		cout = new PrintStream(client.getOutputStream());
		
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				s = cin.readLine();
				if(s == null) {
					break;
				}
				String z = "Client"+id+": " + s + "\n";
				byte[] byteData = z.getBytes();
				for(int i = 0;  i < byteData.length; i++) {
					try {
						log.write(byteData[i]);
					}catch (IOException t) {
						t.printStackTrace();
						//log.write(b);
						return;
					}
				}
				
				String check = s.toLowerCase();
				if(check.matches("quit")) {
					cout.println("bye");
					z = "Server: bye\n";
					byteData = z.getBytes();
					for(int i = 0;  i < byteData.length; i++) {
						try {
							log.write(byteData[i]);
						}catch (IOException t) {
							t.printStackTrace();
							//log.write(b);
							return;
						}
					}
					break;
				}
				
				s = Encrypt(s);
				cout.println(s);
				s = "Server: " + s + "\n";
				byteData = s.getBytes();
				for(int i = 0;  i < byteData.length; i++) {
					try {
						log.write(byteData[i]);
					}catch (IOException t) {
						t.printStackTrace();
						//log.write(b);
						return;
					}
				}
				
			}
			s = "Connection closed with Client"+id+" Port:" + client.getPort()+"\n";
			byte[] byteData = s.getBytes();
			for(int i = 0;  i < byteData.length; i++) {
				try {
					log.write(byteData[i]);
				}catch (IOException t) {
					t.printStackTrace();
					//log.write(b);
					return;
				}
			}
			cin.close();
			client.close();
			cout.close();
		}catch(IOException e){
			System.out.println("Error : "+ e);
		}
	}
	//The Caesar cipher 5 and 19
	String Encrypt(String s) {
		int x = s.length();
		String cyph[] = new String[x];
		int i = 0;
		int count = 0;
		while(x > 0) {
			char c = s.charAt(i);
			int ascii = (int) c;
			if(ascii >= 65 && ascii <= 90 || ascii >= 97 && ascii <= 122) {
				ascii = Convert((int) c, count);
				cyph[i] = Character.toString((char)ascii);
				x--;
				i++;
				count++;
			}else {
				cyph[i] = Character.toString((char)ascii);
				x--;
				i++;
			}
			//System.out.println(count);
			
			if(count == 5)
				count = 0;
			
		}
		StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < cyph.length; j++) {
            stringBuilder.append(cyph[j]);
        }
		s = stringBuilder.toString();
		return s;
	}
	//Converts an ascii character depending on count number
	int Convert(int x, int count) {
		if(count == 0 || count == 3) {
			if(x >= 118 && x <= 122) {
				return x - 21;
			}else if(x >= 97 && x < 118){
				return x + 5;
			}else if(x >= 86 && x <= 90) {
				return x-21;
			}else if(x >= 65 && x < 86){
				return x + 5;
			}
			return x;
		}else {
			if(x >= 104 && x<= 122) {
				return x - 7;
			}else if(x >= 97 && x < 104){
				return x + 19;
			}else if(x >= 72 && x <= 90) {
				return x - 7;
			}else if(x >= 65 && x < 72){
				return x + 19;
			}
			return x;
		}	
	}
}





