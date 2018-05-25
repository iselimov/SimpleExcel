package com.defrag.lexer;

import com.defrag.Cell;
import com.defrag.Context;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.defrag.lexer.LexerException.Error.CELL_REF_FORMAT;
import static com.defrag.lexer.LexerException.Error.LITERAL_FORMAT;
import static com.defrag.lexer.LexerException.Error.UNKNOWN_FORMAT;
import static com.defrag.lexer.Operation.Type.DIVIDE;
import static com.defrag.lexer.Operation.Type.MULTIPLY;
import static com.defrag.lexer.Operation.Type.PLUS;
import static com.defrag.lexer.Operation.Type.SUBTRACT;

@RequiredArgsConstructor
public class Lexer {

    private final Context context;

    public Optional<Token> next() {
        Cell tokenizeCell = context.getCurrentCell();
        Token result = null;
        while (tokenizeCell.hasNext()) {
            char nextSymbol = tokenizeCell.next();
            if (nextSymbol == '\'') {
                if (tokenizeCell.isFoundEqualsSign()) {
                    throw new LexerException(LITERAL_FORMAT);
                }
                if (result == null) {
                    result = new Literal();
                } else if (result.getType() != Token.Type.LITERAL) {
                    throw new LexerException(LITERAL_FORMAT);
                } else {
                    result.addSymbol(nextSymbol);
                }
            } else if (nextSymbol == '=') {
                if (tokenizeCell.isFoundEqualsSign()) {
                    throw new LexerException(UNKNOWN_FORMAT);
                }
                tokenizeCell.setFoundEqualsSign(true);
            } else if (Character.isDigit(nextSymbol)) {
                if (result == null) {
                    result = new Digit(nextSymbol);
                } else {
                    result.addSymbol(nextSymbol);
                }
            } else if (Character.isLetter(nextSymbol)) {
                if (result != null) {
                    result.addSymbol(nextSymbol);
                } else if (!tokenizeCell.isFoundEqualsSign()) {
                    throw new LexerException(CELL_REF_FORMAT);
                } else {
                    result = new CellReference(nextSymbol);
                }
            } else if (PLUS.isSuitable(nextSymbol)) {
                if (!tokenizeCell.isFoundEqualsSign()) {
                    throw new LexerException(CELL_REF_FORMAT);
                }
                tokenizeCell.setFoundOperator(true);
                if (result != null) {
                    tokenizeCell.prev();
                    break;
                }
                return Optional.of(new Operation(PLUS));
            } else if (SUBTRACT.isSuitable(nextSymbol)) {
                if (!tokenizeCell.isFoundEqualsSign()) {
                    throw new LexerException(CELL_REF_FORMAT);
                }
                tokenizeCell.setFoundOperator(true);
                if (result != null) {
                    tokenizeCell.prev();
                    break;
                }
                return Optional.of(new Operation(SUBTRACT));
            } else if (MULTIPLY.isSuitable(nextSymbol)) {
                if (!tokenizeCell.isFoundEqualsSign()) {
                    throw new LexerException(CELL_REF_FORMAT);
                }
                tokenizeCell.setFoundOperator(true);
                if (result != null) {
                    tokenizeCell.prev();
                    break;
                }
                return Optional.of(new Operation(MULTIPLY));
            } else if (DIVIDE.isSuitable(nextSymbol)) {
                if (!tokenizeCell.isFoundEqualsSign()) {
                    throw new LexerException(CELL_REF_FORMAT);
                }
                tokenizeCell.setFoundOperator(true);
                if (result != null) {
                    tokenizeCell.prev();
                    break;
                }
                return Optional.of(new Operation(DIVIDE));
            }
        }
        boolean isDigit = result != null
                && result.getType() == Token.Type.DIGIT;
        if (isDigit
                && tokenizeCell.isFoundEqualsSign()
                && !tokenizeCell.isFoundOperator()) {
            throw new LexerException(CELL_REF_FORMAT);
        }
        return Optional.ofNullable(result);
    }
}