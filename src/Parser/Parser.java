package Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    File file;

    public Parser(File file){

        this.file = file;
    }

    public void start() {

        try {
            int i = 0;
            boolean isBlackListed = false;
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line = br.readLine();
            StringBuilder processText = new StringBuilder();
            ArrayList<String> message = new ArrayList<>();
            while (line != null) {
                if (line.length() > 1) {
                    String[] temp = line.split(" ");
                    if (i == 0 && (line.charAt(0) != '[' && line.charAt(1) != '2' && line.charAt(2) != '0')) {
                        processText.append(String.join(" ", temp));
                        processText.append("\n");
                    }
                    if (i > 0 && line.charAt(0) == '-' && line.charAt(1) == '-' && line.charAt(2) == '-' && line.charAt(3) == '-') {
                        processText.append("\n");
                        processText.append(String.join(" ", temp));
                        processText.append("\n");
                    }
                    else {
                        if (i > 0 || line.charAt(0) == '-' && line.charAt(1) == '-' && line.charAt(2) == '-' && line.charAt(3) == '-') {
                            if (file.getBlackListUsername().contains(getUsername(line)) || isBlackListed) {
                                message.add("\n");
                                if (temp[0].length() > 1 && temp[0].charAt(1) == ':' && temp[0].charAt(temp[0].length() - 1) == ':') {
                                    message.add("THIS_IS_A_REACTION");
                                }
                                // CHECK TEMP HERE
                                message.addAll(Arrays.asList(temp));
                                isBlackListed = true;
                            } else {
                                message.add("\n");
                                if (temp[0].length() > 1 && temp[0].charAt(1) == ':' && temp[0].charAt(temp[0].length() - 1) == ':') {
                                    message.add("THIS_IS_A_REACTION");
                                    // CHECK TEMP HERE
                                    message.addAll(Arrays.asList(temp));
                                } else {
                                    message.add("LINE CENSURED");
                                }
                                isBlackListed = false;
                            }
                        }
                        if (line.charAt(0) == '[' && line.charAt(1) == '2' && line.charAt(2) == '0') {
                            processText.append(String.join(" ", message));
                            processText.append("\n");
                            i++;
                            message = new ArrayList<>();
                            isBlackListed = false;
                        }
                    }
                }
                line = br.readLine();
            }
            PrintWriter out = new PrintWriter("src/Output/test_output.txt");
            out.println(processText);
        }
        catch(FileNotFoundException e) {
            System.out.println("Could not find, open or use chosen file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get Username
    public String getUsername(String line) {
        String username;

        StringBuilder temp = new StringBuilder();

        for (int j = 0; j < line.length(); j++) {
            if(line.charAt(j) == '<'){
                j++;
                while(line.charAt(j) != '>'){
                    temp.append(line.charAt(j));
                    j++;
                }
                break;
            }
        }

        username = temp.toString();

        return username;
    }

    // Method that goes through the message and check if we need to censure
    public void checkMessage(ArrayList<String> tokens){

        for(String token : tokens){
            if(token.charAt(0) == '@'){

                StringBuilder temp = new StringBuilder();

                for(int i = 1; i<token.length(); i++){
                    temp.append(token.charAt(i));
                }

                String username = temp.toString();

                if(!file.getBlackListUsername().contains(username)){
                    tokens.set(tokens.indexOf(token), "CENSORED NAME");
                }
            }
        }
    }


    
    // Construct the message

    // Method to get Username //AARON

    // Method that goes through the message and check if we need to censure //CLÉMENT

    // Filter the reactions

    // Get reaction

    // Get reactions reactions from deleted messages and check if one of the important guys is in it


    public File getFile() {
        return file;
    }
}
