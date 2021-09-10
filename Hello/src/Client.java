
import java.util.*;
import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader;  
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Client{
	
	int port;
	Socket client = null;
	ServerSocket server = null;
	ExecutorService pool = null;
	private final static AtomicInteger count = new AtomicInteger(0);
	
	public static void main(String[] args) {
		System.out.println("Client Booted.");
		PingClient ping = new PingClient();
		BufferedReader reader =  
                new BufferedReader(new InputStreamReader(System.in));
		String porty = "";
		try {
			System.out.print("Enter Address: ");
			String name = reader.readLine();
			ping.addr = InetAddress.getByName(name);
			
			System.out.print("Enter Port: ");
			porty = reader.readLine();
			System.out.println();
		} catch (UnknownHostException e) {
			System.out.println("Not a valid address, pls try again.");
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ping.port = Integer.parseInt(porty);
			ping.start();
		} catch(Exception e) {
			System.out.println("Goodbye");
		}
	}
		
}

class PingClient extends UDPPinger implements Runnable{
	
	
	int counter = 0;
	int port = 5520;
	InetAddress addr = null;
	
	
	public void run() {
		
		try {
			this.receiveSocket = new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.receiveSocket.connect(addr, port);
		long[] rttArray = new long[10];
		
		for(int x = 0; x< 10;  x++) {
			Calendar c1 = Calendar.getInstance();
			Date dateOne = c1.getTime();
			String payload = "PING " + counter + " " +  dateOne;
			PingMessage pingmessage = new PingMessage(addr,port,payload);
			try {
		    	long startTime = System.currentTimeMillis();
		    	sendPing(pingmessage);
		    	PingMessage pm = receivePing();
		    	long endTime = System.currentTimeMillis();
		    	long rtt = endTime - startTime;
		    	if(pm != null) {
		        	//System.out.print("Ping " + counter  + " received.");
		        	//System.out.println(" RTT: "  + rtt+".");
		    		dateOne = c1.getTime();
		    		System.out.println("Received packet from "+receiveSocket.getInetAddress()+" "+ dateOne);
		        	rttArray[counter] = rtt;
		        }else {
		        	System.out.println("Packet not received.");
		        	rttArray[counter] = 1000;
		        }
		    	TimeUnit.SECONDS.sleep(1);
		    } catch (Exception e) {
		    	System.out.println("Error in Sending/Receiving Ping");
		    	
		    }
			counter++;
		}
		long rttMin = rttArray[0];
		long rttMax = rttArray[0];
		long sum = 0;
		for (int x = 0; x<10;  x++) {
			if(rttMax < rttArray[x]) {
				rttMax = rttArray[x];
			}
			if(rttMin > rttArray[x]) {
				rttMin = rttArray[x];
			}
			sum = sum + rttArray[x];
			
			
		}
		for (int x = 0; x<10;  x++) {
			System.out.print("PING "+ x + " : ");
			if(rttArray[x] == 1000) {
				System.out.println("false RTT: 1000");
			} else {
				System.out.println("true RTT: "+ rttArray[x]);
			}
			
			
		}
		long rttAverage = sum/10;
		System.out.print("RTT Minimum: " + rttMin  + "ms, ");
		System.out.print("RTT Maximum: " + rttMax  + "ms, ");
		System.out.print("RTT Average: " + rttAverage  + "ms");
		receiveSocket.close();
	}
}

class PingMessage{
	InetAddress addr;
	int port = 0;
	String payload;
	
	public PingMessage(InetAddress addr, int port, String payload) {
		this.addr = addr;
		this.port = port;
		this.payload = payload;
	}
	public InetAddress getIP() { //get the destination IP address
		return addr;
		
	}
	public int getPort() {//get the destination port number
		return port;
	}
	public String getPayload() { //get the content of the payload
		return payload;
	}
}

class UDPPinger extends Thread{
	
	DatagramSocket receiveSocket;
	
	public void sendPing(PingMessage ping) {
		String z = ping.getPayload();
		byte[] byteData = z.getBytes();
		byte[] bytes = new byte[512];
		for(int i = 0;i < byteData.length; i++ ) {
			bytes[i] = byteData[i];
		}
		try {
			//this.receiveSocket = receiveSocket;
			
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			receiveSocket.send(packet);	     
		}catch (IOException e) {
	        e.printStackTrace();
	    }
		
		
	}
	//This method receives the UDP packet from the Server. This method may throw
	// SocketTimeoutException.
	public PingMessage receivePing() throws SocketException {
		byte[] buffer = new byte[512];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		InetAddress addr = null;
		int port = 0;
		String msg =  "";
		
		PingMessage ping = new PingMessage(addr,port,msg);
		try {
			receiveSocket.setSoTimeout(1000);
	        receiveSocket.receive(packet);
	        ping.addr = packet.getAddress();
	        ping.port = packet.getPort();
	        ping.payload = new String(packet.getData(), packet.getOffset(), packet.getLength());
	        return ping;
	        
	        
	    } catch (SocketTimeoutException ste) {
	    	System.out.print("SocketTimeout: Receive timed out. ");
	    	return null;
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}





