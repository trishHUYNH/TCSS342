import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

public class Main extends MyStack {

	public static void main(String[] args) throws IOException {
		 FileInputStream file = new FileInputStream("TestFiles/customer.txt");
		 BufferedReader reader = new BufferedReader(new InputStreamReader(file)); int orderCount = 0; String line;
		 
		 while ((line = reader.readLine()) != null) {
		 System.out.println("Processing Order " + orderCount + ": " + line);
		 parseLine(line);
		 orderCount++; 
		 }
		 
		 //testMyStack(); 
		 System.out.println("----------------------------------------------------------------------------------------------------------"); 
		 //testBurger();
	}

	/*
	 * Called from main. Parses String and passes on to correct function to build burger
	 */
	public static void parseLine(String line) throws IOException {
		Burger customerBurger;
		String orderParts[] = line.split(" ");

		if (Arrays.asList(orderParts).contains("Baron")) {
			customerBurger = new Burger(true);
			if(Arrays.asList(orderParts).contains("no") || Arrays.asList(orderParts).contains("but")) {
				baronBurgerOrder(orderParts, customerBurger);
			}
		} 
		else {
			customerBurger = new Burger(false);
			if(Arrays.asList(orderParts).contains("no")  || Arrays.asList(orderParts).contains("with")) {
				regularBurgerOrder(orderParts, customerBurger);
			}
		}
		checkPatty(orderParts, customerBurger);
		System.out.println(customerBurger + "\n");
	}
	
	/*
	 * Helper method for parseLine for Baron Burger.
	 * Iterates through array of orders and adds or remove ingredients
	 * for the Baron Burger
	 */
	private static void baronBurgerOrder(String[] orderParts, Burger customerBurger) {
		int endIndex = 0;
		
		for(int i = 0; i < orderParts.length; i++) {
			if(orderParts[i].equals("but")) {
				endIndex = i;
				break;
			}
			else if(orderParts[i].equals("Double") || (orderParts[i].equals("Triple")) || (orderParts[i].equals("Veggie")) || 
				(orderParts[i].equals("Chicken")) || (orderParts[i].equals("Baron")) || (orderParts[i].equals("Burger"))) {
				continue;
			}
			else if(orderParts[i].equals("Cheese") || orderParts[i].equals("Sauce") || orderParts[i].equals("Veggies")) {
				customerBurger.removeCategory(orderParts[i]);	
			}
			else {
				customerBurger.removeIngredient(orderParts[i]);
			}	
		}
		
		for(int i = endIndex; i < orderParts.length; i++) {
			customerBurger.addIngredient(orderParts[i]);
		}
	}
	
	/*
	 * Helper method for parseLine for regular burgers.
	 * Iterates through array of orders and adds or remove ingredients
	 * for the regular burger
	 */
	private static void regularBurgerOrder(String[] orderParts, Burger customerBurger) {
		int endIndex = 0;
		
		for(int i = 0; i < orderParts.length; i++) {
			if(orderParts[i].equals("but")) {
				endIndex = i;
				break;
			}
			else if(orderParts[i].equals("Double") || (orderParts[i].equals("Triple")) || (orderParts[i].equals("Veggie")) || 
				(orderParts[i].equals("Chicken")) || (orderParts[i].equals("Baron")) || (orderParts[i].equals("Burger"))) {
				continue;
			}
			else if(orderParts[i].equals("Cheese") || orderParts[i].equals("Sauce") || orderParts[i].equals("Veggies")) {
				customerBurger.addCategory(orderParts[i]);	
			}
			else {
				customerBurger.addIngredient(orderParts[i]);
			}	
		}
		
		for(int i = endIndex; i < orderParts.length; i++) {
			customerBurger.removeIngredient(orderParts[i]);
		}
	}

	/*
	 * Helper method for parseLine to check how many and what type
	 * of patty for the burger
	 */
	private static void checkPatty(String[] orderParts, Burger customerBurger) {
		if (Arrays.asList(orderParts).contains("Double")) {
			customerBurger.addPatty();
		} else if (Arrays.asList(orderParts).contains("Triple")) {
			customerBurger.addPatty();
			customerBurger.addPatty();
		}

		if (Arrays.asList(orderParts).contains("Chicken")) {
			customerBurger.changePatties("Chicken");
		} else if (Arrays.asList(orderParts).contains("Veggie")) {
			customerBurger.changePatties("Veggie");
		}

	}

	/*
	 * Tests MyStack class
	 */
	public static void testMyStack() {
		MyStack<String> stack = new MyStack<String>();

		stack.push("Bottom Bun");
		stack.push("Mushrooms");
		stack.push("Beef");
		stack.push("Pepperjack");
		stack.push("Tomato");
		stack.push("Lettuce");
		stack.push("Mayonaise");
		stack.push("Top Bun");

		System.out.println("Original burger: " + stack.toString());
		System.out.println("Size before popping: " + stack.size());
		System.out.println("Last ingredient added: " + stack.peek());
		stack.pop();
		System.out.println("Front of stack after first pop: " + stack.peek());
		System.out.println("Size after popping: " + stack.size());
		System.out.println("Is the stack empty?: " + stack.isEmpty());
		System.out.println("Burger after popping: " + stack.toString());
	}

	/*
	 * Tests Burger class
	 */
	private static void testBurger() {

		Burger burgerBaronTest = new Burger(true);
		System.out.println("Baron Burger stack:");
		System.out.println(burgerBaronTest.toString());
		System.out.println("Removing patty:");
		burgerBaronTest.removePatty();
		burgerBaronTest.addPatty();
		System.out.println("Added a patty:");
		System.out.println(burgerBaronTest.toString());
		burgerBaronTest.changePatties("Chicken");
		System.out.println("Changed patty to chicken: ");
		System.out.println(burgerBaronTest.toString());
		burgerBaronTest.removeIngredient("Pepperjack");
		System.out.println("Removing Pepperjack:");
		System.out.println(burgerBaronTest.toString());
		burgerBaronTest.removeCategory("Sauces");
		System.out.println("Removing sauces: " + burgerBaronTest.toString());
		burgerBaronTest.removeCategory("Cheese");
		burgerBaronTest.removeCategory("Veggies");
		System.out.println("Removing cheese & veggies: "
				+ burgerBaronTest.toString());

		Burger normalBurgerTest = new Burger(false);

		System.out.println("\n\nNormal Burger stack:");
		System.out.println(normalBurgerTest.toString());
		normalBurgerTest.addPatty();
		normalBurgerTest.addPatty();
		System.out.println("Added two patties:");
		System.out.println(normalBurgerTest.toString());
		normalBurgerTest.addCategory("Veggies");
		System.out.println("Adding veggie categories: ");
		System.out.println(normalBurgerTest.toString());
		normalBurgerTest.addCategory("Cheese");
		System.out.println("Adding cheese categories: ");
		System.out.println(normalBurgerTest.toString());
		normalBurgerTest.addIngredient("Ketchup");
		System.out.println("Add ketchup: ");
		System.out.println(normalBurgerTest.toString());
		System.out.println("Adding more than three patties: ");
		normalBurgerTest.addPatty();
		normalBurgerTest.changePatties("Veggie");
		System.out.println("Changed patty to veggie: ");
		System.out.println(normalBurgerTest.toString());
		normalBurgerTest.removePatty();
		System.out.println("Removing a patty:");
		System.out.println(normalBurgerTest.toString());
	}
}
