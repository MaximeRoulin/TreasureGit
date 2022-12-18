package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;

public class PoisonCard extends Card {

	
	//Constructor
	public PoisonCard(GameSystem system) {
		this.setType(CardType.PIRATE);
		this.system = system;
		this.setAvailability(CardAvailability.BEFOREMOVE);
	}	
	
	@Override
	public void playCard() {
		system.pickPawnPoison();
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("poison");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("poison_desc");
	}
	
	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("poison_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}

}
