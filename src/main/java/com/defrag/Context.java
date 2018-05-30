package com.defrag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Context for both lexer and parser
 */
@RequiredArgsConstructor
public class Context {

    private final Cell[] inputData;
    @Getter private final int rowsCount;
    @Getter private final int colsCount;
    private int currIndex;

    public Cell getCurrentCell() {
        return inputData[currIndex];
    }

    /**
     * @param index index of cell to need
     * @return need cell by index
     */
    public Cell getCell(int index) {
        return inputData[index];
    }

    /**
     * @param index some index that is changing current index of context
     */
    public void jumpToCell(int index) {
        currIndex = index;
    }

    /**
     * @return cell if it was not handled and has not errors
     */
    public Optional<Cell> nextUnprocessed() {
        for (Cell cell : inputData) {
            if (!cell.isHandled() && !cell.isWithErrors()) {
                return Optional.of(cell);
            }
        }
        return Optional.empty();
    }
}