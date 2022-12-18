package ie.tipreels.treasure.game.info;

public class TreasurePosition extends TreasureGameInfo {
	
	//Attributes
	private int row;
	private int column;
	
	//Builder
	public TreasurePosition(int row, int column) {
		this.type = TreasureGameInfoType.TREASUREPOSITION;
		this.row = row;
		this.column = column;
	}

	//Getters and setters
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
