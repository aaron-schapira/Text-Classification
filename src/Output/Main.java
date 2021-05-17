package Output;

import Parser.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[]args) {

        String path = "";
        ArrayList<Employee> blackList = new ArrayList<>();
//        for (int i = 0; i < ; i++) {
//
//        }
        Parser parser = new Parser(new File(path, blackList));
        parser.start();
    }
}
