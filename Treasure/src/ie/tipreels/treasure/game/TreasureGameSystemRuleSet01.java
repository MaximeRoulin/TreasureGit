package ie.tipreels.treasure.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import ie.tipreels.treasure.TreasureGamePanel;
import ie.tipreels.treasure.TreasureMainScreen;
import ie.tipreels.treasure.audio.Mood;
import ie.tipreels.treasure.audio.TreasureAudioSystem;
import ie.tipreels.treasure.game.cards.ArtilleryCard;
import ie.tipreels.treasure.game.cards.Card;
import ie.tipreels.treasure.game.cards.CardAvailability;
import ie.tipreels.treasure.game.cards.CardType;
import ie.tipreels.treasure.game.cards.DinghyCard;
import ie.tipreels.treasure.game.cards.DispensaryCard;
import ie.tipreels.treasure.game.cards.EruptionCard;
import ie.tipreels.treasure.game.cards.HamacCard;
import ie.tipreels.treasure.game.cards.HurricaneCard;
import ie.tipreels.treasure.game.cards.LastHurrahCard;
import ie.tipreels.treasure.game.cards.ReinforcementsCard;
import ie.tipreels.treasure.game.cards.ReliefCard;
import ie.tipreels.treasure.game.cards.RumCard;
import ie.tipreels.treasure.game.cards.SpyCard;
import ie.tipreels.treasure.game.cards.UnderstandingCard;
import ie.tipreels.treasure.game.cards.VirusCard;
import ie.tipreels.treasure.game.info.TreasureGameInfo;

public class TreasureGameSystemRuleSet01 implements GameSystem {
	//Attributes
	private GameLog log;
	private TreasureGamePanel gamePanel;
	private Map<Player, List<Card>> playersHands;
	private List<Player> players;
//	private List<Card> pirateDeck;
	private List<Card> playerCards;
	private List<Card> discardPile;
	private ResourceBundle gamelogBundle;
	private ResourceBundle messagesBundle;
	private Random randomizer;
	private int lastInfoCardPlayed;
	private boolean firstTurn;
	private int currentPlayerIndex;
	private TreasureMainScreen caller;
	private TreasureAudioSystem audioSystem;
	private boolean reroll;
	private boolean hideTurnPopUp;
	private boolean piratePlayedCard;
	private boolean pirateHasMoved;
	private boolean newInfo;
	private int tippedPlayerIndex;
	private int tempIndex;
	private boolean outOfTurnPirateControl;
	private int testCounter;
	private List<Player> hammocked;
	private List<Player> rummed;
	private List<Player> virused;
	boolean fightInitiated;
	private Player hostile;
	private Player attacked;
	private Player loser;
	private int firstModifier;
	private int secondModifier;
	private boolean initiated;
	private GivenInfoScreen givenInfoScreen;
	private TipScreen tipScreen;
	private int pickedPawn;
	private boolean elevation;
	private List<Cancelable> cancelable;
	private int turn;
	
	//Constructor
	public TreasureGameSystemRuleSet01(TreasureGamePanel gamePanel, ResourceBundle gamelogBundle, ResourceBundle messagesBundle, List<Player> players, boolean hideTurnPopUp, TreasureMainScreen caller, TreasureAudioSystem audioSystem) {
		super();
//		this.board = board;
		this.gamePanel = gamePanel;
		this.log = gamePanel.getLog();
		this.players = players;
		this.gamelogBundle = gamelogBundle;
		this.messagesBundle = messagesBundle;
		this.hideTurnPopUp = hideTurnPopUp;
		this.caller = caller;
		this.audioSystem = audioSystem;
		currentPlayerIndex = 0;
		firstTurn = true;
		randomizer = new Random();
		reroll = false;
		lastInfoCardPlayed = 0;
		outOfTurnPirateControl = false;
		givenInfoScreen = null;
		tipScreen = null;
		testCounter = 0;
		pickedPawn = 0;
		elevation = false;
		turn = 0;
		log.appendWithLineBreak(gamelogBundle.getString("building_decks"));
		hammocked = new ArrayList<Player>();
		rummed = new ArrayList<Player>();
		virused = new ArrayList<Player>();
//		pirateDeck = new ArrayList<Card>();
		playerCards = new ArrayList<Card>();
		discardPile = new ArrayList<Card>();
		cancelable = new ArrayList<Cancelable>();
		playersHands = new HashMap<Player, List<Card>>();
		ArrayList<Card> temp = null;
		for(Player player: players) {
			temp = new ArrayList<Card>();
			if(player.equals(players.get(0))) {				
				playersHands.put(players.get(0), temp);
			}
			else {				
				playersHands.put(player, temp);
			}
		}
		createCardStack();
	}
	
	//Getters and Setters
	public List<Player> getPlayers() {
		return players;
	}
	
	public int getNumberOfPlayers() {
		return players.size();
	}
	
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	
	public List<Card> getPirateDeck() {
		return playersHands.get(players.get(0));
	}
	
	public GameLog getLog() {
		return log;
	}
	
	public TreasureGamePanel getGamePanel() {
		return gamePanel;
	}
	
	public String getPirateName() {
		return players.get(0).getName();
	}
	
	public void setHideTurnPopUp(boolean hideTurnPopUp) {
		this.hideTurnPopUp = hideTurnPopUp;
	}

