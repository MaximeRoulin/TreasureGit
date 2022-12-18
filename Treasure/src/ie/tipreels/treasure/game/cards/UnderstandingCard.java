package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;

public class UnderstandingCard extends Card {

	
	//Constructor
	public UnderstandingCard(GameSystem system) {
		this.type = CardType.KEEP;
		this.system = system;
		this.setAvailability(CardAvailability.ALWAYS);
	}
	
	@Override
	public void playCard() {
		system.showUnderstandingScreen();
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("understanding");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("understanding_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("understanding_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}
	
}
