package persistence;


import model.Flag;
import model.FlagList;
import model.Game;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
// Based on JsonReader from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Game read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkRoom(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Game from JSON object and returns it
    private Game parseWorkRoom(JSONObject jsonObject) {
        int answered = jsonObject.getInt("answered");
        int correct = jsonObject.getInt("correct");
        int diff = jsonObject.getInt("difficulty");
        JSONArray fl = jsonObject.getJSONArray("gameList");

        Game g = new Game(parseFlagList(fl), answered, correct, diff);
        return g;
    }

    // EFFECTS: parses FlagList from JSONArray
    private FlagList parseFlagList(JSONArray jsonArray) {
        FlagList fl = new FlagList();
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            fl.addFlag(parseFlag(nextThingy));
        }
        return fl;
    }

    // EFFECTS: parses Flag from FlagList
    private Flag parseFlag(JSONObject flag) {
        String name = flag.getString("name");
        String file = flag.getString("file");
        int diff = flag.getInt("difficulty");
        String code = flag.getString("code");
        Flag f = new Flag(name, code, file, diff);
        return f;
    }
}
