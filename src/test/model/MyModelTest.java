package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameTest {
    private FlagList testList;

    @BeforeEach
    private void setUp() {
        testList = new FlagList();
    }

    @Test
    void testEmptyList() {
        assertEquals(-1, testList.returnIndex("Flag"));
        assertEquals(0, testList.getSize());
        assertFalse(testList.contains(new Flag("Japan", "JP", "japan.png", 1)));
    }

    @Test
    void testFlagList() {
        Flag china = new Flag("China",  "CN", "china.png", 1);
        testList.addFlag(china);
        assertTrue(testList.contains(china));
        assertEquals("China", testList.getFlag(0).getName());
        assertEquals(1, testList.getSize());
        testList.clear();
        assertEquals(0, testList.getSize());
    }

    @Test
    void testFlagClass() {
        Flag canada = new Flag("", "", "", 0);
        canada.setDiff(1);
        canada.setName("Canada");
        canada.setCode("CA");
        canada.setFile("canada.png");
        assertEquals(1, canada.getDiff());
        assertEquals("Canada", canada.getName());
        assertEquals("CA", canada.getCode());
        assertEquals("canada.png", canada.getFile());
    }

    @Test
    void testReturnIndex() {
        testList.addFlag(new Flag("Afghanistan", "AF", "afghanistan.png", 1));
        testList.addFlag(new Flag("Italy", "IT", "italy.png", 1));
        testList.addFlag(new Flag("China", "CN", "china.png", 1));
        testList.addFlag(new Flag("Qatar", "QA", "qatar.png", 2));

        assertEquals(0, testList.returnIndex("Afghanistan"));
        assertEquals(1, testList.returnIndex("Italy"));
        assertEquals(2, testList.returnIndex("China"));
        assertEquals(3, testList.returnIndex("Qatar"));
        assertEquals(-1, testList.returnIndex("Canada"));
        testList.clear();
        assertEquals(0, testList.getSize());
    }

    public static class JSONReaderTest {
    }
}