package chat.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import chat.chat.Chat;

public class OptionWindow extends JFrame{
	private static final long serialVersionUID = 2L;
	
	private JButton save;
	private JCheckBox isServer;
	private JTextField nickname, address;
	private String lastAddress;
	
	public OptionWindow() {
		this.setTitle("Settings");
		this.setSize(300, 200);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridLayout(4,2,0,15));
		
		this.getContentPane().add(new JLabel("Connection accept:"));
		this.getContentPane().add(isServer(false));
		this.getContentPane().add(new JLabel("Nickname:"));
		this.getContentPane().add(nickname("user"+new Random().nextInt(10000)));
		this.getContentPane().add(new JLabel("Server IP-address:"));
		this.getContentPane().add(address("localhost"));
		this.getContentPane().add(new JLabel());
		this.getContentPane().add(save());
		
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private JCheckBox isServer(boolean server) {
		isServer = new JCheckBox(actions);
		isServer.setSelected(server);
		return isServer;
	}
	
	private JTextField nickname(String nick) {
		nickname = new JTextField();
		nickname.setText(nick);
		return nickname;
	}
	
	private JTextField address(String addresse) {
		address = new JTextField();
		address.setText(addresse);
		return address;
	}
	
	private JButton save() {
		save = new JButton(actions);
		save.setText("Apply");
		return save;
	}
	
	private void hideWindow() {
		this.setVisible(false);
	}
	
	private Action actions = new AbstractAction() {
		private static final long serialVersionUID = 12L;
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == save) {
				if(nickname.getText().length() < 4 || nickname.getText().length() > 16) {
					JOptionPane.showMessageDialog(null, "You have entered an invalid username\nPlease enter a username that's between 5 and 16 characters long", "Username-Error", 0);
				} else if(address.getText().length() < 0) {
					JOptionPane.showMessageDialog(null, "You have entered an invalid address\nPlease enter a valid IP-address or 'localhost'", "IP-Error", 0);
				} else {
					Chat.establishConnection(isServer.isSelected(), nickname.getText(), address.getText());
					hideWindow();
				}
			} else if(e.getSource() == isServer) {
				if(isServer.isSelected()) {
					address.setEditable(false);
					lastAddress = address.getText();
					address.setText("");
				} else {
					address.setEditable(true);
					address.setText(lastAddress);
				}
			}
		}
		
	};
}
