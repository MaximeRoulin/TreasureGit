package ie.tipreels.treasure.game.info;

public abstract class TreasureGameInfo {
	protected TreasureGameInfoType type;
	
	//Getter and Setter
	public TreasureGameInfoType getType() {
		return type;
	}
	
	public void setType(TreasureGameInfoType type) {
		this.type = type;
	}
}
