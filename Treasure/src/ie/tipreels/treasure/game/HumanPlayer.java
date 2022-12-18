package ie.tipreels.treasure.game;

import java.util.ResourceBundle;


public class HumanPlayer extends Player {
	
	//Constructors
	public HumanPlayer(PlayerRole role, String name) {
		construct(role, name);
	}
	
	@Override
	public String toString() {
		return "Player [role=" + role + ", health=" + health + "]";
	}
	
	@Override
	public String getDisplay(ResourceBundle resource) {
		String display = "";
		display = " " + name;
		String health = "";
		switch(role) {
		case PIRATE:
			health = "";
			break;
		case CARTOGRAPHER:
			health = "\n" + health + " " + resource.getString("hp");
			break;
		case DOCTOR:
			health = "\n" + health + " " + resource.getString("hp");
			break;
		case ENGINEER:
			health = "\n" + health + " " + resource.getString("hp");
			break;
		case SOLDIER:
			health = "\n" + health + " " + resource.getString("hp");
		}
		display += health;
		return display;
	}
}
