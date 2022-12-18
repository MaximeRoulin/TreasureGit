package ie.tipreels.treasure.game;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ie.tipreels.treasure.game.info.TreasureGameInfo;

public abstract class Player {
	
	//Attributes
	protected PlayerRole role;
	protected String name;
	protected int health;
	protected List<int[]> positions;
	protected boolean isAllowedToPlay;
	protected int attackRange;
	protected int attackModifier;
	protected List<TreasureGameInfo> infos;
	
	//Factorised part of children's constructors
	protected void construct(PlayerRole role, String name) {
		this.role = role;
		this.name = name;
		switch(role) {
			case DOCTOR:
				health = 6;
				break;
			default:
				health = 4;
		}
		positions = new ArrayList<int[]>();
		int firstPosition[];
		switch(role) {
			case PIRATE:
				int piratePosition[] = { 5, 7 };
				firstPosition = piratePosition; 
				break;
			default:
				int explorerPosition[] = { -1, -1 };
				firstPosition = explorerPosition; 
		}
		positions.add(firstPosition);
		isAllowedToPlay = true;
		attackRange = 1;
		infos = new ArrayList<TreasureGameInfo>();
	}
	
	//Getters and Setters
	public PlayerRole getRole() {
		return role;
	}

	public void setRole(PlayerRole role) {
		this.role = role;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public List<int[]> getPositions() {
		return positions;
	}

	public void setPositions(List<int[]> positions) {
		this.positions = positions;
	}

	public boolean isAlone() {
		return positions.size() == 1;
	}
	
	public boolean getIsAllowedToPlay() {
		return isAllowedToPlay;
	}
	
	public void setIsAllowedToPlay(boolean isAllowedToPlay) {
		this.isAllowedToPlay = isAllowedToPlay;
	}
	
	public int useRange() {
		int currentRange = attackRange;
		attackRange = 1;
		return currentRange;
	}
	
	public void addRange() {
		attackRange = 2;
	}
	
	public int useModifier() {
		int currentModifier = attackModifier;
		attackModifier = 0;
		return currentModifier;
	}
	
	public void addModifier() {
		attackModifier++;
	}
	
	public List<TreasureGameInfo> getInfos() {
		return infos;
	}
	
	public void addInfo(TreasureGameInfo info) {
		infos.add(info);
	}
	
	public boolean reinforce() {
		if(positions.size() == 1 && !role.equals(PlayerRole.PIRATE)) {
			int newPosition[] = { -1, -1 };
			positions.add(newPosition);
			return true;
		}
		else
			return false;
	}

	@Override
	public String toString() {
		return "Player [role=" + role + ", health=" + health + "]";
	}
	
	public boolean isAlive() {
		boolean result = health != 0;
		return result;
	}
	
	public boolean isMaxHealth() {
		boolean result = false;
		switch(role) {
		case PIRATE:
			break;
		case DOCTOR:
			result = health == 6;
			break;
		default:
			result = health == 4;
		}
		return result;
	}
	
	public abstract String getDisplay(ResourceBundle resource);
}
