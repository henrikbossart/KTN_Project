package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import net.sf.json.JSONObject;

public class ThreadedTCPServer {
	private static ServerSocket serverSocket;
	private static HashMap<String, Socket> clients;

	public static void start() throws Exception {
		// create the server
		serverSocket = new ServerSocket(10000);
		clients = new HashMap<String, Socket>();

		while (true) {
			Socket client = serverSocket.accept();
			new Thread(new ClientHandler(client)).start();
		}
	}

	public static boolean addClient(String username, Socket clientSocket) {
		if (!clients.containsKey(username)) {
			clients.put(username, clientSocket);
			return true;
		} else {
			return false;
		}
	}

	public static void broadcast(JSONObject message) {
		// send JSONObject to all clients
	}
}