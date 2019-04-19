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


Construct	    Description
.	            Any character (may or may not match line terminators)
\d	            A digit: [0-9]
\D	            A non-digit: [^0-9]
\s	            A whitespace character: [ \t\n\x0B\f\r]
\S	            A non-whitespace character: [^\s]
\w	            A word character: [a-zA-Z_0-9]
\W	            A non-word character: [^\w]
private final String REGEX = "\\d"; // a single digit


Greedy	Reluctant	Possessive	Meaning
X?	    X??	        X?+	        X, once or not at all
X*	    X*?	        X*+	        X, zero or more times
X+	    X+?	        X++	        X, one or more times
X{n}	X{n}?	    X{n}+	    X, exactly n times
X{n,}	X{n,}?	    X{n,}+	    X, at least n times
X{n,m}	X{n,m}?	    X{n,m}+	    X, at least n but not more than m times

    Enter your regex: a?
    Enter input string to search: aaaaa
    I found the text "a" starting at index 0 and ending at index 1.
    I found the text "a" starting at index 1 and ending at index 2.
    I found the text "a" starting at index 2 and ending at index 3.
    I found the text "a" starting at index 3 and ending at index 4.
    I found the text "a" starting at index 4 and ending at index 5.
    I found the text "" starting at index 5 and ending at index 5.

    Enter your regex: a*
    Enter input string to search: aaaaa
    I found the text "aaaaa" starting at index 0 and ending at index 5.
    I found the text "" starting at index 5 and ending at index 5.

    Enter your regex: a+
    Enter input string to search: aaaaa
    I found the text "aaaaa" starting at index 0 and ending at index 5.

    Enter your regex: .*foo  // greedy quantifier
    Enter input string to search: xfooxxxxxxfoo
    I found the text "xfooxxxxxxfoo" starting at index 0 and ending at index 13.

    Enter your regex: .*?foo  // reluctant quantifier
    Enter input string to search: xfooxxxxxxfoo
    I found the text "xfoo" starting at index 0 and ending at index 4.
    I found the text "xxxxxxfoo" starting at index 4 and ending at index 13.

    Enter your regex: .*+foo // possessive quantifier
    Enter input string to search: xfooxxxxxxfoo
    No match found.

    public int start(int group): Returns the start index of the subsequence captured by the given group during the previous match operation.
    public int end (int group): Returns the index of the last character, plus one, of the subsequence captured by the given group during the previous match operation.
    public String group (int group): Returns the input subsequence captured by the given group during the previous match operation.

    To match any 2 digits, followed by the exact same two digits, you would use (\d\d)\1 as the regular expression:

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
