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

    public int getSize() {
        return this.flags.size();
    }

    public Boolean contains(Flag f) {
        return this.flags.contains(f);
    }

    // gets the flag with given index
    public Flag getFlag(int n) {
        return flags.get(n - 1);
    }
}
