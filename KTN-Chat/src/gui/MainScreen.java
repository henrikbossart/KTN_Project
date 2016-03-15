package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainScreen extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JButton send;
	private JButton login;
	private JButton logout;
	private JTextField address;
	private JTextField username;
	private JTextField input;
	private JTextArea chatLog;
	private JTextArea users;

	private void init() {
		this.setTitle("Chat");
		this.setLayout(new BorderLayout(5,5));
		this.setMinimumSize(new Dimension(400,300));
		this.setLocationRelativeTo(null);
		
		this.getContentPane().add(chatLog(), BorderLayout.CENTER);
		this.getContentPane().add(input(), BorderLayout.SOUTH);
		this.getContentPane().add(users(), BorderLayout.WEST);
		this.getContentPane().add(login(), BorderLayout.NORTH);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		this.pack();
	}

	private void run() {
		while(true) {
			
		}
	}

	public static void main(String[] args) {
		MainScreen gui = new MainScreen();
		gui.init();
		gui.run();
	}
	
	private JPanel chatLog() {
		JPanel panel = new JPanel(new BorderLayout(5,5));
		chatLog = new JTextArea(15,20);
		JScrollPane scrollPane = new JScrollPane(chatLog);
		panel.add(scrollPane);
		return panel;
	}
	
	private JPanel input() {
		JPanel panel = new JPanel(new BorderLayout(5,5));
		send = new JButton("Send");
		input = new JTextField();
		panel.add(input, BorderLayout.CENTER);
		panel.add(send, BorderLayout.EAST);
		return panel;
	}
	
	private JPanel users() {
		JPanel panel = new JPanel(new BorderLayout(5,5));
		users = new JTextArea(15,10);
		JScrollPane scrollPane = new JScrollPane(users);
		panel.add(scrollPane);
		return panel;
	}
	
	private JPanel login() {
		JPanel panel = new JPanel(new GridLayout(3,2));
		JLabel usernameLabel = new JLabel("Username");
		JLabel addressLabel = new JLabel("IP-Address");
		login = new JButton("login");
		logout = new JButton("logout");
		username = new JTextField(10);
		address = new JTextField(15);
		panel.add(usernameLabel);
		panel.add(addressLabel);
		panel.add(username);
		panel.add(address);
		panel.add(login);
		panel.add(logout);
		return panel;
	}
}
