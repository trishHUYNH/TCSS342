import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

public class Maze {
	/*
	 * n of G(n,m). Number of rows
	 */
	private int width;
	/*
	 * m of G(m,n). Number of columns
	 */
	private int depth;
	/*
	 * Flag to set whether or not to print each step in maze creation
	 */
	private boolean debug;
	/*
	 * Adjacent matrix for map
	 */
	private Room maze[][];
	/*
	 * Matrix to keep track of visited rooms
	 */
	private int numberMaze[][];
	/*
	 * Total number of rooms in maze (n*m)
	 */
	private int totalRooms;
	/*
	 * Keeps track of how many rooms have been visited
	 */
	private int visitedCount;
	/*
	 * Current Room that is the location of the maze
	 */
	private Room current;
	/*
	 * Stack that keeps track of rooms that have been visited
	 */
	private Stack<Room> visitedRooms;
	/*
	 * Flag to notify methods that the finish point has been found
	 */
	boolean foundFinish;

	private class Room {
		/*
		 * Vertices of maze[][]
		 */
		int n, m;
		/*
		 * Wall flags. False if wall is destroyed. True if wall is still present
		 */
		boolean left, right, up, down, visited;

		private Room(int n, int m) {
			this.n = n;
			this.m = m;
			this.left = true; // 1
			this.right = true; // 2
			this.up = true; // 3
			this.down = true; // 4
			visited = false;
		}

		private Room(int n, int m, int randomCase, boolean wall) {
			this.n = n;
			this.m = m;
			this.left = true; // 1
			this.right = true; // 2
			this.up = true; // 3
			this.down = true; // 4
			visited = true;
			
			switch (randomCase) {
			case 1:
				this.left = wall;
				break;
			case 2:
				this.right = wall;
				break;
			case 3:
				this.up = wall;
				break;
			case 4:
				this.down = wall;
				break;
			}
		}
	}

