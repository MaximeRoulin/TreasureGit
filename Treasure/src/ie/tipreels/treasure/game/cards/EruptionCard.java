package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.TileType;

public class EruptionCard extends Card {
	
	//Constructor
	public EruptionCard(GameSystem system) {
		this.system = system;
		this.type = CardType.INSTANT;
		this.setAvailability(CardAvailability.BEFOREMOVE);
	}
	
	public GameSystem getSystem() {
		return system;
	}
	
	public void setSystem(GameSystem system) {
		this.system = system;
	}
	
	@Override
	public void playCard() {
		// TODO Auto-generated method stub
		system.startRelocate(TileType.COAST);
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("eruption");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("eruption_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("eruption_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}
	
}
