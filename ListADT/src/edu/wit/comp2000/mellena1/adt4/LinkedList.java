package edu.wit.comp2000.mellena1.adt4;

import java.util.Iterator;


/**
 * <b>LinkedList</b></br>
 * Holds elements in an ordered list
 * 
 * @author Andrew Mellen
 * @date November 13, 2016
 * @class COMP2000
 * @assignment List ADT
 *
 * @param <T>  generic type of elements in the list
 */
public class LinkedList<T> implements ListInterface<T>, Iterable<T>{
	
	/**first node in singly linked list*/
	private Node firstNode;
	/**last node in singly linked list*/
	private Node lastNode;
	/**number of entries in the list*/
	private int numberOfEntries;
	
	/**Constructor*/
	public LinkedList(){
		firstNode = null;
		lastNode = null;
		numberOfEntries = 0;
	}
	
	
	//--------------------------------PUBLIC METHODS----------------------------------------------
	
	@Override
	public boolean add(T newEntry) {
		if(newEntry == null){ //null entries not allowed
			return false;
		}
		if(firstNode == null){ //list is empty
			firstNode = new Node(newEntry);
			lastNode = firstNode;
			numberOfEntries++;
			return true;
		}else{ //all other cases
			Node newLastNode = new Node(newEntry);
			lastNode.next = newLastNode;
			lastNode = newLastNode;
			numberOfEntries++;
			return true;
		}
	}

	@Override
	public boolean add(int newPosition, T newEntry) {
		if(newPosition < 0 || newPosition > getLength()){ //cases to return false
			return false;
		}else{
			if(newPosition == getLength()){ //adding entry to end of the list
				return add(newEntry);
			}
			if(newPosition == 0){ //adding entry to beginning of the list
				Node newNode = new Node(newEntry, firstNode);
				firstNode = newNode;
				return true;
			}
			//adding in between two elements
			Node nodeBeforeNewPos = getNode(newPosition - 1);
			Node newNode = new Node(newEntry, nodeBeforeNewPos.next);
			nodeBeforeNewPos.next = newNode;
			return true;
		}
	}

	@Override
	public T remove(int givenPosition) {
		if(isOkToGetNode(givenPosition)){
			//Change node before givenPosition's next pointer to node after givenPosition
			Node nodeBeforeToRemove = getNode(givenPosition - 1);
			T dataRemoved = nodeBeforeToRemove.next.data;
			nodeBeforeToRemove.next = nodeBeforeToRemove.next.next;
			numberOfEntries--;
			return dataRemoved;
		}else return null;
	}

	@Override
	public void clear() {
		firstNode = null;
		lastNode = null;
		numberOfEntries = 0;
	}

	@Override
	public boolean replace(int givenPosition, T newEntry) {
		if(isOkToGetNode(givenPosition)){
			getNode(givenPosition).data = newEntry;
			return true;
		}else return false;
	}

	@Override
	public T getEntry(int givenPosition) {
		if(isOkToGetNode(givenPosition))
			return getNode(givenPosition).data;
		else return null;
	}

	@Override
	public boolean contains(T anEntry) {
		for(T obj : this){
			if(obj.equals(anEntry)){
				return true;
			}
		}
		return false;
	}

	@Override
	public int getLength() {
		return numberOfEntries;
	}

	@Override
	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	@Override
	//Will never return true, no max size of linked list
	public boolean isFull() {
		return false;
	}
	
	/**Shuffles the list*/
	public void shuffle(){
		Node currentNode = firstNode;
		//Iterate through list, switch each node's data with another randomly chosen node
		for(int x = 0; x < getLength(); x++){
			T currentData = currentNode.data;
			
			int otherNodeIndex = (int)(getLength() * Math.random());
			Node otherNode = getNode(otherNodeIndex);
			T otherData = otherNode.data;
			
			currentNode.data = otherData;
			otherNode.data = currentData;
			
			currentNode = currentNode.next;
		}
	}
	
	/**Sort the list*/
	@SuppressWarnings("unchecked") //cast to Comparable is fine, checked instanceof
	public void sort(){
		if(getLength() == 0 || getLength() == 1){
			return; //with 0 or 1 element, its already sorted
		}
		if(!(firstNode.data instanceof Comparable)){
			//T doesn't implement Comparable, unable to sort
			throw new IllegalStateException("T doesn't implement Comparable");
		}
		
		Node currentNode = firstNode;
		T currentData;
		Node nextNode = currentNode.next;
		T nextData;
		
		//go to length - 1 because last element will already be in the right spot 
		for(int x = 0; x < getLength() - 1; x++){
			//i=x+1 because nextNode starts one spot ahead of currentNode
			for(int i = x + 1; i < getLength(); i++){
				//get new data values into variables
				currentData = currentNode.data;
				nextData = nextNode.data;
				
				//switch data if further node is lower value
				if( ((Comparable<T>)currentData).compareTo(nextData) > 0){
					currentNode.data = nextData;
					nextNode.data = currentData;
				}
				//move next node further through list
				nextNode = nextNode.next;
			}
			//move nodes forward 1
			currentNode = currentNode.next;
			nextNode = currentNode.next;
		}
	}
	
