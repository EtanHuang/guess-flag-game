package persistence;

import model.Game;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Based on JSONWriterTest from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JSONWriterTest {

    @Test
    void testWriterIllegalFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {

        }
    }

    @Test
    void testWriterEmptyGame() {
        try {
            JsonReader reader = new JsonReader("./data/testEmptyGame.json");
            Game g = reader.read();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyGame.json");
            writer.open();
            writer.write(g);
            writer.close();

            reader = new JsonReader("./data/testWriterEmptyGame.json");
            Game emptyGame = reader.read();
            assertEquals(0, emptyGame.getAnswered());
            assertEquals(0, emptyGame.getDifficulty());
            assertEquals(0, emptyGame.getGameList().getSize());
            assertEquals(0, emptyGame.getCorrect());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGame() {
        try {
            JsonReader reader = new JsonReader("./data/testSavedGame.json");
            Game g = reader.read();
            JsonWriter writer = new JsonWriter("./data/testWriterSavedGame.json");
            writer.open();
            writer.write(g);
            writer.close();
            reader = new JsonReader("./data/testWriterSavedGame.json");
            Game savedGame = reader.read();
            assertEquals(2, savedGame.getAnswered());
            assertEquals(1, savedGame.getDifficulty());
            assertEquals(3, savedGame.getGameList().getSize());
            assertEquals(2, savedGame.getCorrect());
            assertEquals("Germany", savedGame.getGameList().getFlag(1).getName());
            assertEquals("RU", savedGame.getGameList().getFlag(2).getCode());
            assertEquals(1, savedGame.getGameList().getFlag(2).getDiff());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
