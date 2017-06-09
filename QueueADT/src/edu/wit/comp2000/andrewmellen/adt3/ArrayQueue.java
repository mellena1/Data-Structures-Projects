package edu.wit.comp2000.andrewmellen.adt3;/**   A class that implements the ADT queue by using an expandable   circular array with a counter.      @author Andrew Mellen   @dueDate October 23, 2016   @assignment Lab 3 - Queue ADT   @class COMP2000-3fa*/public final class ArrayQueue<T> implements QueueInterface<T>{   private T[] queue; // Circular array of queue entries   private int frontIndex;   private int backIndex;   private int count; // Amount of elements in the array   private static final int DEFAULT_CAPACITY = 50;   private static final int MAX_CAPACITY = 10000;         /* ------------------------------CONSTRUCTORS------------------------------- */      /** Default Constructor, makes an empty queue */   public ArrayQueue()   {      this(DEFAULT_CAPACITY);   } // end default constructor      /** Initialize an ArrayQueue with a given capacity    *  @param initialCapacity  starting capacity of the underlying array */   public ArrayQueue(int initialCapacity)   {      if(!checkCapacity(initialCapacity)){    	  initialCapacity = MAX_CAPACITY;      }      // The cast is safe because the new array contains null entries      @SuppressWarnings("unchecked")      T[] tempQueue = (T[]) new Object[initialCapacity];      queue = tempQueue;      frontIndex = 0;      backIndex = -1; //first thing enqueued will be at 0      count = 0;   } // end constructor      /** Adds a new entry to the back of this queue.    *   @param newEntry  An object to be added.    *   @throws IllegalStateException if the queue is full */   @Override   public void enqueue(T newEntry) throws IllegalStateException {	  if(isArrayFull()){		  throw new IllegalStateException("Can't push anymore entries, queue is at max capacity");	  }	  if(backIndex == queue.length - 1){ //loop back around circular array		  backIndex = 0;	  }else{		  backIndex++;	  }	  queue[backIndex] = newEntry;	  count++;   } //end enqueue   /** Removes and returns the entry at the front of this queue.    *   @return  The object at the front of the queue.     *   @throws  EmptyQueueException if the queue is empty before the operation. */   @Override	public T dequeue() throws EmptyQueueException{	   if(isEmpty()){		   throw new EmptyQueueException();	   }	   T returnObj = queue[frontIndex];	   queue[frontIndex] = null;	   if(frontIndex == queue.length - 1){ //loop back around circular array		   frontIndex = 0;	   }else{		   frontIndex++;	   }	   count--;	   return returnObj;   } //end dequeue   /**  Retrieves the entry at the front of this queue.    *   @return  The object at the front of the queue.    *   @throws  EmptyQueueException if the queue is empty. */   @Override   public T getFront() throws EmptyQueueException{	   if(isEmpty()){		   throw new EmptyQueueException();	   }	   return queue[frontIndex];   } //end getFront   /** Detects whether this queue is empty.    *  @return  True if the queue is empty, or false otherwise. */   @Override   public boolean isEmpty() {	   return count == 0;   } //end isEmpty   /** Removes all entries from this queue. */   @SuppressWarnings("unchecked") //Creating new array of null entries   @Override   public void clear() {	   int length = queue.length;	   queue = (T[])new Object[length];	   frontIndex = 0;	   backIndex = -1;	   count = 0;   } //end clear      /** @return String of all elements in the queue */	@Override	public String toString(){		String toReturn = "QUEUE:\n{\nNumber of elements: " + count + 								"\nQueue elements:\n";		if(isEmpty())			return "Queue is empty.";		for(int x = frontIndex; x != backIndex + 1; x++){			toReturn = toReturn + "~ "+ queue[x] + "\n";			if(x == queue.length - 1 && x != backIndex){ //loop back for circuluar array				x = 0;			}		}		return toReturn + "}";	} //end toString			/* ---------------------PRIVATE METHODS---------------------- */     /** Make sure capacity < MAX_CAPACITY	*  @param capacity  size you are trying to make array of	*  @return true if capacity is allowed (<MAX_CAPACITY) */	private boolean checkCapacity(int capacity){		return capacity <= MAX_CAPACITY ;	} //end checkCapacity  	/** Check to see if the queue array needs to be made bigger	 *  @return true if array is full of MAX_CAPACITY */	private boolean isArrayFull(){		if(count < queue.length){ //Array still has more room			return false;		}		//Array is full:		int newArraySize = queue.length * 2;		if(!checkCapacity(newArraySize)){ //length*2 is too big, try to max it out			if(queue.length == MAX_CAPACITY){ //Array is full at Max Capacity				return true;			}			else{				newArraySize = MAX_CAPACITY;			}		}		makeNewArray(newArraySize);		return false;	} //end isArrayFull		/** Makes a new array in the given size	 *  @param newArraySize  size of new array being made */	private void makeNewArray(int newArraySize){		// The cast is safe because the new array contains null entries		@SuppressWarnings("unchecked")		T[] tempArray = (T[])new Object[newArraySize];		if(frontIndex > backIndex){			//copy front			System.arraycopy(queue, frontIndex, tempArray, 0, queue.length - frontIndex);			//copy back			System.arraycopy(queue, 0, tempArray, queue.length - frontIndex, backIndex + 1);			frontIndex = 0;			backIndex = queue.length - 1;		}else{ //only case is frontIndex == 0 && backIndex == queue.length - 1			System.arraycopy(queue, frontIndex, tempArray, 0, queue.length);		}		queue = tempArray;	} //end makeNewArray			/* -----------------------TESTER METHODS------------------------- */		/** Runs the tester methods */	public static void main(String[] args){	   		System.out.println("-----------TESTING ENQUEUE-----------");		testEnqueue(100);		System.out.println("\n-----------TESTING DEQUEUE-----------");		testDequeue(100);		System.out.println("\n-----------TESTING GETFRONT-----------");		testGetFront("should be front element");		System.out.println("\n-----------TESTING ISEMPTY-----------");		testIsEmpty();		System.out.println("\n-----------TESTING CLEAR-----------");		testClear();		System.out.println("\n-----------TESTING ISARRAYFULL-----------");		testIsArrayFull();		System.out.println("\n-----------TESTING INVALID ENTRIES-----------");		testInvalidInputs();	} //end main		/** Test the enqueue method	 *  @param numOfElements  how many elements to enqueue */	private static void testEnqueue(int numOfElements){	   ArrayQueue<String> queue1 = new ArrayQueue<>();	   for(int x = 0; x < numOfElements; x++){		   queue1.enqueue("hi");	   }	   System.out.println(queue1);	} //end testEnqueue		/** Test the dequeue method	 *  @param numOfElements  how many elements to enqueue and then dequeue */	private static void testDequeue(int numOfElements){		ArrayQueue<String> queue1 = new ArrayQueue<>();		for(int x = 0; x < numOfElements; x++){			queue1.enqueue("hi");		}		for(int x = 0; x < numOfElements; x++){			System.out.println(queue1.dequeue());		}		System.out.println(queue1);	} //end testDequeue		/** Test the getFront method	 *  @param element  what to enqueue and then getFront of */	private static void testGetFront(String element){		ArrayQueue<String> queue1 = new ArrayQueue<>();		queue1.enqueue(element);		queue1.enqueue("hi");		System.out.println(queue1.getFront());		System.out.println(queue1);	} //end testGetFront		/** Test the isEmpty method */	private static void testIsEmpty(){		ArrayQueue<String> queue1 = new ArrayQueue<>();		System.out.println("NOTHING ADDED Empty?: " + queue1.isEmpty());		queue1.enqueue("not empty");		System.out.println("SOMETHING ADDED Empty?: " + queue1.isEmpty());	} //end testIsEmpty		/** Test the clear method */	private static void testClear(){		ArrayQueue<String> queue1 = new ArrayQueue<>();		for(int x = 0; x < 5; x++){			queue1.enqueue("hi");		}		System.out.println("NOT CLEARED:\n" + queue1);		queue1.clear();		System.out.println("CLEARED:\n" + queue1);	} //end testClear		/** Test the isArrayFull private method */	private static void testIsArrayFull(){		ArrayQueue<String> queue1 = new ArrayQueue<>();		//Add 50 stuff		for(int x = 0; x < 50; x++){			queue1.enqueue("stuff");		}		//Take away 25 stuff		for(int x = 0; x < 25; x++){			queue1.dequeue();		}		//Add 50 new stuff so array has to get bigger		for(int x = 0; x < 50; x++){			queue1.enqueue("new stuff");		}		System.out.println(queue1);	} //end testIsArrayFull		/** Tests some invalid entries */	private static void testInvalidInputs(){		ArrayQueue<String> queue1 = new ArrayQueue<>();		try{			for(int x = 0; x < MAX_CAPACITY + 1; x++){				queue1.enqueue("hi");			}		}catch(IllegalStateException ex){			System.out.println("Enqueue too many items");			ex.printStackTrace();		}		queue1.clear();		try{			queue1.dequeue();		}catch(EmptyQueueException ex){			System.out.println("Dequeue on empty queue");			ex.printStackTrace();		}		try{			queue1.getFront();		}catch(EmptyQueueException ex){			System.out.println("GetFront on empty queue");			ex.printStackTrace();		}	}} // end ArrayQueue