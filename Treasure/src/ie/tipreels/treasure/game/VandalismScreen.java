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
import javax.swing.JPanel;

import ie.tipreels.treasure.game.cards.Card;


public class VandalismScreen extends JFrame implements HandPanelParent {

	//Attributes
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.7);
	private static final int windowHeight = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().height)*0.8);
	private GameSystem system;
	private VandalismScreen pointer;
	private JButton discardCardButton;
	private Player explorer;
	private HandPanel handPanel;
		
	//Constructor
	public VandalismScreen(GameSystem system) throws HeadlessException {
		super();
		this.system = system;
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setResizable(true);		
		setTitle(system.getMessagesBundle().getString("vandalism_screen"));
		pointer = this;
		JPanel mainPanel = new JPanel(new GridBagLayout());
		JLabel label = new JLabel(system.getMessagesBundle().getString("vandalism"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		mainPanel.add(label, gbc);
		JPanel playersPanel = new JPanel(new GridBagLayout());
		for(Player player : system.getPlayers()) {
			if(!player.getRole().equals(PlayerRole.PIRATE)) {
				String text = player.getName() + " " + system.getPlayerHand(player).size() + " " + system.getMessagesBundle().getString("cards");
				JButton button = new JButton(text);
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						handPanel.update(system.getPlayerHand(player), true, true, false);
						explorer = player;
						pointer.revalidate();
					}
					
				});
				playersPanel.add(button, gbc);
				gbc.gridy++;
			}
		}
		gbc.gridy = 1;
		mainPanel.add(playersPanel, gbc);
		handPanel = new HandPanel(new GridBagLayout(), system.getMessagesBundle().getLocale());
		HandScroll handScroll = new HandScroll(handPanel);
		handPanel.setParent(this);
		handPanel.setScroll(handScroll);
		gbc.gridy++;
		mainPanel.add(handScroll, gbc);
		discardCardButton = new JButton(system.getMessagesBundle().getString("discard_card_button"));
		discardCardButton.setEnabled(false);
		gbc.gridy++;
		mainPanel.add(discardCardButton, gbc);
		add(mainPanel);
	}

	//Methods
	public void selectCard(CardButton cardButton) {
		for(ActionListener listener : discardCardButton.getActionListeners()) {
			discardCardButton.removeActionListener(listener);
		}
		discardCardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Card card = cardButton.getCard();
				Player selectedPlayer = explorer;
				system.discardCard(selectedPlayer, card);
				system.getCancelable().add(new Cancelable () {

					@Override
					public void undo() {
						if(selectedPlayer.isAlive()) {							
							system.getPlayerHand(selectedPlayer).add(card);
							if(system.getCurrentPlayer().equals(selectedPlayer))
								system.getGamePanel().updateCards();
						}
						// TODO Auto-generated method stub
						
					}

					@Override
					public Player getPlayer() {
						Player player = system.getCurrentPlayer();
						return player;
					}

					@Override
					public int getTurn() {
						int turn = system.getTurn();
						return turn;
					}

					@Override
					public CancelableType getCancelableType() {
						return CancelableType.CARDLOSS;
					}
					
				});
				system.allowMove(system.getCurrentPlayer(), true);
				pointer.dispose();
			}
				
		});
		discardCardButton.setEnabled(true);
	}
	
	@Override
	public void unselectCard() {
		for(ActionListener listener : discardCardButton.getActionListeners()) {
			discardCardButton.removeActionListener(listener);
		}
		discardCardButton.setEnabled(false);
	}
	
}