package ie.tipreels.treasure;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Main Class for Treasure
 * @author Maxime Roulin
 *
 */
public class TreasureMain {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		TreasureMainScreen screen = null;
//		System.out.println((int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)));
		try {
			screen = new TreasureMainScreen();
			screen.getFocusableWindowState();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(screen != null)
				screen.showError("There was an unexpected error loading the resources files. These should be placed next to the jar file,Please verify their integrity (and existence!).");
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
