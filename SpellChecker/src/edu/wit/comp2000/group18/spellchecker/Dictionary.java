package edu.wit.comp2000.group18.spellchecker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Dictionary Class<br/>
 * ----------------<br/>
 * Reads in from the Dictionary file and
 * includes a method to determine if a
 * word is in the dictionary
 * 
 * @author Group 18 - Bradley Rawson, Andrew Mellen, Olivia Deng, Kai Zhao
 * @courseNumber COMP2000-05
 * @assignment Application 1
 * @date September 22, 2016
 */
public class Dictionary {
	//HashMap to hold the Bags of words
	private Map<Character, BagInterface<String>> dictMap = new HashMap<>();
	
	/**
	 * Creates a new dictionary and populates it with the words in american-english-JL.txt
	 */
	public Dictionary() {
		BagInterface<String> dict = new LinkedBag<>(); //*CHANGE FOR TESTING OTHER ADTs*
													   //temp dict, going to be broken up
													   //into a bag with each letter
													   //and put into the hash map
		try(Scanner sc = new Scanner(new File("american-english-JL.txt"))) {
			char currentFirstLetter = 'A'; //Start at top of dictionary
			while(sc.hasNext()) {
				//Get in each word, when it gets to the next letter, make a new bag
				String word = sc.next();
				if(word.charAt(0) != currentFirstLetter){ //New first letter found
					dictMap.put(currentFirstLetter, dict);
					currentFirstLetter = word.charAt(0);
					dict = new LinkedBag<>(); //*CHANGE FOR TESTING OTHER ADTs*
				}
				dict.add(word); // Add each of the words to the bag through iteration
			}
			dictMap.put(currentFirstLetter, dict); //add in the remaining bag (last letter)
			sc.close();
		} catch(Exception ex) {	
			System.out.println("Dictionary object didn't intialize correctly. ERROR: " + ex); // Error message
		}
	}
	
	/**
	 * Checks for a word in the dictionary
	 * 
	 * @param word
	 * @return true if it is a word, false otherwise
	 */
	public boolean checkWord(String word) {
		BagInterface<String> letterBag = dictMap.get(word.charAt(0));
		return letterBag != null && letterBag.contains(word);	
	}
	
}
