package Parser;

import java.util.ArrayList;

public class File extends java.io.File {

    private String path;
    private ArrayList<String> blackListUsername;
    private ArrayList<String> blackListName;

    public File(String path, ArrayList<String> blackListUsername, ArrayList<String> blackListName) {
        super(path);
        this.path = path;
        this.blackListUsername = blackListUsername;
        this.blackListName = blackListName;
    }

    public String getPath() {
        return path;
    }

    public ArrayList<String> getBlackListUsername() {
        return blackListUsername;
    }

    public ArrayList<String> getBlackListName() {
        return blackListName;
    }
}
