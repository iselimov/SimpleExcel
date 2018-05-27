package com.defrag;

import lombok.Getter;
import lombok.Setter;

public class Cell {

    @Getter
    private final int index;
    private final String input;
    private int pointerPos;
    @Getter
    private String output;
    @Getter
    private boolean inProcessing;
    @Getter
    private boolean handled;
    @Getter
    @Setter
    private boolean foundEqualsSign;
    @Getter
    @Setter
    private boolean foundOperator;

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

    public void setOutput(String output) {
        this.output = output;
        handled = true;
    }

    public void markAsProcessing() {
        inProcessing = true;
    }

    public void unmarkAsProcessing() {
        inProcessing = false;
    }
}