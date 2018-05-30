package com.defrag.parser;

import com.defrag.Cell;
import com.defrag.Context;
import com.defrag.lexer.Lexer;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Entry point for parsing cells
 */
@Slf4j
public class Parser {

    private final Context context;
    private final Lexer lexer;

    public Parser(Context context) {
        this.context = context;
        this.lexer = new Lexer(context);
    }

    public void parse() {
        Optional<Cell> next;
        while ((next = context.nextUnprocessed()).isPresent()) {
            Cell nextCell = next.get();
            CellReferenceExpression rootExpr = new CellReferenceExpression(nextCell, lexer, context);
            rootExpr.process();
        }
    }
}