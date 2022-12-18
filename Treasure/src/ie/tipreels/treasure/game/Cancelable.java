package ie.tipreels.treasure.game;

public interface Cancelable {
	public void undo();
	public Player getPlayer();
	public int getTurn();
	public CancelableType getCancelableType();
}
