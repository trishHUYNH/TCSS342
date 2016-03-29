/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Genome {

	
	private final String nucleotide[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z", "_", " ", "-", "'" };
	ArrayList<String> dNA;
	double mutationRate;
	Random randomMutation = new Random();
	int targetSize = Population.Target.length();

	/*
	 * Contructs a default genome of 'A' and gives it a mutation rate between 0
	 * and 1
	 */
	public Genome(double mutationRate) {
		dNA = new ArrayList<String>();
		dNA.add("A");
		this.mutationRate = mutationRate;
	}

	/*
	 * Copy constructor
	 */
	public Genome(Genome gene) {
		this.dNA = new ArrayList<String>(gene.dNA);
		this.mutationRate = gene.mutationRate;
	}

	/*
	 * Mutates the genome by adding or deleting a character
	 */
	public void mutate() {
		double randomChance = randomMutation.nextInt();
			
			if (randomChance <= 0.3) {
				//ADDING CHARACTER
				dNA.add(randomMutation.nextInt(dNA.size() + 1), nucleotide[randomMutation.nextInt(nucleotide.length - 1)]);
			} else if (randomChance > 0.3 && randomChance <= 0.6) {
				//DELETING CHRACTER
				dNA.remove(randomMutation.nextInt(dNA.size() - 1));
			} else if (randomChance > 0.6) {
				//CHANGING CHARACTER
				dNA.set(randomMutation.nextInt(dNA.size()), nucleotide[randomMutation.nextInt(nucleotide.length - 1)]);
			}
	}

	/*
	 * Updates current Genome by crossing a mom genome with a dad genome
	 */
	public void crossover(Genome other) {
		int randomParent = randomMutation.nextInt(Population.targetArray.size());
		Genome dad = new Genome(Population.populationOfGenomes[randomParent]);
		int dadSize = dad.getSize();
		int momSize = other.getSize();
		int length = Math.min(momSize, dadSize);

		for(int i = 0; i < length; i++) {
			double momOrDad = randomMutation.nextInt();
			
			if(momOrDad <= 0.5) { //Take dad's
				dNA.set(i, dad.getCharacter(i));
			}
		}
	}

	/*
	 * Calculate fitness compared to target String
	 */
	public int fitness() {
		int n = dNA.size();
		int m = targetSize;
		int length = Math.min(n, m);
		int f = Math.abs(m - n);

		for (int i = 0; i < length; i++) {
			//If char at index does not match, f++
			if (!dNA.get(i).equals(Population.targetArray.get(i))) {
				f++;
			}
		}
		return f;
	}

	/*
	 * Iterates through Genome ArrayList and prints each element at each index
	 */
	public void printGenome() {
		for (int i = 0; i < dNA.size(); i++) {
			System.out.print(dNA.get(i));
		}
		//System.out.println();
	}
	
	/*
	 * Returns size of given Genome
	 */

	private int getSize() {
		return dNA.size();
	}
	
	/*
	 * Returns character at given index of Genome as a String
	 */
	private String getCharacter(int index) {
		return dNA.get(index);
	}
}
