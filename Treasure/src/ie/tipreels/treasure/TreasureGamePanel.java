package ie.tipreels.treasure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
//import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
//import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import ie.tipreels.treasure.game.BoardPanel;
import ie.tipreels.treasure.game.CardButton;
import ie.tipreels.treasure.game.ContentType;
import ie.tipreels.treasure.game.GameBoard;
import ie.tipreels.treasure.game.GameLog;
import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.GivenInfoScreen;
import ie.tipreels.treasure.game.HandPanel;
import ie.tipreels.treasure.game.HandPanelParent;
import ie.tipreels.treasure.game.HandScroll;
import ie.tipreels.treasure.game.Player;
import ie.tipreels.treasure.game.PlayerRole;
import ie.tipreels.treasure.game.TerrainType;
import ie.tipreels.treasure.game.Tile;
import ie.tipreels.treasure.game.TileButton;
import ie.tipreels.treasure.game.TileType;
import ie.tipreels.treasure.game.cards.CardAvailability;
import ie.tipreels.treasure.game.info.ColumnPosition;
import ie.tipreels.treasure.game.info.RowPosition;
import ie.tipreels.treasure.game.info.TrapPosition;
import ie.tipreels.treasure.game.info.TreasureGameInfo;
import ie.tipreels.treasure.game.info.TreasureGameInfoType;
import ie.tipreels.treasure.game.info.TreasurePosition;

public class TreasureGamePanel extends JPanel implements HandPanelParent {
	//Attributes
	private static final long serialVersionUID = 1L;
	private GameBoard board;
	private JPanel playersPanel;
	private JPanel logPanel;
	private BoardPanel boardPanel;
	private HandPanel handPanel;
	private GameLog log;
	private List<Player> players;
	private ResourceBundle gamelog;
	private ResourceBundle messages;
	private CardButton selectedCard;
	private static Border currentPlayerBorder = BorderFactory.createLineBorder(Color.red);
	private static Border defaultBorder = BorderFactory.createLineBorder(Color.black);
	private GameSystem system;
	private JButton showInfoButton;
	private ThrowDiceButton throwDiceButton;
	private JButton playCardButton;
	private JButton fightButton;
	private JButton endTurnButton;
	private List<TileButton> highlightedTiles;
	private boolean myInfosShown;
	private List<TileButton> playerInfoTiles;

