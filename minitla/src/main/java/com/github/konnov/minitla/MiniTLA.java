package com.github.konnov.minitla;

import com.github.konnov.minitla.io.SyntaxError;

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
}
