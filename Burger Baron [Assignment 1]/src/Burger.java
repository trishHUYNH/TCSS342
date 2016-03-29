/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

public class Burger<Type> {

	private MyStack<String> bottomHalf = new MyStack<String>();
	private MyStack<String> topHalf = new MyStack<String>();
	private MyStack<String> baronBurgerReference = new MyStack<String>();
	private MyStack<String> tempReference = new MyStack<String>();

	private int pattyCount;

	/*
	 * Constructor. If theWorks is true, builds a baron burger.
	 * Else, it builds a normal burger. Default pattyCount to one
	 * and patty is beef. Builds baronBurgerReference
	 */
	public Burger(boolean theWorks) {
		pattyCount = 1;
		buildBaronBurger(baronBurgerReference); // Builds reference burger
		if (theWorks) {
			buildBaronBurger(bottomHalf);
		} else {
			bottomHalf.push("Bottom Bun");
			bottomHalf.push("Beef");
			bottomHalf.push("Top Bun");
		}
	}

	/*
	 * Helper method to build Baron Burger
	 */
	private void buildBaronBurger(MyStack<String> burger) {
		burger.push("Bottom Bun");
		burger.push("Ketchup");
		burger.push("Mustard");
		burger.push("Mushrooms");
		burger.push("Beef");
		burger.push("Cheddar");
		burger.push("Mozzarella");
		burger.push("Pepperjack");
		burger.push("Onions");
		burger.push("Tomato");
		burger.push("Lettuce");
		burger.push("Baron-Sauce");
		burger.push("Mayonnaise");
		burger.push("Top Bun");
		burger.push("Pickle");
	}

	/*
	 * Pops ingredients off of bottom half if it is not beef. If it is beef, pop
	 * beef off and push desired patty
	 */
	public void changePatties(String pattyType) {
		String ingredient = "";
		while (!bottomHalf.isEmpty()) {
			ingredient = bottomHalf.pop();
			if (ingredient.equals("Beef")) {
				bottomHalf.push(pattyType);
			} else {
				topHalf.push(ingredient);
			}
		}
		combineHalves(bottomHalf, topHalf);
	}

	/*
	 * Add patties. If there are already three patties, exits out of method.
	 * Pops ingredients off bottom half and push on to top half. If the
	 * ingredient is a patty, push on to bottom half, then break out of loop.
	 */
	public void addPatty() {

		if (pattyCount == 3) {
			System.out
					.println("No more patties can be added. Health concern error!");
			return;
		}

		String ingredient = "";
		while (!bottomHalf.isEmpty()) {
			ingredient = bottomHalf.peek();
			if (ingredient.equals("Pepperjack") || ingredient.equals("Mozzarella") 
					|| ingredient.equals("Cheddar") || ingredient.equals("Beef")) {
				bottomHalf.push("Beef");
				break;
			}
			else {
				topHalf.push(bottomHalf.pop());	
			}
		}
		pattyCount++;
		combineHalves(bottomHalf, topHalf);
	}

	/*
	 * Remove patties. If patty count is one, exits out of method. Pops
	 * ingredients off of bottom half, when first patty is found, break out of
	 * loop. Else, push ingredients from bottom half to top half.
	 */
	public void removePatty() {

		if (pattyCount == 1) {
			System.out.println("Burger must have at least one patty!");
			return;
		}

		String ingredient = "";
		while (!bottomHalf.isEmpty()) {
			ingredient = bottomHalf.pop();
			if (ingredient.equals("Beef") || ingredient.equals("Chicken")
					|| ingredient.equals("Veggie")) {
				break;
			} else {
				topHalf.push(ingredient);
			}
		}
		combineHalves(bottomHalf, topHalf);
	}

	/*
	 * Applies only to normal burgers. <Patty Count> <Patty Type> Burger with
	 * <additions> but no <exceptions>
	 */
	public void addCategory(String type) {
        if(type.equals("Cheese")) {
            addIngredient("Pepperjack");
            addIngredient("Mozzarella");
            addIngredient("Cheddar");
        } 
        else if (type.equals("Sauce")){
            addIngredient("Mayonnaise");
            addIngredient("Baron-Sauce");
            addIngredient("Mustard");  
            addIngredient("Ketchup");
        } 
        else if (type.equals("Veggies")){
        	addIngredient("Pickle");
            addIngredient("Lettuce");
            addIngredient("Tomato");
            addIngredient("Onions");  
            addIngredient("Mushrooms");  
        }
	}

