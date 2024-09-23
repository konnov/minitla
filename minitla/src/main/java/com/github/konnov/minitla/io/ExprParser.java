package com.github.konnov.minitla.io;

import com.github.konnov.minitla.ir.BoolValueExpr;
import com.github.konnov.minitla.ir.Expr;
import com.github.konnov.minitla.ir.NameExpr;
import com.github.konnov.minitla.ir.OperatorExpr;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.*;

import static com.github.konnov.minitla.MiniTLA.syntaxError;

/**
 * A simple parser of <a href="https://en.wikipedia.org/wiki/S-expression">S-expressions</a>
 * that are supported by our fragment of MiniTLA.
 */
public class ExprParser {
    // Supported operators and directives.
    public static final HashSet<String> OPERATORS = new HashSet<>(Arrays.asList(
            "and", "or", "not", "implies", "iff",
            ":const", ":set-const"
    ));

    private final String sourceName;
    private final StreamTokenizer tokenizer;
    // The number of open parentheses, which is the main syntax feature of S-expressions.
    // Invariant: nOpenParentheses >= 0.
    private int nOpenParentheses = 0;
    // An internal stack of operator names.
    // Invariant: operatorStack.size() <= nOpenParentheses.
    private Stack<String> operatorStack;
    // An internal stack of operands.
    // Invariant: operandStack.size() == operatorStack.size() + 1.
    private Stack<ArrayList<Expr>> operandStack;

    /**
     * Creates a new reader. It is the caller's responsibility to close the reader.
     * @param sourceName the source, typically, the filename
     * @param reader the input reader that is going to be consumed by the parser until EOF is reached
     *               or a parser error is found
     */
    public ExprParser(String sourceName, Reader reader) {
        this.sourceName = sourceName; this.tokenizer = new StreamTokenizer(reader);
        tokenizer.wordChars(':', ':');
        tokenizer.wordChars('-', '-');
    }

    /**
     * Read a stream till the end and produce an array of expressions, if possible.
     * @return the list of expressions that are parsed from the reader
     * @throws SyntaxError if there is an error
     */
    public ArrayList<Expr> parse() {
        operatorStack = new Stack<>();
        operandStack = new Stack<>();
        // we collect the parsed expressions at the very bottom of the stack
        operandStack.push(new ArrayList<>());

        try {
            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                switch (tokenizer.ttype) {
                    case StreamTokenizer.TT_WORD:
                        if (!parseBoolLiteral(tokenizer.sval)) {
                            // treat the token as a name
                            operandStack.peek().add(new NameExpr(tokenizer.sval));
                        }
                        break;

                    case '(':
                        nOpenParentheses++;
                        parseOperatorHead();
                        break;

                    case ')':
                        if (nOpenParentheses == 0) {
                            syntaxError(sourceName, tokenizer.lineno(), "Too many closing parentheses ')'");
                        }
                        if (operatorStack.isEmpty()) {
                            // this may happen when one writes, e.g., (false) or (true)
                            syntaxError(sourceName, tokenizer.lineno(), "No operator to end with ')'");
                        }
                        pushExpr(new OperatorExpr(operatorStack.pop(), operandStack.pop().toArray(Expr[]::new)));
                        nOpenParentheses--;
                        break;

                    default:
                        syntaxError(sourceName, tokenizer.lineno(), "Unexpected token: " + tokenizer.ttype);
                }
            }
        } catch (IOException e) {
            syntaxError(sourceName, tokenizer.lineno(), "I/O error: " + e.getMessage());
        }

        if (nOpenParentheses > 0) {
            syntaxError(sourceName, tokenizer.lineno(), String.format("%d unclosed parentheses remain", nOpenParentheses));
        }
        assert(operatorStack.isEmpty() && operandStack.size() == 1);

        return operandStack.pop();
    }

    /**
     * Push a parsed expression to the operands on the top of the stack.
     * @param expr the expression to push
     */
    private void pushExpr(Expr expr) {
        operandStack.peek().add(expr);
    }

    private void parseOperatorHead() throws IOException {
        tokenizer.nextToken();
        if (tokenizer.ttype != StreamTokenizer.TT_WORD) {
            syntaxError(sourceName, tokenizer.lineno(), "Expected an operator/directive name, found: " + tokenizer.ttype);
        }
        var identifier = tokenizer.sval;
        if (!OPERATORS.contains(identifier) || nOpenParentheses <= operatorStack.size()) {
            syntaxError(sourceName, tokenizer.lineno(), "Unexpected operator/directive: " + identifier);
        }
        // start building a new operator expression on the stack
        operatorStack.push(identifier);
        operandStack.push(new ArrayList<>());
    }

    /**
     * Try to parse a token as a Boolean literal. On success, push the expression to the stack.
     * @param token the token to parse
     * @return true if the token was parsed as a Boolean literal, false otherwise
     */
    private boolean parseBoolLiteral(String token) {
        if (Objects.equals(token, "false")) {
            pushExpr(new BoolValueExpr(false));
            return true;
        } else if (Objects.equals(token, "true")) {
            pushExpr(new BoolValueExpr(true));
            return true;
        } else {
            return false;
        }
    }
}
