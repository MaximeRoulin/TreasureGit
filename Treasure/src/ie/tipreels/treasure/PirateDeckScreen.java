package ie.tipreels.treasure;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import ie.tipreels.treasure.game.CardButton;
import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.TurnScreen;
import ie.tipreels.treasure.game.cards.AraCard;
import ie.tipreels.treasure.game.cards.Card;
import ie.tipreels.treasure.game.cards.LieCard;
import ie.tipreels.treasure.game.cards.NoiseCard;
import ie.tipreels.treasure.game.cards.PlotCard;
import ie.tipreels.treasure.game.cards.PoisonCard;
import ie.tipreels.treasure.game.cards.TrapCard;
import ie.tipreels.treasure.game.cards.VandalismCard;

public class PirateDeckScreen extends JFrame {

	//Attributes
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.95);
	private static final int windowHeight = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().height)*0.75);
	private static final int panelWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.90);
	private static final int panelHeight = 230;
	private static final Dimension panelDimension = new Dimension(panelWidth, panelHeight);
	private int cardsLeftToChoose;
	private ResourceBundle cardsBundle;
	private JLabel selectLabel;
	private PirateDeckScreen window;
	private TurnScreen popUp;
	
	//Constructor
	public PirateDeckScreen(GameSystem system, Locale currentLocale, boolean hideTurnPopUp, TreasureMainScreen caller) {
		super();
		window = this;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setResizable(true);
		setTitle(ResourceBundle.getBundle("GamelogBundle", currentLocale).getString("deck"));
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				caller.showMainMenu();
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
			
		});
		JPanel deckPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		cardsLeftToChoose = 0;
		int araCards = 0;
		int lieCards = 0;
		ResourceBundle messagesBundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
		switch(system.getNumberOfPlayers()) {
			case 3:
				araCards = 2;
				lieCards = 1;
				cardsLeftToChoose = 7;
				break;
			case 4:
				araCards = 3;
				lieCards = 1;
				cardsLeftToChoose = 11;
				break;
			case 5:
				araCards = 4;
				lieCards = 2;
				cardsLeftToChoose = 14;
				break;
			default:
				JOptionPane.showMessageDialog(null, messagesBundle.getString("error"));
				caller.showMainMenu();
		}
		for(int i = 0 ; i < araCards ; i++) {
			system.pickCard(new AraCard(system));
//			System.out.println("ajout d'une carte ara bavard. Taille du deck : " + system.getPirateHand().size() + ".");
		}
		for(int i = 0 ; i < lieCards ; i++) {
			system.pickCard(new LieCard(system));
//			System.out.println("ajout d'une carte mensonge. Taille du deck : " + system.getPirateHand().size() + ".");
		}
		cardsBundle = ResourceBundle.getBundle("CardsBundle", currentLocale);
		JLabel pirateHandLabel = new JLabel(cardsBundle.getString("pirate_hand"));
		deckPanel.add(pirateHandLabel, gbc);
		JPanel handPanel = new JPanel(new GridBagLayout());
		CardButton cardButton;
		for(Card card: system.getPirateDeck()) {
			cardButton = new CardButton(card, currentLocale, false);
			cardButton.setEnabled(false);
			handPanel.add(cardButton, gbc);
			gbc.gridx++;
		}
		JScrollPane handScroll = new JScrollPane(handPanel);
		handScroll.getHorizontalScrollBar().setUnitIncrement(20);
		JPanel selectPanel = new JPanel(new GridBagLayout());
		selectLabel = new JLabel(cardsBundle.getString("pirate_deck") + cardsLeftToChoose);
		selectLabel.setName("selectLabel");
		gbc.gridx = 0;
		gbc.gridy = 0;
		ArrayList<Card> deck = new ArrayList<Card>();
		for(int i = 0 ; i < 7 ; i++) {
			deck.add(new NoiseCard(system));
		}
		for(int i = 0 ; i < 6 ; i++) {
			deck.add(new PlotCard(system));
		}
		for(int i = 0 ; i < 6 ; i++) {
			deck.add(new TrapCard(system));
		}
		deck.add(new PoisonCard(system));
		for(int i = 0 ; i < 4 ; i++) {
			deck.add(new VandalismCard(system));
		}
		for(Card card: deck) {			
			CardButton pickableCard = new CardButton(card, currentLocale);
			pickableCard.addNextActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(pickableCard.getSelected()) {
						cardsLeftToChoose--;
						system.pickCard(card);
					}
					else {
						cardsLeftToChoose++;
						system.discardPirateCard(card);
					}
					updateDeck();
				}
				
			});
			selectPanel.add(pickableCard, gbc);
			gbc.gridx++;
		}
		JScrollPane selectScroll = new JScrollPane(selectPanel);
		selectScroll.getHorizontalScrollBar().setUnitIncrement(20);
		selectScroll.setMinimumSize(panelDimension);
		selectScroll.setMaximumSize(panelDimension);
		selectScroll.setPreferredSize(panelDimension);
		gbc.gridx = 0;
		gbc.gridy = 1;
		deckPanel.add(handScroll, gbc);
		gbc.gridy++;
		deckPanel.add(selectLabel, gbc);
		gbc.gridy++;
		deckPanel.add(selectScroll, gbc);
		JButton validateButton = new JButton(messagesBundle.getString("validate"));
		validateButton.setEnabled(false);
		validateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
//				for(Card card: system.getPirateHand()) {					
//					System.out.println(card.title(currentLocale));
//				}
				popUp.dispose();
				window.setVisible(false);
//				System.out.println("Let's go!");
				try {					
					system.play(0);
				} catch (Exception exception) {
					system.showExceptionMessage(exception);
				}
				caller.toFront();
			}
			
		});
		gbc.gridy++;
		deckPanel.add(validateButton, gbc);
		JScrollPane verticalScrollPane = new JScrollPane(deckPanel);
//		verticalScrollPane.add(deckPanel);
		this.getContentPane().add(verticalScrollPane);
//		System.out.println("Taille des cartes obligatoires pour le pirate : " + system.getPirateCards().size() + "\nTaille des cartes � selectionner par le pirate : " + cardsLeftToChoose + ".\nNumber of players : " + system.getNumberOfPlayers() + ".");
		this.setVisible(true);
		ResourceBundle gamelogBundle = ResourceBundle.getBundle("GamelogBundle", currentLocale);
		if(!hideTurnPopUp) {			
			popUp = new TurnScreen(gamelogBundle, messagesBundle, system.getPirateName(), true, this, system);
		}
//		popUp.toFront();
	}

	protected void updateDeck() {
		selectLabel.setText(cardsBundle.getString("pirate_deck") + cardsLeftToChoose);
		//TODO: Update because of ScrollPane addition
		JScrollPane verticalScrollPane = (JScrollPane) this.getContentPane().getComponent(0);
//		System.out.println("Number of components : " + verticalScrollPane.getComponentCount() + verticalScrollPane.getComponent(0));
		JPanel mainPanel = (JPanel) verticalScrollPane.getViewport().getView();
		JButton button = (JButton) mainPanel.getComponent(4);
		if(cardsLeftToChoose == 0) {
			button.setEnabled(true);
			JScrollBar scrollBar = verticalScrollPane.getVerticalScrollBar();
			if(scrollBar != null)
				scrollBar.setValue(scrollBar.getMaximum());
		}
		else
			button.setEnabled(false);
		this.revalidate();
	}
	
	public TurnScreen getPopUp() {
		return popUp;
	}
	
}
