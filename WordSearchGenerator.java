import java.util.Random;
import java.lang.Math;

/** 
 * WordSearchGenerator.class
 * @Author: Jakob Tak
 * @Date: 24.01.2024
 * @Class: CS&145
 * @Assignment: Assignment1
 */
public class WordSearchGenerator {
    private static final char PLACEHOLDER = 'X';
    private static final int MAX_ATTEMPTS = 100;

    private String[] words;
    private char[][] grid;
    private int maxWordLength;
    private Random random = new Random();

    /**
     * Constructor
     * @param size Width and height of the word search game. Always square shaped.
     * 
     * @return Null
     */
    public WordSearchGenerator(int size) {
        this.maxWordLength = (int) Math.sqrt(2*size*size);
        grid = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = PLACEHOLDER;
            }
        }
    }

    /**
     * Set words to be found
     * @param words
     * 
     * @return Null
     */
    public void setWords(String[] words) {
        this.words = words;
        for (String word: words){
            placeWord(word.toUpperCase()); // set all words in upper case
        }
    }

    /**
     * Returns the words to be found
     * 
     * @return String
     */
    public String[] getWords() {
        return words;
    }

    /**
     * Returns a game to play.
     * Replaces empty spot as a random character from the solution
     * 
     * @return String
     */
    public String print() {
        String game = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == PLACEHOLDER) {
                    game += (char) ('A' + random.nextInt(26));
                } else {
                    game += grid[i][j];
                }
                game += " "; // add a space for better visualization
            }
            game += "\n"; // nextline
        }
        return game.toString();
    }

    /**
     * Returns solution of the game.
     * 
     * @return String
     */
    public String solution() {
        String solution = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                solution += grid[i][j];
                solution += " "; // add a space for better visualization
            }
            solution += "\n"; // nextline
        }
        return solution;
    }

    /**
     * Returns the max length of the word in the game using pythagorean theorem. REF: [L:27]
     * 
     * @return
     */
    public int getMaxWordLength() {
        return maxWordLength;
    }    

    /**
     * Places the word in the board. 
     * If it fails to rocate in given attempts, returns false
     * 
     * @param word
     * @return boolean
     */
    private boolean placeWord(String word) {
        int size = grid.length;
        int wordLength = word.length();
        int attempts = 0; // Limit attempts to place the word to prevent infinite loops

        while (attempts++ < MAX_ATTEMPTS) {
            int direction = random.nextInt(8); // 0 to 7, representing 8 directions
            int rowStart = random.nextInt(size);
            int colStart = random.nextInt(size);
            int rowDelta = 0, colDelta = 0;
            
            switch (direction) {
                case 0: rowDelta = -1; break; // Up
                case 1: rowDelta = 1; break; // Down
                case 2: colDelta = -1; break; // Left
                case 3: colDelta = 1; break; // Right
                case 4: rowDelta = -1; colDelta = -1; break; // Up-Left
                case 5: rowDelta = -1; colDelta = 1; break; // Up-Right
                case 6: rowDelta = 1; colDelta = -1; break; // Down-Left
                case 7: rowDelta = 1; colDelta = 1; break; // Down-Right
            }
            if (canPlaceWord(word, rowStart, colStart, rowDelta, colDelta)) {
                for (int i = 0; i < wordLength; i++) {
                    grid[rowStart + i * rowDelta][colStart + i * colDelta] = word.charAt(i);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Maths if its possible to locate the word in given postion.
     * 
     * @param word
     * @param row - Row of where the word starts
     * @param col - Column of where the word starts
     * @param rowDelta - Space between each letter in row
     * @param colDelta - Space between each letter in column
     * @return
     */
    private boolean canPlaceWord(String word, int row, int col, int rowDelta, int colDelta) {
        for (int i = 0; i < word.length(); i++) {
            int newRow = row + (i * rowDelta);
            int newCol = col + (i * colDelta);
            if (newRow < 0 || newCol < 0 || newRow >= grid.length || newCol >= grid[0].length) {
                return false; // Out of bounds
            } else if (grid[newRow][newCol] != PLACEHOLDER && grid[newRow][newCol] != word.charAt(i)) {
                return false; // Collision with a different letter
            }
        }
        return true;
    }
}
