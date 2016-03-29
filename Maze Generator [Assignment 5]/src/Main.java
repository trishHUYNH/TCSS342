import java.util.Random;
/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

public class Main {
	
	public static void main(String[] args) {
		System.out.println("--- 5x5 MAZE WITH DE-BUGGING ON ---");
		Maze fiveByFive = new Maze(5, 5, true);
		System.out.println("--- 15x15 MAZE WITH DE-BUGGING OFF ---");
		Maze twentyByTwenty = new Maze(15, 15, false);
	}

}
