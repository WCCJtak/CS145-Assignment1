import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/** 
 * Main.class
 * @Author: Jakob Tak
 * @Date: 24.01.2024
 * @Class: CS&145
 * @Assignment: Assignment1
 */
public class Main {
    private static final int WORD_SEARCH_SIZE = 20;

    private static final String PRESET_FILE_NAME = "preset.txt";
    private static final File PRESET_FILE = new File(PRESET_FILE_NAME);

    private static final char OPTION_GENERATE = 'g';
    private static final char OPTION_PRINT = 'p';
    private static final char OPTION_SOLUTION = 's'; // duplicate
    private static final char OPTION_QUIT = 'q';

    // Suboptions may not be listed with options due to duplication issue
    private static final char SUBOPTION_LOAD = 'r';
    private static final char SUBOPTION_MANUAL = 'm';
    private static final char SUBOPTION_SAVE = 's'; // duplicate

    public static void main(String[] args) {
        boolean isRunning = true;
        WordSearchGenerator generator = null;
        Scanner scanner = new Scanner(System.in);

        while(isRunning) {
            try {
                printIntro();
                char command = scanner.nextLine().toLowerCase().charAt(0);
                switch (command) {
                    case OPTION_GENERATE:
                        generator = new WordSearchGenerator(WORD_SEARCH_SIZE);
                        commandGenerate(scanner, generator);
                        break;
                    case OPTION_PRINT:
                        commandPrint(scanner, generator);
                        break;
                    case OPTION_SOLUTION:
                        commandSolution(generator);
                        break;
                    case OPTION_QUIT:
                        isRunning = false;
                        break;
                    default:
                        System.out.printf("Given command (%c) is not a valid command. ", command);
                        System.out.printf("Please try it again.\n");
                }
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("You provided an empty command. Please try it again.");
            }
        }
        scanner.close();
    }

    /**
     * Runs the command to generate.
     * First it requests how many words that user wants in the game
     * Then it can be generated automatically with preset words or manually done.
     */
    private static void commandGenerate(Scanner scanner, WordSearchGenerator generator) {
        System.out.println("How many words do you need?");
        int wordsCount = Integer.valueOf(scanner.nextLine()); // to prevent enter bug
        String[] words = new String[wordsCount];
        
        System.out.printf("If you want to add random words (%c)\n", SUBOPTION_LOAD);
        System.out.printf("If you want to manually add your words (%c)\n", SUBOPTION_MANUAL);
        char command = scanner.nextLine().toLowerCase().charAt(0);
        switch (command) {
            case SUBOPTION_LOAD:
                try {
                    String[] preset = loadPreset();
                    for (int i=0; i<words.length; i++) {
                        Random random = new Random();
                        String word = preset[random.nextInt(preset.length-1)];
                        if (word.length() > generator.getMaxWordLength()) {
                            i--; // lower the index and find another word
                        }
                        words[i] = word;
                    }
                    break;
                } catch (FileNotFoundException e) {
                    System.out.printf("Failed to load %s\n", PRESET_FILE_NAME);
                    return; // escape command if the file cannot be found.
                }
            case SUBOPTION_MANUAL:
                for (int i=0; i<words.length; i++) {
                    System.out.printf("Please input your next word. Index: %d\n", i+1);
                    String word = scanner.nextLine().replace(" ", ""); // No space allowed.
                    if (word.length() > generator.getMaxWordLength()) {
                        System.out.println("Given word is too long to be fit in the game. Please retry");
                        i--; // lower the index
                    }
                    words[i] = word;
                }
                break;
            default:
                System.out.printf("Given command (%c) is not a valid command. ");
                System.out.printf("Please try it again.\n", command);
        }
        generator.setWords(words);
    }

    /**
     * Runs the command to print.
     * If user wants to save, it requests the filename to save the game.
     * Otherwise it only displays the game in current shell.
     */
    private static void commandPrint(Scanner scanner, WordSearchGenerator generator) {
        if (generator != null) {
            System.out.printf("Press (%c) to save ", SUBOPTION_SAVE);
            System.out.printf("or press any keys to continue\n");
            char command = scanner.nextLine().toLowerCase().charAt(0);
            switch (command) {
                case SUBOPTION_SAVE:
                    System.out.println("Please input the filename to be saved as");
                    save(scanner.nextLine(), generator.print());
                    break;
                default:
                    System.out.println(generator.print());
            }
        } else {
            System.out.println("You have to generate a word search first.");
        } 
    }

    /**
     * Runs the command to show the solution
     */
    private static void commandSolution(WordSearchGenerator generator) {
        if (generator != null) {
            System.out.println(generator.solution());
        } else {
            System.out.println("You have to generate a word search first.");
        } 
    }

    /**
     * Saves the string data into given filename.
     * 
     * @param fileName name of the file to save
     * @param data string data
     * @return Null
     */
    private static void save(String fileName, String data) {
        File file =  new File(fileName);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(data);
            writer.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

    /**
     * Loads the presetfile and returns all words in the file.
     * We assume the words are seperated by line.
     * 
     * @return String[]
     */
    private static String[] loadPreset() throws FileNotFoundException {
        String tmp = "";
        Scanner fileReader = new Scanner(PRESET_FILE);
        while (fileReader.hasNextLine()) {
            tmp += fileReader.nextLine();
            tmp += "\n"; // newline disappears after loading
        }
        fileReader.close();
        return tmp.split("\n");
    }
    
    public static void printIntro() {
        System.out.printf("Welcome to my word search generator!\n");
        System.out.printf("This programs will allow you to generate your own search puzzle\n");
        System.out.printf("Please select and option:\n");
        System.out.printf("Generate a new word search (%c)\n", OPTION_GENERATE);
        System.out.printf("Print out your word search (%c)\n", OPTION_PRINT);
        System.out.printf("Show the solution to your word search (%c)\n", OPTION_SOLUTION);
        System.out.printf("Quit the program (%c)\n", OPTION_QUIT);
    }
}
