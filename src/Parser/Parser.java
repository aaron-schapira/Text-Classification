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
                            isBlackListed = file.getBlackListUsername().contains((getUsername(temp).toLowerCase()));
                        }
                        if ((i > 1 || line.charAt(0) == '-' && line.charAt(1) == '-' && line.charAt(2) == '-' && line.charAt(3) == '-')
                            && temp.length > 1) {
                            if (file.getBlackListUsername().contains((getUsername(temp).toLowerCase())) || isBlackListed) {
                                message.add("\n");
                                if (temp[0].length() > 1 && temp[0].charAt(1) == ':' && temp[0].charAt(temp[0].length() - 1) == ':') {
                                    temp = addReactionSymbol(temp);
                                }
                                // CHECK TEMP HERE
                                temp = filterReaction(temp);
                                temp = checkMessage(temp);
                                message.addAll(Arrays.asList(temp));
                            }
                            else {
                                message.add("\n");
                                if (temp[0].length() > 1 && temp[0].charAt(1) == ':' && temp[0].charAt(temp[0].length() - 1) == ':') {
                                    temp = addReactionSymbol(temp);
                                    // CHECK TEMP HERE
                                    temp = filterReaction(temp);
                                    temp = checkMessage(temp);
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

    // Method that goes through the message and check if we need to censure
    public String[] checkMessage(String[] tokens){

        ArrayList<String> copy = new ArrayList<>(Arrays.asList(tokens));
        for(String token : tokens){
            if (token.length() > 1) {
                if (token.charAt(0) == '@') {

                    StringBuilder temp = new StringBuilder();

                    for (int i = 1; i < token.length(); i++) {
                        temp.append(token.charAt(i));
                    }

                    String username = temp.toString();

                    if (!file.getBlackListUsername().contains(username)) {
                        copy.set(copy.indexOf(token), "CENSORED NAME");
                    }
                }
            }
        }
        return copy.toArray(new String[0]);
    }

    public String getUsername(String[] tokens){

        String username = " ";

        //First case: We see the <> for the username
        if (tokens.length > 2 && tokens[2].length() > 1) {
            if (tokens[2].charAt(0) == '<' && tokens[2].charAt(tokens[2].length() - 1) == '>') {

                StringBuilder temp = new StringBuilder();

                for (int i = 1; i < tokens[2].length() - 1; i++) {
                    temp.append(tokens[2].charAt(i));
                }
                username = temp.toString();
            }
            // Second case: We see the @
            else if (tokens[2].charAt(0) == '@') {

                StringBuilder temp = new StringBuilder();

                for (int i = 1; i < tokens[2].length(); i++) {
                    temp.append(tokens[2].charAt(i));
                }

                username = temp.toString();
            } else {
                username = tokens[2];
            }
        }
        return username;
    }

    public String[] filterReaction(String[] tokens){

        ArrayList<String> copy = new ArrayList<>(Arrays.asList(tokens));
        ArrayList<String> out = new ArrayList<>();

        // Check if we have a reaction
        if(tokens[0].equals("THIS_IS_A_REACTION")) {
            int counter = 0;
            // Check the emoji and the person that reacted, We start 2 index after the token to get the first name
            for (int i = 2; i < tokens.length; i++) {
                // Check if the person that reacted is in the blacklist
                if(file.getBlackListUsername().contains(tokens[i])) {
                    counter++;
                }
                else {
                    copy.set(copy.indexOf(tokens[i]), "CENSORED NAME,");
                }
            }

            if(counter == 0) {
                out.add("REACTION");
                out.add("CENSORED");

                return out.toArray(new String[0]);
            }
            else {
                copy.remove(tokens[0]); // remove the THIS_IS_A_REACTION message
            }
        }
        return copy.toArray(new String[0]);
    }

    public String[] addReactionSymbol(String[] arr) {

        String[] temp = new String[arr.length+1];
        temp[0] = "THIS_IS_A_REACTION";
        for (int i = 1; i < temp.length; i++) {
            temp[i] = arr[i-1];
        }
        return temp;
    }

    public File getFile() {
        return file;
    }
}
