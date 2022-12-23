package ie.tipreels.treasure.game;

import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class GameLog extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameLog(String str, int width) {
		super(str, 18, width);
		DefaultCaret caret = (DefaultCaret) getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	public void appendWithLineBreak(String str){
	    append(str + "\n");
	    setCaretPosition(getDocument().getLength());
	}

}
