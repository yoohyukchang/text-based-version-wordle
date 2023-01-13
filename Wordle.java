import java.util.Scanner;
import java.util.Random;

/**
 * Project 2: Wordle
 *
 * This project has you create a text based version of Wordle
 * (https://www.nytimes.com/games/wordle/index.html). Wordle is a word guessing
 * game in which you have 6 tries to guess a 5-letter word. You are told whether
 * each letter of your guess is in the word and in the right position, in the
 * word but in the wrong position, or not in the word at all.
 *
 * Some key differences in our version are:
 *
 * - Text menu based with no grid. Players have to scroll up to see their
 * previous guesses.
 *
 * - Support for 4, 5, or 6 letter words
 *
 * - We don't check for whether a guess is a valid word or not. Players can
 * guess anything they want (of the correct length).
 *
 * Fun facts: The original Wordle was developed by Josh Wardle. Wardle's wife
 * chose the official word list for the game.
 *
 * 500.112 Gateway Computing: Java Spring 2022
 *
 * The purpose of the program is to make a player guess a word by providing
 * clues based on if the player correctly guessed right letters and/or locations
 * of the letters.
 * Author: Yoohyuk Chang
 * JHED: ychang82
 * October 6, 2022
 */
public class Wordle {

   /**
    * Defining the only Random variable you may (and must) use. DO NOT CHANGE
    * THIS LINE OF CODE.
    */
   static Random gen = new Random(0);

   /**
    * Defines the number of guesses the player starts with for each word. DO NOT
    * CHANGE THIS LINE OF CODE.
    */
   static final int MAX_GUESSES = 6;
   /**
    * Defines the number of hints the player starts with for each word. DO NOT
    * CHANGE THIS LINE OF CODE.
    */
   static final int MAX_HINTS = 2;

   /**
    * The main method. This is where most of your menu logic and game logic
    * (i.e. implementation of the rules of the game ) will end up. Feel free to
    * move logic in to smaller subroutines as you see fit.
    *
    * @param args commandline args
    */
   public static void main(String[] args) {

      String word;
   
      Scanner kb = new Scanner(System.in);
   
      int numGuess = MAX_GUESSES;
      int numHint = MAX_HINTS;
      boolean isGuessValid;
      boolean wordsMatch;
      
      printMenu();
      word = newWord();
      while (true) {
         System.out.print("Please enter a choice: ");
         String text = kb.nextLine();
         if ("n".equals(text) || "N".equals(text)) {
            word = newWord();
            printMenu();
            numGuess = MAX_GUESSES;
            numHint = MAX_HINTS;
         }
         else if ("h".equals(text) || "H".equals(text)) {
            // Extracted to hintInMainMethod to reduce complexity of the loop
            numHint = hintInMainMethod(word, numHint);
         }
         else if ("g".equals(text) || "G".equals(text)) {
            if (numGuess == -1) {
               System.out.println("Sorry, you're out of guesses! " 
                  + "Use the \"n\"/\"N\" command to play again.");
               printMenu();
               continue;
            }
            System.out.println("Enter a guess:");
            String guess = kb.nextLine();
            isGuessValid = validateGuess(word.length(), guess);
            // Extracted to guessInMainMethod to reduce complexity of the loop
            numGuess = guessInMainMethod(word, guess, numGuess, isGuessValid);
            printMenu();
         }
         else if ("e".equals(text) || "E".equals(text)) {
            System.exit(0);
         }
         else {
            System.out.println("Invalid option! Try again!");
            printMenu();
         }
      }
   }
   
   /**
    * Extracted a portion of the main method to reduce complexity of the loop.
    * Method for when the player makes valid guesses
    *
    * @param word The current word the player is trying to guess.
    * @param guess The guess that a player has entered.
    * @param numGuess The number of guesses the player has.
    * @param isGuessValid True when guess is valid, False when it is not.
    * @return numGuess Returns how many number of guesses the player has.
    */
   static int guessInMainMethod(String word, String guess, int numGuess, 
                                boolean isGuessValid) {
      boolean wordsMatch;
      if (isGuessValid) {
         numGuess = numGuess - 1;
         wordsMatch = checkGuess(word, guess);
         if (wordsMatch) {
            System.out.println("Congratulations! You found the word!");
            numGuess = -1;
         }
         else if (!(wordsMatch)) {
            if (numGuess == 0) {
               System.out.println("Sorry, you're out of guesses! " 
                  + "The word was " + word + ". " 
                  + "Use the \"n\"/\"N\" command to play again.");
               numGuess = -1;
            }
            else if (numGuess < 0) {
               System.out.println("Sorry, you're out of guesses! " 
                  + "Use the \"n\"/\"N\" command to play again.");
            }
            else if (numGuess == 1) {
               System.out.println("You have " 
                  + numGuess + " guess remaining.");
            }
            else {
               System.out.println("You have " 
                  + numGuess + " guesses remaining.");
            }
         
         }
      }
      return numGuess;
   }
   
