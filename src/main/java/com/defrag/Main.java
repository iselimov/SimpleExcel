package com.defrag;

import com.defrag.parser.Parser;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("input.txt"));
        int rowSize = s.nextInt();
        int colSize = s.nextInt();
        int size = rowSize * colSize;
        Cell[] data = new Cell[size];
        for (int i = 0; i < size; i++) {
            data[i] = new Cell(i, s.next());
        }
        new Parser(new Context(data, rowSize, colSize)).parse();
        for (int i = 0; i < rowSize; i ++) {
            for (int j = 0; j < colSize; j++) {
                System.out.print(data[i * colSize + j].getOutput());
                if (j != colSize - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}