package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

// might or might not keep this idk
public class Globals {
    public FlagList easyFlagList = new FlagList(); // All flags of difficulty 1
    public FlagList mediumFlagList = new FlagList(); // All flags of difficulty 2
    public FlagList hardFlagList = new FlagList(); // All flags of difficulty 3

    public void scanFile() {
        Scanner scan;
        try {
            scan = new Scanner(new File("data\\countries.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        addFlags(scan);
    }

    // MODIFIES: this
    // EFFECTS: adds each flag to its difficulty's list
    public void addFlags(Scanner scan) {
        String line = scan.nextLine();
        while (line != null) {
            if (line.equals("break")) {
                break;
            }
            String[] vals = line.split(",");
            String name = vals[0];
            String code = vals[1];
            String image = vals[2];
            int diff = Integer.parseInt(vals[3]);
            if (diff == 1) {
                easyFlagList.addFlag(new Flag(name, code, image, diff));
            } else if (diff == 2) {
                mediumFlagList.addFlag(new Flag(name, code, image, diff));
            } else if (diff == 3) {
                hardFlagList.addFlag(new Flag(name, code, image, diff));
            }
            line = scan.nextLine();
        }
    }

    public FlagList createGameList(int count, int diff) {
        FlagList gameList = new FlagList();
        gameList.clear();
        FlagList fl = new FlagList();
        if (diff == 1) {
            fl = easyFlagList;
        } else if (diff == 2) {
            fl = mediumFlagList;
        } else if (diff == 3) {
            fl = hardFlagList;
        }
        while (gameList.getSize() < count) {
            Random random = new Random();
            int number = random.nextInt(fl.getSize());
            if (!gameList.contains(fl.getFlag(number))) {
                gameList.addFlag(fl.getFlag(number));
            }
        }

        return gameList;
    }
}
