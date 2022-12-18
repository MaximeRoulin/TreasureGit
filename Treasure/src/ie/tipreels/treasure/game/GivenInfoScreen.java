package ie.tipreels.treasure.game;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GivenInfoScreen extends JFrame {

	//Attributes
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.4);
	private static final int windowHeight = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().height)*0.7);
	
	//Constructor
	public GivenInfoScreen(GameSystem system, List<Player> players, ResourceBundle messages) throws HeadlessException {
		super();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setResizable(true);
		setTitle(messages.getString("given_info_screen"));
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel label = new JLabel(messages.getString("given_info_screen"));
		mainPanel.add(label, gbc);
		gbc.gridy++;
		for(Player player : players) {
			if(!player.getRole().equals(PlayerRole.PIRATE)) {
				JButton playerButton = new JButton(player.getName());
				playerButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						system.highlightPlayerInfo(player);	
					}
					
				});
				switch(player.getRole()) {
				case CARTOGRAPHER:
					playerButton.setForeground(Color.GREEN);
					break;
				case DOCTOR:
					playerButton.setForeground(Color.BLUE);
					break;
				case ENGINEER:
					playerButton.setForeground(Color.BLACK);
					playerButton.setBackground(Color.YELLOW);
					break;
				case SOLDIER:
					playerButton.setForeground(Color.RED);
					break;
				case PIRATE:
					system.showErrorMessage("Player type should NOT be pirate");
				}
				mainPanel.add(playerButton, gbc);
				gbc.gridy++;
			}
		}
		add(mainPanel);
		setVisible(true);
	}

}
