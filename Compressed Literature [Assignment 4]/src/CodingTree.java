import java.util.PriorityQueue;

/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

public class CodingTree {
	/*
	 * HashTable of word to frequencies
	 */
	MyHashTable<String, Integer> frequency;
	/*
	 * HashTable of word to binary code
	 */
	static MyHashTable<String, String> codes;
	/*
	 * Nodes of words in order by frequency
	 */
	private PriorityQueue<Node> huffmanQueue;
	/*
	 * Root node of Huffman Tree
	 */
	private Node root;

	String bits;

	private static class Node implements Comparable<Node> {
		private String word;
		private int count;
		private Node left;
		private Node right;
		boolean isWord;

		Node(String word, int wordCount, Node left, Node right, boolean isWord) {
			this.word = word;
			this.count = wordCount;
			this.left = left;
			this.right = right;
			this.isWord = isWord;
		}

		Node(Node a, Node b) {
			this.left = a;
			this.right = b;
			this.count = a.count + b.count;
		}

		@Override
		public int compareTo(Node other) {
			return (this.count - other.count);
		}

		private boolean isLeaf() {
			return (left == null && right == null);
		}
	}

	/*
	 * Constructor. Takes in txt file as one large String
	 * Print statements used to test running time of each method
	 */
	public CodingTree(String fullText) {
		System.out.println("Starting to count words!");
		countFrequency(fullText);
		System.out.println("Word count complete!");
		buildPriorityQueue();
		System.out.println("Putting words into Priority Queue complete!");
		root = buildHuffmanTree();
		System.out.println("Building Huffman Tree complete!");
		buildCodeTable(root);
		System.out.println("Assigning binary values complete!");
	}

	/*
	 * Iterates through String and counts frequency of character and updates the
	 * letterFrequency HashMap
	 */
	private void countFrequency(String fullText) {
		frequency = new MyHashTable<String, Integer>();
		char fullTextChar[] = fullText.toCharArray();
		StringBuilder stringCombine = new StringBuilder();
		char c;
		String word, notWord;

		for (int i = 0; i < fullTextChar.length; i++) {
			c = fullTextChar[i];
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
				|| (c >= '0' && c <= '9') || (c == '\'') || (c == '-')) {
				stringCombine.append(c);
			} else {
				if (stringCombine != null && stringCombine.length() > 0) {
					word = stringCombine.toString();
					if (frequency.containsKey(word)) {
						frequency.put(word, frequency.get(word) + 1);
					} else {
						frequency.put(word, 1);
					}
					stringCombine.setLength(0);
				}
				notWord = fullText.substring(i, i + 1);
				if (frequency.containsKey(notWord)) { // If table has key
					frequency.put(notWord, frequency.get(notWord) + 1);
				} else {
					frequency.put(notWord, 1);
				}
			}
		}
	}

	/*
	 * Builds PriorityQueue using values from HashMap and implementing with
	 * Nodes.
	 */
	private void buildPriorityQueue() {
		huffmanQueue = new PriorityQueue<Node>();
		for (String word : frequency.keySet()) {
			huffmanQueue.offer(new Node(word, frequency.get(word), null, null, true));
		}
	}

	/*
	 * Builds Huffman Tree by removing two smallest Nodes and making them
	 * children of a new Node, add the frequency of each into the new Node and
	 * put back into Priority Queue
	 */
	private Node buildHuffmanTree() {
		Node a, b;

		while (huffmanQueue.size() > 1) {
			a = huffmanQueue.poll();
			b = huffmanQueue.poll();
			huffmanQueue.offer(new Node(a, b));
		}
		return huffmanQueue.poll();
	}

	/*
	 * Calls helper method to traverse Post order
	 */
	private void buildCodeTable(Node root) {
		codes = new MyHashTable<String, String>();

		traversePostOrder(root, new String());
	}

	/*
	 * Recursive method that traverses through the tree until it reaches the
	 * leaf. Adds 0 to String if left, adds 1 if right.
	 */
	private void traversePostOrder(Node treeNode, String line) {
		if (!treeNode.isLeaf()) {
			traversePostOrder(treeNode.left, line + "0");
			traversePostOrder(treeNode.right, line + "1");
		}

		if (treeNode.isWord) {
			//System.out.println(treeNode.word + " = " + line);
			codes.put(treeNode.word, line);
		}
	}
}

