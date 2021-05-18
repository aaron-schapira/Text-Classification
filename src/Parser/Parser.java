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
            boolean isTimer = false;
            try(FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
                out.println("[20");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line = br.readLine();
            StringBuilder processText = new StringBuilder();
            ArrayList<String> message = new ArrayList<>();
            String[] savedDate = null;
            while (line != null) {
                if (line.length() > 1) {
                    String[] temp = line.split(" ");
                    if (i == 0 && (line.charAt(0) != '[' && line.charAt(1) != '2' && line.charAt(2) != '0')) {
                        processText.append(String.join(" ", temp));
                        processText.append("\n");
                    }
                    if (i > 0 && line.charAt(0) == '-' && line.charAt(1) == '-' && line.charAt(2) == '-' && line.charAt(3) == '-') {
                        isTimer = true;
                        savedDate = temp;
                    }
                    else {
                        if (line.charAt(0) == '[' && line.charAt(1) == '2' && line.charAt(2) == '0') {
                            processText.append(String.join(" ", message));
                            if (isTimer) {
                                processText.append("\n");
                                processText.append("\n");
                                processText.append(String.join(" ", savedDate));
                                processText.append("\n");
                                isTimer = false;
                            }
                            i++;
                            message = new ArrayList<>();
                            isBlackListed = file.getBlackListUsername().contains(getUsername(line));
                        }
                        if ((i > 1 || line.charAt(0) == '-' && line.charAt(1) == '-' && line.charAt(2) == '-' && line.charAt(3) == '-')
                        && temp.length > 1) {
                            if (file.getBlackListUsername().contains(getUsername(line)) || isBlackListed) {
                                message.add("\n");
                                if (temp[0].length() > 1 && temp[0].charAt(1) == ':' && temp[0].charAt(temp[0].length() - 1) == ':') {
                                    message.add("THIS_IS_A_REACTION");
                                }
                                // CHECK TEMP HERE
                                message.addAll(Arrays.asList(temp));
                            }
                            else {
                                message.add("\n");
                                if (temp[0].length() > 1 && temp[0].charAt(1) == ':' && temp[0].charAt(temp[0].length() - 1) == ':') {
                                    message.add("THIS_IS_A_REACTION");
                                    // CHECK TEMP HERE
                                    message.addAll(Arrays.asList(temp));
                                }
                                else {
                                    message.add("LINE CENSORED");
                                }
                            }
                        }
                    }
                }
                line = br.readLine();
            }
//            PrintWriter out = new PrintWriter("src/Output/test_output.txt");
//            PrintWriter out = new PrintWriter("src/Output/02_general_output.txt");
//            System.out.println(processText);
//            out.println(processText);
            java.io.File file = new java.io.File("src/Output/test_output.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(processText.toString());
            }
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

    public String isUsernameBlacklisted(ArrayList<String> tokens){

        String username = " ";


        //First case: We see the <> for the username
        if(tokens.get(1).charAt(0) == '<' && tokens.get(1).charAt(tokens.get(1).length()-1) == '>'){

            StringBuilder temp = new StringBuilder();

            for (int i = 1; i < tokens.get(1).length()-1 ; i++) {
                temp.append(tokens.get(1).charAt(i));
            }
            username = temp.toString();
        }

        // Second case: We see the @
        else if(tokens.get(1).charAt(0) == '@'){

            StringBuilder temp = new StringBuilder();

            for(int i = 1; i<tokens.get(1).length(); i++){
                temp.append(tokens.get(1).charAt(i));
            }

            username = temp.toString();
        }
        else{
            username = tokens.get(1);
        }

        return username;
    }


    public File getFile() {
        return file;
    }
}
