package com.defrag;

import lombok.Getter;
import lombok.Setter;

/**
 * Central entity of parsing
 */
public class Cell {

    @Getter private final int index;
    @Getter private final String input;
    private int pointerPos;
    @Getter private Object output;
    /**
     * Sign that help to find a cycles
     */
    @Getter private boolean inProcessing;
    @Getter private boolean handled;
    @Getter private boolean withErrors;
    @Getter @Setter private boolean foundEqualsSign;
    @Getter @Setter private boolean foundOperator;

    public Cell(int index, String input) {
        this.index = index;
        this.input = input.replaceAll("\\s+", "");
    }

    public boolean hasNext() {
        return pointerPos < input.length();
    }

    public Character next() {
        if (!hasNext()) {
            throw new IllegalStateException("Next symbol not found");
        }
        return input.charAt(pointerPos++);
    }

    public boolean hasPrevious() {
        return pointerPos > 0;
    }

    public Character prev() {
        if (!hasPrevious()) {
            throw new IllegalStateException("Prev symbol not found");
        }
        return input.charAt(--pointerPos);
    }

    /**
     * Method which set lexer info to default
     * It may need, for example, after processing this cell by lexer
     */
    public void refreshLexerInfo() {
        pointerPos = 0;
        foundEqualsSign = false;
        foundOperator = false;
        handled = false;
        withErrors = false;
        output = null;
    }

    public void setOutput(Object output) {
        this.output = output;
        handled = true;
    }

    public void markAsProcessing() {
        inProcessing = true;
    }

    public void unmarkAsProcessing() {
        inProcessing = false;
    }

    public void markAsHasErrors() {
        withErrors = true;
    }
}