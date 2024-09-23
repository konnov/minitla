package com.github.konnov.minitla.io;

/**
 * An exception thrown when the input does not match the syntax of S-expressions.
 *
 * @author Igor Konnov, 2024
 */
public class SyntaxError extends RuntimeException {
    public SyntaxError(String source, int line, String message) {
        super(String.format("%s:%d: %s", source, line, message));
    }
}
