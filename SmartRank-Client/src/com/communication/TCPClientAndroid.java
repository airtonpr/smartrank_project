package com.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class used as a utils class for treat data and send it to a server. 
 * @author airton
 *
 */
public class TCPClientAndroid {
	private static Socket clientSocket;
	private static OutputStream outToServer;
	private String ip;

	public TCPClientAndroid(String host, int port) throws UnknownHostException, IOException{
		clientSocket = new Socket();
		clientSocket.connect(new InetSocketAddress(host, port), 4000);
		this.ip = host;
	}
	
    public String sendPicture(String filePath) throws IOException{
    	return this.sendBytes(imageToBytes(filePath));
    }
    
    public String sendBytes(byte[] myByteArray) throws IOException {
	    return sendBytes(myByteArray, 0, myByteArray.length);
	}

	public String sendBytes(byte[] myByteArray, int start, int len) throws IOException {
		String answer;
		if (len < 0)
	        throw new IllegalArgumentException("Negative length not allowed");
	    if (start < 0 || start >= myByteArray.length)
	        throw new IndexOutOfBoundsException("Out of bounds: " + start);
	    
	    
	    outToServer = clientSocket.getOutputStream(); 
	    DataOutputStream dos = new DataOutputStream(outToServer);

	    dos.writeInt(len);
	    if (len > 0) {
	        dos.write(myByteArray, start, len);
	        System.out.println("sending...");
	    }

	    DataInputStream c = new DataInputStream(clientSocket.getInputStream());
	    answer = readData();
	    System.err.println("The result from server "+ ip +" is: " + answer);
	    
	    return answer;
	}
	
	public String readData() {
	    try {
	    InputStream is = clientSocket.getInputStream();
	    ObjectInputStream ois = new ObjectInputStream(is);
	     return (String)ois.readObject();
	    } catch(Exception e) {
	        return null;
	    }
	}
	
	private static byte[] imageToBytes(String inFile) throws IOException{   
        InputStream is = null;    
        byte[] buffer = null;   
        is = new FileInputStream(inFile);   
        buffer = new byte[is.available()];   
        is.read(buffer);   
        is.close();   
        return buffer;  
 }  
}