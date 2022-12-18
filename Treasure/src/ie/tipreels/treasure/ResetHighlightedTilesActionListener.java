package ie.tipreels.treasure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ResetHighlightedTilesActionListener implements ActionListener {

//Attributes
private TreasureGamePanel panel;
private JButton showButton;
private JButton endTurnButton;
	
	public ResetHighlightedTilesActionListener(TreasureGamePanel panel, JButton showButton, JButton endTurnButton) {
		this.panel = panel;
		this.showButton = showButton;
		this.endTurnButton = endTurnButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		panel.resetHighlightedTilesAndInfoCards();
//		System.out.println("Source equals endTurnButton: " + e.getSource().equals(endTurnButton));
		if(e.getSource().equals(endTurnButton))
			panel.setInfosShown(false);
		showButton.removeActionListener(this);
		endTurnButton.removeActionListener(this);
	}

	public TreasureGamePanel getPanel() {
		return panel;
	}

	public void setPanel(TreasureGamePanel panel) {
		this.panel = panel;
	}
}
