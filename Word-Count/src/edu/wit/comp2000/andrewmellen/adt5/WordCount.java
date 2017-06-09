package edu.wit.comp2000.andrewmellen.adt5;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/** WordCount Class
 * @author Andrew Mellen
 * @date December 7, 2016
 * @class COMP2000*/
public class WordCount {

	public static void main(String[] args) {
		countWords("american-english-JL.txt", 50000, 1.0);
		countWords("sources.txt", 100, 3.0);
		countWords("the-lancashire-cotton-famine.txt", 100, 3.0);
		countWords("wit-attendance-policy.txt", 100, 3.0);
	}
	
	/**Reads in a file and then prints out the outputs to files*/
	private static void countWords(String fileName, int initTableSize, double maxLamda){
		try(Scanner sc = new Scanner(new File(fileName));
			PrintWriter pwOut = new PrintWriter(fileName.substring(0, fileName.length() - 4) + ".out");
			PrintWriter pwMet = new PrintWriter(fileName.substring(0, fileName.length() - 4) + ".metrics")){
			HashTable<String, Integer> table = new HashTable<>(initTableSize, true);
			table.setMaxLamda(maxLamda);
			ArrayList<String> keys = new ArrayList<>();
			
			//Read in the words and put them into the HashTable
			while(sc.hasNext()){
				String word = sc.next().toLowerCase();
				word = word.replace(".", "");
				word = word.replace("?", ""); 
				word = word.replace("!", "");
				word = word.replace(";", "");
				word = word.replace(":", "");
				word = word.replace(",", "");
				word = word.replace("\"", "");
				word = word.replace("'", "");
				if(table.containsKey(word))
					table.replace(word, table.get(word) + 1);
				else{
					table.put(word, 1);
					keys.add(word);
				}
			}
			
			//Printing all the outputs
			String[] minKeys = new String[10];
			int maxMinKeyIndex = 0; //highest value in the min key array
			boolean minChanged = false;
			String[] maxKeys = new String[10];
			int minMaxKeyIndex = 0; //lowest value in the max key array
			boolean maxChanged = false;
			for(int x = 0; x < keys.size(); x++){ //find the 10 highest and least occuring words
				if(x < 10){ //set first 10 elements as the mins and maxes
					minKeys[x] = keys.get(x);
					minChanged = true;
					maxKeys[x] = keys.get(x);
					maxChanged = true;
				}
				else{ //look for new high or low values
					if(table.get(keys.get(x)) < table.get(minKeys[maxMinKeyIndex])){ //see if new value is greater than last max
						minKeys[maxMinKeyIndex] = keys.get(x);
						minChanged = true;
					}
					if(table.get(keys.get(x)) > table.get(maxKeys[minMaxKeyIndex])){ //see if new value is less than last min
						maxKeys[minMaxKeyIndex] = keys.get(x);
						maxChanged = true;
					}
				}
				
				//Find new min or max values if the arrays were changed
				if(minChanged){ 
					for(int y = 0; y < minKeys.length; y++) //find new highest number in min array
						if(minKeys[y] != null)
							if(table.get(minKeys[y]) > table.get(minKeys[maxMinKeyIndex]))
								maxMinKeyIndex = y;
					minChanged = false;
				}
				if(maxChanged){
					for(int y = 0; y < maxKeys.length; y++) //find new lowest number in max array
						if(minKeys[y] != null)
							if(table.get(maxKeys[y]) < table.get(maxKeys[minMaxKeyIndex]))
								minMaxKeyIndex = y;
					maxChanged = false;
				}
			}
			
			//Print the arrays of the words
			pwOut.println("10 Most Occuring words:");
			for(String key : maxKeys){
				pwOut.println(key + ": " + table.get(key));
			}
			pwOut.println("\n10 Least occuring words:");
			for(String key : minKeys){
				pwOut.println(key + ": " + table.get(key));
			}
			
			//Print metrics
			System.out.println("Table after \"" + fileName + "\":\n" + table  + "\n");
			pwMet.print(table);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
