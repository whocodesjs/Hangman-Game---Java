package Assignment5_000905188;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.util.Random;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.text.FontWeight;




/**
 * Implements a Hangman game using JavaFX.
 * The user can guess letters in a word and the game will keep track of the number of wrong guesses.
 * The game ends when the user guesses all the letters in the word or when the user makes 6 wrong guesses.
 * @author Jovain Chisholom
 */
public class HangmanGame extends Application {

    /** Hangman game model */
    private HangmanModel model;
    /** Label to display texts within game */
    private Label wordLabel, wrongGuessesLabel, roundsPlayedLabel, roundsWonLabel, roundsLostLabel, messageLabel;
    /** Text field to enter a guess */

    private TextField guessTextField;
    /** Button to submit a guess */
    private Button guessButton;
    /**   Canvas to draw the hangman */
    private Canvas canvas;
    /** Graphics context to draw on the canvas */
    private GraphicsContext gc;

    /** rounds played, rounds won, rounds lost */
    private int roundsPlayed, roundsWon, roundsLost;;

    /** generate a random word from the list of words */
    private String generateWord() {
        String[] words = {"JAVA", "PYTHON", "JAVASCRIPT", "PHP", "RUBY", "SWIFT", "KOTLIN", "SCALA", "GO"};
        return words[new Random().nextInt(words.length)];
    }

