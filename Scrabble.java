
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

/**
 * The main game class where all graphics and mouse events occurs
 * @author Jashanjot S Sohanpal, Evan Cao, Jason Wang
 *
 */
public class Scrabble extends JFrame
{
	// The current location of the piece
	public static final int ON_BOARD = 0;
	public static final int ON_BAG = 1;
	public static final int ON_PLAYER = 2;

	// Graphics numbers pertaining to the board
	final static int PIXELS_OF_BOX = 42;
	final static int TOP_LEFT_BOARD = 65;
	
	// Variable to show classic or fancy tile graphics
	static boolean tileGraphics;
	
	// Variable to check whether to play sound or not
	private boolean playSound;

	 // Bag, board, and player variables
	private Bag bag;
	private Board board;
	private Player player1;
	private AI computer;
	private Player player2;
	
	// Variables for who's turn, number of moves done and how many skips have occured
	private int turn;
	private int moves;
	private int skips = 0;
	
	// Variable to keep track of tiles to swap
	ArrayList<Piece> forSwap;

	// TextArea to display any messages to the player
	private JTextArea txt;
	
	// Declaring image variables
	private Image credits;
	private Image pvp;	
	private Image pvcH;	
	private Image pvcE;	
	private Image mainGlow;	
	private Image swapS;
	private Image swapC;
	private Image swapMenu;
	private Image background;
	private Image swapP;
	private Image newP;
	private Image submitP;
	private Image clearP;
	private Image passP;
	private Image dictionaryP;
	private Image resignInstruc;	
	private Image instrucP;	
	private Image resignMenuN;	
	private Image resignMenuY;	
	private Image resignP;	
	private Image resignMenu;
	private Image quitMenu;	
	private Image quitP;	
	private Image menuP;	
	private Image mainMenu;
	private Image modeSelect;
	private Image mainButtons;	
	private Image difficulty;
	private Image vsPlayerP;	
	private Image vsComputerP;	
	private Image startP;	
	private Image helpP;	
	private Image backP;	
	private Image creditP;	
	private Image easyP;	
	private Image hardP;	
	private Image torch1;	
	private Image torch2;
	private Image instruc2;	
	private Image instruc1;	
	private Image instruc3;
	private Image instruc4;	
	private Image instruc5;	
	private Image instruc6;	
	private Image ins1;
	private Image ins2;
	private Image ins3;
	private Image ins4;
	private Image ins5;
	private Image ins6;
	private Image insClose;
	private Image showSpecial;
	private Image hideSpecial;
	private Image musicP;
	private Image musicS;
	private Image soundY;
	private Image soundN;
	private int pressed;	
	private int mainMenuPress;	
	
	// Declaring variables for audio
	private AudioClip bgm;
    private AudioClip place;
    private AudioClip button;
    private AudioClip win;
 

    // The piece clicked on
	Piece selectedPiece;
	
	// Dragging points
	Point lastPoint;
	Point previousPoint;
	
	// Keeps track of whether the piece is from the board
	private static boolean fromBoard;
	
	// Keeps track of whether the player clicked resign
	private boolean resignMode;
	
	// Keeps track of the mode of the game (vs AI, vs Player)
	private int mode;
	
	// Keeps track of the AI difficulty
	private boolean strength;
	
	// Keeps track of whether it is on the menu 
	private boolean menuMode;
	
	// Keeps track of what menu buttons to show
	private int mainButtonMode;
	
	// Keeps track of whether it is showing instructions
	private boolean instrucMode;
	
	// Keeps track of which instruction page to show
	private int insPage;	
	
	// Keeps track of whether to show credits
	private boolean showCredits;
	
	// Keeps track of whether to play music or not
	private boolean playMusic;

	/**
     * Creates instance of game
     */
	public Scrabble()
	{
		// Create Main Frame
		super("Scrabble");
		this.getContentPane().setPreferredSize(new Dimension(1280, 768));
		this.setResizable(false);

		// Initializes game
		gameInitialize();
		BoardArea myBoard = new BoardArea();
		this.add(myBoard);	
	}

	 /**
     * Creates a new game
     */
	private void newGame()
	{
		 // Makes instances of classes for the game to use
		bag = new Bag();
		board = new Board();
		player1 = new Player();
		
		// Creates second player instance based on mode
		if (mode == 1)
			computer = new AI();
		else if (mode == 2)
			player2 = new Player();
			
			
		// Resets variables
		moves = 0;
		turn = 1;
		fromBoard = false;

		selectedPiece = null;
		lastPoint = new Point();
		previousPoint = new Point();
		skips = 0;
		forSwap = new ArrayList<Piece>();
		
		resignMode = false;
		instrucMode = false;

		// Fills the players rack and displays opening message
		whichPlayer().fillPieces(bag, myGame);
		txt.setText("");
		txt.append("It is your turn. \n");

	}

	/**
	 * Turns the music on or off
	 * @param play
	 * 			true play music
	 * 			false stop music
	 */
	private void playMusic(boolean play)
	{
		if (play)
		{
			bgm.loop();
			playMusic = true;
		}
		else
		{
			bgm.stop();
			playMusic = false;
		}
	}
	
	/**
	 * Gets the URL of the sound file
	 * @param fileName the name of the file
	 * @return
	 */
    private URL getCompleteURL (String fileName)
    {
        try
        {
            return new URL ("file:" + System.getProperty ("user.dir") + "\\" + fileName);
        }
        catch (MalformedURLException e)
        {
            System.err.println (e.getMessage ());
        }
        return null;
    }
	
    // Variable to hold the dictionary
	private Dictionary dictionary;

	/**
    * Initializes game for first time
    */
	private void gameInitialize()
	{
		// Creates anagram dictionary instance
		dictionary = new Dictionary();
		
		// Sets initial variables to show menu screen before game
		menuMode = true;
		mainButtonMode = 0;
		mainMenuPress = -1;
		skips = 3;
		mode = 0;
		resignMode = false;
		instrucMode = false;
		instrucMode = false;
		showCredits = false;
		insPage = 1;
		tileGraphics = true;
		playMusic = true;
		playSound = true;
		
		// Reads in audio files
		place = Applet.newAudioClip (getCompleteURL ("Game Resources/place.wav"));  
        button = Applet.newAudioClip (getCompleteURL ("Game Resources/button.wav"));
        win = Applet.newAudioClip (getCompleteURL ("Game Resources/win.wav"));   
		bgm = Applet.newAudioClip (getCompleteURL ("Game Resources/bgm.wav"));
		bgm.loop();

		// Reads in image files
		soundN = new ImageIcon("Game Resources/layout/soundN.png").getImage(); 
		soundY = new ImageIcon("Game Resources/layout/soundY.png").getImage();      
		musicS = new ImageIcon("Game Resources/layout/stopMusic.png").getImage(); 		
		musicP = new ImageIcon("Game Resources/layout/playMusic.png").getImage();        
        credits = new ImageIcon("Game Resources/layout/credits.png")
		.getImage(); 				
		showSpecial = new ImageIcon("Game Resources/layout/showSpecial.png")
		.getImage(); 		
		hideSpecial = new ImageIcon("Game Resources/layout/hideSpecial.png")
		.getImage(); 		
		pvcH = new ImageIcon("Game Resources/layout/pvcH.png")
		.getImage(); 	
		pvcE = new ImageIcon("Game Resources/layout/pvcE.png")
		.getImage(); 	
		pvp = new ImageIcon("Game Resources/layout/pvp.png")
		.getImage(); 		
		insClose = new ImageIcon("Game Resources/layout/insClose.png")
		.getImage(); 	
		instruc2 = new ImageIcon("Game Resources/layout/instruc2.png")
		.getImage(); 			
		instruc1 = new ImageIcon("Game Resources/layout/instruc1.png")
		.getImage();			
		instruc3 = new ImageIcon("Game Resources/layout/instruc3.png")
		.getImage();			
		instruc4 = new ImageIcon("Game Resources/layout/instruc4.png")
		.getImage();			
		instruc5 = new ImageIcon("Game Resources/layout/instruc5.png")
		.getImage();			
		instruc6 = new ImageIcon("Game Resources/layout/instruc6.png")
		.getImage();			
		ins1 = new ImageIcon("Game Resources/layout/ins1.png")
		.getImage();			
		ins2 = new ImageIcon("Game Resources/layout/ins2.png")
		.getImage();
		ins3 = new ImageIcon("Game Resources/layout/ins3.png")
		.getImage();		
		ins4 = new ImageIcon("Game Resources/layout/ins4.png")
		.getImage();	
		ins5 = new ImageIcon("Game Resources/layout/ins5.png")
		.getImage();		
		ins6 = new ImageIcon("Game Resources/layout/ins6.png")
		.getImage();		
		mainGlow = new ImageIcon("Game Resources/mainGlow.gif")
		.getImage(); 	
		background = new ImageIcon("Game Resources/background/mainTextNew.png")
				.getImage();
		swapC = new ImageIcon("Game Resources/buttons/swapC.png").getImage();
		swapS = new ImageIcon("Game Resources/buttons/swapS.png").getImage();
		swapP = new ImageIcon("Game Resources/buttons/swapP.png").getImage();
		newP = new ImageIcon("Game Resources/buttons/newP.png").getImage();
		submitP = new ImageIcon("Game Resources/buttons/submitP.png")
				.getImage();
		clearP = new ImageIcon("Game Resources/buttons/clearP.png").getImage();
		passP = new ImageIcon("Game Resources/buttons/passP.png").getImage();
		dictionaryP = new ImageIcon("Game Resources/buttons/dictionaryP.png")
				.getImage();
		torch1 = new ImageIcon("Game Resources/torchAnimation.gif").getImage();
		torch2 = new ImageIcon("Game Resources/torchAnimation1.gif").getImage();
		swapMenu = new ImageIcon("Game Resources/buttons/swapMenu.png")
				.getImage();
		resignInstruc = new ImageIcon("Game Resources/buttons/resignInstruc.png")
		.getImage();
		instrucP = new ImageIcon("Game Resources/buttons/instrucP.png")
		.getImage();
		resignMenuN = new ImageIcon("Game Resources/buttons/resignMenuN.png")
		.getImage();
		resignMenuY = new ImageIcon("Game Resources/buttons/resignMenuY.png")
		.getImage();
		resignP = new ImageIcon("Game Resources/buttons/resignP.png")
		.getImage();		
		resignMenu = new ImageIcon("Game Resources/buttons/resignMenu.png")
			.getImage();		 
		quitMenu = new ImageIcon("Game Resources/buttons/quitMenu.png")
		.getImage();					
		quitP = new ImageIcon("Game Resources/buttons/quitP.png")
		.getImage();					
		menuP = new ImageIcon("Game Resources/buttons/menuP.png")
		.getImage();		
		mainMenu = new ImageIcon("Game Resources/layout/mainMenu.png")
		.getImage();
		modeSelect = new ImageIcon("Game Resources/layout/modeSelect.png")
		.getImage();		
		mainButtons = new ImageIcon("Game Resources/layout/mainButtons.png")
		.getImage();		
		difficulty = new ImageIcon("Game Resources/layout/difficulty.png")
		.getImage();	
		vsPlayerP = new ImageIcon("Game Resources/buttons/vsPlayerP.png")
		.getImage();		
		vsComputerP = new ImageIcon("Game Resources/buttons/vsComputerP.png")
		.getImage();		
		startP = new ImageIcon("Game Resources/buttons/startP.png")
		.getImage();		
		backP = new ImageIcon("Game Resources/buttons/backP.png")
		.getImage();		
		creditP = new ImageIcon("Game Resources/buttons/creditP.png")
		.getImage();		
		easyP = new ImageIcon("Game Resources/buttons/easyP.png")
		.getImage();		
		hardP = new ImageIcon("Game Resources/buttons/hardP.png")
		.getImage();			
		helpP = new ImageIcon("Game Resources/buttons/helpP.png")
		.getImage();	
	}

