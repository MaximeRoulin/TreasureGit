package ie.tipreels.treasure;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import ie.tipreels.treasure.audio.Mood;
import ie.tipreels.treasure.audio.TreasureAudioSystem;
import ie.tipreels.treasure.game.CPUDifficulty;
import ie.tipreels.treasure.game.CPUPlayer;
import ie.tipreels.treasure.game.GameSystem;
import ie.tipreels.treasure.game.HumanPlayer;
import ie.tipreels.treasure.game.Player;
import ie.tipreels.treasure.game.PlayerRole;
import ie.tipreels.treasure.game.TreasureGameSystemRuleSet01;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The screen of our little game
 * @author Maxime Roulin
 *
 */
public class TreasureMainScreen extends JFrame {

	/**
	 * Attributes
	 */
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().width)*0.9);
	private static final int windowHeight = (int) ((double)(Toolkit.getDefaultToolkit().getScreenSize().height)*0.9);
	private TreasureAudioSystem system;
	//private ResourceBundle settings;
	private ResourceBundle messages;
	private Locale currentLocale;
	private Properties userSettings;
	private int numberOfPlayers ;
	private List<String> availableRoles ;
	private Comparator<String> alphabetical ;
	private boolean automatedChange;
//	private GameBoard board;
	private float volume;
	private TreasureGamePanel gamePanel;
	private GameSystem game;
	private Map<Integer, String> playersRolesCache;
	private Map<Integer, String> playersNamesCache;
	private Map<Integer, Boolean> playersCPUCache;
	private Map<Integer, Integer> playersCPUDifficultyCache;
