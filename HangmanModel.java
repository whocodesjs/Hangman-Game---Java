package Assignment5_000905188;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements the model for the Hangman game. The model keeps track of the word to be guessed,
 * the letters that have been guessed, and the number of wrong guesses.
 *
 * @author Jovain Chisholm
 */
public class HangmanModel {

    private final String word; // The word to be guessed
    private String hiddenWord; // The word to be displayed to the user with the unguessed letters replaced by underscores
    private int wrongGuesses; // The number of wrong guesses so far
    private final Set<Character> guesses = new HashSet<>(); // The set of guessed letters

    /**
     * Constructor for the HangmanModel class.
     *
     * @param word The word to be guessed.
     */
    public HangmanModel(String word) {
        this.word = word.toUpperCase();
        this.hiddenWord = word.replaceAll("[A-Za-z]", "_");
    }

    /**
     * Takes a character as input and updates the state of the model accordingly.
     *
     * @param c The character to be guessed.
     * @return true if the guessed letter is in the word, false otherwise.
     */
    public boolean guess(char c) {
        char upperC = Character.toUpperCase(c);
        // If the letter has already been guessed, return true
        if (!guesses.add(upperC)) {
            return true;
        }
        boolean found = false;
        // Iterate over the characters in the word and update the hidden word if the guessed letter is found
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == upperC) {
                hiddenWord = hiddenWord.substring(0, i) + upperC + hiddenWord.substring(i + 1);
                found = true;
            }
        }
        // If the guessed letter is not found in the word, increment the number of wrong guesses
        if (!found) {
            wrongGuesses++;
        }
        // Return true if the guessed letter is in the word, false otherwise
        return found;
    }

    /**
     * Returns the word to be guessed.
     *
     * @return The word to be guessed.
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns the hidden word with the unguessed letters replaced by underscores.
     *
     * @return The hidden word.
     */
    public String getHiddenWord() {
        return hiddenWord;
    }

    /**
     * Returns the number of wrong guesses so far.
     *
     * @return The number of wrong guesses.
     */
    public int getWrongGuesses() {
        return wrongGuesses;
    }}
