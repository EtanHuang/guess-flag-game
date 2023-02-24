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
    private FlagList allFlagsOfDifficulty = new FlagList(); // All flags of a specific difficulty
    private FlagList gameList = new FlagList(); // Flags for each game
    Scanner sc = new Scanner(System.in);

    // EFFECTS: Runs the Game Application
    public Game() {
        runGame();
    }

    public FlagList getGameList() {
        return this.gameList;
    }

    // MODIFIES: this
    // EFFECTS: creates a list with all flags of a difficulty from 1-3
    public void getAllFlagsDifficulty(int d) {
        Scanner scan;
        try {
            scan = new Scanner(new File("data\\countries.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            if (d == diff) {
                allFlagsOfDifficulty.addFlag(new Flag(name, code, image, diff));
            }
            line = scan.nextLine();
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a game list with count flags and given difficulty
    public void createGameList(int count, int diff) {
        allFlagsOfDifficulty.clear();
        gameList.clear();
        getAllFlagsDifficulty(diff);
        int number;
        while (gameList.getSize() < count) {
            Random random = new Random();
            number = random.nextInt(allFlagsOfDifficulty.getSize());
            if (!gameList.contains(gameList.getFlag(number))) {
                gameList.addFlag(allFlagsOfDifficulty.getFlag(number));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user commands
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
        System.out.println("You got " + Integer.toString(correct) + "/" + count + ".");
    }

    // EFFECTS: restarts game
    public void restartGame() {
        runGame();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runGame() {
        System.out.println("How many Countries would you like to guess?");
        int count = parseInt(sc.nextLine());
        System.out.println("What difficulty would you like? 1 = Easy, 2 = Medium, 3 = Hard");
        int diff = parseInt(sc.nextLine());
        while (count < 0 || count > 210) {
            System.out.println("Enter a number between 1-210!");
            count = parseInt(sc.nextLine());
        }
        createGameList(count, diff);
        run(count);
    }
}
