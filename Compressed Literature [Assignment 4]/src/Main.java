import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

public class Main {

	static CodingTree text;
	static File inputFile;
	static File compressedFile;
	static FileInputStream input;
	static FileOutputStream compressedTXT;
	static FileOutputStream codesTXT;

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		File inputFile = new File("Files/WarAndPeace.txt");
		File compressedFile = new File("Files/compressed.txt");
		
		compress(inputFile);
		text.frequency.stats();
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("COMPRESSION COMPLETE!");
		System.out.println("Original size of file: " + inputFile.length() + " bytes");
		System.out.println("Compressed size of file: " + compressedFile.length() + " bytes");
		System.out.println("Compression Ratio: " + ((compressedFile.length() * 100) / inputFile.length()) + "%");
		System.out.println("Running time: " + (endTime - startTime) + " milliseconds.");
		
		// testMyHashTable();
		// testHarryPotter();
		
		input.close();
		codesTXT.close();
		compressedTXT.close();
	}

	/*
	 * Reads input file by character and builds on to a large String to pass to
	 * CodingTree constructor.
	 */
	public static void compress(File fileName) throws IOException {
		input = new FileInputStream(fileName);
		codesTXT = new FileOutputStream(new File("Files/codes.txt"));
		compressedTXT = new FileOutputStream(new File("Files/compressed.txt"));
		StringBuilder fullText = new StringBuilder();
		StringBuilder codeBuild = new StringBuilder();
		StringBuilder wordBuild = new StringBuilder();
		StringBuilder compressedBuild = new StringBuilder();
		int c;

		while ((c = input.read()) != -1) {
			fullText.append((char) c);
		}
		System.out.println("Reading text file complete!");
		// Pass large txt file as a String to constructor
		text = new CodingTree(fullText.toString());
		// Prints codes to codes.txt
		System.out.println("Starting to print to codes.txt!");
		codesTXT.write(text.codes.toString().getBytes());
		System.out.println("Printing codes to codes.txt complete!");

		for (int i = 0; i < fullText.length(); i++) {
			Character ch = fullText.charAt(i);
			// words
			if ((ch.compareTo('A') >= 0 && ch.compareTo('Z') <= 0) || (ch.compareTo('a') >= 0 && ch.compareTo('z') <= 0)
				|| (ch.compareTo('0') >= 0 && ch.compareTo('9') <= 0) || ch.equals('\'') || ch.compareTo('-') == 0) {
				wordBuild.append(ch);
			} else { // separator that is not "-" or "'"
				String codeStr = new String(wordBuild);
				//  binary code for word
				if (codeStr.length() > 0) {
					codeBuild.append(text.codes.get(codeStr));
				}
				// binary code for separator
				codeBuild.append(text.codes.get(ch.toString()));
				wordBuild = new StringBuilder();
			}
			if (codeBuild.length() > 256) {
				while (codeBuild.length() > 8) {
					int charAsInt = Integer.parseInt(codeBuild.substring(0, 8), 2);
					compressedTXT.write(charAsInt);
					compressedBuild.append(codeBuild.substring(0, 8));
					codeBuild.delete(0, 8);
				}
			}
		}
		while (codeBuild.length() > 8) {
			int chr = Integer.parseInt(codeBuild.substring(0, 8), 2);
			compressedTXT.write(chr);
			compressedBuild.append(codeBuild.substring(0, 8));
			codeBuild.delete(0, 8);
		}
	}

	/*
	 * Private method to test MyHashTable
	 */
	private static void testMyHashTable() {
		MyHashTable testHash = new MyHashTable();

		testHash.put("Apple", 3);
		testHash.put("Orange", 5);
		testHash.put("Banana", 4);
		testHash.put("Avocado", 7);
		testHash.put("Berry", 2);
		testHash.put("Orange", 21);

		System.out.println("Does testHash contain Apple?: " + testHash.containsKey("Apple"));
		System.out.println("Does testHash contain Plums?: " + testHash.containsKey("Plums"));
		System.out.println("What is Orange's value?: " + testHash.get("Orange"));
		System.out.println("What is Grapes' value?: " + testHash.get("Grapes"));
		System.out.println("Key set: " + testHash.keySet());
		testHash.stats();
	}
	
	/*
	 * Private method to test a different .txt file
	 * Harry Potter & The Sorcerer's Stone
	 */
	private static void testHarryPotter() throws IOException {
		long startTime = System.currentTimeMillis();
		
		File compressedFile = new File("Files/compressed.txt");
		File testHarry = new File("Files/HarryPotterAndTheSorcerer'sStone.txt");
		
		compress(testHarry);
		text.frequency.stats();
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("COMPRESSION COMPLETE!");
		System.out.println("Original size of file: " + testHarry.length() + " bytes");
		System.out.println("Compressed size of file: " + compressedFile.length() + " bytes");
		System.out.println("Compression Ratio: " + ((compressedFile.length() * 100) / testHarry.length()) + "%");
		System.out.println("Running time: " + (endTime - startTime) + " milliseconds.");
	}
}
