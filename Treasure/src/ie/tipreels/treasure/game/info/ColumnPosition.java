package ie.tipreels.treasure.game.info;

public class ColumnPosition extends TreasureGameInfo {
	
	//Attributes
	private int firstColumn;
	private int lastColumn;
	
	//Builder
	public ColumnPosition(int firstColumn, int lastColumn) {
		this.type = TreasureGameInfoType.COLUMNPOSITION;
		this.firstColumn = firstColumn;
		this.lastColumn = lastColumn;
	}

	//Getters and setters
	public int getfirstColumn() {
		return firstColumn;
	}

	public void setFirstColumn(int firstColumn) {
		this.firstColumn = firstColumn;
	}

	public int getLastColumn() {
		return lastColumn;
	}

	public void setLastColumn(int lastColumn) {
		this.lastColumn = lastColumn;
	}
}
