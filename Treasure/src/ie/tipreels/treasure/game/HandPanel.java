package ie.tipreels.treasure.game;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;

import javax.swing.JPanel;

import ie.tipreels.treasure.game.cards.Card;
import ie.tipreels.treasure.game.cards.CardType;


public class HandPanel extends JPanel {

	//Attributes
	private static final long serialVersionUID = 1L;
	private Locale locale;
	private HandPanelParent parent;
	private HandScroll scroll;
	
	public HandPanel(LayoutManager layout, Locale locale) {
		super(layout);
		this.locale = locale;
	}
	
	public void setParent(HandPanelParent parent) {
		this.parent = parent;
	}
	
	public HandScroll getScroll() {
		return scroll;
	}
	
	public void setScroll(HandScroll scroll) {
		this.scroll = scroll;
	}
	
	public void update(List<Card> cards, boolean hiddenCards, boolean piratePlaying, boolean showInfo) {
		int numberOfCardsRemoved = this.getComponentCount();
//		System.out.println("Component count = " + this.getComponentCount());
		for(int i = 0 ; i < numberOfCardsRemoved ; i++) {			
			this.remove(0);
		}
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		for(Card card: cards) {
			CardButton keepCard;
			if(piratePlaying && card.getType().equals(CardType.INFO) && !showInfo)
				keepCard = new CardButton(card, locale, true, true);
			else
				keepCard = new CardButton(card, locale, true, hiddenCards);
			keepCard.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(keepCard.getSelected()) {
//						System.out.println("It is selected!");
						parent.unselectCard();
					}
					else {
//						System.out.println("It is unselected!");
						parent.selectCard(keepCard);
					}
					
				}
				
			});
			this.add(keepCard, gbc);
			gbc.gridx++;
		}
		parent.revalidate();
//		parent.repaint();
//		System.out.println("Update has been called!");
		scroll.resize(cards);
		scroll.revalidate();
//		System.out.println("scroll size : " + scroll.getSize());
	}

	public void disable() {
		for(Component comp : getComponents()) {
			CardButton cardButton = (CardButton) comp;
			cardButton.setEnabled(false);
		}
	}

	public void disableNonInfoCards() {
		for(Component comp : getComponents()) {
			CardButton cardButton = (CardButton) comp;
			if(!cardButton.getCard().getType().equals(CardType.INFO))
				cardButton.setEnabled(false);
		}
	}
	
	
}
