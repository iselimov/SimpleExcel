package com.defrag.lexer;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CellReferenceTest {

    @Test(expected = LexerException.class)
    public void testForRowOutOfBoundIndex() {
        CellReference ref = new CellReference(0, 'A', 2, 2);
        ref.addSymbol('3');
    }

    @Test(expected = LexerException.class)
    public void testForColOutOfBoundIndex() {
        CellReference ref = new CellReference(0, 'C', 2, 2);
        ref.addSymbol('1');
    }

    @Test
    public void testForCorrectCellIndexG2() {
        CellReference ref = new CellReference(0, 'G', 2, 7);
        ref.addSymbol('2');
        // rowIndex * colSize + colIndex
        assertThat(ref.getValue()).isEqualTo(1 * 7 + 6);
    }

    @Test
    public void testForCorrectCellIndexB8() {
        CellReference ref = new CellReference(0, 'B', 8, 7);
        ref.addSymbol('8');
        // rowIndex * colSize + colIndex
        assertThat(ref.getValue()).isEqualTo(7 * 7 + 1);
    }

    @Test(expected = LexerException.class)
    public void testForNotSetRowIndexValue() {
        new CellReference(0, 'A', 8, 7).getValue();
    }

    @Test(expected = LexerException.class)
    public void testForSetZeroRowIndex() {
        CellReference ref = new CellReference(0, 'A', 1, 7);
        ref.addSymbol('0');
    }

    @Test(expected = LexerException.class)
    public void testForWrongColName() {
        CellReference ref = new CellReference(0, '[', 1, 7);
        ref.addSymbol('1');
    }
}