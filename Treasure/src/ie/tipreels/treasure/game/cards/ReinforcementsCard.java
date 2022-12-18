package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.Cancelable;
import ie.tipreels.treasure.game.CancelableType;
import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.Player;

public class ReinforcementsCard extends Card implements Cancelable{
	//Attributes
	private Player player;
	private int turn;
	
	//Constructor
	public ReinforcementsCard(GameSystem system) {
		this.type = CardType.KEEP;
		this.system = system;
		this.setAvailability(CardAvailability.BEFOREMOVE);
		turn = 0;
	}

	//Getters and setter
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public int getTurn() {
		return turn;
	}
	
	//Methods
	@Override
	public void playCard() {
		player = system.getCurrentPlayer();
		system.reinforceCurrentPlayer();
		turn = system.getTurn();
		system.getCancelable().add(this);
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("reinforcements");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("reinforcements_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("reinforcements_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}

	@Override
	public void undo() {
		if(!player.isAlone())
			system.injurePlayer(player, 1, true);
	}
	
	@Override
	public CancelableType getCancelableType() {
		return CancelableType.REINFORCEMENTS;
	}
}
