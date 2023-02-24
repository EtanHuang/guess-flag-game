package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private FlagList testList;

    @BeforeEach
    private void setUp() {
        testList = new FlagList();
    }

    @Test
    void testFlagList() {
        Flag china = new Flag("China",  "CN", "china.png", 1);
        testList.addFlag(china);
        assertEquals("China", testList.getFlag(0).getName());
        assertTrue(testList.contains(china));
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
        Scanner scan;
        try {
            scan = new Scanner(new File("data\\countries.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line = scan.nextLine();
        while (line != null) {
            if (line.equals("break")) {
                break;
            }
            String[] vals = line.split(",");
            testList.addFlag(new Flag(vals[0], vals[1], vals[2], Integer.parseInt(vals[3])));
            line = scan.nextLine();
        }
        assertEquals(0, testList.returnIndex("Afghanistan"));
        assertEquals(209, testList.returnIndex("Zimbabwe"));
        assertEquals(90, testList.returnIndex("Italy"));
        assertEquals(36, testList.returnIndex("China"));
        assertEquals(153, testList.returnIndex("Qatar"));
        assertEquals(-1, testList.returnIndex("Country Not Found"));
        testList.clear();
    }
}