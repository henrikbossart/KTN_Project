package chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import chat.chat.Chat;

public class Server extends Thread {
	private ServerSocket server;
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;
	private String clientAddress;
	private List<String> connectedUsers = new ArrayList<>();

	public Server(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				client = server.accept();
				clientAddress = client.getRemoteSocketAddress().toString().split(":")[0].substring(1);
				Chat.log(clientAddress + " has established a connection");
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
				
				String line = "";
				while((line = in.readLine()) != null) {
					Chat.log(line);
				}
				Chat.log(clientAddress + " has disconnected");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg) {
		if(client != null){
			out.println(msg);
		}
	}
}
