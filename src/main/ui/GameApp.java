package ui;

import model.Flag;
import model.FlagList;
import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;
import model.exceptions.NoSavedGameException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// Game Application
public class GameApp {
    private FlagList easyFlagList = new FlagList(); // All flags of difficulty 1
    private FlagList mediumFlagList = new FlagList(); // All flags of difficulty 2
    private FlagList hardFlagList = new FlagList(); // All flags of difficulty 3
    private FlagList gameList = new FlagList(); // Stores flags for a game
    Scanner sc = new Scanner(System.in);
    private static final String JSON_STORE = "./data/lastsession.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private int correct; // number of flags the user answered correct so far
    private int current; // number of flags the user answered so far
    private int diff; // difficulty of current game
    private Game game;
    boolean loaded = false;

    // EFFECTS: Runs the Game Application
    public GameApp() throws NoSavedGameException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        scanFile();
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
                easyFlagList.addFlag(new Flag(name, code, image, diff));
            } else if (diff == 2) {
                mediumFlagList.addFlag(new Flag(name, code, image, diff));
            } else if (diff == 3) {
                hardFlagList.addFlag(new Flag(name, code, image, diff));
            }
            line = scan.nextLine();
        }
    }

    // MODIIFES: this
    // EFFECTS: loads a game from file
    public void yesLoadGame() throws NoSavedGameException {
        try {
            game = jsonReader.read();
            this.diff = game.getDifficulty();
            this.current = game.getAnswered();
            this.correct = game.getCorrect();
            this.gameList = game.getGameList();
            loaded = true;
            if ((diff == 0) && (current == 0) && (correct == 0) && gameList.getSize() == 0) {
                throw new NoSavedGameException("You don't have a saved game! You must start a new game.");
            }
            System.out.println("Loaded from " + JSON_STORE);
            showInfo();
            run(current, game.getGameList().getSize());
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user commands to load a game from file
    public void loadGame() throws NoSavedGameException {
        System.out.println("Would you like to load your game from last time? Y - yes, N - no");
        String yesNo = sc.nextLine();
        boolean keepGoing = true;
        while (keepGoing) {
            if (yesNo.trim().equalsIgnoreCase("y")) {
                keepGoing = false;
                yesLoadGame();
            } else if (yesNo.trim().equalsIgnoreCase("n")) {
                keepGoing = false;
                runGame();
            } else {
                System.out.println("Enter Y or N!");
                yesNo = sc.nextLine();
            }
        }
    }

    // REQUIRES: 1 <= diff <= 3
    // MODIFIES: this
    // EFFECTS: creates a game list of count flags with given difficulty
    public void createGameList(int count) {
        gameList.clear();
        FlagList fl = new FlagList();
        if (diff == 1) {
            fl = easyFlagList;
        } else if (diff == 2) {
            fl = mediumFlagList;
        } else if (diff == 3) {
            fl = hardFlagList;
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
    public void run(int start, int count) throws NoSavedGameException {
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

    // EFFECTS: saves current game to file
    public void writeCurrentGame(Game g) {
        jsonWriter.write(g);
        System.out.println("Your game was successfully saved to " + JSON_STORE + ".");
        System.out.println("Goodbye!");
        jsonWriter.close();
    }

    // EFFECTS: displays the quit menu to the user
    public void quitMenu(int start) {
        System.out.println("Would you like to save your game? Y - Yes N - No");
        String yesNo = sc.nextLine();
        boolean keepGoing = true;
        while (keepGoing) {
            if (yesNo.trim().equalsIgnoreCase("y")) {
                keepGoing = false;
                try {
                    jsonWriter.open();
                    Game currentGame = new Game(gameList, start, correct, diff);
                    writeCurrentGame(currentGame);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (yesNo.trim().equalsIgnoreCase("n")) {
                keepGoing = false;
                System.out.println("Goodbye!");
                return;
            } else {
                System.out.println("Enter Y or N!");
                yesNo = sc.nextLine();
            }
        }
    }

    // EFFECTS: prints 2 different versions of delete game messages for 2 situations.
    public void printDeleteGameMessages(int a) {
        if (a == 1) {
            System.out.println("You finished your loaded game and it will be deleted.");
            System.out.println("You will start a new game.");
        } else if (a == 2) {
            System.out.println("FYI you finished your loaded game and it was deleted.");
        }
    }

    // EFFECTS: deletes the saved game in file
    public void deleteSave(int a) throws NoSavedGameException {
        printDeleteGameMessages(a);
        try {
            jsonWriter.open();
            Game currentGame = new Game(new FlagList(), 0, 0,0);
            jsonWriter.write(currentGame);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: processes end game user commands
    public void endGame(int correct, int count) throws NoSavedGameException {
        System.out.println("You got " + Integer.toString(correct) + "/" + count + " for this round.");
        System.out.println("Would you like to play again? Y/N");
        String again = sc.nextLine();
        boolean end = false;
        while (!end) {
            if (again.equalsIgnoreCase("y")) {
                end = true;
                if (loaded) {
                    deleteSave(1);
                }
                runGame();
            } else if (again.equalsIgnoreCase("n")) {
                if (loaded) {
                    deleteSave(2);
                }
                System.out.println("Goodbye!");
                return;
            } else {
                System.out.println("Y/N?");
                again = sc.nextLine();
            }
        }
    }

    // EFFECTS: restarts the game
    public void restartGame() throws NoSavedGameException {
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
    public void runGame() throws NoSavedGameException {
        inputDifficulty();
        System.out.println("How many countries would you like to guess?");
        int count = parseInt(sc.nextLine());
        if (1 == diff) {
            while (count < 0 || count > easyFlagList.getSize()) {
                System.out.println("Enter a number between 1-" + Integer.toString(easyFlagList.getSize()));
                count = parseInt(sc.nextLine());
            }
        } else if (2 == diff) {
            while (count < 0 || count > mediumFlagList.getSize()) {
                System.out.println("Enter a number between 1-" + Integer.toString(mediumFlagList.getSize()));
                count = parseInt(sc.nextLine());
            }
        } else if (3 == diff) {
            while (count < 0 || count > hardFlagList.getSize()) {
                System.out.println("Enter a number between 1-" + Integer.toString(hardFlagList.getSize()));
                count = parseInt(sc.nextLine());
            }
        }
        createGameList(count);
        showInfo();
        reset();
        run(current, count);
    }

    // MODIFIES: this
    // EFFECTS: resets current and correct to 0
    public void reset() {
        this.current = 0;
        this.correct = 0;
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
