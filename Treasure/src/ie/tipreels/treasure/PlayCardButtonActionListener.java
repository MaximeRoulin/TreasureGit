package ie.tipreels.treasure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ie.tipreels.treasure.game.CardButton;
import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.HandPanel;

public class PlayCardButtonActionListener implements ActionListener {

	//Attributes
	private GameSystem system;
	private CardButton selectedCard;
	private JButton playCardButton;
	private JButton endTurnButton;
	private HandPanel handPanel;
	
	//Constructor
	public PlayCardButtonActionListener(GameSystem system, CardButton selectedCard, JButton playCardButton, JButton endTurnButton, HandPanel handPanel) {
		this.system = system;
		this.selectedCard = selectedCard;
		this.playCardButton = playCardButton;
		this.endTurnButton = endTurnButton;
		this.handPanel = handPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(selectedCard != null) {			
			system.playCard(selectedCard);
			if(system.getCurrentPlayerIndex() == 0)
				system.setPiratePlayedCard(true);
			selectedCard = null;
			playCardButton.setEnabled(false);
			if(system.getCurrentPlayerIndex() == 0) {
				if(system.isPirateHasMoved())
					endTurnButton.setEnabled(true);
				handPanel.disable();
			}
			handPanel.repaint();
		}
		else system.showErrorMessage("No selected card when play is clicked");
	}

}
