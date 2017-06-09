package edu.wit.comp2000.andrewmellen.adt5;

/** HashTable Class
 * @author Andrew Mellen
 * @date December 7, 2016
 * @class COMP2000
 *
 * @param <K> Key
 * @param <V> Value*/
public class HashTable<K, V>{
	private boolean forcePrimes = true; //by default keep the table size prime
	private Bucket<K, V>[] hashTable;
	private int numOfElements = 0;
	private int numOfBuckets = 0;
	private double maxLamda = 1.0;
	
	@SuppressWarnings("unchecked")
	/**Makes a new hash table of starting size 151*/
	public HashTable(){
		hashTable = new Bucket[151]; //Unchecked, all empty
	}
	
	@SuppressWarnings("unchecked")
	/**Makes a new hash table with a given starting capacity
	 * @param initialCapacity  starting capacity of the table
	 * @param forcePrimes  forces all next resizes to be a prime number*/
	public HashTable(int initialCapacity, boolean forcePrimes){
		if(forcePrimes)
			initialCapacity = findNextPrime(initialCapacity);
		hashTable = new Bucket[initialCapacity]; //Unchecked, all empty
		this.forcePrimes = forcePrimes;
	}
	
	/**@return the amount of elements in the table*/
	public int size() {
		return numOfElements;
	}

	/**@return if there are no elements in the table*/
	public boolean isEmpty() {
		return numOfElements == 0;
	}

	/** @param key  the key to look for
	 * @return true if the key is in the table*/
	public boolean containsKey(K key) {
		if(key == null) //don't allow null keys
			return false;
		int hashIndex = hashIndex(hash(key));
		if(hashTable[hashIndex] == null) //if no bucket return false
			return false;
		else if(hashTable[hashIndex].containsKey(key)) //check the indexed bucket for the key
			return true;
		else //wasn't in that bucket
			return false;
	}

	/**@param value  the value to look for
	 * @return true if the value is in the table*/
	public boolean containsValue(V value) {
		if(value == null) //don't allow null values
			return false;
		for(Bucket<K, V> b : hashTable){ //check every bucket for the value
			if(b != null)
				if(b.containsValue(value))
					return true;
		}
		return false;
	}

	/**Gets the value from a given key
	 * @param key  the key
	 * @return The value from the key or null if it doesn't exist*/
	public V get(K key) {
		if(key == null) //don't allow null keys
			return null;
		int hashedIndex = hashIndex(hash(key));
		if(hashTable[hashedIndex] == null)
			return null;
		return hashTable[hashedIndex].get(key);
	}

	/**Puts the data in the HashTable</br>
	 * Does not allow null keys or null values
	 * @param key  the key
	 * @param value  the value*/
	public void put(K key, V value) {
		if(key == null || value == null) //don't allow null entries
			return;
		if(getLoadFactor() >= maxLamda){ //if load factor is >= the max lamda increase the array's size
			increaseSize();
		}
		int hashIndex = hashIndex(hash(key));
		if(hashTable[hashIndex] == null){
			hashTable[hashIndex] = new Bucket<>(key, value);
			numOfBuckets++;
		}
		else{
			hashTable[hashIndex].add(key, value);
		}
		numOfElements++;
	}

	/**Removes the element with that key
	 * @param key  the key to remove
	 * @return the value that that key referenced to or null if the key wasn't found*/
	public V remove(K key) {
		if(key == null) //don't allow null keys
			return null;
		int hashIndex = hashIndex(hash(key));
		if(hashTable[hashIndex] == null) //no bucket there
			return null;
		V retVal = hashTable[hashIndex].remove(key);
		if(hashTable[hashIndex].size() == 0){ //get rid of bucket if it is now empty
			hashTable[hashIndex] = null;
			numOfBuckets--;
		}
		if(retVal != null) //decrement size if something was removed
			numOfElements--;
		return retVal;
	}

	/**Replace the element at the given key with the newValue</br>
	 * Does nothing if the key is not found
	 * @param key  the key to replace the element at
	 * @param newValue  the value to replace the old value*/
	public void replace(K key, V newValue){
		if(key == null) //don't allow null keys
			return;
		int hashIndex = hashIndex(hash(key));
		hashTable[hashIndex].replace(key, newValue);
	}
	
