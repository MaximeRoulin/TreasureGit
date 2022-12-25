package ie.tipreels.treasure.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ie.tipreels.treasure.game.cards.DispensaryCard;
import ie.tipreels.treasure.game.cards.HamacCard;
import ie.tipreels.treasure.game.cards.ReinforcementsCard;
import ie.tipreels.treasure.game.cards.RumCard;

public class UnderstandingScreen extends JFrame {

	//Attributes
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.7);
	private static final int windowHeight = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().height)*0.7);
	private UnderstandingScreen pointer;
		
	//Constructor	
	public UnderstandingScreen(GameSystem system) throws HeadlessException {
		super();
		// TODO Auto-generated constructor stub
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setResizable(true);		
		setTitle(system.getMessagesBundle().getString("understanding_screen"));
		pointer = this;
		JPanel mainPanel = new JPanel(new GridBagLayout());
		JLabel label = new JLabel(system.getMessagesBundle().getString("understanding"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		mainPanel.add(label, gbc);
		JPanel buttonsPanel = new JPanel(new GridBagLayout());
		for(Cancelable cancelable : system.getCancelable()) {
			JButton button = new JButton(system.getCancelableString(cancelable.getCancelableType(), cancelable.getPlayer(), cancelable.getTurn()));
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					cancelable.undo();
					system.getCancelable().remove(cancelable);
					system.getLog().appendWithLineBreak(system.getCancelableString(cancelable.getCancelableType(), cancelable.getPlayer(), cancelable.getTurn()) + " " + system.getGamelogBundle().getString("has_been_canceled"));
					system.resumeTurn();
					pointer.dispose();
				}
				
			});
			if(cancelable.getCancelableType().equals(CancelableType.INJURY) && (!cancelable.getPlayer().isAlone() || !cancelable.getPlayer().isAlive() || cancelable.getPlayer().isMaxHealth()))
				button.setEnabled(false);
			if(cancelable.getCancelableType().equals(CancelableType.CARDLOSS) && !cancelable.getPlayer().isAlive())
				button.setEnabled(false);
			if(cancelable instanceof DispensaryCard && (!cancelable.getPlayer().isAlone() || !cancelable.getPlayer().isAlive()))
				button.setEnabled(false);
			if(cancelable instanceof HamacCard && !system.getHammocked().contains(cancelable.getPlayer()))
				button.setEnabled(false);
			if(cancelable instanceof ReinforcementsCard && (cancelable.getPlayer().isAlone() || cancelable.getPlayer().isAlive()))
				button.setEnabled(false);
			if(cancelable instanceof RumCard && (!system.getRummed().contains(cancelable.getPlayer()) || cancelable.getPlayer().isAlive()))
				button.setEnabled(false);
			buttonsPanel.add(button, gbc);
			gbc.gridy++;
		}
		JButton backButton = new JButton(system.getMessagesBundle().getString("back"));
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int selectedOption = JOptionPane.showConfirmDialog(null, system.getMessagesBundle().getString("are_you_sure"));
				if(selectedOption == 0) {
					system.resumeTurn();
					pointer.dispose();
				}
			}
			
		});
		buttonsPanel.add(backButton, gbc);
		JScrollPane scroll = new JScrollPane(buttonsPanel);
		gbc.gridy = 1;
		mainPanel.add(scroll, gbc);
		add(mainPanel);
	}
		
		
}
