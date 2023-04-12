package ui;

import model.Flag;
import model.FlagList;
import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

// GUI representation of Game
public class GameGUI extends JFrame implements ActionListener, WindowListener {

    private static final String JSON_STORE = "./data/lastsession.json"; // address to store saved game

    private JFrame frame;
    private JPanel mainScreen;
    private JPanel flagPanel;
    private JLabel flagLabel;
    private JLabel correctWrongLabel;
    private JLabel mainBackground;
    private JLabel currentFlagLabel;

    private FlagList easyFlagList = new FlagList(); // All flags of difficulty 1
    private FlagList mediumFlagList = new FlagList(); // All flags of difficulty 2
    private FlagList hardFlagList = new FlagList(); // All flags of difficulty 3

    // buttons to handle user interaction
    private JButton btnSave;
    private JButton btnSubmit;
    private JButton btnRestart;
    private JButton btnSkip;
    private JButton btnQuit;
    private JButton btnAnswerKey;

    private int difficulty; // difficulty of current game
    private int count; // number of flags in current game
    private int correct; // number of flags the user answered correct so far
    private int current; // number of flags the user answered so far

    private JTextField textField; // textfield for user input answers

    private FlagList gameList = new FlagList(); // list of flags for current game
    private Boolean savedGame = false;
    private Game game;

    JsonWriter jsonWriter;
    JsonReader jsonReader;