   /**
    * Extracted a portion of the main method to reduce complexity of the loop.
    * Method for giving hints to the player when the player asks.
    * Shows how many hints are remaining
    *
    * @param word The word to give a hint for.
    * @param numHint Number of hints the player has.
    * @return numHint Returns number of hints the player has.
    */
   static int hintInMainMethod(String word, int numHint) {
      if (numHint == 1) {
         giveHint(word);
         numHint = numHint - 1;
         System.out.println("You have " + numHint + " hints remaining.");
         printMenu();
      }
      else if (numHint == 2) {
         giveHint(word);
         numHint = numHint - 1;
         System.out.println("You have " + numHint + " hint remaining.");
         printMenu();
      }
      else {
         System.out.println("Sorry, you're out of hints!");
         printMenu();
      }
      return numHint;
   }

   /**
    * Prints "HINT! The word contains the letter: X" where X is a randomly
    * chosen letter in the word parameter.
    *
    * @param word The word to give a hint for.
    */
   static void giveHint(String word) {
      int hintLetterLocation = gen.nextInt(word.length());
      System.out.println("HINT! The word contains the letter: " 
         + word.charAt(hintLetterLocation));
   }

   /**
    * Checks the players guess for validity. We define a valid guess as one that
    * is the correct length and contains only lower case letters and upper case
    * letters. If either validity condition fails, a message is printed 
    * indicating which condition(s) failed.
    *
    * @param length The length of the current word that the player is trynig to
    *               guess.
    * @param guess  The guess that the player has entered.
    * @return true if the guess is of the correct length and contains only valid
    * characters, otherwise false.
    */
   static boolean validateGuess(int length, String guess) {
      boolean isGuessValid;
      
      // Check if guess only contains alphabet. 
      // Produce true if contains only alphabet, and if not, false.
      boolean isGuessAlphabet = true;
      int guessLength = guess.length();
      int i = 0;
      while (i < guessLength) {
         if ((guess.charAt(i) >= 'a' && guess.charAt(i) <= 'z') 
            || (guess.charAt(i) >= 'A' && guess.charAt(i) <= 'Z')) {
            isGuessAlphabet = true;
         }
         else {
            isGuessAlphabet = false;
            break;
         }
         i = i + 1;
      }
      // Returns false if guess has incorrect length or has non-alphabets.
      // Otherwise, returns true.
      if (guessLength != length && !(isGuessAlphabet)) {
         System.out.println("You must enter a guess of length " + length);
         System.out.println("Your guess must only contain upper case letters "
            + "and lower case letters");
         isGuessValid = false;
      }
      else if (guess.length() != length) {
         System.out.println("You must enter a guess of length " + length);
         isGuessValid = false;
      }
      else if (!(isGuessAlphabet)) {
         System.out.println("Your guess must only contain upper case letters "
            + "and lower case letters");
         isGuessValid = false;
      }
      else {
         isGuessValid = true;
      }
      
      return isGuessValid;
   }

   /**
    * Checks the player's guess against the current word. Capitalization is
    * IGNORED for this comparison. This function also prints a string
    * corresponding to the player's guess. An X indicates a letter that isn't in
    * the word at all. A lower case letter indicates that the letter is in the
    * word but not in the correct position. An upper case letter indicates a
    * correct letter in the correct position. Example:
    *
    * SPLINE (the correct word)
    *
    * SPEARS (the player's guess)
    *
    * SPeXXs (the output printed by this function)
    *
    * Suggestion 1: Convert guess to upper case before doing anything else. This
    * can help simplify later logic.
    *
    * Suggestion 2: Consider using String.indexOf
    *
    * @param word  The current word the player is trying to guess.
    * @param guess The guess that a player has entered.
    * @return true if the word and guess match IGNORING CASE, otherwise false.
    */
   static boolean checkGuess(String word, String guess) {
      guess = guess.toUpperCase();
      int wordLength = word.length();
      boolean wordsMatch;
      String output = "";
      
      int i = 0;
      while (i < wordLength) {
         int j = 0;
         while (j < wordLength) {
            if (guess.charAt(i) == word.charAt(i)) {
               output = output + (word.charAt(i));
               break;
            }
            else if (guess.charAt(i) == word.charAt(j)) {
               output = output + (Character.toLowerCase(guess.charAt(i)));
               break;
            }
            else if (j == wordLength - 1) {
               output = output + "?";
               break;
            }
            j = j + 1;
         }
         i = i + 1;
      }
      
      System.out.println(output);
      
      if (output.equals(word)) {
         wordsMatch = true;
      }
      else {
         wordsMatch = false;
      }
      
      return wordsMatch;
   }

   /**
    * Chooses a random word using WordProvider.getWord(int length). This should
    * print "New word length: X" where x is the length of the word.
    *
    * @return the randomly chosen word
    */
   static String newWord() {
      // Get new word between length of 4 to 6
      String word = WordProvider.getWord(gen.nextInt(3) + 4); 
      
      // Outputs the word length
      int wordLength = word.length();
      System.out.println("New word length: " + wordLength);
      
      return word;
   }

   /**
    * Prints menu options.
    */
   static void printMenu() {
      System.out.println("n/N: New word");
      System.out.println("h/H: Get a hint");
      System.out.println("g/G: Make a guess");
      System.out.println("e/E: Exit");
      System.out.println("-------------");
   }
}
