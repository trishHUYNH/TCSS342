import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
	static StringBuffer stringBuff;
	static StringBuilder message;

	public static void main(String[] args) throws IOException {
		inputFile = new File("Files/WarAndPeace.txt");
		compressedFile = new File("Files/compressed.txt");
		//Test File
		File testHarry = new File("Files/HarryPotterAndTheSorcerer'sStone.txt");

		
		long startTime = System.currentTimeMillis();
		
		buildTree(inputFile);
		compress(inputFile);
		/*
		buildTree(testHarry);
		compress(testHarry);
		*/
		long endTime = System.currentTimeMillis();
		printStats(startTime, endTime, inputFile);
		//printStats(startTime, endTime, testHarry);
		
		input.close();
		compressedTXT.close();
		codesTXT.close();
	}

	/*
	 * Reads input file by character and builds on to a large String to pass to
	 * CodingTree constructor
	 */
	public static void buildTree(File fileName) throws IOException {
		System.out.println("Building Huffman Tree for " + fileName);
		input = new FileInputStream(fileName);
		message = new StringBuilder();
		int c;

		while ((c = input.read()) != -1) {
			message.append((char) c);
		}
		// Pass large txt file as a String to constructor
		text = new CodingTree(message.toString());
	}

	/*
	 * Prints binary codes to codes.txt. Prints bits to compression.txt
	 */
	public static void compress(File fileName) throws IOException {
		System.out.println("Compressing " + fileName + " and encoding to Files/compressed.txt\n");
		stringBuff = new StringBuffer();
		compressedTXT = new FileOutputStream(new File("Files/compressed.txt"));
		codesTXT = new FileOutputStream(new File("Files/codes.txt"));

		codesTXT.write(text.codes.toString().getBytes());

		// When buffer's length > 256, output bytes
		for (int i = 0; i < message.length(); i++) {
			stringBuff.append(text.codes.get(message.charAt(i)));
			if (stringBuff.length() > 256) {
				while (stringBuff.length() > 8) {
					int chr = Integer.parseInt(stringBuff.substring(0, 8), 2);
					compressedTXT.write(chr);
					stringBuff.delete(0, 8);
				}
			}
		}

		// output the bytes that are < 256
		while (stringBuff.length() > 8) {
			int chr = Integer.parseInt(stringBuff.substring(0, 8), 2);
			compressedTXT.write(chr);
			stringBuff.delete(0, 8);
		}

		compressedTXT.write(Integer.parseInt(stringBuff.toString(), 2));
	}

	public static void printStats(long startTime, long endTime, File input) {
		System.out.println("COMPRESSION COMPLETE!");
		System.out.println("Running time: " + (endTime - startTime) + " milliseconds.");
		System.out.println("Original size of file: " + (input.length() / 1024) + "KB");
		System.out.println("Compressed size of file: " + (compressedFile.length() / 1024) + "KB");
		System.out.println("Compression Ratio: " + ((compressedFile.length() * 100) / inputFile.length()) + "%");
	}
}
