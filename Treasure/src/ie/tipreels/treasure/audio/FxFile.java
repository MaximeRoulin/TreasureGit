package ie.tipreels.treasure.audio;

/**
 * An effects file is a specialized audio file with a short name to make calling one easier
 * @author maxim
 *
 */
public class FxFile extends AudioFile {

	//Attributes
	private static final long serialVersionUID = 1L;
	private String shortName;

	//Constructor
	public FxFile(String pathname, int length, String shortName) {
		super(pathname, length);
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	
}