	/**
	 * Converts the point to its row or column
	 * 
	 * @param point the point to change to a row or column
	 * @return the row or column of the point
	 */
	public static int pointToCord(int point, int TOP_LEFT_BOARD,
			int PIXELS_OF_BOX)
	{
		return (point - TOP_LEFT_BOARD) / PIXELS_OF_BOX;
	}

	/**
	 * Converts the row or column to a point
	 * 
	 * @param location the row or column to convert to a point
	 * @return the point of the row or column
	 */
	public static int cordToPoint(int location)
	{
		return (location * PIXELS_OF_BOX + TOP_LEFT_BOARD);
	}

	
	/**
     * Checks if piece was from board or rack
     * @return true if from board and false if from rack
     */
	public static boolean getFromBoard()
	{
		return fromBoard;
	}
	
	/**
	 * Checks whether to show fancy tile graphics or not
	 * @return 	true yes
	 * 			false no
	 */
	public static boolean getTileGraphics()
	{
		return tileGraphics;
	}
	
	/**
     * Finds who's turn it is
     * @return true if computers turn and false if players turn
     */
	private boolean isCompTurn()
	{
		if (whichPlayer() == computer)
			return true;
		return false;
	}
	
	/**
     * adds and removes pieces from the bag
     * @param piece the piece to change location
     * @param from where the piece currently is
     * @param to where to move the piece
     * @param player the player who the piece is being taken or added to
     */
	public void addRemoveFromBag(Piece piece, int from, int to, Player player)
	{
		piece.setLocation(to);

		if (from == ON_BAG)
			bag.remove();
		else
			player.remove(piece);

		if (to == ON_BAG)
			bag.add(piece);
		else
			player.add(piece);
	}

	// Variable of the game
	private static Scrabble myGame;

	 /**
     * Creates game and a frame for it
     */
	public static void main(String[] args)
	{
		myGame = new Scrabble();
		myGame.pack();
		myGame.setLocationRelativeTo(null);
		myGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myGame.setVisible(true);

	}

	/**
     * Finds and returns who's turn it is
     * @return the instance of the players who's turn it is
     */
	Player whichPlayer()
	{
		if (turn == 1)
			return player1;

		if (mode == 1)
			return computer;
		else
			return player2;
			
	}
	
	/**
	 * Shows who's turn it is
	 * @return a string that describe who the player is
	 */
	String whichPlayerStr()
	{
		if (whichPlayer() == player1)
		{
			return "Player 1";
		}
		else
		{
			return "Player 2";
		}
	}
	

	/**
	 * Checks the strength of the difficulty of the AI
	 * @return		true - hard
	 * 				false - easy
	 */
	public boolean getStrength(){
		return strength;
	}
	
	/**
     * Sets the strength of the AI
     * @param difficulty true if hard and false if easy
     */
	public void setStrength(boolean difficulty)
	{
		strength = difficulty;
	}
	
	/**
	 * Plays a sound file
	 * @param sound the type of sound to play
	 */
	private void playSound(int sound)
	{
		if (playSound)
		{
			if (sound == 1)
				place.play();
			else if (sound == 2)
				button.play();
			else if (sound == 3)
				win.play();
		}
	}
	
	 /**
     * Fills up players pieces when there is a change in turn
     */
	private void changeTurn()
	{
		if (bag.isBagEmpty())
			whichPlayer().fillPieces(bag, myGame);
		moves++;
		txt.append("Tiles Left: " + bag.bagSize() + "\n");
		
		if (mode == 1)
		txt.append("Current Scores - " + "Player 1: " + player1.getScore()
				+ " | " + "Player 2: " + computer.getScore() + "\n \n");
		else if (mode == 2)
			txt.append("Current Scores - " + "Player 1: " + player1.getScore()
					+ " | " + "Player 2: " + player2.getScore() + "\n \n");
		
		if (board.isFirstTurn())
		{
			board.setFirstTurn(false);
		}
	}

	/**
     * Skips the turn
     */
	public void skip()
	{
		turn *= -1;
		skips++;
		changeTurn();
	}

	/**
     * Checks if the bag is empty
     * @return returns true if the bag is empty and false if it is not
     */
	public boolean isBagEmpty()
	{
		return bag.isBagEmpty();
	}

	/**
     * Checks if a point is in bounds of the rack
     * @param selectedPoint the point to check location
     * @return true if the point is on the rack area
     */
	private boolean inBoundsOfPlayerArea(Point selectedPoint)
	{
		return !(selectedPoint.x < 768
				|| selectedPoint.x > 768 + 64 * whichPlayer().numOfPieces()
				|| selectedPoint.y < 396 || selectedPoint.y > 460);
	}

	/**
     * Checks if a point is in bounds of the board
     * @param selectedPoint the point to check location
     * @return true if the point is on the board area
     */
	private boolean inBoundsOfBoardArea(Point selectedPoint)
	{
		return !(selectedPoint.x <= TOP_LEFT_BOARD
				|| selectedPoint.x >= TOP_LEFT_BOARD + PIXELS_OF_BOX * 15
				|| selectedPoint.y <= TOP_LEFT_BOARD || selectedPoint.y >= TOP_LEFT_BOARD
				+ PIXELS_OF_BOX * 15);
	}

	 /**
     * Checks the board to see if the move done was valid
     * @return true if the move is valid
     */
	public boolean isValidMove()
	{
		return board.validMove(moves, txt, isCompTurn());
	}

	// Keeps track of the words to show in the TextArea of the game
	private ArrayList<String> wordsToDisplay = new ArrayList<String>();

	/**
     * Checks if the ArrayList of words are all valid
     * @param words the ArrayList of words
     * @return true if the words are all valid and false otherwise
     */
	public boolean isValidWord(ArrayList<ArrayList<Piece>> words)
	{
		wordsToDisplay = new ArrayList<String>();
		for (int index = 0; index < words.size(); index++)
		{
			ArrayList<Piece> word = new ArrayList<Piece>();
			word = words.get(index);
			String wordToCheck = "";
			for (int indexInner = 0; indexInner < word.size(); indexInner++)
			{
				wordToCheck += word.get(indexInner).theLetter();
			}
			
			if (!dictionary.contains(wordToCheck.toLowerCase()))
			{
				if (whichPlayer() != computer)
					txt.append("\"" + wordToCheck.toUpperCase() + "\" is not a valid word! \n");
				return false;
			}

			if (wordToCheck.length() > 1)
				wordsToDisplay.add(wordToCheck);
		}

		// System.out.println("Is word");
		return true;
	}

	/**
     * Pins all pieces not pinned to the board so they can't be moved
     */
	private void pinToBoard()
	{
		board.pinToBoard();
	}

