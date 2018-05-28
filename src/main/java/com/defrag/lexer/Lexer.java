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
                result = handleQuoteSymbol(tokenizeCell, result, nextSymbol);
            } else if (nextSymbol == '=') {
                handleEqualSymbol(tokenizeCell);
            } else if (Character.isDigit(nextSymbol)) {
                result = handleDigitSymbol(tokenizeCell, result, nextSymbol);
            } else if (Character.isLetter(nextSymbol)) {
                result = handleLetterSymbol(tokenizeCell, result, nextSymbol);
            } else if (PLUS.isSuitable(nextSymbol)) {
                if (handledOperatorSymbol(tokenizeCell, result)){
                    break;
                }
                return Optional.of(new Operation(PLUS));
            } else if (SUBTRACT.isSuitable(nextSymbol)) {
                if (handledOperatorSymbol(tokenizeCell, result)){
                    break;
                }
                return Optional.of(new Operation(SUBTRACT));
            } else if (MULTIPLY.isSuitable(nextSymbol)) {
                if (handledOperatorSymbol(tokenizeCell, result)){
                    break;
                }
                return Optional.of(new Operation(MULTIPLY));
            } else if (DIVIDE.isSuitable(nextSymbol)) {
                if (handledOperatorSymbol(tokenizeCell, result)){
                    break;
                }
                return Optional.of(new Operation(DIVIDE));
            }
        }
        checkDigitConditions(tokenizeCell, result);
        return Optional.ofNullable(result);
    }

    private boolean handledOperatorSymbol(Cell tokenizeCell, Token result) {
        if (!tokenizeCell.isFoundEqualsSign()) {
            throw new LexerException(CELL_REF_FORMAT, tokenizeCell.getIndex());
        }
        tokenizeCell.setFoundOperator(true);
        if (result != null) {
            tokenizeCell.prev();
            return true;
        }
        return false;
    }

    private Token handleQuoteSymbol(Cell tokenizeCell, Token result, char nextSymbol) {
        if (tokenizeCell.isFoundEqualsSign()) {
            throw new LexerException(LITERAL_FORMAT, tokenizeCell.getIndex());
        }
        if (result == null) {
            result = new Literal();
        } else if (result.getType() != Token.Type.LITERAL) {
            throw new LexerException(LITERAL_FORMAT, tokenizeCell.getIndex());
        } else {
            result.addSymbol(nextSymbol);
        }
        return result;
    }

    private void handleEqualSymbol(Cell tokenizeCell) {
        if (tokenizeCell.isFoundEqualsSign()) {
            throw new LexerException(UNKNOWN_FORMAT, tokenizeCell.getIndex());
        }
        tokenizeCell.setFoundEqualsSign(true);
    }

    private Token handleDigitSymbol(Cell tokenizeCell, Token result, char nextSymbol) {
        if (result == null) {
            result = new Digit(tokenizeCell.getIndex(), nextSymbol);
        } else {
            result.addSymbol(nextSymbol);
        }
        return result;
    }

    private Token handleLetterSymbol(Cell tokenizeCell, Token result, char nextSymbol) {
        if (result != null) {
            result.addSymbol(nextSymbol);
        } else if (!tokenizeCell.isFoundEqualsSign()) {
            throw new LexerException(CELL_REF_FORMAT, tokenizeCell.getIndex());
        } else {
            result = new CellReference(tokenizeCell.getIndex(), nextSymbol, context.getRowsCount(), context.getColsCount());
        }
        return result;
    }

    private void checkDigitConditions(Cell tokenizeCell, Token result) {
        boolean isDigit = result != null
                && result.getType() == Token.Type.DIGIT;
        if (isDigit && tokenizeCell.isFoundEqualsSign() && !tokenizeCell.isFoundOperator()) {
            throw new LexerException(CELL_REF_FORMAT, tokenizeCell.getIndex());
        }
    }
}