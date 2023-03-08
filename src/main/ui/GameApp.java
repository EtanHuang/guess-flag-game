package ui;

import model.Flag;
import model.FlagList;
import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// Game Application
public class GameApp {
    private FlagList diff1 = new FlagList(); // All flags of difficulty 1
    private FlagList diff2 = new FlagList(); // All flags of difficulty 2
    private FlagList diff3 = new FlagList(); // All flags of difficulty 3
    private FlagList gameList = new FlagList(); // Stores flags for a game
    Scanner sc = new Scanner(System.in);
    private static final String JSON_STORE = "./data/lastsession.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private int correct;
    private int current;
    private int diff;
    private Game game;

    // EFFECTS: Runs the Game Application
    public GameApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        scanFile();
        loadGame();
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
            if (diff == 1) {
                diff1.addFlag(new Flag(name, code, image, diff));
            } else if (diff == 2) {
                diff2.addFlag(new Flag(name, code, image, diff));
            } else if (diff == 3) {
                diff3.addFlag(new Flag(name, code, image, diff));
            }
            line = scan.nextLine();
        }
    }

    public void loadGame() {
        System.out.println("Would you like to load your game from last time? Y - yes, N - no");
        String yesNo = sc.nextLine();
        if (yesNo.trim().equalsIgnoreCase("y")) {
            try {
                game = jsonReader.read();
                System.out.println("Loaded from " + JSON_STORE);
                this.diff = game.getDifficulty();
                this.current = game.getAnswered();
                this.correct = game.getCorrect();
                this.gameList = game.getGameList();
                run(current, game.getGameList().getSize());
                return;
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        } else {
            runGame();
        }
    }

    // REQUIRES: 1 <= diff <= 3
    // MODIFIES: this
    // EFFECTS: creates a game list of count flags with given difficulty
    public void createGameList(int count) {
        gameList.clear();
        FlagList fl = new FlagList();
        if (1 == diff) {
            fl = diff1;
        } else if (2 == diff) {
            fl = diff2;
        } else if (3 == diff) {
            fl = diff3;
        }
        while (gameList.getSize() < count) {
            Random random = new Random();
            int number = random.nextInt(fl.getSize());
            if (!gameList.contains(fl.getFlag(number))) {
                gameList.addFlag(fl.getFlag(number));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user commands
    public void run(int start, int count) {
        while (start < count) {
            Flag currentFlag = gameList.getFlag(start);
            System.out.println(currentFlag.getCode());
            String command = sc.nextLine();
            if (command.trim().equals("quit")) {
                quitMenu(start);
                return;
            } else if (command.trim().equalsIgnoreCase("restart")) {
                restartGame();
                return;
            } else if (command.trim().equalsIgnoreCase(currentFlag.getName())) {
                System.out.println("You got it! Good job");
                correct++;
                start++;
            } else if (command.trim().equalsIgnoreCase("skip")) {
                System.out.println("The correct answer was " + currentFlag.getName());
                start++;
            } else if (!command.trim().equalsIgnoreCase(currentFlag.getName())) {
                System.out.println("Nope. Try again!");
            }
        }
        endGame(correct, count);
    }

    // EFFECTS: displays the quit menu to the user
    public void quitMenu(int start) {
        System.out.println("Would you like to save your game? Y - Yes N - No");
        String yesNo = sc.nextLine();
        if (yesNo.trim().equalsIgnoreCase("y")) {
            try {
                jsonWriter.open();
                Game currentGame = new Game(gameList, start, correct, diff);
                jsonWriter.write(currentGame);
                System.out.println("Your game was successfully saved to " + JSON_STORE + ".");
                System.out.println("Goodbye!");
                jsonWriter.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Goodbye!");
            return;
        }
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



    // EFFECTS: restarts the game
    public void restartGame() {
        runGame();
    }

    // EFFECTS: processes user input for difficulty
    public void inputDifficulty() {
        System.out.println("What difficulty would you like? 1 = Easy, 2 = Medium, 3 = Hard");
        diff = 0;
        do {
            System.out.println("Enter a number from 1-3 please");
            diff = parseInt(sc.nextLine());
        } while (diff < 1 || diff > 3);
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runGame() {
        inputDifficulty();
        System.out.println("How many countries would you like to guess?");
        int count = parseInt(sc.nextLine());
        if (1 == diff) {
            while (count < 0 || count > diff1.getSize()) {
                System.out.println("Enter a number between 1-" + Integer.toString(diff1.getSize()));
                count = parseInt(sc.nextLine());
            }
        } else if (2 == diff) {
            while (count < 0 || count > diff2.getSize()) {
                System.out.println("Enter a number between 1-" + Integer.toString(diff2.getSize()));
                count = parseInt(sc.nextLine());
            }
        } else if (3 == diff) {
            while (count < 0 || count > diff3.getSize()) {
                System.out.println("Enter a number between 1-" + Integer.toString(diff3.getSize()));
                count = parseInt(sc.nextLine());
            }
        }
        createGameList(count);
        showInfo();
        current = 0;
        correct = 0;
        run(current, count);
    }

    // EFFECTS: displays game commands for the user
    public void showInfo() {
        System.out.println("**********************************");
        System.out.println("Enter quit to quit the game");
        System.out.println("Enter restart to start a new game");
        System.out.println("Enter skip to skip the current country");
        System.out.println("**********************************");
    }
}
