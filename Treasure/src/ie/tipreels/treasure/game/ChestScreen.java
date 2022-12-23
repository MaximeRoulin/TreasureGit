package ie.tipreels.treasure.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ie.tipreels.treasure.TreasureMainScreen;

public class ChestScreen extends JFrame {

	//Attributes
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.7);
	private static final int windowHeight = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().height)*0.7);
	private ImageIcon image;
	private ChestScreen self;
	
	public ChestScreen(Player player, int pawn, ContentType content, ResourceBundle messages, TreasureMainScreen mainScreen, TreasureGameSystemRuleSet01 system) {
		super();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setResizable(true);
		setTitle(messages.getString("chest_found"));
		self = this;
		JPanel chestPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel label = new JLabel(messages.getString("chest_found"));
		chestPanel.add(label, gbc);
		gbc.gridy++;
		image = new ImageIcon(getClass().getResource("/assets/img/chest.png"));
		label = new JLabel(image);
		chestPanel.add(label, gbc);
		gbc.gridy++;
		label = new JLabel(messages.getString("insignia"));
		chestPanel.add(label, gbc);
		gbc.gridy++;
		JButton openButton = new JButton(messages.getString("open"));
		switch(content) {
		case TREASURE:
			openButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					system.getGamePanel().disableBoard();
					system.getGamePanel().getHandPanel().disable();
					system.getGamePanel().disableButtons(false);
					system.win(player);
					self.dispose();
				}
				
			});
			break;
		case TRAPPED:
			openButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					system.getLog().appendWithLineBreak(messages.getString("trapped"));
					int index = system.findPlayerIndex(player.getRole());
					if(index == -1)
						JOptionPane.showMessageDialog(null, messages.getString("error"), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
					else {
						if(player.getRole() == PlayerRole.ENGINEER)
							system.getLog().appendWithLineBreak(system.getGamelogBundle().getString("trapped_engineer"));
						else
							system.injurePlayer(index, pawn);
						self.dispose();
					}
				}
				
			});
			break;
		default:
			JOptionPane.showMessageDialog(null, "Unexpected ContentType from the clicked tile", messages.getString("error"), JOptionPane.ERROR_MESSAGE);
			
		}
		chestPanel.add(openButton, gbc);
		this.add(chestPanel);
	}

}
