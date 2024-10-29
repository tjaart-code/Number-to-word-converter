package word_converter_fn;

import java.util.ArrayList;

/**
 * Number to word converter.
 * @author elmiguel
 */
public class WordConverter {

    // Arrays representing ones, elevens, tens, and large number groupings
    private static final String[] ONES = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private static final String[] ELEVENS = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    private static final String[] TENS = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
    private static final String[] GROUPING = {"hundred", "thousand", "million", "billion", "trillion", "quadrillion", "sextillion", "septillion", "octillion", "nonillion", "decillion"};

    /**
     * Returns the corresponding word for a given character digit in the given array.
     */
    public static String getDigit(String[] type, char digit) {
        if (type == TENS) digit -= 2;  // Adjust for tens index
        return type[digit - '0'].trim();
    }

    /**
     * Processes a two-digit number and converts it to words.
     */
    public static String processDouble(char first, char second) {
        String result = (first == '1') ? getDigit(ELEVENS, second) 
                                       : getDigit(TENS, first) + " " + getDigit(ONES, second);
        // Remove trailing "zero" if necessary
        if (result.endsWith("zero")) {
            result = result.substring(0, result.length() - 4);
        }
        return result.trim();
    }

    /**
     * Processes a three-digit number and converts it to words.
     */
    public static String processTriple(char hundreds, char tens, char ones, int set) {
        String hundredsPart = (hundreds == '0') ? "" : getDigit(ONES, hundreds) + " " + GROUPING[0];
        String tensOnesPart = (tens == '0') ? getDigit(ONES, ones) : processDouble(tens, ones);
        String suffix = GROUPING[set];

        return (hundredsPart + " " + tensOnesPart + " " + suffix).trim();
    }

    /**
     * Pads a number string with leading zeros to ensure it has a length that is a multiple of three.
     */
    public static String prePad(String number, int pad) {
        switch (pad) {
            case 1: return "00" + number;  // Pad two zeros for one digit
            case 2: return "0" + number;   // Pad one zero for two digits
            default: return number;
        }
    }

    /**
     * Processes a list of three-digit number groups and converts them to words.
     */
    public static String process(ArrayList<String> numbers) {
        StringBuilder result = new StringBuilder();
        String[] numArray = numbers.toArray(new String[0]);

        for (int i = 0; i < numArray.length; i++) {
            int setIndex = (numArray.length - 1) - i;
            result.append(" ").append(processTriple(numArray[i].charAt(0), numArray[i].charAt(1), numArray[i].charAt(2), setIndex));
        }
        // Remove the trailing grouping suffix and trim the result
        return result.substring(0, result.length() - 8).trim();
    }

    /**
     * Converts a numeric string into its word equivalent.
     */
    public static String convert(String number) {
        String result = "";
        int length = number.length();

        // Handle single-digit numbers
        if (length == 1) {
            return getDigit(ONES, number.charAt(0));
        }

        // Handle two-digit numbers
        if (length == 2) {
            if (number.charAt(0) == '1') {
                return getDigit(ELEVENS, number.charAt(1));  // Special case for numbers between 10 and 19
            } else {
                result = processDouble(number.charAt(0), number.charAt(1));
            }
        }

        // Handle numbers with three or more digits
        if (length >= 3) {
            // Pad the number with zeros to make its length a multiple of three
            number = prePad(number, number.length() % 3);

            ArrayList<String> groups = new ArrayList<>();
            for (int i = 0; i <= (number.length() - 1) / 3; i++) {
                groups.add(number.substring(i * 3, (i * 3) + 3));
            }
            result = process(groups);
        }
        return result.trim();
    }

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg != null && !arg.isEmpty()) {
                String input = arg.trim();
                boolean isNegative = input.startsWith("-");
                if (isNegative) {
                    input = input.substring(1);
                }
                String output = (isNegative ? "negative " : "") + convert(input);
                System.out.println("Input: " + arg + "\nOutput: " + output);
            }
        }
    }
}
