package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;


/**
 * 
 * @author maxim
 *
 */
public abstract class Card {
	
	//Attributes
	protected CardType type;
	protected GameSystem system;
	protected CardAvailability availability;
	
	//Getters and Setters
	public CardType getType() {
		return type;
	}
	
	public void setType(CardType type) {
		this.type = type;
	}
	
	public GameSystem getSystem() {
		return system;
	}
	
	public void setSystem(GameSystem system) {
		this.system = system;
	}
	
	public CardAvailability getAvailability() {
		return availability;
	}
	
	public void setAvailability(CardAvailability availability) {
		this.availability = availability;
	}
	
	public String hidden(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("hidden");
	}
	
	//Methods
	public abstract void playCard();
	public abstract String title(Locale locale);
	public abstract String description(Locale locale);
	public abstract String tooltip(Locale locale);
}
