package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;

public class DinghyCard extends Card {

	
	//Constructor
	public DinghyCard(GameSystem system) {
		this.system = system;
		this.type = CardType.KEEP;
		this.setAvailability(CardAvailability.BEFOREMOVE);
	}
	
	@Override
	public void playCard() {
		// TODO Auto-generated method stub
		system.getGamePanel().dinghy();
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("dinghy");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("dinghy_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("dinghy_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}
	
}
