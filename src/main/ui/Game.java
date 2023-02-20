package ui;

import model.Flag;
import model.FlagList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class Game {
    private FlagList fl = new FlagList();
    private FlagList gameList = new FlagList();
    Scanner sc = new Scanner(System.in);

    public Game() {
        runGame();
    }

    private void createGameList(int count) {
        Scanner scan;
        try {
            scan = new Scanner(new File("data\\countries_old.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line = scan.nextLine();

       for(int i=0; i<228; i++) {
            String[] vals = line.split(",");
            int id = parseInt(vals[0]);
            String name = vals[1];
            String code = vals[2];
            String img = vals[3];
            fl.addFlag(new Flag(name, id, img, code));
            line = scan.nextLine();
        }

        // generates count random flags without duplication
        while(gameList.getSize() <= count) {
            Random random = new Random();
            int number = random.nextInt(fl.getSize());
            if(!gameList.contains(fl.getFlag(number))) {
                gameList.addFlag(fl.getFlag(number));
            }
        }
        //for(int i=1; i<=count; i++) {
        // System.out.println(gameList.getFlag(i).getName());
        // }
    }

    private void runGame() {
        boolean keepGoing = true;
        System.out.println("How many Countries would you like to guess?");
        int count = parseInt(sc.nextLine());

        // choose count random flags without duplication from the flags
        while (count <= 0 || count >= 229) {
            System.out.println("Enter a number between 0-229!");
            count = parseInt(sc.nextLine());
        }

        createGameList(count);
        //System.out.println("What next?");
        int current = 1;
        while(current <= gameList.getSize()) {
            Flag currentFlag = gameList.getFlag(current);
            System.out.println(currentFlag.getCode());
            String command = sc.nextLine();
            if (command.equals("quit"))
            {
                System.out.println("Quitted");
                break;
            }
            else if (command.equals(currentFlag.getName()))
            {
                System.out.println("You got it! Good job");
                current++;
            }
            else if (command.equals("give up")) {
                System.out.println("The correct answer was " + currentFlag.getName());
                current++;
            }
            else if (!command.equals(currentFlag.getName()))
            {
                System.out.println("Nope. Try again!");
            }

        }
        System.out.println("Goodbye!");
    }


}
