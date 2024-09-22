package com.github.konnov.minitla;

import com.github.konnov.minitla.io.ExprParser;
import com.github.konnov.minitla.io.SyntaxError;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2 || !args[0].equals("parse")) {
            System.err.println("Use: <program> parse input");
            System.exit(100);
        } else {
            parse(args[1]);
        }
    }

    private static void parse(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            new ExprParser(filename, reader).parse();
            // at this point, we are not doing anything with the parsed expressions
        } catch (SyntaxError error) {
            System.err.println("Syntax error: " + error.getMessage());
            System.exit(101);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            System.exit(102);
        }
    }
}