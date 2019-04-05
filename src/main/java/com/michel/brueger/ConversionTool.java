package com.michel.brueger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class ConversionTool {

    /*
Regex-Metacharacters sind ( ) { { \ ^ $ | ? * + . < > - = !
                          <([{\^-=$!|]})?*+.>
The metacharacter "." means "any character"

There are two ways to force a metacharacter to be treated as an ordinary character:
precede the metacharacter with a backslash, or
enclose it within \Q (which starts the quote) and \E (which ends it).
When using this technique, the \Q and \E can be placed at any location within the expression, provided that the \Q comes first.


Construct	    Description
[abc]	        a, b, or c (simple class)
[^abc]	        Any character except a, b, or c (negation)
[a-zA-Z]	    a through z, or A through Z, inclusive (range)
[a-d[m-p]]	    a through d, or m through p: [a-dm-p] (union)
[a-z&&[def]]	d, e, or f (intersection)
[a-z&&[^bc]]	a through z, except for b and c: [ad-z] (subtraction)
[a-z&&[^m-p]]	a through z, and not m through p: [a-lq-z] (subtraction)

     */

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
