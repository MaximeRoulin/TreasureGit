package ie.tipreels.treasure.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ie.tipreels.treasure.game.info.ColumnPosition;
import ie.tipreels.treasure.game.info.RowPosition;
import ie.tipreels.treasure.game.info.TrapPosition;
import ie.tipreels.treasure.game.info.TreasureGameInfo;
import ie.tipreels.treasure.game.info.TreasureGameInfoType;
import ie.tipreels.treasure.game.info.TreasurePosition;

public class TipScreen extends JFrame {

	//Attributes
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.8);
	private static final int windowHeight = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().height)*0.8);
	private GameBoard board;
	private List<Player> players;
	private ResourceBundle messages;
	private JPanel boardPanel;
	private boolean trueInfo;
	private GameSystem system;
	private boolean infosShown;
	private JComboBox<String> selectTippedPlayer;
	private JComboBox<String> selectInfo;
	private List<TileButton> highlightedTiles;
	private List<TileButton> playerInfoTiles;
	private TipScreen pointer;
	private boolean endTurnButtonEnabled;
	private boolean treasureInArea;
	
	//Constructor
	public TipScreen(GameBoard board, List<Player> players, ResourceBundle messages, boolean trueInfo, GameSystem system) throws HeadlessException {
		super();
//		System.out.println("Constructor new tip screen.");
		this.board = board;
		this.players = players;
		this.messages = messages;
		infosShown = false;
		boardPanel = new BoardPanel(board);
		highlightedTiles = new ArrayList<TileButton>();
		playerInfoTiles = new ArrayList<TileButton>();
		int tmpInt;
		TileButton tmpTB;
		for(Player player : players) {
			for(int[] positions : player.getPositions()) {
				if(positions[0] != -1 && positions[1] != -1) {					
					tmpInt = positions[0] * 15 + positions[1];
					tmpTB = (TileButton) boardPanel.getComponent(tmpInt);
					tmpTB.color(player);
				}
			}
		}
		this.trueInfo = trueInfo;
		this.system = system;
		system.setTipScreen(this);
		pointer = this;
		endTurnButtonEnabled = system.getGamePanel().getEndTurnButton().isEnabled();
//		System.out.println("EndTurnButton is enabled: " + system.getGamePanel().getEndTurnButton().isEnabled());
		if(endTurnButtonEnabled) {
//			System.out.println("The end turn button was enabled, disabling...");
			system.getGamePanel().getEndTurnButton().setEnabled(false);
		}
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				system.disposeOfGivenInfoScreen();
				system.setTipScreen(null);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				system.disposeOfGivenInfoScreen();
				system.setTipScreen(null);
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setResizable(true);
		setTitle(messages.getString("tip_screen"));
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel title = new JLabel(messages.getString("tip_screen"));
		mainPanel.add(title, gbc);
		gbc.gridy++;
		selectTippedPlayer = new JComboBox<String>();
		for(Player player : players) {
			if(!player.getRole().equals(PlayerRole.PIRATE))
				selectTippedPlayer.addItem(player.getName());
		}
		mainPanel.add(selectTippedPlayer, gbc);
		gbc.gridx++;
		selectInfo = new JComboBox<String>();
		selectInfo.addItem(messages.getString("tip_by_row"));
		selectInfo.addItem(messages.getString("tip_by_column"));
		gbc.gridx=0;
		gbc.gridy++;
		mainPanel.add(selectInfo, gbc);
		gbc.gridy++;
		mainPanel.add(boardPanel, gbc);
		gbc.gridy++;
		JButton showInfoButton = new JButton(messages.getString("show_info"));
		showInfoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Show info button clicked.\ninfosShown: " + infosShown);
				if(infosShown) {
					resetHighlightedTiles(true);
					infosShown = false;
					system.disposeOfGivenInfoScreen();
				}
				else {
					int result = JOptionPane.showConfirmDialog(null, messages.getString("confirm_show_info"));
					if(result == JOptionPane.YES_OPTION)
						showInfo();
				}
			}
			
		});		
		mainPanel.add(showInfoButton, gbc);
		this.add(mainPanel);
		setUpForSelect();
		setVisible(true);
		//DEBUG