	/**@return An array of type T*/
	public T[] toArray(){
		@SuppressWarnings("unchecked") //new empty array
		T[] array = (T[])(new Object[this.getLength()]);
		int counter = 0;
		for(T obj : this){
			array[counter] = obj;
			counter++;
		}
		return array;
	}
	
	@Override
	public String toString(){
		String reference = super.toString();
		String toReturn = "List " + reference.substring(reference.length() - 9) + ":\n";
		int counter = 0;
		for(T obj : this){
			toReturn += "\tElement " + counter + ": " + obj + "\n";
			counter++;
		}
		if(this.getLength() == 0){//print empty if list is empty
			toReturn += "\tEmpty\n";
		}
		return toReturn.substring(0, toReturn.length() - 1);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new ListIterator<T>();
	}
	
	
	//-------------------------------PRIVATE METHODS---------------------------------------------
	
	/**Iterates through list to return the desired Node*/
	private Node getNode(int givenPosition){
		Node currentNode = firstNode;
		if(givenPosition == 0){ //return firstNode if desired position is 0
			return currentNode;
		}
		for(int x = 0; x < givenPosition; x++){
			currentNode = currentNode.next;
		}
		return currentNode;
	}
	
	/** @return false if length of list == 0, givenPosition < 0, or givenPosition > length of list;
	 * 			true otherwise */
	private boolean isOkToGetNode(int givenPosition){
		//Cases to return false
		if(getLength() == 0){return false;}
		if(givenPosition < 0){return false;}
		if(givenPosition > getLength()){return false;}
		return true;
	}
	
	
	//-----------------------------INNER CLASSSES-------------------------------------------------
	
	/**Private Node class for singly linked list*/
	private class Node {
		/**Entry in bag*/
		private T data; 
		/**Link to next node*/
		private Node next; 

		/**Create a newNode given the data
		 * @param  dataPortion  T object*/
		private Node(T dataPortion) {
			this(dataPortion, null);
		}
		
		/**Create a newNode given the data and nextNode pointer
		 * @param  dataPortion  T object
		 * @param  nextNode pointer to the next node*/
		private Node(T dataPortion, Node nextNode) {
			data = dataPortion;
			next = nextNode;
		}
	} // end Node

	/**Private class to implement Iterable*/
	private class ListIterator<E> implements Iterator<T>{
		Node currentNode = firstNode;
		int position = 0;
		
		@Override
		public boolean hasNext() {
			return currentNode != null;
		}

		@Override
		public T next() {
			T dataToReturn = currentNode.data;
			currentNode = currentNode.next;
			position++;
			return dataToReturn;
		}
		
		@Override
		public void remove(){
			currentNode.data = currentNode.next.data;
			currentNode.next = currentNode.next.next;
			numberOfEntries--;
		}
	}
	
	
	//----------------------------TESTER METHODS------------------------------------------------------
	public static void main(String[] args){
		testAdd();
		System.out.println();
		testAddInPos();
		System.out.println();
		testRemove();
		System.out.println();
		testClear();
		System.out.println();
		testReplace();
		System.out.println();
		testGetEntry();
		System.out.println();
		testContains();
		System.out.println();
		testGetLength();
		System.out.println();
		testIsEmpty();
		System.out.println();
		testIsFull();
		System.out.println();
		testShuffle();
		System.out.println();
		testSort();
		System.out.println();
		testToArray();
		System.out.println();
		testIterator();
	}
	
	/**Tests LinkedList's add method*/
	private static void testAdd(){
		System.out.println("***************TESTING ADD METHOD******************");
		System.out.println("*Adding 6 elements");
		LinkedList<Integer> LL = makeNewList();
		System.out.println(LL);
	}
	
	/**Tests LinkedList's add(int pos, T obj) method*/
	private static void testAddInPos(){
		System.out.println("***************TESTING ADD(pos, T) METHOD******************");
		System.out.println("*Adding 9 to position 2");
		LinkedList<Integer> LL = makeNewList();
		LL.add(2, 9);
		System.out.println(LL);
	}
	
