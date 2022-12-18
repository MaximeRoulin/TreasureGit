package ie.tipreels.treasure.audio;

import java.util.HashMap;

/**
 * An effects library is a hashmap of effects files
 * @author Maxime Roulin
 *
 */
public class FxLibrary {

	//Attributes
	HashMap<String, FxFile> library;

	//Constructor
	public FxLibrary() {
		super();
		library = new HashMap<String, FxFile>();
	}
	
	//Getters and Setters
	public boolean addFxFile(FxFile fxFile) {
		if(library.containsKey(fxFile.getShortName()))
			return false;
		else {
			library.put(fxFile.getShortName(), fxFile);
			return true;
		}
	}
	
	public boolean removeFxFile(String shortName) {
		if(library.containsKey(shortName)) {
			library.remove(shortName);
			return true;
		}
		else {
			return false;
		}
	}
	
	public FxFile getFxFile(String shortName) {
		if(library.containsKey(shortName))
			return library.get(shortName);
		else
			return null;
	}
}
