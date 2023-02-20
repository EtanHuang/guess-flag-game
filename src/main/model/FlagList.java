package model;

import java.util.ArrayList;

public class FlagList {
    private ArrayList<Flag> flags;

    public FlagList() {
        this.flags = new ArrayList<>();
    }

    public void addFlag(Flag f) {
        this.flags.add(f);
    }
}
