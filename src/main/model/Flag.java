package model;

public class Flag {
    private String name;
    private int id;
    private String flag;
    private String code;


    public Flag(String name, int id, String flag, String code) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
