package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.Player;

public class LastHurrahCard extends Card {
	
	//Attributes
	private boolean loserInjured;
	private ResourceBundle log;
	private Player player;
	
	//Constructor
	public LastHurrahCard(GameSystem system, ResourceBundle log) {
		this.setType(CardType.KEEP);
		this.setAvailability(CardAvailability.AFTERFIGHT);
		this.system = system;
		this.log = log;
		loserInjured = true;
	}

	//Getters and Setters
	public boolean getLoserInjured() {
		return loserInjured;
	}
	
	public void setLoserInjured(boolean loserInjured) {
		this.loserInjured = loserInjured;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public void playCard() {
		system.getGamePanel().getLog().appendWithLineBreak(system.getCurrentPlayer().getName() + " " + log.getString("has_played_hurrah_card"));;
		system.restartFight(loserInjured, player);
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("last_hurrah");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("last_hurrah_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("last_hurrah_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}
	
}
