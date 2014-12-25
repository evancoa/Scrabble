import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * The Piece class which keeps tracks of the tiles score, images and board position
 * @author Jashanjot S Sohanpal, Evan Cao
 */
public class Piece extends Rectangle
{
	// Initialize image variables
	private Image image;
	private Image imageBoard;
	private Image imagePlain;
	private Image plainBig;
	private Image imagePlainBoard;
	private Image imagePlainBlank;
	private Image swapBorder;
	
	// Initialize variables for tile specifications
	private char letter;
	private int value;
	private int location;
	private boolean pinnedToBoard;
	public boolean isBeingDragged;
	private boolean selected;
	private Image imageGlow;
	private boolean wildCard;
	private int wildRow;
	private int wildCol;
	private boolean swap;
	private boolean swapSelect;

	// Constant that details on the special letter slots on the board
	final private int[][] scoreGrid = {
			{ 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1 },
			{ 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1 },
			{ 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1 },
			{ 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1 },
			{ 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1 },
			{ 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1 },
			{ 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2 },
			{ 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1 } };

	/**
	 * Constructor for a tile
	 * @param row the row of the tile
	 * @param column the column of the tile
	 * @param letter the letter of the tile
	 * @param value the value of the tile
	 * @param image the string in order to import the image
	 * @param location the location of the tile
	 */
	public Piece(int row, int column, char letter, int value, String image,
			int location)
	{
		super(row * Scrabble.PIXELS_OF_BOX + Scrabble.TOP_LEFT_BOARD, column
				* Scrabble.PIXELS_OF_BOX + Scrabble.TOP_LEFT_BOARD,
				Scrabble.PIXELS_OF_BOX, Scrabble.PIXELS_OF_BOX);

		this.letter = letter;
		this.value = value;
		this.image = new ImageIcon("Game Resources/tiles/" + image + ".png")
				.getImage();
		this.imageGlow = new ImageIcon("Game Resources/tilesGlow/" + image
				+ "G.png").getImage();
		this.imageBoard = new ImageIcon("Game Resources/tilesBoard/" + image
				+ "B.png").getImage();
		this.imagePlain = new ImageIcon("Game Resources/plain/" + letter
				+ ".png").getImage();
		this.imagePlainBoard = new ImageIcon("Game Resources/plainBoard/"
				+ letter + "P.png").getImage();
		this.swapBorder = new ImageIcon("Game Resources/plain/swapBorder.png")
				.getImage();
		this.location = location;
		pinnedToBoard = false;
		isBeingDragged = false;
		selected = false;
		wildCard = false;
		swap = false;

		// Checks if the tile is a wild card
		if (letter == '0')
		{
			wildCard = true;
		}
	}

	/**
	 * Gets a letter
	 * 
	 * @return letter of the tile
	 */
	public char theLetter()
	{
		return letter;
	}

	/**
	 * Gets the value of the tile
	 * 
	 * @return value the value of the tile
	 */
	public int value()
	{
		return value;
	}

	/**
	 * Gets the location
	 * 
	 * @return location the location of the tile
	 */
	public int getThisLocation()
	{
		return location;
	}

	/**
	 * Checks to see if it is a blank tile
	 * 
	 * @return wildCard if the tile is a blank tile
	 */
	public boolean isWildCard()
	{
		return wildCard;
	}

	/**
	 * Sets the x,y location of the blank tile
	 * 
	 * @param row the row of the blank tile
	 * @param col the column of the blank tile
	 */
	public void setWildRowCol(int row, int col)
	{
		wildRow = row;
		wildCol = col;
	}

	/**
	 * Gets the row of the blank tile
	 * 
	 * @return wildRow the row of the blank tile
	 */
	public int getWildRow()
	{
		return wildRow;
	}

	/**
	 * Gets the column of the blank tile
	 * 
	 * @return wildCol the column of the blank tile
	 */
	public int getWildCol()
	{
		return wildCol;
	}

	/**
	 * Sets all tiles to be capable of swap
	 * 
	 * @param set the tiles to be swapped
	 */
	public void setSwap(boolean set)
	{
		swap = set;
	}

	/**
	 * Selects the tile to be swapped
	 * 
	 * @param set the selected tiles to be swapped
	 */
	public void setSwapSelect(boolean set)
	{
		swapSelect = set;
	}

	/**
	 * Changes the wild card images to the selected letter image
	 * @param letter the letter chosen by the user
	 */
	public void changeLetter(char letter)
	{
		this.letter = Character.toUpperCase(letter);
		this.imageGlow = new ImageIcon("Game Resources/blankGlow/" + letter
				+ "BG.png").getImage();
		this.imageBoard = new ImageIcon("Game Resources/Blank/" + letter
				+ "B.png").getImage();
		this.plainBig = new ImageIcon("Game Resources/plainBig/" + letter
				+ ".png").getImage();
		this.imagePlainBoard = new ImageIcon("Game Resources/plainBoardBlank/"
				+ letter + "B.png").getImage();
	}

