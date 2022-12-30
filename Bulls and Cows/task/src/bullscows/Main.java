package bullscows;

import java.util.*;

public class Main {
    static Scanner in = new Scanner(System.in);
    static StringBuilder number = new StringBuilder();
    static StringBuilder asterics = new StringBuilder();

    static boolean end = false;  // keeps game running

    public static void main(String[] args) {

        // creates new random number generator
        Random random = new Random();
        // for creating random letters
        Random r_letter = new Random();

        // initialize string/int variables for error checking
        String get_input = "";
        String get_c_range = "";
        int input = 0;
        int c_range = 0;

        // check input; part of assignment requirements
        try {
            System.out.println("Please, enter the secret code's length:");
            // get secret code length
            get_input = in.nextLine();
            input = Integer.parseInt(get_input);
            if (input == 0) {
                System.out.println("Error");
                System.exit(0);
            }

        } catch (Exception e) {
            System.out.printf("Error: \"%s\" isn't a valid number.\n", get_input);
            System.exit(0);
        }

        try {
            System.out.println("Input the number of possible symbols in the code:");
            // get number of possible letters (in this exercise)
            get_c_range = in.nextLine();
            c_range = Integer.parseInt(get_c_range);

        } catch (Exception r) {
            System.out.printf("Error: \"%s\" isn't a valid number.\n", get_c_range);
            System.exit(0);
        }

        String character = Character.toString((char) (c_range - 11 + 'a'));

        // so user can see *** matching code length
        asterics.append("*".repeat(Math.max(0, input)));

        String answer;  // String for answers in game

        if (input > 36) {
            System.out.println("Error: " +
                    "can't generate a secret number with a length of 36 because there aren't enough unique digits.");
            end = true;
        } else if (c_range > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            end = true;
        } else if (input > c_range) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d with %d " +
                    "unique symbols.\n", input, c_range);
            end = true;
        } else {
            if (c_range > 10) {
                System.out.printf("The secret is prepared: %s (0-9, a-%s).\n", asterics, character);
            } else {
                System.out.printf("The secret is prepared: %s (0-9).\n", asterics);
            }
            getRandom(input, random, c_range, r_letter, c_range);
            end = false;  // update end condition
        }
//        System.out.println(number); // (Just for checking number)

        int turn = 1;  // simple variable for counting turns

        //START

        // so we don't print the message if the game will not start
        if (!end) {
            System.out.println("Okay, let's start a game!");
        }

        while (!end) {
            System.out.printf("Turn %d:\n", turn);

            answer = in.nextLine();

            // check for bulls and cows
            int bulls = getBulls(answer, number.toString(), input);
            int cows = getCows(answer, number.toString(), input);

            // determine singular or plural; same right now for assignment requirements
            String b = bulls > 1 ? "bulls" : "bull";
            String c = cows - bulls > 1 ? "cows" : "cow";

            if (cows - bulls > 0 && bulls > 0) {
                System.out.printf("Grade: %d %s and %d %s.", bulls, b, cows - bulls, c);
            } else if (bulls > 0) {
                System.out.printf("Grade: %d %s.", bulls, b);
            } else if (cows - bulls > 0) {
                System.out.printf("Grade: %d %s.", cows - bulls, c);
            } else if (cows == 0 && bulls == 0) {
                System.out.println("Grade: None.");
            }
            if (bulls == input) {  // win condition
                System.out.println("\nCongratulations! You guessed the secret code.");
                end = true;
            } else {
                System.out.println();
            }
            turn++;
        }
        //END
    }

    // looks for exact positional matches; compares answer to secret code 1:1
    private static int getBulls(String answer, String secret_code, int input) {
        int c = 0;
        for (int j = 0; j < input; j++) {
            c = answer.charAt(j) == secret_code.charAt(j) ? ++c : c;
        }
        return c;
    }

    // looks for any match; compares every answer digit against each secret code position
    private static int getCows(String answer, String secret_code, int input) {
        int c = 0;
        for (int j = 0; j < input; j++) {
            for (int x = 0; x < input; x++) {
                c = answer.charAt(x) == secret_code.charAt(j) ? ++c : c;
            }
        }
        return c;
    }
    // gets random number or letter; ensures that there are no repeats
    private static void getRandom(int input, Random random, int num_symbols, Random r_letter, int c_range) {

        // for checking good/bad candidates
        boolean check = false;
        String candidate;

        // adds numbers to string based on user input (max 9, min 0)
        while (!end) {
            // randomize if the next character is number or letter
            int cVSn = (int) Math.floor(Math.random() * 9);
            // first element add a sense of randomness; second ensures number is first; second ensures we want letters
            if (cVSn < 5 &&  number.length() != 0 && c_range > 10) {
                // assigns random letter within range
                candidate = Character.toString((char) (r_letter.nextInt(c_range - 10) + 'a'));
            }
            else  {
                // assigns random number to string candidate
                candidate = Integer.toString(random.nextInt(10));
            }
            // first number isn't zero (old requirement)
            if (number.length() == 0) {
                number.append(candidate);
            }
            // after we assign our first value, we can start checking our previous assignments
            else {
                check = checkPrevious(candidate.charAt(0));
            }
            // if the number/letter isn't like any previous, we assign it.
            if (check) {
                number.append(candidate);
            }
            // end if we hit our secret code length
            if (number.length() == input) {
                end = true;
            }
        }

    }

    // looks at current length of number, then compares candidate against each character
    // false if it's a bad candidate; true if a good candidate
        private static boolean checkPrevious (char candidate) {
        for (int i = number.length(); i > 0; i--) {
                if (Objects.equals(candidate, number.charAt(i - 1))) {
                    return false;
                }
            }
            return true;
        }
}