	@SuppressWarnings("unused")
	/**Clears the table*/
	public void clear() {
		for(Bucket<K, V> b : hashTable){ //b is unused because its just being set to null
			b = null;
		}
		numOfElements = 0;
	}
	
	/**Get a hash code value from the key
	 * @param key  the key
	 * @return the hash value*/
	private int hash(K key){
		if(key instanceof String){
			String str = (String)key;
			int hashcode = 0;
			for(int x = 0; x < str.length(); x++){
				hashcode += (str.charAt(x) * (x + 1));
			}
			return hashcode;
		}
		else{
			return key.hashCode();
		}
	}
	
	/**Gets the hashIndex relative to hashTable.length
	 * @param hashCode  the hashCode of an element
	 * @return the hashIndex relative to hashTable.length*/
	private int hashIndex(int hashCode){
		return hashCode % hashTable.length;
	}
	
	/**Makes the next array at next size*/
	private void increaseSize(){
		System.out.println("BEFORE RESIZE:\n" + this);
		if(forcePrimes){
			int size = (hashTable.length * 2) + 1;
			resizeArray(findNextPrime(size));
		}else{
			resizeArray(hashTable.length * 2);
		}
		System.out.println("AFTER RESIZE:\n" + this + "\n");
	}
	
	/**Resize the array to the new size
	 * @param newSize  next size of the array*/
	@SuppressWarnings("unchecked")
	private void resizeArray(int newSize){
		Bucket<K, V>[] oldTable = hashTable;
		hashTable = new Bucket[newSize]; //unchecked: empty array everything is fine
		
		//reset number of buckets and elements to be re-added to newly sized table
		numOfBuckets = 0;
		numOfElements = 0;
		
		for(int x = 0; x < oldTable.length; x++){ //loop through all elements in table
			if(oldTable[x] != null){
				for(int y = 0; y < oldTable[x].size(); y++){ //there is a bucket there, re-add all elements in it to new table
					K key = oldTable[x].getNode(y).key;
					V value = oldTable[x].getNode(y).value;
					put(key, value);
				}
			}
		}
	}
	
	/**Finds the next prime starting from num
	 * @param num  the input
	 * @return the prime*/
	private static int findNextPrime(int num){
		if(num % 2 == 0) //make sure num starts odd
			num++;
		for(int x = num; true; x+=2){ //find the next prime
			boolean primeFound = true; //assume that this number is prime
			for(int y = 3; y < Math.sqrt(num); y+=2){//see if there is a factor of the size
				if(x % y == 0){
					primeFound = false; //if any factor is found set primeFound to false
					break; //not a prime
				}
			}
			//found a prime number
			if(primeFound){
				num = x;
				break;
			}
		}
		return num;
	}
	
	/**Sets a new max lamda to resize the table at
	 * @param lamda  the new max lamda*/
	public void setMaxLamda(double lamda){
		maxLamda = lamda;
	}
	
	/**@return the load factor*/
	private double getLoadFactor(){
		return (numOfElements * 1.0) / (hashTable.length * 1.0);
	}
	
	@Override
	public String toString(){
		String output = "";
		output += "Table Size: " + hashTable.length;
		
		output += "\nNumber of entries in the dictionary: " + numOfElements;
		
		double loadFactor = getLoadFactor();
		String formattedLoadFac = "";
		if(loadFactor % 1 == 0)
			formattedLoadFac = String.format("%.0f", loadFactor);
		else
			formattedLoadFac = String.format("%s", loadFactor);
		output += "\nLoad Factor: " + formattedLoadFac;
		output += "\nNumber of buckets in use: " + numOfBuckets;
		
		double percentBuckets = (numOfBuckets / (hashTable.length * 1.0)) * 100;
		String formattedPercent = "";
		if(percentBuckets % 1 == 0)
			formattedPercent = String.format("%.0f%%", percentBuckets);
		else
			formattedPercent = String.format("%s%%", percentBuckets);
		output += "\nPercentage of buckets in use: " + formattedPercent;
		
		int minBucket = Integer.MAX_VALUE;
		int maxBucket = 0;
		for(Bucket<K, V> b : hashTable){
			if(b != null){
				if(b.size() > maxBucket)
					maxBucket = b.size();
				if(b.size() < minBucket)
					minBucket = b.size();
			}
		}
		output += "\nMin bucket size: " + minBucket + " | Average bucket size: " + (numOfElements /numOfBuckets) 
											+ " | Max bucket size: " + maxBucket;
		
		return output;
	}
	
