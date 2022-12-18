package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.TileType;

public class HurricaneCard extends Card {

	//Constructor
	public HurricaneCard(GameSystem system) {
		this.type = CardType.INSTANT;
		this.setAvailability(CardAvailability.BEFOREMOVE);
		this.system = system;
	}
	
	@Override
	public void playCard() {
		system.startRelocate(TileType.PLAINS);
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("hurricane");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("hurricane_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("hurricane_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}
	
}
