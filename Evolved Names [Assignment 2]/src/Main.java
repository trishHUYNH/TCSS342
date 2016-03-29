/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

import java.util.Arrays;
import java.util.Random;


public class Main {
	
	/*
	 * Creates population of 100 Genomes and calls day until fitness is 0.
	 * Then prints out number of generations and running time of program.
	 */
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		int dayNumber = 1;
		Population myPopulation = new Population(100, 0.05);
		while(myPopulation.mostFit.fitness() != 0) {
			System.out.println("Generation number: " + dayNumber);
			myPopulation.day();
			System.out.print("(\"");
			myPopulation.mostFit.printGenome();
			System.out.println("\"), Fitness: " + myPopulation.mostFit.fitness());
			dayNumber++;
		}
		System.out.println("Generations: " + dayNumber);
		long endTime = System.currentTimeMillis();
		System.out.println("Running time: " + (endTime - startTime) + " milliseconds.");
		
		
		/*
		System.out.println("----------- Population Test -----------");
		testPopulation();
		System.out.println("------------- Genome Test -------------");
		testGenome();
		*/
	}
	
	public static void testPopulation() {
		System.out.println("Population of ten: ");
		Population testPopulation = new Population(10, 0.05);
		testPopulation.printPopulation();
		testPopulation.day();
		System.out.println("First Generation has passed: ");
		testPopulation.printPopulation();
		testPopulation.day();
		System.out.println("Second Generation has passed: ");
		testPopulation.printPopulation();
		
	}
	
	public static void testGenome() {
		Genome testGenome = new Genome(0.05);
		testGenome.mutate();
		testGenome.printGenome();
		System.out.println();
		testGenome.mutate();
		testGenome.printGenome();
		System.out.println();
		testGenome.mutate();
		testGenome.printGenome();
		System.out.println();
		System.out.println("Fitness is: " + testGenome.fitness()); 
		testGenome.crossover(testGenome);
		testGenome.printGenome();
	}
}
