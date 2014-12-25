
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * An implementation of the Trie data structure.
 * @author Jashanjot S Sohanpal
 */
public class Dictionary {
	private static HashMap<String, ArrayList<String>> dictionary;

	/**
	 * Construcs the dictionary and imports it from a text file
	 */
	public Dictionary() {
		dictionary = new HashMap<String, ArrayList<String>>();

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("TWL06.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int i = 0;
		while (scanner.hasNext()) {
			i++;
			String word = scanner.next().toLowerCase();

			char[] chars = word.toCharArray();
			Arrays.sort(chars);
			String word1 = "";
			for (int index = 0; index < chars.length; index++)
				word1 += chars[index];

			ArrayList<String> temp = (ArrayList<String>) dictionary.get(word1);

			if (temp==null){
				ArrayList<String> input = new ArrayList<String>();
				input.add(word);
				dictionary.put(word1,input);
			}else{
				temp.add(word);
				dictionary.put(word1,temp);
			}
		}
	}

	/**
	 * Check if the word exists in the dictionary
	 * @param sequence the word to check
	 * @return
	 */
	public boolean contains(String sequence) {
		if (sequence.length()==1)
			return true;
		
		char[] chars = sequence.toCharArray();
		Arrays.sort(chars);
		String word1 = "";
		for (int index = 0; index < chars.length; index++)
			word1 += chars[index];

		ArrayList<String> list = new ArrayList<String> ();
		list = (ArrayList<String>) dictionary.get(word1);

		if (list==null)
			return false;
		
		if (list.contains(sequence))
			return true;

		return false;
	}
	
	/**
	 * Finds all the words that are possible to be made given
	 * a sequence of letters
	 * @param sequence the sequence of letters
	 * @return
	 */
	public ArrayList<String>  anagram(String sequence) {
		if (sequence.length()==1)
			return null;
		
		char[] chars = sequence.toCharArray();
		Arrays.sort(chars);
		String word1 = "";
		for (int index = 0; index < chars.length; index++)
			word1 += chars[index];

		ArrayList<String> list = new ArrayList<String> ();
		list = (ArrayList<String>) dictionary.get(word1);
		
		return list;
	}
}
