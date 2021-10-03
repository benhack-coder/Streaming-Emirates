package common;

public class UserInputChecker {

    public static String userInputAsUrl(String input) {
        input = input.replace(" ", "+");
        return input;
    }
}
