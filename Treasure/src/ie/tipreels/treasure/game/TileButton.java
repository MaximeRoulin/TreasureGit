package ie.tipreels.treasure.game;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ie.tipreels.treasure.game.info.TreasureGameInfoType;

public class TileButton extends JButton {

	//Attributes
	private static final long serialVersionUID = 1L;
//	private static Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK);
//	private static Border yellowBorder = BorderFactory.createLineBorder(Color.YELLOW);
	private boolean reachable;
	private Color foregroundBeforeHighlight;
	private Color backgroundBeforeHighlight;
	private Tile tile;
//	private int[] position;
	
	public TileButton(String label/*, Tile tile, int[] position*/) {
		super(label);
		reachable = false;
		setEnabled(false);
//		this.tile = tile;
//		this.position = position;
	}
	
	//Getters
//	public Tile getTile() {
//		return tile;
//	}
//	
//	public int[] getPosition() {
//		return position;
//	}
	public boolean isReachable() {
		return reachable;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
//	public Color getForegroundBeforeHighlight() {
//		return foregroundBeforeHighlight;
//	}
//	
//	public void setForegroundBeforeHighlight(Color foregroundBeforeHighlight) {
//		this.foregroundBeforeHighlight = foregroundBeforeHighlight;
//	}
//	
//	public Color getBackgroundBeforeHighlight() {
//		return backgroundBeforeHighlight;
//	}
//	
//	public void setBackgroundBeforeHighlight(Color backgroundBeforeHighlight) {
//		this.backgroundBeforeHighlight = backgroundBeforeHighlight;
//	}
	
	//Methods
	public void color(Tile tile) {
		switch(tile.getType()) {
		case OCEAN:
		case SHALLOWWATERS:
			setForeground(Color.WHITE);
			setBackground(Color.CYAN);
			break;
		default:
			if(tile.getDiscovered()) {				
				setForeground(Color.WHITE);
				setBackground(Color.DARK_GRAY);
			}
		}
	}
	
	public void color(Player player) {
		switch(player.getRole()) {
		case PIRATE:
			setForeground(Color.WHITE);
			setBackground(Color.BLACK);
			break;
		case CARTOGRAPHER:
			setForeground(Color.BLACK);
			setBackground(Color.GREEN);
			break;
		case SOLDIER:
			setForeground(Color.WHITE);
			setBackground(Color.RED);
			break;
		case DOCTOR:
			setForeground(Color.WHITE);
			setBackground(Color.BLUE);
			break;
		case ENGINEER:
			setForeground(Color.BLACK);
			setBackground(Color.YELLOW);
		}
	}
	
	public void showReachable(boolean reachable) {
		this.reachable = reachable;
		if(reachable)
			setEnabled(true);
		else
			setEnabled(false);
	}
	
	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}
	
	public void highlight(TreasureGameInfoType type) {
		foregroundBeforeHighlight = getForeground();
		backgroundBeforeHighlight = getBackground();
		switch(type) {
		case TREASUREPOSITION:
			setForeground(Color.WHITE);
			setBackground(Color.ORANGE);
			break;
		case TRAPPOSITION:
			setForeground(Color.WHITE);
			setBackground(Color.MAGENTA);			
			break;
		case ROWPOSITION:
		case COLUMNPOSITION:
			setForeground(Color.WHITE);
			setBackground(Color.PINK);
		}
	}
	
	public void dehighlight() {
		setForeground(foregroundBeforeHighlight);
		setBackground(backgroundBeforeHighlight);
	}
	
	public void clearActionListeners() {
		for(ActionListener listener : getActionListeners()) {
			removeActionListener(listener);
		}
	}
}
