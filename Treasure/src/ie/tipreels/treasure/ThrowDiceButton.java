package ie.tipreels.treasure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ie.tipreels.treasure.game.GameSystem;

public class ThrowDiceButton extends JButton {

	//Attributes
	private static final long serialVersionUID = 1L;
	private GameSystem system;
	private int pawn;
	private boolean endTurn;
	private ActionListener moveActionListener;
	
	//Constructor
	public ThrowDiceButton(String text, GameSystem system) {
		super(text);
		this.system = system;
		pawn = 0;
		endTurn = true;
	}

	//Getter and Setters	
	public GameSystem getSystem() {
		return system;
	}

	public void setSystem(GameSystem system) {
		this.system = system;
		for(ActionListener actionListener : getActionListeners()) {
			removeActionListener(actionListener);
		}
		moveActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: allow for reinforcements
				system.throwDiceForMove(pawn, endTurn);
			}
			
		};
		addActionListener(moveActionListener);
	}

	public int getPawn() {
		return pawn;
	}
	
	public void setPawn(int pawn) {
		this.pawn = pawn;
	}
	
	public boolean isEndTurn() {
		return endTurn;
	}

	public void setEndTurn(boolean endTurn) {
		this.endTurn = endTurn;
	}
	
	public ActionListener getMoveActionListener() {
		return moveActionListener;
	}
	
	public void changeActionListener(ActionListener newActionListener) {
		for(ActionListener oldActionListener : getActionListeners()) {
			removeActionListener(oldActionListener);
		}
		addActionListener(newActionListener);
	}
	
}
