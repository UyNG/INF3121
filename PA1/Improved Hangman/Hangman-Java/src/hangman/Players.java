package hangman;
import java.io.Serializable;

public class Players implements Serializable{  // Objects of this class are made for highscore purposes.

	private String name;
	private int scores;

	public Players(String name, int scores) { // Constructor
		this.name = name;
		this.scores = scores;
	}

	public String getName() { // Method to get name of player
		return name;
	}

	public int getScores() { // Method to get score of player
		return scores;
	}
}