	//Constructor
	public TreasureGamePanel(Locale currentLocale, List<Player> players) {
		super(new GridBagLayout());
		board = new GameBoard();
		highlightedTiles = new ArrayList<TileButton>();
		playerInfoTiles = new ArrayList<TileButton>();
		myInfosShown = false;
		selectedCard = null;
		this.players = players;
		//System.out.println("Board before call to validate :\n" + board.showBoardForDebug());
		try {
			board.validate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		playersPanel = new JPanel(new GridBagLayout());
		boardPanel = new BoardPanel(board);
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagConstraints playersGbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		playersGbc.gridx = 0;
		playersGbc.gridy = 0;
		playersGbc.insets = new Insets(10, 10, 10, 10);
		JLabel playerLabel;
		messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
		String health = "";
		Color foregroundColor = Color.BLACK;
		for(Player p : players) {
			switch(p.getRole()) {
				case PIRATE:
					health = "";
					foregroundColor = Color.BLACK;
					break;
				case CARTOGRAPHER:
					health = "\n" + p.getHealth() + " " + messages.getString("hp");
					foregroundColor = Color.GREEN;
					break;
				case DOCTOR:
					health = "\n" + p.getHealth() + " " + messages.getString("hp");
					foregroundColor = Color.BLUE;
					break;
				case ENGINEER:
					health = "\n" + p.getHealth() + " " + messages.getString("hp");
					foregroundColor = Color.YELLOW;
					break;
				case SOLDIER:
					health = "\n" + p.getHealth() + " " + messages.getString("hp");
					foregroundColor = Color.RED;
			}
			String cpuString = "";
//			if(p.isCpu())
//				cpuString = " (" + messages.getString("cpu") + ")";
			playerLabel = new JLabel(" " + p.getName() + cpuString + " " + health);
			playerLabel.setForeground(foregroundColor);
			if(p.getRole().equals(PlayerRole.ENGINEER)) {
				playerLabel.setOpaque(true);
				playerLabel.setBackground(Color.DARK_GRAY);
			}
			playerLabel.setPreferredSize(new Dimension(200, 30));
			switch (health) {
				case "":
					playerLabel.setBorder(currentPlayerBorder);
					break;
				default:
					playerLabel.setBorder(defaultBorder);
			}
			playersPanel.add(playerLabel, playersGbc);
			playersGbc.gridy++;
		}
		this.add(playersPanel, gbc);
		gbc.gridx++;
		this.add(boardPanel, gbc);
		
		logPanel = new JPanel();
		gamelog = ResourceBundle.getBundle("GamelogBundle", currentLocale);
		int logWidth;
		if((int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)) < 1530)
			logWidth = 20;
		else
			logWidth = 40;
		log = new GameLog(gamelog.getString("welcome_to_lobby") + "\n", logWidth);
		JScrollPane scroll = new JScrollPane(log);
		log.setEditable(false);
		logPanel.add(scroll);
		gbc.gridx++;
		this.add(logPanel, gbc);
		handPanel = new HandPanel(new GridBagLayout(), currentLocale);
		HandScroll handScroll = new HandScroll(handPanel);
		handPanel.setParent(this);
		handPanel.setScroll(handScroll);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		this.add(handScroll, gbc);
		gbc.gridy++;
		JPanel actionsButtonPanel = new JPanel(new FlowLayout());
		showInfoButton = new JButton(messages.getString("show_info"));
		showInfoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(myInfosShown) {
//					System.out.println("infosShown true");
					myInfosShown = false;
				}
				else {
//					System.out.println("infosShown false");
					Player currentPlayer = players.get(system.getCurrentPlayerIndex());
					String confirmMessage = "";
					if(system.isOutOfTurnPirateControl())
						confirmMessage = messages.getString("out_of_turn_pirate_control");
					else
						confirmMessage = messages.getString("confirm_show_info");
					int result = JOptionPane.showConfirmDialog(null, confirmMessage);
					if(result == JOptionPane.YES_OPTION) {
						showMyInfo(currentPlayer);
						handPanel.update(system.getPlayerHand(system.getCurrentPlayer()), false, system.getCurrentPlayer().getRole().equals(PlayerRole.PIRATE), myInfosShown);
					}
				}
			}
			
		});
		actionsButtonPanel.add(showInfoButton);
		throwDiceButton = new ThrowDiceButton(messages.getString("dice_throw"), system);
		throwDiceButton.setEnabled(false);
		actionsButtonPanel.add(throwDiceButton);
		playCardButton = new JButton(messages.getString("play_card"));
		playCardButton.setEnabled(false);
		actionsButtonPanel.add(playCardButton);
		fightButton = new JButton(messages.getString("fight"));
		fightButton.setEnabled(false);
		actionsButtonPanel.add(fightButton);
		endTurnButton = new JButton(messages.getString("end_turn"));
		endTurnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateDisplayForNextPlayer();
				system.playNext();
				handPanel.repaint();
			}
			
		});
		endTurnButton.setEnabled(false);
		actionsButtonPanel.add(endTurnButton);
		this.add(actionsButtonPanel, gbc);
	}

	public GameBoard getBoard() {
		return board;
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}
	
	public GameLog getLog() {
		return log;
	}
	
	public HandPanel getHandPanel() {
		return handPanel;
	}
	
	public JPanel getPlayersPanel() {
		return playersPanel;
	}
	
	public JButton getPlayCardButton() {
		return playCardButton;
	}
	
	public JButton getEndTurnButton() {
		return endTurnButton;
	}
	
	public GameSystem getSystem() {
		return system;
	}
	
	public void setSystem(GameSystem system) {
		this.system = system;
		throwDiceButton.setSystem(system);
	}
	
	public ThrowDiceButton getThrowDiceButton() {
		return throwDiceButton;
	}
	
	public void setInfosShown(boolean infosShown) {
		this.myInfosShown = infosShown;
	}
	
	public void selectCard(CardButton cardButton) {
//		System.out.println("Selecting card " + cardButton.getCard().getType());
//		System.out.println("Selected cards size before select: " + selectedCards.size());
		if(selectedCard != null)
			selectedCard.doClick();
		selectedCard = cardButton;	
		for(ActionListener a : playCardButton.getActionListeners()) {
			playCardButton.removeActionListener(a);
		}
		playCardButton.addActionListener(new PlayCardButtonActionListener(system, selectedCard, playCardButton, endTurnButton, handPanel));
		playCardButton.setEnabled(true);
	}
	
	public void unselectCard() {
		selectedCard = null;
		for(ActionListener a : playCardButton.getActionListeners()) {
			playCardButton.removeActionListener(a);
		}
		playCardButton.setEnabled(false);
	}
	
	public void propagateReachable(int moveLeft, int y, int x, TerrainType startingType, int[] startingPosition, boolean fullMove, Player playing, int pawn, boolean endTurn) {
		Tile tile = board.getBoard()[y][x];
		TileButton button = ((TileButton) (boardPanel.getComponent(y*15+x)));
		button.clearActionListeners();
		if(!tile.getTaken() || (startingPosition[0] == y && startingPosition[1] == x)) {	
			if((startingType.equals(TerrainType.LAND) || board.getBoard()[y][x].getTerrain().equals(TerrainType.LAND)) && (startingPosition[0] != y || startingPosition[1] != x)) {			
				button.showReachable(true);
				if(tile.getContent().equals(ContentType.EMPTY)) {					
					button.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							throwDiceButton.setEnabled(false);
							if(playing.getRole().equals(PlayerRole.PIRATE))
								system.setPirateHasMoved(true);
							movePlayerPawnToTile(playing, pawn, tile, button, y, x);
							system.setTurnState(CardAvailability.AFTERMOVE);
//							button.color(playing);
//							tile.setTaken(true);
//							tile.setOccupant(playing);
//							if(startingPosition[0] != -1 && startingPosition[1] != -1) {
//								boardPanel.vacateTile(startingPosition[0], startingPosition[1]);
//							}
//							int tab[] = {y, x};
//							playing.getPositions().set(pawn, tab);
//							disableBoard();
							for(ActionListener a : fightButton.getActionListeners()) {
								fightButton.removeActionListener(a);
							}
							fightButton.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									lookForAFight(playing);	
									system.setTurnState(CardAvailability.AFTERFIGHT);
								}							
								
							});
//					System.out.println("New position y = " + y + " & x = " + x + " and last position was [" + startingPosition[0] + "," + startingPosition[1] + "].");
							fightButton.setEnabled(true);
							if(playing.getRole().equals(PlayerRole.PIRATE) || endTurn) 
								endTurnButton.setEnabled(true);
							changeAvailableCards(CardAvailability.AFTERMOVE);
						}		
					});
				}
				else {
					if(tile.getContent().equals(ContentType.TREASURE)) {
						switch(playing.getRole()) {
						case PIRATE:
							button.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {									
									JOptionPane.showMessageDialog(null, gamelog.getString("pirate_click_treasure_tile"));
								}
				
							});
							break;
						default:							
							button.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									//TODO: call moveToTile()
									movePlayerPawnToTile(playing, pawn, tile, button, y, x);
									system.showChestScreen(playing, pawn, ContentType.TREASURE);
								}
								
							});
						}
					}
					else {
						switch(playing.getRole()) {
						case PIRATE:
							button.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {									
									JOptionPane.showMessageDialog(null, gamelog.getString("pirate_click_treasure_tile"));
								}
				
							});
							break;
						default:							
							button.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									//TODO: call moveToTile()
									movePlayerPawnToTile(playing, pawn, tile, button, y, x);
									system.showChestScreen(playing, pawn, ContentType.TRAPPED);
								}
								
							});
						}
					}
				}
			}
			else {
				button.setReachable(true);
			}
