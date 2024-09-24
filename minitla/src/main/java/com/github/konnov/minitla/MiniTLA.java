package com.github.konnov.minitla;

import com.github.konnov.minitla.io.ExprParser;
import com.github.konnov.minitla.io.SyntaxError;
import com.github.konnov.minitla.ir.Expr;

import java.io.Reader;
import java.util.ArrayList;

/**
 * A collection of functions that are used throughout the MiniTLA project.
 *
 * @author Igor Konnov, 2024
 */
public class MiniTLA {
    /**
     * Trigger a syntax error, which is propagated via an exception of type `SyntaxError`
     * @param sourceName the source, typically, the filename
     * @param line the line number
     * @param message the error message
     * @throws SyntaxError whenever this function is called
     */
    public static void syntaxError(String sourceName, int line, String message) throws SyntaxError {
        throw new SyntaxError(sourceName, line, message);
    }

    /**
     * Parse a sequence of expressions from a reader.
     * @param sourceName a human-readable name of the source, e.g., the filename
     * @param reader the input reader
     * @return a list of parsed expressions
     * @throws SyntaxError or IOException in case of a syntax or I/O error
     */
    public static ArrayList<Expr> parse(String sourceName, Reader reader) {
        return new ExprParser(sourceName, reader).parse();
    }
}
