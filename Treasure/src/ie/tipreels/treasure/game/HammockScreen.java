package ie.tipreels.treasure.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ie.tipreels.treasure.TreasureMainScreen;

public class HammockScreen extends JFrame {

	//Attributes
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.4);
	private static final int windowHeight = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().height)*0.8);
	private int remainingActions;
	
	//Constructor
	public HammockScreen(GameSystem system, ResourceBundle messagesBundle, TreasureMainScreen mainScreen) {
		super();
		remainingActions = 3;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setResizable(true);
		setTitle(messagesBundle.getString("hammock_screen"));
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel remainingActionsLabel = new JLabel(messagesBundle.getString("hammock_actions_1") + " " + remainingActions + " " + messagesBundle.getString("hammock_actions_2"));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		mainPanel.add(remainingActionsLabel, gbc);
		JButton moveButton = new JButton(messagesBundle.getString("hammock_move"));
		moveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(remainingActions > 1) {
					system.allowMove(system.getCurrentPlayer(), false);
					mainScreen.toFront();
					remainingActions--;
					remainingActionsLabel.setText(messagesBundle.getString("hammock_actions_1") + " " + remainingActions + " " + messagesBundle.getString("hammock_actions_2"));
				}
				else {
					system.allowMove(system.getCurrentPlayer(), true);
					system.clearCurrentPlayerFromHammocked();
					dispose();
				}
			}
			
		});
		gbc.gridy++;
		mainPanel.add(moveButton, gbc);
		JButton pickCardButton = new JButton(messagesBundle.getString("hammock_draw_card"));
		HammockScreen pointer = this;
		pickCardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(remainingActions > 1) {
					system.drawCard(pointer);
					remainingActions--;
					remainingActionsLabel.setText(messagesBundle.getString("hammock_actions_1") + " " + remainingActions + " " + messagesBundle.getString("hammock_actions_2"));
				}
				else {
					system.drawCard(pointer);
					system.clearCurrentPlayerFromHammocked();
					dispose();
				}
			}
			
		});
		gbc.gridy++;
		mainPanel.add(pickCardButton, gbc);
		this.add(mainPanel);
		setVisible(true);
	}
	
	//Getter
	public int getRemainingActions() {
		return remainingActions;
	}
}
