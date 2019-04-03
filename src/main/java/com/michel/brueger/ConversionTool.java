package com.michel.brueger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class ConversionTool {

    public void readGraphFile(String filename) throws IOException {
//        String filesFolder = "/Users/michelbrueger/IdeaProjects/gkaPraktikum1/src/main/files/";
        String filesFolder = "src/main/files/";
        FileReader fileReader = new FileReader(filesFolder+filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Graph resultGraph = new Graph();

        String line = bufferedReader.readLine();
// /Users/michelbrueger/IdeaProjects/gkaPraktikum1/src/main/java/com/michel/brueger/ConversionTool.java
        while (line != null) {
            System.out.println(line);                                                                      // DebugSout
            if (line.equals("#directed")) resultGraph.setDirected();
            String[] segs = line.split(Pattern.quote(","));
//            String[] segs = line.split("(\\s|\\p{Punct})+");
            System.out.println("Arraylaenge = "+segs.length);
            for(String string:segs){
                System.out.println(string);
            }
            System.out.println("---------------------------");
            line = bufferedReader.readLine();
        }

    }





    public void drawGraphFile(){

    }

}
