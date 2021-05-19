package Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    private ArrayList<File> files;
    private String outputPath;

    public Parser(ArrayList<File> files, String outputPath){

        this.files = files;
        this.outputPath = outputPath;
    }

    public void start() {

        StringBuilder processText = new StringBuilder();
        int count = 0;
        for (File file : files) {
            try {
                int i = 0;
                boolean isBlackListed = false;
                boolean isTimer = false;
                try (FileWriter fw = new FileWriter(file, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.println("[20");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);
                String line = br.readLine();
                ArrayList<String> message = new ArrayList<>();
                String[] savedDate = null;
                while (line != null) {
                    if (line.length() > 2) {
                        String[] temp = line.split(" ");
                        if (i == 0 && (line.charAt(0) != '[' && line.charAt(1) != '2' && line.charAt(2) != '0')) {
                            temp = checkUsername(temp, file);
                            processText.append(String.join(" ", temp));
                            processText.append("\n");
                        }
                        else if (i > 0 && line.charAt(0) == '-' && line.charAt(1) == '-' && line.charAt(2) == '-') {
                            isTimer = true;
                            savedDate = temp;
                        } else {
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
                            if ((i >= 1 || line.charAt(0) == '-' && line.charAt(1) == '-' && line.charAt(2) == '-' && line.charAt(3) == '-') && temp.length > 1) {
                                if (file.getBlackListUsername().contains((getUsername(temp).toLowerCase())) || isBlackListed) {
                                    message.add("\n");
                                    if (temp[0].length() > 1 && temp[0].charAt(1) == ':' && temp[0].charAt(temp[0].length() - 1) == ':') {
                                        temp = addReactionSymbol(temp);
                                    }
                                    // CHECK TEMP HERE
                                    temp = filterReaction(temp, file);
                                    temp = checkMessage(temp, file);
                                    message.addAll(Arrays.asList(temp));
                                } else {
                                    message.add("\n");
                                    if (temp[0].length() > 1 && temp[0].charAt(1) == ':' && temp[0].charAt(temp[0].length() - 1) == ':') {
                                        temp = addReactionSymbol(temp);
                                        // CHECK TEMP HERE
                                        temp = filterReaction(temp, file);
                                        temp = checkMessage(temp, file);
                                        message.addAll(Arrays.asList(temp));
                                    }
                                    else if (isNameInLine(temp, file)) {
                                        message.add("LINE CENSORED WITH MENTION TO -> " + getNamesInLine(temp, file));
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
                count++;
                if (count < files.size()) {
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("---------------- NEW DISCUSSION ----------------");
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("\n");
                }
                else {
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("---------------- END ----------------");
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("\n");
                    processText.append("\n");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Could not find, open or use chosen file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        java.io.File file_out = new java.io.File(outputPath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_out))) {
            writer.write(processText.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] checkUsername(String[] tokens, File file) {

        ArrayList<String> copy = new ArrayList<>(Arrays.asList(tokens));
        if (tokens.length > 1) {
            if (tokens[0].equals("Private")) {
                for (int i = 1; i < tokens.length; i++) {
                    if (tokens[i].equals("between")) {
                        for (int j = i+1; j < tokens.length; j++) {
                            if (!file.getBlackListUsername().contains(tokens[j])) {
                                copy.set(j, "CENSORED NAME,");
                            }
                        }
                    }
                }
            }
            else if (tokens[0].equals("Created:") || tokens[0].equals("Purpose:") || tokens[0].equals("Topic:") || tokens[0].equals("Discipline")) {
                for (int i = 1; i < tokens.length; i++) {
                    if (tokens[i].equals("by")) {
                        for (int j = i+1; j < tokens.length; j++) {
                            if (!file.getBlackListUsername().contains(tokens[j])) {
                                copy.set(j, "CENSORED NAME,");
                            }
                        }
                    }
                }
            }
        }
        return copy.toArray(new String[0]);
    }

    // Method that goes through the message and check if we need to censure
    public String[] checkMessage(String[] tokens, File file){

        ArrayList<String> copy = new ArrayList<>(Arrays.asList(tokens));
        for (String token : tokens){
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

    public boolean isNameInLine(String[] tokens, File file) {

        for (String token : tokens) {
            if (token.length() > 1) {
                if (token.charAt(0) == '@') {

                    StringBuilder temp = new StringBuilder();

                    for (int i = 1; i < token.length(); i++) {
                        temp.append(token.charAt(i));
                    }

                    String username = temp.toString();

                    if (file.getBlackListUsername().contains(username)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getNamesInLine(String[] tokens, File file) {

        StringBuilder out = new StringBuilder();
        for (String token : tokens) {
            if (token.length() > 1) {
                if (token.charAt(0) == '@') {

                    StringBuilder temp = new StringBuilder();

                    for (int i = 1; i < token.length(); i++) {
                        temp.append(token.charAt(i));
                    }

                    String username = temp.toString();

                    if (file.getBlackListUsername().contains(username)) {
                        out.append(token);
                        out.append(", ");
                    }
                }
            }
        }
        return out.toString();
    }

    public String getUsername(String[] tokens){

        String username = " ";

        int index = 2;
        if (tokens.length > 2 && tokens[index].length() > 2) {
            if (tokens[2].equals("(pinned_item)") || tokens[2].equals("(unpinned_item)")) {
                index = 3;
            }
        }
        if (tokens.length > 2 && tokens[index].length() > 2) {
            //First case: We see the <> for the username
            if (tokens[2].charAt(0) == '<' && tokens[index].charAt(tokens[index].length() - 1) == '>') {

                StringBuilder temp = new StringBuilder();

                for (int i = 1; i < tokens[index].length() - 1; i++) {
                    temp.append(tokens[index].charAt(i));
                }
                username = temp.toString();
            }
            // Second case: We see the @
            else if (tokens[index].charAt(0) == '@') {

                StringBuilder temp = new StringBuilder();

                for (int i = 1; i < tokens[index].length(); i++) {
                    temp.append(tokens[index].charAt(i));
                }

                username = temp.toString();
            } else {
                username = tokens[index];
            }
        }
        return username;
    }

    public String[] filterReaction(String[] tokens, File file){

        ArrayList<String> copy = new ArrayList<>(Arrays.asList(tokens));
        ArrayList<String> out = new ArrayList<>();

        // Check if we have a reaction
        if(tokens[0].equals("THIS_IS_A_REACTION")) {
            int counter = 0;
            // Check the emoji and the person that reacted, We start 2 index after the token to get the first name
            for (int i = 2; i < tokens.length; i++) {
                // Check if the person that reacted is in the blacklist
                if (tokens[i].length() > 0) {
                    if (tokens[i].charAt(tokens[i].length() - 1) == ',') {
                        if (file.getBlackListUsername().contains(sliceRange(tokens[i], 0, tokens[i].length() - 1))) {
                            counter++;
                        } else {
                            copy.set(copy.indexOf(tokens[i]), "CENSORED NAME,");
                        }
                    } else {
                        if (file.getBlackListUsername().contains(tokens[i])) {
                            counter++;
                        } else {
                            copy.set(copy.indexOf(tokens[i]), "CENSORED NAME,");
                        }
                    }
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

    public String sliceRange(String s, int startIndex, int endIndex) {

        if (startIndex < 0) {
            startIndex = s.length() + startIndex;
        }
        if (endIndex < 0) {
            endIndex = s.length() + endIndex;
        }

        return s.substring(startIndex, endIndex);
    }

    public String getOutputPath() {
        return outputPath;
    }

    public ArrayList<File> getFiles() {
        return files;
    }
}
