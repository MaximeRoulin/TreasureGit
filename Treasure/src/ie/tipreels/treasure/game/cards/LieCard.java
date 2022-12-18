package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;

public class LieCard extends Card {

	
	//Constructor
	public LieCard(GameSystem system) {
		this.setType(CardType.INFO);
		this.system = system;
		this.setAvailability(CardAvailability.BEFOREMOVE);
	}	
	
	@Override
	public void playCard() {
		system.selectInfo(false);
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("lie");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("lie_desc");
	}
	
	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("lie_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}

}
