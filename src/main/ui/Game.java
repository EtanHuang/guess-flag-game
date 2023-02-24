package ui;

import model.Flag;
import model.FlagList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// Game Application
public class Game {
    private FlagList diff1 = new FlagList(); // All flags of difficulty 1
    private FlagList diff2 = new FlagList(); // All flags of difficulty 2
    private FlagList diff3 = new FlagList(); // All flags of difficulty 3
    private FlagList gameList = new FlagList(); // Stores flags for a game

    // EFFECTS: Runs the Game Application
    public Game() {
        scanFile();
        runGame();
    }

    public FlagList getGameList() {
        return this.gameList;
    }

    // EFFECTS: scans the flags file
    public void scanFile() {
        Scanner scan;
        try {
            scan = new Scanner(new File("data\\countries.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        addFlags(scan);
    }

    // MODIFIES: this
    // EFFECTS: adds each flag to its difficulty's list
    public void addFlags(Scanner scan) {
        String line = scan.nextLine();
        while (line != null) {
            if (line.equals("break")) {
                break;
            }
            String[] vals = line.split(",");
            String name = vals[0];
            String code = vals[1];
            String image = vals[2];
            int diff = Integer.parseInt(vals[3]);
            if (1 == diff) {
                diff1.addFlag(new Flag(name, code, image, diff));
            } else if (2 == diff) {
                diff2.addFlag(new Flag(name, code, image, diff));
            } else if (3 == diff) {
                diff3.addFlag(new Flag(name, code, image, diff));
            }
            line = scan.nextLine();
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a game list with count flags and given difficulty
    public void createGameList(int count, int diff) {
        gameList.clear();
        FlagList difficulty = new FlagList();
        if (1 == diff) {
            difficulty = diff1;
        } else if (2 == diff) {
            difficulty = diff2;
        } else if (3 == diff) {
            difficulty = diff3;
        }
        int number;
        while (gameList.getSize() < count) {
            Random random = new Random();
            number = random.nextInt(difficulty.getSize());
            if (!gameList.contains(difficulty.getFlag(number))) {
                gameList.addFlag(difficulty.getFlag(number));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user commands
    Scanner sc = new Scanner(System.in);
    public void run(int count) {
        int current = 0;
        int correct = 0;
        while (current < count) {
            Flag currentFlag = gameList.getFlag(current);
            System.out.println(currentFlag.getCode());
            String command = sc.nextLine();
            if (command.equals("quit")) {
                return;
            } else if (command.equalsIgnoreCase("restart")) {
                restartGame();
                return;
            } else if (command.equalsIgnoreCase(currentFlag.getName())) {
                System.out.println("You got it! Good job");
                correct++;
                current++;
            } else if (command.equalsIgnoreCase("skip")) {
                System.out.println("The correct answer was " + currentFlag.getName());
                current++;
            } else if (!command.equalsIgnoreCase(currentFlag.getName())) {
                System.out.println("Nope. Try again!");
            }
        }
        endGame(correct, count);
    }

    // EFFECTS: processes end game user commands
    public void endGame(int correct, int count) {
        System.out.println("You got " + Integer.toString(correct) + "/" + count + " for this round.");
        System.out.println("Would you like to play again? Y/N");
        String again = sc.nextLine();
        boolean end = false;
        while (!end) {
            if (again.equalsIgnoreCase("y")) {
                end = true;
                runGame();
            } else if (again.equalsIgnoreCase("n")) {
                System.out.println("Goodbye!");
                return;
            } else {
                System.out.println("Y/N?");
                again = sc.nextLine();
            }
        }
    }

    // EFFECTS: restarts game
    public void restartGame() {
        runGame();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runGame() {
        System.out.println("What difficulty would you like? 1 = Easy, 2 = Medium, 3 = Hard");
        int diff = parseInt(sc.nextLine());
        System.out.println("How many Countries would you like to guess?");
        int count = parseInt(sc.nextLine());
        if (1 == diff) {
            while (count < 0 || count > 41) {
                System.out.println("Enter a number between 1-41");
                count = parseInt(sc.nextLine());
            }
        } else if (2 == diff) {
            while (count < 0 || count > 26) {
                System.out.println("Enter a number between 1-26");
                count = parseInt(sc.nextLine());
            }
        } else if (3 == diff) {
            while (count < 0 || count > 144) {
                System.out.println("Enter a number between 1-144");
                count = parseInt(sc.nextLine());
            }
        }
        createGameList(count, diff);
        run(count);
    }
}
