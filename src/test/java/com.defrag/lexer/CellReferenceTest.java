package com.defrag.lexer;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CellReferenceTest {

    @Test(expected = LexerException.class)
    public void test1() {
        CellReference ref = new CellReference(tokenizeCell.getIndex(), 'A', 2, 2);
        ref.addSymbol('3');
    }

    @Test(expected = LexerException.class)
    public void test2() {
        CellReference ref = new CellReference(tokenizeCell.getIndex(), 'C', 2, 2);
        ref.addSymbol('1');
    }

    @Test
    public void test3() {
        CellReference ref = new CellReference(tokenizeCell.getIndex(), 'G', 2, 7);
        ref.addSymbol('2');
        // rowIndex * colSize + colIndex
        assertThat(ref.getValue()).isEqualTo(1 * 7 + 6);
    }

    @Test
    public void test4() {
        CellReference ref = new CellReference(tokenizeCell.getIndex(), 'B', 8, 7);
        ref.addSymbol('8');
        // rowIndex * colSize + colIndex
        assertThat(ref.getValue()).isEqualTo(7 * 7 + 1);
    }

    @Test(expected = LexerException.class)
    public void test5() {
        new CellReference(tokenizeCell.getIndex(), 'A', 8, 7).getValue();
    }

    @Test(expected = LexerException.class)
    public void test6() {
        CellReference ref = new CellReference(tokenizeCell.getIndex(), 'A', 1, 7);
        ref.addSymbol('0');
    }

    @Test(expected = LexerException.class)
    public void test7() {
        CellReference ref = new CellReference(tokenizeCell.getIndex(), '[', 1, 7);
        ref.addSymbol('1');
    }
}