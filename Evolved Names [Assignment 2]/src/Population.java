/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

import java.util.ArrayList;
import java.util.Random;

public class Population {
	protected static final String Target = "SOME_SUPER_COOL_REALLY_LONG_NAME"; 
	protected static final ArrayList<String> targetArray = new ArrayList<String>();
	protected static Genome[] populationOfGenomes;
	Genome mostFit;
	double mutate;
	Random rand = new Random();
	
	public Population(int numGenomes, double mutationRate) {
		mutate = mutationRate;
		populationOfGenomes = new Genome[numGenomes];
		for(int i = 0; i < numGenomes; i++) {
			//Creates Genomes and adds to Array
			populationOfGenomes[i] = new Genome(mutationRate);
		}
		
		//Sets mostFit as the first genome in Array
		mostFit = populationOfGenomes[0];
		//Converts target String to ArrayList
		for(int i = 0; i < Target.length(); i++) {
			targetArray.add(Character.toString(Target.charAt(i)));
		}
	}
	
	/*
	 * Each day is a generation. Updates mostFit, deletes the least fit and make new Genomes
	 * until population reaches Array length
	 */
	public void day() {
		
		//Bubble sorts by fitness
		sortPopulation();
		mostFit = populationOfGenomes[0];
		
		//Overwrites second half of Array to "delete" weakest links
		for(int i = populationOfGenomes.length / 2; i < populationOfGenomes.length; i++) {
			if(rand.nextDouble() >= mutate) {
				//Creates copy of a random Genome and adds to Array
				populationOfGenomes[i] = new Genome(populationOfGenomes[rand.nextInt(populationOfGenomes.length / 2)]);
				//Mutates the the genome just created
				populationOfGenomes[i].mutate();
			}
			else {
				//Creates copy of a random Genome and adds to Array
				populationOfGenomes[i] = new Genome(populationOfGenomes[rand.nextInt(populationOfGenomes.length / 2)]);
				//Makes a baby Genome with the Genome just created
				populationOfGenomes[i].crossover(populationOfGenomes[i]);
			}
		}
	}
	
	/*
	 * Sort population by fitness.
	 * Bubble Sort
	 */
	private void sortPopulation() {
		Genome temp;
		for(int i = 0; i < populationOfGenomes.length; i++) {
			for(int j = 0; j < populationOfGenomes.length - 1; j++) {
				if(populationOfGenomes[j].fitness() > populationOfGenomes[j + 1].fitness()) {
					temp = populationOfGenomes[j];
					populationOfGenomes[j] = populationOfGenomes[j + 1];
					populationOfGenomes[j + 1] = temp;
				}
			}
		}
	}
	
	/*
	 * Prints out total population by calling printGenome from Genome class
	 */
	public void printPopulation() {
		for(int i = 0; i < populationOfGenomes.length; i++) {
			populationOfGenomes[i].printGenome();
			System.out.println();
		}
	}
}
