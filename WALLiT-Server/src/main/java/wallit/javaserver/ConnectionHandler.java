package wallit.javaserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * @author skner
 *
 */
public class ConnectionHandler extends Thread {

	private Socket clientSocket;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private int handlerID;
	
	public ConnectionHandler(Socket s, int id)	{
		try {
			handlerID = id;
			clientSocket = s;
			objectIn = new ObjectInputStream(clientSocket.getInputStream());
			objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
			consolePrint("Handler online.");
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run()	{
		// Wait for client requests and direct them to the main processing thread
		while(true)	{
			try {
				String receivedData = (String)objectIn.readObject();
				consolePrint("Received data from user. It reads: ");
				System.out.println(receivedData);
				consolePrint("Sending ack to client");
				objectOut.writeObject("Message received: " + receivedData);
	            objectOut.reset();
			} catch (IOException | ClassNotFoundException e) {
				consolePrint("Problem with receiving client's data.");
			}
		}
	}
	
	public ObjectOutputStream getObjectOut()	{
		return objectOut;
	}
	
	public ObjectInputStream getObjectIn()	{
		return objectIn;
	}
	
	private void consolePrint(String text)	{
		System.out.println("[Handler " + handlerID +  "] " + text);
	}
	
}
