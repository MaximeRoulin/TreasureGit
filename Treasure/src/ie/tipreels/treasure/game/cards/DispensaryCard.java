package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.Cancelable;
import ie.tipreels.treasure.game.CancelableType;
import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.Player;

public class DispensaryCard extends Card implements Cancelable {
	//Attributes
	private Player player;
	private int turn;
	
	//Constructor
	public DispensaryCard(GameSystem system) {
		this.type = CardType.KEEP;
		this.setAvailability(CardAvailability.ALWAYS);
		this.system = system;
		turn = 0;
	}
	
	//Getters and Setter
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
		if(null != player) {			
			system.healPlayer(player);
			turn = system.getTurn();
			system.getCancelable().add(this);
		}
		else
			system.showErrorMessage("No player associated with this card");
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("dispensary");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("dispensary_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("dispensary_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}

	@Override
	public void undo() {
		if(player.isAlive()) {			
			if(player.isAlone())
				system.injurePlayer(player, 0, true);			
			else
				system.unReinforcePlayer(player);
		}
	}
	
	@Override
	public CancelableType getCancelableType() {
		return CancelableType.DISPENSARY;
	}
}
