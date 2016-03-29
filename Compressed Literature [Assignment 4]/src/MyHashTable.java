import java.util.HashSet;
import java.util.Set;

/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

public class MyHashTable<K, V> {
	/*
	 * Default global depth
	 */
	private static final int BUCKET_CAPACITY = 32768;
	/*
	 * Array of buckets
	 */
	private HashNode<K, V>[] buckets;
	/*
	 * Array of probing statistics
	 */
	private int[] hashStats;
	/*
	 * Counter that keeps track of the maximum number or probes
	 */
	private int maxProbe;
	/*
	 * Counter that keeps track of word count
	 */
	private int wordCount;

	/*
	 * Private Node class that holds <Key, Value>
	 */
	private class HashNode<K, V> {
		private K key;
		private V value;

		public HashNode(K thisKey, V thisValue) {
			key = thisKey;
			value = thisValue;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}
	}

	/*
	 * Empty constructor that passes static bucket size
	 */
	public MyHashTable() {
		this(BUCKET_CAPACITY);
	}

	/*
	 * Creates HashTable with 2^15 capacity. 32768 buckets
	 */
	public MyHashTable(int capacity) {
		buckets = new HashNode[capacity];
		hashStats = new int[capacity];
		maxProbe = 0;
		wordCount = 0;

		// Fill Array with null
		for (int i = 0; i < BUCKET_CAPACITY; i++) {
			buckets[i] = null;
		}
	}

	/*
	 * Add or update newValue to HashTable. Uses linear probing.
	 */
	public void put(K searchKey, V newValue) {
		HashNode<K, V> newNode = new HashNode<K, V>(searchKey, newValue);
		int index = hash(searchKey);
		int probeCounter = 0;

		// if index is null & searchKey is not in table
		if (buckets[index] == null) {
			wordCount++;
			hashStats[probeCounter] = hashStats[probeCounter] + 1;
			buckets[index] = newNode;
			return;
		}
		// first item checked was the searchKey
		else if (buckets[index].getKey().equals(searchKey)) {
			buckets[index] = newNode;
			return;
		}
		else if (checkBuckets(searchKey)) {
			for(int i = index + 1; i < BUCKET_CAPACITY; i++) {
				if(buckets[i] != null && buckets[i].getKey().equals(searchKey)) {
					buckets[i] = newNode;
					return;
				}
			}
			int probeValue = reProbe(searchKey, index, probeCounter, false);
			buckets[probeValue] = newNode;
		}
		// key is not in table, bucket was full. Probe
		else {
			wordCount++;
			for (int i = index + 1; i < BUCKET_CAPACITY; i++) {
				probeCounter++;
				if (buckets[i] == null) {
					if (probeCounter > maxProbe) {
						maxProbe = probeCounter;
					}
					hashStats[probeCounter] = hashStats[probeCounter] + 1;
					buckets[i] = newNode;
					return;
				}
			}
			int probeValue = reProbe(searchKey, index, probeCounter, true);
			buckets[probeValue] = newNode;
		}
	}

	/*
	 * Return value of key. If bucket does not contain correct key, linear probe
	 * to find the correct key.
	 */
	public V get(K searchKey) {
		int index = hash(searchKey);
		if (buckets[index] != null) { // Key is in table
			if (buckets[index].getKey().equals(searchKey)) { // Found correct // key
				return (V) buckets[index].getValue();
			} else { // Search through array if key was not in correct bucket
				for (int i = index; i < BUCKET_CAPACITY; i++) {
					if (buckets[i] != null && buckets[i].key.equals(searchKey)) {
						return (V) buckets[i].getValue();
					}
				}
				// Key not found, loop and re-probe
				return (V) buckets[reProbe(searchKey, index, 0, false)].getValue();
			}
		}
		return null;
	}

