package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import demo.Test;
import scrabbleGame.PlayerAction;
import scrabbleGame.ScrabbleGameLobbyController;

import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.awt.event.ActionEvent;

public class GameLobbyGUI {
	//Scrabble Game lobby realated attributes
	private String playerName;
	private ScrabbleGameLobbyController lobby;
	

	private JFrame frame;
	private JTextArea txtrPlayerList;
	private JButton btnNewGame;
	private JTextArea txtrRoomlist;
	private JButton btnJoinRoom;
	private JButton btnExitGame;

	/**
	 * Launch the application.
	 */
	public void run(String playerName, Test test) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameLobbyGUI window = new GameLobbyGUI(playerName, test);
					window.frame.setVisible(true);
				} catch (Exception e) {
					popException(e, "Failed to initialize Lobby");
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameLobbyGUI(String playerName, Test test) {
		this.lobby = test.lobby;
		this.playerName = playerName;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 696, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle(String.format("Scrabble Game Lobby - loged in as <%s>", playerName));
		
		txtrPlayerList = new JTextArea();
		txtrPlayerList.setEditable(false);
		txtrPlayerList.setLineWrap(true);
		txtrPlayerList.setWrapStyleWord(true);
		txtrPlayerList.setText("playerList");
		txtrPlayerList.setBounds(10, 11, 125, 215);
		frame.getContentPane().add(txtrPlayerList);
		
		JScrollPane jsp = new JScrollPane(txtrPlayerList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setBounds(10, 11, 125, 215);
		frame.getContentPane().add(jsp);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		
		btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					int n = JOptionPane.showConfirmDialog(null,
							"Start a new game? ", "Create Game", 
							JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						createNewGame();
					} else refresh();
					
				} catch (Exception e1) {
					popException(e1, "Create Game Error");
				}
			}
		});
		
		txtrRoomlist = new JTextArea();
		txtrRoomlist.setEditable(false);
		txtrRoomlist.setLineWrap(true);
		txtrRoomlist.setWrapStyleWord(true);
		txtrRoomlist.setText("RoomList");
		txtrRoomlist.setBounds(10, 237, 125, 215);
		frame.getContentPane().add(txtrRoomlist);
		
		JScrollPane jsp2 = new JScrollPane(txtrRoomlist, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp2.setBounds(10, 237, 125, 215);
		frame.getContentPane().add(jsp2);
		
		btnNewGame.setBounds(559, 191, 111, 35);
		frame.getContentPane().add(btnNewGame);
		btnRefresh.setBounds(559, 135, 111, 35);
		frame.getContentPane().add(btnRefresh);
		
		btnJoinRoom = new JButton("Join Room");
		btnJoinRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					String room = JOptionPane.showInputDialog("Please Enter the 4 digit Room ID: ");
					joinGame(Integer.parseInt(room));
				} catch (Exception e1) {
					popException(e1, "Join Room Error");
				}
			}
		});
		btnJoinRoom.setBounds(559, 254, 111, 35);
		frame.getContentPane().add(btnJoinRoom);
		
		btnExitGame = new JButton("Exit Game");
		btnExitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int n = JOptionPane.showConfirmDialog(null,
							"Exit? ", "Exit Game", 
							JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						lobby.performAction(playerName, PlayerAction.LOGOUT_LOBBY);
						frame.dispose();
					} else refresh();
					
				}catch (Exception err){
					popException(err, "Exit Error");
				}
				
			}
		});
		btnExitGame.setBounds(559, 330, 111, 35);
		frame.getContentPane().add(btnExitGame);
	}
	
	private void popException(Exception e, String title) {
		JOptionPane.showMessageDialog(null, e.toString(), title,
				JOptionPane.ERROR_MESSAGE);
	}
	
	
	
	private void createNewGame() throws Exception {
		lobby.performAction(playerName, PlayerAction.CREATE_GAME);
		//generate a new game GUI
		GameGUI scrabbleGame = new GameGUI(playerName, lobby);
		scrabbleGame.run();
	}
	
	private void joinGame(int roomId) throws Exception {
		lobby.performAction(playerName, PlayerAction.JOIN_GAME, roomId);
		//generate a new game GUI
		GameGUI scrabbleGame = new GameGUI(playerName, lobby);
		scrabbleGame.run();
	}
	
	private void exitGame() {
		//TODO exit game
	}
	
	private void checkInvite(){
		HashMap<String, String> inviteList = lobby.getGameLobbyData().getInviteList();
		//if you been invited
		if (inviteList.containsKey(playerName)) {
			String invitor = inviteList.get(playerName);
			try {
				int n = JOptionPane.showConfirmDialog(null,
						String.format("%s has invited you to join his/her game.\nAccept?", invitor), "Invitation ", 
						JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					lobby.performAction(playerName, PlayerAction.ACCEPT_INVITE);
					//generate a new game GUI
					GameGUI scrabbleGame = new GameGUI(playerName, lobby);
					scrabbleGame.run();
				} else {
					lobby.performAction(playerName, PlayerAction.REJECT_INVITE);
				}
			} catch (Exception err) {
				popException(err, "Invitaion Error");
			}
			
			
			
		}
	}
	
	public void refresh() {
		txtrPlayerList.setText(lobby.getGameLobbyData().getPlayerList());
		txtrRoomlist.setText(lobby.getGameLobbyData().getRoomList());
		checkInvite();
	}
	
	

}
