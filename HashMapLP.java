
/**
 * name: Eric Osterman
 * assignment: PP3
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

/**
 * Class HashMap an implementation of the hash table
 * data structure using linear probing
 * Uses HashMap implementation given in PP3 UML
 */
@SuppressWarnings("unchecked")
public class HashMapLP<K, V> {
	private int size;
	private double loadFactor;
	// not using linked list since class implements linear probing
	private MapEntry<K, V>[] hashTable;
	public static int collisions = 0;
	public static int iterations;

	/**
	 * Default constructor
	 * Creates an empty hash table with capacity 100
	 * and default load factor of 0.5
	 */
	public HashMapLP() {
		this(100, 0.5);
		collisions = 0;
	}

	/**
	 * Constructor with one parameters
	 * Creates an empty hash table with capacity c
	 * and default load factor of 0.5
	 */
	public HashMapLP(int c) {
		this(c, 0.5);
		collisions = 0;
	}

	/**
	 * Constructor with two parameters
	 * Creates an empty hash table with capacity c
	 * and load factor lf
	 */
	public HashMapLP(int c, double lf) {
		hashTable = new MapEntry[trimToPowerOf2(c)];
		loadFactor = lf;
		size = 0;
	}

	/**
	 * Private method to find the closest power of 2
	 * to the capacity of the hash table
	 * 
	 * @param c desired capacity for the hash table
	 * @return closest power of 2 to c
	 */
	private int trimToPowerOf2(int c) {
		int capacity = 1;
		while (capacity < c)
			capacity = capacity << 1; // multiplies size by 2
		return capacity;
	}

	/**
	 * hash method: convert hashCode into value ranged 0 to sizeOf(arraylist) - 1
	 * 
	 * @param hashCode
	 * @return valid index in the hash table
	 */
	private int hash(int hashCode) {
		return hashCode & (hashTable.length - 1);
	}

	/**
	 * Rehash method called when the size of the hashtable
	 * reached lf * capacity
	 */
	private void rehash() {
		ArrayList<MapEntry> list = toList(); // O(n)
		hashTable = new MapEntry[hashTable.length << 1]; // *2 the length
		size = 0;
		for (MapEntry<K, V> entry : list) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Method size
	 * 
	 * @return the number of elements added to the hash table
	 */
	public int size() {
		return size;
	}

	/**
	 * Method clear
	 * resets all the hash table elements to null
	 * and clears all the linked lists attached to the hash table
	 */
	public void clear() {
		size = 0;
		for (int i = 0; i < hashTable.length; i++) {
			if (hashTable[i] != null) {
				hashTable[i] = null;
			}
		}
	}

	/**
	 * Method isEmpty
	 * 
	 * @return true if there are no valid data in the hash table
	 */
	public boolean isEmpty() { // O(1)
		return (size == 0);
	}

	/**
	 * Search method
	 * 
	 * @param key being searched for
	 * @return true if key is found, false otherwise
	 */
	public boolean containsKey(K key) {
		if (get(key) != null)
			return true;
		return false;
	}

	/**
	 * Get method
	 * 
	 * @param key being searched for
	 * @return the value of the hash table entry if the key is found,
	 *         null if the key is not found
	 *         O(1) time to get key, displayed in Test class
	 */
	public V get(K key) {
		iterations = 0;
		int HTIndex = hash(key.hashCode());
		V temp = null;
		while (hashTable[HTIndex] != null) {
			iterations++;
			if (hashTable[HTIndex].getKey().equals(key)) {
				temp = hashTable[HTIndex].getValue();
				return temp;
			}

			if (HTIndex != hashTable.length - 1) {
				HTIndex++;
			} else {
				HTIndex = 0;
			}
		}
		return temp;
	}

	/**
	 * Add a new entry to the hash table
	 * 
	 * @param key   key of the entry to be added
	 * @param value value of the entry to be added
	 * @return the old value of the entry if an entry with the same key is found
	 *         the parameter value is returned if a new entry has been added
	 *         O(1) or O(n) depending opn if need to rehash
	 */
	public V put(K key, V value) {
		int HTIndex = 0;
		if (get(key) != null) {
			HTIndex = hash(key.hashCode());

			while (hashTable[HTIndex] != null) {
				if (hashTable[HTIndex].getKey().equals(key)) {
					V old = hashTable[HTIndex].getValue();
					hashTable[HTIndex].setValue(value);
					return old;
				}

				if (HTIndex != hashTable.length - 1) {
					HTIndex += 1;
				} else {
					HTIndex = 0;
				}
			}
		}

		// key not in the hash map - check load factor, rehash if need
		if (size >= hashTable.length * loadFactor)
			rehash();
		HTIndex = hash(key.hashCode());

		// if index is free, insert key & element
		if (hashTable[HTIndex] == null) {
			hashTable[HTIndex] = new MapEntry<K, V>(key, value);
		} else { // else index is taken, collision occurs
			collisions++;

			// while hashTable at index is full, incease index after checking not bigger
			// than size
			while (hashTable[HTIndex] != null) {
				if (HTIndex != hashTable.length - 1) {
					HTIndex++;
				} else {
					HTIndex = 0;
				}
			}
			// adding new key & value to hashTable
			hashTable[HTIndex] = new MapEntry<>(key, value);
		}

		// increase size after new addition
		size++;

		return value;
	}

	/**
	 * Method toList used by rehash
	 * toList creates new ArrayList if current HashTable is full used in rehashing
	 * 
	 * @return all the entries in the hash table in an array list
	 */
	public ArrayList<MapEntry> toList() {
		ArrayList<MapEntry> list = new ArrayList<>();
		for (int i = 0; i < hashTable.length; i++) {
			if (hashTable[i] != null) {
				list.add(hashTable[i]);
			}
		}
		return list;
	}

	/**
	 * toString method to print out key and value pair
	 * 
	 * @return the hashtable entries formatted as a string
	 */
	public String toString() {
		String out = "[";
		for (int i = 0; i < hashTable.length; i++) {
			if (hashTable[i] != null) {
				out += hashTable[i].toString() + "\n";
			}
		}
		out += "]";
		return out;
	}

} // end of class