//		System.out.println("Tile " + x + "," + y + " is reachable!");
			if(moveLeft > 0) {			
				switch(startingType) {
				case SEA:
					if(x != 0 && !((TileButton) (boardPanel.getComponent((y*15)+x-1))).isReachable()) {
						switch(board.getBoard()[y][x-1].getTerrain()) {
						case SEA:
							propagateReachable(1, y, x-1, startingType, startingPosition, false, playing, pawn, endTurn);
							break;
						case LAND:
							propagateReachable(0, y, x-1, startingType, startingPosition, false, playing, pawn, endTurn);
							
						}
					}
					if(x < 14 && !((TileButton) (boardPanel.getComponent((y*15)+x+1))).isReachable()) {
						switch(board.getBoard()[y][x+1].getTerrain()) {
						case SEA:
							propagateReachable(1, y, x+1, startingType, startingPosition, false, playing, pawn, endTurn);
							break;
						case LAND:
							propagateReachable(0, y, x+1, startingType, startingPosition, false, playing, pawn, endTurn);
						}
					}
					if(y != 0 && !((TileButton) (boardPanel.getComponent((y*15)+x-15))).isReachable()) {
						switch(board.getBoard()[y-1][x].getTerrain()) {
						case SEA:
							propagateReachable(1, y-1, x, startingType, startingPosition, false, playing, pawn, endTurn);
							break;
						case LAND:
							propagateReachable(0, y-1, x, startingType, startingPosition, false, playing, pawn, endTurn);
							
						}
					}
					if(y < 10 && !((TileButton) (boardPanel.getComponent((y*15)+x+15))).isReachable()) {
						switch(board.getBoard()[y+1][x].getTerrain()) {
						case SEA:
							propagateReachable(1, y+1, x, startingType, startingPosition, false, playing, pawn, endTurn);
							break;
						case LAND:
							propagateReachable(0, y+1, x, startingType, startingPosition, false, playing, pawn, endTurn);
						}
					}
					break;
				case LAND:
					if(x != 0) {
						switch(board.getBoard()[y][x-1].getTerrain()) {
						case SEA:
							if(fullMove)
								propagateReachable(0, y, x-1, startingType, startingPosition, false, playing, pawn, endTurn);
							break;
						case LAND:
							propagateReachable(moveLeft-1, y, x-1, startingType, startingPosition, false, playing, pawn, endTurn);
						}
					}
					if(x < 14) {
						switch(board.getBoard()[y][x+1].getTerrain()) {
						case SEA:
							if(fullMove)
								propagateReachable(0, y, x+1, startingType, startingPosition, false, playing, pawn, endTurn);
							break;
						case LAND:
							propagateReachable(moveLeft-1, y, x+1, startingType, startingPosition, false, playing, pawn, endTurn);
						}
					}
					if(y != 0) {
						switch(board.getBoard()[y-1][x].getTerrain()) {
						case SEA:
							if(fullMove)
								propagateReachable(0, y-1, x, startingType, startingPosition, false, playing, pawn, endTurn);
							break;
						case LAND:
							propagateReachable(moveLeft-1, y-1, x, startingType, startingPosition, false, playing, pawn, endTurn);
							
						}
					}
					if(y < 10) {
						switch(board.getBoard()[y+1][x].getTerrain()) {
						case SEA:
							if(fullMove)
								propagateReachable(0, y+1, x, startingType, startingPosition, false, playing, pawn, endTurn);
							break;
						case LAND:
							propagateReachable(moveLeft-1, y+1, x, startingType, startingPosition, false, playing, pawn, endTurn);
						}
					}
				}
			}
		}
	}

	protected void movePlayerPawnToTile(Player playing, int pawn, Tile tile, TileButton button, int row, int column) {
//		System.out.println("Coloring button with player " + playing + " of type " + playing.getType());
		button.color(playing);
		tile.setTaken(true);
		tile.setOccupant(playing);
		if(playing.getPositions().get(pawn)[0] != -1 && playing.getPositions().get(pawn)[1] != -1) {
			boardPanel.vacateTile(playing.getPositions().get(pawn)[0], playing.getPositions().get(pawn)[1]);
		}
		int tab[] = {row, column};
		playing.getPositions().set(pawn, tab);
//		System.out.println("New position of player " + playing.getName() +" pawn number " + pawn + " is {" + playing.getPositions().get(pawn)[0] + "," + playing.getPositions().get(pawn)[1] + "}\nExpected: {" + y + "," + x + "}");
		disableBoard();	
	}

	public void disableBoard() {
		TileButton tileButton;
		for(int i = 0 ; i < boardPanel.getComponentCount() ; i++) {
			tileButton = (TileButton) boardPanel.getComponent(i);
			ActionListener listeners[] = tileButton.getActionListeners();
			for(ActionListener listener : listeners) {				
				tileButton.removeActionListener(listener);
			}
			tileButton.showReachable(false);
		}
		revalidate();
	}
	
	public void chooseTreasureTile() {
		TileButton tileButton;
		for(int i = 0 ; i < boardPanel.getComponentCount() ; i++) {
			tileButton = (TileButton) boardPanel.getComponent(i);
			ActionListener listeners[] = tileButton.getActionListeners();
			for(ActionListener listener : listeners) {				
				tileButton.removeActionListener(listener);
			}
			String[] coordinates = tileButton.getName().split("_");
			int x = Integer.parseInt(coordinates[1]);
			int y = Integer.parseInt(coordinates[2]);
			if(board.getBoard()[x][y].getTerrain().equals(TerrainType.LAND)) {
				tileButton.setEnabled(true);
				tileButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						board.getBoard()[x][y].setContent(ContentType.TREASURE);
//						System.out.println("The coordinates are x = " + x + " and y = " + y + ".");
						disableBoard();
						log.appendWithLineBreak(gamelog.getString("buried"));
//						System.out.println("Treasure buried at tile row " + x + " and column " + y + ".");
						players.get(0).addInfo(new TreasurePosition(x, y));
						endTurnButton.setEnabled(true);
					}
					
				});
			}
		}
		revalidate();
	}

	public void updateDisplayForNextPlayer() {
		disableBoard();
		int playerIndex = system.getCurrentPlayerIndex();
		JLabel playerLabel = (JLabel) playersPanel.getComponent(playerIndex);
		playerLabel.setBorder(defaultBorder);
		if(playerIndex < players.size()  - 1) {
			JLabel nextPlayerLabel = (JLabel) playersPanel.getComponent(playerIndex +1);
			nextPlayerLabel.setBorder(currentPlayerBorder);
		}
		else {
			JLabel nextPlayerLabel = (JLabel) playersPanel.getComponent(0);
			nextPlayerLabel.setBorder(currentPlayerBorder);
		}
		//TODO: Check if current player can move
		endTurnButton.setEnabled(false);
		playersPanel.revalidate();
//		System.out.println(system.getNextHand());
		handPanel.update(system.getNextHand(), false, system.getNextPlayer().getRole().equals(PlayerRole.PIRATE), false);
	}
	
	public void updateDisplayAfterInjury(int injuredPlayerIndex, boolean endTurn) {
		JLabel playerLabel = (JLabel) playersPanel.getComponent(injuredPlayerIndex);
		Player p = players.get(injuredPlayerIndex);
		playerLabel.setText((" " + p.getName() + " " + p.getHealth() + " " + messages.getString("hp")));
		if(endTurn)
			endTurnButton.setEnabled(true);
		playersPanel.revalidate();
	}
	
	public void killPlayer(Player deadman) {
		boardPanel.vacateTile(deadman.getPositions().get(0)[0], deadman.getPositions().get(0)[1]);
		deadman.setHealth(0);
		players.remove(deadman);
		rebuildPlayerPanel();
	}
	
	private void rebuildPlayerPanel() {
		GridBagConstraints playersGbc = new GridBagConstraints();
		for(Component c : playersPanel.getComponents()) {
			playersPanel.remove(c);
		}
		playersGbc.gridx = 0;
		playersGbc.gridy = 0;
		playersGbc.insets = new Insets(10, 10, 10, 10);
		JLabel playerLabel;
		String health = "";
		Color foregroundColor = Color.BLACK;
		for(Player p : players) {
			switch(p.getRole()) {
				case PIRATE:
					health = "";
					foregroundColor = Color.BLACK;
					break;
				case CARTOGRAPHER:
					health = "\n" + p.getHealth() + " " + messages.getString("hp");
					foregroundColor = Color.GREEN;
					break;
				case DOCTOR:
					health = "\n" + p.getHealth() + " " + messages.getString("hp");
					foregroundColor = Color.BLUE;
					break;
				case ENGINEER:
					health = "\n" + p.getHealth() + " " + messages.getString("hp");
					foregroundColor = Color.YELLOW;
					break;
				case SOLDIER:
					health = "\n" + p.getHealth() + " " + messages.getString("hp");
					foregroundColor = Color.RED;
			}
			playerLabel = new JLabel(" " + p.getName() + " " + health);
			playerLabel.setForeground(foregroundColor);
			if(p.getRole().equals(PlayerRole.ENGINEER)) {
				playerLabel.setOpaque(true);
				playerLabel.setBackground(Color.DARK_GRAY);
			}
			playerLabel.setPreferredSize(new Dimension(200, 30));
			switch (health) {
				case "":
					playerLabel.setBorder(currentPlayerBorder);
					break;
				default:
					playerLabel.setBorder(defaultBorder);
			}
			playersPanel.add(playerLabel, playersGbc);
			playersGbc.gridy++;
		}
	}
	
	public void disableButtons(boolean endTurn) {
		for(ActionListener a : fightButton.getActionListeners()) {
			fightButton.removeActionListener(a);
		}
		for(ActionListener a : playCardButton.getActionListeners()) {
			playCardButton.removeActionListener(a);
		}
		throwDiceButton.setEnabled(false);
		fightButton.setEnabled(false);
		playCardButton.setEnabled(false);
		if(endTurn)
			endTurnButton.setEnabled(true);
		else
			endTurnButton.setEnabled(false);
	}
	
	public void resetButtonsAfterResume() {
		//TODO: reset buttons based on system.getTurnState
		switch(system.getTurnState()) {
		case BEFOREMOVE:
			system.allowMove(system.getCurrentPlayer(), true);
			break;
		case AFTERMOVE:
			for(ActionListener a : fightButton.getActionListeners()) {
				fightButton.removeActionListener(a);
			}
			fightButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					lookForAFight(system.getCurrentPlayer());
					
				}
			
			});
			fightButton.setEnabled(true);
			endTurnButton.setEnabled(true);
			changeAvailableCards(CardAvailability.AFTERMOVE);
			break;
		case AFTERFIGHT:
			endTurnButton.setEnabled(true);
			break;
		default:
			
		}
	}
	
	public void lookForAFight(Player hostile) {
//		audioSystem.changeMood(Mood.BATTLE);
		disableBoard();
		if(hostile.isAlone()) {
			int range = hostile.useRange();
			int row = hostile.getPositions().get(0)[0];
			int column = hostile.getPositions().get(0)[1];
			for(int i = -range ; i <= range ; i++) {
				//TODO : if tile position + i, position holds enemy, or position, position + i holds enemy
				Tile differentRowTile = null;
				if(row + i >= 0 && row + i <= 10)
					differentRowTile = board.getBoard()[row + i][column];
				Tile differentColumnTile = null;
				if(column + i >= 0 && column + i <= 14)
					differentColumnTile = board.getBoard()[row][column + i];
				if(i != 0 && differentRowTile != null && differentRowTile.getTaken() && !differentRowTile.getOccupant().getRole().equals(hostile.getRole())) {
					TileButton tileButton = (TileButton) boardPanel.getComponent((row + i) * 15 + column);
					ActionListener listeners[] = tileButton.getActionListeners();
					for(ActionListener listener : listeners) {				
						tileButton.removeActionListener(listener);
					}
					final Tile rowClickedTile = differentRowTile;
					tileButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							system.firstSetUpFight(hostile, rowClickedTile.getOccupant(), true);
						}
						
					});
					tileButton.showReachable(true);				
				}
				if(i != 0 && differentColumnTile != null && differentColumnTile.getTaken() && !differentColumnTile.getOccupant().getRole().equals(hostile.getRole())) {
					TileButton tileButton = (TileButton) boardPanel.getComponent((row) * 15 + column + i);
					ActionListener listeners[] = tileButton.getActionListeners();
					for(ActionListener listener : listeners) {				
						tileButton.removeActionListener(listener);
					}
					final Tile columnClickedTile = differentColumnTile;
					tileButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							system.firstSetUpFight(hostile, columnClickedTile.getOccupant(), true);
						}
						
					});
					tileButton.showReachable(true);
				}
			}
		}
		else {
			//TODO: Manage reinforced hostile
		}
	}
	
	public void showMyInfo(Player player) {
		if(player.getInfos().isEmpty() && !player.getRole().equals(PlayerRole.PIRATE))
			JOptionPane.showMessageDialog(null, messages.getString("no_info"), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
		else {
			myInfosShown = true;
			TileButton button;
//			System.out.println("infosShown true");
			for(TreasureGameInfo info : player.getInfos()) {
				switch(info.getType()) {
				case TREASUREPOSITION:
					TreasurePosition treasurePosition = ((TreasurePosition) info);
					int treasureRow = treasurePosition.getRow();
					int treasureColumn = treasurePosition.getColumn();
//					System.out.println("Treasure position is row " + ((TreasurePosition) info).getRow() + " and column " + ((TreasurePosition) info).getColumn()  + ".");
					button = ((TileButton) (boardPanel.getComponent(treasureRow*15+treasureColumn)));
					button.highlight(info.getType());
					highlightedTiles.add(button);
					break;
				case TRAPPOSITION:
					TrapPosition trapPosition = ((TrapPosition) info);
					int trapRow = trapPosition.getRow();
					int trapColumn = trapPosition.getColumn();
//					System.out.println("Treasure position is row " + ((TreasurePosition) info).getRow() + " and column " + ((TreasurePosition) info).getColumn()  + ".");
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
			}
			if(player.getRole().equals(PlayerRole.PIRATE)) {
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
			ResetHighlightedTilesActionListener resetActionListener = new ResetHighlightedTilesActionListener(this, showInfoButton, endTurnButton); 
			showInfoButton.addActionListener(resetActionListener);
			endTurnButton.addActionListener(resetActionListener);
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
	
	public void resetHighlightedTilesAndInfoCards() {
		for(TileButton button : highlightedTiles) {
			button.dehighlight();
		}
		highlightedTiles.clear();
		resetPlayerInfoTiles();
		system.disposeOfGivenInfoScreen();
		if(system.getCurrentPlayer().getRole().equals(PlayerRole.PIRATE)) {
			handPanel.update(system.getPlayerHand(system.getCurrentPlayer()), false, true, false);
		}
//		System.out.println("ResetHighlightedTiles called.");
//		if(highlightedTiles.isEmpty())
//			System.out.println("It reset the list successfully");
//		else
//			System.out.println("There was an issue cleaning the highlighted tiles list.");
	}

	public void resetPlayerInfoTiles() {
		for(TileButton button : playerInfoTiles) {
			button.dehighlight();
		}
		playerInfoTiles.clear();
	}
	
	public void changeAvailableCards(CardAvailability availability) {
//		System.out.println("Calling change available cards with availability: " + availability);
		if(selectedCard != null) {
			selectedCard.resetCardButtonBorder();
			playCardButton.setEnabled(false);
			selectedCard = null;
		}
		CardButton button;
		boolean available = false;
		for(Component comp : handPanel.getComponents()) {
			button = (CardButton) comp;
			switch(button.getCard().getAvailability()) {
			case ALWAYS:
				available = true;
				break;
			case BEFOREMOVE:
				if(availability.equals(CardAvailability.BEFOREMOVE))
					available = true;
				else
					available = false;
				break;
			case AFTERMOVE:
				if(availability.equals(CardAvailability.AFTERMOVE))
					available = true;
				else
					available = false;
				break;
			case AFTERFIGHT:
				if(availability.equals(CardAvailability.AFTERFIGHT))
					available = true;
				else
					available = false;
				break;
			case ENDTURN:
				if(availability.equals(CardAvailability.ENDTURN))
					available = true;
				else
					available = false;
			}
			button.setEnabled(available);
		}
	}
	
	public void forceInfoCard() {
		CardButton button;
		boolean available = false;
		for(Component comp : handPanel.getComponents()) {
			button = (CardButton) comp;
			switch(button.getCard().getType()) {
			case INFO:
				button.setEnabled(true);
				break;
			default:
				available = false;
			}
			button.setEnabled(available);
		}
	}
	
	public void dinghy() {
		int startingRow= 0 ;
		int startingColumn = 0;
		int pawn = 0;
		Player player = players.get(system.getCurrentPlayerIndex());
		if(!player.isAlone()) {
			system.pickPawnForMove(player, true);
		}
		else {
			startingRow = player.getPositions().get(0)[0];
//			System.out.println("starting row is: " + startingRow);
			startingColumn = player.getPositions().get(0)[1];
//			System.out.println("starting column is: " + startingColumn);
			pawn = 0;
		}
		setBoardPanelForDinghy(player, pawn, startingRow, startingColumn);
	}

	private void setBoardPanelForDinghy(Player player, int pawn, int startingRow, int startingColumn) {
		log.appendWithLineBreak(player.getName() + " " + gamelog.getString("has_played_dinghy_card"));
		for(Component comp : boardPanel.getComponents()) {
			TileButton button = (TileButton) comp;
			if(button.getTile().getType().equals(TileType.COAST)) {
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						throwDiceButton.setEnabled(false);
						if(player.getRole().equals(PlayerRole.PIRATE))
							system.setPirateHasMoved(true);
						button.color(player);
						Tile tile = button.getTile();
						tile.setTaken(true);
						tile.setOccupant(player);
						if(startingRow != -1 && startingColumn != -1) {
							boardPanel.vacateTile(startingRow, startingColumn);
						}
//						System.out.println("Before change position is: " + player.getPositions().get(pawn)[0] + "," + player.getPositions().get(pawn)[1]);
						String[] temp = button.getName().split("_");
						player.getPositions().get(pawn)[0] = Integer.parseInt(temp[1]);
						player.getPositions().get(pawn)[1] = Integer.parseInt(temp[2]);
//						System.out.println("After change position is: " + player.getPositions().get(pawn)[0] + "," + player.getPositions().get(pawn)[1]);
						disableBoard();
						for(ActionListener a : fightButton.getActionListeners()) {
							fightButton.removeActionListener(a);
						}
						fightButton.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								lookForAFight(player);
								
							}
						
						});
						fightButton.setEnabled(true);
						endTurnButton.setEnabled(true);
						changeAvailableCards(CardAvailability.AFTERMOVE);
					}
					
				});
				button.setEnabled(true);	
			}
			else
				button.setEnabled(false);
		}
	}

	public void setUpRelocatePlayerPawn(int playerIndex, int pawn, TileType tileType) {
//		System.out.println("Call to GamePanel relocatePlayerPawn with\nplayerIndex: " + playerIndex + "\npawn: " + pawn + "\ntile type: " + tileType);
		Player player = players.get(playerIndex);
		boolean lastPawn = false;
		if(players.size() == (playerIndex + 1) && (player.isAlone() || pawn == 1))
			lastPawn = true;
//		System.out.println("lastPawn: " + lastPawn);
		if(pawn == 0) {
			if(player.isAlone()) {
				log.appendWithLineBreak(gamelog.getString("relocate_single_pawn_1") + " " + player.getName() + gamelog.getString("relocate_single_pawn_2"));
			}
			else {
				log.appendWithLineBreak(gamelog.getString("relocate_first_pawn_1") + " " + player.getName() + gamelog.getString("relocate_first_pawn_2"));
			}
		}
		else {
			log.appendWithLineBreak(gamelog.getString("relocate_last_pawn_1") + " " + player.getName() + gamelog.getString("relocate_last_pawn_2"));
		}
		TileButton button;
		int i = 0;
		int row;
		int column;
		for(Component comp : boardPanel.getComponents()) {
			button = (TileButton) comp;
//			System.out.println("Is this tile type plains? " + tileType.equals(button.getTile().getType()));
			if(!button.getTile().getTaken() && tileType.equals(button.getTile().getType())) {
				row = i/15;
				column = i%15;
				button.addActionListener(new RelocatePawnActionListener(this, player, pawn, row, column, tileType, board, button, lastPawn));
				button.showReachable(true);
			}
			i++;
		}
	}
	
	public int findPlayerIndex(PlayerRole playerType) {
		return system.findPlayerIndex(playerType);
	}
	
	public int playerIndexAfter(Player playerBefore) {
		int result = -1;
		int index = system.findPlayerIndex(playerBefore.getRole());
		if(index != (players.size() - 1)) {			
			result = index + 1;
		}
		return result;
	}

	public void appendLogWithString(String string) {
		log.appendWithLineBreak(gamelog.getString(string));
	}

	public void endTurn() {
		changeAvailableCards(CardAvailability.ENDTURN);
		disableBoard();
		disableButtons(true);
	}
	
	public void selectPawnNoise() {
		disableBoard();
		throwDiceButton.setEnabled(false);
		appendLogWithString("has_played_noise_card");
		for(Player player : players) {
			if(!player.getRole().equals(PlayerRole.PIRATE)) {
				for(int[] pawnLocation : player.getPositions()) {
					if(pawnLocation[0] != -1 && pawnLocation[1] != -1) {	
						TileButton button = ((TileButton) (boardPanel.getComponent(pawnLocation[0] * 15 + pawnLocation[1])));
						button.clearActionListeners();
						button.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								int pawn = 1;
								if(player.getPositions().get(0).equals(pawnLocation))
									pawn = 0;
								setUpRelocatePawnNoise(player, pawn);
							}
							
						});
						button.showReachable(true);						
					}
				}
			}	
		}
	}
	
	public void setUpRelocatePawnNoise(Player player, int pawn) {
		disableBoard();
		int row = player.getPositions().get(pawn)[0];
		int column = player.getPositions().get(pawn)[1];
		if(row != -1 && column != -1) {			
			if(row != 0) {
				TileButton button = ((TileButton) (boardPanel.getComponent((row - 1) * 15 + column)));
				if(!button.getTile().getTaken()) {					
					button.clearActionListeners();
					button.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							movePlayerPawnToTile(player, pawn, board.getBoard()[row - 1][column], button, row - 1, column);
							String position_1 = " [" + row + "," + column + "] ";
							String position_2 = " [" + (row - 1) + "," + column + "].";
							log.appendWithLineBreak(gamelog.getString("has_selected_pawn_noise_1") + " " + player.getName() + " " + gamelog.getString("has_selected_pawn_noise_2") + " " + position_1 + " " + gamelog.getString("has_selected_pawn_noise_3") + position_2);
							system.allowMove(system.getCurrentPlayer(), true);
						}
						
					});
					button.showReachable(true);
				}
			}
			if(row != 10) {
				TileButton button = ((TileButton) (boardPanel.getComponent((row + 1) * 15 + column)));
				if(!button.getTile().getTaken()) {								
					button.clearActionListeners();
					button.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							movePlayerPawnToTile(player, pawn, board.getBoard()[row + 1][column], button, row + 1, column);
							String position_1 = " [" + row + "," + column + "] ";
							String position_2 = " [" + (row + 1) + "," + column + "].";
							log.appendWithLineBreak(gamelog.getString("has_selected_pawn_noise_1") + " " + player.getName() + " " + gamelog.getString("has_selected_pawn_noise_2") + " " + position_1 + " " + gamelog.getString("has_selected_pawn_noise_3") + position_2);
							system.allowMove(system.getCurrentPlayer(), true);
						}
						
					});
					button.showReachable(true);
				}
			}
			if(column != 0) {
				TileButton button = ((TileButton) (boardPanel.getComponent(row * 15 + column - 1)));
				if(!button.getTile().getTaken()) {								
					button.clearActionListeners();
					button.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							movePlayerPawnToTile(player, pawn, board.getBoard()[row][column - 1], button, row, column - 1);
							String position_1 = " [" + row + "," + column + "] ";
							String position_2 = " [" + row + "," + (column - 1) + "].";
							log.appendWithLineBreak(gamelog.getString("has_selected_pawn_noise_1") + " " + player.getName() + " " + gamelog.getString("has_selected_pawn_noise_2") + " " + position_1 + " " + gamelog.getString("has_selected_pawn_noise_3") + position_2);
							system.allowMove(system.getCurrentPlayer(), true);
						}
						
					});
					button.showReachable(true);
				}
			}
			if(column != 14) {
				TileButton button = ((TileButton) (boardPanel.getComponent(row * 15 + column + 1)));
				button.clearActionListeners();
				if(!button.getTile().getTaken()) {								
					button.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							movePlayerPawnToTile(player, pawn, board.getBoard()[row][column + 1], button, row, column + 1);
							String position_1 = " [" + row + "," + column + "] ";
							String position_2 = " [" + row + "," + (column + 1) + "].";
							log.appendWithLineBreak(gamelog.getString("has_selected_pawn_noise_1") + " " + player.getName() + " " + gamelog.getString("has_selected_pawn_noise_2") + " " + position_1 + " " + gamelog.getString("has_selected_pawn_noise_3") + position_2);
							system.allowMove(system.getCurrentPlayer(), true);
						}
						
					});
					button.showReachable(true);
				}
			}
		}
	}
	
	public void selectFirstPawnPlot() {
		disableBoard();
		throwDiceButton.setEnabled(false);
		appendLogWithString("has_played_plot_card");
		for(Player player : players) {
			if(!player.getRole().equals(PlayerRole.PIRATE)) {
				for(int[] pawnLocation : player.getPositions()) {
					if(pawnLocation[0] != -1 && pawnLocation[1] != -1) {						
						TileButton button = ((TileButton) (boardPanel.getComponent(pawnLocation[0] * 15 + pawnLocation[1])));
						button.clearActionListeners();
						button.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								int pawn = 1;
								if(player.getPositions().get(0).equals(pawnLocation))
									pawn = 0;
								selectSecondPawnPlot(player, pawn);
							}
							
						});
						button.showReachable(true);
					}
				}
			}	
		}
	}
	
	public void selectSecondPawnPlot(Player firstExplorer, int firstPawn) {
		disableBoard();
		throwDiceButton.setEnabled(false);
		for(Player secondExplorer : players) {
			if((!secondExplorer.getRole().equals(PlayerRole.PIRATE)) && !secondExplorer.equals(firstExplorer)) {
				for(int[] pawnLocation : secondExplorer.getPositions()) {
					if(pawnLocation[0] != -1 && pawnLocation[1] != -1) {
						TileButton button = ((TileButton) (boardPanel.getComponent(pawnLocation[0] * 15 + pawnLocation[1])));
						button.clearActionListeners();
						button.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								int secondPawn = 1;
								if(secondExplorer.getPositions().get(0).equals(pawnLocation))
									secondPawn = 0;
								setUpFightPlot(firstExplorer, firstPawn, secondExplorer, secondPawn);
							}
							
						});
						button.showReachable(true);						
					}
				}
			}	
		}
	}
	
	public void setUpFightPlot(Player firstExplorer, int firstPawn, Player secondExplorer, int secondPawn) {
		log.appendWithLineBreak(gamelog.getString("plot_selected_fighters_1") + " " + firstExplorer.getName() + " " + gamelog.getString("plot_selected_fighters_2") + " " + secondExplorer.getName() + ".");
		system.firstSetUpFight(firstExplorer, secondExplorer, false);
	}
	
	public void selectPawnPoison() {
		disableBoard();
		throwDiceButton.setEnabled(false);
		appendLogWithString("has_played_poison_card");
		for(Player player : players) {
			if(!player.getRole().equals(PlayerRole.PIRATE)) {
				for(int[] pawnLocation : player.getPositions()) {
					if(pawnLocation[0] != -1 && pawnLocation[1] != -1) {						
						TileButton button = ((TileButton) (boardPanel.getComponent(pawnLocation[0] * 15 + pawnLocation[1])));
						button.clearActionListeners();
						button.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								int pawn = 1;
								if(player.getPositions().get(0).equals(pawnLocation))
									pawn = 0;
								log.appendWithLineBreak(gamelog.getString("has_selected_injured_poison") + " " + player.getName() + ".");
								system.injurePlayer(player, pawn, false);
								system.allowMove(system.getCurrentPlayer(), true);
							}
							
						});
						button.showReachable(true);
					}
				}
			}	
		}
	}
	
	public void selectPawnSpy(Player selector) {
		disableBoard();
		throwDiceButton.setEnabled(false);
		appendLogWithString("has_played_spy_card");
		int firstPawn = 0;
		if(!selector.isAlone()) {
			firstPickPawnSpy(selector);
		}
		else {
			lastPickPawnSpy(selector, firstPawn);
		}
	}

	public void firstPickPawnSpy(Player player) {
		log.appendWithLineBreak(player.getName() + " " + gamelog.getString("is_reinforced"));
		for(int[] pawnLocation : player.getPositions()) {
			if(pawnLocation[0] != -1 && pawnLocation[1] != -1) {				
				TileButton button = ((TileButton) (boardPanel.getComponent(pawnLocation[0] * 15 + pawnLocation[1])));
				button.clearActionListeners();
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						int firstPawn = 1;
						if(player.getPositions().get(0).equals(pawnLocation))
							firstPawn = 0;
						lastPickPawnSpy(player, firstPawn);
						
					}
					
				});
				button.showReachable(true);
			}
		}
	}

	public void lastPickPawnSpy(Player selector, int firstPawn) {
		for(Player player : players) {
			if(!player.equals(selector)) {
				for(int[] pawnLocation : player.getPositions()) {
					if(pawnLocation[0] != -1 && pawnLocation[1] != -1) {						
						TileButton button = ((TileButton) (boardPanel.getComponent(pawnLocation[0] * 15 + pawnLocation[1])));
						button.clearActionListeners();
						button.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								int lastPawn = 1;
								if(player.getPositions().get(0).equals(pawnLocation))
									lastPawn = 0;
								log.appendWithLineBreak(selector.getName() + " " + gamelog.getString("has_selected_swapped_spy") + " " + player.getName() + ".");
								swapPawns(selector, firstPawn, player, lastPawn);
							}
							
						});
						button.showReachable(true);
					}
				}
			}	
		}

	}
	
	public void swapPawns(Player selector, int firstPawn, Player swapped, int lastPawn) {
		int[] selectorPosition = selector.getPositions().get(firstPawn);
		int[] swappedPosition = swapped.getPositions().get(lastPawn);
		boardPanel.clearPawnFromBoard(swapped, lastPawn);
		if(selectorPosition[0] != -1 && selectorPosition[1] != -1) {
			boardPanel.clearPawnFromBoard(selector, firstPawn);			
			movePlayerPawnToTile(swapped, firstPawn, board.getBoard()[selectorPosition[0]][selectorPosition[1]], (TileButton) boardPanel.getComponent(selectorPosition[0] * 15 + selectorPosition[1]), selectorPosition[0], selectorPosition[1]);
		}
		else {
			int[] outOfBoard = {-1, -1};
			swapped.getPositions().set(lastPawn, outOfBoard);
		}
		movePlayerPawnToTile(selector, lastPawn, board.getBoard()[swappedPosition[0]][swappedPosition[1]], (TileButton) boardPanel.getComponent(swappedPosition[0] * 15 + swappedPosition[1]), swappedPosition[0], swappedPosition[1]);
		endTurn();
	}
	
	public void reinforcePlayer(Player reinforced) {
		if(reinforced.isAlone()) {
			int[] tab = {-1,-1};
			reinforced.getPositions().add(tab);
			log.appendWithLineBreak(reinforced.getName() + " " + gamelog.getString("has_played_reinforcements"));
			system.allowMove(reinforced, true, 1);
		}
		else {
			system.showErrorMessage(messages.getString("player_already_reinforced"));
		}
	}
	
	public void pickPawn(Player player, boolean endTurn) {
		log.appendWithLineBreak(player.getName() + " " + gamelog.getString("is_reinforced"));
		for(int[] pawnLocation : player.getPositions()) {
			if(pawnLocation[0] != -1 && pawnLocation[1] != -1) {				
				TileButton button = ((TileButton) (boardPanel.getComponent(pawnLocation[0] * 15 + pawnLocation[1])));
				button.clearActionListeners();
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						int pawn = 1;
						if(player.getPositions().get(0).equals(pawnLocation))
							pawn = 0;
						system.setPickedPawn(pawn);
						throwDiceButton.setPawn(pawn);
						disableBoard();
						system.allowMove(player, endTurn, system.getPickedPawn());
					}
					
				});
				button.showReachable(true);
			}
		}
	}
	
	public void placeTrap() {
		TileButton tileButton;
		for(int i = 0 ; i < boardPanel.getComponentCount() ; i++) {
			tileButton = (TileButton) boardPanel.getComponent(i);
			ActionListener listeners[] = tileButton.getActionListeners();
			for(ActionListener listener : listeners) {				
				tileButton.removeActionListener(listener);
			}
			String[] coordinates = tileButton.getName().split("_");
			int x = Integer.parseInt(coordinates[1]);
			int y = Integer.parseInt(coordinates[2]);
			Tile tile = board.getBoard()[x][y];
			if(tile.getTerrain().equals(TerrainType.LAND) && !tile.getTaken()) {
				tileButton.setEnabled(true);
				tileButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if(tile.getContent().equals(ContentType.EMPTY)) {
							board.getBoard()[x][y].setContent(ContentType.TRAPPED);
//						System.out.println("The coordinates are x = " + x + " and y = " + y + ".");
							disableBoard();
//						System.out.println("Treasure buried at tile row " + x + " and column " + y + ".");
							players.get(0).addInfo(new TrapPosition(x, y));
							system.allowMove(system.getCurrentPlayer(), true);
						}
						else {
							JOptionPane.showMessageDialog(null, gamelog.getString("pirate_click_treasure_tile"));
						}
					}
					
				});
			}
		}
		revalidate();
	}
	
	public void outbreak(Player contaminated) {
		for(int[] pawn : contaminated.getPositions()) {
			int row = pawn[0];
			int column = pawn[1];
			for(int i = -1; i < 2; i++) {
				for(int j = -1; j < 2; j++) {
					if(row + i >= 0 && column + j >= 0 && row + i < 11 && column + j < 15) {
						Tile tile = getBoard().getBoard()[row + i][column + j];
						if(null != tile.getOccupant())
							system.contaminate(tile.getOccupant());
					}
				}
			}
		}
		endTurn();
	}
	
	public void updateCards() {
		handPanel.update(system.getPlayerHand(system.getCurrentPlayer()), false, system.getCurrentPlayer().getRole().equals(PlayerRole.PIRATE), myInfosShown);
	}
}
