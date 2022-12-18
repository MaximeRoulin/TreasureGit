package ie.tipreels.treasure.audio;

import java.io.File;

/**
 * An abstract representation of audio files, with pathnames (inherited from java.io.File) and length
 * @author Maxime Roulin
 *
 */
public class AudioFile extends File {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Attributes
	private String pathname;
	private int length;
	
	//Constructor
	public AudioFile(String pathname, int length) {
		super(pathname);
		this.pathname = pathname;
		this.length = length;
	}
	
	//Getters and Setters
	public String getPathname() {
		return pathname;
	}
	
	public void setPathname(String pathname) {
		this.pathname = pathname;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	
}
