package ie.tipreels.treasure.game;

import java.util.ResourceBundle;

public class CPUPlayer extends Player {

	//Attributes
	private CPUDifficulty difficulty;

	//Constructor
	public CPUPlayer(PlayerRole role, String name, CPUDifficulty difficulty) {
		construct(role, name);
		this.difficulty = difficulty;
	}
	
	//Methods
	public CPUDifficulty getDifficulty() {
		return difficulty;
	}
	
	public void setDifficulty(CPUDifficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	@Override
	public String getDisplay(ResourceBundle resource) {
		String display = "";
		//TODO: add cpu level
		String level = " - ";
		switch(difficulty) {
			case EASY:
				level += resource.getString("easy");
				break;
			case MEDIUM:
				level += resource.getString("medium");
				break;
			case HARD:
				level += resource.getString("hard");
		}
		display = " " + name + " (" + resource.getString("cpu") + level + ")";
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