    // EFFECTS: Initializes and creates the game GUI
    public GameGUI() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        scanFile();
        correctWrongLabel = new JLabel("");
        correctWrongLabel.setBounds(840,90,100,50);
        currentFlagLabel = new JLabel("");
        currentFlagLabel.setBounds(840, 140, 130, 50);
        frame = new JFrame("Flag Guessing Game");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(1000,600);
        frame.setResizable(false);
        initializeBackground();
        mainBackground.add(correctWrongLabel);
        mainBackground.add(currentFlagLabel);
        addButtons();
        addButtons2();
        setButtonActions();
        initializePanels();
        frame.add(mainScreen);
        frame.setVisible(true);
        frame.addWindowListener(this);
        startGame();
    }

    // MODIIFES: this
    // EFFECTS: Adds an image to the background
    public void initializeBackground() {
        ImageIcon backgroundImg = new ImageIcon("data/e4eafd10-b723-4f9d-8b33-e62b59d2b724.jpg");
        Image img = backgroundImg.getImage();
        Image scaledImg = img.getScaledInstance(1000,600, Image.SCALE_SMOOTH);
        backgroundImg = new ImageIcon(scaledImg);
        mainBackground = new JLabel("");
        mainBackground.setIcon(backgroundImg);
        mainBackground.setBounds(0,0,1000,600);
        mainScreen = new JPanel();
        mainScreen.setLayout(null);
        mainScreen.add(mainBackground);
    }

    // MODIFIES: this
    // EFFECTS: creates the flag panel and textfields
    public void initializePanels() {
        flagPanel = new JPanel();
        flagPanel.setBounds(25,25,750,500);
        textField = new JTextField();
        textField.setFont(new Font("Consolas", Font.PLAIN, 25));
        textField.setPreferredSize(new Dimension(100,40));
        textField.setBounds(790, 50, 180,40);
        mainScreen.add(textField);
    }

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

    // MODIFIES: this
    // EFFECTS: displays the current flag on screen
    public void displayFlag(Flag flag) {
        String route = "data//flags//" + flag.getFile();
        ImageIcon flagImage = new ImageIcon(route);
        flagImage.setImage(flagImage.getImage().getScaledInstance(750,500, Image.SCALE_SMOOTH));
        flagLabel = new JLabel(flagImage);
        flagLabel.setLocation(10,10);
        flagPanel.removeAll();
        flagPanel.add(flagLabel);
        currentFlagLabel.setForeground(Color.white);
        currentFlagLabel.setText("Flag " + Integer.toString(current + 1) + "/" + gameList.getSize());
        currentFlagLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        mainBackground.add(currentFlagLabel);
        mainScreen.add(flagPanel);
        frame.add(mainScreen);
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: displays the next flag on screen
    public void displayNextFlag() {
        // finished game
        if (current == gameList.getSize() - 1) {
            JOptionPane.showMessageDialog(frame,
                    "You got " + Integer.toString(correct) + " out of " + gameList.getSize(),
                    "",
                    JOptionPane.PLAIN_MESSAGE);
            endGame();
        } else {
            current++;
            displayFlag(gameList.getFlag(current));
        }
    }

    // MODIFIES: this
    // EFFECTS: starts the game
    public void startGame() {
        try {
            game = jsonReader.read();
            if (game.getDifficulty() != 0) {
                savedGame = true;
            }
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
        if (savedGame) {
            int n = JOptionPane.showConfirmDialog(frame, "You have a saved game. Would you like to load it?", "",
                    JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                this.gameList = game.getGameList();
                this.correct = game.getCorrect();
                this.current = game.getAnswered();
                this.difficulty = game.getDifficulty();
                displayFlag(gameList.getFlag(current));
            } else if (n == JOptionPane.NO_OPTION) {
                createGame();
            }
        } else { // no saved game
            createGame();
        }
    }

    // MODIFIES: this
    // EFFECTS: starts a new game
    public void createGame() {
        gameList.clear();
        this.correct = 0;
        this.current = 0;
        String diff = "";
        do {
            diff = JOptionPane.showInputDialog("Please enter a difficulty between 1-3");
        } while (Integer.parseInt(diff) < 1 || Integer.parseInt(diff) > 3);
        this.difficulty = Integer.parseInt(diff);
        inputNumberOfFlags();
        createGameList(count, difficulty);
        currentFlagLabel.setText("Flag 1/" + gameList.getSize());
        currentFlagLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        currentFlagLabel.setForeground(Color.white);
        mainBackground.add(currentFlagLabel);
        displayFlag(gameList.getFlag(0));
    }

    // MODIFIES: this
    // EFFECTS: takes user input for number of flags
    public void inputNumberOfFlags() {
        String c = "";
        if (this.difficulty == 1) {
            do {
                c = JOptionPane.showInputDialog("How many flags? Enter between 1-"
                + Integer.toString(easyFlagList.getSize()));
            } while (Integer.parseInt(c) <= 0 || Integer.parseInt(c) > easyFlagList.getSize());
            this.count = Integer.parseInt(c);
        } else if (this.difficulty == 2) {
            do {
                c = JOptionPane.showInputDialog("How many flags? Enter between 1-"
                        + Integer.toString(mediumFlagList.getSize()));
            } while (Integer.parseInt(c) <= 0 || Integer.parseInt(c) > mediumFlagList.getSize());
            this.count = Integer.parseInt(c);
        } else if (this.difficulty == 3) {
            do {
                c = JOptionPane.showInputDialog("How many flags? Enter between 1-"
                        + Integer.toString(hardFlagList.getSize()));
            } while (Integer.parseInt(c) <= 0 || Integer.parseInt(c) > hardFlagList.getSize());
            this.count = Integer.parseInt(c);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates list of flags for the game
    public void createGameList(int count, int diff) {
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
        Game currentGame = new Game(gameList, 1, correct, difficulty);
        currentGame.logGameCreated();
    }

    // MODIFIES: this
    // EFFECTS: sets button actions
    public void setButtonActions() {
        btnSubmit.addActionListener(this);
        btnSubmit.setActionCommand("Submit");
        btnSave.addActionListener(this);
        btnSave.setActionCommand("Save");
        btnRestart.addActionListener(this);
        btnRestart.setActionCommand("Restart");
        btnSkip.addActionListener(this);
        btnSkip.setActionCommand("Skip");
        btnQuit.addActionListener(this);
        btnQuit.setActionCommand("Quit");
        btnAnswerKey.addActionListener(this);
        btnAnswerKey.setActionCommand("Answer");
    }

    // MODIFIES: this
    // EFFECTS: does corresponding actions on button click
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Submit":
                checkAnswer();
                break;
            case "Save":
                saveGame();
                break;
            case "Quit":
                quitGameAction();
                break;
            case "Restart":
                restartAction();
                break;
            case "Skip":
                skipAction();
                break;
            case "Answer":
                String answerKey = generateAnswers();
                JOptionPane.showMessageDialog(frame, answerKey, "Answer Key", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user interaction when Restart button is clicked
    public void restartAction() {
        int n = JOptionPane.showConfirmDialog(frame, "You are starting a new game. "
                + "Your current game will be lost", "Restart", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            clearJsonSave();
            createGame();
        }
    }

    // EFFECTS: generates a String with the names of all the flags in current game
    public String generateAnswers() {
        String answers = "";
        for (int i = 0; i < gameList.getSize(); i++) {
            answers += Integer.toString(i + 1)  + ". " + gameList.getFlag(i).getName() + "\n";
        }

        return answers;
    }

    // MODIFIES: this
    // EFFECTS: action for skip button
    public void skipAction() {
        this.textField.setText("");
        correctWrongLabel.setText("");
        mainBackground.add(correctWrongLabel);
        Game currentGame = new Game(gameList, current + 1, correct, difficulty);
        currentGame.logSkipNextFlag();
        displayNextFlag();
    }

    // MODIFIES: this
    // EFFECTS: checks user entered answer
    public void checkAnswer() {
        String answer = textField.getText().trim();
        if (gameList.getFlag(current).getName().equalsIgnoreCase(answer)) {
            this.correct++;
            correctWrongLabel.setForeground(Color.green);
            correctWrongLabel.setText("Correct!");
            correctWrongLabel.setFont(new Font("Serif", Font.PLAIN, 25));
            mainBackground.add(correctWrongLabel);
            Game currentGame = new Game(gameList, current + 1, correct, difficulty);
            currentGame.logGuessFlagCorrect();
            displayNextFlag();
            textField.setText("");
        } else {
            correctWrongLabel.setForeground(Color.red);
            correctWrongLabel.setText("Wrong!");
            correctWrongLabel.setFont(new Font("Serif", Font.PLAIN, 25));
            mainBackground.add(correctWrongLabel);
        }
    }

    // MODIFIES: this
    // EFFECTS: launches popup menu that prompts user to save their game
    public void quitGameAction() {
        int n = JOptionPane.showConfirmDialog(frame, "Would you like to save your game before you quit?", "",
                JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            saveGame();
            frame.dispose();
        } else {
            frame.dispose();
        }
        Game currentGame = new Game(gameList, current, correct, difficulty);
        currentGame.printLog();
    }

    // MODIFIES: this
    // EFFECTS: adds buttons to frame
    public void addButtons() {
        btnSave = new JButton();
        btnSave.setText("Save");
        btnSave.setBounds(830,200,100,40);
        btnSave.setBackground(Color.white);
        mainBackground.add(btnSave);
        btnSubmit = new JButton();
        btnSubmit.setText("Submit");
        btnSubmit.setBounds(830,250,100,40);
        btnSubmit.setBackground(Color.white);
        mainBackground.add(btnSubmit);
        btnRestart = new JButton();
        btnRestart.setText("Restart");
        btnRestart.setBounds(830,300,100,40);
        btnRestart.setBackground(Color.white);
        mainBackground.add(btnRestart);
        btnSkip = new JButton();
        btnSkip.setText("Skip");
        btnSkip.setBounds(830,350,100,40);
        btnSkip.setBackground(Color.white);
        mainBackground.add(btnSkip);
    }

    // MODIFIES: this
    // EFFECTS: adds buttons to frame
    public void addButtons2() {
        btnQuit = new JButton();
        btnQuit.setText("Quit");
        btnQuit.setBounds(830,400,100,40);
        btnQuit.setBackground(Color.white);
        mainBackground.add(btnQuit);
        btnAnswerKey = new JButton();
        btnAnswerKey.setText("Answer Key");
        btnAnswerKey.setBounds(830,450,100,40);
        btnAnswerKey.setBackground(Color.white);
        mainBackground.add(btnAnswerKey);
    }

    // MODIFIES: this
    // EFFECTS: saves the game
    public void saveGame() {
        try {
            jsonWriter.open();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Game currentGame = new Game(gameList, current, correct, difficulty);
        jsonWriter.write(currentGame);
        jsonWriter.close();
    }

    // EFFECTS: clears the currently saved game
    public void clearJsonSave() {
        try {
            jsonWriter.open();
            game = new Game(new FlagList(), 0, 0,0);
            jsonWriter.write(game);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes end game commands
    public void endGame() {
        int n = JOptionPane.showConfirmDialog(frame, "Would you like to play again?", "",
                JOptionPane.YES_NO_OPTION);
        clearJsonSave();
        if (n == JOptionPane.YES_OPTION) {
            savedGame = false;
            startGame();
        }
        if (n == JOptionPane.NO_OPTION) {
            frame.dispose();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Game currentGame = new Game(gameList, current, correct, difficulty);
        currentGame.printLog();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
