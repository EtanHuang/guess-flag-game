package ui;

import model.Flag;
import model.FlagList;
import model.Game;
import model.Globals;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Random;

public class GameGUI extends JFrame implements ActionListener {

    private static final String JSON_STORE = "./data/lastsession.json";

    private JFrame f;
    private JPanel mainScreen;
    private JPanel flagPanel;
    private JLabel flagLabel;

    private JButton save;
    private JButton submit;
    private JButton restart;
    private JButton skip;
    private JButton quit;
    private Boolean gameOver;
    // I need to let the user input difficulty
    // so, when the user begins a new game then a pop menu pops up

    private int difficulty;
    private int count;
    private int correct; // number of flags the user answered correct so far
    private int current; // number of flags the user answered so far

    private JLabel flagImage;
    private JTextField textField;

    private Flag currentFlag;
    private FlagList gameList = new FlagList();

    // easy medium and hard flag lists are in globals
    Globals g = new Globals();

    JsonWriter jsonWriter;
    JsonReader jsonReader;

    public GameGUI() {

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        g.scanFile();

        f = new JFrame("Flag Guessing Game");
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.setSize(1000,600);
        f.setResizable(false);
        mainScreen = new JPanel();
        mainScreen.setBackground(Color.lightGray);
        mainScreen.setLayout(null);
        addButtons();
        setButtonActions();
        initializePanels();

        f.add(mainScreen);

        // we want to have the same flag lists, be able to randomly choose flags, and get the flag images for the image panel.
        f.setVisible(true);
        startGame();
    }

    public void initializePanels() {
        flagPanel = new JPanel();
        flagPanel.setBounds(200,30,600,400);
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(250,40));
        textField.setBounds(450, 500, 100,20);
        mainScreen.add(textField);
    }

    public void displayFlag(Flag flag) {
        String route = "data//flags//" + flag.getFile();
        ImageIcon flagImage = new ImageIcon(route);
        Image flagimg = flagImage.getImage(); // ImageIcon has a method called getImage();
        Image newimg = flagimg.getScaledInstance(600,420, Image.SCALE_AREA_AVERAGING);
        flagImage = new ImageIcon(newimg);

        flagLabel = new JLabel(flagImage);
        flagPanel.removeAll();
        flagPanel.add(flagLabel);
        mainScreen.add(flagPanel);
        f.add(mainScreen);
        f.setVisible(true);
    }

    public void displayNextFlag() {
        if (current==gameList.getSize()-1) {
            JOptionPane.showMessageDialog(f,
                    "You got " + Integer.toString(correct) + " out of " + gameList.getSize(),
                    "",
                    JOptionPane.PLAIN_MESSAGE);
            endGame();
        } else {
            current++;
            displayFlag(gameList.getFlag(current));
        }
    }

    // when user opens the game they should be asked do they want to load
    // if there is no saved game starts a new game and ask the user difficulty and number of flags

    public void startGame() {
        gameList.clear();
        this.correct = 0;
        this.current = 0;
        String diff = JOptionPane.showInputDialog("Please enter a difficulty");
        this.difficulty = Integer.parseInt(diff);
        String c = JOptionPane.showInputDialog("How many flags?");
        this.count = Integer.parseInt(c);
        createGameList(count, difficulty);
        displayFlag(gameList.getFlag(0));
    }

    public void createGameList(int count, int diff) {
        FlagList fl = new FlagList();
        if (diff == 1) {
            fl = g.easyFlagList;
        } else if (diff == 2) {
            fl = g.mediumFlagList;
        } else if (diff == 3) {
            fl = g.hardFlagList;
        }
        while (gameList.getSize() < count) {
            Random random = new Random();
            int number = random.nextInt(fl.getSize());
            if (!gameList.contains(fl.getFlag(number))) {
                gameList.addFlag(fl.getFlag(number));
            }
        }
    }

    public void setButtonActions() {
        submit.addActionListener(this);
        submit.setActionCommand("Submit");
        save.addActionListener(this);
        save.setActionCommand("Save");
        restart.addActionListener(this);
        restart.setActionCommand("Restart");
        skip.addActionListener(this);
        skip.setActionCommand("Skip");
        quit.addActionListener(this);
        quit.setActionCommand("Quit");

    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        switch(e.getActionCommand()) {
            case "Submit":
                checkAnswer();
                break;
            case "Save":
                saveGame();
                break;
            case "Quit":
                saveGameAction();
                // prompts the user to save the game and quits application
                break;
            case "Restart":
                startGame();
                break;
            case "Skip":
                this.textField.setText("");
                displayNextFlag();
                break;
        }
    }


    public void checkAnswer() {
        String answer = textField.getText().trim();
        System.out.println(answer);
        if (gameList.getFlag(current).getName().equalsIgnoreCase(answer)) {
            this.correct++;
            System.out.println("Correct");
            displayNextFlag();
            textField.setText("");
        } else {
            System.out.println("Wrong!");
        }
    }

    // MODIFIES: this
    // EFFECTS: launches popup menu that prompts user to save their game
    public void saveGameAction() {
        int n = JOptionPane.showConfirmDialog(f, "Would you like to save your game?", "",
                JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            saveGame();
            f.dispose();
        } else {
            f.dispose();
        }
    }

    // use an abstract class for the buttons
    public void addButtons() {
        save = new JButton();
        save.setText("Save");
        save.setBounds(50,200,70,30);
        mainScreen.add(save);
        submit = new JButton();
        submit.setText("Submit");
        submit.setBounds(50,300,70,30);
        mainScreen.add(submit);
        restart = new JButton();
        restart.setText("Restart");
        restart.setBounds(50,400,70,30);
        mainScreen.add(restart);
        skip = new JButton();
        skip.setText("Skip");
        skip.setBounds(50,450,70,30);
        mainScreen.add(skip);
        quit = new JButton();
        quit.setText("Quit");
        quit.setBounds(50,500,70,30);
        mainScreen.add(quit);
    }

    // method for save
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

    // method for load
    public void loadGame() {

    }


    // method for ending a game
    // I need to tell the user their score, ask them do you want to play again like my console app
    // and input the difficulty and number of flags (basically starts a new game)
    public void endGame() {
        int n = JOptionPane.showConfirmDialog(f, "Would you like to play again?", "",
                JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            startGame();
        }
        if (n == JOptionPane.NO_OPTION) {
            f.dispose();
        }
    }
}