	//----------------------------------------------BUCKET INNER CLASS-----------------------------------------------------------
	
	@SuppressWarnings("hiding")
	/**A Linked list of key value pairs*/
	private class Bucket<K,V>{
		private Node<K,V> firstNode;
		int size = 0;
		
		/**Make a new bucket with a given first node*/
		private Bucket(K key, V value){
			add(key, value);
		}
		
		/**Adds a new key value pair to the bucket*/
		private void add(K key, V value){
			if(firstNode == null)
				firstNode = new Node<>(key, value);
			else{
				Node<K,V> tempN = new Node<>(key, value, firstNode);
				firstNode = tempN;
			}
			size++;
		}
		
		/**@return the Value for the given key*/
		private V get(K key){
			Node<K,V> n = getNode(key);
			if(n == null)
				return null;
			else
				return (V)n.value;
		}
		
		/**@return true if there is the given key in the bucket*/
		private boolean containsKey(K key){
			if(get(key) == null)
				return false;
			else
				return true;
		}
		
		/**@return true if there is the given value in the bucket*/
		private boolean containsValue(V value){
			Node<K,V> currentNode = firstNode;
			while(currentNode != null){
				if(currentNode.value.equals(value)) //found a matching value
					return true;
				else//keep checking through list
					 currentNode = currentNode.next;
			}
			return false; //nothing was found
		}
		
		/**Replace the value at key with the newValue</br>
		 *Does nothing if the key is not found*/
		private void replace(K key, V newValue){
			Node<K, V> n = getNode(key);
			if(n == null) //the key wasn't in the list
				return;
			n.value = newValue;
		}
		
		/**Removes the value with the given key
		 * @return the value that was given with the key*/
		private V remove(K key){
			Node<K, V> previousNode = null;
			Node<K, V> currentNode = firstNode;
			V retValue = null;
			for(int x = 0; x < size; x++){
				if(currentNode.key.equals(key)){ //if the key is found
					retValue = currentNode.value;
					if(currentNode == firstNode)
						firstNode = currentNode.next;
					else
						previousNode.next = currentNode.next;
					break; //found it
				}
				if(previousNode == null) //currentNode still on firstNode
					previousNode = firstNode;
				else
					previousNode = previousNode.next;
				currentNode = currentNode.next;
			}
			if(retValue != null) //actually removed something
				size--;
			return retValue;
		}
		
		/**@return the node containing the key*/
		private Node<K, V> getNode(K key){
			Node<K, V> currentNode = firstNode;
			while(currentNode != null){
				if(currentNode.key.equals(key)) //found the right key
					return currentNode;
				else//keep checking through list
					 currentNode = currentNode.next;
			}
			return null;
		}
		
		/**@return the Node at a given index*/
		private Node<K,V> getNode(int index){
			Node<K, V> currentNode = firstNode;
			for(int x = 0; x < index; x++){
				currentNode = currentNode.next;
			}
			return currentNode;
		}
		
		/**@return the amount of nodes in the list*/
		private int size(){
			return size;
		}
	}//end Bucket
	
	
	//-----------------------------------------------NODE INNER CLASS-----------------------------------------------------------
	
	@SuppressWarnings("hiding")
	/**A Node containing a key value pair*/
	private class Node<K,V>
	{
	  private K    key; // Entry in bag
	  private V    value;
	  private Node<K, V> next; // Link to next node

	  	/**Makes a new node with data and value*/
		private Node(K dataPortion, V valuePortion)
		{
			this(dataPortion, valuePortion, null);	
		} // end constructor
		
		/**Makes a new node with data, value, and the nextNode in the list*/
		private Node(K keyPortion, V valuePortion, Node<K, V> nextNode)
		{
			key = keyPortion;
			value = valuePortion;
			next = nextNode;	
		} // end constructor
	} // end Node
	
	
	//----------------------------------------TESTING METHODS-------------------------------------------------------------------
	
