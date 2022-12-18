package ie.tipreels.treasure.audio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * The audio system for the game. The other classes will use its methods to change the mood at will
 * @author Maxime Roulin
 *
 */
public class TreasureAudioSystem {

	//Attributes
	
	//Lists of audio files matching an mood
	private TrackList menu;
	private TrackList inGame;
	private TrackList battle;
	private TrackList credits;
	
	//Current tracklist (depending on the mood)
	private TrackList current;
	
	//List of audio files used for effects
	private FxLibrary fx;
	
	private Clip clip ;
	private Clip fxClip;
	private Timer blankTimer;
	private Random rand;
	
	private float volume;
	
	//Constructor
	public TreasureAudioSystem(float volume) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
		super();
		rand = new Random();
		menu = new TrackList();
		inGame = new TrackList();
		battle = new TrackList();
		credits = new TrackList();
		fx = new FxLibrary();
		clip = AudioSystem.getClip();
		fxClip = AudioSystem.getClip();
		if(isInProduction()) {
			menu.addAudioFile("assets/audio/soundtrack/menu/mystery_man_mix.wav", 203);
			menu.addAudioFile("assets/audio/soundtrack/menu/Right before the Sunrise_first_mix.wav", 112);
			menu.addAudioFile("assets/audio/soundtrack/menu/Watching the waves from afar.wav", 41);	
			inGame.addAudioFile("assets/audio/soundtrack/inGame/Camelback Caravan.wav", 255);
			inGame.addAudioFile("assets/audio/soundtrack/inGame/Bivouac.wav", 268);
			inGame.addAudioFile("assets/audio/soundtrack/inGame/Wild horses double mix n mas.wav", 221);
			inGame.addAudioFile("assets/audio/soundtrack/inGame/In the oasis.wav", 83);
			inGame.addAudioFile("assets/audio/soundtrack/inGame/Gone with the tide.wav", 81);
			inGame.addAudioFile("assets/audio/soundtrack/inGame/Fresh Breeze_prod.wav", 145);
			inGame.addAudioFile("assets/audio/soundtrack/inGame/Dew Drops reworked mastered.wav", 258);
			battle.addAudioFile("assets/audio/soundtrack/battle/To the canons.wav", 174);
		}
		else {	
			menu.addAudioFile("src/assets/audio/soundtrack/menu/mystery_man_mix.wav", 203);
			menu.addAudioFile("src/assets/audio/soundtrack/menu/Right before the Sunrise_first_mix.wav", 112);
			menu.addAudioFile("src/assets/audio/soundtrack/menu/Watching the waves from afar.wav", 41);	
			inGame.addAudioFile("src/assets/audio/soundtrack/inGame/Camelback Caravan.wav", 255);
			inGame.addAudioFile("src/assets/audio/soundtrack/inGame/Bivouac.wav", 268);
			inGame.addAudioFile("src/assets/audio/soundtrack/inGame/Wild horses double mix n mas.wav", 221);
			inGame.addAudioFile("src/assets/audio/soundtrack/inGame/In the oasis.wav", 83);
			inGame.addAudioFile("src/assets/audio/soundtrack/inGame/Gone with the tide.wav", 81);
			inGame.addAudioFile("src/assets/audio/soundtrack/inGame/Fresh Breeze_prod.wav", 145);
			inGame.addAudioFile("src/assets/audio/soundtrack/inGame/Dew Drops reworked mastered.wav", 258);
			battle.addAudioFile("src/assets/audio/soundtrack/battle/To the canons.wav", 174);
		}
		current = menu;
		this.volume = volume ;
		firstStart();
		//File tmp = new File(".");
		//System.out.println(tmp.getAbsolutePath());
		
		//File welcomeSoundFile = new File(Toolkit.getDefaultToolkit().getObject.class.getResource("/assets/audio/soundtrack/Treasure ~ Intro Sequence.wav"));

	}
	
	//Methods
	public Clip getClip() {
		return this.clip;
	}
	
	public float getVolume(Clip concernedClip) {
	    FloatControl gainControl = (FloatControl) concernedClip.getControl(FloatControl.Type.MASTER_GAIN);        
	    return (float) Math.pow(10f, gainControl.getValue() / 20f);
	}

	public void setVolume(float volume, Clip concernedClip) {
		//System.out.println(volume);
	    if (volume < 0f || volume > 1f)
	        throw new IllegalArgumentException("Volume not valid: " + volume);
	    FloatControl gainControl = (FloatControl) concernedClip.getControl(FloatControl.Type.MASTER_GAIN);        
	    gainControl.setValue(20f * (float) Math.log10(volume));
	}
	
	
	
	public void firstStart() throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
