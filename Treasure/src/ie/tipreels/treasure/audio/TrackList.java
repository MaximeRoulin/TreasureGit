package ie.tipreels.treasure.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A list of audio files that are supposed to play in the same environnement
 * @author Maxime Roulin
 *
 */
public class TrackList {
	
	//Attributes
	private List<AudioFile> list;
	private Random randomizer;
	
	//Constructors
	public TrackList() {
		super();
		this.list = new ArrayList<AudioFile>();
		randomizer = new Random();
	}
	
	public TrackList(List<AudioFile> list) {
		super();
		this.list = list;
		randomizer = new Random();
	}
	
	//Getters and setters
	public void addAudioFile(AudioFile audioFile) {
		list.add(audioFile);
	}
	
	public void addAudioFile(String pathname, int length) {
		list.add(new AudioFile(pathname, length));
	}

	public void removeAudioFile(AudioFile audioFile) {
		list.remove(audioFile);
	}
	
	public void removeAudioFile(int index) {
		list.remove(index);
	}
	
	public AudioFile next() {
		int next = randomizer.nextInt(list.size());
//		System.out.println("Next track is #" + (next+1) + " of " + list.size() + " tracks. Its pathname is :" + list.get(next));
		return list.get(next);
	}
}
