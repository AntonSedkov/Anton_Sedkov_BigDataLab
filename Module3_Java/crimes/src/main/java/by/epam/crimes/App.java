package by.epam.crimes;

import by.epam.crimes.console.ConsoleRunner;

import java.util.Arrays;

public class App {

    public static void main(String[] args) {
        ConsoleRunner runner = new ConsoleRunner();
        System.out.println(Arrays.toString(args));
        runner.run(args);
    }

}