package ie.tipreels.treasure.game.info;

public class RowPosition extends TreasureGameInfo {
	
	//Attributes
	private int firstRow;
	private int lastRow;
	
	//Builder
	public RowPosition(int firstRow, int lastRow) {
		this.type = TreasureGameInfoType.ROWPOSITION;
		this.firstRow = firstRow;
		this.lastRow = lastRow;
	}

	//Getters and setters
	public int getfirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getLastRow() {
		return lastRow;
	}

	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}
}
