package Parser;

import java.util.ArrayList;

public class File {

    private String path;
    private ArrayList<Employee> blackList;

    public File(String path, ArrayList<Employee> blackList) {

        this.path = path;
        this.blackList = blackList;
    }

    public String getPath() {
        return path;
    }

    public ArrayList<Employee> getBlackList() {
        return blackList;
    }
}
