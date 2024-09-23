package com.github.konnov.minitla.eval;

/**
 * An exception thrown when an error occurs during evaluation.
 * @author Igor Konnov, 2024
 */
public class EvalError extends RuntimeException {
    public EvalError(String message) {
        super(message);
    }
}
