package ie.tipreels.treasure.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;


public class BoardPanel extends JPanel {

	//Attributes
	private static final long serialVersionUID = 1L;
	private GameBoard board;
	
	//Builder
	public BoardPanel(GameBoard board) {
		super(new GridBagLayout());
		this.setBoard(board);
		GridBagConstraints gbc = new GridBagConstraints();
		TileButton tileButton;
		String label = "";
		for(int i = 0 ; i < 11 ; i++) {
			for(int j = 0 ; j < 15 ; j++) {
				switch(board.getBoard()[i][j].getType()) {
				case OCEAN:
					label = "O";
					break;
				case SHALLOWWATERS:
					label = "S";
					break;
				case COAST:
					label = "C";
					break;
				case PLAINS:
					label = "P";
				}
				tileButton = new TileButton(label);
				tileButton.color(board.getBoard()[i][j]);
				tileButton.setName("tileButton_" + i + "_" + j);
				tileButton.setTile(board.getBoard()[i][j]);
				gbc.gridx = j ;
				gbc.gridy = i ;
				add(tileButton, gbc);
			}
		}
	}
	
	public GameBoard getBoard() {
		return board;
	}
	
	public void setBoard(GameBoard board) {
		this.board = board;
	}
	
	public void clearBoardPanel(List<Player> players) {
		int[] outOfBoard = {-1, -1};
		for(Player player : players) {
			for(int[] position : player.getPositions()) {
				vacateTile(position[0], position[1]);
				position = outOfBoard;
			}
		}
		repaint();
	}
	
	public void clearPawnFromBoard(Player player, int pawn) {
		int[] outOfBoard = {-1, -1};
		vacateTile(player.getPositions().get(pawn)[0], player.getPositions().get(pawn)[1]);
		player.getPositions().set(pawn, outOfBoard);
	}
	
	public void vacateTile(int row, int column) {
		if(row != -1 && column != -1) {			
			TileButton tileButton = (TileButton) getComponent(row * 15 + column);
			Tile tile = board.getBoard()[row][column];
			tile.setDiscovered(true);
			tileButton.color(tile);
			tile.setTaken(false);
			tile.setOccupant(null);
		}
	}
}
