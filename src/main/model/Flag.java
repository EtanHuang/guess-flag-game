package model;

// Class representation of a Flag with a name, difficulty, file name and a 2 letter code.
public class Flag {
    private String name; // name of the country
    private int difficulty; // difficulty to guess (1 = easy, 2 = medium, 3 = hard)
    private String file; // file name of the png
    private String code; // 2 letter abbreviation of the country name


    public Flag(String name, String code, String file, int diff) {
        this.name = name;
        this.difficulty = diff;
        this.file = file;
        this.code = code;
    }

    public int getDiff() {
        return difficulty;
    }

    public void setDiff(int diff) {
        this.difficulty = diff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
