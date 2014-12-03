import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

import java.io.*;

import javax.sound.sampled.*;

/**
 * Ruoyu Zhi
 * CS 110
 * Professor:Jackie Horton
 * This is a GUI program that simulates a casino war game.
 * We have two players in this game, one is computer which is the dealer, the other is the user.
 * @author Ramon
 *
 */
public class GUIwar extends JFrame
{	
	private static final long serialVersionUID = 1L;
	/**
	 * @param startPanel panel at the bottom that contains three buttons:deal, war and fold,deal and the other two never show up at the same time
	 */
	private static JPanel startPanel = new JPanel();
	private static JButton dealButton = new JButton("Deal");
	private static JButton warButton = new JButton("War!");
	private static JButton foldButton = new JButton("Fold");
	
	/**
	 * @param wagerPanel panel shows at the top center of the window, allowing user to select wager
	 */
	private static JPanel wagerPanel = new JPanel();
	private static JLabel wagerLabel = new JLabel("<html><font color='white'>What's the wager for this round?</font></html>");
	private static JComboBox wagerBox;
	private static String[] wagerList = {"1","2","3","4","5","6","7","8","9",};
	
	/**
	 * @param cardPanel panel that holds all the card labels
	 */
	private static JPanel cardPanel = new JPanel();
	private static JLabel dealerHandLabel;
	private static JLabel dealerStatus;
	private static JLabel playerHandLabel;
	private static JLabel playerStatus;
	private static JLabel roundResult;
	private static JLabel versusGifLabel;
	private static JLabel explosionGifLabel;
	private static ArrayList<JLabel> playerCardsOnDesk = new ArrayList<>();
	private static ArrayList<JLabel> dealerCardsOnDesk = new ArrayList<>();
	private static JCheckBox soundSwitch;
	
	/**
	 * @param menuBar menu
	 */
	private static JMenuBar menuBar;
	private static JMenu fileMenu;
	private static JMenuItem exitItem;
	private static JMenuItem helpItem;
	private static JMenuItem newGameItem;
	
	/**@param deck deck of cards*/
	private static Deck deck;
	/**@param playerHand cards that player holds*/
	private static Deck playerHand;
	/**@param dealerHand cards that dealer holds*/
	private static Deck dealerHand;
	/**@param playerCard card that the player deals out*/
	private static Card playerCard;
	/**@param dealerCard card that the dealer deals out*/
	private static Card dealerCard;
	/**@param cardsOnDesk totals cards dealt by both the player and dealer within one round*/
	private static ArrayList<Card> cardsOnDesk = new ArrayList<>();
	
