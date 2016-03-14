package hangman;

import java.util.Random;
import java.util.Scanner;

public class Game { // This class handles the game while it's running
	private static final String[] wordForGuesing = { "computer", "programmer", // What kind of words will be presented and dguessable
			"software", "debugger", "compiler", "developer", "algorithm",
			"array", "method", "variable" };

	private String guesWord;
    private StringBuffer dashedWord;
	private FileReadWriter filerw;

	public Game(boolean autoStart) {
		guesWord = getRandWord();
		dashedWord = getW(guesWord);
		filerw = new FileReadWriter();
		if(autoStart) {
			displayMenu();
		}
	}

	private String getRandWord() { // Rreturns a random word from the array wordforGuessing
		Random rand = new Random();
		String randWord = wordForGuesing[rand.nextInt(9)];
		return randWord;
	}
	public void displayMenu() { // Greets the player with instructions
		System.out
				.println("Welcome to �Hangman� game. Please, try to guess my secret word.\n"
						+ "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
						+ "'HELP' to cheat and 'EXIT' to quit the game.");

		findLetterAndPrintIt();
	}
	private void findLetterAndPrintIt() {
		boolean isHelpUsed = false;
		String letter = "";
		StringBuffer dashBuff = new StringBuffer(dashedWord);
		int mistakes = 0;

		do {
			System.out.println("The secret word is: " + printDashes(dashBuff));
			System.out.println("DEBUG " + guesWord);
			do {
				System.out.println("Enter your gues(1 letter alowed): ");
				Scanner input = new Scanner(System.in);
				letter = input.next();
				isHelpUsed = cheat(letter,isHelpUsed,dashBuff);
				menu(letter);
			} while (!letter.matches("[a-z]"));

			int counter = 0;
            counter = CheckIfCorrect(guesWord, dashBuff, letter,counter);
            mistakes = mistakeChecker(counter, mistakes,letter); // Checks if users guess was correct
		} while (!dashBuff.toString().equals(guesWord));

        cheatChecker(isHelpUsed,mistakes,dashBuff); // Checks if user have used help
		// restart the game
		new Game(true);
		
	}// end method
    private void menu(String letter) { // Handles input for extra functions ; restart, top and exit.
        if (letter.equals(Command.restart.toString())) { // If user enters restart
            new Game(true);
        } else if(letter.equals(Command.top.toString())) { // If user enters top
            filerw.openFiletoRead();
            filerw.readRecords();
            filerw.tryCloseFileFromReading();
            filerw.printAndSortScoreBoard();
            new Game(true);
        }
            else if (letter.equals(Command.exit.toString())) { // If user enters exit
                System.exit(1);
            }
        }

	private int mistakeChecker(int x,int mistakes,String letter) { // Checks if the user has made any mistakes during round

        if (x == 0) {
            ++mistakes;
            System.out.printf(
                    "Sorry! There are no unrevealed letters \'%s\'. \n",
                    letter);
            return mistakes;
        } else {
            System.out.printf("Good job! You revealed %d letter(s).\n",
                    x);
            return mistakes;
        }

    }

    private int CheckIfCorrect(String guesWord, StringBuffer dashBuff, String letter, int counter) {
        for (int i = 0; i < guesWord.length(); i++) {
            String currentLetter = Character.toString(guesWord.charAt(i));
            if (letter.equals(currentLetter)) {
                ++counter;
                dashBuff.setCharAt(i, letter.charAt(0));
            }
        }
        return counter;
    }

	private boolean cheat(String letter, boolean isHelpUsed, StringBuffer dashBuff) {

		if (letter.equals(Command.help.toString())) {
			isHelpUsed = true;
			int i = 0, j = 0;
			while (j < 1) {
				if (dashBuff.charAt(i) == '_') {
					dashBuff.setCharAt(i, guesWord.charAt(i));
					++j;
				}
				++i;
			}
			System.out.println("The secret word is: "
					+ printDashes(dashBuff));
			return isHelpUsed;
		}// end if
        return  isHelpUsed;
	}

    private void cheatChecker(boolean isHelpUsed,int mistakes,StringBuffer dashBuff  ) { // Checks if the user has used the cheat function
        if (!isHelpUsed) { // If cheat was not used
            System.out.println("You won with " + mistakes + " mistake(s).");
            System.out.println("The secret word is: " + printDashes(dashBuff));
            System.out.println("Please enter your name for the top scoreboard:");
            Scanner input = new Scanner(System.in);
            String playerName = input.next();
            saveToFile(filerw,mistakes, playerName); // Saves to file
        } else { // If cheat was used
            System.out
                    .println("You won with "
                            + mistakes
                            + " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
            System.out.println("The secret word is: " + printDashes(dashBuff));
        }
    }

	private StringBuffer getW(String word) {
		StringBuffer dashes = new StringBuffer("");
		for (int i = 0; i < word.length(); i++) {
			dashes.append("_");
		}
		return dashes;
	}
	private String printDashes(StringBuffer word) {
		String toDashes = "";
		for (int i = 0; i < word.length(); i++) {
            toDashes += (" " + word.charAt(i));
		}
		return toDashes;
	}
	public enum Command {
		restart, top, exit, help;
	}

    public void saveToFile(FileReadWriter filerw,int mistakes, String playerName) {
        filerw.openFileToWite();
        filerw.addRecords(mistakes, playerName);
        filerw.closeFileFromWriting();
        filerw.openFiletoRead();
        filerw.readRecords();
        filerw.tryCloseFileFromReading();
        filerw.printAndSortScoreBoard();
    }
}