package model;

public class Flag {
    private String name;
    private int difficulty;
    private String flag;
    private String code;


    public Flag(String name, String code, String flag, int diff) {
        this.name = name;
        this.difficulty = diff;
        this.flag = flag;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
