package Output;

import Parser.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[]args) {

        String path = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/Ryanair/src/Input/test_text.txt";
//        String path = "/Users/clemdetry/Documents/Documents – Clem's MacBook Pro/Ryanair/src/Input/02_general.txt";
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

        ArrayList<File> files = new ArrayList<>();

//        files.add(new File(, blackListUsername, blackListName));
//        files.add(new File(, blackListUsername, blackListName));
//        files.add(new File(, blackListUsername, blackListName));
//        files.add(new File(, blackListUsername, blackListName));
//        files.add(new File(, blackListUsername, blackListName));

        Parser parser = new Parser(new File(path, blackListUsername, blackListName));
        parser.start();

//        String line = "[2018-02-22 18:01:51] <tomflood737> Brendan - you can edit messages here. @aaronschapira one advantage you'll like over WhatsApp ";
//
//        String[] temp = line.split(" ");
//
//        ArrayList<String> tokens = new ArrayList<>();
//
//        tokens.addAll(Arrays.asList(temp));
//
//        System.out.println(parser.getUsername(line));
//
//        parser.checkMessage(tokens);
//
//        System.out.println("tokens = " + tokens);
    }
}
