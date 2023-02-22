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
        assertEquals("China", testList.getFlag(0).getName());
        assertTrue(testList.contains(china));
        assertEquals(1, testList.getSize());
    }

    @Test
    void testFlagClass() {
        Flag canada = new Flag("", "", "", 0);
        canada.setDiff(1);
        canada.setName("Canada");
        canada.setCode("CA");
        canada.setFlag("canada.png");
        assertEquals(1, canada.getDiff());
        assertEquals("Canada", canada.getName());
        assertEquals("CA", canada.getCode());
        assertEquals("canada.png", canada.getFlag());
    }
}