	/**
	 * Establishes main GUI,consists of three parts: startPanel, wagerPaner and cardPanel.
	 * plays music when opening
	 */
	public GUIwar() 
	{
		//set basic property of the window
		setTitle("War");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//setBackground(Color.RED);
		Color color = new Color(0,102,0);
		getContentPane().setBackground(color);
		
		//build all parts
		buildMenuBar();
		startPanel();
		wagerPanel();
		cardPanel();
		
		setSize(800, 600);
		
		//set parts to opaque so the background color can expose
		startPanel.setOpaque(false);
		wagerPanel.setOpaque(false);
		cardPanel.setOpaque(false);
		
		//add all the panels to the window
		add(cardPanel,BorderLayout.CENTER);
		add(startPanel,BorderLayout.SOUTH);
		add(wagerPanel,BorderLayout.NORTH);
		
		//plays sounds denoting the game starts
		if(soundSwitch.isSelected())
		{
			try 
			{
				String soundName = "./sounds/gameStart.wav";    
				AudioInputStream audioInputStream;
				audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
	    		clip.start();
			} 
			catch (UnsupportedAudioFileException | IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			catch (LineUnavailableException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		setVisible(true);
		setResizable(false);
	}
	
	/**
	 * method that builds the menu bar
	 */
	private void buildMenuBar()
	{
		menuBar = new JMenuBar();
		
		buildFileMenu();
		
		menuBar.add(fileMenu);
		
		setJMenuBar(menuBar);
	}
	
	/**
	 * method that build the file menu,consists New Game button, Help button and Exit button
	 */
	private void buildFileMenu()
	{
		//menuBar item: File
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		//three items under "File": New Game, Help, Exit
		newGameItem = new JMenuItem("New Game");
		newGameItem.setMnemonic(KeyEvent.VK_N);
		newGameItem.addActionListener(new NewGameListener());
		
		helpItem = new JMenuItem("Help");
		helpItem.setMnemonic(KeyEvent.VK_H);
		helpItem.addActionListener(new HelpListener());
		
		exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ExitListener());
		
		//add all the items to menu
		fileMenu.add(newGameItem);
		fileMenu.add(helpItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
	}
	
	/**
	 * build startPanel that contains three buttons:deal, war and fold,deal and the other two never show up at the same time
	 */
	public void startPanel()
	{
		startPanel.removeAll();
		
		startPanel.setLayout(new FlowLayout());
		
		//add listeners to the button
		warButton.addActionListener(new warButtonListener());
		dealButton.addActionListener(new dealButtonListener());
		foldButton.addActionListener(new foldButtonListener());
		
		//by default, deal card as beginning, war and fold cannot be seen
		dealButton.setVisible(true);
		warButton.setVisible(false);
		foldButton.setVisible(false);
		
		//add buttons to startPanel
		startPanel.add(dealButton);
		startPanel.add(foldButton);
		startPanel.add(warButton);
	}
	
	/**
	 * build wagerPanel, allowing user to select wager
	 */
	public void wagerPanel()
	{
		wagerPanel.removeAll();
		
		wagerPanel.setLayout(new FlowLayout());
		
		wagerPanel.add(wagerLabel);
		wagerBox = new JComboBox(wagerList);
		wagerPanel.add(wagerBox);
	}
	
	/**
	 * build cardPanel that holds all the card labels
	 */
	public void cardPanel()
	{
		cardPanel.removeAll();
		
		//no default layout, use absolute position to place the labels
		cardPanel.setLayout(null);
		
		//indicate how many cards left on the dealer's side
		dealerStatus = new JLabel("<html><font color='white'>dealer has " + dealerHand.getSize() + " cards left</font></html>");
		cardPanel.add(dealerStatus);
		dealerStatus.setBounds(20,0,150,15);
		
		//indicate the dealer's card pile
		ImageIcon image1 = new ImageIcon("./cardPics/backPile.jpg");
		dealerHandLabel = new JLabel(image1);
		cardPanel.add(dealerHandLabel);
		dealerHandLabel.setBounds(20, 20, 143, 150);
		
		//show the result of each round
		roundResult = new JLabel("");
		cardPanel.add(roundResult);
		roundResult.setBounds(20, 200, 400, 60);
		
		//indicate how many cards left on the player's side
		playerStatus = new JLabel("<html><font color='white'>player has " + playerHand.getSize() + " cards left</font></html>");
		cardPanel.add(playerStatus);
		playerStatus.setBounds(20,280,150,15);
		
		//indicate the dealer's card pile
		playerHandLabel = new JLabel(image1);
		cardPanel.add(playerHandLabel);
		playerHandLabel.setBounds(20, 300, 143, 150);
		
		//enable or disable sound effect
		soundSwitch = new JCheckBox("Sound effect",true);
		cardPanel.add(soundSwitch);
		soundSwitch.setBounds(670,0,100,25);
		
		//versus gif
		image1 = new ImageIcon("versus.gif");
		versusGifLabel = new JLabel(image1);
		cardPanel.add(versusGifLabel);
		versusGifLabel.setBounds(300,175,200,100);
		versusGifLabel.setVisible(true);
		
		//explosion gif
		image1 = new ImageIcon("explosion.gif");
		explosionGifLabel = new JLabel(image1);
		cardPanel.add(explosionGifLabel);
		explosionGifLabel.setBounds(300,185,200,100);
		explosionGifLabel.setVisible(false);
	}
	
	/**
	 * deal button listener
	 * @author Ramon
	 *
	 */
	private class dealButtonListener implements ActionListener
	{
		/**
		 * method that implements the ActionListener interface that responds to the click of deal button, 
		 * collect cards on the desk of last round and give them to the winner, 
		 * deal card to both player and determines who is the winner for this round.
		 * It also detects cards out of bounds exception and determines who is the winner of the game and plays according sound
		 */
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				playerStatus.setText("<html><font color='white'>player has " + playerHand.getSize() + " cards left</font></html>");
				dealerStatus.setText("<html><font color='white'>dealer has " + dealerHand.getSize() + " cards left</font></html>");
				
				//hide explosion gif and show versus gif
				versusGifLabel.setVisible(true);
				explosionGifLabel.setVisible(false);
				
				//remove all the cards left on the desk in the GUI
				while(dealerCardsOnDesk.size() != 0)
				{
					cardPanel.remove(dealerCardsOnDesk.remove(0));
				}
				
				while(playerCardsOnDesk.size() != 0)
				{
					cardPanel.remove(playerCardsOnDesk.remove(0));
				}
				
				cardPanel.repaint();
				
				//deals card to dealer
				Thread.sleep(50);
				dealerCard = dealerHand.dealCard();
				cardsOnDesk.add(dealerCard);
				ImageIcon image1 = new ImageIcon("./cardPics/" + dealerCard.getPicName());
				JLabel label1 = new JLabel(image1);
				dealerCardsOnDesk.add(label1);
				cardPanel.add(label1);
				label1.setBounds(200, 20, 105, 150);
				//plays card dealing sound
				if(soundSwitch.isSelected())
				{
					try 
					{
						String soundName = "./sounds/cardPlace1.wav";    
						AudioInputStream audioInputStream;
						audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
			    		clip.start();
					} 
					catch (UnsupportedAudioFileException | IOException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					catch (LineUnavailableException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				//deal card to player
				Thread.sleep(50);
				playerCard = playerHand.dealCard();
				cardsOnDesk.add(playerCard);
				ImageIcon image2 = new ImageIcon("./cardPics/" + playerCard.getPicName());
				JLabel label2 = new JLabel(image2);
				playerCardsOnDesk.add(label2);
				cardPanel.add(label2);
				label2.setBounds(200, 300, 105, 150);
				
				//update both players' number of cards left
				playerStatus.setText("<html><font color='white'>player has " + playerHand.getSize() + " cards left</font></html>");
				dealerStatus.setText("<html><font color='white'>dealer has " + dealerHand.getSize() + " cards left</font></html>");
				
				//judge the winner and give hime all the cards he wan(the GUI stays the same)
				if(playerCard.compareTo(dealerCard) > 0)
				{
					roundResult.setText("<html><HTML><body style=color:yellow> Dealer's card is " + dealerCard + 
										" <br> Player's card is " + playerCard + 
										" <br> Player wins the round.</body></html>");
					System.out.println("player's card is " + playerCard);
					System.out.println("dealer's card is " + dealerCard);
					while(cardsOnDesk.size() != 0)
					{
						//System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
						playerHand.addToBottom(cardsOnDesk.remove(0));
					}
					
					System.out.println("player has " + playerHand.getSize() + " cards left");
					System.out.println("dealer has " + dealerHand.getSize() + " cards left");
				}
				else if(playerCard.compareTo(dealerCard) < 0)
				{
					roundResult.setText("<html><HTML><body style=color:yellow> Dealer's card is " + dealerCard + 
							" <br> Player's card is " + playerCard + 
							" <br> Dealer wins the round.</body></html>");
					System.out.println("player's card is " + playerCard);
					System.out.println("dealer's card is " + dealerCard);
					while(cardsOnDesk.size() != 0)
					{
						//System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
						dealerHand.addToBottom(cardsOnDesk.remove(0));
					}
					
					System.out.println("player has " + playerHand.getSize() + " cards left");
					System.out.println("dealer has " + dealerHand.getSize() + " cards left");
				}
				//if a tie occurs, war and fold button shows and deal button goes away, let the user choose
				else
				{
					roundResult.setText("<html><HTML><body style=color:yellow> Dealer's card is " + dealerCard + 
							" <br> Player's card is " + playerCard + 
							" <br> Go into a war?</body></html>");
					
					foldButton.setVisible(true);
					warButton.setVisible(true);
					dealButton.setVisible(false);
				}
			}
			//exception to determine if either player has run out of cards and plays according sound to celebrate
			catch(OutOfCardsException e1)
			{
				if(playerHand.getSize() <= 0)
				{
					cardPanel.remove(playerHandLabel);
					cardPanel.repaint();
					if(soundSwitch.isSelected())
					{
						try 
						{
							String soundName = "./sounds/gameLose.wav";    
							AudioInputStream audioInputStream;
							audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
				    		clip.start();
						} 
						catch (UnsupportedAudioFileException | IOException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						} 
						catch (LineUnavailableException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(null,"Player is out of cards,dealer wins!");
				}
				else
				{
					cardPanel.remove(dealerHandLabel);
					cardPanel.repaint();
					if(soundSwitch.isSelected())
					{
						try 
						{
							String soundName = "./sounds/gameWin.wav";    
							AudioInputStream audioInputStream;
							audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
				    		clip.start();
						} 
						catch (UnsupportedAudioFileException | IOException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						} 
						catch (LineUnavailableException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(null,"Dealer is out of cards,player wins!");
				}
			} 
			//exception to handle Thread.sleep exceptions
			catch (InterruptedException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * war button listener
	 * @author Ramon
	 *
	 */
	private class warButtonListener implements ActionListener
	{
		/**
		 * war button only shows up when there is a tie, if user chose to go into a war, certain amount of card (specified by the wagerBox) will be dealt to each player
		 * and compare the next faced up card, the one with the higher card takes all on the desk
		 * It also detects cards out of bounds exception and determines who is the winner of the game and plays according sound
		 */
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				//update both players' number of cards left
				playerStatus.setText("<html><font color='white'>player has " + playerHand.getSize() + " cards left</font></html>");
				dealerStatus.setText("<html><font color='white'>dealer has " + dealerHand.getSize() + " cards left</font></html>");
				
				//show versus gif
				versusGifLabel.setVisible(false);
				explosionGifLabel.setVisible(true);
				
				ImageIcon backImage = new ImageIcon("./cardPics/back.jpg");
				//deal cards as wagers(at backstage, GUI stays the same)
				for(int i = 0;i <= wagerBox.getSelectedIndex();i++)
				{
					playerCard = playerHand.dealCard();
					cardsOnDesk.add(playerCard);
					dealerCard = dealerHand.dealCard();
					cardsOnDesk.add(dealerCard);
				}
				//deal cards on the GUI,also plays sound while dealing
				for(int i = 0;i <= wagerBox.getSelectedIndex();i++)
				{
					JLabel backLabel = new JLabel(backImage);
					cardPanel.add(backLabel);
					backLabel.setBounds(200 + dealerCardsOnDesk.size()*25, 20, 105, 150);		
					dealerCardsOnDesk.add(backLabel);
					cardPanel.setComponentZOrder(backLabel, 0);
					cardPanel.repaint();
					Thread.sleep(120);
					
					if(soundSwitch.isSelected())
					{
						try 
						{
							String soundName = "./sounds/cardPlace1.wav";    
							AudioInputStream audioInputStream;
							audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
				    		clip.start();
						} 
						catch (UnsupportedAudioFileException | IOException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						catch (LineUnavailableException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				for(int i = 0;i <= wagerBox.getSelectedIndex();i++)
				{
					JLabel backLabel = new JLabel(backImage);
					cardPanel.add(backLabel);
					backLabel.setBounds(200 + playerCardsOnDesk.size()*25, 300, 105, 150);
					cardPanel.setComponentZOrder(backLabel, 0);
					playerCardsOnDesk.add(backLabel);
					cardPanel.repaint();
					Thread.sleep(120);
				}
				
				//deal cards to be compared(on backstage, GUI stays the same)
				playerCard = playerHand.dealCard();
				cardsOnDesk.add(playerCard);
				dealerCard = dealerHand.dealCard();
				cardsOnDesk.add(dealerCard);
				
				//deal cards to be compared on GUI and plays card dealing sound
				ImageIcon image1 = new ImageIcon("./cardPics/" + dealerCard.getPicName());
				System.out.println(dealerCard.getPicName());
				JLabel label1 = new JLabel(image1);
				cardPanel.add(label1);
				label1.setBounds(200 + dealerCardsOnDesk.size()*25, 20, 105, 150);
				dealerCardsOnDesk.add(label1);
				cardPanel.setComponentZOrder(label1, 0);
				
				if(soundSwitch.isSelected())
				{
					try 
					{
						String soundName = "./sounds/cardPlace1.wav";    
						AudioInputStream audioInputStream;
						audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
			    		clip.start();
					} 
					catch (UnsupportedAudioFileException | IOException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					catch (LineUnavailableException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				ImageIcon image2 = new ImageIcon("./cardPics/" + playerCard.getPicName());
				System.out.println(playerCard.getPicName());
				JLabel label2 = new JLabel(image2);
				cardPanel.add(label2);
				label2.setBounds(200 + playerCardsOnDesk.size()*25, 300, 105, 150);
				playerCardsOnDesk.add(label2);
				cardPanel.setComponentZOrder(label2, 0);
				
				//update both players' number of cards left
				playerStatus.setText("<html><font color='white'>player has " + playerHand.getSize() + " cards left</font></html>");
				dealerStatus.setText("<html><font color='white'>dealer has " + dealerHand.getSize() + " cards left</font></html>");
				
				//judge the winner of the war and give him all the cards he wan(the GUI stays the same)
				if(playerCard.compareTo(dealerCard) > 0)
				{
					
					System.out.println("player's card is " + playerCard);
					System.out.println("dealer's card is " + dealerCard);
					
					//give all the cards on desk to the winner
					while(cardsOnDesk.size() != 0)
					{
						//System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
						playerHand.addToBottom(cardsOnDesk.remove(0));
					}
					
					//plays sound to celebrate
					if(soundSwitch.isSelected())
					{
						try 
						{
							String soundName = "./sounds/warWin.wav";    
							AudioInputStream audioInputStream;
							audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
				    		clip.start();
						} 
						catch (UnsupportedAudioFileException | IOException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						} 
						catch (LineUnavailableException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						}
					}
					
					System.out.println("player has " + playerHand.getSize() + " cards left");
					System.out.println("dealer has " + dealerHand.getSize() + " cards left");
					
					//update round result
					roundResult.setText("<html><HTML><body style=color:yellow> Dealer's card is " + dealerCard + 
							" <br> Player's card is " + playerCard + 
							" <br> Player wins the war.</body></html>");
					
					//set war and fold to invisible and deal to visible again to continue the game
					foldButton.setVisible(false);
					warButton.setVisible(false);
					dealButton.setVisible(true);
				}
				//dealer is larger
				else if(playerCard.compareTo(dealerCard) < 0)
				{
					//hide "VS" gif and show explosion gif
					versusGifLabel.setVisible(false);
					explosionGifLabel.setVisible(true);
					
					System.out.println("player's card is " + playerCard);
					System.out.println("dealer's card is " + dealerCard);
					
					//give all the cards on desk to the winner
					while(cardsOnDesk.size() != 0)
					{
						//System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
						dealerHand.addToBottom(cardsOnDesk.remove(0));
					}
					System.out.println("player has " + playerHand.getSize() + " cards left");
					System.out.println("dealer has " + dealerHand.getSize() + " cards left");
					
					//plays sound to celebrate
					if(soundSwitch.isSelected())
					{
						try 
						{
							String soundName = "./sounds/warLose.wav";    
							AudioInputStream audioInputStream;
							audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
				    		clip.start();
						} 
						catch (UnsupportedAudioFileException | IOException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						} 
						catch (LineUnavailableException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						}
					}
					
					//update round result
					roundResult.setText("<html><HTML><body style=color:yellow> Dealer's card is " + dealerCard + 
							" <br> Player's card is " + playerCard + 
							" <br> Dealer wins the war.</body></html>");
					
					//set war and fold to invisible and deal to visible again to continue the game
					foldButton.setVisible(false);
					warButton.setVisible(false);
					dealButton.setVisible(true);
				}
				//when there is a tie in a war, just refresh the result and buttons stays the same and let user to choose to go into a war inside a war or give up
				else
				{
					roundResult.setText("<html><HTML><body style=color:yellow> Dealer's card is " + dealerCard + 
							" <br> Player's card is " + playerCard + 
							" <br> Go into a war?</body></html>");
				}
			}
			//exception to determine if either player has run out of cards and plays according sound to celebrate
			catch(OutOfCardsException e1)
			{
				if(playerHand.getSize() <= 0)
				{
					cardPanel.remove(playerHandLabel);
					cardPanel.repaint();
					if(soundSwitch.isSelected())
					{
						try 
						{
							String soundName = "./sounds/gameLose.wav";    
							AudioInputStream audioInputStream;
							audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
				    		clip.start();
						} 
						catch (UnsupportedAudioFileException | IOException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						} 
						catch (LineUnavailableException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(null,"Player is out of cards,dealer wins!");
				}
				else
				{
					cardPanel.remove(dealerHandLabel);
					cardPanel.repaint();
					if(soundSwitch.isSelected())
					{
						try 
						{
							String soundName = "./sounds/gameWin.wav";    
							AudioInputStream audioInputStream;
							audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
				    		clip.start();
						} 
						catch (UnsupportedAudioFileException | IOException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						} 
						catch (LineUnavailableException e11) 
						{
							// TODO Auto-generated catch block
							e11.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(null,"Dealer is out of cards,player wins!");
				}
			} 
			catch (InterruptedException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * fold button only shows up when there is a tie, if user chose to fold, he will lose half of his cards on desk and the dealer will take all of the others.
	 * @author Ramon
	 *
	 */
	private class foldButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//give back dealer's cards on the desk(backstage, GUI stays the same)
			for(int i = 0;i < dealerCardsOnDesk.size();i++)
			{
				//System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
				dealerHand.addToBottom(cardsOnDesk.remove(0));
			}
			//give back half of player's cards on the desk(backstage, GUI stays the same)
			for(int i = 0;i < playerCardsOnDesk.size()/2;i++)
			{
				//System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
				playerHand.addToBottom(cardsOnDesk.remove(0));
			}
			//give the other half of player's cards on desk to dealer(backstage, GUI stays the same)
			for(int i = playerCardsOnDesk.size()/2;i < playerCardsOnDesk.size();i++)
			{
				//System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
				dealerHand.addToBottom(cardsOnDesk.remove(0));
			}
			
			//remove all the cards on desk(just GUI, backstage stays the same)
			while(dealerCardsOnDesk.size() != 0)
			{
				cardPanel.remove(dealerCardsOnDesk.remove(0));
			}
			
			while(playerCardsOnDesk.size() != 0)
			{
				cardPanel.remove(playerCardsOnDesk.remove(0));
			}
			cardPanel.repaint();
			
			//update both players' number of cards left
			playerStatus.setText("<html><font color='white'>player has " + playerHand.getSize() + " cards left</font></html>");
			dealerStatus.setText("<html><font color='white'>dealer has " + dealerHand.getSize() + " cards left</font></html>");
			
			//set war and fold to invisible and deal to visible again to continue the game
			foldButton.setVisible(false);
			warButton.setVisible(false);
			dealButton.setVisible(true);
		}
	}
	
	/**
	 * New Game item listener, restore initial game status and refresh the GUI
	 * @author Ramon
	 *
	 */
	private class NewGameListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		//initialize both players' deck to original status
    		deck = new Deck(true);
    		deck.shuffle();
    		playerHand = new Deck();
    		dealerHand = new Deck();
    		
    		//evenly hand out the cards to the player and dealer/computer
    		for(int i = 0;i <= 51;i++)
    		{
    			if(i <= 25)
    				playerHand.addToBottom(deck.dealCard());
    			else
    				dealerHand.addToBottom(deck.dealCard());
    		}
    		
    		cardsOnDesk.clear();
    		
    		//clear cardPanel on GUI
    		while(playerCardsOnDesk.size() != 0)
			{
				//System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
				cardPanel.remove(playerCardsOnDesk.remove(0));
			}
    		while(dealerCardsOnDesk.size() != 0)
			{
				//System.out.println("There are " + cardsOnDesk.size() + " cards on desk");
				cardPanel.remove(dealerCardsOnDesk.remove(0));
			}
    		
    		//plays the sound denoting deck's been shuffled
    		if(soundSwitch.isSelected())
    		{
	    		try 
				{
					String soundName = "./sounds/cardFan1.wav";    
					AudioInputStream audioInputStream;
					audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
		    		clip.start();
				} 
				catch (UnsupportedAudioFileException | IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
	    		catch (LineUnavailableException e1) 
	    		{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
    		
    		//update both players' number of cards left
    		playerStatus.setText("<html><font color='white'>player has " + playerHand.getSize() + " cards left</font></html>");
			dealerStatus.setText("<html><font color='white'>dealer has " + dealerHand.getSize() + " cards left</font></html>");
    		
			//set round result text to blank
			roundResult.setText("");
			
			//set buttons as they originally were
			foldButton.setVisible(false);
			warButton.setVisible(false);
			dealButton.setVisible(true);
			
			//refresh the cardPanel
    		cardPanel.repaint();
    	}
    }
	
	/**
	 * Exit menu item listener, when clicked, program will exit
	 * @author Ramon
	 *
	 */
	private class ExitListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		System.exit(0);
    	}
    }
	
	/**
	 * Help menu item listener, when clicked, a dialog window with instructions will pop up 
	 * @author Ramon
	 *
	 */
	private class HelpListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String str = "GAME PLAY:\n"
					+ "One card each is dealt to a dealer and to a player. If the player's card is higher, he or she wins the wager they bet. \n"
					+ "However, if the dealer's card is higher, the player loses their bet.The cards are ranked in the same way that cards in poker games\n"
					+ "are ranked, with aces being the highest cards.\n"
					+ "TIE:\n"
					+ "A tie occurs when the dealer and the player each have cards of the same rank. In a tie situation, the player has two options:war\n"
					+ "or fold.\n"
					+ "WAR:\n"
					+ "If you want to go into a war during a tie,you deal at least one card face down(or by selected in the box) and another card face up\n"
					+ " to be compared,dealer does the same. Winner of the the face up card takes all."
					+ "FOLD:\n"
					+ "If you want to give up during a tie, you get half of your cards on desk(round down) back and dealer takes the others."
					+ "WIN OR LOSE:\n"
					+ "Once a player runs out of cards, the other player wins.";
			JOptionPane.showMessageDialog(null,str);
		}
	}
	
	/**
	 * main method, Initialize the deck, call the GUI builder
	 * @param args
	 */
	public static void main(String[] args)
	{
		//initialize the deck and both players' deck
		deck = new Deck(true);
		deck.shuffle();
		playerHand = new Deck();
		dealerHand = new Deck();
		
		//evenly hand out the cards to the player and dealer/computer
		for(int i = 0;i <= 51;i++)
		{
			if(i <= 25)
				playerHand.addToBottom(deck.dealCard());
			else
				dealerHand.addToBottom(deck.dealCard());
		}
		//set up a new GUI
		new GUIwar();
	}
}