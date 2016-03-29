import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */
public class CodingTree {
	/*
	 * Map of chars to binary code
	 */
	public Map<Character, String> codes;
	/*
	 * Encoded book using Huffman
	 */
	public List<Byte> bits;
	/*
	 * Stores characters in text and the frequency
	 */
	private HashMap<Character, Integer> letterFrequency;
	/*
	 * Nodes in order by frequency
	 */
	private PriorityQueue<Node> huffmanQueue;
	/*
	 * Root node of Huffman Tree
	 */
	private Node root;
	
	private static class Node implements Comparable<Node> {
		private char character;
		private int count;
		private Node left;
		private Node right;
		boolean isChar;

		Node(char character, int charCount, Node left, Node right, boolean isChar) {
			this.character = character;
			this.count = charCount;
			this.left = left;
			this.right = right;
			this.isChar = isChar;
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
	 * Constructor that calls other private methods to compress
	 */
	public CodingTree(String message) {
		countFrequency(message);
		buildPriorityQueue();
		root = buildHuffmanTree();
		buildCodeTable(root);
	}
	
	/*
	 * Iterates through String and counts frequency of character
	 * and updates the letterFrequency HashMap
	 */
	private void countFrequency(String message) {
		letterFrequency = new HashMap<Character, Integer>();
		Integer checkMap;
		for (int i = 0; i < message.length(); i++) {
			checkMap = letterFrequency.get(new Character(message.charAt(i)));

			if (checkMap != null) {
				letterFrequency.put(message.charAt(i), new Integer(checkMap + 1));
			} else {
				letterFrequency.put(message.charAt(i), 1);
			}
		}
	}
	
	/*
	 * Builds PriorityQueue using values from HashMap and
	 * implementing with Nodes.
	 */
	private void buildPriorityQueue() {
		huffmanQueue = new PriorityQueue<Node>();
		for (Character letter : letterFrequency.keySet()) {
			huffmanQueue.offer(new Node(letter, letterFrequency.get(letter), null, null, true));
		}
	}
	
	/*
	 * Builds Huffman Tree by removing two smallest Nodes and making them
	 * children of a new Node, add the frequency of each into the new Node 
	 * and put back into Priority Queue
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
		codes = new HashMap<Character, String>();
		
		traversePostOrder(root, new String());
	}
	
	/*
	 * Recursive method that traverses through the tree until it reaches 
	 * the leaf. Adds 0 to String if left, adds 1 if right.
	 */
	private void traversePostOrder(Node treeNode, String line) {
		if(!treeNode.isLeaf()) {
			traversePostOrder(treeNode.left, line + "0");
			traversePostOrder(treeNode.right, line + "1");
		}
		
        if(treeNode.isChar) {
           System.out.println("Char Value: " + treeNode.character + " and ASCII value: " + (int) treeNode.character + " = " + line);
           codes.put(treeNode.character, line);
        }   
	}
}