	/*
	 * Applies only to Baron Burger. <Patty Count> <Patty Type> Baron Burger
	 * with no <omissions> but <exceptions> Removes category by peeking at top
	 * of stack, if it's the correct ingredient method will pop off. If not,
	 * then will pop off and push on to top half to combine.
	 */
	public void removeCategory(String type) {
		String ingredient = "";

		if (type.equals("Cheese")) {
			while (!bottomHalf.isEmpty()) {
				ingredient = bottomHalf.peek();
				if (ingredient.equals("Pepperjack")
						|| ingredient.equals("Mozzarella")
						|| ingredient.equals("Cheddar")) {
					bottomHalf.pop();
				} else {
					topHalf.push(bottomHalf.pop());
				}
			}
		} else if (type.equals("Sauce")) {
			while (!bottomHalf.isEmpty()) {
				ingredient = bottomHalf.peek();
				if (ingredient.equals("Ketchup")
						|| ingredient.equals("Mustard")
						|| ingredient.equals("Mayonnaise")
						|| ingredient.equals("Baron-Sauce")) {
					bottomHalf.pop();
				} else {
					topHalf.push(bottomHalf.pop());
				}
			}
		} else if (type.equals("Veggies")) {
			while (!bottomHalf.isEmpty()) {
				ingredient = bottomHalf.peek();
				if (ingredient.equals("Lettuce") || ingredient.equals("Tomato")
						|| ingredient.equals("Onions")
						|| ingredient.equals("Pickle")
						|| ingredient.equals("Mushrooms")) {
					bottomHalf.pop();
				} else {
					topHalf.push(bottomHalf.pop());
				}
			}
		}
		combineHalves(bottomHalf, topHalf);
	}

	/*
	 * Applies to both Baron Burger and normal burger
	 */
	public void addIngredient(String type) {
		String referenceIngredient = "";
		while (!baronBurgerReference.isEmpty()) {
			referenceIngredient = baronBurgerReference.pop(); //pop off first in stack
			if (referenceIngredient.equals(type)) { //if ingredient popped is equal to type passed
				referenceIngredient = baronBurgerReference.peek(); //then ingredient is now the next item on stack
				addIngredientHelper(referenceIngredient, type); //call helper method
				break;
			} 
			else { //if it's not, then push the reference on to a temp stack
				tempReference.push(referenceIngredient);
			}
		}
		combineHalves(bottomHalf, topHalf);
		combineHalves(baronBurgerReference, tempReference);
	}
	
	/*
	 * Helper method for addIngredients
	 */
	private void addIngredientHelper(String reference, String type) {
		boolean foundIt = false; 
		while(!foundIt) { //while boolean foundIt is false
			while(!bottomHalf.isEmpty()) { //loop until the bottomHalf stack is empty
				if(reference.equals(bottomHalf.peek())) { //if reference passed is equal to peek of bottomHalf
					bottomHalf.push(type); //push the onion on
					foundIt = true; // found it is true
					return; //Return back to addIngredient
				}
				else {
					topHalf.push(bottomHalf.pop()); //else pop off top bun put on to tophalf
				}

			}
			combineHalves(bottomHalf, topHalf); //bottom half is now empty, so combine
			tempReference.push(baronBurgerReference.pop()); //pop off reference and push to temp
			reference = baronBurgerReference.peek(); //reference is the new peek
		}
	}

	/*
	 * Remove ingredient passed. Pops ingredients off of bottom half, if
	 * ingredient popped is equal to ingredient to remove, break out of loop.
	 * Cannot remove buns. Else, push ingredients from bottom half to top half.
	 */
	public void removeIngredient(String type) {

		if (type.equals("Top Bun") || type.equals("Bottom Bun")) {
			System.out.println("Buns cannot be removed!");
		}

		String ingredient = "";
		while (!bottomHalf.isEmpty()) {
			ingredient = bottomHalf.pop();
			if (ingredient.equals(type)) {
				break;
			} else {
				topHalf.push(ingredient);
			}
		}
		combineHalves(bottomHalf, topHalf);
	}

	/*
	 * Combines  two stacks by popping off the second and
	 * pushing on to the first
	 */
	public void combineHalves(MyStack<String> bottom, MyStack<String> top) {
		while (!top.isEmpty()) {
			bottom.push(top.pop());
		}
	}

	/*
	 * Prints to console content of burger from top to bottom.
	 */
	public String toString() {
		String result;
		result = bottomHalf.toString();
		return result;
	}
}