	public boolean isPiratePlayedCard() {
		return piratePlayedCard;
	}

	public void setPiratePlayedCard(boolean piratePlayedCard) {
		this.piratePlayedCard = piratePlayedCard;
	}

	public boolean isPirateHasMoved() {
		return pirateHasMoved;
	}

	public void setPirateHasMoved(boolean pirateHasMoved) {
		this.pirateHasMoved = pirateHasMoved;
	}

	public boolean isOutOfTurnPirateControl() {
		return outOfTurnPirateControl;
	}
	
	public void setOutOfTurnPirateControl(boolean outOfTurnPirateControl) {
		this.outOfTurnPirateControl = outOfTurnPirateControl;
	}
	
	@Override
	public void setGivenInfoScreen(GivenInfoScreen givenInfoScreen) {
		this.givenInfoScreen = givenInfoScreen;
	}
	
	@Override
	public void setTipScreen(TipScreen tipScreen) {
		this.tipScreen = tipScreen;
	}
	
	@Override
	public int getPickedPawn() {
		return pickedPawn;
	}
	
	@Override
	public void setPickedPawn(int pickedPawn) {
		this.pickedPawn = pickedPawn;
	}
	
	@Override
	public void setElevation(boolean elevation) {
		this.elevation = elevation;
	}
	
	@Override
	public List<Cancelable> getCancelable() {
		return cancelable;
	}
	
	@Override
	public int getTurn() {
		return turn;
	}
	
	@Override
	public List<Player> getHammocked() {
		return hammocked;
	}
	
	@Override
	public List<Player> getRummed() {
		return rummed;
	}
	
	@Override
	public ResourceBundle getMessagesBundle() {
		return messagesBundle;
	}
	
	@Override
	public ResourceBundle getGamelogBundle() {
		return gamelogBundle;
	}
	
	//Methods
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
	public void goBackToCurrentPlayer() {
		currentPlayerIndex = tempIndex;
	}

	public void pickCard(Card card) {
		playersHands.get(players.get(0)).add(card);
	}
	
	public void discardPirateCard(Card card) {
		playersHands.get(players.get(0)).remove(card);
	}

	public void confirmPirateDeck() {
		log.appendWithLineBreak(gamelogBundle.getString("deck_confirmed"));
	}
	
	public void confirmCardStack() {
		log.appendWithLineBreak(gamelogBundle.getString("stack_confirmed"));
	}
	
	public void createCardStack() {
		for(int i = 0 ; i < 4 ; i++) {			
			playerCards.add(new VirusCard(this));
		}
		for(int i = 0 ; i < 4 ; i++) {			
			playerCards.add(new ReliefCard(this));
		}
		playerCards.add(new HurricaneCard(this));
		playerCards.add(new EruptionCard(this));
		for(int i = 0 ; i < 4 ; i++) {			
			playerCards.add(new ArtilleryCard(this, messagesBundle, gamelogBundle));
		}
		for(int i = 0 ; i < 4 ; i++) {			
			playerCards.add(new LastHurrahCard(this, gamelogBundle));
		}
		for(int i = 0 ; i < 4 ; i++) {			
			playerCards.add(new DispensaryCard(this));
		}
		for(int i = 0 ; i < 2 ; i++) {			
			playerCards.add(new SpyCard(this));
		}
		for(int i = 0 ; i < 2 ; i++) {			
			playerCards.add(new HamacCard(this));
		}
		for(int i = 0 ; i < 2 ; i++) {			
			playerCards.add(new DinghyCard(this));
		}
		for(int i = 0 ; i < 4 ; i++) {			
			playerCards.add(new UnderstandingCard(this));
		}
		for(int i = 0 ; i < 4 ; i++) {			
			playerCards.add(new RumCard(this));
		}
		for(int i = 0 ; i < 4 ; i++) {			
			playerCards.add(new ReinforcementsCard(this));
		}
		confirmCardStack();
	}
	
	public boolean drawCard() {
		return(drawCard(getCurrentPlayer()));
	}
	
	public boolean drawCard(Player playing) {
		boolean cardEndsTurn = false;
//		Card picked = forceCardPicked();
		Card picked = pickExplorerCard();
		//				System.out.println(picked.toString());
		switch(picked.getType()) {
		case INSTANT:
			log.appendWithLineBreak(gamelogBundle.getString("instant_card"));
			picked.playCard();
			break;
		case KEEP:
			log.appendWithLineBreak(gamelogBundle.getString("keep_card"));
			playersHands.get(playing).add(picked);
			gamePanel.getHandPanel().update(playersHands.get(playing), false, false, false);
			break;
		default:
			JOptionPane.showMessageDialog(null, gamelogBundle.getString("error"));
		}
		if(picked instanceof EruptionCard || picked instanceof HurricaneCard || picked instanceof VirusCard)
			cardEndsTurn = true;
		else {
			if(picked instanceof LastHurrahCard) {
				LastHurrahCard hurrah = (LastHurrahCard) picked;
				hurrah.setPlayer(playing);
			}
		}
		if(!cardEndsTurn)		
			allowMove(playing, true);
		else
			gamePanel.endTurn();
		return cardEndsTurn;
	}
	
