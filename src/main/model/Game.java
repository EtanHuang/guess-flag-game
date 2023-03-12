package model;

import org.json.JSONArray;
import org.json.JSONObject;

// Represents the state of a game
public class Game {
    private FlagList flags;
    private int answered;
    private int correct;
    private int difficulty;

    // EFFECTS: constructs a game with a list of flags, number of flags answered so far, correct so far and difficulty
    public Game(FlagList flags, int ans, int correct, int diff) {
        this.flags = flags;
        this.answered = ans;
        this.correct = correct;
        this.difficulty = diff;
    }

    // EFFECTS: returns the fields in game as a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("gameList", flagsToJson());
        json.put("answered", answered);
        json.put("correct", correct);
        json.put("difficulty", difficulty);
        return json;
    }

    // EFFECTS: returns the flags in the game as a JSONArray
    public JSONArray flagsToJson() {
        JSONArray flagsArray = new JSONArray();
        for (Flag f : flags.getFlags()) {
            flagsArray.put(f.toJson());
        }
        return flagsArray;
    }

    public FlagList getGameList() {
        return this.flags;
    }

    public int getAnswered() {
        return this.answered;
    }

    public int getCorrect() {
        return this.correct;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

}
