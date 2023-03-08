package model;

import java.util.ArrayList;

// Class representation of a list of flags containing Flag objects.
public class FlagList {
    private ArrayList<Flag> flags;

    // EFFECTS: creates an empty list to store Flag objects.
    public FlagList() {
        this.flags = new ArrayList<>();
    }

    public ArrayList<Flag> getFlags() { return this.flags; }

    // MODIFIES: this
    // EFFECTS: adds a flag to the back of flags
    public void addFlag(Flag f) {
        this.flags.add(f);
    }

    // EFFECTS: returns the size of flags
    public int getSize() {
        return this.flags.size();
    }

    // EFFECTS: returns a boolean representing whether given flag is in flags
    public boolean contains(Flag f) {
        return this.flags.contains(f);
    }

    // REQUIRES: flags.getSize() > n
    // EFFECTS: returns the flag at index n
    public Flag getFlag(int n) {
        return flags.get(n);
    }

    // MODIFIES: this
    // EFFECTS: clears the flag list
    public void clear() {
        this.flags.clear();
    }

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
