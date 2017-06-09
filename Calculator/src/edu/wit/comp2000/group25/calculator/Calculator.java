package edu.wit.comp2000.group25.calculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.EmptyStackException;
import java.util.Scanner;

public class Calculator {
	
	// For testing purposes
	public static void main(String[] args) {
		System.out.println("Invalid Expressions Text File:");
		checkFile("Infix Calculator Expressions - multi-digit with invalid expressions -- 2016-10-04 01.txt");
		System.out.println("Valid Expressions Text File:");
		checkFile("Infix Calculator Expressions - valid -- 2016-10-04 01.txt");
		System.out.println("Multi Digit Expressions Text File:");
		checkFile("Infix Calculator Expressions - valid multi-digit -- 2016-10-04 01.txt");
	}

	private static VectorStack<String> operators;
	private static VectorStack<Double> operands;
	
	/**
	 * Takes an infix expression and returns the result.
	 * @param expression
	 * @return result
	 */
	public static Double evaluate(String expression) {
		try {
			return rawEvaluate(expression);
		} catch(EmptyStackException ex) {
			throw new IllegalStateException();
		}
	}
	
	private static Double rawEvaluate(String expression) {
		operators = new VectorStack<String>();
		operands = new VectorStack<Double>();
		String validOperands = "1234567890.";
		String validOperators = "+-*/()";
		String operandBuilder = ""; // Stores the operand as its being sieved from the array
		
		expression = expression.replaceAll(" ", ""); // Gets rid of spaces, they are irrelevant
		char[] expressionCharacterArray = expression.toCharArray();
		int lastIndex = expressionCharacterArray.length - 1;
		
		for(int index = 0; index <= lastIndex; index++) { // Loops through the expression
			String character = "" + expressionCharacterArray[index];
			String nextCharacter = (index != lastIndex)? "" + expressionCharacterArray[index + 1] : " ";
			
			if(validOperands.contains(character)) { // Filters for digits
				operandBuilder = operandBuilder + character;
				
				// Checks to see if the tempOperand is finished
				if(!validOperands.contains(nextCharacter)) {
					operands.push(Double.parseDouble(operandBuilder));
					operandBuilder = "";
				}
			} else if(validOperators.contains(character)) {
				// Processes the expression on the stack based on order of operations.
				if(!operators.isEmpty() && !operators.peek().equals("(") && isPriorityTo(operators.peek(),character)) {
					doAnOperation();
				}
				operators.push(character);
			}
		}
		
		// Finishes evaluation
		while(!operators.isEmpty()) {
			doAnOperation();
		}
		
		// Returns the result
		return operands.pop();
	}
	
	/**
	 * Evaluates the top operator with the top two operands.
	 */
	private static void doAnOperation() {
		String operator = operators.pop();
		
		if(operator.equals(")")) {
			while(!operators.peek().equals("(")) {
				doAnOperation();
			}
			operators.pop();
		} else {
			Double b = operands.pop();
			Double a = operands.isEmpty()? 0 : operands.pop();
		
			switch(operator) {
			case "+":
				operands.push(a+b);
				break;
			case "-":
				operands.push(a-b);
				break;
			case "*":
				operands.push(a*b);
				break;
			case "/":
				if(b == 0) throw new ArithmeticException();
				operands.push(a/b);
				break;
			default:
				return;
			}
		}
	}
	
	// Compares the priority of the operators
	private static boolean isPriorityTo(String operatorOne, String operatorTwo) {
		return getPriority(operatorOne) >= getPriority(operatorTwo);
	}
	
	// Defines the priority of different operators
	private static int getPriority(String operator) {
		switch(operator) {
		case "+":
			return 1;
		case "-":
			return 1;
		case "*":
			return 2;
		case "/":
			return 2;
		case "(":
			return 3;
		case ")":
			return 3;
		default:
			return 0;
		}
	}
	
	// Evaluates the stuff in the files
	private static void checkFile(String fileName){
		String input = "";
		Double output;
		try(Scanner sc = new Scanner(new File(fileName))) {
			while(sc.hasNext()) {
				input = sc.next();
				try {
					output = evaluate(input);
				} catch(Exception ex) {
					System.out.println("\""+input+"\" is an invalid expression.");
					continue;
				}
				if(output % 1 == 0) {
					System.out.printf(input + " = %.0f%n", output);
				} else {
					System.out.printf(input + " = %s%n", output);
				}
			}
		}
		catch(FileNotFoundException ex) {
			System.out.println("The file you have selected could not be found.%n"
				+ "Please make sure the file is located in the application folder.");
		}
	}
}