	/**Test LinkedList's remove method*/
	private static void testRemove(){
		System.out.println("***************TESTING REMOVE METHOD******************");
		System.out.println("*Removing element from pos 2 from list (Remove 3)");
		LinkedList<Integer> LL = makeNewList();
		LL.remove(2);
		System.out.println(LL);
	}
	
	/**Test LinkedList's clear method*/
	private static void testClear(){
		System.out.println("***************TESTING CLEAR METHOD******************");
		System.out.println("*Clear everything");
		LinkedList<Integer> LL = makeNewList();
		LL.clear();
		System.out.println(LL);
	}
	
	/**Test LinkedList's replace method*/
	private static void testReplace(){
		System.out.println("***************TESTING REPLACE METHOD******************");
		System.out.println("*Replace Element 2 with 15");
		LinkedList<Integer> LL = makeNewList();
		LL.replace(2, 15);
		System.out.println(LL);
	}
	
	/**Test LinkedList's getEntry method*/
	private static void testGetEntry(){
		System.out.println("***************TESTING GETENTRY METHOD******************");
		System.out.println("*Get Element 2 (Should be 3)");
		LinkedList<Integer> LL = makeNewList();
		System.out.println(LL.getEntry(2));
	}
	
	/**Test LinkedList's contains method*/
	private static void testContains(){
		System.out.println("***************TESTING CONTAINS METHOD******************");
		LinkedList<Integer> LL = makeNewList();
		System.out.println("*Contains 3 (Should be true)");
		System.out.println(LL.contains(3));
		System.out.println("*Contains 7 (Should be false)");
		System.out.println(LL.contains(7));
	}
	
	/**Test LinkedList's getLength method*/
	private static void testGetLength(){
		System.out.println("***************TESTING GETLENGTH METHOD******************");
		System.out.println("*getLength (should be 6)");
		LinkedList<Integer> LL = makeNewList();
		System.out.println(LL.getLength());
	}
	
	/**Test LinkedList's isEmpty method*/
	private static void testIsEmpty(){
		System.out.println("***************TESTING ISEMPTY METHOD******************");
		LinkedList<Integer> LL = new LinkedList<>();
		System.out.println("*getEmpty (Should be true)");
		System.out.println(LL.isEmpty());
		LL = makeNewList();
		System.out.println("*getEmpty (Should be false)");
		System.out.println(LL.isEmpty());
	}
	
	/**Test LinkedList's isFull method*/
	private static void testIsFull(){
		System.out.println("***************TESTING ISFULL METHOD******************");
		System.out.println("*Should always be false");
		LinkedList<Integer> LL = makeNewList();
		System.out.println(LL.isFull());
	}
	
	/**Test LinkedList's shuffle method*/
	private static void testShuffle(){
		System.out.println("***************TESTING SHUFFLE METHOD******************");
		System.out.println("*Before shuffle");
		LinkedList<Integer> LL = makeNewList();
		System.out.println(LL);
		System.out.println("*After shuffle");
		LL.shuffle();
		System.out.println(LL);
	}
	
	/**Test LinkedList's sort method*/
	private static void testSort(){
		System.out.println("***************TESTING SORT METHOD******************");
		System.out.println("*Before sort");
		LinkedList<Integer> LL = makeNewList();
		LL.shuffle();
		System.out.println(LL);
		System.out.println("*After sort");
		LL.sort();
		System.out.println(LL);
	}
	
	/**Test LinkedList's toArray method*/
	private static void testToArray(){
		System.out.println("***************TESTING TOARRAY METHOD******************");
		System.out.println("*List printing");
		LinkedList<Integer> LL = makeNewList();
		System.out.println(LL);
		System.out.println("*ToArray Printing");
		Object[] intArray = LL.toArray();
		for(Object x : intArray){
			System.out.println((Integer)x);
		}
	}
	
	/**Test LinkedList's iterator method*/
	private static void testIterator(){
		System.out.println("***************TESTING ITERATOR METHOD******************");
		LinkedList<Integer> LL = makeNewList();
		Iterator<Integer> it = LL.iterator();
		System.out.println("*Print first element, remove next, print one after (1, 3)");
		System.out.println(it.next());
		it.remove();
		System.out.println(it.next());
	}
	
	/**Makes a new LinkedList with 6 elements for testing*/
	private static LinkedList<Integer> makeNewList(){
		LinkedList<Integer> LL = new LinkedList<>();
		LL.add(1);
		LL.add(2);
		LL.add(3);
		LL.add(4);
		LL.add(5);
		LL.add(6);
		return LL;
	}
}

