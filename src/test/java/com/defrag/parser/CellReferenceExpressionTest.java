package com.defrag.parser;

import com.defrag.Cell;
import com.defrag.Context;
import com.defrag.lexer.Lexer;
import org.junit.Test;

import static com.defrag.parser.Expression.Type.*;
import static com.defrag.parser.OperationExpression.OperationType.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CellReferenceExpressionTest {

    @Test
    public void testPrepareWithDigitToken() {
        Cell cell = new Cell(0, "2");
        Context context = new Context(new Cell[] {cell}, 1, 1);
        Lexer lexer = new Lexer(context);
        CellReferenceExpression expression = new CellReferenceExpression(cell, lexer, context);
        expression.prepare();
        assertThat(expression.getCell()).isEqualTo(cell);
        assertThat(expression.getParent()).isNull();
        assertThat(expression.getChild().getType()).isEqualTo(CONSTANT);
        assertThat(expression.getChild().getValue()).isEqualTo(2);
        assertThat(expression.getChild().getParent()).isEqualTo(expression);
    }

    @Test
    public void testPrepareWithLiteralToken() {
        Cell cell = new Cell(0, "'Test");
        Context context = new Context(new Cell[] {cell}, 1, 1);
        Lexer lexer = new Lexer(context);
        CellReferenceExpression expression = new CellReferenceExpression(cell, lexer, context);
        expression.prepare();
        assertThat(expression.getCell()).isEqualTo(cell);
        assertThat(expression.getParent()).isNull();
        assertThat(expression.getChild().getType()).isEqualTo(LITERAL);
        assertThat(expression.getChild().getValue()).isEqualTo("Test");
        assertThat(expression.getChild().getParent()).isEqualTo(expression);
    }

    @Test
    public void testPrepareWithPlusOperationToken() {
        Cell cell = new Cell(0, "=2+3");
        Context context = new Context(new Cell[] {cell}, 1, 1);
        Lexer lexer = new Lexer(context);
        CellReferenceExpression expression = new CellReferenceExpression(cell, lexer, context);
        expression.prepare();
        assertThat(expression.getCell()).isEqualTo(cell);
        assertThat(expression.getParent()).isNull();
        assertThat(expression.getChild().getType()).isEqualTo(OPERATOR);
        OperationExpression operationExpr = (OperationExpression) expression.getChild();
        assertThat(operationExpr.operationType).isEqualTo(PLUS);
        assertThat(operationExpr.getParent()).isEqualTo(expression);
        assertThat(operationExpr.getLeft().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr.getLeft().getValue()).isEqualTo(2);
        assertThat(operationExpr.getLeft().getParent()).isEqualTo(operationExpr);
        assertThat(operationExpr.getRight().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr.getRight().getValue()).isEqualTo(3);
        assertThat(operationExpr.getRight().getParent()).isEqualTo(operationExpr);
    }

    @Test
    public void testPrepareWithSubtractOperationToken() {
        Cell cell = new Cell(0, "=2-3");
        Context context = new Context(new Cell[] {cell}, 1, 1);
        Lexer lexer = new Lexer(context);
        CellReferenceExpression expression = new CellReferenceExpression(cell, lexer, context);
        expression.prepare();
        assertThat(expression.getCell()).isEqualTo(cell);
        assertThat(expression.getParent()).isNull();
        assertThat(expression.getChild().getType()).isEqualTo(OPERATOR);
        OperationExpression operationExpr = (OperationExpression) expression.getChild();
        assertThat(operationExpr.operationType).isEqualTo(SUBTRACT);
        assertThat(operationExpr.getParent()).isEqualTo(expression);
        assertThat(operationExpr.getLeft().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr.getLeft().getValue()).isEqualTo(2);
        assertThat(operationExpr.getLeft().getParent()).isEqualTo(operationExpr);
        assertThat(operationExpr.getRight().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr.getRight().getValue()).isEqualTo(3);
        assertThat(operationExpr.getRight().getParent()).isEqualTo(operationExpr);
    }

    @Test
    public void testPrepareWithMultiplyOperationToken() {
        Cell cell = new Cell(0, "=2*3");
        Context context = new Context(new Cell[] {cell}, 1, 1);
        Lexer lexer = new Lexer(context);
        CellReferenceExpression expression = new CellReferenceExpression(cell, lexer, context);
        expression.prepare();
        assertThat(expression.getCell()).isEqualTo(cell);
        assertThat(expression.getParent()).isNull();
        assertThat(expression.getChild().getType()).isEqualTo(OPERATOR);
        OperationExpression operationExpr = (OperationExpression) expression.getChild();
        assertThat(operationExpr.operationType).isEqualTo(MULTIPLY);
        assertThat(operationExpr.getParent()).isEqualTo(expression);
        assertThat(operationExpr.getLeft().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr.getLeft().getValue()).isEqualTo(2);
        assertThat(operationExpr.getLeft().getParent()).isEqualTo(operationExpr);
        assertThat(operationExpr.getRight().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr.getRight().getValue()).isEqualTo(3);
        assertThat(operationExpr.getRight().getParent()).isEqualTo(operationExpr);
    }

    @Test
    public void testPrepareWithDivideOperationToken() {
        Cell cell = new Cell(0, "=2/3");
        Context context = new Context(new Cell[] {cell}, 1, 1);
        Lexer lexer = new Lexer(context);
        CellReferenceExpression expression = new CellReferenceExpression(cell, lexer, context);
        expression.prepare();
        assertThat(expression.getCell()).isEqualTo(cell);
        assertThat(expression.getParent()).isNull();
        assertThat(expression.getChild().getType()).isEqualTo(OPERATOR);
        OperationExpression operationExpr = (OperationExpression) expression.getChild();
        assertThat(operationExpr.operationType).isEqualTo(DIVIDE);
        assertThat(operationExpr.getParent()).isEqualTo(expression);
        assertThat(operationExpr.getLeft().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr.getLeft().getValue()).isEqualTo(2);
        assertThat(operationExpr.getLeft().getParent()).isEqualTo(operationExpr);
        assertThat(operationExpr.getRight().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr.getRight().getValue()).isEqualTo(3);
        assertThat(operationExpr.getRight().getParent()).isEqualTo(operationExpr);
    }

    @Test
    public void testPrepareWithComplexOperationTokens() {
        Cell cell = new Cell(0, "=2/3-4+5*6");
        Context context = new Context(new Cell[] {cell}, 1, 1);
        Lexer lexer = new Lexer(context);
        CellReferenceExpression expression = new CellReferenceExpression(cell, lexer, context);
        expression.prepare();
        assertThat(expression.getCell()).isEqualTo(cell);
        assertThat(expression.getParent()).isNull();

        OperationExpression operationExpr1 = (OperationExpression) expression.getChild();
        assertThat(operationExpr1.getType()).isEqualTo(OPERATOR);
        assertThat(operationExpr1.operationType).isEqualTo(MULTIPLY);
        assertThat(operationExpr1.getParent()).isEqualTo(expression);
        assertThat(operationExpr1.getRight().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr1.getRight().getValue()).isEqualTo(6);
        assertThat(operationExpr1.getRight().getParent()).isEqualTo(operationExpr1);

        OperationExpression operationExpr2 = (OperationExpression) operationExpr1.getLeft();
        assertThat(operationExpr2.getType()).isEqualTo(OPERATOR);
        assertThat(operationExpr2.operationType).isEqualTo(PLUS);
        assertThat(operationExpr2.getParent()).isEqualTo(operationExpr1);
        assertThat(operationExpr2.getRight().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr2.getRight().getValue()).isEqualTo(5);
        assertThat(operationExpr2.getRight().getParent()).isEqualTo(operationExpr2);

        OperationExpression operationExpr3 = (OperationExpression) operationExpr2.getLeft();
        assertThat(operationExpr3.getType()).isEqualTo(OPERATOR);
        assertThat(operationExpr3.operationType).isEqualTo(SUBTRACT);
        assertThat(operationExpr3.getParent()).isEqualTo(operationExpr2);
        assertThat(operationExpr3.getRight().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr3.getRight().getValue()).isEqualTo(4);
        assertThat(operationExpr3.getRight().getParent()).isEqualTo(operationExpr3);

        OperationExpression operationExpr4 = (OperationExpression) operationExpr3.getLeft();
        assertThat(operationExpr4.getType()).isEqualTo(OPERATOR);
        assertThat(operationExpr4.operationType).isEqualTo(DIVIDE);
        assertThat(operationExpr4.getParent()).isEqualTo(operationExpr3);
        assertThat(operationExpr4.getRight().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr4.getRight().getValue()).isEqualTo(3);
        assertThat(operationExpr4.getRight().getParent()).isEqualTo(operationExpr4);

        assertThat(operationExpr4.getLeft().getType()).isEqualTo(CONSTANT);
        assertThat(operationExpr4.getLeft().getValue()).isEqualTo(2);
        assertThat(operationExpr4.getLeft().getParent()).isEqualTo(operationExpr4);
    }

    @Test
    public void testPrepareWithCellReferenceToken() {
        Cell cell1 = new Cell(0, "=B1");
        Cell cell2 = new Cell(1, "3");
        Context context = new Context(new Cell[] {cell1, cell2}, 1, 2);
        Lexer lexer = new Lexer(context);
        CellReferenceExpression expression = new CellReferenceExpression(cell1, lexer, context);
        expression.prepare();
        assertThat(expression.getCell()).isEqualTo(cell1);
        assertThat(expression.getParent()).isNull();
        assertThat(expression.getChild().getType()).isEqualTo(CELL_REFERENCE);
        CellReferenceExpression child = (CellReferenceExpression) expression.getChild();
        assertThat(child.getCell()).isEqualTo(cell2);
        assertThat(child.getParent()).isEqualTo(expression);
        assertThat(child.getChild().getType()).isEqualTo(CONSTANT);
        assertThat(child.getChild().getValue()).isEqualTo(3);
        assertThat(child.getChild().getParent()).isEqualTo(child);
    }
}