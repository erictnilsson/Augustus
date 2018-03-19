package com.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String mainFilepath = "";
        String mergingFilepath = "";

        // make sure that the filepath is a .txt-file, prompting the user to enter the filepath as a .txt-file
        while (!mainFilepath.endsWith(".txt")) {
            System.out.println("Enter the filepath of the main dataset.");
            mainFilepath = scanner.nextLine();

            if (!mainFilepath.endsWith(".txt")) {
                System.out.println("File must be a \".txt\"-file.");
            }
        }

        while (!mergingFilepath.endsWith(".txt")) {
            System.out.println("Enter the filepath of the merging dataset.");
            mergingFilepath = scanner.nextLine();

            if (!mergingFilepath.endsWith(".txt")) {
                System.out.println("File must be a \".txt\"-file.");
            }
        }

        ArrayList<String[]> content = ParseFile(mainFilepath);
        ArrayList<String[]> mergeData = ParseFile(mergingFilepath);

        BuildFile(mergeData, content);
    }

    private static void BuildFile(ArrayList<String[]> mergeData, ArrayList<String[]> content) {
        // make sure that both the mergeData and content is valid and not empty
        if (mergeData != null || !mergeData.isEmpty() || content != null || !content.isEmpty()) {
            HashMap<String, String> data = new HashMap<>();

            for(int i = 0; i < mergeData.size(); i++) {
                String keys = mergeData.get(i)[0] + " " + mergeData.get(i)[1];
                if(!data.containsKey(keys)) {
                    if (mergeData.get(i).length == 2 || mergeData.get(i)[2].equals(" ")) {
                        data.put(keys, "");
                    } else {
                        data.put(keys, mergeData.get(i)[2]);
                    }
                }
            }

            ArrayList<String> output = new ArrayList<>();

            for(int i = 0; i < content.size(); i++) {
                String line = "";
                // mother

                String mKeys = content.get(i)[1] + " " + " ";

                if (content.get(i).length >= 2) {
                    mKeys = content.get(i)[1] + " " + content.get(i)[2];
                }
                // father
                String fKeys = content.get(i)[1] + " " + "";

                    if (content.get(i).length >= 3) {
                        fKeys = content.get(i)[1] + " " + content.get(i)[3];
                    }


                if (data.containsKey(mKeys))
                    content.get(i)[4] = data.get(mKeys);

                if(data.containsKey(fKeys))
                    content.get(i)[5] = data.get(fKeys);

                output.add(String.join("\t", content.get(i)));
            }

            CreateFile(output);
        }
    }

    private static void CreateFile(ArrayList<String> content){
        if (content != null || !content.isEmpty()) {
            try {
                Files.write(Paths.get("/Users/EricNilsson/Desktop/Finito.txt"),content, Charset.forName("UTF-8"));
            } catch (IOException e) {
                System.out.println("Ooops... Something went wrong: " + e.getMessage());
            }

        }
    }

    private static HashMap<String, String> GetIndexes(String[] headers) {
        HashMap<String, String> indexes = new HashMap<String, String>();

        // make sure that the headers is valid and not empty
        if (headers != null || headers.length != 0) {
            for (int i = 0; i < headers.length; i++) {
                indexes.put(headers[i], Integer.toString(i));
            }
        }

        return indexes;
    }

    private static ArrayList<String[]> ParseFile(String filepath) {
        ArrayList<String[]> content = new ArrayList<String[]>();

        File file = new File(filepath);
        // make sure that the file exists and isn't a directory
        if (file.exists() && !file.isDirectory()) {
            content = GetRows(readFile(file));
        }
        return content;
    }

    private static ArrayList<String[]> GetRows(ArrayList<String> text) {
        ArrayList<String[]> content = new ArrayList<String[]>();

        // make sure that the text-array is valid on not empty
        if (text != null || !text.isEmpty())
            for(String row : text)
                content.add(row.split("\t"));

        return content;
    }

    private static ArrayList<String> readFile(File file) {
        ArrayList<String> lines = new ArrayList<String>();

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
        }  catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        return lines;
    }
}
