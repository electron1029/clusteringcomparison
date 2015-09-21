package CS599MachineLearningP4.utils;

import java.util.Vector;

public class MaxHeap<K extends Comparable<K>>
{
	// data values for max heap
	private static final int DEFAULT_SIZE = 300;	// so large to make more efficient in our case...
													// test data is only ~100 files so assuming eval data
													// is within a factor of 2 (as stated likely in class)
													// to avoid resizing the heap
	private int capacity;							// how many items can be in the heap
	private int size = 0;							// the actual number of items in the heap
	private K kVals[];

	/**
	 * Constructor.
	 * Creates an empty heap of the default size.
	 */
	public MaxHeap()
	{
		capacity = DEFAULT_SIZE;
		kVals = (K[])new Comparable[DEFAULT_SIZE];
	}

	/**
	 * Inserts a ScoreEntry into the heap
	 */
	public void insert(K k)
	{
		// just incase the number of documents is more than expected
		// we check if the heap is full and resize if necessary
		if (size == capacity)
		{
			resizeHeap();
		}

		// add score to end of heap and increase heap size
		kVals[size] = k;
		size++;

		// repair heap
		bubbleUp(size - 1);
	}

	/**
	 * Method to resize heap if it reaches capacity.
	 * Behavior is double current heap size.
	 */
	private void resizeHeap()
	{
		capacity = capacity * 2;						// determine new capacity
		K[] scores = (K[])new Comparable[capacity];	// create a new scoreentry array at the new capacity

		// copy old heap into the new heap
		for (int i = 0; i < size; i++)
		{
			scores[i] = this.kVals[i];
		}

		// reassign the scoreentry array reference to the newly created array
		this.kVals = scores;
	}

	/**
	 * Method to remove the root of the heap (the max value)
	 * Returns the score entry that was removed.
	 */
	public K removeMax()
	{
		K toRemove = kVals[0];	// get a reference to the entry being removed

		kVals[0] = kVals[size - 1];		// move last item in heap to the root position
		kVals[size - 1] = null;			// null out the place where the last item used to be
		size--;								// decrease heap size
		bubbleDown(0);						// repair the heap
		
		return toRemove;					// return the scoreentry that was removed
	}


	/**
	 * Bubble up implementation for a max heap.
	 */
	private void bubbleUp(int k)
	{
		// find the parent of the node we are bubbling up
		int parent = (k - 1) / 2;

		// go through the parents up the heap, finding the right place for the new entry
		// right place is where the new entry is less than the parent being considered for
		// switching
		while((kVals[k]).compareTo(kVals[parent]) == 1)
		{
			// swap the new scoreentry and its parent
			K temp = kVals[k];
			kVals[k] = kVals[parent];
			kVals[parent] = temp;
			k = parent;
			
			// find the new parent
			parent = (k - 1) / 2;
		}
	}

	/**
	 * Bubble down implementation for a max heap.
	 */
	private void bubbleDown(int k)
	{
		while(k * 2 + 1 < size)
		{
			int largest = k * 2 + 1;		// get one child for the scoreentry being bubbled down
			int left = largest + 1;				// get the other child
			
			// check if a root is smaller than left child
			if (left < size && (kVals[left].compareTo(kVals[largest]) == 1))
			{
				// if it is fix the largest reference
				largest = left;
			}
			// check if root is smaller than right child
			if (kVals[largest].compareTo(kVals[k]) == 1)
			{
				// if they are swap the scoreentry being bubbled down and the right child
				K temp = kVals[k];
				kVals[k] = kVals[largest];
				kVals[largest] = temp;

				// fix the largest reference
				k = largest;
			} else  
			{
				// otherwise the new scoreentry goes to the bottom of the heap
				k = size;
			}
		}
	}

	/**
	 * Method to perform heapsort k times on the heap.
	 * Returns the k max items in a Vector to the caller
	 */
	public Vector<K> readTopK(int k)
	{
		Vector<K> topK = new Vector<K>();

		// if the heap is smaller than k, we just return everything in order
		if (size < k)
		{
			k = size;
		}

		// get the k entries in decreasing order
		for (int i = 0; i < k; i++)
		{
			topK.add(removeMax());
		}

		// return the k entries to caller
		return topK;
	}
}
