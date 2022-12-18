package ie.tipreels.treasure.game.info;

public class TrapPosition extends TreasureGameInfo {
	
	//Attributes
	private int row;
	private int column;
	
	//Builder
	public TrapPosition(int row, int column) {
		this.type = TreasureGameInfoType.TRAPPOSITION;
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