	/**Runs through and tests all of the methods*/
	public static void main(String[] args){
		System.out.println("-----TESTING SIZE-----");
		testSize();
		System.out.println("\n-----TESTING ISEMPTY-----");
		testIsEmpty();
		System.out.println("\n-----TESTING CONTAINSKEY-----");
		testContainsKey();
		System.out.println("\n-----TESTING CONTAINSVALUE-----");
		testContainsValue();
		System.out.println("\n-----TESTING GET-----");
		testGet();
		System.out.println("\n-----TESTING PUT-----");
		testPut();
		System.out.println("\n-----TESTING REMOVE-----");
		testRemove();
		System.out.println("\n-----TESTING REPLACE-----");
		testReplace();
		System.out.println("\n-----TESTING CLEAR-----");
		testClear();
		System.out.println("\n-----TESTING TOSTRING-----");
		testToString();
		System.out.println("\n-----TESTING RESIZE AND LAMDA-----");
		testResizeAndLamda();
	}
	
	/**Tests the size method*/
	private static void testSize(){
		System.out.println("Size prints correct size?: " + (makePopulatedTable().size() == 50));
	}
	
	/**Tests the isEmpty method*/
	private static void testIsEmpty(){
		System.out.println("Populated table is !empty?: " + !makePopulatedTable().isEmpty());
		System.out.println("Empty new table is empty?: " + new HashTable<String, Integer>().isEmpty());
	}

	/**Tests the containsKey method*/
	private static void testContainsKey() {
		System.out.println("Contains hi 1?: " + makePopulatedTable().containsKey("hi 1"));
		System.out.println("Doesn't contains hi 100?: " + !makePopulatedTable().containsKey("hi 100"));
	}

	/**Tests the containsValue method*/
	private static void testContainsValue() {
		System.out.println("Contains 1?: " + makePopulatedTable().containsValue(1));
		System.out.println("Doesn't contains 100?: " + !makePopulatedTable().containsValue(100));
	}

	/**Tests the get method*/
	private static void testGet(){
		System.out.println("Returns 15 at hi 15?: " + (makePopulatedTable().get("hi 15") == 15));
		makePopulatedTable().get("invalid"); //invalid entry
	}

	/**Tests the put method*/
	private static void testPut(){
		HashTable<String, Integer> table = makePopulatedTable();
		boolean everyElementIsIn = true;
		for(int x = 0; x < 50; x++){
			everyElementIsIn = (table.get("hi " + x) != null); //set to false if one of the elements doesn't exist
		}
		System.out.println("All elements were added and are in the Table?: " + everyElementIsIn);
	}

	/**Tests the remove method*/
	private static void testRemove(){
		HashTable<String, Integer> table = makePopulatedTable();
		for(int x = 0; x < 50; x++){
			table.remove("hi " + x);
		}
		table.remove("invalid"); //test invalid entry
		System.out.println("Table is empty?: " + table.isEmpty());
	}

	/**Tests the replace method*/
	private static void testReplace(){
		HashTable<String, Integer> table = makePopulatedTable();
		table.replace("hi 27", 5000);
		table.replace("hi 9000", 10); //test invalid entry
		System.out.println("hi 27 now has value of 5000?: " + (table.get("hi 27") == 5000));
	}

	/**Tests the clear method*/
	private static void testClear(){
		HashTable<String, Integer> table = makePopulatedTable();
		table.clear();
		System.out.println("Populated table empty after clear?: " + table.isEmpty());
	}

	/**Tests the toString method*/
	private static void testToString(){
		System.out.println(makePopulatedTable());
	}

	/**Tests the resize ability*/
	private static void testResizeAndLamda(){
		HashTable<String, Integer> table = new HashTable<String, Integer>(7, false);
		table.setMaxLamda(5.0);
		System.out.println("Changed resize lamda to 5.0 and add 100 elements to a table size of 10");
		for(int x = 0; x < 100; x++){
			table.put("hi " + x, x);
		}
		System.out.println("*All elements added*:\n" + table);
	}
	
	/**@return a hashtable of 50 elements*/
	private static HashTable<String, Integer> makePopulatedTable(){
		HashTable<String, Integer> table = new HashTable<String, Integer>();
		for(int x = 0; x < 50; x++){
			table.put("hi " + x, x);
		}
		return table;
	}
}