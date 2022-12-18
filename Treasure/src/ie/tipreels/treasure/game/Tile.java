package ie.tipreels.treasure.game;

/**
 * Tiles make up the game board. They have a type (mainly for aesthetic considerations), a selected terrain (impacting the movement of the players),
 * @author Maxime Roulin
 *
 */
public class Tile {
	
	//Attribute
	private TileType type ;
	private TerrainType terrain;
	private ContentType content;
	private boolean discovered;
	private boolean taken;
	private Player occupant;
	
	//Constructors
	public Tile() {
		super();
		content = ContentType.EMPTY;
		discovered = false;
		taken = false;
		occupant = null;
	}
	
	public Tile(TileType type, TerrainType terrain) {
		super();
		this.type = type;
		this.terrain = terrain;
		content = ContentType.EMPTY;
		discovered = false;
		taken = false;
		occupant = null;
	}
	
	public Tile(TileType type) {
		super();
		this.type = type;
		switch(type) {
			case OCEAN:
			case SHALLOWWATERS:
				terrain = TerrainType.SEA;
				content = ContentType.EMPTY;
				discovered = false;
				taken = false;
				occupant = null;
				break;
			case COAST:
			case PLAINS:
				terrain = TerrainType.LAND;
				content = ContentType.EMPTY;
				discovered = false;
				taken = false;
				occupant = null;
				break;
			default:
				terrain = null;
		}
	}
	
	public Tile(TerrainType terrain) {
		super();
		this.terrain = terrain;
		content = ContentType.EMPTY;
		discovered = false;
		taken = false;
		occupant = null;
	}

	//Getters and Setters
	public TileType getType() {
		return type;
	}

	public boolean setType(TileType type) {
		if(null == this.terrain) {
			this.type = type;
			return true;
		}
		else {			
			switch(this.terrain) {
				case LAND:
					switch(type) {
					case COAST:
					case PLAINS:
						this.type = type;
						return true;
					default:
						return false;
					}
				case SEA:
					switch(type) {
						case OCEAN:
						case SHALLOWWATERS:
							this.type = type;
							return true;
						default:
							return false;
					}
				default:
					return false;
			}
		}
	}

	public TerrainType getTerrain() {
		return terrain;
	}

	public boolean setTerrain(TerrainType terrain) {
		if(null == this.type) {
			this.terrain = terrain;
			return true;
		}
		else {			
			switch(this.type) {
				case OCEAN:
				case SHALLOWWATERS:
					switch(terrain) {
						case SEA:
							this.terrain = terrain;
							return true;
						default:
							return false;
					}
				case COAST:
				case PLAINS:
					switch(terrain) {
						case LAND:
							this.terrain = terrain;
							return true;
						default:
							return false;
					}
				default:
					return false;
			}
		}
	}
	
	public ContentType getContent() {
		return content;
	}
	
	public void setContent(ContentType content) {
		this.content = content;
	}
	
	public boolean isValid() {
		boolean result = (this.getType() != null && this.getTerrain() != null);
		return result;
	}
	
	public boolean getDiscovered() {
		return discovered;
	}
	
	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}
	
	public boolean getTaken() {
		return taken;
	}
	
	public void setTaken(boolean taken) {
		this.taken = taken;
	}
	
	public Player getOccupant() {
		return occupant;
	}
	
	public void setOccupant(Player occupant) {
		this.occupant = occupant;
	}
}
