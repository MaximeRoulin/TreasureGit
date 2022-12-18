package ie.tipreels.treasure.game;

import java.util.Random;

/**
 * The game board is made of tiles
 * @author Maxime Roulin
 *
 */
public class GameBoard {
	
	//Attributes
	private Tile[][] board ;
	private Random randomizer;
	
	//Constructor
	public GameBoard() {
		super();
		board = new Tile[11][15];
		
		randomizer = new Random();
		for(int i = 0 ; i < 11 ; i++) {
			switch(i) {
				case 0:
				case 10:
					for(int j = 0 ; j < 15 ; j++) {
						board[i][j] = new Tile(TileType.OCEAN);
					}
					break;
				default:
					board[i][0] = new Tile(TileType.OCEAN);
					board[i][14] = new Tile(TileType.OCEAN);
					for(int j = 1 ; j < 14 ; j++) {
						int rand = randomizer.nextInt(10);
						switch(rand) {
						case 0:
							board[i][j] = new Tile(TerrainType.SEA);
							break;
						default:
							board[i][j] = new Tile(TerrainType.LAND);
							
						}
					}
			}
		}
	}
	
	//Getters and Setters
	public Tile[][] getBoard() {
		return board;
	}



	public void setBoard(Tile[][] board) {
		this.board = board;
	}



	// Methods
	public String showBoardForDebug() {
		StringBuffer sB = new StringBuffer();
		if(board[8][5].isValid()) {
			for(int i = 0 ; i < 11 ; i++) {
				sB.append("|");
				for(int j = 0 ; j < 15 ; j++) {
					switch(board[i][j].getType()) {
						case OCEAN:
							sB.append("o|");
							break;
						case SHALLOWWATERS:
							sB.append("s|");
							break;
						case COAST:
							sB.append("c|");
							break;
						case PLAINS:
							sB.append("p|");
							break;
						default:
							sB.append("Error!");
					}
				}
				sB.append("\n");
			}
		}
		else {			
			for(int i = 0 ; i < 11 ; i++) {
				sB.append("|");
				for(int j = 0 ; j < 15 ; j++) {
					switch(board[i][j].getTerrain()) {
					case SEA:
						sB.append("S|");
						break;
					case LAND:
						sB.append("L|");
						break;
					default:
						sB.append("Error!");
					}
				}
				sB.append("\n");
			}
		}
		return sB.toString();
	}
	
	public void validate() throws Exception {
		boolean typeFound;
		int xCounter;
		int yCounter;
		for(int i = 0 ; i < 11 ; i++) {
			for(int j = 0 ; j < 15 ; j++) {
				//System.out.println("Testing the validity of tile " + i + " " + j);
				if(!board[i][j].isValid()) {
					switch(board[i][j].getTerrain()) {
						case SEA:
							typeFound = false;
							xCounter = -1;
							yCounter = -1;
							while(!typeFound && xCounter < 2) {
								if(board[i + xCounter][j].getType() != null && board[i + xCounter][j].getType() == TileType.OCEAN) {
									typeFound = true;
								}
								else {
									if(xCounter == -1)
										xCounter = xCounter + 2;
									else {
										if(board[i][j + yCounter].getType() != null && board[i][j + yCounter].getType() == TileType.OCEAN) {
											typeFound = true;
										}
										else {
											if(yCounter == -1)
												yCounter = yCounter + 2;
											else {
												xCounter = xCounter + 2;
												yCounter = yCounter + 2;
											}
										}
									}
								}
							}
							if(typeFound)
								board[i][j].setType(TileType.OCEAN);
							else
								board[i][j].setType(TileType.SHALLOWWATERS);
							break;
						case LAND:
							typeFound = false;
							xCounter = -1;
							while(!typeFound && xCounter < 2) {
								yCounter = -1;
								while(!typeFound && yCounter < 2) {
									if(board[i + xCounter][j + yCounter].getTerrain() == TerrainType.SEA) {
										typeFound = true ;
									}
									else
										yCounter++;
								}
								xCounter++;
							}
							if(typeFound)
								board[i][j].setType(TileType.COAST);
							else
								board[i][j].setType(TileType.PLAINS);
							break;
						default:
							throw new Exception("Unexpected findings when validate was called!");
					}
				}
			}
		}
	}
}
