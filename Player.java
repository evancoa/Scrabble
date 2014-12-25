import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * The Player class which manages the players rack and scores
 * @author Jashanjot S Sohanpal, Evan Cao
 *
 */
public class Player extends Rectangle
{

	protected ArrayList<Piece> playersPieces;
	private int score = 0;
	private int endScore;
	private boolean swap;

	/**
	 * Constructor for the player
	 * Sets up the array to keep track of the rack
	 */
	public Player()
	{
		playersPieces = new ArrayList<Piece>();
		swap = false;
	}

	/**
	 * Set the tiles to be swapped
	 * 
	 * @param set the set of pieces to swap
	 */
	public void setSwap(boolean set)
	{
		if (set)
		{
			for (Piece tile : playersPieces)
			{
				tile.setSwap(true);
			}

		}
		else
		{
			for (Piece tile : playersPieces)
			{
				tile.setSwap(false);
				tile.setSwapSelect(false);
			}

		}

	}

	/** Adds a piece to the player's rack
	* @param piece the piece to add
	*/
	
	public void add(Piece piece)
	{
		playersPieces.add(piece);
	}
	
 /** Removes a piece from the player's rack
	* @param piece the piece to remove
	*/
	public void remove(Piece piece)
	{
		int indexOf = playersPieces.indexOf(piece);
		playersPieces.set(indexOf, null);
	}

	/**
	 * Removes a piece from the AI's rack
	 * 
	 * @param piece the piece to remove
	 */
	public void removeAI(Piece piece)
	{

		for (int i = 0; i < playersPieces.size(); i++)
		{
			if (playersPieces.get(i) != null && playersPieces.get(i).theLetter() == piece.theLetter())
			{
				playersPieces.set(i, null);
				return;
			}
		}
	}

	/**
	 * Gets the number of tiles the player has
	 * 
	 * @return the number of pieces the player has
	 */
	public int numOfPieces()
	{
		return playersPieces.size();
	}

	/**
	 * Draw the tiles
	 * 
	 * @param g the pictures to draw for the tiles
	 */
	public void draw(Graphics g)
	{
		for (int index = 0; index < playersPieces.size(); index++)
		{
			if (playersPieces.get(index) != null)
				playersPieces.get(index).draw(g, index);
		}
	}

	/**
	 * Gets the all the player's pieces
	 * 
	 * @return all the player's pieces
	 */
	public ArrayList<Piece> pieces()
	{
		return playersPieces;
	}

	/**
	 * Adds another piece inside the current list of pieces
	 * 
	 * @param piece the piece to add
	 * @param index where the piece is placed in the rack
	 */
	public void insertPiece(Piece piece, int index)
	{
		int indexOf = playersPieces.indexOf(piece);

		if (playersPieces.get(index) == null)
		{
			playersPieces.set(index, piece);
			playersPieces.set(indexOf, null);
		}
		else if (indexOf > index)
		{
			playersPieces.add(index, piece);
			playersPieces.remove(indexOf + 1);
		}
		else if (indexOf < index)
		{
			playersPieces.add(index + 1, piece);
			playersPieces.remove(indexOf);
		}
	}

	/**
	 * Adds a piece back to the rack from the board
	 * 
	 * @param index the place to add on the rack
	 * @param piece the piece to add back to the rack
	 */
	public void addFromBoard(int index, Piece piece)
	{
		if (playersPieces.get(index) == null)
		{
			playersPieces.remove(index);
			playersPieces.add(index, piece);
			return;
		}

		playersPieces.add(index, piece);

		for (int index1 = 0; index < playersPieces.size(); index1++)
		{
			if (playersPieces.get(index1) == null)
			{
				playersPieces.remove(index1);
				return;
			}
		}

	}

	/**
	 * Gets the player's pieces at a certain index
	 * 
	 * @param index the location of the piece to get
	 * @return the piece at the specified index
	 */
	public Piece getPiece(int index)
	{
		return playersPieces.get(index);
	}

	/**
	 * Adds the score
	 * 
	 * @param score the current score
	 */
	public void score(int score)
	{
		this.score += score;
	}

	/**
	 * Gets the score of the player
	 * 
	 * @return the current score
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * Sets it to swap mode
	 * 
	 * @param set the tiles to enter swap mode
	 */
	public void setSwapMode(boolean set)
	{
		swap = set;
	}

	/**
	 * Gets the state of whether to swap or not
	 * 
	 * @return true to be able to get swapped
	 */
	public boolean getSwap()
	{
		return swap;
	}

	/**
	 * Fills the player rack with tiles
	 * 
	 * @param bag the bag of tiles
	 * @param scrabble the game
	 */
	public void fillPieces(Bag bag, Scrabble scrabble)
	{
		int numOfNulls = numOfNulls();
		if (bag.bagSize() >= numOfNulls)
		{
			removeNulls(numOfNulls);
			for (int index = playersPieces.size(); index < 7; index++)
			{
				Piece temp = bag.getPiece();
				scrabble.addRemoveFromBag(temp, temp.getThisLocation(), 2, this);
			}
			return;
		}
		else
		{
			removeNulls(bag.bagSize());
			for (int index = playersPieces.size(); index < 7; index++)
			{
				Piece temp = bag.getPiece();
				scrabble.addRemoveFromBag(temp, temp.getThisLocation(), 2, this);
			}
			return;
		}

	}

	/**
	 * Finds the number of nulls in the players rack
	 * 
	 * @return the total number of nulls
	 */
	public int numOfNulls()
	{
		int numOfNulls = 0;
		for (int index = 0; index < playersPieces.size(); index++)
			if (playersPieces.get(index) == null)
				numOfNulls++;

		return numOfNulls;
	}

	/**
	 * Removes all the nulls in the players rack
	 * 
	 * @param numberOfNulls the number of current nulls
	 */
	public void removeNulls(int numberOfNulls)
	{
		int nulls = 0;
		for (int index = 0; index < playersPieces.size()
				&& nulls != numberOfNulls; index++)
		{
			if (playersPieces.get(index) == null)
			{
				nulls++;
				playersPieces.remove(index);
				index--;
			}
		}
	}

	/**
	 * Changes the score to a new value
	 * 
	 * @param score the score to change
	 * @return the new modified score
	 */
	public void changeScore(int score)
	{
		this.score = score;
	}

	/**
	 * Returns the endgame score of the player
	 * 
	 * @return the score at the end
	 */
	public int endScore()
	{
		return endScore;
	}

	/**
	 * Calculates the end game score by subtracting tile values from current score
	 * 
	 * @param otherPlayer the opponent you are playing against
	 */
	public void endGameScore(Player otherPlayer)
	{
		endScore = score;
		if (playersPieces.size() == 0)
		{
			addEndScore(otherPlayer);
			return;
		}
		for (int index = 0; index < playersPieces.size(); index++)
			if (playersPieces.get(index) != null)
				score -= playersPieces.get(index).value();
	}

	/**
	 * Adds bonus score for the player that won
	 * 
	 * @param otherPlayer the player to add the score to
	 */
	private void addEndScore(Player otherPlayer)
	{
		for (int index = 0; index < otherPlayer.playersPieces.size(); index++)
			score += otherPlayer.playersPieces.get(index).value();
	}

	/**
	 * Swaps tiles from rack
	 * 
	 * @param toSwap the pieces to swap
	 * @param piece the piece to swap it with
	 */
	public void swapTiles(Piece toSwap, Piece replace)
	{
		playersPieces.remove(toSwap);
		playersPieces.add(replace);
	}
}
