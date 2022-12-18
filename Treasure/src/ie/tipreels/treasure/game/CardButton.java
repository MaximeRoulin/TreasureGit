package ie.tipreels.treasure.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import ie.tipreels.treasure.game.cards.Card;

public class CardButton extends JButton {
	//Attributes
	static final long serialVersionUID = 1L;
	private Card card;
	private String cardTitle;
	private String description;
	private String tooltip;
	private String hidden;
	private boolean selected;
	private boolean hiddenCard;
	private static Border DEFAULTBORDER = BorderFactory.createLineBorder(Color.BLACK);
	private static Border YELLOWBORDER = BorderFactory.createLineBorder(Color.YELLOW);
	private List<ActionListener> nextActions;
	
	//Constructors
	public CardButton(Card card, Locale locale, boolean selectable, boolean hiddenCard) {
		super();
		this.setCard(card);
		this.cardTitle = card.title(locale);
		this.description = card.description(locale);
		this.tooltip = card.tooltip(locale);
		this.hiddenCard = hiddenCard;
		this.hidden = card.hidden(locale);
		this.setLayout(new GridBagLayout());
		Dimension d = new Dimension(150, 200);
		Dimension d2 = new Dimension(150, 170);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		this.setPreferredSize(d);
		JLabel title = new JLabel();
		String descriptiveText;
		if(hiddenCard) {
			title.setText(hidden);
			descriptiveText = "";
		}
		else {
			title.setText(cardTitle);
			descriptiveText = description;
		}
		title.setFont(new Font(Font.SERIF,  Font.BOLD|Font.ITALIC, 15));
		JPanel titlePanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1;
		titlePanel.add(title, gbc);
		titlePanel.setOpaque(false);
		this.add(titlePanel, gbc);
		JPanel descriptionPanel = new JPanel(new FlowLayout());
		descriptionPanel.setMinimumSize(d2);
		descriptionPanel.setMaximumSize(d2);
		descriptionPanel.setPreferredSize(d2);
		descriptionPanel.setOpaque(false);
		String[] descriptionArray = descriptiveText.split(" ");
		for(String s: descriptionArray) {
			JLabel wordLabel = new JLabel(s);
			descriptionPanel.add(wordLabel);
		}
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.weighty = 1;
		this.add(descriptionPanel, gbc);
		
		nextActions = new ArrayList<ActionListener>();
		if(!selectable) {
			this.setBorder(YELLOWBORDER);
			selected = true;
		}		
		else {
			this.setBorder(DEFAULTBORDER);
			selected = false;
			CardButton caller = this;
			this.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(selected) {
						selected = false;
						caller.setBorder(DEFAULTBORDER);
					}
					else {
						selected = true;
						caller.setBorder(YELLOWBORDER);
					}
					for(ActionListener listener: nextActions) {
						listener.actionPerformed(e);
					}
				}
				
			});
		}
		this.setToolTipText(tooltip);
	}
	
	public CardButton(Card card, Locale locale, boolean selectable) {
		this(card, locale, true, false);
	}
	
	public CardButton(Card card, Locale locale) {
		this(card, locale, true);
	}
	
	//Getters and Setters
	public boolean getSelected() {
		return selected;
	}
	
	public void addNextActionListener(ActionListener actionListener) {
		nextActions.add(actionListener);
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
	
	public boolean getHiddenCard() {
		return hiddenCard;
	}
	
	public void setHiddenCard(boolean hiddenCard) {
		this.hiddenCard = hiddenCard;
	}
	
	//Method
	public void resetCardButtonBorder() {
		selected = false;
		setBorder(DEFAULTBORDER);
	}
	
}
