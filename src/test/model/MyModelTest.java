package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private FlagList testList;

    @BeforeEach
    private void setUp() {
        testList = new FlagList();
    }

    @Test
    void testConstructor() {
        Flag china = new Flag("China",  "CN", "china.png", 1);
        testList.addFlag(china);
        assertEquals("China", testList.getFlag(1).getName());
        assertTrue(testList.contains(china));
        assertEquals(1, testList.getSize());
    }
}