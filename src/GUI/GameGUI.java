package GUI;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import demo.Test;
import exceptions.GameNotCreatedException;
import exceptions.NoGameDataCreatedException;
import scrabbleGame.GamePhase;
import scrabbleGame.PlayerAction;
import scrabbleGame.ScrabbleGameController;
import scrabbleGame.ScrabbleGameLobbyController;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class GameGUI{
	
	//scrabble game specific info
	private String playerName;
	private int rows, cols;
	private String votedWord = null;
	private ScrabbleGameLobbyController lobby;
	private ScrabbleGameController game;
	

	private JFrame frame;
	private JTextField[][] cells;
	private JLabel lblCurrentTurn;
	private JTextArea txtrPhase;
	private JTextArea txtrRank;
	private JScrollPane sbrText;
	private JButton btnPass;
	private JButton btnLogOut;
	private JTextArea txtrGameLog;
	private JButton btnStart;
	private JButton btnInvite;
	

	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameGUI window = new GameGUI(playerName, lobby);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	/**
	 * Create the application.
	 */
	public GameGUI(String playerName, ScrabbleGameLobbyController lobby ) {
		try {
			//initialize attributes
			this.playerName = playerName;
			this.lobby = lobby;
			//TODO: chaneg the link to RMI later
			this.game = this.lobby.getSpecificGame(playerName);
		
			
			this.rows = game.getGameData().getRows();
			this.cols = game.getGameData().getColums();
			initialize();
		} catch (Exception e) {
			popException(e, "Initialize Game Error");
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		frame = new JFrame();
		frame.setTitle(playerName+ "'s GameWindow\n");
		frame.setBounds(100, 100, 909, 620);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBounds(205, 11, 519, 411);
		
		renderGameBoard(rows, cols, panel);
		
		
		
		//TODO: Add button
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(779, 101, 89, 23);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//An arraylist contains following data [numberOfNewWriteDowns, theLastUpdateRow, theLastUpdateCol]
				ArrayList<Integer> newWordData = scanBoard();

				//write down new word
				writeWord(newWordData);		
				
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(20, 20, 0, 0));
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int n = JOptionPane.showConfirmDialog(null,
							"Start now?", "Start Game", 
							JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						game.performAction(playerName, PlayerAction.START_GAME);
					} else refresh();
				} catch (Exception e1) {
					popException(e1, "Start Game Error");
				}
			}
		});
		btnStart.setBounds(779, 29, 89, 23);
		frame.getContentPane().add(btnStart);
		
		btnInvite = new JButton("Invite");
		btnInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String otherPlayer = JOptionPane.showInputDialog(lobby.getFreePlayers());
					if(otherPlayer != null)	invitePlayer(otherPlayer);
				} catch (Exception err) {
					popException(err, "Inivite Error");
				}
				
				
			}
		});
		btnInvite.setBounds(779, 67, 89, 23);
		frame.getContentPane().add(btnInvite);
		frame.getContentPane().add(btnAdd);
		
		//TODO: Pass button
		btnPass = new JButton("Pass");
		btnPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int n = JOptionPane.showConfirmDialog(null,
							"(｡•́︿•̀｡) You will pass this turn.", "Pass Turn", 
							JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						game.performAction(playerName, PlayerAction.PASS_TURN);
					} else refresh();
				} catch (Exception err) {
					popException(err, "Passing Turn Error");
				}
				
			}

			
		});
		btnPass.setBounds(779, 145, 89, 23);
		frame.getContentPane().add(btnPass);
		
		//TODO: Log Out
		btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int n = JOptionPane.showConfirmDialog(null,
							"π_π Really don't want to play few more mins? ", "Log Out", 
							JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						game.performAction(playerName, PlayerAction.LOGOUT);
					} else refresh();
				}catch (Exception err) {
					popException(err, "Log Out Error");
				}
			}
		});
		btnLogOut.setBounds(779, 202, 89, 23);
		frame.getContentPane().add(btnLogOut);
		
		//TODO: Refresh Button 
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(779, 271, 89, 23);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (Exception e1) {
					popException(e1, "Refresh Game Error");
				}
				
			}
		});
		frame.getContentPane().add(btnRefresh);
		
		lblCurrentTurn = new JLabel("CurrentTurn");
		lblCurrentTurn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCurrentTurn.setBounds(30, 11, 134, 23);
		frame.getContentPane().add(lblCurrentTurn);
		
		txtrPhase = new JTextArea();
		txtrPhase.setText("CurrentTurn");
		txtrPhase.setWrapStyleWord(true);
		txtrPhase.setLineWrap(true);
		txtrPhase.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtrPhase.setEditable(false);
		txtrPhase.setBounds(30, 45, 134, 40);
		frame.getContentPane().add(txtrPhase);
		
		txtrRank = new JTextArea();
		txtrRank.setEditable(false);
		txtrRank.setWrapStyleWord(true);
		txtrRank.setText("PlayerRank");
		txtrRank.setBounds(30, 115, 134, 101);
		frame.getContentPane().add(txtrRank);
		
		txtrGameLog = new JTextArea();
		txtrGameLog.setEditable(false);
		txtrGameLog.setLineWrap(true);
		txtrGameLog.setWrapStyleWord(true);
		txtrGameLog.setBounds(30, 443, 778, 110);
		
		frame.getContentPane().add(txtrGameLog);
		
		JScrollPane jsp = new JScrollPane(txtrRank, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setBounds(30, 115, 134, 101);
		frame.getContentPane().add(jsp);
		
		JScrollPane jsp2 = new JScrollPane(txtrGameLog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp2.setBounds(30, 443, 838, 110);
		frame.getContentPane().add(jsp2);
		
	}
	
	/**
	 * @param err
	 */
	private void popException(Exception err, String title) {
		JOptionPane.showMessageDialog(null, err.toString(), title,
				JOptionPane.ERROR_MESSAGE);
	}
	
	
	private void renderGameBoard(int rows, int cols, JPanel panel) {
		cells = new JTextField[rows][cols];
		//TODO change rows back to dynamic??Problem, can't actually do dynamic dividing 
		panel.setLayout(new GridLayout(20, cols, 0 ,0));
		//create a rows*cols size gameboard
		for (int i = 0; i<rows; i++)
			for (int j = 0; j<cols; j++)
				//text is inisized to null
				panel.add(cells[i][j] = new JTextField());
	}
	
	/**
	 * A method to find how many cells has been updated
	 * 
	 * @return ArrayList<Integer> [numberOfNewWriteDowns, theLastUpdateRow,
	 *         theLastUpdateCol]
	 */
	private ArrayList<Integer> scanBoard() {
		// An arraylist contains following data [numberOfNewWriteDowns,
		// theLastUpdateRow, theLastUpdateCol]
		ArrayList<Integer> result = new ArrayList<Integer>();
		boolean  test3, test2;
		int counter = 0;
		int changedRow = -1;
		int changedCol = -1;
		String newCell, oldCell;
		// find newly writed word
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {

				try {
					newCell = game.getGameData().getGameBoard()[i][j];
					oldCell = cells[i][j].getText();
					test2 = (oldCell.isEmpty());
					test3 = oldCell.equals(newCell);
					if (!test2 && !test3) {
						changedCol = j;
						changedRow = i;
						counter++;
					}
				} catch (NoGameDataCreatedException | GameNotCreatedException e1) {
					popException(e1, "Scan Board Error");
				}
			}

		result.add(counter);
		result.add(changedRow);
		result.add(changedCol);

		return result;
	}
	
	
	/**
	 * Trying to write word to the game, but with some conditions applied on it
	 * @param newWordData
	 */
	private void writeWord(ArrayList<Integer> newWordData) {
		int changedWordNm = newWordData.get(0);
		int changedRow = newWordData.get(1);
		int changedCol = newWordData.get(2);
		
		if ( changedWordNm == 1) {
			JOptionPane.showConfirmDialog(null, "( • ̀ω•́ )✧  Want to add this letter?", "Letter confirm",
					JOptionPane.YES_NO_OPTION);
			// action write down a letter sent to the server
			try {
				game.performAction(playerName, PlayerAction.WRITE_DOWN_LETTER
						, changedRow, changedCol, cells[changedRow][changedCol].getText());
			} catch (Exception e1) {
				popException(e1, "Write Down Word Error");
			}
		} else if (changedWordNm < 1) {
			JOptionPane.showMessageDialog(null, "(๑°ㅁ°๑)ᵎᵎᵎ You haven't add any letter!", "Adding letter error!",
					JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "(。-`ω´-) You have added more than one letter!", "Adding letter error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}


	/**
	 * synicize the local board with the game data
	 */
	private void updateLocalBoard() {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				try {
					cells[i][j].setText(game.getGameData().getGameBoard()[i][j]);
				} catch (NoGameDataCreatedException | GameNotCreatedException e1) {
					popException(e1, "Update Board Error");
				}
			}
		}
	}
	
	private void checkVoting() throws Exception {
		boolean isTimeToVote = game.getGameData().isTimeToVote();
		String wordToVote= game.getGameData().getVotedWord();
		
		//if player need to vote
		if (isTimeToVote ) {
			//is the player has voted for this word, prevent duplicate voting 
			if (votedWord == wordToVote) {
				//do Nothing
			} else {
				int n = JOptionPane.showConfirmDialog(null,
						"ヾ(◍'౪`◍)ﾉﾞ Do you agree \"" + game.getGameData().getVotedWord() + "\" is a word?", "Vote",
						JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					PlayerAction action1 = PlayerAction.ACCEPT_WORD;
					game.performAction(playerName, action1);
				} else {
					PlayerAction action2 = PlayerAction.REJECT_WORD;
					game.performAction(playerName, action2);
				}
				//update the player's voting history
				votedWord = wordToVote;
			}
			
		}
	}
	
	private void checkGameEnding() throws 
	NoGameDataCreatedException, GameNotCreatedException {
		String phase = game.getGameData().getCurrentPhase();
		if ( phase == "ENDING") {
			JOptionPane.showMessageDialog(null, "(。-`ω´-) The Game Ends !", "Warning", JOptionPane.INFORMATION_MESSAGE);
			JOptionPane.showMessageDialog(null, game.getGameData().getPlayerRank(), "Ranking",
					JOptionPane.INFORMATION_MESSAGE);
			JOptionPane.showMessageDialog(null, "(´-ι_-｀) Now you will go back to game waiting room !", "Warning",
					JOptionPane.INFORMATION_MESSAGE);
			this.frame.setVisible(false);
		}
	}
	
	private void invitePlayer(String otherPlayer) throws Exception {
		lobby.performAction(playerName, otherPlayer, PlayerAction.INVITE_PLAYER);
	}
	
	public void refresh() throws Exception {
		//format1: Every text field need to be updated here
		//format2: Every flag detection happens here
		updateLocalBoard();
		lblCurrentTurn.setText("Current player: "+ game.getGameData().getCurrentTurn());
		txtrPhase.setText("Current Phase: \n"+ game.getGameData().getCurrentPhase());
		txtrRank.setText(game.getGameData().getPlayerRank());
		txtrGameLog.setText(game.getGameData().getGameLog());
		checkVoting();
		checkGameEnding();
	}
}
