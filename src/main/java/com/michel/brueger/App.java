package com.michel.brueger;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        ConversionTool ct = new ConversionTool();
        ct.readGraphFile("graph04.graph");
    }
}
