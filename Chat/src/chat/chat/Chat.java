package chat.chat;

import chat.client.Client;
import chat.server.Server;
import chat.ui.MainWindow;
import chat.ui.OptionWindow;

public class Chat {

	private static Server server;
	private static Client client;
	private static MainWindow win;
	private static boolean isServer;
	private static String nickname, address;

	public static void main(String[] args) {
		win = new MainWindow();
		new OptionWindow();
	}

	public static void log(String msg) {
		win.log(msg.replace("%nick%", "(" + nickname + "): "));
	}

	public static void sendMessage(String msg) {
		if (isServer) {
			server.sendMessage(msg.replace("%nick%", "(" + nickname + "): "));
		} else {
			client.sendMessage(msg.replace("%nick%", "(" + nickname + "): "));
		}
	}

	public static void establishConnection(boolean isserver, String nick, String addresse) {
		isServer = isserver;
		nickname = nick;
		address = addresse;

		if (isServer) {
			(server = new Server(10000)).start();
		} else {
			(client = new Client(address, 10000)).start();
		}
	}
}
