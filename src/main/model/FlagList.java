package model;

import java.util.ArrayList;

// Class representation of a list of flags containing Flag objects.
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

    // REQUIRES: flags.getSize() >= 1
    // EFFECTS: returns a boolean representing whether flag is in flag list
    public boolean contains(Flag f) {
        return this.flags.contains(f);
    }

    public Flag getFlag(int n) {
        return flags.get(n);
    }

    // MODIFIES: this
    // EFFECTS: clears the flag list
    public void clear() {
        this.flags.clear();
    }

    // REQUIRES: flags.getSize >= 1
    // EFFECTS: returns the index of a flag in the flag list given the name
    public int returnIndex(String target) {
        for (int i = 0; i < this.flags.size(); i++) {
            if (this.flags.get(i).getName().equals(target)) {
                return i;
            }
        }
        return -1;
    }
}
