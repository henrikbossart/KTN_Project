package chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import chat.chat.Chat;

public class Client extends Thread {
	private Socket client;
	private String address;
	private PrintWriter out;
	private BufferedReader in;
	private int port;
	
	public Client(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			client = new Socket(InetAddress.getByName(address), port);
			Chat.log("Connection to "+address+" set up");
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
			String line = "";
			while((line = in.readLine()) != null) {
				Chat.log(line);
			}
			Chat.log(address + " has disconnected from the server");
		} catch (IOException e) {
			Chat.log("Connection failed: " + e.getMessage());
		}
	}
	
	public void sendMessage(String msg) {
		if(client.isConnected()) {
			out.println(msg);
		}
	}
}
