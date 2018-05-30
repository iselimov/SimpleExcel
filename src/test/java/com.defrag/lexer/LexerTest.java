package com.defrag.lexer;

import com.defrag.Cell;
import com.defrag.Context;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LexerTest {

    @Test
    public void testForCellRefToken() {
        Lexer lexer = createLexer("=A2");
        CellReference ref = (CellReference) lexer.next().get();
        assertThat(ref.getColName()).isEqualTo('A');
        assertThat(ref.getRowIndex()).isEqualTo(1);
        assertThat(lexer.next()).isNotPresent();
    }

    @Test
    public void testForOneDigitToken() {
        Lexer lexer = createLexer("2");
        Digit digit = (Digit) lexer.next().get();
        assertThat(digit.getValue()).isEqualTo(2);
        assertThat(lexer.next()).isNotPresent();
    }

    @Test
    public void testForTwoDigitToken() {
        Lexer lexer = createLexer("23");
        Digit digit = (Digit) lexer.next().get();
        assertThat(digit.getValue()).isEqualTo(23);
        assertThat(lexer.next()).isNotPresent();
    }

    @Test(expected = LexerException.class)
    public void testForWrongRefToken() {
        createLexer("A2").next();
    }

    @Test
    public void testForLiteralToken() {
        Lexer lexer = createLexer("'g4sg");
        Literal str = (Literal) lexer.next().get();
        assertThat(str.getValue()).isEqualTo("g4sg");
        assertThat(lexer.next()).isNotPresent();
    }

    @Test(expected = LexerException.class)
    public void testForWrongLiteralToken() {
        createLexer("gfsdf").next();
    }

    @Test
    public void testForCellRefWithOperationToken() {
        Lexer lexer = createLexer(" =4 +2");
        Digit first = (Digit) lexer.next().get();
        assertThat(first.getValue()).isEqualTo(4);
        Operation second = (Operation) lexer.next().get();
        assertThat(second.getValue()).isEqualTo(Operation.Type.PLUS);
        Digit third = (Digit) lexer.next().get();
        assertThat(third.getValue()).isEqualTo(2);
        assertThat(lexer.next()).isNotPresent();
    }

    @Test
    public void testForCellRefWithComplexOperationToken() {
        Lexer lexer = createLexer(" =41 +2-5/33*  10");
        Digit first = (Digit) lexer.next().get();
        assertThat(first.getValue()).isEqualTo(41);
        Operation second = (Operation) lexer.next().get();
        assertThat(second.getValue()).isEqualTo(Operation.Type.PLUS);
        Digit third = (Digit) lexer.next().get();
        assertThat(third.getValue()).isEqualTo(2);
        Operation fourth = (Operation) lexer.next().get();
        assertThat(fourth.getValue()).isEqualTo(Operation.Type.SUBTRACT);
        Digit fifth = (Digit) lexer.next().get();
        assertThat(fifth.getValue()).isEqualTo(5);
        Operation sixth = (Operation) lexer.next().get();
        assertThat(sixth.getValue()).isEqualTo(Operation.Type.DIVIDE);
        Digit seventh = (Digit) lexer.next().get();
        assertThat(seventh.getValue()).isEqualTo(33);
        Operation eight = (Operation) lexer.next().get();
        assertThat(eight.getValue()).isEqualTo(Operation.Type.MULTIPLY);
        Digit ninth = (Digit) lexer.next().get();
        assertThat(ninth.getValue()).isEqualTo(10);
        assertThat(lexer.next()).isNotPresent();
    }

    @Test(expected = LexerException.class)
    public void testForWrongOperationToken() {
        createLexer("4+2").next();
    }

    @Test
    public void testForCellRefWithAnotherCellRefToken() {
        Lexer lexer = createLexer(" =4 +A5-B2/3");
        Digit first = (Digit) lexer.next().get();
        assertThat(first.getValue()).isEqualTo(4);
        Operation second = (Operation) lexer.next().get();
        assertThat(second.getValue()).isEqualTo(Operation.Type.PLUS);
        CellReference third = (CellReference) lexer.next().get();
        assertThat(third.getColName()).isEqualTo('A');
        assertThat(third.getRowIndex()).isEqualTo(4);
        Operation fourth = (Operation) lexer.next().get();
        assertThat(fourth.getValue()).isEqualTo(Operation.Type.SUBTRACT);
        CellReference fifth = (CellReference) lexer.next().get();
        assertThat(fifth.getColName()).isEqualTo('B');
        assertThat(fifth.getRowIndex()).isEqualTo(1);
        Operation sixth = (Operation) lexer.next().get();
        assertThat(sixth.getValue()).isEqualTo(Operation.Type.DIVIDE);
        Digit seventh = (Digit) lexer.next().get();
        assertThat(seventh.getValue()).isEqualTo(3);
        assertThat(lexer.next()).isNotPresent();
    }

    @Test(expected = LexerException.class)
    public void testForWrongCellRefToken() {
        createLexer(" =AA5").next().get();
    }

    @Test(expected = LexerException.class)
    public void testForWrongCellRefDigitToken() {
        createLexer(" =23").next().get();
    }

    @Test(expected = LexerException.class)
    public void testForWrongCellRefLiteralTokent() {
        createLexer(" ='fsdfdsfsd").next().get();
    }

    @Test(expected = LexerException.class)
    public void testForTooMuchEqualsSings() {
        createLexer("=A3=A2").next().get();
    }

    private Lexer createLexer(String input) {
        return new Lexer(
                new Context(
                        new Cell[] {new Cell(0, input)},
                        10, 10));
    }
}