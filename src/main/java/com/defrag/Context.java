package com.defrag;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class Context {

    private final Cell[] inputData;
    private final int rowsCount;
    private final int colsCount;
    private int currIndex;

    public Cell getCurrentCell() {
        return inputData[currIndex];
    }

    public boolean shiftCell() {
        if (currIndex == inputData.length - 1) {
            return false;
        }
        currIndex++;
        return true;
    }

    public boolean jumpToCell(int index) {
        if (index >= inputData.length) {
            return false;
        }
        currIndex = index;
        return true;
    }

    public Optional<Cell> nextUnprocessed() {
        for (Cell cell : inputData) {
            if (!cell.isInProcessing()) {
                return Optional.of(cell);
            }
        }
        return Optional.empty();
    }
}