//	private Map<Integer, Boolean> cpuPlayersCache;
	private boolean hideTurnPopUp;
	
	/**
	 * Constructor
	 * @throws LineUnavailableException 
	 * @throws UnsupportedAudioFileException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public TreasureMainScreen() throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(windowWidth, windowHeight);
		setLocationRelativeTo(null);
		setResizable(true);
		setTitle("Treasure!");
		automatedChange = false;
		hideTurnPopUp = false;
		showMainMenu();
		//TODO: Set IconImage
		setVisible(true);
//		detectSettings();
		playersRolesCache = new HashMap<Integer, String>();
		playersNamesCache = new HashMap<Integer, String>();
		playersCPUCache = new HashMap<Integer, Boolean>();
		playersCPUDifficultyCache = new HashMap<Integer, Integer>();
		system = new TreasureAudioSystem(volume);
	}

	private void detectSettings() {
		InputStream input = null;
		if(new File("./treasureSettings.properties").exists()) {
//			System.out.println("The settings file exists");
			try {
				input = new FileInputStream("./treasureSettings.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userSettings = new Properties(); //ResourceBundle.getBundle("settings");
			try {
				userSettings.load(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentLocale = new Locale(userSettings.getProperty("language"), userSettings.getProperty("country"));
			volume = Float.parseFloat(userSettings.getProperty("volume"));
			hideTurnPopUp = Boolean.parseBoolean(userSettings.getProperty("hide"));
			//System.out.println("The volume in settings is :"+volume);
		} 
		else {
//			System.out.println("The settings file doesn't appear to exist");
			currentLocale = Locale.getDefault();
			volume = 1.0f;
		}
//		System.out.println("The chosen Locale was " + currentLocale.toString());
		messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
	}

	public void showMainMenu() {
		detectSettings();
		this.getContentPane().removeAll();
		if(system != null && system.getMood() != Mood.MENU)
			system.changeMood(Mood.MENU);
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel welcomeMessage = new JLabel(messages.getString("welcome_message"));
		welcomeMessage.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
		JPanel welcomePanel = new JPanel(new GridBagLayout());
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1;
		//System.out.println("GridBagConstraints : anchor = " + gbc.anchor + " and weighty = " + gbc.weighty);
		welcomePanel.add(welcomeMessage, gbc);
		mainPanel.add(welcomePanel, gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		JButton localPlay = new JButton(messages.getString("local"));
		Dimension d = new Dimension(150, 30);
		localPlay.setMinimumSize(d);
		localPlay.setMaximumSize(d);
		localPlay.setPreferredSize(d);
		ActionListener wip = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, messages.getString("error_wip"), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
			}
			
		};
//		singlePlayer.addActionListener(wip);
//		singlePlayer.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				try {
//					String ip = JOptionPane.showInputDialog("Enter the ip you're trying to reach");
//					Socket clientSocket = new Socket(ip, 4444);
//					clientSocket.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//			
//		});
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		localPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				localPlayersSelect();
			}
			
		});
		buttonPanel.add(localPlay, gbc);
		gbc.gridy++;
//		JButton localMultiplayer = new JButton(messages.getString("local"));
//		localMultiplayer.setMinimumSize(d);
//		localMultiplayer.setMaximumSize(d);
//		localMultiplayer.setPreferredSize(d);
//		localMultiplayer.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				localPlayersSelect();
//				
//			}
//			
//		});
//		buttonPanel.add(localMultiplayer, gbc);
//		gbc.gridy++;
		JButton onlineMultiplayer = new JButton(messages.getString("online"));
		onlineMultiplayer.setMinimumSize(d);
		onlineMultiplayer.setMaximumSize(d);
		onlineMultiplayer.setPreferredSize(d);
		onlineMultiplayer.addActionListener(wip);
//		onlineMultiplayer.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				try {
//					System.out.println(InetAddress.getLocalHost());
//					ServerSocket serverSocket = new ServerSocket(4444);
//					Socket clientSocket = serverSocket.accept();
//					JOptionPane.showMessageDialog(gamePanel, "Success");;
//					serverSocket.close();
//					clientSocket.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//			
//		});
		buttonPanel.add(onlineMultiplayer, gbc);
		gbc.gridy++;
		JButton settings = new JButton(messages.getString("settings"));
		settings.setMinimumSize(d);
		settings.setMaximumSize(d);
		settings.setPreferredSize(d);
		settings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showSettings();
			}
			
		});
		buttonPanel.add(settings, gbc);
		gbc.gridy = 1;
		mainPanel.add(buttonPanel, gbc);
		gbc.gridy++;
		JPanel rightsPanel = new JPanel(new GridBagLayout());
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		JLabel rights = new JLabel(messages.getString("rights"));
		rightsPanel.add(rights, gbc);
		gbc.gridy = 2;
		mainPanel.add(rightsPanel, gbc);
		this.repaint();
		this.getContentPane().add(mainPanel);
		this.validate();
		
	}
	
	protected void localPlayersSelect() {
		this.getContentPane().remove(0);
		numberOfPlayers = 3;
		availableRoles = new ArrayList<String>();
		availableRoles.add(messages.getString("cartographer"));
		availableRoles.add(messages.getString("soldier"));
		availableRoles.add(messages.getString("doctor"));
		availableRoles.add(messages.getString("engineer"));
		alphabetical = new Comparator<String>() {
			public int compare(String string1,String string2) {
				return string1.toString().compareTo(string2.toString());
			}
		};
		//System.out.println(availableRoles);
		availableRoles.sort(alphabetical);
		//System.out.println(availableRoles);
		//Collections.sort(availableRoles);
		availableRoles.add(0, messages.getString("select_your_role"));
		JPanel localPlayerSelectPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel localPlayerSelectLabel = new JLabel(messages.getString("local_player_select"));
		localPlayerSelectLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1;
		gbc.gridwidth = 4;
		localPlayerSelectPanel.add(localPlayerSelectLabel, gbc);
		JLabel playerNumberBox = new JLabel(messages.getString("player_number"));
		//gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		localPlayerSelectPanel.add(playerNumberBox, gbc);
		gbc.gridx++;
		List<String> numberOfPlayersList = new ArrayList<String>();
		numberOfPlayersList.add(messages.getString("3_players"));
		numberOfPlayersList.add(messages.getString("4_players"));
		numberOfPlayersList.add(messages.getString("5_players"));
		JComboBox<String> numberSelection = new JComboBox<String>((String[]) numberOfPlayersList.toArray(new String[] {}));
		numberSelection.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				numberOfPlayers = numberSelection.getSelectedIndex() + 3;
				showPlayerSetUp(localPlayerSelectPanel);
				localPlayerSelectPanel.repaint();
				localPlayerSelectPanel.validate();
			}
			
		});
		localPlayerSelectPanel.add(numberSelection, gbc);
		showPlayerSetUp(localPlayerSelectPanel);
//		System.out.println(localPlayerSelectPanel.getComponentCount());
		JButton back = new JButton(messages.getString("back"));
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(playersRolesCache != null)
					playersRolesCache.clear();
				if(playersNamesCache != null)
					playersNamesCache.clear();
				if(playersCPUCache != null)
					playersCPUCache.clear();
				if(playersCPUDifficultyCache != null)
					playersCPUDifficultyCache.clear();
				showMainMenu();
			}
			
		});
		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		localPlayerSelectPanel.add(back, gbc);
		JButton play = new JButton(messages.getString("play"));
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Play has been pushed!");
				try {
					play();
				} catch (RoleErrorException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
				} catch (RuleSetErrorException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage(), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
				} catch (CpuException e3) {
					JOptionPane.showMessageDialog(null, e3.getMessage(), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
				} catch (CPUDifficultyNotSelectedException e4) {
					JOptionPane.showMessageDialog(null, e4.getMessage(), messages.getString("error"), JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		gbc.gridx++;
		localPlayerSelectPanel.add(play, gbc);
		this.getContentPane().add(localPlayerSelectPanel);
		this.validate();
	}

	@SuppressWarnings("unchecked")
	private void showPlayerSetUp(JPanel context) {
//		System.out.println(context.getComponentCount());
		if (context.getComponentCount() > 3)
			context.remove(3);
		JPanel setUpPlayersPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 5, 5);
		JPanel piratePanel = new JPanel(new GridBagLayout());
		JComboBox<String> pirateBox = new JComboBox<String>();
		pirateBox.addItem(messages.getString("select_your_role"));
		pirateBox.addItem(messages.getString("pirate"));
		pirateBox.setToolTipText(messages.getString("pirate_role_tooltip"));
		pirateBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(playersRolesCache == null)
					playersRolesCache = new HashMap<Integer, String>();
				String pirateRole =	playersRolesCache.putIfAbsent(0, (String) pirateBox.getSelectedItem());
				if(pirateRole != null && !pirateRole.equals((String) pirateBox.getSelectedItem()))
					playersRolesCache.replace(0, (String) pirateBox.getSelectedItem());
			}
			
		});
		piratePanel.add(pirateBox, gbc);
//		setUpPlayersPanel.add(pirateBox, gbc);
		JComboBox<String> pirateCpuBox = new JComboBox<String>();
		pirateCpuBox.addItem(messages.getString("cpu"));
		pirateCpuBox.addItem(messages.getString("human"));
		pirateCpuBox.setSelectedIndex(1);
		pirateCpuBox.setToolTipText(messages.getString("cpuBox_tooltip"));
		pirateCpuBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(playersCPUCache == null)
					playersCPUCache = new HashMap<Integer, Boolean>();
				Boolean pirateCPU =	playersCPUCache.putIfAbsent(0, pirateCpuBox.getSelectedIndex() == 0);
				if(pirateCPU != null && !pirateCPU.equals(pirateCpuBox.getSelectedIndex() == 0))
					playersCPUCache.replace(0, pirateCpuBox.getSelectedIndex() == 0);
			}
			
		});
		JComboBox<String> pirateCpuDifficultyBox = new JComboBox<String>();
		pirateCpuDifficultyBox.setName("pirateCpuDifficultyBox");
		pirateCpuDifficultyBox.addItem(messages.getString("selectAIDifficulty"));
		pirateCpuDifficultyBox.addItem(messages.getString("easy"));
		pirateCpuDifficultyBox.addItem(messages.getString("medium"));
		pirateCpuDifficultyBox.addItem(messages.getString("hard"));
		pirateCpuDifficultyBox.setToolTipText(messages.getString("cpuDifficultyBox_tooltip"));
		pirateCpuDifficultyBox.setEnabled(false);
		pirateCpuDifficultyBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(playersCPUDifficultyCache == null)
					playersCPUDifficultyCache = new HashMap<Integer, Integer>();
				playersCPUDifficultyCache.replace(0, pirateCpuDifficultyBox.getSelectedIndex());
			}
			
		});
		pirateCpuDifficultyBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(playersCPUDifficultyCache == null)
					playersCPUCache = new HashMap<Integer, Boolean>();
				Integer pirateCPUDifficulty =	playersCPUDifficultyCache.putIfAbsent(0, pirateCpuDifficultyBox.getSelectedIndex());
				if(pirateCPUDifficulty != null && !pirateCPUDifficulty.equals(pirateCpuDifficultyBox.getSelectedIndex()))
					playersCPUCache.replace(0, pirateCpuDifficultyBox.getSelectedIndex() == 0);
			}
			
		});
		pirateCpuBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pirateCpuDifficultyBox.setEnabled((pirateCpuBox.getSelectedIndex() == 0));
			}
			
		});
		gbc.gridx++;
		piratePanel.add(pirateCpuBox, gbc);
		gbc.gridx++;
		piratePanel.add(pirateCpuDifficultyBox, gbc);
//		setUpPlayersPanel.add(pirateCpuBox, gbc);
		JTextField pirateName = new JTextField();
		pirateName.setToolTipText(messages.getString("name_tooltip"));
		if(playersNamesCache != null && playersNamesCache.containsKey(0))
			pirateName.setText(playersNamesCache.get(0));
		else
			pirateName.setText(messages.getString("player_1_name_placeholder"));
		pirateName.setColumns(20);
		pirateName.setMargin(new Insets(3, 10, 3, 10));
		pirateName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if(pirateName.getText().equals(messages.getString("player_1_name_placeholder")))
					pirateName.setText("");				
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(pirateName.getText().isEmpty())
					pirateName.setText(messages.getString("player_1_name_placeholder"));
				if(playersNamesCache == null)
					playersNamesCache = new HashMap<Integer, String>();
				String tmp = playersNamesCache.putIfAbsent(0, pirateName.getText()) ;
				if(tmp != null && !tmp.equals(pirateName.getText()))
					playersNamesCache.replace(0, pirateName.getText());
//				System.out.println(playersNamesCache.get(0));
			}
			
		});
		gbc.gridx++;
		piratePanel.add(pirateName, gbc);
		gbc.gridx = 0;
		gbc.gridy = 0;
		setUpPlayersPanel.add(piratePanel, gbc);
		for(int i = 1 ; i < numberOfPlayers ; i++) {
			JPanel playerPanel = new JPanel(new GridBagLayout());
			gbc.gridx = 0;
			gbc.gridy = 0;
			JComboBox<String> roleBox = new JComboBox<String>((String[]) (availableRoles.toArray(new String[] {})));
			roleBox.setName("roleBox"+i);
			roleBox.setToolTipText(messages.getString("select_your_role"));
			roleBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(!automatedChange)
//						System.out.println(roleBox.getName() + " is calling dibs!");
						dibs();			
				}

				private void dibs() {
					//System.out.println(numberOfPlayers);
//					System.out.println("Dibbs has been called, changing the boolean!");
					automatedChange = true;
					JPanel playerJ = new JPanel();
					JPanel playerK = new JPanel();
					JComboBox<String> numberJ = new JComboBox<String>();
					JComboBox<String> numberK = new JComboBox<String>();
//					System.out.println("Reading the selected values...");
					for(int j = 1 ; j < numberOfPlayers; j++) {
						//System.out.println(setUpPlayersPanel.getComponent(j*2).getName());
						try {
							playerJ = (JPanel) setUpPlayersPanel.getComponent(j);
							numberJ = (JComboBox<String>) playerJ.getComponent(0);
//							System.out.println("ComboBox #" + j + " has item #" + numberJ.getSelectedIndex()+ " selected. Its value is : " + numberJ.getSelectedItem());
						if(0 != numberJ.getSelectedIndex()) {
//							System.out.println("ComboBox #" + j + " has an item selected (1st if).");
							if(playersRolesCache.containsKey(j)) {
								if(!playersRolesCache.get(j).equals((String) numberJ.getSelectedItem())) {
//									System.out.println("This comboBox had an item selected already, and it changed (2nd if)!");
									for(int k = 1 ; k < numberOfPlayers; k++) {
										//System.out.println(setUpPlayersPanel.getComponent(j*2).getName());
//										System.out.println("K Loop #" + k);
										try {
											playerK = (JPanel) setUpPlayersPanel.getComponent(k);
											numberK = (JComboBox<String>) playerK.getComponent(0);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
//										System.out.println("if k != j with k = " + k + " and j = " + j);
										if (k != j) {
//											System.out.println("k != j");
											try {
												String cache = (String) numberK.getSelectedItem();
												numberK.addItem(playersRolesCache.get(j));
												String selectDefault = numberK.getItemAt(0);
												List<String> tmp = new ArrayList<String>();
												for(int l = 1 ; l < numberK.getItemCount() ; l++) {
													tmp.add(numberK.getItemAt(l));
												}
//												System.out.println("List of options in JComboBox #" + k + " : " + tmp);
												tmp.sort(alphabetical);
												//Collections.sort(tmp);
//												System.out.println(tmp);
												tmp.add(0, selectDefault);
//												System.out.println(tmp);
												numberK.removeAllItems();
												for(String s : tmp) {
													numberK.addItem(s);
												}
												numberK.setSelectedItem(cache);
//												numberK.validate();
											} catch (Exception e1) {
												e1.printStackTrace();
											}
										}									
									}
									playersRolesCache.remove(j);
								}
							}
							playersRolesCache.putIfAbsent(j, (String) numberJ.getSelectedItem());
						}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					for(int j = 1 ; j < numberOfPlayers; j++) {
						try {
							playerJ = (JPanel) setUpPlayersPanel.getComponent(j);
							numberJ = (JComboBox<String>) playerJ.getComponent(0);
							for(Map.Entry<Integer, String> entry : playersRolesCache.entrySet()) {
								if(entry.getKey() != j)
									numberJ.removeItem(entry.getValue());
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					automatedChange = false;
				}
				
			});
//			if(playersRolesCache.containsKey(i))
//				roleBox.setSelectedItem(playersRolesCache.get(i));
			playerPanel.add(roleBox, gbc);
//			setUpPlayersPanel.add(roleBox, gbc);
			JComboBox<String> playerCpuBox = new JComboBox<String>();
			playerCpuBox.setName("cpuBox" + i);
			playerCpuBox.addItem(messages.getString("cpu"));
			playerCpuBox.addItem(messages.getString("human"));
			playerCpuBox.setToolTipText(messages.getString("cpuBox_tooltip"));
			if(playersCPUCache != null && playersCPUCache.containsKey(i) && (playersCPUCache.get(i)))
				playerCpuBox.setSelectedIndex(0);
			else
				playerCpuBox.setSelectedIndex(1);
			int cpt1 = i;
			playerCpuBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(playersCPUCache == null)
						playersCPUCache = new HashMap<Integer, Boolean>();
					Boolean playerCPU =	playersCPUCache.putIfAbsent(cpt1, playerCpuBox.getSelectedIndex() == 0);
					if(playerCPU != null && !playerCPU.equals(playerCpuBox.getSelectedIndex() == 0))
						playersCPUCache.replace(cpt1, playerCpuBox.getSelectedIndex() == 0);
				}
				
			});
			JComboBox<String> cpuDifficultyBox = new JComboBox<String>();
			cpuDifficultyBox.setName("cpuDifficultyBox" + i);
			cpuDifficultyBox.addItem(messages.getString("selectAIDifficulty"));
			cpuDifficultyBox.addItem(messages.getString("easy"));
			cpuDifficultyBox.addItem(messages.getString("medium"));
			cpuDifficultyBox.addItem(messages.getString("hard"));
			cpuDifficultyBox.setToolTipText(messages.getString("cpuDifficultyBox_tooltip"));
			if(playersCPUDifficultyCache != null && playersCPUDifficultyCache.containsKey(i))
				cpuDifficultyBox.setSelectedIndex(playersCPUDifficultyCache.get(i));
			cpuDifficultyBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(playersCPUDifficultyCache == null)
						playersCPUCache = new HashMap<Integer, Boolean>();
					Integer playerCPUDifficulty =	playersCPUDifficultyCache.putIfAbsent(cpt1, cpuDifficultyBox.getSelectedIndex());
					if(playerCPUDifficulty != null && !playerCPUDifficulty.equals(pirateCpuDifficultyBox.getSelectedIndex()))
						playersCPUCache.replace(cpt1, pirateCpuDifficultyBox.getSelectedIndex() == 0);
				}
				
			});
			cpuDifficultyBox.setEnabled(false);
			playerCpuBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					cpuDifficultyBox.setEnabled((playerCpuBox.getSelectedIndex() == 0));
				}
				
			});
			gbc.gridx++;
			playerPanel.add(playerCpuBox, gbc);
			gbc.gridx++;
			playerPanel.add(cpuDifficultyBox, gbc);
//			setUpPlayersPanel.add(playerCpuBox, gbc);
			JTextField playerName = new JTextField();
			playerName.setToolTipText(messages.getString("name_tooltip"));
			if(playersNamesCache != null && playersNamesCache.containsKey(i))
				playerName.setText(playersNamesCache.get(i));
			else
				playerName.setText(messages.getString("player_name_placeholder") + " " + (i+1));
			playerName.setColumns(20);
			playerName.setMargin(new Insets(3, 10, 3, 10));
			int cpt2 = i;
			playerName.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					if(playerName.getText().equals(messages.getString("player_name_placeholder") + " " + (cpt2+1)))
						playerName.setText("");
				}

				@Override
				public void focusLost(FocusEvent e) {
					if(playerName.getText().isEmpty())
						playerName.setText(messages.getString("player_name_placeholder") + " " + (cpt2+1));
					if(playersNamesCache == null) {
						playersNamesCache = new HashMap<Integer, String>();
					}
					String tmp = playersNamesCache.putIfAbsent(cpt2, playerName.getText());
					if(tmp != null && !tmp.equals(playerName.getText()))
						playersNamesCache.replace(cpt2, playerName.getText());
				}
				
			});
			gbc.gridx++;
			playerPanel.add(playerName, gbc);
			gbc.gridx = 0;
			gbc.gridy = i;
			setUpPlayersPanel.add(playerPanel, gbc);
		}
		JComboBox<String> ruleSetBox = new JComboBox<String>();
		ruleSetBox.addItem(messages.getString("select_your_ruleset"));
		ruleSetBox.addItem(messages.getString("alpha_ruleset"));
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(5, 5, 5, 5);
		setUpPlayersPanel.add(ruleSetBox, gbc);
//		System.out.println(setUpPlayersPanel.getComponentCount());
//		System.out.println(setUpPlayersPanel.getComponents());
//		setUpPlayersPanel.getComponent(0).setSize(setUpPlayersPanel.getComponent(4).getSize());
//		System.out.println(setUpPlayersPanel.getComponent(0).getSize());
//		System.out.println(setUpPlayersPanel.getComponent(4).getSize());
		
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		if(context.getComponentCount() < 3)
			context.add(setUpPlayersPanel, gbc);
		else
			context.add(setUpPlayersPanel, gbc, 3);
		for(int i = 0 ; i < numberOfPlayers ; i++) {
//			System.out.println("Context: " + context);
//			System.out.println("Context fourth component: " + context.getComponent(3));
//			System.out.println("Context fourth component's " + i * 2 + " component " + ((JPanel) (context.getComponent(3))).getComponent(i * 2));
			if(playersRolesCache != null && playersRolesCache.containsKey(i) && i < numberOfPlayers)
				((JComboBox<String>) ((JPanel) ((JPanel) (context.getComponent(3))).getComponent(i)).getComponent(0)).setSelectedItem(playersRolesCache.get(i));
		}
	}

	@SuppressWarnings("unchecked")
	protected void play() throws RoleErrorException, RuleSetErrorException, CpuException, CPUDifficultyNotSelectedException {
//		board = new GameBoard();
//		//System.out.println("Board before call to validate :\n" + board.showBoardForDebug());
//		try {
//			board.validate();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		//System.out.println("Board after call to validate :\n" + board.showBoardForDebug());
//		this.getContentPane().remove(0);
//		JPanel gamePanel = new JPanel(new GridBagLayout());
//		JPanel boardPanel = new JPanel(new GridBagLayout());
//		GridBagConstraints gbc = new GridBagConstraints();
//		JButton tileButton;
//		String label = "";
//		for(int i = 0 ; i < 11 ; i++) {
//			for(int j = 0 ; j < 15 ; j++) {
//				switch(board.getBoard()[i][j].getType()) {
//				case OCEAN:
//					label = "O";
//					break;
//				case SHALLOWWATERS:
//					label = "S";
//					break;
//				case COAST:
//					label = "C";
//					break;
//				case PLAINS:
//					label = "P";
//				}
//				tileButton = new JButton(label);
//				gbc.gridx = j ;
//				gbc.gridy = i ;
//				boardPanel.add(tileButton, gbc);
//			}
//		}
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		gamePanel.add(boardPanel, gbc);
		List<Player> players = new ArrayList<Player>();
		JPanel playerSetUpPanel = (JPanel) (((JPanel) (this.getContentPane().getComponent(0))).getComponent(3));
		JComboBox<String> roleI = new JComboBox<String>();
		JComboBox<String> cpuI = new JComboBox<String>();
		JComboBox<String> cpuDifficultyI = new JComboBox<String>();
//		JComboBox<String> cpuBoxI = new JComboBox<String>();
		for(int i = 0 ; i < numberOfPlayers ; i++) {
			JPanel panelI = (JPanel) playerSetUpPanel.getComponent(i);
			roleI = (JComboBox<String>) panelI.getComponent(0);
			cpuI = (JComboBox<String>) panelI.getComponent(1);
			cpuDifficultyI = (JComboBox<String>) panelI.getComponent(2);
			JTextField nameI = (JTextField) panelI.getComponent(3);
			String name = "";
			if(nameI.getText().isEmpty())
				name = messages.getString("player_name_placeholder") + " " + (i+1) + " (" + roleI.getSelectedItem() + ")";
			else {
				if(i == 0 && nameI.getText().equals(messages.getString("player_1_name_placeholder")))
					name = nameI.getText();
				else
					name = nameI.getText() + " (" + roleI.getSelectedItem() + ")";				
			}
//			cpuBoxI = (JComboBox<String>) playerSetUpPanel.getComponent((3 * i) + 1); 
//			boolean isICpu = false;
//			if(cpuBoxI.getSelectedIndex() == 0) {
//				isICpu = true;
//			}
//			else {
//				if(cpuBoxI.getSelectedIndex() != 1) {
//					throw new CpuException("Unknown cpu state");
//				}
//			}
			PlayerRole role;
			if(roleI.getSelectedItem().equals(messages.getString("pirate")))
				role = PlayerRole.PIRATE;
			else {				
				if(roleI.getSelectedItem().equals(messages.getString("cartographer")))
					role = PlayerRole.CARTOGRAPHER;
				else {
					if(roleI.getSelectedItem().equals(messages.getString("doctor")))
						role = PlayerRole.DOCTOR;
					else {
						if(roleI.getSelectedItem().equals(messages.getString("engineer")))
							role = PlayerRole.ENGINEER;
						else {
							if(roleI.getSelectedItem().equals(messages.getString("soldier")))
								role = PlayerRole.SOLDIER;
//								players.add(new HumanPlayer(PlayerRole.SOLDIER, name));
							else
								throw new RoleErrorException(messages.getString("error_roles"));
						}
					}
					
				}
			}
			if(cpuI.getSelectedIndex() == 1) {
				players.add(new HumanPlayer(role, name));
			}
			else {
				switch(cpuDifficultyI.getSelectedIndex()) {
					case 0:
						throw new CPUDifficultyNotSelectedException(messages.getString("error_cpu_difficulty"));
				case 1:
						players.add(new CPUPlayer(role, name, CPUDifficulty.EASY));
						break;
					case 2:
						players.add(new CPUPlayer(role, name, CPUDifficulty.MEDIUM));
						break;
				}
			}
//			System.out.println(players);		
//			}
//			switch((String) numberI.getSelectedItem()) {
//				case pirate:
//					break;
//				case messages.getString(""):
//					break;
//			}
		}
//		System.out.println(playerSetUpPanel.getComponentCount());
		JComboBox<String> ruleSetBox = (JComboBox<String>) playerSetUpPanel.getComponent(numberOfPlayers);
		if(!ruleSetBox.getSelectedItem().equals(messages.getString("alpha_ruleset")))
			throw new RuleSetErrorException(messages.getString("error_ruleset"));
		JComboBox<String> cpuBox;
		JComboBox<String> cpuDifficultyBox;
		JPanel playersPanel = (JPanel) (((JPanel) (this.getContentPane().getComponent(0))).getComponent(3));
		
		for(int i = 0; i < playersPanel.getComponentCount() - 1; i++) {			
			cpuBox = (JComboBox<String>) (((JPanel) (playersPanel.getComponent(i))).getComponent(1));
			cpuDifficultyBox = (JComboBox<String>) (((JPanel) (playersPanel.getComponent(i))).getComponent(2));
			if((cpuBox.getSelectedIndex() == 0) && (cpuDifficultyBox.getSelectedIndex() == 0)) {
				throw new CPUDifficultyNotSelectedException(messages.getString("error_cpu_difficulty"));
			}
		}
		gamePanel = new TreasureGamePanel(currentLocale, players);
		game = new TreasureGameSystemRuleSet01(gamePanel, ResourceBundle.getBundle("GamelogBundle", currentLocale), ResourceBundle.getBundle("MessagesBundle", currentLocale), players, hideTurnPopUp, this, system);
		gamePanel.setSystem(game);
		if(playersRolesCache != null)
			playersRolesCache.clear();
		if(playersNamesCache != null)
			playersNamesCache.clear();
		if(system == null)
			
		system.changeMood(Mood.INGAME);
		this.getContentPane().removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weighty = 1;
		gbc.gridy = 0;
		gbc.gridx = 0;
//		this.getContentPane().setLayout(new GridBagLayout());
		JPanel lobbyPanel = new JPanel(new GridBagLayout());
		lobbyPanel.add(gamePanel, gbc);
		gbc.anchor = GridBagConstraints.SOUTH;
//		gbc.weighty = 1;
		gbc.gridy++;
		JButton backToMenu = new JButton(messages.getString("back_to_menu"));
		backToMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null, messages.getString("back_to_menu_confirm"), messages.getString("confirm_title"), JOptionPane.OK_CANCEL_OPTION);
//				System.out.println(choice);
				if(choice == 0) {
					getContentPane().removeAll();
					system.changeMood(Mood.MENU);
					showMainMenu();
				}
			}
			
		});
		this.repaint();
		lobbyPanel.add(backToMenu, gbc);
		this.getContentPane().add(lobbyPanel);
		this.validate();
		PirateDeckScreen deckBuilder = new PirateDeckScreen(game, currentLocale, hideTurnPopUp, this);
		deckBuilder.setVisible(true);
		deckBuilder.toBack();
		this.toBack();
		deckBuilder.getPopUp().toFront();
		//game.play();
	}

	private void showSettings() {
		detectSettings();
		this.getContentPane().remove(0);
		//repaint();
		JPanel settingsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel settingsLabel = new JLabel(messages.getString("settings"));
		settingsLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1;
		gbc.gridwidth = 2;
		settingsPanel.add(settingsLabel, gbc);
		JLabel languageBox = new JLabel(messages.getString("language"));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		settingsPanel.add(languageBox, gbc);
		gbc.gridx++;
		List<String> languageList = new ArrayList<String>();
		languageList.add(messages.getString("english"));
		languageList.add(messages.getString("french"));
		JComboBox<String> languageSelection = new JComboBox<String>((String[]) languageList.toArray(new String[] {}));
		File settingsFile = new File("./treasureSettings.properties");
		if(settingsFile.exists() && currentLocale.getLanguage().equals("fr"))
			languageSelection.setSelectedIndex(1);
		settingsPanel.add(languageSelection, gbc);
		JLabel volumeLabel = new JLabel(messages.getString("volume_setting"));
		gbc.gridy++;
		gbc.gridx = 0;
		settingsPanel.add(volumeLabel, gbc);
		automatedChange = true;
		JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (volume * 100));
		volumeSlider.setMinorTickSpacing(5);
		volumeSlider.setMajorTickSpacing(20);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setPaintLabels(true);
//		volumeSlider.setExtent(1);
		volumeSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if(!automatedChange) {
//					System.out.println(volumeSlider.getValue());
					volume = 0.01f * volumeSlider.getValue();
					try {
						system.setVolume(volume, system.getClip());
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			}
			
		});
		gbc.gridx++;
		settingsPanel.add(volumeSlider, gbc);
		automatedChange = false;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		JCheckBox hideBox = new JCheckBox(messages.getString("hide_pop_up"));
		hideBox.setSelected(hideTurnPopUp);
		settingsPanel.add(hideBox, gbc);
		JButton back = new JButton(messages.getString("back"));
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				detectSettings();
//				System.out.println(currentLocale.getLanguage());
				try {
					system.setVolume(volume, system.getClip());
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				showMainMenu();
			}
			
		});
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.SOUTH;
		settingsPanel.add(back, gbc);
		gbc.gridx++;
		JButton save = new JButton(messages.getString("save"));
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File settingsFile = new File("./treasureSettings.properties");
				if(!settingsFile.exists()) {
					try {
						settingsFile.createNewFile();
					} catch(IOException exception) {
						exception.printStackTrace();
					}
				} 
				try {
					FileWriter writer = new FileWriter("./treasureSettings.properties");
					//System.out.println("The comboBox has selected item #"+languageSelection.getSelectedIndex());
					switch(languageSelection.getSelectedIndex()) {
						case 0:
							writer.write("language = en\ncountry = US\nvolume = " + volume + "\nhide = " + hideBox.isSelected());
							writer.close();
							break;
						case 1:
							writer.write("language = fr\ncountry = FR\nvolume = " + volume + "\nhide = " + hideBox.isSelected());
							writer.close();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				System.out.println("The chosen Locale was " + currentLocale.toString());
				InputStream input = null;
				userSettings = new Properties();
				try {					
					input = new FileInputStream("./treasureSettings.properties");
					userSettings.load(input);
				} catch(Exception exc1) {
					exc1.printStackTrace();
				}
				//System.out.println(userSettings);
				currentLocale = new Locale(userSettings.getProperty("language"), userSettings.getProperty("country"));
				//System.out.println("Current Locale : "+currentLocale);
				messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
			}
			
		});
		settingsPanel.add(save, gbc);
		this.getContentPane().add(settingsPanel);
		//System.out.println("before repaint");
		this.validate();
		this.repaint();
	}
	
	public void showError(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage);
	}
}
