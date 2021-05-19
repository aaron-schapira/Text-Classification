package Parser;

import Parser.*;

import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static void main(String[]args) {

//        String path = "src/Input/test_text.txt";
//        String path = "src/Input/02_general.txt";
//        String out_path = "src/Output/02_general_output.txt";
        String path = "src/Input/Edward_O'Neill/edoneill1-masterharkin.txt";
        String out_path = "src/Output/edoneill1-masterharkin.txt";
        ArrayList<String> blackListName = new ArrayList<>();

        blackListName.add("Thomas Flood");
        blackListName.add("Andrew Crowe");
        blackListName.add("Glen Healy");
        blackListName.add("Brendan Jacobs");
        blackListName.add("John Murray");
        blackListName.add("Fergus Cassidy");
        blackListName.add("Seamus Bugler");
        blackListName.add("Edward O’Neill");
        blackListName.add("Stephen Harkin");

        ArrayList<String> blackListUsername = new ArrayList<>();

        blackListUsername.add("tomflood737");
        blackListUsername.add("andy.airamerica");
        blackListUsername.add("healyglen");
        blackListUsername.add("brendanjacobs");
        blackListUsername.add("j_johnmurray737");
        blackListUsername.add("fergus.cassidy");
        blackListUsername.add("seamusbugler");
        blackListUsername.add("edoneill1");
        blackListUsername.add("masterharkin");

        blackListUsername.add("tomflood737,");
        blackListUsername.add("andy.airamerica,");
        blackListUsername.add("healyglen,");
        blackListUsername.add("brendanjacobs,");
        blackListUsername.add("j_johnmurray737,");
        blackListUsername.add("fergus.cassidy,");
        blackListUsername.add("seamusbugler,");
        blackListUsername.add("edoneill1,");
        blackListUsername.add("masterharkin,");

//        ArrayList<File> file = new ArrayList<>();
//        file.add(new File(path, blackListUsername, blackListName));
//        Parser parser = new Parser(file, out_path);
//        parser.start();

        String[] dir_names = new String[]{"Andrew_Crowe", "Brendan_Jacobs", "Channels", "Edward_O'Neill", "Fergus_Cassidy", "Glen_Healy", "John_Murray", "Robby_Garland", "Seamus_Bugler", "Stephen_Harkin", "Thomas_Flood"};

        for (String name : dir_names) {
            java.io.File path_dir = new java.io.File("src/Input/" + name);
            java.io.File[] files_dir = path_dir.listFiles();
            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < Objects.requireNonNull(files_dir).length; i++) {
                files.add(new File(files_dir[i].getPath(), blackListUsername, blackListName));
            }
            Parser parser = new Parser(files, "src/Output/" + name + "_processed.txt");
            parser.start();
        }
    }
}