	/**
     * Checks if game is over
     * @return true if the game is over and false otherwise
     */
	private boolean isGameOver()
	{
		 // Game is over when there are 3 skips in a row
		if (skips >= 3)
			return true;

		return false;
	}

	/**
	 * Checks if the player is swapping tiles
	 * @return true if yes, false if not
	 */
	public boolean isSwapMode()
	{
		if (whichPlayer().getSwap() == true)
			return true;
		return false;
	}

	/**
     * Checks dictionary to see if the word is valid
     * @param word the sequence of letters that make up the word
     * @return true if the word is valid and false otherwise
     */
	public boolean isWord(String word)
	{
		if (dictionary.contains(word))
			return true;
		return false;
	}

	/**
	 * Clears the board of any unpinned tiles
	 */
	private void clearUnpinned()
	{
		ArrayList<Piece> returnTiles = board.clearBoard();
		whichPlayer().removeNulls(
				whichPlayer().numOfNulls());
		for (Piece nextTile : returnTiles)
		{
			whichPlayer().add(nextTile);
		}
	}
	
	/**
     * Those important scoring and graphical things when game is over
     * @param otherPlayer
     */
	private void gameOver(Player otherPlayer)
	{
		 // Does end gaming scoring for both players
		
		player1.endGameScore(otherPlayer);
		otherPlayer.endGameScore(player1);
	
		if (player1.getScore() == otherPlayer.getScore())
		{
			player1.changeScore(player1.endScore());
			otherPlayer.changeScore(otherPlayer.endScore());
		}
	
		System.out.println(player1.getScore() + "    :    "
				+ otherPlayer.getScore());
		
		txt.append("Final Scores - Player 1: " 
				+ player1.getScore() 
				+ " | Player 2: "
				+ otherPlayer.getScore()
				+ "\n");
		
		// Displays message onto text area
		if (resignMode)	
			txt.append(whichPlayerStr() + " has resigned!");
		else if (player1.getScore() > otherPlayer.getScore())
			txt.append("Congratulations! Player 1 has won! \n");
		else if (player1.getScore() < otherPlayer.getScore())
			txt.append("Congratulations! Player 2 has won! \n");
		else
			txt.append("Tie Game! \n");		
		
		playSound(3);
		skips = 3;
	}

	/**
	 * Checks what game mode it currently is
	 * @param mode the game mode
	 * @return	1 - vs AI
	 * 			2 - vs player
	 */
	private Player getMode(int mode)
	{
		if (mode == 1)
			return computer;
		else if (mode == 2)
			return player2;
		else 
			return null;
	}
	
