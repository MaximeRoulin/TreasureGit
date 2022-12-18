package ie.tipreels.treasure.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TurnScreen extends JFrame {

	//Attributes
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.5);
	private static final int windowHeight = 400;
	private TurnScreen pointer;
	
	public TurnScreen(ResourceBundle bundle, ResourceBundle messages, String playerName, boolean firstTurn, JFrame caller, GameSystem system) {
		super();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setResizable(true);
		setTitle(bundle.getString("turn") + "...");
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		JLabel text = new JLabel(bundle.getString("turn") + " " + playerName + ".");
		panel.add(text, gbc);
		if(firstTurn) {
			gbc.gridy++;
			JLabel firstTurnLabel_1 = new JLabel(bundle.getString("first_turn_1"));
			panel.add(firstTurnLabel_1, gbc);
			gbc.gridy++;
			JLabel firstTurnLabel_2 = new JLabel(bundle.getString("first_turn_2"));
			panel.add(firstTurnLabel_2, gbc);
		}
		gbc.gridy++;
		JCheckBox hideBox = new JCheckBox(messages.getString("hide_pop_up"));
		panel.add(hideBox, gbc);
		gbc.gridy++;
		JLabel thisGameLabel = new JLabel(messages.getString("this_game"));
		panel.add(thisGameLabel, gbc);
		gbc.gridy++;
		JButton okButton = new JButton(messages.getString("ok"));
		pointer = this;
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				caller.toFront();
				pointer.setVisible(false);
				pointer.dispose();
				system.setHideTurnPopUp(hideBox.isSelected());
			}
			
		});
		panel.add(okButton, gbc);
		this.add(panel);
		this.setVisible(true);
	}
	
	public void setCloseOnWindowFocusLost(boolean close) {
		if(close) {			
			this.addWindowFocusListener(new WindowFocusListener() {
				
				@Override
				public void windowGainedFocus(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowLostFocus(WindowEvent e) {
					pointer.setVisible(false);
					pointer.dispose();
				}
				
				
				
			});;
		}
		else {
			for(WindowFocusListener listener : this.getWindowFocusListeners()) {				
				this.removeWindowFocusListener(listener);
			}
		}
	}
	
}
