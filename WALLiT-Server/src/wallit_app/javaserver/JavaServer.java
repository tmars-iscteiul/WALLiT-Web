package wallit_app.javaserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import wallit_app.main.Main;


/**
 * The java server will serve as an initial connection establishing access point to android clients. 
 * For every client wanting to connect, it will instantiate and run a thread of {@link ConnectionHandler}, that will handle its socket.
 * @author skner
 *
 */
@Service
public class JavaServer extends Thread	{
	

	private ServerSocket serverSocket;
	private ArrayList<ConnectionHandler> connectedClients;
	private boolean running;
	
	public JavaServer()	{
		try {
			serverSocket = new ServerSocket(Main.javaserverPort);
			System.out.println("[JavaServer] Server launched on " + InetAddress.getLocalHost().getHostAddress() + ":" + Main.javaserverPort);
			running = true;
			connectedClients = new ArrayList<ConnectionHandler>();
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()	{
		// This thread will ONLY await client connection attempts, setting up a connection handler for each connected client.
		System.out.println("[JavaServer] Awaiting connections...");
		while(running)	{
			try {
				Socket clientSocket = serverSocket.accept();	// Locked until a new client attempts to connect
				System.out.println("[JavaServer] Connection accepted: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
				connectedClients.add(new ConnectionHandler(clientSocket, connectedClients.size(), this));
				connectedClients.get(connectedClients.size()-1).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void clearConnectionHandler(int id)	{
		connectedClients.remove(id);
	}
	
	public boolean isOnline()	{
		return running;
	}
	
	public int getConnectedClients()	{
		return connectedClients.size();
	}
}
