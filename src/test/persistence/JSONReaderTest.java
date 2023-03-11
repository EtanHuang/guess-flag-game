package persistence;

import model.Game;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JSONReaderTest {

    @Test
    void testFileNotFound() {
        JsonReader reader = new JsonReader("./data/foundnotfound.json");
        try {
            Game g = reader.read();
            fail("IOException expected");
        } catch (IOException e) {

        }
    }

    @Test
    void testFileEmptyGame() throws IOException {
        JsonReader reader = new JsonReader("./data/testEmptyGame.json");
        try {
            Game g = reader.read();
            assertEquals(0, g.getAnswered());
            assertEquals(0, g.getDifficulty());
            assertEquals(0, g.getGameList().getSize());
            assertEquals(0, g.getCorrect());
        } catch (IOException e) {
            fail("Cannot read file.");
        }
    }

    @Test
    void testFileSavedGame() throws IOException {
        JsonReader reader = new JsonReader("./data/testSavedGame.json");
        try {
            Game g = reader.read();
            assertEquals(2, g.getAnswered());
            assertEquals(1, g.getDifficulty());
            assertEquals(3, g.getGameList().getSize());
            assertEquals(2, g.getCorrect());
            assertEquals("Germany", g.getGameList().getFlag(1).getName());
            assertEquals("RU", g.getGameList().getFlag(2).getCode());
            assertEquals(1, g.getGameList().getFlag(2).getDiff());
        } catch (IOException e) {
            fail("Cannot read file.");
        }
    }
}
