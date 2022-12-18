package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;

public class VirusCard extends Card {

	
	//Constructor
	public VirusCard(GameSystem system) {
		this.setType(CardType.INSTANT);
		this.system = system;
		this.setAvailability(CardAvailability.BEFOREMOVE);
	}
	
	@Override
	public void playCard() {
		system.outbreak(system.getCurrentPlayer());
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("virus");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("virus_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("virus_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}
	
}
