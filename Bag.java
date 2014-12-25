import java.util.ArrayList;

/**
 * The Bag class which holds all the game tiles
 * 
 * @author Jashanjot S Sohanpal, Evan Cao, Jason Wang
 * 
 */
public class Bag
{

	// Create the pieces and assign the number of that piece type, the letter,
	// and the score value.
	public ArrayList<Piece> pieces;

	// Sets strings which declare the amount of each letter tile to make
	private String[] letters = { "12E1", "9A1", "9I1", "8O1", "6N1", "6R1",
			"6T1", "4L1", "4S1", "4U1", "4D2", "3G2", "2B3", "2C3", "2M3",
			"2P3", "2F4", "2H4", "2V4", "2W4", "2Y4", "1K5", "1J8", "1X8",
			"1Q10", "1Z10", "200" };

	/**
	 * Constructs a bag by going through the arraylist of pieces and creates 100
	 * pieces
	 */
	public Bag()
	{
		pieces = new ArrayList<Piece>(99);

		for (int index = 0; index < letters.length; index++)
		{
			int amount;
			if (index == 0)
			{
				amount = Integer.parseInt(letters[0].substring(0, 2));
				letters[0] = letters[0].substring(2);
			}
			else
			{
				amount = Integer.parseInt(letters[index].substring(0, 1));
				letters[index] = letters[index].substring(1);
			}

			char letter = letters[index].charAt(0);

			int value = Integer.parseInt(letters[index].substring(1));

			for (int index1 = 0; index1 < amount; index1++)
			{
				pieces.add(new Piece(0, 0, letter, value, letters[index],
						Scrabble.ON_BAG));
			}
		}

		// Shuffle the bag to ensure that the pieces are random
		shuffle();
	}

	/**
	 * A getter method to get the number of tiles in the bag
	 * 
	 * @return the number of tiles in the bag
	 */
	public int bagSize()
	{
		return pieces.size();
	}

	/**
	 * A getter method to get a piece from the bag
	 * 
	 * @return the random piece that is taken from the bag
	 */
	public Piece getPiece()
	{
		return pieces.get(0);
	}

	// Which one
	/**
	 * A method to check for if the bag has anymore pieces left in the bag
	 * 
	 * @return true if the bag is not empty
	 */
	public boolean isEmpty()
	{
		return !(pieces.size() > 0);
	}

	/**
	 * A method to check for if the bag has anymore pieces left in the bag
	 * 
	 * @return true if the bag is not empty
	 */
	public boolean isBagEmpty()
	{
		return pieces.size() > 0;
	}

	/**
	 * Shuffles the deck randomly using Fisher-Yates shuffle
	 */
	public void shuffle()
	{
		// Goes through deck switching each card with a random integer which is
		// between the length
		// of the deck and then swapping those 2 positions
		for (int cardIndex = 0; cardIndex < pieces.size(); cardIndex++)
		{
			int randomIndex = (int) (Math.random() * pieces.size());
			Piece temp = pieces.get(cardIndex);
			pieces.set(cardIndex, pieces.get(randomIndex));
			pieces.set(randomIndex, temp);
		}
	}

	/**
	 * Adds a piece back into the bag
	 * 
	 * @param piece the piece to readd
	 */
	public void add(Piece piece)
	{
		pieces.add(piece);
	}

	/**
	 * Removes a piece from the bag
	 */
	public void remove()
	{
		pieces.remove(getPiece());
	}
}