	public boolean drawCard(HammockScreen hammockScreen) {
		boolean cardEndsTurn = false;
		Card picked = forceCardPicked();
		//				Card picked = pickExplorerCard();
		//				System.out.println(picked.toString());
		switch(picked.getType()) {
		case INSTANT:
			log.appendWithLineBreak(gamelogBundle.getString("instant_card"));
			picked.playCard();
			break;
		case KEEP:
			log.appendWithLineBreak(gamelogBundle.getString("keep_card"));
			playersHands.get(getCurrentPlayer()).add(picked);
			gamePanel.getHandPanel().update(playersHands.get(getCurrentPlayer()), false, getCurrentPlayer().getRole().equals(PlayerRole.PIRATE), false);
			break;
		default:
			JOptionPane.showMessageDialog(null, gamelogBundle.getString("error"));
		}
		if(picked instanceof EruptionCard || picked instanceof HurricaneCard || picked instanceof VirusCard)
			cardEndsTurn = true;
		if(cardEndsTurn) {				
			JOptionPane.showMessageDialog(null, messagesBundle.getString("unlucky_draw_1") + " " + picked.title(messagesBundle.getLocale()) + " " + messagesBundle.getString("unlucky_draw_2"));
			hammockScreen.dispose();
		}
		else {
			JOptionPane.showMessageDialog(null, messagesBundle.getString("normal_draw_1") + " " + picked.title(messagesBundle.getLocale()) + " " + messagesBundle.getString("normal_draw_2"));
			if(hammockScreen.getRemainingActions() == 1)
				endTurn();
		}
		return cardEndsTurn;
	}
	
	public Card forceCardPicked() {
		Card returned;
		if(testCounter != 3)
			returned = new SpyCard(this);
		else
			returned = new VirusCard(this);
		testCounter++;
		return returned;
	}
	
	public Card pickExplorerCard() {
		if(playerCards.isEmpty()) {
			for(Card card: discardPile) {
				playerCards.add(card);
			}
			discardPile = new ArrayList<Card>();
		}
		int index = randomizer.nextInt(playerCards.size());
//		System.out.println("RNG: " + index);
//		System.out.println(playerCards.toString());
		Card picked = playerCards.get(index);
		playerCards.remove(index);
//		playersHands.get(player).add(picked);
		return picked;
	}
	
	public void discardExplorerCard(Card card) {
		discardPile.add(card);
	}
	
	public void play(int playerIndex) throws Exception {
		currentPlayerIndex = playerIndex;
		if(currentPlayerIndex == 0) {
			turn++;
			gamePanel.getLog().appendWithLineBreak(gamelogBundle.getString("new_turn") + " " + turn);
		}
		disposeOfGivenInfoScreen();
		if(elevation)
			elevation = false;
/**		System.out.println("Currently playing is #" + currentPlayerIndex  + " and its position is : [" + 
		players.get(currentPlayerIndex).getPositions().get(0)[0] + "," + players.get(currentPlayerIndex).getPositions().get(0)[1] + "]");*/
		Player playing = players.get(currentPlayerIndex);
		hostile = null;
		attacked = null;
		loser = null;
		gamePanel.getThrowDiceButton().setPawn(0);
		gamePanel.getThrowDiceButton().changeActionListener(gamePanel.getThrowDiceButton().getMoveActionListener());
		boolean availableCardsFixed = false;
		switch(playing.getRole()) {
		case PIRATE:
			if(firstTurn) {
				TileButton  centerButton = (TileButton) (gamePanel.getBoardPanel().getComponent(82));
				centerButton.color(players.get(0));
				log.appendWithLineBreak(gamelogBundle.getString("select"));
				firstTurn = false;
				gamePanel.chooseTreasureTile();
			}
			else {
				triggerPopUp(playing);
				if(playersHands.get(players.get(0)).isEmpty())
					win(playing);
				else {
					log.appendWithLineBreak(gamelogBundle.getString("turn") + playing.getName());
					if(playersHands.get(players.get(0)).get(0).getType().equals(CardType.INFO) && lastInfoCardPlayed == 2) {
						gamePanel.appendLogWithString("forced_info_card");
						gamePanel.getHandPanel().disableNonInfoCards();
						availableCardsFixed = true;
					}
					gamePanel.resetButtons(false);
				}
			}
			break;
		default:
			triggerPopUp(playing);
			gamePanel.resetButtons(false);
			if(newInfo && tippedPlayerIndex == currentPlayerIndex) {
				JOptionPane.showMessageDialog(null, messagesBundle.getString("new_info"));
				newInfo = false;
			}
			log.appendWithLineBreak(gamelogBundle.getString("turn") + playing.getName() + ".");
			if(hammocked.contains(playing)) {
				log.appendWithLineBreak(gamelogBundle.getString("hammock_up_1") + " " + getCurrentPlayer().getName() + " " + gamelogBundle.getString("hammock_up_2"));
				HammockScreen hammockScreen = new HammockScreen(this, messagesBundle, caller);
				hammockScreen.toFront();
				gamePanel.getThrowDiceButton().setEndTurn(false);
			}
			else {				
				if(virused.contains(getCurrentPlayer())) {
					gamePanel.appendLogWithString("sick");
					availableCardsFixed = true;
					gamePanel.endTurn();
				}
				else {					
					availableCardsFixed = drawCard();
				}
			}
		}
		if(!availableCardsFixed)
			gamePanel.changeAvailableCards(CardAvailability.BEFOREMOVE);
	}
	
