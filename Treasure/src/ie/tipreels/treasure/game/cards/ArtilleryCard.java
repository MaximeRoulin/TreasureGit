package ie.tipreels.treasure.game.cards;

import java.util.Locale;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.HumanPlayer;

public class ArtilleryCard extends Card {
	//Attributes
	private HumanPlayer player;
	private ResourceBundle messages;
	private ResourceBundle log;
	
	//Constructors
	public ArtilleryCard(HumanPlayer player, GameSystem system, ResourceBundle messages, ResourceBundle log) {
		this.setType(CardType.KEEP);
		this.setAvailability(CardAvailability.ALWAYS);
		this.player = player;
		this.system = system;
		this.messages = messages;
		this.log = log;
	}
	
	public ArtilleryCard(GameSystem system, ResourceBundle messages, ResourceBundle log) {
		this(null, system, messages, log);
	}
	
	//Getter and Setter
	public HumanPlayer getPlayer() {
		return player;
	}
	
	public void setPlayer(HumanPlayer player) {
		this.player = player;
	}
	
	@Override
	public void playCard() {
		// TODO Auto-generated method stub
		if(player != null) {			
			system.getGamePanel().getLog().appendWithLineBreak(player.getName() + " " + log.getString("has_played_artillery_card"));
			player.addModifier();
			player.addRange();
		}
		else {
			system.showErrorMessage(messages.getString("error_no_set_player"));
		}
	}

	@Override
	public String title(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("artillery");
	}

	@Override
	public String description(Locale locale) {
		return ResourceBundle.getBundle("CardsBundle", locale).getString("artillery_desc");
	}

	@Override
	public String tooltip(Locale locale) {
		try {
			return ResourceBundle.getBundle("CardsBundle", locale).getString("artillery_tooltip");
		}
		catch (Exception e1) {
			return description(locale);
		}
	}
	
}
