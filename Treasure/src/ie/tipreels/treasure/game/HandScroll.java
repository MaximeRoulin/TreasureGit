package ie.tipreels.treasure.game;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ie.tipreels.treasure.game.cards.Card;

public class HandScroll extends JScrollPane {

	//Attributes
	private static final long serialVersionUID = 1L;
	private static final int scrollWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.85);
	private static final int scrollHeight = 220;
	
	public HandScroll(JPanel jpanel) {
		super(jpanel);
	}
	
	public void resize(List<Card> list) {
		if(list.size() > 5)
			this.setPreferredSize(new Dimension(scrollWidth, scrollHeight));
		else {			
			if(list.size() == 0) {				
				this.setPreferredSize(new Dimension(0, 0));
			}
			else {
				this.setPreferredSize(new Dimension(170*list.size(), 220));
			}
		}
	}
}