//		TileButton temp; 
//		for(int i = 0 ; i < 15 ; i++) {
//			temp = (TileButton) boardPanel.getComponent(15 + i);
//			System.out.println(temp.getBackground());
//		}
	}

	//Getters and Setters
	public GameBoard getBoard() {
		return board;
	}

	public void setBoard(GameBoard board) {
		this.board = board;
	}

	public JPanel getBoardPanel() {
		return boardPanel;
	}

	public void setBoardPanel(JPanel boardPanel) {
		this.boardPanel = boardPanel;
	}

	public boolean isTrueInfo() {
		return trueInfo;
	}

	public void setTrueInfo(boolean trueInfo) {
		this.trueInfo = trueInfo;
	}	
	
	
	public JComboBox<String> getSelectTippedPlayer() {
		return selectTippedPlayer;
	}

	public void setSelectTippedPlayer(JComboBox<String> selectTippedPlayer) {
		this.selectTippedPlayer = selectTippedPlayer;
	}

	public JComboBox<String> getSelectInfo() {
		return selectInfo;
	}

	public void setSelectInfo(JComboBox<String> selectInfo) {
		this.selectInfo = selectInfo;
	}

	//Methods
	public void showInfo() {
		infosShown = true;
//		System.out.println("infosShown true");
		TileButton button;
		for(TreasureGameInfo info : players.get(0).getInfos()) {
			switch(info.getType()) {
			case TREASUREPOSITION:
				TreasurePosition treasurePosition = ((TreasurePosition) info);
				int treasureRow = treasurePosition.getRow();
				int treasureColumn = treasurePosition.getColumn();
//				System.out.println("Treasure position is row " + ((TreasurePosition) info).getRow() + " and column " + ((TreasurePosition) info).getColumn()  + ".");
				button = ((TileButton) (boardPanel.getComponent(treasureRow*15+treasureColumn)));
				button.highlight(info.getType());
				highlightedTiles.add(button);
				break;
			case TRAPPOSITION:
				TrapPosition trapPosition = ((TrapPosition) info);
				int trapRow = trapPosition.getRow();
				int trapColumn = trapPosition.getColumn();
//				System.out.println("Treasure position is row " + ((TreasurePosition) info).getRow() + " and column " + ((TreasurePosition) info).getColumn()  + ".");
				button = ((TileButton) (boardPanel.getComponent(trapRow*15+trapColumn)));
				button.highlight(info.getType());
				highlightedTiles.add(button);
				break;
			case ROWPOSITION:
				RowPosition rowInfo = (RowPosition) info;
				for (int i = rowInfo.getfirstRow() ; i <= rowInfo.getLastRow() ; i++) {			
					for(int j = 0 ; j < 15 ; j++) {
						button = (TileButton) boardPanel.getComponent((i * 15) + j);
						Color background = button.getBackground();
						if((background.getRed() == 238 && background.getGreen() == 238 && background.getBlue() == 238) || background.equals(Color.DARK_GRAY) || background.equals(Color.CYAN)) {
							button.highlight(TreasureGameInfoType.ROWPOSITION);
							highlightedTiles.add(button);
						}
					}
				}
				break;
			case COLUMNPOSITION:
				ColumnPosition columnInfo = (ColumnPosition) info;
				for (int i = 0 ; i < 11 ; i++) {			
					for(int j = columnInfo.getfirstColumn() ; j <= columnInfo.getLastColumn() ; j++) {
						button = (TileButton) boardPanel.getComponent((i * 15) + j);
						Color background = button.getBackground();
						if((background.getRed() == 238 && background.getGreen() == 238 && background.getBlue() == 238) || background.equals(Color.DARK_GRAY) || background.equals(Color.CYAN)) {
							button.highlight(TreasureGameInfoType.COLUMNPOSITION);
							highlightedTiles.add(button);
						}
					}
				}
			}
			boolean infoGiven = false;
			for(Player explorer : players) {
				if(!(explorer.getRole().equals(PlayerRole.PIRATE) && !infoGiven)) {
					if(!explorer.getInfos().isEmpty())
						infoGiven = true;
				}
			}
			if(infoGiven) {
				GivenInfoScreen explorerInfoScreen = new GivenInfoScreen(system, players, messages);
				system.setGivenInfoScreen(explorerInfoScreen);
			}
		}
	}
	
	public void showPlayerInfo(Player player) {
		if(player.getInfos().isEmpty())
			JOptionPane.showMessageDialog(null, messages.getString("no_info"), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
		else {
			TileButton button;
			for(TreasureGameInfo info : player.getInfos()) {
				switch(info.getType()) {
				case TREASUREPOSITION:
					TreasurePosition treasurePosition = ((TreasurePosition) info);
					int row = treasurePosition.getRow();
					int column = treasurePosition.getColumn();
					button = ((TileButton) (boardPanel.getComponent(row*15+column)));
					button.highlight(info.getType());
					highlightedTiles.add(button);
					break;
				case TRAPPOSITION:
					
					break;
				case ROWPOSITION:
					RowPosition rowInfo = (RowPosition) info;
					for (int i = rowInfo.getfirstRow() ; i <= rowInfo.getLastRow() ; i++) {			
						for(int j = 0 ; j < 15 ; j++) {
							button = (TileButton) boardPanel.getComponent((i * 15) + j);
							if(!highlightedTiles.contains(button)) {								
								Color background = button.getBackground();
								if((background.getRed() == 238 && background.getGreen() == 238 && background.getBlue() == 238) || background.equals(Color.DARK_GRAY) || background.equals(Color.CYAN)) {
									button.highlight(TreasureGameInfoType.ROWPOSITION);
									playerInfoTiles.add(button);
								}
							}
						}
					}
					break;
				case COLUMNPOSITION:
					ColumnPosition columnInfo = (ColumnPosition) info;
					for (int i = 0 ; i < 11 ; i++) {			
						for(int j = columnInfo.getfirstColumn() ; j <= columnInfo.getLastColumn() ; j++) {
							button = (TileButton) boardPanel.getComponent((i * 15) + j);
							if(!highlightedTiles.contains(button)) {								
								Color background = button.getBackground();
								if((background.getRed() == 238 && background.getGreen() == 238 && background.getBlue() == 238) || background.equals(Color.DARK_GRAY) || background.equals(Color.CYAN)) {
									button.highlight(TreasureGameInfoType.COLUMNPOSITION);
									highlightedTiles.add(button);
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	public void setUpForSelect() {
		TileButton temp;
		for(Component comp : boardPanel.getComponents()) {
			temp = (TileButton) (comp);
			temp.setEnabled(true);
			String[] position = temp.getName().split("_");
			int row = Integer.parseInt(position[1]);
			int column = Integer.parseInt(position[2]);
			temp.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					if(getSelectInfo().getSelectedIndex() == 0) {
						if(row < 2)
							highlightRows(0, 3);
						else {
							if(row > 7)
								highlightRows(7, 10);
							else
								highlightRows(row - 1, row + 2);
						}
					}
					else {
						if(column < 3)
							highlightColumns(0, 5);
						else {
							if(column > 10)
								highlightColumns(9, 14);
							else
								highlightColumns(column - 2, column + 3);
						}
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					resetHighlightedTiles(false);
				}
				
			});
			temp.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					TreasureGameInfo info;
					if(getSelectInfo().getSelectedIndex() == 0) {
						if(row < 2)
							info = new RowPosition(0, 3);
						else {
							if(row > 7)
								info = new RowPosition(7, 10);
							else
								info = new RowPosition(row - 1, row + 2);
						}
						if(trueInfo) {
							RowPosition rowPosition = (RowPosition) info;
							TreasurePosition treasure = ((TreasurePosition) (players.get(0).getInfos().get(0)));
							if(treasure.getRow() < rowPosition.getfirstRow() || treasure.getRow() > rowPosition.getLastRow()) {
								JOptionPane.showMessageDialog(null, messages.getString("treasure_not_in_area"));
								treasureInArea = false;
							}
							else
								treasureInArea = true;
						}
					}
					else {
						if(column < 3)
							info = new ColumnPosition(0, 5);
						else {
							if(column > 10)
								info = new ColumnPosition(9, 14);
							else
								info = new ColumnPosition(column - 2, column + 3);
						}
						if(trueInfo) {
							ColumnPosition columnPosition = (ColumnPosition) info;
							TreasurePosition treasure = ((TreasurePosition) (players.get(0).getInfos().get(0)));
							if(treasure.getColumn() < columnPosition.getfirstColumn() || treasure.getColumn() > columnPosition.getLastColumn()) {
								JOptionPane.showMessageDialog(null, messages.getString("treasure_not_in_area"));
								treasureInArea = false;
							}
							else
								treasureInArea = true;
						}
					}
					if(trueInfo == false || treasureInArea) {						
						system.giveInfoToPlayer(info, selectTippedPlayer.getSelectedIndex() + 1);
						system.allowMove(system.getCurrentPlayer(), true, 0);
						pointer.dispose();
					}
				}
				
			});
		}
	}
	
	public void highlightRows(int startRow, int endRow) {
		TileButton temp;
		for (int i = startRow ; i <= endRow ; i++) {			
			for(int j = 0 ; j < 15 ; j++) {
				temp = (TileButton) boardPanel.getComponent((i * 15) + j);
				Color background = temp.getBackground();
				if((background.getRed() == 238 && background.getGreen() == 238 && background.getBlue() == 238) || background.equals(Color.DARK_GRAY) || background.equals(Color.CYAN)) {
					temp.highlight(TreasureGameInfoType.ROWPOSITION);
					highlightedTiles.add(temp);
				}
			}
		}
	}
	
	public void highlightColumns(int startColumn, int endColumn) {
		TileButton temp;
		for (int i = 0 ; i < 11 ; i++) {			
			for(int j = startColumn ; j <= endColumn ; j++) {
				temp = (TileButton) boardPanel.getComponent((i * 15) + j);
				Color background = temp.getBackground();
				if((background.getRed() == 238 && background.getGreen() == 238 && background.getBlue() == 238) || background.equals(Color.DARK_GRAY) || background.equals(Color.CYAN)) {
					temp.highlight(TreasureGameInfoType.COLUMNPOSITION);
					highlightedTiles.add(temp);
				}
			}
		}
	}
	
	public void resetHighlightedTiles(boolean resetShownInfos) {
		List<TileButton> temp = new ArrayList<TileButton>();
		for(TileButton button : highlightedTiles) {
			if(button.getTile().getContent().equals(ContentType.EMPTY) || !infosShown || resetShownInfos ) {
				button.dehighlight();				
			}
			else {
				temp.add(button);
			}
		}
		highlightedTiles.clear();
		highlightedTiles = temp;
		for(TileButton button : playerInfoTiles) {
			if(button.getTile().getContent().equals(ContentType.EMPTY) || !infosShown || resetShownInfos ) {
				button.dehighlight();				
			}
			else {
				temp.add(button);
			}
		}
	}
}
