package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;

public class SpyCard extends Card {

	
	//Constructor
	public SpyCard(GameSystem system) {
		this.type = CardType.KEEP;
		this.system = system;
		this.setAvailability(CardAvailability.BEFOREMOVE);
	}
	
	@Override
	public void playCard() {
		system.pickPawnSpy();
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("spy");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("spy_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("spy_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}
	
}
