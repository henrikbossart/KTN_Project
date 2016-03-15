package gui;

import client.Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

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
	private Client client;

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
		send = new JButton(actions);
		send.setText("Send");
		input = new JTextField();
		panel.add(input, BorderLayout.CENTER);
		panel.add(send, BorderLayout.EAST);
        input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    input.setText("");
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
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
		login = new JButton(actions);
		login.setText("Login");
		logout = new JButton(actions);
		logout.setText("Logout");
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

	private Action actions = new AbstractAction() {
		private static final long serialVersionUID = 1L;
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == login && username.getText().length() > 0 && address.getText().length() > 0) {
				client = new Client(address.getText(), 10000, username.getText());
				client.start();
			}
			if (e.getSource() == send && input.getText().length() > 0) {
				client.sendMessage(client.createPayload("msg", input.getText()));
			}
			if (e.getSource() == logout) {
				client.disconnect();
			}
		}
	};
}
