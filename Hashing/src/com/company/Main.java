package com.company;

import java.util.Scanner;

public class Main { //not using files this time
    //strictly alphanumeric, not symbols

    public static void main(String[] args) { // do not let them input in symbols

        boolean yn = false;

        Hash hash = new Hash();

        Scanner uIn = new Scanner(System.in);
        String words = "";
        int response;

        System.out.println("""
                This is a hashing program that will organise inputted words into a hashed map\s
                by converting each character within each word in the inputted string into a numerical value and
                converting this value into binary. The hash map will them be sorted based off this binary number
                divided by the current Array size. The maximum number of words allowed is 4480.""");

        do {
            System.out.println("\nInput the string you would like to hash: ");
            try {
                words = uIn.nextLine();
                System.out.println("This is the string: " + words);
                System.out.println("Is this correct?");
                do {
                    String input;
                    input = uIn.next();
                    if ("y".contains(input)) {
                        yn = true;
                    }
                } while (!yn);
            } catch (Exception e) {
                String garbage = uIn.next();
                System.out.println("Error: Invalid input. Value \"" + garbage + "\" is not valid.");
            }
        } while (!yn);

        hash.splitString(words);

        yn = false;

        do {
            System.out.println("\nWhich type of probing do you want: \n\t1. Linear\n\t2. Quadratic\n\t3. Analysis (Both)");
            try {
                response = uIn.nextInt();
                switch (response) {
                    case 1 -> hash.setProbingL(true);
                    case 2 -> hash.setProbingQ(true);
                    case 3 -> {
                        hash.setProbingL(true);
                        hash.reset();
                        hash.setProbingQ(true);
                        hash.analysis();
                    }
                }
                yn = true;
            } catch (Exception e) {
                String garbage = uIn.next();
                System.out.println("Error: Invalid input. Please input 1, 2, or 3. Value \"" + garbage + "\" is not valid.");
            }
        } while (!yn);

    }
}
