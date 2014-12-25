import java.util.ArrayList;

/**
 * AI class which finds the AI's best move and does it on the board
 * @author Jashanjot S Sohanpal
 *
 */
public class AI extends Player{

        /**
         * Makes instance of class
         */
        public AI(){
                super();
        }
        
        // The combinations of pieces variable
        private ArrayList<ArrayList<Piece>> combinations;
        
        /**
         * Finds ever combination of pieces in alphabetical order
         */
        public void allCombinations(){
                // Sorts the AI's rack
                combinations = new ArrayList<ArrayList<Piece>>();
                ArrayList<Piece> pieces = playersPieces;
                this.removeNulls(numOfNulls());
                for (int index = 0; index < pieces.size();index++){
                        Piece temp = pieces.get(index);
                        char compareWord = pieces.get(index).theLetter();
                        int lowerIndex = index-1;

                        while (lowerIndex >=0 && compareWord < pieces.get(lowerIndex).theLetter()){
                                pieces.set(lowerIndex+1, pieces.get(lowerIndex));
                                lowerIndex--;
                        }

                        pieces.set(lowerIndex+1,temp);
                }
                
                // Makes a list of all combinations through the recursive combine method
                combine(pieces, 0, new ArrayList<Piece>());
                insertionSort(combinations);
                combinations.remove(0);
                
                return;
        }

        /**
         * Combines pieces together in different orders
         * @param numbers the pieces to make different combinations of
         * @param index the index where the recursion is up to
         * @param combinePieces the list of the combined pieces
         */
        private void combine(ArrayList<Piece> numbers, int index,ArrayList<Piece> combinePieces) {
                // Leaves when you get to end of players pieces
                if (index == numbers.size()) {
                        combinations.add(combinePieces);
                } 
                // Includes the current piece or doesn't include it in combination
                else {
                        ArrayList<Piece> Include = new ArrayList<Piece>();

                        for (int i = 0; i < combinePieces.size(); i++)
                                Include.add(combinePieces.get(i));

                        Include.add(numbers.get(index));

                        // Recalls method for next run
                        combine(numbers, ++index, Include);
                        combine(numbers, index, combinePieces);
                }
        }

        //Variables for making legal words such as the letters it must include
        private ArrayList<String> wordsToDisplay = new ArrayList<String>();
        private ArrayList<Character> mustUse;
        private ArrayList<Character> mustUseVert;
        private Board testBoard;
        