	/**
	 *	Changes a set wildcard tile back into a blank tile and updates images
	 */
	public void changeToWild()
	{
		this.letter = '0';
		this.imageGlow = new ImageIcon("Game Resources/tilesGlow/" + "00"
				+ "G.png").getImage();
		this.imageBoard = new ImageIcon("Game Resources/tilesBoard/" + "00"
				+ "B.png").getImage();
		this.imagePlainBoard = new ImageIcon("Game Resources/plainBoard/" + "0"
				+ "P.png").getImage();
	}

	/** Sets another location
	 * @param change the new location
	 */
	public void setLocation(int change)
	{
		location = change;
	}

	/**
	 * Moves a piece on the board
	 * 
	 * @param initialPos the place where the piece last moved used to be
	 * @param finalPos the point where the piece last moved was moved to
	 */
	public void move(Point initialPos, Point finalPos)
	{
		// Finds the change in the change in the original and place moved to of
		// the piece
		int y = finalPos.y - initialPos.y;
		int x = finalPos.x - initialPos.x;

		// Moves the x and y coordinates of the piece to the new position
		translate(x, y);
	}

	/** Sets it to be dragged
	 * @param drag true the tile is being dragged, false the tile is not being dragged
	 */
	public void dragging(boolean drag)
	{
		isBeingDragged = drag;
	}

	/** Checks to see if the piece is being selected
	 * @param isSelected true the piece is being selected, false if the piece is not selected
	 */
	public void selectedPiece(boolean isSelected)
	{
		selected = isSelected;
	}

	/**
	 * Draws the piece on the board depending on its position
	 */
	public void draw(Graphics g)
	{
		if (!swap)
			if (!Scrabble.getFromBoard())
			{
				if (Scrabble.getTileGraphics())
					g.drawImage(imageGlow, this.x, this.y, null);
				else
					g.drawImage(imagePlain, this.x, this.y, null);
			}
			else
			{
				if (Scrabble.getTileGraphics())
					g.drawImage(imageGlow, this.x - 11, this.y - 11, null);
				else
				{
					if (isWildCard())
						g.drawImage(plainBig, this.x - 11, this.y - 11, null);
					else
						g.drawImage(imagePlain, this.x - 11, this.y - 11, null);
				}
			}

	}

	/**
	 * Draws the piece on the board depending on its position
	 */
	public void draw(Graphics g, int index)
	{
		if (!selected)
		{
			if (!isBeingDragged)
			{
				this.x = 768 + 64 * index;
				this.y = 396;
				if (swapSelect)
				{
					if (Scrabble.getTileGraphics())
						g.drawImage(imageGlow, this.x, this.y, null);
					else
					{
						g.drawImage(imagePlain, this.x, this.y, null);
						g.drawImage(swapBorder, this.x, this.y, null);
					}
				}

				else
				{
					if (Scrabble.getTileGraphics())
						g.drawImage(image, this.x, this.y, null);
					else
						g.drawImage(imagePlain, this.x, this.y, null);
				}
				return;
			}
		}
	}

	/**
	 * Draws the piece on the board depending on its position
	 */
	public void draw(Graphics g, int row, int colum)
	{
		if (!isBeingDragged)
		{
			this.x = Scrabble.TOP_LEFT_BOARD + Scrabble.PIXELS_OF_BOX * colum;
			this.y = Scrabble.TOP_LEFT_BOARD + Scrabble.PIXELS_OF_BOX * row;

		}
		{
			if (Scrabble.getTileGraphics())
				g.drawImage(imageBoard, this.x, this.y, null);
			else
				g.drawImage(imagePlainBoard, this.x - 1, this.y - 1, null);
		}
	}

	/** Pins the tile to the board
	 * 
	 */
	public void pinToBoard()
	{
		pinnedToBoard = true;
	}

	/** Checks to see if the tile is pinned to the board
	 * @return pinntedToBoard if it is pinned to the board
	 */
	public boolean isPinned()
	{
		return pinnedToBoard;
	}

	/** Finds the score of the pinned word
	 * @return score the score of the word placed
	 */
	public int score()
	{
		if (this.isPinned())
			return value;

		return value
				* scoreGrid[(this.y - Scrabble.TOP_LEFT_BOARD)
						/ Scrabble.PIXELS_OF_BOX][(this.x - Scrabble.TOP_LEFT_BOARD)
						/ Scrabble.PIXELS_OF_BOX];
	}

	/** Finds the score that the AI makes
	 * @param board the scrabble board
	 * @return value the score of the AI
	 */
	public int scoreAI(Board board)
	{
		if (this.isPinned())
			return value;

		return value
				* scoreGrid[board.rowOfPiece(this)][board.colOfPiece(this)];
	}

}
