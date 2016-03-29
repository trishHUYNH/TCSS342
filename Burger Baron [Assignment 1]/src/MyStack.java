/*
 * Trish Huynh & Mary Lou Melendez
 * TCSS 342B
 * Spring 2015
 */

import java.util.*;

public class MyStack<Type> {

	private int size;
	private Node first;

	protected class Node {
		private Type ingredient;
		private Node next;
	}

	public MyStack() {
		first = null;
		size = 0;
	}

	/*
	 * If the first Node is null, then stack is empty.
	 */
	public boolean isEmpty() {
		if (first == null) {
			return true;
		} else {
			return false;
		}
	}

	public void push(Type item) {
		Node ogFirst = first;
		first = new Node();
		first.ingredient = item;
		first.next = ogFirst;
		size++;
	}

	public Type pop() {
		Type itemToReturn = first.ingredient;
		first = first.next;
		size--;
		return itemToReturn;
	}

	public Type peek() {
		if (isEmpty()) {
			System.out.println("Stack is empty.");
		}
		return first.ingredient;
	}

	public int size() {
		return size;
	}

	public String toString() {
		int counter = 0;
		String result = "[";
		Node current = first;

		while (current != null) {
			if (counter == size - 1) {
				result += current.ingredient + "]";
				current = current.next;
			} else {
				result += current.ingredient + ", ";
				current = current.next;
				counter++;
			}
		}
		return result;
	}
}
