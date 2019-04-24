package wallit.javaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * @author skner
 *
 */
public class JavaServer extends Thread	{
	
	public static final int port = 8080;
	private ServerSocket serverSocket;
	private ArrayList<ConnectionHandler> connectedClients;
	// Connected clients list with various threads per active connection
	// 
	private boolean running;
	
	public JavaServer()	{
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("[JavaServer] Server launched on port " + port);
			running = true;
			connectedClients = new ArrayList<ConnectionHandler>();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run()	{
		System.out.println("[JavaServer] Awaiting connections...");
		while(running)	{
			try {
				Socket clientSocket = serverSocket.accept();	// Locked until a new client attempts to connect
				System.out.println("[JavaServer] Connection accepted: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
				connectedClients.add(new ConnectionHandler(clientSocket, connectedClients.size()));
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
