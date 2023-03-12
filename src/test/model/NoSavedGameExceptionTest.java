package model;

import model.exceptions.NoSavedGameException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoSavedGameExceptionTest  {

    @Test
    void testNoSavedGameException() {
        try {
            throw new NoSavedGameException("No saved game found!");
        } catch (NoSavedGameException e) {
            assertEquals("No saved game found!", e.getMessage());
        }
    }
}
