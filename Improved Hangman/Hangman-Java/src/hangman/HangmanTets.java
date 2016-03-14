package hangman;

public class HangmanTets { // Main class, used to create the Game object, and initiate software.
	public static void main(String[] args) {
	startGame(); // Starts game
	}

public static void startGame() { // Method that initiates software
	Game myGame = new Game(false); // Creates a Game object
	myGame.displayMenu();  // Runs the displaymenu method, which'll print greet message to player, and initiate game
}}