	public void playNext() {
		resetReroll();
		setPirateHasMoved(false);
		setPiratePlayedCard(false);
		if(currentPlayerIndex == players.size() -1)
			currentPlayerIndex = 0;
		else
			currentPlayerIndex++;
		if(!audioSystem.getMood().equals(Mood.INGAME))
			audioSystem.changeMood(Mood.INGAME);
		try {			
			play(currentPlayerIndex);
		} catch (Exception e) {
			showExceptionMessage(e);
		}
	}
	
	public void pickPawnForMove(Player player, boolean endTurn) {
		gamePanel.pickPawn(player, endTurn);
	}

	public void allowMove(Player playing, boolean endTurn) {
		if(playing.isAlone())
			allowMove(playing, endTurn, 0);
		else
			pickPawnForMove(playing, endTurn);
	}
	
	public void allowMove(Player playing, boolean endTurn, int pawn) {
//		System.out.println("Call to allowMove method w/ parameters playing = " + playing + " and pawn = " + pawn);
		int[] position = playing.getPositions().get(pawn);
//		System.out.println("position of selected pawn = [" + position[0] + ";" + position[1] + "].");
		if(position[0] == -1 && position[1] == -1) {
			log.appendWithLineBreak(gamelogBundle.getString("move_on_sea"));
			gamePanel.propagateReachable(1, 0, 0, TerrainType.SEA, playing.getPositions().get(0), true, playing, pawn, endTurn);
		}
		else {
			if(position[0] <= 10 && position[0] >= 0 && position[1] <= 14 && position[1] >= 0) {
				int move = 0;
				switch(gamePanel.getBoard().getBoard()[position[0]][position[1]].getTerrain()) {
				case SEA:
					log.appendWithLineBreak(gamelogBundle.getString("move_on_sea"));
					move = 1;
					gamePanel.propagateReachable(move, position[0], position[1], gamePanel.getBoard().getBoard()[position[0]][position[1]].getTerrain(), position, true, playing, pawn, endTurn);
					break;
				case LAND:
					gamePanel.getThrowDiceButton().changeActionListener(gamePanel.getThrowDiceButton().getMoveActionListener());
					gamePanel.getThrowDiceButton().setEnabled(true);
					log.appendWithLineBreak(gamelogBundle.getString("move_dice_throw"));
					break;
				default:
					JOptionPane.showMessageDialog(null, gamelogBundle.getString("error"));
				}
			}
			else {
				JOptionPane.showMessageDialog(null, gamelogBundle.getString("error"));
			}
		}
	}

//	public void move(Player player, int[] lastPosition, int[] newPosition) {
//		
//	}

	
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}
	
	public void showExceptionMessage(Exception e) {
		int result = JOptionPane.showConfirmDialog(null, "Unknown error. Save error log for debug team?");
		if(result == JOptionPane.YES_OPTION) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");  
			LocalDateTime now = LocalDateTime.now();  
			File log = new File("./treasureErrorLog" + dtf.format(now) + ".txt");
			if(!log.exists()) {
				try {
					log.createNewFile();
				} catch(IOException exception) {
					showErrorMessage("Could not create error log.");
					exception.printStackTrace();
				}
			} 
			try {
				FileWriter writer = new FileWriter(log.getPath());
				//System.out.println("The comboBox has selected item #"+languageSelection.getSelectedIndex());
				String written = "";
				if(gamePanel != null && gamePanel.getLog() != null)
					written += gamePanel.getLog().getText();
				written += "\n\n" + e.getMessage();
				writer.write(written);
				writer.close();
			} catch (IOException e1) {
				showErrorMessage("Error writing error log.");
				e1.printStackTrace();
			}
		}
	}
	
	public void win(Player player) {
		String victory = player.getName() + " " + gamelogBundle.getString("win");
		JOptionPane.showMessageDialog(null, victory);
		log.appendWithLineBreak(victory);
		gamePanel.disableBoard();
		gamePanel.disableButtons(false);
	}
	
	public List<Card> getNextHand() {
		int nextPlayerIndex;
		if(currentPlayerIndex == players.size() - 1) {
			nextPlayerIndex = 0;
		}
		else {
			nextPlayerIndex = currentPlayerIndex + 1;
		}
		return playersHands.get(players.get(nextPlayerIndex));
			
	}

	@Override
	public void throwDiceForMove(int pawn, boolean endTurn) {
		if(players.get(currentPlayerIndex).getRole().equals(PlayerRole.CARTOGRAPHER)) {			
			if(!reroll) {
				reroll = true;
//				System.out.println("This player is a cartographer w/ a reroll left.");
			}
			else {	
				reroll = false;
				gamePanel.disableBoard();
//				System.out.println("This player is a cartographer w/ no reroll left.");
			}			
		}
//		else
//			System.out.println("This player isn't a cartographer");
		int move;
		if(elevation) {
			move = 1;
		}
		else
			move = throwDice(0);			
		if(rummed.contains(getCurrentPlayer())) {
			gamePanel.appendLogWithString("drunk_explorer");
			move *= 2;
			rummed.remove(getCurrentPlayer());
		}
		int[] position = players.get(currentPlayerIndex).getPositions().get(pawn);
		if(!reroll)
			gamePanel.getThrowDiceButton().setEnabled(false);
		if(players.get(currentPlayerIndex).getRole().equals(PlayerRole.PIRATE)) {
			log.appendWithLineBreak(gamelogBundle.getString("dice_throw_result") +  (move + 1));
			gamePanel.propagateReachable(move + 1, position[0], position[1], gamePanel.getBoard().getBoard()[position[0]][position[1]].getTerrain(), position, true, players.get(currentPlayerIndex), pawn, endTurn);
		}
		else {			
			switch(move) {
				case 0:
					log.appendWithLineBreak(gamelogBundle.getString("critical_result"));
					injurePlayer(currentPlayerIndex, pawn);
					gamePanel.getEndTurnButton().setEnabled(true);
					break;
				default:
					if(elevation) {
						move = 0;
						elevation = false;
					}
					log.appendWithLineBreak(gamelogBundle.getString("dice_throw_result") + " " + (move + 1));
					if(reroll)
						//TODO: Strings for cartographer reroll
						log.appendWithLineBreak("You're the cartographer, you can reroll once");
					gamePanel.propagateReachable(move + 1, position[0], position[1], gamePanel.getBoard().getBoard()[position[0]][position[1]].getTerrain(), position, true, players.get(currentPlayerIndex), pawn, endTurn);
			}
		}
	}
	
	@Override
	public void injurePlayer(int playerIndex, int pawn) {
//		System.out.println("Call to injure player method");
		Player injured = players.get(playerIndex);
		injurePlayer(injured, pawn, true);
	}

	@Override
	public void injurePlayer(Player injured, int pawn, boolean endTurn) {
		switch(injured.getRole()) {
			case PIRATE:
				log.appendWithLineBreak(injured.getName() + " " + gamelogBundle.getString("pirate_injured"));
				injured.setIsAllowedToPlay(false);
				break;
			default:
				if(!injured.isAlone()) {
					log.appendWithLineBreak(injured.getName() + " " + gamelogBundle.getString("reinforced_player_injured"));
					gamePanel.getBoardPanel().vacateTile(injured.getPositions().get(pawn)[0], injured.getPositions().get(pawn)[1]);
				}
				else {
					if(injured.getHealth() == 1) {
						log.appendWithLineBreak(injured.getName() + " " + gamelogBundle.getString("solo_player_injured") + " " + injured.getHealth());
						log.appendWithLineBreak(injured.getName() + " " + gamelogBundle.getString("player_killed"));
						gamePanel.getBoardPanel().vacateTile(injured.getPositions().get(pawn)[0], injured.getPositions().get(pawn)[1]);
						players.remove(injured);
						gamePanel.killPlayer(injured);
					}
					else {
						injured.setHealth(injured.getHealth() - 1);
						log.appendWithLineBreak(injured.getName() + " " + gamelogBundle.getString("solo_player_injured") + " " + injured.getHealth());
						cancelable.add(new Cancelable() {

							@Override
							public void undo() {
								if((injured.getRole().equals(PlayerRole.DOCTOR) && injured.getHealth() != 6) || (!injured.getRole().equals(PlayerRole.PIRATE) && !injured.getRole().equals(PlayerRole.DOCTOR) && injured.getHealth() != 4))
									injured.setHealth(injured.getHealth() + 1);
							}

							@Override
							public Player getPlayer() {
								return injured;
							}

							@Override
							public int getTurn() {
								return turn;
							}
							
							@Override
							public CancelableType getCancelableType() {
								return CancelableType.INJURY;
							}
						});
					}
				}
				gamePanel.updateDisplayAfterInjury(findPlayerIndex(injured.getRole()), endTurn);;
//				System.out.println("revalidate");
		}
	}
	
	public int throwDice(int modifier) {
		int result = randomizer.nextInt(6) + modifier;
		return result;
	}
	
	public void showChestScreen(Player player, int pawn, ContentType content) {
		ChestScreen chest = new ChestScreen(player, pawn, content, gamelogBundle, caller, this);
		chest.setVisible(true);
	}
	
	public int findPlayerIndex(PlayerRole type) {
		if(type.equals(PlayerRole.PIRATE))
			return 0;
		else {
			int i = 1;
			boolean found = false;
			while(i <= players.size() || !found) {
				if(players.get(i).getRole().equals(type)) {
					found = true;
					return i;
				}
				else {
					i++;
				}
			}
			return -1;
		}
	}
	
	public void firstSetUpFight(Player hostile, Player attacked, boolean initiated) {
		if(!audioSystem.getMood().equals(Mood.BATTLE))
			audioSystem.changeMood(Mood.BATTLE);
		gamePanel.changeAvailableCards(CardAvailability.AFTERMOVE);
//		System.out.println("first set up");
		this.hostile = hostile;
		this.attacked = attacked;
		this.initiated = initiated;
		gamePanel.disableButtons(false);
		gamePanel.disableBoard();
		log.appendWithLineBreak(gamelogBundle.getString("fight_initiated_1") + hostile.getName() + gamelogBundle.getString("fight_initiated_2"));
		checkForArtilleryCard(hostile);
		gamePanel.getThrowDiceButton().changeActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int score = throwDice(1);
				firstModifier = 0;
				fightInitiated = false;
				if(hostile.getRole().equals(PlayerRole.SOLDIER)) {
					score++;
					log.appendWithLineBreak(gamelogBundle.getString("soldier_fighting"));
				}
				if(initiated) {
					score++;
					fightInitiated = true;
					log.appendWithLineBreak(gamelogBundle.getString("fight_chosen"));
				}
				firstModifier = hostile.useModifier();
				final int finalScore = score + firstModifier;
				if(firstModifier > 0) {
					log.appendWithLineBreak(gamelogBundle.getString("card_modifier_1") + firstModifier + gamelogBundle.getString("card_modifier_2"));
				}
				log.appendWithLineBreak(gamelogBundle.getString("final_score") + " " + finalScore + ".");
				gamePanel.disableButtons(false);
				log.appendWithLineBreak(gamelogBundle.getString("fight_continued_1") + attacked.getName() + gamelogBundle.getString("fight_continued_2"));
				checkForArtilleryCard(attacked);
				gamePanel.getThrowDiceButton().changeActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						secondSetUpFight(hostile, finalScore, attacked, initiated);
					}
					
				});
				gamePanel.getThrowDiceButton().setEnabled(true);
			}
			
		});
		gamePanel.getThrowDiceButton().setEnabled(true);
	}
	
	private void checkForArtilleryCard(Player player) {
		boolean artilleryCardPresent = false;
		for(Card card : playersHands.get(player)) {
			if(card instanceof ArtilleryCard)
				artilleryCardPresent = true;
		}
		if(artilleryCardPresent) {
			String confirmDialog = messagesBundle.getString("confirm_use_artillery_1") + player.getName() + messagesBundle.getString("confirm_use_artillery_2");
			int result = JOptionPane.showConfirmDialog(null, confirmDialog);
			if(result == JOptionPane.YES_OPTION)
				findAndPlayArtilleryCard(player);
		}	
	}

	private void findAndPlayArtilleryCard(Player player) {
		boolean cardPlayed = false;
		List<Card> hand = playersHands.get(player);
		int handSize = hand.size();
		int i = 0;
		Card temp;
		CardButton button;
		while(i<handSize && !cardPlayed) {
			temp = hand.get(i);
			if(temp instanceof ArtilleryCard)
				if(players.get(getCurrentPlayerIndex()).equals(player)) {
//					System.out.println("Automatically playing a card from the hand of current player.");
					button = (CardButton) gamePanel.getHandPanel().getComponent(i);
					cardPlayed = true;
					playCard(button);
				}
				else {
					cardPlayed = true;
					playCardForPlayer(temp, player);
				}
			i++;
		}
		
	}

	public void secondSetUpFight(Player hostile, int hostileScore, Player attacked, boolean initiated) {
//		System.out.println("second set up");
		gamePanel.resetButtons(true);
		int attackedScore = throwDice(1);
		if(attacked.getRole().equals(PlayerRole.SOLDIER)) {
			attackedScore++;
			log.appendWithLineBreak(gamelogBundle.getString("soldier_fighting"));
		}
		secondModifier = attacked.useModifier();
		attackedScore += secondModifier;
		if(secondModifier > 0) {
			log.appendWithLineBreak(gamelogBundle.getString("card_modifier_1") + secondModifier + gamelogBundle.getString("card_modifier_2"));
		}
		log.appendWithLineBreak(gamelogBundle.getString("final_score") + " " + attackedScore + ".");
		boolean hurrahPlayed = false;
		if(hostileScore > attackedScore) {
			loser = attacked;
			if(checkForHurrahCard(attacked)) {
				hurrahPlayed = true;
				findAndPlayHurrahCard(attacked, false);
			}
			else {				
				log.appendWithLineBreak(gamelogBundle.getString("player_has_won_1") + hostile.getName() + " " + gamelogBundle.getString("player_has_won_2"));
			}
		}
		else {
			if(hostileScore < attackedScore) {
				loser = hostile;
				if(checkForHurrahCard(hostile)) {
					hurrahPlayed = true;
					findAndPlayHurrahCard(hostile, false);
				}
				else {					
					log.appendWithLineBreak(gamelogBundle.getString("player_has_won_1") + attacked.getName() + " " + gamelogBundle.getString("player_has_won_2"));
				}
			}
			else {
				log.appendWithLineBreak(gamelogBundle.getString("draw"));
			}
		}
		if(loser != null && !hurrahPlayed) {
			injurePlayer(findPlayerIndex(loser.getRole()), 0);
			gamePanel.changeAvailableCards(CardAvailability.AFTERFIGHT);
			audioSystem.changeMood(Mood.INGAME);			
		}
		if(!initiated)
			allowMove(getCurrentPlayer(), true);
	}

	private void findAndPlayHurrahCard(Player player, boolean loserInjured) {
		boolean cardPlayed = false;
		List<Card> hand = playersHands.get(player);
		int handSize = hand.size();
		int i = 0;
		Card temp;
		CardButton button;
		while(i<handSize && !cardPlayed) {
			temp = hand.get(i);
			if(temp instanceof LastHurrahCard) {				
				LastHurrahCard hurrah = (LastHurrahCard) temp;
				hurrah.setLoserInjured(false);
				if(players.get(getCurrentPlayerIndex()).equals(player)) {
//					System.out.println("Automatically playing a card from the hand of current player.");
					button = (CardButton) gamePanel.getHandPanel().getComponent(i);
					cardPlayed = true;
					playCard(button);
				}
				else {
					cardPlayed = true;
					playCardForPlayer(temp, player);
				}
			}
			i++;
		}
		
	}
	
	public void resetReroll() {
		reroll = false;
	}
	
	private void triggerPopUp(Player playing) {
		if(!hideTurnPopUp) {					
			TurnScreen popUp = new TurnScreen(gamelogBundle, messagesBundle, playing.getName(), firstTurn, caller, this);
			popUp.setVisible(true);
			popUp.setCloseOnWindowFocusLost(true);
		}
	}
	
	public void playCard(CardButton cardButton) {
		Player currentPlayer = getCurrentPlayer();
		if(cardButton.getCard() instanceof DispensaryCard)
			((DispensaryCard) cardButton.getCard()).setPlayer(currentPlayer);
		cardButton.getCard().playCard();
//		System.out.println("Current hand before play has " + playersHands.get(currentPlayer).size() + " cards.");
		playersHands.get(currentPlayer).remove(cardButton.getCard());
		gamePanel.getHandPanel().update(playersHands.get(currentPlayer), false, getCurrentPlayer().getRole().equals(PlayerRole.PIRATE), false);
//		System.out.println("Current hand after play has " + playersHands.get(currentPlayer).size() + " cards.");
		if(currentPlayer.getRole().equals(PlayerRole.PIRATE)) {
			if(cardButton.getCard().getType().equals(CardType.INFO))
				lastInfoCardPlayed = 0;
			else
				lastInfoCardPlayed++;
		}
		gamePanel.revalidate();
		gamePanel.repaint();
	}
	
	public void playCardForPlayer(Card card, Player player) {
		if(card instanceof DispensaryCard)
			((DispensaryCard) card).setPlayer(player);
		card.playCard();
		playersHands.get(player).remove(card);
	}
	
	public void selectInfo(boolean trueInfo) {
//		TODO: show tipScreen
		@SuppressWarnings("unused")
		TipScreen tip = new TipScreen(gamePanel.getBoard(), players, messagesBundle, trueInfo, this);
		gamePanel.getLog().appendWithLineBreak(players.get(0).getName() + " " + gamelogBundle.getString("has_played_secret_card"));
	}
	
	public void giveInfoToPlayer(TreasureGameInfo info, int tippedPlayerIndex) {
		players.get(tippedPlayerIndex).addInfo(info);
		newInfo = true;
		this.tippedPlayerIndex = tippedPlayerIndex;
	}

	@Override
	public void healPlayer(Player player) {
		switch(player.getRole()) {
		case DOCTOR:
			if(player.getHealth() < 6) {
				player.setHealth(player.getHealth() + 1);
				gamePanel.getLog().appendWithLineBreak(players.get(currentPlayerIndex).getName() + " " + gamelogBundle.getString("has_played_dispensary_card"));
				gamePanel.updateDisplayAfterInjury(findPlayerIndex(player.getRole()), true);
			}
			else
				showErrorMessage(messagesBundle.getString("full_health"));
			break;
		case PIRATE:
			showExceptionMessage(new Exception("This player shouldn't have access to this card"));
			break;
		default:
			if(player.getHealth() < 4) {
				player.setHealth(player.getHealth() + 1);
				gamePanel.updateDisplayAfterInjury(findPlayerIndex(player.getRole()), true);
			}
			else
				showErrorMessage(messagesBundle.getString("full_health"));
		}
		
	}

	@Override
	public void startRelocate(TileType tileType) {
		setOutOfTurnPirateControl(true);
		gamePanel.getBoardPanel().clearBoardPanel(players);
		if(tileType.equals(TileType.COAST)) {
			log.appendWithLineBreak(gamelogBundle.getString("eruption"));
		}
		else {
			log.appendWithLineBreak(gamelogBundle.getString("hurricane"));
		}
		tempIndex = currentPlayerIndex;
		currentPlayerIndex = 0;
		gamePanel.setUpRelocatePlayerPawn(currentPlayerIndex, 0, tileType);
	}

	@Override
	public void prepareForHammock() {
		Player player = getCurrentPlayer();
		if(player.getRole().equals(PlayerRole.PIRATE) || hammocked.contains(player))
			showErrorMessage(messagesBundle.getString("already_played_hammock"));
		else {
			hammocked.add(player);
			gamePanel.getLog().appendWithLineBreak(player.getName() + " " + gamelogBundle.getString("has_played_hammock"));
			endTurn();
		}
	}

	@Override
	public void endTurn() {
		gamePanel.endTurn();
	}

	
	@Override
	public void clearCurrentPlayerFromHammocked() {
		if(hammocked.contains(getCurrentPlayer())) {
			hammocked.remove(getCurrentPlayer());
			gamePanel.getThrowDiceButton().setEndTurn(true);
		}
	}

	private boolean checkForHurrahCard(Player player) {
		boolean hurrahCardPresent = false;
		for(Card card : playersHands.get(player)) {
			if(card instanceof LastHurrahCard)
				hurrahCardPresent = true;
		}
		if(hurrahCardPresent) {
			String confirmDialog = messagesBundle.getString("confirm_use_hurrah_1") + player.getName() + messagesBundle.getString("confirm_use_hurrah_2");
			int result = JOptionPane.showConfirmDialog(null, confirmDialog);
			return(result == JOptionPane.YES_OPTION);
		}
		else {
			return false;
		}
	}
	
	@Override
	public void restartFight(boolean loserInjured, Player player) {
//		System.out.println("Call to restart fight with\nhostile: " + hostile + "\nattacked: " + attacked + "\nloser: " + loser);
		if(null != hostile && null != attacked && player.equals(loser)) {
			if(loserInjured)
				healPlayer(loser);
			for(int i = 0 ; i < firstModifier; i ++) {				
				hostile.addModifier();
			}
			for(int i = 0 ; i < secondModifier; i ++) {				
				attacked.addModifier();
			}
			firstSetUpFight(hostile, attacked, initiated);
		}
		else
			showExceptionMessage(new Exception("This card should not have been played"));
	}

	@Override
	public void highlightPlayerInfo(Player player) {
		if(tipScreen == null)
			gamePanel.showPlayerInfo(player);
		else
			tipScreen.showPlayerInfo(player);
	}

	@Override
	public void disposeOfGivenInfoScreen() {
		if(givenInfoScreen != null) {			
			givenInfoScreen.dispose();
			givenInfoScreen = null;
		}
	}

	@Override
	public void pickPawnNoise() {
		gamePanel.selectPawnNoise();
	}

	@Override
	public void pickFirstPawnPlot() {
		gamePanel.selectFirstPawnPlot();
	}

	@Override
	public void pickPawnPoison() {
		gamePanel.selectPawnPoison();
	}

	@Override
	public void pickPawnSpy() {
		gamePanel.selectPawnSpy(getCurrentPlayer());
	}

	@Override
	public void reinforceCurrentPlayer() {
		gamePanel.reinforcePlayer(getCurrentPlayer());
	}
	
	@Override
	public void elevationPlayed() {
		gamePanel.appendLogWithString("elevation");
		setElevation(true);
	}
	
	@Override
	public void rumPlayed() {
		gamePanel.appendLogWithString("has_played_rum");;
		rummed.add(getCurrentPlayer());
		endTurn();
	}
	
	@Override
	public void placeTrap() {
		gamePanel.getLog().appendWithLineBreak(getCurrentPlayer().getName() + " " + gamelogBundle.getString("has_played_secret_card"));
		gamePanel.placeTrap();
	}
	
	@Override
	public void showUnderstandingScreen() {
		log.appendWithLineBreak(getCurrentPlayer().getName() + " " + gamelogBundle.getString("has_played_understanding"));
		gamePanel.disableBoard();
		gamePanel.disableButtons(false);
		UnderstandingScreen understandingScreen = new UnderstandingScreen(this);
		understandingScreen.setVisible(true);
	}

	@Override
	public void removeFromHammocked(Player player) {
		if(hammocked.contains(player))
			hammocked.remove(player);
	}
	
	@Override
	public void removeFromRummed(Player player) {
		if(rummed.contains(player))
			rummed.remove(player);
	}
	
	@Override
	public void unReinforcePlayer(Player player) {
		if(!player.isAlone())
			injurePlayer(player, 1, true);
	}
	
	@Override
	public String getCancelableString(CancelableType cancelable, Player player, int turn) {
		String result = "";
		switch(cancelable) {
		case INJURY:
			result = messagesBundle.getString("cancelable_injury") + player.getName() + " " + messagesBundle.getString("turn") + " " + turn + ")";
			break;
		case CARDLOSS:
			result = messagesBundle.getString("cancelable_card_loss") + player.getName() + " " + messagesBundle.getString("turn") + " " + turn + ")";
			break;
		case DISPENSARY:	
			result = messagesBundle.getString("cancelable_dispensary") + player.getName() + " " + messagesBundle.getString("turn") + " " + turn + ")";
			break;
		case HAMMOCK:
			result = messagesBundle.getString("cancelable_hammock") + player.getName() + " " + messagesBundle.getString("turn") + " " + turn + ")";
			break;
		case REINFORCEMENTS:
			result = messagesBundle.getString("cancelable_reinforcements") + player.getName() + " " + messagesBundle.getString("turn") + " " + turn + ")";
			break;
		case RUM:
			result = messagesBundle.getString("cancelable_rum") + player.getName() + " " + messagesBundle.getString("turn") + " " + turn + ")";
		}
		return result;
	}
	
	@Override
	public void showVandalismScreen() {
		gamePanel.appendLogWithString("has_played_vandalism");
		VandalismScreen vandalismScreen = new VandalismScreen(this);
		vandalismScreen.setVisible(true);
	}
	
	@Override
	public void discardCard(Player player, Card card) {
		log.appendWithLineBreak(player.getName() + " " + gamelogBundle.getString("has_lost_card") + " " + card.title(gamelogBundle.getLocale()) + ".");
		playersHands.get(player).remove(card);
	}
	
	@Override
	public List<Card> getPlayerHand(Player player) {
		return playersHands.get(player);
	}
	
	@Override
	public Player getNextPlayer() {
		Player result;
		if(currentPlayerIndex == players.size() -1)
			result = players.get(0);
		else
			result = players.get(currentPlayerIndex + 1);
		return result; 
	}
	
	@Override
	public void outbreak(Player contaminated) {
		gamePanel.appendLogWithString("patient_zero");
		gamePanel.outbreak(contaminated);
	}
	
	@Override
	public void contaminate(Player sick) {
		if(!virused.contains(sick) && !sick.equals(getCurrentPlayer())) {
			gamePanel.getLog().appendWithLineBreak(sick.getName() + " " + gamelogBundle.getString("infected"));
			virused.add(sick);
		}
	}
}
