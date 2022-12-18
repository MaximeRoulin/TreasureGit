package ie.tipreels.treasure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ie.tipreels.treasure.game.GameBoard;
import ie.tipreels.treasure.game.Player;
import ie.tipreels.treasure.game.TileButton;
import ie.tipreels.treasure.game.TileType;

public class RelocatePawnActionListener implements ActionListener {
	
	//Attributes
	private TreasureGamePanel gamePanel;
	private Player player;
	private int pawn;
	private int row;
	private int column;
	private TileType tileType;
	private GameBoard board;
	private TileButton button;
	private boolean lastPawn;

	//Builder
	public RelocatePawnActionListener(TreasureGamePanel gamePanel, Player player, int pawn, int row, int column, TileType tileType, GameBoard board, TileButton button, boolean lastPawn) {
		this.gamePanel = gamePanel;
		this.player = player;
		this.pawn = pawn;
		this.row = row;
		this.column = column;
		this.tileType = tileType;
		this.board = board;
		this.button = button;
		this.lastPawn = lastPawn;
	}
	
	//Getters and Setters	
	public TreasureGamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(TreasureGamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getPawn() {
		return pawn;
	}

	public void setPawn(int pawn) {
		this.pawn = pawn;
	}

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

	public TileType getTileType() {
		return tileType;
	}

	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}

	public boolean isLastPawn() {
		return lastPawn;
	}

	public void setLastPawn(boolean lastPawn) {
		this.lastPawn = lastPawn;
	}

	//Method overriden
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		gamePanel.movePlayerPawnToTile(player, pawn, board.getBoard()[row][column], button, row, column);
		if(lastPawn) {			
			gamePanel.appendLogWithString("last_pawn");
			gamePanel.disableBoard();
			gamePanel.getSystem().goBackToCurrentPlayer();
			gamePanel.getEndTurnButton().setEnabled(true);
			gamePanel.getSystem().setOutOfTurnPirateControl(false);
		}
		else {
			if(player.isAlone() || pawn ==1)			
				gamePanel.setUpRelocatePlayerPawn(gamePanel.playerIndexAfter(player), 0, tileType);
			else
				gamePanel.setUpRelocatePlayerPawn(gamePanel.findPlayerIndex(player.getRole()), 1, tileType);
		}
	}

}
