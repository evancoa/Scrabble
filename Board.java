import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;

/**
 * The Board class which keeps track of the tile positions
 * as well as images for mousing over
 * @author Jashanjot S Sohanpal, Evan Cao
 *
 */
public class Board extends Rectangle
{

	// Declares a 2d array to hold pieces
    private Piece[] [] grid;
    
    // Declare images
    private Image tl;
    private Image tw;
    private Image dl;
    private Image dw;
    private Image yesPopUp;
    private Image noPopUp;
    private Image highlight;
    private Image wildBox;
    private int type;
    private Point boxPoint;
    
    private boolean isMousedOver;
    private boolean isSwapMode;
    private boolean showPopUp;
    private boolean showHighlight;
    private boolean wildCardMode;
    private boolean firstTurn;

    private int x;
    private int y;


    // Create the grid and set the special score squares to their respective number
    final private int[] [] scoreGrid = {
	    {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3},
	    {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
	    {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
	    {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
	    {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
	    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    {3, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 3},
	    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1},
	    {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
	    {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
	    {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1},
	    {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3}};

    /**
     *  Constucts the board by setting up a 2d array plus images
     */
    public Board ()
    {

	// Creates a 2d array and add the images
	grid = new Piece [15] [15];
	type = 0;
	showPopUp = true;
	showHighlight = false;
	isSwapMode = false;
	wildCardMode = false;
	firstTurn = true;

	dl = new ImageIcon ("Game Resources/popUps/DL.png").getImage ();
	tl = new ImageIcon ("Game Resources/popUps/TL.png").getImage ();
	dw = new ImageIcon ("Game Resources/popUps/DW.png").getImage ();
	tw = new ImageIcon ("Game Resources/popUps/TW.png").getImage ();
	yesPopUp = new ImageIcon ("Game Resources/layout/yesPopUp.png").getImage ();
	noPopUp = new ImageIcon ("Game Resources/layout/noPopUp.png").getImage ();
	highlight = new ImageIcon ("Game Resources/layout/highlight.png").getImage ();
	wildBox = new ImageIcon ("Game Resources/layout/blankTileDialog.png").getImage ();


    }


    /** Method to check for the first turn
     * @return true if it is the first turn in the game
     */
    public boolean isFirstTurn ()
    {
	return firstTurn;
    }


    /** Sets the first turn
     * @param set the true or false of the set
     */
    public void setFirstTurn (boolean set)
    {
	firstTurn = set;
    }


    private ArrayList < Integer > rowCheck;
    private ArrayList < Integer > colCheck;
    private boolean rowOrCol;

    /** Method to check for the valids moves
     * @param moves the current number of moves
     * @parm txt the area that is being checked
     * @param isComputer to see if the move is being made by the computer
     * @return true if the move is valid
     */
    public boolean validMove (int moves, JTextArea txt, boolean isComputer)
    {
	rowCheck = new ArrayList < Integer > ();
	colCheck = new ArrayList < Integer > ();

	for (int row = 0 ; row < 15 ; row++)
	    for (int col = 0 ; col < 15 ; col++)
		if (grid [row] [col] != null)
		    if (!grid [row] [col].isPinned ())
		    {
			rowCheck.add (row);
			colCheck.add (col);
		    }

	// Checks the row to see if you have placed anything on the row
	if (rowCheck.size () == 0)
	{
	    if (colCheck.size () == 0)
	    {
		if (!isComputer)
		    txt.append ("You have not placed anything on the board! \n");
		return false;
	    }
	}

	// Checks to see that the word you made is greater than 1 letter in length
	else if (rowCheck.size () == 1 && colCheck.size () == 1 && isFirstTurn ())
	{
	    if (!isComputer)
		txt.append ("The word must be greater than 1 letter in length! \n");
	    return false;
	}

	if (allSameInList (rowCheck))
	{
	    rowOrCol = true;
	    if (increasingList (colCheck, rowCheck.get (0), moves))
	    {
		return true;
	    }

	    // If the tiles are not placed adjacently but diagonally then prompt the user to place them linearly
	    if (!isComputer)
		txt.append ("Invalid! Tiles must be placed adjacent to each other linearly! \n");
	}

	// Checks the column to see if the move is valid
	else if (allSameInList (colCheck))
	{
	    rowOrCol = false;
	    if (increasingList (colCheck.get (0), rowCheck, moves))
	    {
		return true;
	    }
	    else
	    {
		if (!isComputer)
		    txt.append ("Invalid! Tiles must be placed adjacent to each other linearly! \n");
	    }
	}

	// Ensures that the user places the tile in only 1 row and column
	else
	{
	    if (!isComputer)
		txt.append ("Invalid! Each tile must be placed in a single row or column! \n");
	}
	return false;
    }


    /** Checks to see if they are all in the same arraylist
    * @param list the arraylist of numbers
	 * @return true or false
	 */
    private boolean allSameInList (ArrayList < Integer > list)
    {
	int index = 1;
	int number = list.get (0);
	while (index < list.size () && list.get (index) == number)
	{
	    index++;
	}

	if (index == list.size ())
	    return true;

	return false;
    }


    ArrayList < Integer > listOfWord;
    
    /** 
	 * @return true if the list of words in contained inside the list
	 */
    private boolean increasingList (ArrayList < Integer > list, int row, int moves)
    {
	listOfWord = new ArrayList < Integer > ();
	int index = list.get (0);
	while (index >= 0 && grid [row] [index] != null)
	    index--;

	index++;

	for (int indexIn = index ; indexIn < 15 && grid [row] [indexIn] != null ; indexIn++)
	    listOfWord.add (indexIn);

	if (moves != 0)
	    if (!isSurrounded ())
		return false;

	if (listOfWord.containsAll (list))
	{
	    return true;
	}

	return false;
    }

/** The 
* @param col the column
* @param list the list being looked at
* @param the moves being made
	 * @return the current row of the piece
	 */
    private boolean increasingList (int col, ArrayList < Integer > list, int moves)
    {
	listOfWord = new ArrayList < Integer > ();
	int index = list.get (0);
	while (index >= 0 && grid [index] [col] != null)
	    index--;

	index++;

	for (int indexIn = index ; indexIn < 15 && grid [indexIn] [col] != null ; indexIn++)
	    listOfWord.add (indexIn);

	if (moves != 0)
	    if (!isSurrounded ())
		return false;

	if (listOfWord.containsAll (list))
	{
	    return true;
	}

	return false;
    }

/** Checks to see if the tile is surrounded
	 */
    private boolean isSurrounded ()
    {

	for (int i = 0 ; i < colCheck.size () ; i++)
	{
	    if (rowCheck.get (i) + 1 < 15)
		if (grid [rowCheck.get (i) + 1] [colCheck.get (i)] != null && grid [rowCheck.get (i) + 1] [colCheck.get (i)].isPinned ())
		    return true;

	    if (rowCheck.get (i) - 1 >= 0)
		if (grid [rowCheck.get (i) - 1] [colCheck.get (i)] != null && grid [rowCheck.get (i) - 1] [colCheck.get (i)].isPinned ())
		    return true;
	}

	for (int i = 0 ; i < rowCheck.size () ; i++)
	{
	    if (colCheck.get (i) + 1 < 15)
		if (grid [rowCheck.get (i)] [colCheck.get (i) + 1] != null && grid [rowCheck.get (i)] [colCheck.get (i) + 1].isPinned ())
		    return true;

	    if (colCheck.get (i) - 1 >= 0)
		if (grid [rowCheck.get (i)] [colCheck.get (i) - 1] != null && grid [rowCheck.get (i)] [colCheck.get (i) - 1].isPinned ())
		    return true;
	}
	return false;
    }


    /**
     * Adds piece to grid
     * @param piece
     * @param row
     * @param colum
     */
    public void add (Piece piece, int row, int colum)
    {
	grid [row] [colum] = piece;
    }


    /**
    *getspiecesfromgrid
    *@paramrow
    *@paramcolumn
    *@return
    */
    public Piece getPiece (int row, int column)
    {
	if (grid [row] [column] != null)
	    return grid [row] [column];
	return null;
    }


    /**
     * removes piece from grid
     * @param row
     * @param colum
     */
    public void remove (int row, int colum)
    {
	grid [row] [colum] = null;
    }


    /**
     * draws board
     * @param g
     */
    public void draw (Graphics g)
    {
	for (int row = 0 ; row < 15 ; row++)
	    for (int column = 0 ; column < 15 ; column++)
		if (grid [row] [column] != null)
		    grid [row] [column].draw (g, row, column);
	if (showPopUp)
	{

	    if (isMousedOver)
	    {
		x = Scrabble.TOP_LEFT_BOARD + Scrabble.PIXELS_OF_BOX * Scrabble.pointToCord (boxPoint.x,
			Scrabble.TOP_LEFT_BOARD, Scrabble.PIXELS_OF_BOX);
		y = Scrabble.TOP_LEFT_BOARD + Scrabble.PIXELS_OF_BOX * Scrabble.pointToCord (boxPoint.y,
			Scrabble.TOP_LEFT_BOARD, Scrabble.PIXELS_OF_BOX);

		if (type == 1)
		    g.drawImage (tw, x, y, null);
		else if (type == 2)
		    g.drawImage (dl, x, y, null);
		else if (type == 3)
		    g.drawImage (dw, x, y, null);
		else if (type == 4)
		    g.drawImage (tl, x, y, null);
	    }
	    g.drawImage (yesPopUp, 45, 720, null);
	}
	else
	    g.drawImage (noPopUp, 45, 720, null);

	if (showHighlight)
	{
	    x = Scrabble.TOP_LEFT_BOARD + Scrabble.PIXELS_OF_BOX * Scrabble.pointToCord (boxPoint.x,
		    Scrabble.TOP_LEFT_BOARD, Scrabble.PIXELS_OF_BOX);
	    y = Scrabble.TOP_LEFT_BOARD + Scrabble.PIXELS_OF_BOX * Scrabble.pointToCord (boxPoint.y,
		    Scrabble.TOP_LEFT_BOARD, Scrabble.PIXELS_OF_BOX);
	    g.drawImage (highlight, x, y, null);
	}

	if (wildCardMode)
	{
	    g.drawImage (wildBox, 0, 0, null);
	}

	return;
    }

/** Sets the mode to wildCardMode         
	 */
    public void setWildCardMode (boolean mode)
    {
	wildCardMode = mode;
    }

/** Checks to see if it is wildCardMode
	 * @return wildCardMode
	 */
    public boolean getWildCardMode ()
    {
	return wildCardMode;
    }


    /** Checks for the mouse over a location
	 */
    public void mousing (boolean mouse)
    {
	isMousedOver = mouse;
    }

/** Sets the type
* @param slot the slot being changed
	 */
    public void type (int slot)
    {
	type = slot;
    }

/** Points to a place on the box
       * @param point the point being pointed to
	 */
    public void pointing (Point point)
    {
	boxPoint = point;
    }

/** Shows the high lighted box
* @param show to see if it needs to be shown
	 */
    public void showHighlight (boolean show)
    {
	showHighlight = show;
    }

/** Gets the showed tile
* @param the tiled that is showed/ highlighted
	 */
    public void getShow (boolean show)
    {
	showPopUp = show;
    }

/** Gets the tile that is being swapped
* @param the swap to see if is capable of being swapped
	 */
    public void getSwap (boolean swap)
    {
	isSwapMode = swap;
    }

/** Gets the mouse cursor
	 * @return whether it is being moused over or not
	 */
    public boolean getMouse ()
    {
	return isMousedOver;
    }

/** Makes the tile pop up from the board by setting it to pop up
* @param set to set it to pop up or not
	 */
    public void setPopUp (boolean set)
    {
	showPopUp = set;
    }


    public boolean getPopUpMode ()
    {
	return showPopUp;
    }


    /**
     * adds piece to grid
     * @param selectedPiece
     * @param row
     * @param col
     */
    public void insertPiece (Piece selectedPiece, int row, int col)
    {
	grid [row] [col] = selectedPiece;
    }


    /**
     * calculates score
     * @param words
     * @return
     */
    public int score (ArrayList < ArrayList < Piece >> words, Player player)
    {
	int score = 0;
	for (int index = 0 ; index < words.size () ; index++)
	{
	    ArrayList < Piece > temp = words.get (index);
	    int scoreMulti = 1;
	    int wordScore = 0;
	    for (int index1 = 0 ; index1 < temp.size () && temp.size () > 1 ; index1++)
	    {
		Piece tempPiece = temp.get (index1);
		wordScore += tempPiece.score ();
		if (!tempPiece.isPinned ())
		    scoreMulti *= scoreGrid [(tempPiece.y - Scrabble.TOP_LEFT_BOARD) / Scrabble.PIXELS_OF_BOX] [(tempPiece.x - Scrabble.TOP_LEFT_BOARD) / Scrabble.PIXELS_OF_BOX];
	    }
	    score += (wordScore * scoreMulti);
	}

	System.out.println (score);
	return score;
    }

/** Calculates the score for the AI
* @param the pieces that the AI is using to create the words
	 * @return the score of the AI
	 */
    public int scoreAI (ArrayList < ArrayList < Piece >> words)
    {
	int score = 0;
	for (int index = 0 ; index < words.size () ; index++)
	{
	    ArrayList < Piece > temp = words.get (index);
	    int scoreMulti = 1;
	    int wordScore = 0;
	    for (int index1 = 0 ; index1 < temp.size () && temp.size () > 1 ; index1++)
	    {
		Piece tempPiece = temp.get (index1);
		wordScore += tempPiece.scoreAI (this);
		if (!tempPiece.isPinned ())
		{
		    scoreMulti *= scoreGrid [rowOfPiece (tempPiece)] [colOfPiece (tempPiece)];
		}
	    }
	    score += (wordScore * scoreMulti);
	}
	return score;
    }

/** To find the row of the piece
* @param piece the row of the current piece
	 * @return the current row of the piece
	 */
    public int rowOfPiece (Piece piece)
    {
	for (int row = 0 ; row < 15 ; row++)
	    for (int column = 0 ; column < 15 ; column++)
		if (grid [row] [column] == piece)
		    return row;
	return -1;
    }

/** To find the column of the piece
* @param piece the column of the current piece
	 * @return the current column of the piece
	 */
    public int colOfPiece (Piece piece)
    {
	for (int row = 0 ; row < 15 ; row++)
	    for (int column = 0 ; column < 15 ; column++)
		if (grid [row] [column] == piece)
		    return column;
	return -1;
    }

/** Finds how many pieces are pinned to the board
	 * @return the number of pieces pinned to the board
	 */
    public int numNonPinned ()
    {
	int number = 0;
	for (int row = 0 ; row < 15 ; row++)
	    for (int column = 0 ; column < 15 ; column++)
		if (grid [row] [column] != null)
		    if (!grid [row] [column].isPinned ())
			number++;

	return number;
    }


    /**
     * pins piece to board
     */
    public void pinToBoard ()
    {
	for (int row = 0 ; row < 15 ; row++)
	    for (int column = 0 ; column < 15 ; column++)
		if (grid [row] [column] != null)
		    grid [row] [column].pinToBoard ();
    }


    /**
     * Clears the board of any tiles placed during this turn
     */
    public ArrayList < Piece > clearBoard ()
    {
	ArrayList < Piece > tempTiles = new ArrayList < Piece > ();
	for (int row = 0 ; row < 15 ; row++)
	{
	    for (int column = 0 ; column < 15 ; column++)
		if (grid [row] [column] != null)
		{
		    if (grid [row] [column].isPinned () == false)
		    {
			if (grid [row] [column].isWildCard ())
			    grid [row] [column].changeToWild ();

			tempTiles.add (grid [row] [column]);
			grid [row] [column] = null;
		    }
		}
	}
	return tempTiles;
    }


    /**
     * makes word from 2 lists
     * @return the list of words
     */
    public ArrayList < ArrayList < Piece >> words ()
    {
	ArrayList < ArrayList < Piece >> words = new ArrayList < ArrayList < Piece >> ();

	boolean sameRowOrCol;

	ArrayList < Piece > word = new ArrayList < Piece > ();
	if (rowOrCol)
	{
	    for (int index = 0 ; index < listOfWord.size () ; index++)
	    {
		word.add (grid [rowCheck.get (0)] [listOfWord.get (index)]);
	    }
	    sameRowOrCol = true;
	}
	else
	{
	    for (int index = 0 ; index < listOfWord.size () ; index++)
	    {
		word.add (grid [listOfWord.get (index)] [colCheck.get (0)]);
	    }
	    sameRowOrCol = false;
	}

	words.add (word);

	if (sameRowOrCol == true)
	{
	    for (int ind = 0 ; ind < colCheck.size () ; ind++)
	    {
		ArrayList < Piece > listOf = new ArrayList < Piece > ();
		int index = rowCheck.get (0);
		while (index >= 0 && grid [index] [colCheck.get (ind)] != null)
		    index--;

		index++;

		for (int indexIn = index ; indexIn < 15 && grid [indexIn] [colCheck.get (ind)] != null ; indexIn++)
		    listOf.add (grid [indexIn] [colCheck.get (ind)]);

		words.add (listOf);
	    }
	}
	else if (sameRowOrCol == false)
	{
	    for (int ind = 0 ; ind < rowCheck.size () ; ind++)
	    {
		ArrayList < Piece > listOf = new ArrayList < Piece > ();
		int index = colCheck.get (0);
		while (index >= 0 && grid [rowCheck.get (ind)] [index] != null)
		    index--;

		index++;

		for (int indexIn = index ; indexIn < 15 && grid [rowCheck.get (ind)] [indexIn] != null ; indexIn++)
		    listOf.add (grid [rowCheck.get (ind)] [indexIn]);

		words.add (listOf);
	    }
	}

	return words;
    }

/** To see if the center piece is used (used for the first turn to ensure that the player goes in the center)
	 * @return true if the center piece is filled
	 */
    public boolean centerIsFilled ()
    {
	if (grid [7] [7] != null)
	    return true;

	return false;
    }

    /** Clears all nonpinned letters from the rack
	 */
    public void clearNonPinned ()
    {
	for (int row = 0 ; row < 15 ; row++)
	{
	    for (int column = 0 ; column < 15 ; column++)
		if (grid [row] [column] != null)
		{
		    if (grid [row] [column].isPinned () == false)
		    {
			if (grid [row] [column].isWildCard ())
			    grid [row] [column].changeToWild ();

			grid [row] [column] = null;
		    }
		}
	}
    }
}
