package ui;

import model.Flag;
import model.FlagList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Game {
    private FlagList fl = new FlagList();
    private FlagList gameList = new FlagList();
    Scanner sc = new Scanner(System.in);

    public Game() {
        runGame();
    }

    public FlagList getGameList() {
        return this.gameList;
    }

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
                fl.addFlag(new Flag(name, code, image, diff));
            }
            line = scan.nextLine();
        }
    }

    public void createGameList(int count, int diff) {
        fl.clear();
        gameList.clear();
        getAllFlagsDifficulty(diff);
        int number;
        while (gameList.getSize() < count) {
            Random random = new Random();
            number = random.nextInt(fl.getSize());
            if (!gameList.contains(fl.getFlag(number))) {
                gameList.addFlag(fl.getFlag(number));
            }
        }
    }

    // MODIFIES: this
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

    public void restartGame() {
        runGame();
    }

    public void runGame() {
        System.out.println("How many Countries would you like to guess?");
        int count = parseInt(sc.nextLine());
        System.out.println("What difficulty would you like? 1 = Easy, 2 = Medium, 3 = Hard");
        int diff = parseInt(sc.nextLine());
        while (count < 0 || count >= 229) {
            System.out.println("Enter a number between 1-229!");
            count = parseInt(sc.nextLine());
        }
        createGameList(count, diff);
        run(count);
    }
}
