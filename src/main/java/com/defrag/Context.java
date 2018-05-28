package com.defrag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class Context {

    private final Cell[] inputData;
    @Getter private final int rowsCount;
    @Getter private final int colsCount;
    private int currIndex;

    public Cell getCurrentCell() {
        return inputData[currIndex];
    }

    public Cell getCell(int currIndex) {
        return inputData[currIndex];
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
            if (!cell.isHandled() && !cell.isWithErrors()) {
                return Optional.of(cell);
            }
        }
        return Optional.empty();
    }
}