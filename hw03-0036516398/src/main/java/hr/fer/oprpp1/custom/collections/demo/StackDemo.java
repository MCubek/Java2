package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {

    public static void main(String[] args) {
        ObjectStack stack = new ObjectStack();

        try {
            for (var element : args[0].split(" ")) {
                if (element.matches("-?\\d+")) {
                    stack.push(Integer.valueOf(element));
                } else {
                    Integer rightElement = (Integer) stack.pop();
                    Integer leftElement = (Integer) stack.pop();

                    Integer result =
                            switch (element) {
                                case "+" -> leftElement + rightElement;
                                case "-" -> leftElement - rightElement;
                                case "*" -> leftElement * rightElement;
                                case "/" -> leftElement / rightElement;
                                default -> throw new IllegalArgumentException(element + " is not a valid operator!");
                            };

                    stack.push(result);
                }
            }
            if (stack.size() != 1)
                throw new IllegalArgumentException("Expression does not evaluate to one value!");

            System.out.println("Expression evaluates to " + stack.pop()+".");

        } catch (ArithmeticException | IllegalArgumentException exception) {
            System.err.println("Program encountered an error and has stopped");
            System.err.println(exception.getMessage());
        }
    }
}
