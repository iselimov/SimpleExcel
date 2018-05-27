package com.defrag.lexer;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CellReferenceTest {

    @Test(expected = LexerException.class)
    public void test1() {
        CellReference ref = new CellReference('A', 1, 7);
        ref.addSymbol('3');
    }

    @Test(expected = LexerException.class)
    public void test2() {
        CellReference ref = new CellReference('G', 1, 5);
        ref.addSymbol('1');
    }

    @Test
    public void test3() {
        CellReference ref = new CellReference('G', 1, 7);
        ref.addSymbol('2');
        // rowIndex * colSize + colIndex
        assertThat(ref.getValue()).isEqualTo(1 * 7 + 6);
    }

    @Test
    public void test4() {
        CellReference ref = new CellReference('B', 8, 7);
        ref.addSymbol('9');
        // rowIndex * colSize + colIndex
        assertThat(ref.getValue()).isEqualTo(8 * 7 + 1);
    }

    @Test(expected = LexerException.class)
    public void test5() {
        new CellReference('A', 8, 7).getValue();
    }

    @Test(expected = LexerException.class)
    public void test6() {
        CellReference ref = new CellReference('A', 1, 7);
        ref.addSymbol('0');
    }

    @Test(expected = LexerException.class)
    public void test7() {
        CellReference ref = new CellReference('[', 1, 7);
        ref.addSymbol('1');
    }
}