	/*
	 * Returns true if there is a value for key
	 */
	public boolean containsKey(K searchKey) {
		int index = hash(searchKey);
		HashNode<K, V> newNode = buckets[index];

		if (newNode != null) { // bucket is not empty
			if (newNode.getKey().equals(searchKey)) {
				return true;
			} else {
				for (int i = index; i < BUCKET_CAPACITY; i++) {
					if (buckets[i] != null && buckets[i].getKey().equals(searchKey)) {
						return true;
					}
				}
				// Key is not found, re-probe at beginning
				int probeValue = reProbe(searchKey, index, 0, false);
				if (probeValue == -1) {
					return false;
				} else if (buckets[probeValue].getKey().equals(searchKey)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Displays block of HashTable, including number of entries & buckets,
	 * history of probes, file percentage, max linear probe, and average linear
	 * probe
	 */
	public void stats() {

		int probeAvg = 0;
		System.out.println("\nHash Table Stats");
		System.out.println("================");
		System.out.println("Number of entries: " + wordCount);
		System.out.println("Number of buckets: " + buckets.length);
		System.out.println("Histogram of Probes: ");
		System.out.print("[" + hashStats[0] + ",");
		for (int i = 1; i < maxProbe + 1; i++) {
			if(hashStats[i] != 0) {
				probeAvg += hashStats[i];
			}
			System.out.print(hashStats[i] + ",");
		}
		System.out.println(hashStats[maxProbe] + "]");
		System.out.println("Fill Percentage: " + (((double) wordCount / (double) buckets.length) * 100) + "%");
		System.out.println("Max Linear Probe: " + maxProbe);
		System.out.println("Average Linear Probe: " + ((double)wordCount /(double)probeAvg) + "\n"); //Chain/Key
	}

	/*
	 * Returns int from [0...(2^15)-1]
	 */
	private int hash(K key) {
		return Math.abs(key.hashCode() % BUCKET_CAPACITY);
	}

	/*
	 * Converts & returns HashTable as a String. Uses StringBuilder
	 * instead of charAt because Strings are immutable
	 */
	public String toString() {
		StringBuilder hashTableString = new StringBuilder ("[");
		
		for (int i = 0; i < BUCKET_CAPACITY; i++) {
			if (buckets[i] != null) {
				hashTableString.append("(" + buckets[i].getKey() + "," + buckets[i].getValue() + "), ");
			}
		}
		hashTableString.deleteCharAt(hashTableString.length() - 1);
		hashTableString.deleteCharAt(hashTableString.length() - 1);
		hashTableString.append("]");
		return hashTableString.toString();
	}

	/*
	 * Helper method to loop back around to the beginning of the buckets
	 */
	private int reProbe(K searchKey, int indexToStop, int probeCounter, boolean putMethod) {
		int index = -1;

		// Look for null value
		if (putMethod) {
			for (int i = 0; i < indexToStop; i++) {
				if (buckets[i] == null) {
					index = i;
					hashStats[probeCounter] = hashStats[probeCounter] + 1;
					return index;
				}
				probeCounter++;
			}
		}
		// Probing for key
		else {
			for (int i = 0; i < indexToStop; i++) {
				if (buckets[i] != null && buckets[i].getKey().equals(searchKey)) {
					index = i;
				}
			}
		}
		return index;
	}

	/*
	 * Returns all keys as a set. Iterates through and skips array values that
	 * are null
	 */
	public Set<K> keySet() {
		Set<K> keySet = new HashSet<K>();

		for (int i = 0; i < BUCKET_CAPACITY; i++) {
			if (buckets[i] != null) {
				keySet.add((K) buckets[i].getKey());
			}
		}
		return keySet;
	}

	/*
	 * Helper method to iterate through buckets to find
	 * desired key
	 */
	private boolean checkBuckets(K searchKey) {
		for (int i = 0; i < BUCKET_CAPACITY; i++) {
			if (buckets[i] != null && buckets[i].getKey().equals(searchKey)) {
				return true;
			}
		}
		return false;
	}
}

