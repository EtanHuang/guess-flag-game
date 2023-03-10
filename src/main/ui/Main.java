package ui;

import model.exceptions.NoSavedGameException;

public class Main {
    private static GameApp g;

    public static void main(String[] args) throws NoSavedGameException {
        try {
            g = new GameApp();
            g.loadGame();
        } catch (NoSavedGameException e) {
            System.out.println("You don't have a saved game! You must start a new game");
            g = new GameApp();
            g.runGame();
        }
    }
}
