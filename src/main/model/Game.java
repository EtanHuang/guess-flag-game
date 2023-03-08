package model;

import org.json.JSONArray;
import org.json.JSONObject;

public class Game {
    private FlagList flags;
    private int answered;
    private int correct;
    private int difficulty;

    public Game(FlagList flags, int ans, int correct, int diff) {
        this.flags = flags;
        this.answered = ans;
        this.correct = correct;
        this.difficulty = diff;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("gameList", flagsToJson());
        json.put("answered", answered);
        json.put("correct", correct);
        json.put("difficulty", difficulty);
        return json;
    }

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