    /** Draws the hangman based on the number of wrong guesses
     *  @param wrongGuesses number of wrong guesses
     **/
    private void drawHangman(int wrongGuesses) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); /** Clears the canvas */
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);

         /** Draws the scaffold */
        gc.strokeLine(20, 180, 120, 180);
        gc.strokeLine(70, 180, 70, 20);
        gc.strokeLine(70, 20, 170, 20);
        gc.strokeLine(170, 20, 170, 40);

         /** Draws the head */
        if (wrongGuesses >= 1) gc.strokeOval(150, 40, 40, 40);
         /** Draws the body */
        if (wrongGuesses >= 2) gc.strokeLine(170, 80, 170, 140);
        /** Draws the left arm */
        if (wrongGuesses >= 3) gc.strokeLine(170, 90, 140, 120);
        /** Draws the right arm */
        if (wrongGuesses >= 4) gc.strokeLine(170, 90, 200, 120);
        /** Draws the left leg */
        if (wrongGuesses >= 5) gc.strokeLine(170, 140, 150, 170);
        /** Draws the right leg */
        if (wrongGuesses >= 6) {
            gc.setFont(new Font("Helvetica", 12));
            gc.setFill(Color.RED);
            gc.fillText("Game Over!", 20, 200);
            gc.strokeLine(170, 140, 190, 170);
        }
    }


    /**
     * Handles the user's guess input.
     * Updates the game model with the guess, updates the displayed word, wrong guess count and hangman image.
     * Disables guess button and guess text field when the user has made 6 wrong guesses.
     * Updates message label with appropriate messages for different game outcomes.
     *  @param ActionEvent the event that triggered the handler
     *
     * */


    private void guessHandler(ActionEvent e) {
        messageLabel.setText("");
        String guess = guessTextField.getText();
        /*  Only accepts input if the guess is a single letter */
        if (guess.length() == 1) {
            model.guess(guess.charAt(0));
            wordLabel.setText(model.getHiddenWord());
            wrongGuessesLabel.setText(String.valueOf(model.getWrongGuesses()));
            drawHangman(model.getWrongGuesses());
            if (model.getWrongGuesses() == 6) {
                guessButton.setDisable(true);
                guessTextField.setDisable(true);
            }
        } else {
            messageLabel.setText("Please enter a single letter");

        }
        if (model.getWrongGuesses() == 5 && !model.getHiddenWord().equals(model.getWord())) {
            messageLabel.setText("You have one more guess!");
        }

        if (model.getHiddenWord().equals(model.getWord())) {
            messageLabel.setText("You won!");
            roundsWon++;
            roundsPlayed++;
            roundsPlayedLabel.setText("Rounds Played: " + roundsPlayed);
            roundsWonLabel.setText("Rounds Won: " + roundsWon);
            guessButton.setDisable(true);
            guessTextField.setDisable(true);
        }
        if (model.getWrongGuesses() == 6) {
            messageLabel.setText("You lost!");
            roundsLost++;
            roundsPlayed++;
            roundsPlayedLabel.setText("Rounds Played: " + roundsPlayed);
            roundsLostLabel.setText("Rounds Lost: " + roundsLost);
            guessButton.setDisable(true);
            guessTextField.setDisable(true);
        }


        guessTextField.setText("");
        guessTextField.requestFocus();
        guessTextField.selectAll();
    }

    /**
     * Handles the reset button.
     * Resets the game model and updates the displayed word, wrong guess count and hangman image.
     * Enables guess button and guess text field.
     * Clears the message label.
     * @param ActionEvent the event that triggered the handler
     */


    private void resetHandler(ActionEvent e) {
        model = new HangmanModel(generateWord());
        wordLabel.setText(model.getHiddenWord());
        wrongGuessesLabel.setText(String.valueOf(model.getWrongGuesses()));
        guessButton.setDisable(false);
        guessTextField.setDisable(false);
        messageLabel.setText("");
        guessTextField.setText("");
        guessTextField.requestFocus();
        drawHangman(model.getWrongGuesses());
    }

    /**
     * Starts the program.
     *
     * @param stage The main stage
     * @throws Exception if an error occurs during start.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Hangman Game");
        stage.setScene(scene);

        roundsPlayed = 0;
        roundsWon = 0;
        roundsLost = 0;

        /** Passes a string argument to the HangmanModel constructor **/
        model = new HangmanModel(generateWord());


        /** Creates the GUI **/


        canvas = new Canvas(500, 500);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Label rulesLabel = new Label("Welcome to Hangman!\n" +
                "The rules of the game are simple: \n" +
                "1. A random word is selected and its letters are hidden.\n" +
                "2. You must guess the hidden letters by selecting them one by one.\n" +
                "3. You have 6 incorrect guesses before the game is over.\n" +
                "4. If you correctly guess all the letters, you win!");
        wordLabel = new Label(model.getHiddenWord());
        guessButton = new Button("Guess");
        guessTextField = new TextField();
        wrongGuessesLabel = new Label("" + model.getWrongGuesses());
        messageLabel = new Label();
        roundsPlayedLabel = new Label("Rounds Played: " + roundsPlayed);
        roundsWonLabel = new Label("Rounds Won: " + roundsWon);
        roundsLostLabel = new Label("Rounds Lost: " + roundsLost);
        Button resetButton = new Button("Reset");
        VBox stats = new VBox();


       /** adds the components to the root */

        stats.getChildren().addAll(roundsPlayedLabel, roundsWonLabel, roundsLostLabel);
        root.getChildren().addAll(rulesLabel, wordLabel, guessButton, guessTextField, wrongGuessesLabel, messageLabel, stats, resetButton);

        /** configures the components (colors, fonts, size, location) */
        root.setStyle(  "-fx-background-image: url( 'file:assets/imgs/giphy.gif');" +
                "-fx-background-size: 600 600;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center center;");


        rulesLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
        rulesLabel.setPrefWidth(500);
        rulesLabel.setPrefHeight(180);
        rulesLabel.setTextFill(Color.WHITE);
        rulesLabel.setLayoutX(0);
        rulesLabel.setLayoutY(320);
        rulesLabel.setPadding(new Insets(30));
        rulesLabel.setBackground( new Background(new BackgroundFill(Color.rgb(69, 169, 169, 0.8), CornerRadii.EMPTY, Insets.EMPTY)));
        wordLabel.setStyle("-fx-font-size: 20px;");
        wordLabel.setTextFill(Color.WHITE);
        wrongGuessesLabel.setStyle("-fx-font-size: 20px;");
        wrongGuessesLabel.setTextFill(Color.WHITE);
        guessTextField.setPrefWidth(100);
        guessTextField.setPrefHeight(20);
        resetButton.setPrefWidth(100);
        resetButton.setPrefHeight(30);
        guessButton.setPrefWidth(100);
        guessButton.setPrefHeight(30);
        guessTextField.requestFocus();
        wordLabel.setLayoutX(210);
        wordLabel.setLayoutY(80);
        wrongGuessesLabel.setLayoutX(210);
        wrongGuessesLabel.setLayoutY(110);
        guessTextField.setLayoutX(210);
        guessTextField.setLayoutY(140);
        guessButton.setLayoutX(210);
        guessButton.setLayoutY(180);
        resetButton.setLayoutX(210);
        resetButton.setLayoutY(220);
        messageLabel.setTextFill(Color.RED);
messageLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
messageLabel.setBackground( new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        messageLabel.setLayoutX(210);
        messageLabel.setLayoutY(255);
        roundsPlayedLabel.setTextFill(Color.WHITE);
        roundsWonLabel.setTextFill(Color.WHITE);
        roundsLostLabel.setTextFill(Color.WHITE);
        roundsPlayedLabel.setLayoutX(210);
        roundsPlayedLabel.setLayoutY(210);
        roundsWonLabel.setLayoutX(210);
        roundsWonLabel.setLayoutY(240);
        roundsLostLabel.setLayoutX(210);
        roundsLostLabel.setLayoutY(270);
        BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(69, 169, 169, 0.5), CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        stats.setBackground(background);
        stats.setLayoutX(350);
        stats.setLayoutY(30);
        stats.setPadding(new Insets(10, 10, 10, 10));


/** Adds Event Handlers */

        guessButton.setOnAction(this::guessHandler);
        resetButton.setOnAction(this::resetHandler);

        /**draws the hangman */
        drawHangman(model.getWrongGuesses());


        /** Shows the stage */
        stage.setResizable(false);
        stage.show();
    }


    /**
     * Launches the program.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}
