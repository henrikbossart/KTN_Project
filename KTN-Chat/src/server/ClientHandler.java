package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import net.sf.json.JSONObject;

public class ClientHandler implements Runnable {

	private Socket client;

	public ClientHandler(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		boolean connected = true;
		while (connected) {
			// get the incoming message
			ObjectInputStream incoming;
			try {
				incoming = new ObjectInputStream(client.getInputStream());
				JSONObject clientMessage = (JSONObject) incoming.readObject();

				// act based on content of JSONObject
				switch (clientMessage.getString("request")) {
				case "login":
					String username = clientMessage.getString("content");
					if (ThreadedTCPServer.addClient(username, client)) {
						// send reply: successful connection
					} else {
						// send reply: unsuccessful connection, and terminate connection
						connected = false;
					}
					break;

				case "logout":
					// remove client from list of active users

					// terminate connection
					connected = false;
					break;

				case "msg":
					// tell server to broadcast message

					break;

				case "names":
					// send reply with list of user names
					break;

				case "help":
					// send reply with help text
					break;

				default:
					// send reply with unknown command error
					break;
				}

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} 
		}
	}
}