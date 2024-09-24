package com.github.konnov.minitla.ir;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An operator supported by MiniTLA.
 */
public enum Operator {
    AND, OR, NOT, IMPLIES, IFF,
    CONST, SET_CONST;

    /**
     * Parses an operator from its name, if possible.
     * @param name the name to parse
     * @return the operator, if found; otherwise, an empty optional
     */
    public static Optional<Operator> parse(String name) {
        var value = nameToOp.get(name);
        if (value != null) {
            return Optional.of(value);
        } else {
            return Optional.empty();
        }
    }

    private static final Map<String, Operator> nameToOp = new HashMap<>();
    static {
        nameToOp.put("and", AND);
        nameToOp.put("or", OR);
        nameToOp.put("not", NOT);
        nameToOp.put("implies", IMPLIES);
        nameToOp.put("iff", IFF);
        nameToOp.put(":const", CONST);
        nameToOp.put(":set-const", SET_CONST);
    }
}