//		blankTimer = new Timer(500, new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(clip.isActive()) {
//					return;
//				}
//				else {
//					try {
//						clip.open(AudioSystem.getAudioInputStream(new File("src/assets/audio/soundtrack/Treasure ~ Intro Sequence.wav")));
//					} catch (LineUnavailableException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} catch (UnsupportedAudioFileException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					clip.start();
//				}
//			}
//		});
//		blankTimer.setInitialDelay(0);
		try {			
			clip.open(AudioSystem.getAudioInputStream(new File("src/assets/audio/soundtrack/menu/mystery_man_mix.wav")));
		}
		catch(FileNotFoundException e1) {
//			System.out.println("The audio files were not packaged. This program is assumed to be working from a JAR file.");
			clip.open(AudioSystem.getAudioInputStream(new File("assets/audio/soundtrack/menu/mystery_man_mix.wav")));
		} catch(IllegalStateException e2) {
			
		}
		setVolume(volume, clip);
//		clip.open(AudioSystem.getAudioInputStream(new File("src/assets/audio/soundtrack/Treasure ~ Relic & Haven.wav")));
		clip.start();
		clip.addLineListener(new LineListener() {

			@Override
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP) {
//					try {
						int delay = 5000 + rand.nextInt(12000);
						//System.out.println(delay);
						blankTimer = new Timer(delay, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if(clip.isActive()) {
									return;
								}
								else {
									int delay = 5000 + rand.nextInt(12000);
									//System.out.println(delay);
									blankTimer.stop();
									blankTimer.setDelay(delay);
									try {
										//System.out.println("Let's go again!");
										clip.close();
//										System.out.println("The current tracklist is :"+current);
//										System.out.println("A next track could be :"+current.next()+" and its pathname is :" + current.next().getPathname());
										clip.open(AudioSystem.getAudioInputStream(new File(current.next().getPathname())));
										setVolume(volume, clip);
									} catch (LineUnavailableException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (UnsupportedAudioFileException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (NullPointerException e1) {
										e1.printStackTrace();
									}
									clip.start();
								}
							}
						});
						blankTimer.start();
						//clip.open(AudioSystem.getAudioInputStream(new File("src/assets/audio/soundtrack/Treasure ~ Relic & Haven.wav")));
						//clip.start();
//					} catch (LineUnavailableException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (UnsupportedAudioFileException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					//clip.start();
				}
			}
			
		});
	}
	
	public void stop() {
		clip.stop();
	}

	public void changeMood(Mood newMood) {
		stop();
		switch(newMood) {
			case MENU:
				current = menu;
				break;
			case INGAME:
				current = inGame;
				break;
			case BATTLE:
				current = battle;
				break;
			case CREDITS:
				current = credits;
		}
	}

	public Mood getMood() {
		if(current == menu)
			return Mood.MENU;
		else {
			if(current == inGame)
				return Mood.INGAME;
			else {
				if(current == battle)
					return Mood.BATTLE;
				else {
					if(current == credits) {
						return Mood.CREDITS;
					}
					else {
						JOptionPane.showMessageDialog(null, "There is a bug in the coded program. Please contact the makers and send them the following code : Err01GetMoodNotInEnum", "Error", JOptionPane.ERROR_MESSAGE);
						return null;
					}
				}
			}
		}
	}
	
	public void useFx(String shortName) {
		try {
			fxClip.open(AudioSystem.getAudioInputStream(new File(fx.getFxFile(shortName).getAbsolutePath())));
			fxClip.addLineListener(new LineListener() {

				@Override
				public void update(LineEvent event) {
					if(event.getType().equals(LineEvent.Type.CLOSE))
						fxClip.close();
				}
				
			});
		} catch (LineUnavailableException e) {
			JOptionPane.showMessageDialog(null, "There is a bug in the coded program. Please contact the makers and send them the following code : Err02UseFxGetFile", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isInProduction() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		try {
			clip.open(AudioSystem.getAudioInputStream(new File("src/assets/audio/soundtrack/menu/mystery_man_mix.wav")));
			return false;
		} catch(FileNotFoundException e1) {
			return true;
		}
	}
}