        /**
         * Finds and makes the move for the AI
         * @param board access to the board
         * @param dictionary access to the dictionary
         * @param game access to the main game
         * @param moves number of moves done
         * @return 1 if move was done, 2 if skipping turn and 3 if swapping pieces
         */
        public int  allWordsHorizontal(Board board, Dictionary dictionary, Scrabble game, int moves){
                // Initializes variables
                testBoard = board;
                String  topHWord = "";
                int topHScore = 0;
                int topHRow=0;
                int topHCol=0;
                boolean theWay = true;
                boolean found = false;
                ArrayList<Piece> piecesOfTopWord = new ArrayList<Piece>();

                // Goes through ever point on the board
                for (int row = 0; row < 15; row++){
                        for (int col = 0; col < 15; col++) {
                                if (game.numOfMoves()==0){
                                        row = 7;
                                        col = 7;
                                }
                                
                                // The word to make at that point
                                mustUse=new ArrayList<Character>();
                                mustUseVert=new ArrayList<Character>();         
                                int retHorz =  anagramAtSquare(row,col,col,0,false, moves);
                                int retVert = anagramAtSquareVert(row,col,row,0,false, moves);
                        
                                // Puts the word on the board and checks to make sure valid word, and if it is top word it stores information about the word for horizontal words
                                ArrayList<String> allAnagrams = null;
                                if (retHorz!=-1){
                                        allAnagrams = allWords(retHorz, mustUse);
                                        for (int inde = 0; inde < allAnagrams.size(); inde++){
                                                ArrayList<String> test = dictionary.anagram(allAnagrams.get(inde).toLowerCase());
                                                if(test != null){
                                                        for (int all = 0; all < test.size(); all++) {
                                                                if (addToBoardHor(col, row, all, test, board)) {
                                                                        if (game.isValidMove()||moves==0) {
                                                                                ArrayList<ArrayList<Piece>> words = new ArrayList<ArrayList<Piece>>();
                                                                                words.addAll(board.words());
                                                                                if (game.isValidWord(words)){
                                                                                        int trackScore=0;
                                                                                        trackScore=board.scoreAI(words);
                                                                                        if (board.numNonPinned() == 7)
                                                                                                trackScore+=50;
                                                                                        if (trackScore>topHScore && game.getStrength()){
                                                                                                wordsToDisplay = new ArrayList<String>();
                                                                                                for (int index = 0; index < words.size(); index++)
                                                                                                {
                                                                                                        ArrayList<Piece> word = new ArrayList<Piece>();
                                                                                                        word = words.get(index);
                                                                                                        String wordToCheck = "";
                                                                                                        for (int indexInner = 0; indexInner < word.size(); indexInner++)
                                                                                                                wordToCheck += word.get(indexInner).theLetter();

                                                                                                        if (wordToCheck.length() > 1)
                                                                                                                wordsToDisplay.add(wordToCheck);
                                                                                                }

                                                                                                topHWord = test.get(all);
                                                                                                topHScore = trackScore;
                                                                                                topHRow=row;
                                                                                                topHCol=col;
                                                                                                theWay= true;
                                                                                                found = true;
                                                                                                piecesOfTopWord = words.get(0);
                                                                                        } else if (trackScore>topHScore && trackScore<15){
                                                                                                wordsToDisplay = new ArrayList<String>();
                                                                                                for (int index = 0; index < words.size(); index++)
                                                                                                {
                                                                                                        ArrayList<Piece> word = new ArrayList<Piece>();
                                                                                                        word = words.get(index);
                                                                                                        String wordToCheck = "";
                                                                                                        for (int indexInner = 0; indexInner < word.size(); indexInner++)
                                                                                                                wordToCheck += word.get(indexInner).theLetter();

                                                                                                        if (wordToCheck.length() > 1)
                                                                                                                wordsToDisplay.add(wordToCheck);
                                                                                                }

                                                                                                topHWord = test.get(all);
                                                                                                topHScore = trackScore;
                                                                                                topHRow=row;
                                                                                                topHCol=col;
                                                                                                theWay= true;
                                                                                                found = true;
                                                                                                piecesOfTopWord = words.get(0);
                                                                                        }
                                                                                }
                                                                                
                                                                                board.clearNonPinned();
                                                                        } else {
                                                                                board.clearNonPinned();
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                
                                // Puts the word on the board and checks to make sure valid word, and if it is top word it stores information about the word for Vertical words
                                allAnagrams = null;
                                if (retVert!=-1){
                                        allAnagrams = allWords(retVert, mustUse);
                                        for (int inde = 0; inde < allAnagrams.size(); inde++){
                                                ArrayList<String> test = dictionary.anagram(allAnagrams.get(inde).toLowerCase());
                                                if(test != null){
                                                        for (int all = 0; all < test.size(); all++) {
                                                                if (addToBoardVer(col, row, all, test, board)) {
                                                                        if (game.isValidMove()||moves==0) {
                                                                                ArrayList<ArrayList<Piece>> words = new ArrayList<ArrayList<Piece>>();
                                                                                words.addAll(board.words());
                                                                                if (game.isValidWord(words)){
                                                                                        int trackScore=0;
                                                                                        trackScore=board.scoreAI(words);
                                                                                        if (board.numNonPinned() == 7)
                                                                                                trackScore+=50;
                                                                                        if (trackScore>topHScore && game.getStrength()){
                                                                                                wordsToDisplay = new ArrayList<String>();
                                                                                                for (int index = 0; index < words.size(); index++)
                                                                                                {
                                                                                                        ArrayList<Piece> word = new ArrayList<Piece>();
                                                                                                        word = words.get(index);
                                                                                                        String wordToCheck = "";
                                                                                                        for (int indexInner = 0; indexInner < word.size(); indexInner++)
                                                                                                                wordToCheck += word.get(indexInner).theLetter();

                                                                                                        if (wordToCheck.length() > 1)
                                                                                                                wordsToDisplay.add(wordToCheck);
                                                                                                }
                                                                                                
                                                                                                topHWord = test.get(all);
                                                                                                topHScore = trackScore;
                                                                                                topHRow=row;
                                                                                                topHCol=col;
                                                                                                theWay=false;
                                                                                                found = true;
                                                                                                piecesOfTopWord = words.get(0);
                                                                                        } else if (trackScore>topHScore && trackScore<15){
                                                                                                wordsToDisplay = new ArrayList<String>();
                                                                                                for (int index = 0; index < words.size(); index++)
                                                                                                {
                                                                                                        ArrayList<Piece> word = new ArrayList<Piece>();
                                                                                                        word = words.get(index);
                                                                                                        String wordToCheck = "";
                                                                                                        for (int indexInner = 0; indexInner < word.size(); indexInner++)
                                                                                                                wordToCheck += word.get(indexInner).theLetter();

                                                                                                        if (wordToCheck.length() > 1)
                                                                                                                wordsToDisplay.add(wordToCheck);
                                                                                                }
                                                                                                
                                                                                                topHWord = test.get(all);
                                                                                                topHScore = trackScore;
                                                                                                topHRow=row;
                                                                                                topHCol=col;
                                                                                                theWay=false;
                                                                                                found = true;
                                                                                                piecesOfTopWord = words.get(0);
                                                                                        }
                                                                                }
                                                                                
                                                                                board.clearNonPinned();
                                                                        } else {
                                                                                board.clearNonPinned();
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                
                                if (game.numOfMoves()==0){
                                        row = 15;
                                        col = 15;
                                }
                                
                        }
                }
                
                System.out.println("Top Word: " + topHWord + " Score: " + topHScore + " Row: " + topHRow + " Col: " + topHCol+ " HorOrVert: " + theWay);
                for (int i = 0; i < piecesOfTopWord.size(); i++) {
                        System.out.print(piecesOfTopWord.get(i).theLetter());
                }
                System.out.println();
                
                for (int i = 0; i < playersPieces.size(); i++) {
                        System.out.print(playersPieces.get(i).theLetter());
                }
                System.out.println();
                
                // Returns what move to make
                if (found){
                        putOnBoard(topHRow, topHCol, theWay,topHWord, piecesOfTopWord, board);
                        return 1;
                }
                else if (!game.isBagEmpty()){
                        return 2;
                }else
                        return 3;
        }
        
        /**
         * Finds the words the AI made
         * @return the words the AI made
         */
        public ArrayList<String> AIwords(){
                return wordsToDisplay;
        }
        
        /**
         * Makes the move for the AI
         * @param row row to start placing on
         * @param col column to start placing on
         * @param direction direction the word will go
         * @param topWord the word to place
         * @param piecesOfTopWord the pieces needed to make the word
         * @param board the board to place on
         */
        private void putOnBoard(int row, int col, boolean direction,String topWord, ArrayList<Piece> piecesOfTopWord,Board board){
    		
    		if (direction){
    			for (int i = 0; i < piecesOfTopWord.size(); i++) {
    				if (piecesOfTopWord.get(i).isPinned()){
    					piecesOfTopWord.remove(piecesOfTopWord.get(i));
    					i--;
    				}
    			}
    			
    			int index = 0;
    			for (int i = 0; index <piecesOfTopWord.size(); i++) {
    				
    				if (board.getPiece(row, col+i)==null){
    					
    					Piece toPlace = null;
    					for (int r = 0; r < playersPieces.size(); r++) {
    						System.out.println(piecesOfTopWord.get(index).theLetter());
    						if (playersPieces.get(r) != null && playersPieces.get(r).theLetter() == piecesOfTopWord.get(index).theLetter()){
    							toPlace = playersPieces.get(r);
    							break;
    						}
    					}
    					
    					
    					board.insertPiece(toPlace, row, col + i);
    					toPlace.x = (col+i)*42 + 64;
    					toPlace.y = (row)*42 + 64;
    					this.removeAI(toPlace);
    					index++;
    				}
    			}
    		}else{
    			for (int i = 0; i < piecesOfTopWord.size(); i++) {
    				if (piecesOfTopWord.get(i).isPinned()){
    					piecesOfTopWord.remove(piecesOfTopWord.get(i));
    					i--;
    				}
    			}
    			
    			int index = 0;
    			for (int i = 0; index <piecesOfTopWord.size(); i++) {
    				if (board.getPiece(row+i, col)==null){
    					
    					Piece toPlace = null;
    					for (int r = 0; r < playersPieces.size(); r++) {
    						System.out.println(piecesOfTopWord.get(index).theLetter());
    						if (playersPieces.get(r) != null && playersPieces.get(r).theLetter() == piecesOfTopWord.get(index).theLetter()){
    							toPlace = playersPieces.get(r);
    							break;
    						}
    					}
    					
    					
    					board.insertPiece(toPlace, row+i, col);
    					toPlace.y = (row+i)*42 + 64;
    					toPlace.x = (col)*42 + 64;
    					this.removeAI(toPlace);
    					index++;
    				}
    					
    			}
    		}
    	}
        
        /**
         * Tries to place pieces on the board horizontally
         * @param col the starting column
         * @param row the starting row
         * @param all the index of word to place on board
         * @param test the list of words
         * @param board the board to place on
         * @return true if word can be placed and false if it cannot be placed
         */
        private boolean addToBoardHor(int col,int row,int all, ArrayList<String> test, Board board){
                int colToStart = col;
                String onBoard = test.get(all).toUpperCase();
                int newIndex = 0;
                if (col-1>=0 && board.getPiece(row, col-1)!= null){
                        int index = col-1;
                        while (index >=0 &&board.getPiece(row, index)!= null){
                                index--;
                        }
                        index++;
                        
                        while (index<col){
                                if (newIndex<onBoard.length() && board.getPiece(row, index).theLetter() != onBoard.charAt(newIndex)){
                                        board.clearNonPinned();
                                        return false;
                                }
                                newIndex++;
                                index++;
                        }
                }
                int chars = newIndex;
                for (; chars < onBoard.length(); chars++) {
                        char toFind = onBoard.charAt(chars);
                        if (colToStart == 15){
                                board.clearNonPinned();
                                return false;
                        }
                        
                        if (chars == onBoard.length()-1 && colToStart+1 < 15 && board.getPiece(row, colToStart+1)!= null){
                                board.clearNonPinned();
                                return false;
                        }
                        
                        Piece onB = board.getPiece(row, colToStart);
                        if(onB!= null && onB.theLetter()!=toFind){
                                board.clearNonPinned();
                                return false;
                        }
                        else if (onB== null){
                                Piece toAddToBoard = null;
                                for (int allPieces = 0; allPieces < this.playersPieces.size() && toAddToBoard==null; allPieces++) {
                                        if (this.playersPieces.get(allPieces).theLetter()==toFind){
                                                toAddToBoard = this.playersPieces.get(allPieces);
                                        }
                                }
                                if (toAddToBoard==null){
                                        board.clearNonPinned();
                                        return false;
                                }
                                else
                                        board.add(toAddToBoard, row, colToStart);
                        }
                        colToStart++;
                }
                return true;
        }
        
        /**
         * Tries to place pieces on the board vertically
         * @param col the starting column
         * @param row the starting row
         * @param all the index of word to place on board
         * @param test the list of words
         * @param board the board to place on
         * @return true if word can be placed and false if it cannot be placed
         */
        private boolean addToBoardVer(int col,int row,int all, ArrayList<String> test, Board board){
                int rowToStart = row;
                String onBoard = test.get(all).toUpperCase();
                int newIndex = 0;
                if (row-1>=0 && board.getPiece(row-1, col)!= null){
                        int index = row-1;
                        while (index >=0 && board.getPiece(index, col)!= null){
                                index--;
                        }
                        index++;
                        
                        while (index<row){
                                if (newIndex<onBoard.length() && board.getPiece(index, col).theLetter() != onBoard.charAt(newIndex)){
                                        board.clearNonPinned();
                                        return false;
                                }
                                newIndex++;
                                index++;
                        }
                }
                int chars = newIndex;
                for (; chars < onBoard.length(); chars++) {
                        char toFind = onBoard.charAt(chars);
                        if (rowToStart == 15){
                                board.clearNonPinned();
                                return false;
                        }
                        
                        if (chars == onBoard.length()-1 && rowToStart+1 < 15 && board.getPiece(rowToStart+1, col)!= null){
                                board.clearNonPinned();
                                return false;
                        }
                        
                        Piece onB = board.getPiece(rowToStart, col);
                        if(onB!= null && onB.theLetter()!=toFind){
                                board.clearNonPinned();
                                return false;
                        }
                        else if (onB== null){
                                Piece toAddToBoard = null;
                                for (int allPieces = 0; allPieces < this.playersPieces.size() && toAddToBoard==null; allPieces++) {
                                        if (this.playersPieces.get(allPieces).theLetter()==toFind){
                                                toAddToBoard = this.playersPieces.get(allPieces);
                                        }
                                }
                                if (toAddToBoard==null){
                                        board.clearNonPinned();
                                        return false;
                                }
                                else
                                        board.add(toAddToBoard, rowToStart, col);
                        }
                        rowToStart++;
                }
                return true;
        }
        
        /**
         * Recursively finds the max length word that can be placed horizontally at a location
         * @param row the starting row
         * @param column the starting column
         * @param currentColumn the column it is checking currently
         * @param change the length of the word
         * @param attached if the word is attached to the main chain
         * @param moves number of moves
         * @return the max length of word and -1 if word cannot be placed
         */
        private int anagramAtSquare(int row, int column, int currentColumn, int change, boolean attached, int moves){
                if (testBoard.getPiece(row, column)!= null)
                        return -1;
                
                if (!attached){
                        if (row + 1 < 15 && testBoard.getPiece(row + 1, currentColumn) != null)
                                attached = true;
                        else if (row - 1 >= 0 && testBoard.getPiece(row - 1, currentColumn) != null)
                                attached = true;
                        else if (column - 1 >= 0 && testBoard.getPiece(row, column - 1) != null) {
                                for (int col = column - 1; col >= 0 && testBoard.getPiece(row, col) != null; col--){
                                        mustUse.add(0, testBoard.getPiece(row, col).theLetter());
                                }
                                attached = true;
                        } else if (currentColumn + 1 < 15 && testBoard.getPiece(row, currentColumn + 1) != null)
                                attached = true;
                }
                if (currentColumn + 1 < 15)
                        if (testBoard.getPiece(row, currentColumn + 1) != null) {
                                mustUse.add(testBoard.getPiece(row, currentColumn + 1).theLetter());
                                change--;
                        }
                
                if (attached || moves==0) {
                        if (change == playersPieces.size()-1)
                                return change+1;
                        else if (currentColumn + 1 >= 15)
                                return (change+1);
                } else if (!attached && change == playersPieces.size()-1 || currentColumn + 1 >=15)
                        return -1;
        
                return anagramAtSquare(row, column,currentColumn+1, change+1, attached, moves);
        }
        
        /**
         * Recursively finds the max length word that can be placed vertically at a location
         * @param row the starting row
         * @param column the starting column
         * @param currentRow the row it is checking currently
         * @param change1 the length of the word
         * @param attached if the word is attached to the main chain
         * @param moves number of moves
         * @return the max length of word and -1 if word cannot be placed
         */
        private int anagramAtSquareVert(int row, int column, int currentRow, int change1, boolean attached, int moves){
                if (testBoard.getPiece(row, column)!= null)
                        return -1;
                
                if (!attached){
                        if (currentRow + 1 < 15 && testBoard.getPiece(currentRow + 1, column) != null){
                                attached = true;
                        }
                        else if (row - 1 >= 0 && testBoard.getPiece(row - 1, column) != null){
                                for (int row1 = row - 1; row1 >= 0 && testBoard.getPiece(row1, column) != null; row1--){
                                        mustUseVert.add(0, testBoard.getPiece(row1, column).theLetter());
                                }
                                attached = true;
                        }else if (column - 1 >= 0 && testBoard.getPiece(currentRow, column - 1) != null) {
                                attached = true;
                        } else if (column + 1 < 15 && testBoard.getPiece(currentRow, column + 1) != null){
                                attached = true;
                        }
                }
                
                if (currentRow + 1 < 15)
                        if (testBoard.getPiece(currentRow+1, column) != null) {
                                mustUseVert.add(testBoard.getPiece(currentRow+1, column).theLetter());
                                change1--;
                        }
                
                if (attached) {
                        if (change1 == playersPieces.size()-1){
                                return change1+1;
                        }
                        else if (currentRow + 1 >= 15){
                                return (change1+1);
                        }
                } else if (!attached && change1 == playersPieces.size()-1 || currentRow + 1 >=15)
                        return -1;
        
                return anagramAtSquareVert(row, column,currentRow+1, change1+1, attached, moves);
        }
        
        /**
         * Sorts the ArrayList of pieces alphabetically
         * @param combinations2 the list to sort
         */
        private void insertionSort(ArrayList<ArrayList<Piece>> combinations2) {
                for (int index = 1; index < combinations2.size(); index++) {
                        ArrayList<Piece> compareWord = combinations2.get(index);

                        int lowerIndex = index-1;

                        while (lowerIndex >=0 && compareWord.size()<combinations2.get(lowerIndex).size()){
                                combinations2.set(lowerIndex+1, combinations2.get(lowerIndex));
                                lowerIndex--;
                        }

                        combinations2.set(lowerIndex+1,compareWord);
                }
        }
        
        /**
         * Finds all sequences of letters that can be laced at a certain location
         * @param ret the max size of the word
         * @param mustIn characters it must include
         * @return the list of all anagrams
         */
        private ArrayList<String> allWords(int ret, ArrayList<Character> mustIn){
                String mustContain = "";
                for (int inde = 0; inde < mustIn.size(); inde++) {
                        mustContain+=mustIn.get(inde);
                }
                
                ArrayList<String> allAnagrams = new ArrayList<String>();
                
                for (int inde = 0; inde < combinations.size(); inde++){
                        ArrayList<Piece> temp = combinations.get(inde);
                        if (temp.size()<=ret){
                                String lettersToAdd = "";
                                
                                for (int i = 0; i < temp.size(); i++)
                                        lettersToAdd +=temp.get(i).theLetter();
                                
                                allAnagrams.add(mustContain+""+lettersToAdd);
                        }
                }
                
                return allAnagrams;
        }
}