	/**
	 * Opens up a dialog box for the player to check for word validity
	 */
	private void wordDictionaryCheck()
	{
		String str = JOptionPane.showInputDialog(null,
				"Please enter a word: ", "Word Checker", 1);
		if (str != null)
		{
			Pattern pattern = Pattern.compile("[^a-z]",
					Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher(str);
			boolean hasSpecial = match.find();

			if (!hasSpecial)
			{
				if (isWord(str.toLowerCase()) && str.length() > 1)
				{
					JOptionPane
							.showMessageDialog(
									null,
									"\""
											+ str.toUpperCase()
											+ "\" is a valid word!",
									"Word Checker",
									JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane
							.showMessageDialog(
									null,
									"\""
											+ str.toUpperCase()
											+ "\" is not a valid word!",
									"Word Checker",
									JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null,
						"Please enter a word only!",
						"Word Checker",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**         
	 * Finds the number of moves done
     * @return the number of moves done
     */
	public int numOfMoves()
	{
		return moves;
	}

	 /**
     * Changes the letter of a wild card tile
     * @param selectedPiece the piece to change letter
     * @param letter the letter to change the piece too
     */
	private void setWildLetter(Piece selectedPiece, char letter)
	{
		selectedPiece.changeLetter(letter);
	}

	
	/**
     * Changes back wild card tile to a blank
     * @param selectedPiece the piece to change back
     */
	private void removeWild(Piece selectedPiece)
	{
		selectedPiece.changeToWild();
	}

	/**
     * Swaps pieces from your rack and the bag
     * @param toSwap which pieces will be swapped
     * @param game the game variable
     */
	private void swap(ArrayList<Piece> toSwap, Scrabble game)
	{
		for (int i = 0; i < toSwap.size(); i++)
		{
			Piece putBackInBag = toSwap.get(i);
			whichPlayer().remove(putBackInBag);
		}
		whichPlayer().fillPieces(bag, game);
	}

	/**
     * Finds possible AI moves and makes them
     */
	private void AIMove(){
		if (whichPlayer() == computer)
		{
			// Gets the move from AI class and does it on the board
			computer.allCombinations();
			int choice = computer.allWordsHorizontal(board,
					dictionary, myGame, moves);

			if (choice == 1)
			{
				// Checks and score the words the AI did
				if (isValidMove() && board.centerIsFilled())
				{
					ArrayList<ArrayList<Piece>> wordsAI = new ArrayList<ArrayList<Piece>>();
					wordsAI.addAll(board.words());

					int scoreOfAi = board.scoreAI(wordsAI);
					whichPlayer().score(
							board.scoreAI(wordsAI));
					
					// Checks if the AI scored a "bingo"
					if (board.numNonPinned() == 7)
					{
						whichPlayer().score(50);
						scoreOfAi += 50;
					}
					pinToBoard();

					StringBuilder aiWords = new StringBuilder(
							"");
					
					// Add the words scored by the AI to the text box
					for (int wordIndex = 0; wordIndex < computer
							.AIwords().size(); wordIndex++)
					{
						aiWords.append(computer.AIwords()
								.get(wordIndex));
						if (wordIndex < computer.AIwords()
								.size() - 1)
							aiWords.append(", ");

					}

					String player2Text = "Player 2 has scored "
							+ scoreOfAi
							+ " points with the word(s): "
							+ aiWords
							+ " \nPlayer 2 Current Score: "
							+ computer.getScore();
					txt.append(player2Text + "\n");

					skips = 0;
					turn *= -1;
					changeTurn();
					// Prints to console for error check
					System.out.println("Player 1:"
							+ player1.getScore());
					System.out.println("Player 2:"
							+ computer.getScore());

				}
			}
			// Skips AI's turn because no moves
			else if (choice == 2)
			{
				System.out.println(1);
				System.out.println(skips);
				txt.append("Player 2 has skipped.\n");
				skip();

			}
			// Swaps AI's pieces because no possible moves but bag not empty
			else
			{
				System.out.println(2);
				System.out.println(skips);
				if (bag.bagSize() > 7)
				{
					System.out.println("swap");
					swap(computer.pieces(), myGame);
					skips = 0;
					turn *= -1;
					changeTurn();
				}
				else
				{
					txt.append("Player 2 has skipped.\n");
					skip();
				}
			}
		}
	}
	
	  /**
     * The JPanel of the board
     * @author Jashanjot S Sohanpal Evan Cao
     *
     */
	private class BoardArea extends JPanel
	{
		/**
		 * Constructs the JPanel on the jframe
		 */
		
		// Initialize variables for the cursor image and scrolling text box
		JScrollPane scroll;		
		Toolkit toolkit;
		Cursor c;
		
		/**
         * Initializes mouse and key listeners for user input
         * Adds the textbox and scroll bar to the jpanel
         */
		public BoardArea()
		{
			this.setLocation(0, 0);
			this.addMouseListener(new MouseHandler());
			this.addMouseMotionListener(new MouseMotionHandler());
			this.addMouseMotionListener(new MouseHandler());
			this.addKeyListener(new KeyHandler());
			this.setFocusable(true);

			this.setLayout(null);
			txt = new JTextArea();
			txt.setFont(new Font("Rockwell", Font.BOLD, 14));
			txt.setLineWrap(true);
			txt.setEditable(false);
			txt.setOpaque(false);
			txt.setFocusable(false);

			scroll = new JScrollPane(new JViewport(),
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scroll.setBounds(760, 235, 467, 120);
			scroll.getViewport().add(txt);
			scroll.getViewport().setOpaque(false);
			scroll.setOpaque(false);
			scroll.setBorder(null);
			scroll.setFocusable(false);

			this.add(scroll);
			scroll.setVisible(false);
			
			toolkit = Toolkit.getDefaultToolkit();
			Image h1 = toolkit.getImage("Game Resources/h1.png");
			c = toolkit.createCustomCursor(h1 , new Point(this.getX(),
			this.getY()), "img");
			this.setCursor (c);

		}
		
		/**
         * Hides the scroll bar
         * @param set true if want to hide and false if not
         */
		public void hideScroll(boolean set)
		{
			scroll.setVisible(set);
		}

		/**
         * Paints the screen, mostly what screen to show and highlighting different pieces
         */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			// Shows each instruction screen
			if (instrucMode)
			{
				g.drawImage(mainMenu, 0, 0, 1280, 768, null);
				if (insPage == 1)
				{		
					g.drawImage(instruc1, 0, 0, 1280, 768, null);
					g.drawImage(ins1, 0, 0, 1280, 768, null);
				}
				else if (insPage == 2)
				{			
					g.drawImage(instruc2, 0, 0, 1280, 768, null);
					g.drawImage(ins2, 0, 0, 1280, 768, null);
				}
				else if (insPage == 3)
				{
					g.drawImage(instruc3, 0, 0, 1280, 768, null);
					g.drawImage(ins3, 0, 0, 1280, 768, null);
				}
				else if (insPage == 4)
				{
					g.drawImage(instruc4, 0, 0, 1280, 768, null);
					g.drawImage(ins4, 0, 0, 1280, 768, null);
				}
				else if (insPage == 5)
				{					
					g.drawImage(instruc5, 0, 0, 1280, 768, null);
					g.drawImage(ins5, 0, 0, 1280, 768, null);
				}
				else if (insPage == 6)
				{
					g.drawImage(instruc6, 0, 0, 1280, 768, null);
					g.drawImage(ins6, 0, 0, 1280, 768, null);
				}
				
				g.drawImage(insClose, 0, 0, 1280, 768, null);		
			}
			else
			{
				// Draws the main game with board and rack
				if (menuMode)
				{
					g.drawImage(mainMenu, 0, 0, 1280, 768, null);
					g.drawImage(torch1, 315, 350, 78, 180, this);
					g.drawImage(torch2, 927, 350, 78, 180, this);
					g.drawImage(mainGlow,0,0,1280,768, this);
					
					// Draw buttons & pressed button images
					if (mainButtonMode == 0)
					{
						//start, help, credits
						g.drawImage(mainButtons, 0, 0, 1280, 768, null);	
						
						if (mainMenuPress == 0)
						{
							g.drawImage(startP, 0, 0, 1280, 768, null);	
						}
						else if (mainMenuPress == 1)
						{
							g.drawImage(helpP, 0, 0, 1280, 768, null);	
						}
						else if (mainMenuPress == 2)
						{
							g.drawImage(creditP, 0, 0, 1280, 768, null);	
						}
						
					}
					else if (mainButtonMode == 1)
					{
						// Draw vs player or vs computer image
						g.drawImage(modeSelect, 0, 0, 1280, 768, null);	
						
						if (mainMenuPress == 3)
						{
							g.drawImage(backP, 0, 0, 1280, 768, null);	
						}
						else if (mainMenuPress == 4)
						{
							g.drawImage(vsPlayerP, 0, 0, 1280, 768, null);	
						}
						else if (mainMenuPress == 5)
						{
							g.drawImage(vsComputerP, 0, 0, 1280, 768, null);	
						}
					}
					else if (mainButtonMode == 2)
					{
						// Draws the difficulty buttons
						g.drawImage(difficulty, 0, 0, 1280, 768, null);	
						
						if (mainMenuPress == 6)
						{
							g.drawImage(backP, 0, 0, 1280, 768, null);	
						}
						else if (mainMenuPress == 7)
						{
							g.drawImage(easyP, 0, 0, 1280, 768, null);	
						}
						else if (mainMenuPress == 8)
						{
							g.drawImage(hardP, 0, 0, 1280, 768, null);	
						}
					}		
					// Draw credits
					if (showCredits)
						g.drawImage(credits, 0, 0, 1280, 768, null);	
				}
				else
				{
					// Draw background
					g.drawImage(background, 0, 0, 1280, 768, null);
		
					if (mode == 1)
					{
						if (getStrength() == true)
							g.drawImage(pvcH, 0, 0, 1280, 768, null);
						else
							g.drawImage(pvcE, 0, 0, 1280, 768, null);							
					}
					else if (mode == 2)
						g.drawImage(pvp, 10, 0, 1280, 768, null);
					
					g.drawImage(torch1, 706, 510, 78, 162, this);
					g.drawImage(torch2, 1202, 510, 78, 162, this);
		
					// Draws the board and pieces 
					board.draw(g);
		
					// Draws the player rack
					if (turn == 1)
						player1.draw(g);
					else
						getMode(mode).draw(g);		
					
					// Draw the tile graphics
					if (getTileGraphics())
						g.drawImage(showSpecial, 195, 720, null);
					else
						g.drawImage(hideSpecial, 195, 720, null);
					
					if (playMusic)
						g.drawImage(musicP, 345, 720, null);
					else
						g.drawImage(musicS, 345, 720, null);
					
					if (playSound)
						g.drawImage(soundY, 495, 720, null);
					else
						g.drawImage(soundN, 495, 720, null);

					// Draw buttons & pressed button images
					if (pressed == 1)
						g.drawImage(newP, 0, -54, 1280, 768, null);
					if (pressed == 2)
						g.drawImage(submitP, 0, -54, 1280, 768, null);
					if (pressed == 3)
						g.drawImage(clearP, 0, -54, 1280, 768, null);
					if (pressed == 4)
						g.drawImage(swapP, 0, -54, 1280, 768, null);
					if (pressed == 5)
						g.drawImage(passP, 0, 0, 1280, 768, null);
					if (pressed == 6)
						g.drawImage(dictionaryP, 0, 0, 1280, 768, null);
					
					if (!isGameOver())
					{
						// Draw buttons & pressed button images
						if (!resignMode)
						{
							g.drawImage(resignInstruc, 0, 0, 1280, 768, null);
							
							if (pressed == 9)
								g.drawImage(resignP, 0, 0, 1280, 768, null);
							if (pressed == 10)
								g.drawImage(instrucP, 0, 0, 1280, 768, null);
						}
						else
						{			
							g.drawImage(resignMenu, 8, 11, 1280, 768, null);			
										
							if (pressed == 11)
								g.drawImage(resignMenuY, 8, 11, 1280, 768, null);
							if (pressed == 12)
								g.drawImage(resignMenuN, 8, 11, 1280, 768, null);
						}
						
						if (selectedPiece != null && !board.getWildCardMode())
							selectedPiece.draw(g);	
					}
					else
					{
						g.drawImage(quitMenu, 8, 11, 1280, 768, null);
						
						if (pressed == 13)
							g.drawImage(menuP, 8, 11, 1280, 768, null);
						if (pressed == 14)
							g.drawImage(quitP, 8, 11, 1280, 768, null);								
					}
		
					if (isSwapMode())
					{
						g.drawImage(swapMenu, 0, -26, 1280, 768, null);
						if (pressed == 7)
							g.drawImage(swapS, 0, -26, 1280, 768, null);
						if (pressed == 8)
							g.drawImage(swapC, 0, -26, 1280, 768, null);
					}
				}
			}
		}

		 /**
         * Mouse methods for game
         * @author Jashanjot S Sohanpal, Evan Cao
         *
         */
		private class MouseHandler extends MouseAdapter
		{
			/**
             * Mostly finds what screen to show, which button was clicked and which piece was clicked
             */
			public void mousePressed(MouseEvent event)
			{
				Point selectedPoint = event.getPoint();
				showCredits = false;
				
				// Navigates through each instruction page
				if (instrucMode)
				{
					if (selectedPoint.x > 155 && selectedPoint.x < 210
							&& selectedPoint.y > 80 && selectedPoint.y < 135)
					{
						insPage = 1;
						playSound(1);
					}
					
					if (selectedPoint.x > 230 && selectedPoint.x < 285
							&& selectedPoint.y > 80 && selectedPoint.y < 135)
					{
						insPage = 2;
						playSound(1);
					}
					
					if (selectedPoint.x > 310 && selectedPoint.x < 365
							&& selectedPoint.y > 80 && selectedPoint.y < 135)
					{
						insPage = 3;
						playSound(1);
					}
					
					if (selectedPoint.x > 925 && selectedPoint.x < 980
							&& selectedPoint.y > 80 && selectedPoint.y < 135)
					{
						insPage = 4;
						playSound(1);
					}
					
					if (selectedPoint.x > 1000 && selectedPoint.x < 1055
							&& selectedPoint.y > 80 && selectedPoint.y < 135)
					{
						insPage = 5;
						playSound(1);
					}
					
					if (selectedPoint.x > 1080 && selectedPoint.x < 1135
							&& selectedPoint.y > 80 && selectedPoint.y < 135)
					{
						insPage = 6;
						playSound(1);
					}
					
					if (selectedPoint.x > 140 && selectedPoint.x < 260
							&& selectedPoint.y > 655 && selectedPoint.y < 700)
					{
						instrucMode = false;
						insPage = 1;
						if (!menuMode)
							hideScroll(true);
						playSound(2);
					}
					
					
				}
				else
				{
					if (menuMode)
					{
						// Navigates through the main menu buttons
						if (mainButtonMode == 0)
						{
							if (selectedPoint.x > 465 && selectedPoint.x < 589
									&& selectedPoint.y > 518 && selectedPoint.y < 566)
							{
								mainMenuPress = 0;
								playSound(2);
							}
							if (selectedPoint.x > 606 && selectedPoint.x < 730
									&& selectedPoint.y > 518 && selectedPoint.y < 566)
							{
								mainMenuPress = 1;
								playSound(2);
							}
							if (selectedPoint.x > 748 && selectedPoint.x < 872
									&& selectedPoint.y > 518 && selectedPoint.y < 566)
							{
								mainMenuPress = 2;
								playSound(2);
							}
						}
						
						else if (mainButtonMode == 1)
						{
							if (selectedPoint.x > 465 && selectedPoint.x < 589
									&& selectedPoint.y > 518 && selectedPoint.y < 566)
							{
								mainMenuPress = 3;
								playSound(2);
							}
							if (selectedPoint.x > 606 && selectedPoint.x < 730
									&& selectedPoint.y > 518 && selectedPoint.y < 566)
							{
								mainMenuPress = 4;
								playSound(2);
							}
							if (selectedPoint.x > 748 && selectedPoint.x < 872
									&& selectedPoint.y > 518 && selectedPoint.y < 566)
							{
								mainMenuPress = 5;
								playSound(2);
							}
						}
						
						else if (mainButtonMode == 2)
						{
							if (selectedPoint.x > 465 && selectedPoint.x < 589
									&& selectedPoint.y > 518 && selectedPoint.y < 566)
							{
								mainMenuPress = 6;
								playSound(2);
							}
							if (selectedPoint.x > 606 && selectedPoint.x < 730
									&& selectedPoint.y > 518 && selectedPoint.y < 566)
							{
								mainMenuPress = 7;
								playSound(2);
							}
							if (selectedPoint.x > 748 && selectedPoint.x < 872
									&& selectedPoint.y > 518 && selectedPoint.y < 566)
							{
								mainMenuPress = 8;
								playSound(2);
							}
						}
						
					}
					else
						{
						// Show the pressed button images
						if (!board.getWildCardMode() && !isSwapMode() && !resignMode)
						{
		
							if (selectedPoint.x > 1008 && selectedPoint.x < 1200
									&& selectedPoint.y > 570 && selectedPoint.y < 634)
							{
								pressed = 1;
								playSound(2);
							}
		
							if (selectedPoint.x > 784 && selectedPoint.x < 986
									&& selectedPoint.y > 490 && selectedPoint.y < 554)
							{
								pressed = 2;
								playSound(2);
							}
		
							if (selectedPoint.x > 784 && selectedPoint.x < 986
									&& selectedPoint.y > 570 && selectedPoint.y < 634)
							{
								pressed = 3;
								playSound(2);
							}
		
							if (selectedPoint.x > 1008 && selectedPoint.x < 1200
									&& selectedPoint.y > 490 && selectedPoint.y < 554)
							{
								pressed = 4;
								playSound(2);
							}
		
							if (selectedPoint.x > 784 && selectedPoint.x < 986
									&& selectedPoint.y > 650 && selectedPoint.y < 714)
							{
								pressed = 5;
								playSound(2);
							}
		
							if (selectedPoint.x > 1008 && selectedPoint.x < 1200
									&& selectedPoint.y > 650 && selectedPoint.y < 714)
							{
								pressed = 6;
								playSound(2);
							}
							
							
	
							if (selectedPoint.x > 806 && selectedPoint.x < 991
									&& selectedPoint.y > 101 && selectedPoint.y < 161)

							{
								pressed = 9;
								playSound(2);
							}
							
							if (selectedPoint.x > 991 && selectedPoint.x < 1176
									&& selectedPoint.y > 101 && selectedPoint.y < 161)
							{
								pressed = 10;
								playSound(2);
							}
							
							if (selectedPoint.x > 51 && selectedPoint.x < 69
									&& selectedPoint.y > 725 && selectedPoint.y < 742)
							{
								if (board.getPopUpMode())
								{
									board.setPopUp(false);
								}
								else
									board.setPopUp(true);
							}
							
							if (selectedPoint.x > 201 && selectedPoint.x < 219
									&& selectedPoint.y > 725 && selectedPoint.y < 742)
							{
								if (getTileGraphics())
								{
									tileGraphics = false;
								}
								else
									tileGraphics = true;
							}
							
							if (selectedPoint.x > 351 && selectedPoint.x < 369
									&& selectedPoint.y > 725 && selectedPoint.y < 742)
							{
								if (playMusic)
								{
									playMusic(false);
								}
								else
								{
									playMusic(true);
								}
							}
							
							if (selectedPoint.x > 501 && selectedPoint.x < 519
									&& selectedPoint.y > 725 && selectedPoint.y < 742)
							{
								if (playSound)
								{
									playSound = false;
								}
								else
								{
									playSound = true;
								}
							}
		
							if (!isGameOver()
									&& (inBoundsOfPlayerArea(selectedPoint) || inBoundsOfBoardArea(selectedPoint)))
							{
								if (event.getButton() == MouseEvent.BUTTON1)
								{
									if (inBoundsOfPlayerArea(selectedPoint))
									{
										selectedPiece = whichPlayer().getPiece(pointToCord(selectedPoint.x, 768, 64));
										fromBoard = false;
									}
									else
									{
										selectedPiece = board.getPiece(pointToCord(selectedPoint.y,TOP_LEFT_BOARD, PIXELS_OF_BOX),pointToCord(selectedPoint.x,TOP_LEFT_BOARD, PIXELS_OF_BOX));
										fromBoard = true;
										if (selectedPiece != null && selectedPiece.isPinned())
											selectedPiece = null;
									}
		
									if (selectedPiece != null)
									{
										lastPoint = selectedPoint;
										previousPoint = new Point(selectedPiece.x,
												selectedPiece.y);
										selectedPiece.selectedPiece(true);
									}
		
								}
							}
		
		
							board.showHighlight(false);
							board.type(0);
							board.mousing(false);
							forSwap = new ArrayList<Piece>();
							repaint();
						}
						else if (resignMode && !board.getWildCardMode() && !isSwapMode())
						{
							if (selectedPoint.x > 806 && selectedPoint.x < 991
									&& selectedPoint.y > 101 && selectedPoint.y < 161)
							{
								pressed = 11;
								playSound(2);
							}
						
							if (selectedPoint.x > 991 && selectedPoint.x < 1176
									&& selectedPoint.y > 101 && selectedPoint.y < 161)
							{
								pressed = 12;
								playSound(2);
							}
						}
						// Picks up the tile from the board or rack
						else if (isSwapMode() && !resignMode && !board.getWildCardMode())
						{
		
							if (inBoundsOfPlayerArea(selectedPoint))
							{
								selectedPiece = whichPlayer().getPiece(
										pointToCord(selectedPoint.x, 768, 64));
								if (!forSwap.contains(selectedPiece)
										&& forSwap.size() <= bag.bagSize())
								{
									whichPlayer().getPiece(
											pointToCord(selectedPoint.x, 768, 64))
											.setSwapSelect(true);
									forSwap.add(selectedPiece);
								}
								else if (forSwap.contains(selectedPiece))
								{
									forSwap.remove(selectedPiece);
									whichPlayer().getPiece(
											pointToCord(selectedPoint.x, 768, 64))
											.setSwapSelect(false);
								}
							}
							else
							{
								if (selectedPoint.x > 784 && selectedPoint.x < 986
										&& selectedPoint.y > 490
										&& selectedPoint.y < 554)
								{
									pressed = 7;
									playSound(2);
								}
		
								if (selectedPoint.x > 1008 && selectedPoint.x < 1200
										&& selectedPoint.y > 490
										&& selectedPoint.y < 554)
								{
									pressed = 8;
									playSound(2);
								}
							}
						}
						
						if(isGameOver())
						{
							if (selectedPoint.x > 806 && selectedPoint.x < 991
									&& selectedPoint.y > 101 && selectedPoint.y < 161)
							{
								pressed = 13;
								playSound(2);
							}
						
							if (selectedPoint.x > 991 && selectedPoint.x < 1176
									&& selectedPoint.y > 101 && selectedPoint.y < 161)
							{
								pressed = 14;
								playSound(2);
							}
						}
						
						board.showHighlight(false);
						board.type(0);
						board.mousing(false);
						repaint();
						}
					}
				repaint();		
				}

			 /**
             * Activates button functions and movement of pieces/tiles
             */
			public void mouseReleased(MouseEvent event)
			{
				Point releasedPoint = event.getPoint();

				if (!instrucMode)
					{
					// Navigates through the menu button options
					if (menuMode)
						{
							if (mainButtonMode == 0)
							{
								if (releasedPoint.x > 465 && releasedPoint.x < 589
										&& releasedPoint.y > 518 && releasedPoint.y < 566)
								{
									mainButtonMode = 1;
								}
								
								if (releasedPoint.x > 606 && releasedPoint.x < 730
										&& releasedPoint.y > 518 && releasedPoint.y < 566)
								{
									instrucMode = true;
									insPage = 1;
								}
		
		
								if (releasedPoint.x > 748 && releasedPoint.x < 872
										&& releasedPoint.y > 518 && releasedPoint.y < 566)
									
								{
									showCredits = true;
								}
		
							}
							else if (mainButtonMode == 1)
							{
								if (releasedPoint.x > 465 && releasedPoint.x < 589
										&& releasedPoint.y > 518 && releasedPoint.y < 566)
								{
									mainButtonMode = 0;
								}
								if (releasedPoint.x > 606 && releasedPoint.x < 730
										&& releasedPoint.y > 518 && releasedPoint.y < 566)
								{					
									menuMode = false;
									mode = 2;		
									hideScroll(true);
									newGame();
								}
								 
								if (releasedPoint.x > 748 && releasedPoint.x < 872
										&& releasedPoint.y > 518 && releasedPoint.y < 566)
								{
									mainButtonMode = 2;					
								}
		
								
							}
							
							else if (mainButtonMode == 2)
							{
								if (releasedPoint.x > 465 && releasedPoint.x < 589
										&& releasedPoint.y > 518 && releasedPoint.y < 566)
								{
									mainButtonMode = 1;
								}
								
								if (releasedPoint.x > 606 && releasedPoint.x < 730
										&& releasedPoint.y > 518 && releasedPoint.y < 566)
								{
									menuMode = false;
									mode = 1;	
									setStrength(false);
									hideScroll(true);
									newGame();
								}
		
								if (releasedPoint.x > 748 && releasedPoint.x < 872
										&& releasedPoint.y > 518 && releasedPoint.y < 566)
								{
		
									menuMode = false;
									mode = 1;	
									setStrength(true);
									hideScroll(true);
									newGame();
								}
		
									
							}
							
						}
					else
						{
						// Responds to mouse releases with button functions
							if (!board.getWildCardMode() && !isSwapMode() && !instrucMode && !resignMode)
							{
								// Creates a new game
								if (releasedPoint.x > 1008 && releasedPoint.x < 1200
										&& releasedPoint.y > 570 && releasedPoint.y < 634
										&& selectedPiece == null)
								{
									boolean popUpShow = board.getPopUpMode();
									newGame();
									board.setPopUp(popUpShow);
								}
								// Return to main menu or close game
								if (isGameOver())
								{
									if (releasedPoint.x > 806 && releasedPoint.x < 991
											&& releasedPoint.y > 101 && releasedPoint.y < 161)
									{
										menuMode = true;
										mainButtonMode = 0;
										hideScroll(false);
									}
								
									if (releasedPoint.x > 991 && releasedPoint.x < 1176
											&& releasedPoint.y > 101 && releasedPoint.y < 161)
									{
										System.exit(0);
									}
								}						
								else
								{
									
									// Skips the turn
									if (releasedPoint.x > 784 && releasedPoint.x < 986
											&& releasedPoint.y > 650
											&& releasedPoint.y < 714)
									{
			
										clearUnpinned();
			
										txt.append("Player 1 has skipped.\n");
										skip();
										moves--;
			
										// Makes the AI move
										AIMove();
			
									}
			
									// Submits the word for checking of validity
									if (releasedPoint.x > 784 && releasedPoint.x < 986
											&& releasedPoint.y > 490
											&& releasedPoint.y < 554
											&& selectedPiece == null)
									{
										if (isValidMove() && board.centerIsFilled())
										{
											ArrayList<ArrayList<Piece>> words = new ArrayList<ArrayList<Piece>>();
											words.addAll(board.words());
											if (words.get(0).size() == 1 && moves == 0)
											{
												pressed = 0;
												repaint();
												return;
											}
											if (isValidWord(words))
											{
												// Code for displaying the score on the text
												// area after each turn
												int currentScore = board.score(words,
														whichPlayer());
			
												whichPlayer().score(currentScore);
												if (board.numNonPinned() == 7)
												{
													currentScore += 50;
													whichPlayer().score(50);
												}
			
												pinToBoard();
												// Prints out the respective player turn,
												// valid
												// word made during that turn, the score of
												// the
												// word and the total score that player has
												System.out.println("Player 1:"
														+ player1.getScore());
												System.out.println("Player 2:"
														+ getMode(mode).getScore());
												StringBuilder validWords = new StringBuilder(
														"");
												for (int wordIndex = 0; wordIndex < wordsToDisplay
														.size(); wordIndex++)
												{
			
													validWords.append(wordsToDisplay
															.get(wordIndex));
													if (wordIndex < wordsToDisplay.size() - 1)
														validWords.append(", ");
			
												}
			
												if (whichPlayer() == player1)
												{
													String player1Text =
															"Player 1 has scored "
															+ currentScore
															+ " points with the word(s): "
															+ validWords + " \n";
													txt.append(player1Text);
												}
												else if (whichPlayer() == player2)
												{
													
													  String player2Text =
													  "Player 2 has scored "
													  + currentScore
													  + " points with the word(s): " +
													  validWords;// +
													 // " \nPlayer 2 Current Score: " +
													//  player2.getScore();
													  txt.append(player2Text + "\n");
													 
												}
												
												turn *= -1;
												changeTurn();
												skips = 0;
												
												if (getMode(mode) == computer)
												AIMove();
											}											
										}
										else if (!board.centerIsFilled())
										{
											txt.append("The first word must have a tile on the center! \n");
										}
									}
			
									// Clears the board of unpinned tiles
									if (releasedPoint.x > 784 && releasedPoint.x < 986
											&& releasedPoint.y > 570
											&& releasedPoint.y < 634
											&& selectedPiece == null)
									{
										clearUnpinned();			
									}
			
									// Deals with swapping and cancelling swap
									if (releasedPoint.x > 1008 && releasedPoint.x < 1200&& releasedPoint.y > 490&& releasedPoint.y < 554&& selectedPiece == null)
									{
											clearUnpinned();
			
											whichPlayer().setSwapMode(true);
											whichPlayer().setSwap(true);
									}
			
									// Shows a dialog box for Dictionary
									if (releasedPoint.x > 1008 && releasedPoint.x < 1200
											&& releasedPoint.y > 650
											&& releasedPoint.y < 714
											&& selectedPiece == null)
									{
										pressed = 0;
										repaint();
										wordDictionaryCheck();
									}
									
									// Responds to the button presses of resign
									if (!resignMode && selectedPiece == null)
									{
										if (releasedPoint.x > 806 && releasedPoint.x < 991
												&& releasedPoint.y > 101 && releasedPoint.y < 161)
											resignMode = true;
									
										if (releasedPoint.x > 991 && releasedPoint.x < 1176
												&& releasedPoint.y > 101 && releasedPoint.y < 161)
										{
											instrucMode = true;
											insPage = 1;
											hideScroll(false);
										}
										
									}	
										
									// Shows mouse over is player releases mouse over special tile
									if (((releasedPoint.x > 64 && releasedPoint.x < 106) && ((releasedPoint.y > 64 && releasedPoint.y < 106)
											|| (releasedPoint.y > 358 && releasedPoint.y < 400) || (releasedPoint.y > 652 && releasedPoint.y < 694)))
											|| ((releasedPoint.x > 358 && releasedPoint.x < 400) && ((releasedPoint.y > 64 && releasedPoint.y < 106) || (releasedPoint.y > 652 && releasedPoint.y < 694)))
											|| ((releasedPoint.x > 652 && releasedPoint.x < 694) && ((releasedPoint.y > 64 && releasedPoint.y < 106)
													|| (releasedPoint.y > 358 && releasedPoint.y < 400) || (releasedPoint.y > 652 && releasedPoint.y < 694))))
									{
										board.type(1);
										board.mousing(true);
										board.pointing(releasedPoint);
									}
			
									// DL
									else if (((releasedPoint.x > 64 && releasedPoint.x < 106) && 
													((releasedPoint.y > 190 && releasedPoint.y < 232)
													|| (releasedPoint.y > 526 && releasedPoint.y < 568)))
											|| ((releasedPoint.x > 148 && releasedPoint.x < 190) && 
													((releasedPoint.y > 316 && releasedPoint.y < 358) 
													|| (releasedPoint.y > 400 && releasedPoint.y < 442)))
											|| ((releasedPoint.x > 190 && releasedPoint.x < 232) && 
													((releasedPoint.y > 64 && releasedPoint.y < 106)
													|| (releasedPoint.y > 358 && releasedPoint.y < 400)
													|| (releasedPoint.y > 652 && releasedPoint.y < 694)))
											|| ((releasedPoint.x > 316 && releasedPoint.x < 358) && 
													((releasedPoint.y > 148 && releasedPoint.y < 190)
													|| (releasedPoint.y > 316 && releasedPoint.y < 358)
													|| (releasedPoint.y > 400 && releasedPoint.y < 442) 
													|| (releasedPoint.y > 568 && releasedPoint.y < 610)))
											|| ((releasedPoint.x > 358 && releasedPoint.x < 400) && 
													((releasedPoint.y > 190 && releasedPoint.y < 232) 
													|| (releasedPoint.y > 526 && releasedPoint.y < 568)))
											|| ((releasedPoint.x > 400 && releasedPoint.x < 442) && 
													((releasedPoint.y > 148 && releasedPoint.y < 190)
													|| (releasedPoint.y > 316 && releasedPoint.y < 358)
													|| (releasedPoint.y > 400 && releasedPoint.y < 442) 
													|| (releasedPoint.y > 568 && releasedPoint.y < 610)))
											|| ((releasedPoint.x > 526 && releasedPoint.x < 568) && 
													((releasedPoint.y > 64 && releasedPoint.y < 106)
													|| (releasedPoint.y > 358 && releasedPoint.y < 400) 
													|| (releasedPoint.y > 652 && releasedPoint.y < 694)))
											|| ((releasedPoint.x > 568 && releasedPoint.x < 610) && 
													((releasedPoint.y > 316 && releasedPoint.y < 358) 
													|| (releasedPoint.y > 400 && releasedPoint.y < 442)))
											|| ((releasedPoint.x > 652 && releasedPoint.x < 694) && 
													((releasedPoint.y > 190 && releasedPoint.y < 232) 
													|| (releasedPoint.y > 526 && releasedPoint.y < 568))))
									{
										board.type(2);
										board.mousing(true);
										board.pointing(releasedPoint);
									}
			
									// DW
									else if (((releasedPoint.x > 106 && releasedPoint.x < 148) && ((releasedPoint.y > 106 && releasedPoint.y < 148) || (releasedPoint.y > 610 && releasedPoint.y < 652)))
											|| ((releasedPoint.x > 148 && releasedPoint.x < 190) && ((releasedPoint.y > 148 && releasedPoint.y < 190) || (releasedPoint.y > 568 && releasedPoint.y < 610)))
											|| ((releasedPoint.x > 190 && releasedPoint.x < 232) && ((releasedPoint.y > 190 && releasedPoint.y < 232) || (releasedPoint.y > 526 && releasedPoint.y < 568)))
											|| ((releasedPoint.x > 232 && releasedPoint.x < 274) && ((releasedPoint.y > 232 && releasedPoint.y < 272) || (releasedPoint.y > 484 && releasedPoint.y < 526)))
											|| ((releasedPoint.x > 484 && releasedPoint.x < 526) && ((releasedPoint.y > 232 && releasedPoint.y < 272) || (releasedPoint.y > 484 && releasedPoint.y < 526)))
											|| ((releasedPoint.x > 526 && releasedPoint.x < 568) && ((releasedPoint.y > 190 && releasedPoint.y < 232) || (releasedPoint.y > 526 && releasedPoint.y < 568)))
											|| ((releasedPoint.x > 568 && releasedPoint.x < 610) && ((releasedPoint.y > 148 && releasedPoint.y < 190) || (releasedPoint.y > 568 && releasedPoint.y < 610)))
											|| ((releasedPoint.x > 610 && releasedPoint.x < 652) && ((releasedPoint.y > 106 && releasedPoint.y < 148) || (releasedPoint.y > 610 && releasedPoint.y < 652))))
									{
										board.type(3);
										board.mousing(true);
										board.pointing(releasedPoint);
									}
			
									// TL
									else if (((releasedPoint.x > 106 && releasedPoint.x < 148) && ((releasedPoint.y > 274 && releasedPoint.y < 316) || (releasedPoint.y > 442 && releasedPoint.y < 484)))
											|| ((releasedPoint.x > 274 && releasedPoint.x < 316) && ((releasedPoint.y > 274 && releasedPoint.y < 316)
													|| (releasedPoint.y > 442 && releasedPoint.y < 484)
													|| (releasedPoint.y > 106 && releasedPoint.y < 148) || (releasedPoint.y > 610 && releasedPoint.y < 652)))
											|| ((releasedPoint.x > 442 && releasedPoint.x < 484) && ((releasedPoint.y > 274 && releasedPoint.y < 316)
													|| (releasedPoint.y > 442 && releasedPoint.y < 484)
													|| (releasedPoint.y > 106 && releasedPoint.y < 148) || (releasedPoint.y > 610 && releasedPoint.y < 652)))
											|| ((releasedPoint.x > 610 && releasedPoint.x < 652) && ((releasedPoint.y > 274 && releasedPoint.y < 316) || (releasedPoint.y > 442 && releasedPoint.y < 484))))
									{
										board.type(4);
										board.mousing(true);
										board.pointing(releasedPoint);
									}
			
									else
									{
										board.type(0);
										board.mousing(false);
									}
			
									// Deals with the player dropping a selected tile over 
									// the board area or rack
									if (selectedPiece != null)
									{
										selectedPiece.dragging(false);
										if (!fromBoard)
										{
											if (inBoundsOfPlayerArea(releasedPoint))
											{
												int index = pointToCord(releasedPoint.x,768, 64);
												whichPlayer().insertPiece(selectedPiece,index);
											}
											else if (inBoundsOfBoardArea(releasedPoint))
											{
												int row = pointToCord(releasedPoint.y,
														TOP_LEFT_BOARD, PIXELS_OF_BOX);
												int col = pointToCord(releasedPoint.x,
														TOP_LEFT_BOARD, PIXELS_OF_BOX);
												if (board.getPiece(row, col) == null)
												{
													if (selectedPiece.theLetter() == '0')
													{
														board.setWildCardMode(true);
														selectedPiece.setWildRowCol(row,
																col);
													}
													else
													{
														selectedPiece.setLocation(ON_BOARD);
														board.insertPiece(selectedPiece,row, col);
														whichPlayer().remove(selectedPiece);
													}
												}
												playSound(1);
											}
										}
										else if (fromBoard)
										{
											if (inBoundsOfBoardArea(releasedPoint))
											{
												int row = pointToCord(releasedPoint.y,
														TOP_LEFT_BOARD, PIXELS_OF_BOX);
												int col = pointToCord(releasedPoint.x,
														TOP_LEFT_BOARD, PIXELS_OF_BOX);
												if (board.getPiece(row, col) == null)
												{
													selectedPiece.setLocation(ON_PLAYER);
													board.insertPiece(selectedPiece, row,col);
													board.remove(
															pointToCord(previousPoint.y,
																	TOP_LEFT_BOARD,
																	PIXELS_OF_BOX),
															pointToCord(previousPoint.x,
																	TOP_LEFT_BOARD,
																	PIXELS_OF_BOX));
												}
												playSound(1);
											}
											else if (inBoundsOfPlayerArea(releasedPoint))
											{
												if (selectedPiece.isWildCard())
													removeWild(selectedPiece);
												board.remove(
														pointToCord(previousPoint.y,
																TOP_LEFT_BOARD,
																PIXELS_OF_BOX),
														pointToCord(previousPoint.x,
																TOP_LEFT_BOARD,
																PIXELS_OF_BOX));
												int index = pointToCord(releasedPoint.x,
														768, 64);
												whichPlayer().addFromBoard(index,
														selectedPiece);
											}
											else
												playSound(1);
											
			
										}
									}
			
									if (selectedPiece != null && !board.getWildCardMode())
									{
										selectedPiece.selectedPiece(false);
										selectedPiece = null;
									}
			
									// Removes an unpinned tile from the board if right clicked
									if (selectedPiece == null
											&& event.getButton() == MouseEvent.BUTTON3)
										if (inBoundsOfBoardArea(releasedPoint))
										{
			
											Piece returnTile = board.getPiece(
													pointToCord(releasedPoint.y,
															TOP_LEFT_BOARD, PIXELS_OF_BOX),
													pointToCord(releasedPoint.x,
															TOP_LEFT_BOARD, PIXELS_OF_BOX));
			
											if (returnTile != null
													&& !returnTile.isPinned())
											{
												board.remove(
														pointToCord(releasedPoint.y,
																TOP_LEFT_BOARD,
																PIXELS_OF_BOX),
														pointToCord(releasedPoint.x,
																TOP_LEFT_BOARD,
																PIXELS_OF_BOX));
			
												if (returnTile.isWildCard())
													returnTile.changeToWild();
												whichPlayer().removeNulls(1);
			
												whichPlayer().add(returnTile);
											}
										}
			
									// Show highlights on tile
									if (inBoundsOfBoardArea(releasedPoint)
											&& !board.getWildCardMode())
									{
										if (!board.getPopUpMode()
												|| (board.getPopUpMode() && !board
														.getMouse()))
										{
											Piece testPiece = board.getPiece(
													pointToCord(releasedPoint.y,
															TOP_LEFT_BOARD, PIXELS_OF_BOX),
													pointToCord(releasedPoint.x,
															TOP_LEFT_BOARD, PIXELS_OF_BOX));
											if (testPiece != null)
											{
												if (!testPiece.isPinned())
												{
													board.pointing(releasedPoint);
													board.showHighlight(true);
												}
												else
												{
													board.showHighlight(false);
												}
											}
											else
											{
												board.showHighlight(false);
											}
										}
										else
										{
											board.showHighlight(false);
										}
			
									}
									pressed = 0;
									if (isGameOver() && !board.getWildCardMode()){
										gameOver(getMode(mode));
									}
								}
			
								if (isGameOver() && !board.getWildCardMode())
								{
									pressed = 0;
								}
								
								// Scroll down
								txt.setCaretPosition(txt.getDocument().getLength());
							}
							// Responds to swapping and cancelling swap
							else if (!board.getWildCardMode() && isSwapMode() && !isGameOver() && !resignMode)
								{
									//Cancel Swap
									if (releasedPoint.x > 1008 && releasedPoint.x < 1200
											&& releasedPoint.y > 490 && releasedPoint.y < 554)
									{
										whichPlayer().setSwapMode(false);
										whichPlayer().setSwap(false);
										forSwap = new ArrayList<Piece>();
										selectedPiece = null;						
									}
									//Submit Swap
									if (releasedPoint.x > 784 && releasedPoint.x < 986
											&& releasedPoint.y > 490 && releasedPoint.y < 554)
										{
											whichPlayer().setSwapMode(false);
											whichPlayer().setSwap(false);
											swap(forSwap, myGame);
											selectedPiece = null;
											forSwap = new ArrayList<Piece>();
											
											if (whichPlayer() == player1)
												txt.append("Player 1 has swapped tiles. \n");
											else if (whichPlayer() == player2)
												txt.append("Player 2 has swapped tiles. \n");
											
											turn *= -1;
											changeTurn();
											skips = 0;
											moves--;
											
											if (getMode(mode) == computer)
												AIMove();
										}
																				
								}
							// Responds to resign button presses
							else if (resignMode && !board.getWildCardMode() && !isSwapMode() && !isGameOver())
							{
								if (releasedPoint.x > 806 && releasedPoint.x < 991
										&& releasedPoint.y > 101 && releasedPoint.y < 161)
								{
									
									// Clear
									ArrayList<Piece> returnTiles = board.clearBoard();
									whichPlayer().removeNulls(whichPlayer().numOfNulls());
									
									for (Piece nextTile : returnTiles)
									{
										whichPlayer().add(nextTile);
									}
									
									gameOver(whichPlayer());
									resignMode = false;
		
								}
							
								if (releasedPoint.x > 991 && releasedPoint.x < 1176
										&& releasedPoint.y > 101 && releasedPoint.y < 161)
									resignMode = false;
							}
							
							pressed = 0;
						}
					mainMenuPress = -1;
					
					}
	
				repaint();
			}
			

		}

		/**
         * Mouse over and dragging methods
         * @author Jashanjot S Sohanpal, Evan Cao
         *
         */
		private class MouseMotionHandler implements MouseMotionListener
		{
			 /**
             * Drags piece across the screen
             */
			public void mouseDragged(MouseEvent event)
			{
				if (!menuMode && !instrucMode)
				{
					if (!isGameOver() && !board.getWildCardMode() && !isSwapMode())
					{
						Point currentPoint = event.getPoint();
						if (selectedPiece != null)
						{
							selectedPiece.dragging(true);
							selectedPiece.move(lastPoint, currentPoint);
							lastPoint = currentPoint;
							repaint();
						}
					}
				}

			}

			/**
			 * Shows highlights over pieces and special tiles
			 */
			public void mouseMoved(MouseEvent event)
			{
				Point movePoint = event.getPoint();
				
				if (!menuMode && !instrucMode)
					{
					if (!isGameOver() && !board.getWildCardMode() && !isSwapMode())
						{
							if (selectedPiece == null)
							{
								// TW
								if (((movePoint.x > 64 && movePoint.x < 106) && ((movePoint.y > 64 && movePoint.y < 106)
										|| (movePoint.y > 358 && movePoint.y < 400) || (movePoint.y > 652 && movePoint.y < 694)))
										|| ((movePoint.x > 358 && movePoint.x < 400) && ((movePoint.y > 64 && movePoint.y < 106) || (movePoint.y > 652 && movePoint.y < 694)))
										|| ((movePoint.x > 652 && movePoint.x < 694) && ((movePoint.y > 64 && movePoint.y < 106)
												|| (movePoint.y > 358 && movePoint.y < 400) || (movePoint.y > 652 && movePoint.y < 694))))
								{
									board.type(1);
									board.mousing(true);
									board.pointing(movePoint);
								}
		
								// DL
								else if (((movePoint.x > 64 && movePoint.x < 106) && ((movePoint.y > 190 && movePoint.y < 232) || (movePoint.y > 526 && movePoint.y < 568)))
										|| ((movePoint.x > 148 && movePoint.x < 190) && ((movePoint.y > 316 && movePoint.y < 358) || (movePoint.y > 400 && movePoint.y < 442)))
										|| ((movePoint.x > 190 && movePoint.x < 232) && ((movePoint.y > 64 && movePoint.y < 106)
												|| (movePoint.y > 358 && movePoint.y < 400) || (movePoint.y > 652 && movePoint.y < 694)))
										|| ((movePoint.x > 316 && movePoint.x < 358) && ((movePoint.y > 148 && movePoint.y < 190)
												|| (movePoint.y > 316 && movePoint.y < 358)
												|| (movePoint.y > 400 && movePoint.y < 442) || (movePoint.y > 568 && movePoint.y < 610)))
										|| ((movePoint.x > 358 && movePoint.x < 400) && ((movePoint.y > 190 && movePoint.y < 232) || (movePoint.y > 526 && movePoint.y < 568)))
										|| ((movePoint.x > 400 && movePoint.x < 442) && ((movePoint.y > 148 && movePoint.y < 190)
												|| (movePoint.y > 316 && movePoint.y < 358)
												|| (movePoint.y > 400 && movePoint.y < 442) || (movePoint.y > 568 && movePoint.y < 610)))
										|| ((movePoint.x > 526 && movePoint.x < 568) && ((movePoint.y > 64 && movePoint.y < 106)
												|| (movePoint.y > 358 && movePoint.y < 400) || (movePoint.y > 652 && movePoint.y < 694)))
										|| ((movePoint.x > 568 && movePoint.x < 610) && ((movePoint.y > 316 && movePoint.y < 358) || (movePoint.y > 400 && movePoint.y < 442)))
										|| ((movePoint.x > 652 && movePoint.x < 694) && ((movePoint.y > 190 && movePoint.y < 232) || (movePoint.y > 526 && movePoint.y < 568))))
								{
									board.type(2);
									board.mousing(true);
									board.pointing(movePoint);
								}
		
								// DW
								else if (((movePoint.x > 106 && movePoint.x < 148) && ((movePoint.y > 106 && movePoint.y < 148) || (movePoint.y > 610 && movePoint.y < 652)))
										|| ((movePoint.x > 148 && movePoint.x < 190) && ((movePoint.y > 148 && movePoint.y < 190) || (movePoint.y > 568 && movePoint.y < 610)))
										|| ((movePoint.x > 190 && movePoint.x < 232) && ((movePoint.y > 190 && movePoint.y < 232) || (movePoint.y > 526 && movePoint.y < 568)))
										|| ((movePoint.x > 232 && movePoint.x < 274) && ((movePoint.y > 232 && movePoint.y < 272) || (movePoint.y > 484 && movePoint.y < 526)))
										|| ((movePoint.x > 484 && movePoint.x < 526) && ((movePoint.y > 232 && movePoint.y < 272) || (movePoint.y > 484 && movePoint.y < 526)))
										|| ((movePoint.x > 526 && movePoint.x < 568) && ((movePoint.y > 190 && movePoint.y < 232) || (movePoint.y > 526 && movePoint.y < 568)))
										|| ((movePoint.x > 568 && movePoint.x < 610) && ((movePoint.y > 148 && movePoint.y < 190) || (movePoint.y > 568 && movePoint.y < 610)))
										|| ((movePoint.x > 610 && movePoint.x < 652) && ((movePoint.y > 106 && movePoint.y < 148) || (movePoint.y > 610 && movePoint.y < 652))))
								{
									board.type(3);
									board.mousing(true);
									board.pointing(movePoint);
								}
		
								// TL
								else if (((movePoint.x > 106 && movePoint.x < 148) && ((movePoint.y > 274 && movePoint.y < 316) || (movePoint.y > 442 && movePoint.y < 484)))
										|| ((movePoint.x > 274 && movePoint.x < 316) && ((movePoint.y > 274 && movePoint.y < 316)
												|| (movePoint.y > 442 && movePoint.y < 484)
												|| (movePoint.y > 106 && movePoint.y < 148) || (movePoint.y > 610 && movePoint.y < 652)))
										|| ((movePoint.x > 442 && movePoint.x < 484) && ((movePoint.y > 274 && movePoint.y < 316)
												|| (movePoint.y > 442 && movePoint.y < 484)
												|| (movePoint.y > 106 && movePoint.y < 148) || (movePoint.y > 610 && movePoint.y < 652)))
										|| ((movePoint.x > 610 && movePoint.x < 652) && ((movePoint.y > 274 && movePoint.y < 316) || (movePoint.y > 442 && movePoint.y < 484))))
								{
									board.type(4);
									board.mousing(true);
									board.pointing(movePoint);
								}
		
								else
								{
									board.type(0);
									board.mousing(false);
								}
		
								if (inBoundsOfBoardArea(movePoint))
								{
									if (!board.getPopUpMode()
											|| (board.getPopUpMode() && !board
													.getMouse()))
									{
										Piece testPiece = board.getPiece(
												pointToCord(movePoint.y,
														TOP_LEFT_BOARD, PIXELS_OF_BOX),
												pointToCord(movePoint.x,
														TOP_LEFT_BOARD, PIXELS_OF_BOX));
										if (testPiece != null)
										{
											if (!testPiece.isPinned())
											{
												board.pointing(movePoint);
												board.showHighlight(true);
											}
											else
											{
												board.showHighlight(false);
											}
										}
										else
										{
											board.showHighlight(false);
										}
									}
									else
									{
										board.showHighlight(false);
									}
								}
								else
								{
									board.showHighlight(false);
								}		
							}
						}
					}
				repaint();
				}
		}

		/**
		 * Implements a key listener so that the user can set a letter for the blank tile
		 * @author Evan Cao
		 *
		 */
		private class KeyHandler implements KeyListener
		{

			/**
			 * Recieves the letter input from player
			 */
			public void keyPressed(KeyEvent event)
			{
				if (board.getWildCardMode())
				{
					char letter = event.getKeyChar();
					
					// Checks if a letter is typed
					if (Character.isLetter(letter))
					{
						setWildLetter(selectedPiece, letter);
						selectedPiece.setLocation(ON_BOARD);
						board.insertPiece(selectedPiece,
								selectedPiece.getWildRow(),
								selectedPiece.getWildCol());
						whichPlayer().remove(selectedPiece);
						selectedPiece.selectedPiece(false);
						selectedPiece = null;
						board.setWildCardMode(false);
					}
				}
				repaint();

			}

			/**
			 * Unused
			 */
			public void keyReleased(KeyEvent arg0)
			{				
			}

			/**
			 * Unused
			 */
			public void keyTyped(KeyEvent arg0)
			{		
			}
		}

	}

}
