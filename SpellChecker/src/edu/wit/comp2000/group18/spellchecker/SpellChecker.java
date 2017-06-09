package edu.wit.comp2000.group18.spellchecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * SpellChecker Class<br/>
 * ------------------<br/>
 * Reads through text files and instantiates the Dictionary object
 * 
 * @author Group 18 - Bradley Rawson, Andrew Mellen, Olivia Deng, Kai Zhao
 * @courseNumber COMP2000-05
 * @assignment Application 1
 * @date September 22, 2016
 */
public class SpellChecker {
	
	// Initialize Dictionary
	private static Dictionary dict = new Dictionary();
	
	/**
	 * Checks the file for misspelled words
	 * @param fileName - file you are checking
	 * @return output string for a text box
	 */
	public static String checkFile (String fileName) {
		String input = "";
		String mispelled = "The following words were found to be mispelled:\n";
		int wordsPerLine = 0; 
		
		//Get file
		try(Scanner sc = new Scanner(new File(fileName))) {
			while(sc.hasNext()) {
				input = sc.next();
				if(!(isWebLink(input) || isCorrectSpelling(input))) {
					wordsPerLine++;
					if(wordsPerLine == 1) { // How many words are printed per line (output)
						wordsPerLine = 0;
						mispelled = mispelled + "\"" + input + "\", \n";
					} else {
						mispelled = mispelled + "\"" + input + "\", ";
					}
				}
			}
			
			mispelled = mispelled.substring(0, mispelled.length() - 3) + ".";
			return mispelled;
			
		//Error Messages
		} catch(FileNotFoundException ex) {
			return String.format("The file you have selected could not be found.%n"
					+ "Please make sure the file is located in the application folder.");
		}	catch(Exception ex) {
			System.out.println("You done broke it in class SpellChecker.");
			System.out.println(ex);
			for(StackTraceElement error : ex.getStackTrace()) {
				System.out.println(error);
			}
			
			return String.format("Something technical has went wrong.");
		}
		

	}
	
	/**
	 * Checks to see if a word is spelt correctly
	 * @param input - the word
	 * @return true if spelled correctly, false otherwise
	 */
	private static boolean isCorrectSpelling(String input) {
		//Remove dashes and / by splitting the two parts in multiple parts
		for(String dashSplit : input.split("-")) {
			for(String word : dashSplit.split("/")) {
				char lastChar = word.charAt(word.length() - 1);
				//If the word ends in these characters remove it
				if(lastChar == '.'
				|| lastChar == '?'
				|| lastChar == '!'
				|| lastChar == ';'
				|| lastChar == ',') {
					word = word.substring(0,word.length() - 1);
				}
	
				//Just ignore double quotes and colons
				word = word.replaceAll("\"", "");
				word = word.replaceAll(":", "");
			
				//Make sure the word isn't a number, check with both all lowercase and proper nouns
				if(!(isNumber(word)
				|| dict.checkWord(word.toLowerCase())
				|| dict.checkWord(word.toUpperCase().charAt(0) + word.toLowerCase().substring(1)))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param input
	 * @return true if input has www. in the string
	 */
	private static boolean isWebLink(String input) {
		return input.contains("www.");
	}

	/**
	 * @param input
	 * @return true if input is all numbers or number symbols, false otherwise
	 */
	private static boolean isNumber(String input) {
		for(char character : input.toCharArray()) {
			if(!(character >= '0' && character <= '9' 
			|| character == '$'
			|| character == '%'
			|| character == '#'
			|| character == '('
			|| character == ')')) {		
				return false;
			}
		}
		return true;
	}
}