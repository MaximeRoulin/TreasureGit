package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;

public class ReliefCard extends Card {

	//Constructor
	public ReliefCard(GameSystem system) {
		this.type = CardType.INSTANT;
		this.system = system;
		this.setAvailability(CardAvailability.BEFOREMOVE);
	}
	
	@Override
	public void playCard() {
		system.elevationPlayed();
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("relief");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("relief_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("relief_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}
	
}