	/*
	 * Public constructor
	 */
	public Maze(int width, int depth, boolean debug) {
		this.width = width;
		this.depth = depth;
		this.debug = debug;
		maze = new Room[this.width][this.depth];
		numberMaze = new int[this.width][this.depth];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.depth; j++) {
				maze[i][j] = new Room(i, j);
				numberMaze[i][j] = 0;
			}
		}
		numberMaze[0][0] = 1;
		numberMaze[width - 1][depth - 1] = 1;
		visitedRooms = new Stack<Room>();
		// Start of maze
		maze[0][0] = new Room(0, 0, 3, false);
		totalRooms = this.width * this.depth;
		visitedCount = 1;
		current = maze[0][0];
		foundFinish = false;
		buildMaze(current, debug);
		if(debug == false) {
			display();
		}
		//testMaze();
	}

	/*
	 * Display Map using 'X' and ' ' to represent walls and 'o' to represent
	 * path
	 */
	public void display() {		
		
		for(int i = 0; i < width; i++) {
			printTop(i);
			printLeftWall(i);
			
			if(i == (width - 1)) {
				for (int j = 0; j < depth; j++) {
					if(j == width - 1) {
						System.out.print("X   ");
					}
					else {
						System.out.print("X X ");
					}
				}
				System.out.println("X\n");
			}
		}
	}
	
	/*
	 * Private helper method to print top wall of each row
	 */
	private void printTop(int row) {
		for(int j = 0; j < depth; j++) {
			if(maze[row][j].up == false) {
				System.out.print("X   ");
			}
			else if(row != 0 && maze[row - 1][j].down == false) {
				System.out.print("X   ");
			}
			else {
				System.out.print("X X ");
			}
		}
		System.out.println("X");
	}
	
	/*
	 * Private helper method to print left wall of each row. 
	 * Calls solveMaze if debug is false
	 */
	private void printLeftWall(int row) {
		if(debug == true) {
		for(int j = 0; j < depth; j++) {
			if(maze[row][j].left == false && j != 0) {
				System.out.print("  o ");
			}
			else if(j != 0 && maze[row][j - 1].right == false) {
				System.out.print("  o ");
			}
			else if (maze[row][j].visited == true){
				System.out.print("X o ");
			}
			else {
				System.out.print("X   ");
			}
		}
		System.out.println("X");
		}
		else {
			for(int j = 0; j < depth; j++) {
				if(j != 0 && maze[row][j].left == false) {
					if(numberMaze[row][j] == 1) {
						System.out.print("  o ");
					}
					else {
						System.out.print("    ");
					}
				}
				else if(j != 0 && maze[row][j - 1].right == false) {
					if(numberMaze[row][j] == 1) {
						System.out.print("  o ");
					}
					else {
						System.out.print("    ");
					}
				}
				else if(numberMaze[row][j] == 1) {
					System.out.print("X o ");
				}
				else {
					System.out.print("X   ");
				}
			}
			System.out.println("X");
		}
	}
	
	/*
	 * Recursively builds maze by visiting new neighbors and
	 * tearing walls down. If no new neighbors are found,
	 * back track using a stack.
	 */
	private void buildMaze(Room current, boolean debug) {
		ArrayList<Room> neighbors = new ArrayList<Room>();
		Random rando = new Random();
		int randoNeighbor;
		
		while(visitedCount < totalRooms) {
			neighbors = findNeighbors(current);
			if(current.n == width - 1 && current.m == depth - 1) {
				foundFinish = true;
			}
			if(neighbors.size() >= 1) {
				randoNeighbor = rando.nextInt(neighbors.size());
				destroyWalls(neighbors.get(randoNeighbor), current);
				visitedRooms.push(current);
				current = neighbors.get(randoNeighbor);

				if((!foundFinish && !current.visited) || (!foundFinish && current.visited)) {
					numberMaze[current.n][current.m] = 1;
				}
				else if(foundFinish && !current.visited) {
					numberMaze[current.n][current.m] = 0;
				}
				visitedCount++;
				if(debug == true) {
					display();
				}
				buildMaze(current, debug);
			}
			else if (neighbors.size() == 0) {
				if(!foundFinish && current.visited) {
					numberMaze[current.n][current.m] = 1;
				}
				else if((!foundFinish) ||foundFinish && current.visited) {
					numberMaze[current.n][current.m] = 0;
				}
				if(visitedRooms.isEmpty() == true) {
					return;
				}
				else {
					current = visitedRooms.pop();
					buildMaze(current, debug);
				}
			}
		}
	}
	
	/*
	 * Private helper method to tear down walls between
	 * the current Room and the neighbor.
	 */
	private void destroyWalls(Room neighbor, Room current) {
		int n = neighbor.n;
		int m = neighbor.m;
		
		if(n < current.n) {
			//Destroy bottom wall
			maze[n][m] = new Room(n, m, 4, false);
		}
		else if(n > current.n) {
			//Destroy top wall
			maze[n][m] = new Room(n, m, 3, false);
		}
		else if(m < current.m) {
			//Destroy right wall
			maze[n][m] = new Room(n, m, 2, false);
		}
		else if(m > current.m){
			//Destroy left wall
			maze[n][m] = new Room(n, m, 1, false);
		}
	}
	
	/*
	 * Private helper method to find neighbors based
	 * on current vertex and returns in an ArrayList
	 * with new neighbors only.
	 */
	private ArrayList<Room> findNeighbors(Room current) {
		ArrayList<Room> neighbors = new ArrayList<Room>();
		int n = current.n;
		int m = current.m;
		
		if(n == 0 && m == 0) {
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m+1]);
		}
		else if(m == maze.length - 1 && n == maze.length - 1) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n][m-1]);
		}
		else if(n == 0 && m == maze.length - 1) {
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m-1]);
		}
		else if(n == maze.length - 1 && m == 0) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n][m+1]);
		}
		else if(n == 0) {
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m-1]);
			neighbors.add(maze[n][m+1]);
		}
		else if(m == 0) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m+1]);
		}
		else if(n == maze.length - 1) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n][m-1]);
			neighbors.add(maze[n][m+1]);
		}
		else if(m == maze.length - 1) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m-1]);
		}
		else {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m-1]);
			neighbors.add(maze[n][m+1]);
		}
		
		// Removes neighbors that have already been visited
		for(int i = neighbors.size() - 1; i >= 0; i--) {
			if(neighbors.get(i).visited == true) {
				neighbors.remove(i);
			}
		}
		/*
		System.out.print("Neighbors to choose from: ");
		for(int i = 0; i < neighbors.size(); i++) {
			System.out.print("[" + neighbors.get(i).n + "," + neighbors.get(i).m + "]");
		}
		System.out.println();
		*/
		return neighbors;
	}
	
	/*
	 * Private tester method. Tests to work if
	 * findNeigbors works and to test if walls
	 * are present
	 */
	private void testMaze() {
		//Test neighbors
		findNeighbors(current);
		findNeighbors(maze[2][2]);
		findNeighbors(maze[0][4]);
		findNeighbors(maze[4][0]);
		findNeighbors(maze[0][3]);
		findNeighbors(maze[4][4]);
		findNeighbors(maze[1][0]);
		
		//Test walls
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < depth; j++) {
				System.out.println("maze[" + i + "][" + j + "]");
				System.out.print("Left: " + maze[i][j].left + ", ");
				System.out.print("Right: " + maze[i][j].right + ", ");
				System.out.print("Up: " + maze[i][j].up + ", ");
				System.out.println("Down: " + maze[i][j].down);
			}
		}
	}
}
