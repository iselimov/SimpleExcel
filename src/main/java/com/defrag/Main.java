package com.defrag;

import com.defrag.parser.Parser;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Entry point for parsing cells, which data we take from file(input.txt) in resources
 */
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
        System.out.println("Before");
        String[] inputData = new String[size];
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = data[i].getInput();
        }
        prettyPrint(inputData, rowSize, colSize);
        new Parser(new Context(data, rowSize, colSize)).parse();
        System.out.println("After");
        String[] outputData = new String[size];
        for (int i = 0; i < inputData.length; i++) {
            outputData[i] = data[i].getOutput().toString();
        }
        prettyPrint(outputData, rowSize, colSize);
    }

    private static void prettyPrint(String[] data, int rowSize, int colSize) {
        int[] maxLengths = new int[colSize];
        for (int j = 0; j < colSize; j++) {
            int maxDataRowIndex = 0;
            for (int i = 1; i < rowSize; i++) {
                String maxOutput = data[maxDataRowIndex * colSize + j];
                String currOutput = data[i * colSize + j];
                if (currOutput.length() > maxOutput.length()) {
                    maxDataRowIndex = i;
                }
            }
            maxLengths[j] = data[maxDataRowIndex * colSize + j].length();
        }
        for (int i = 0; i < rowSize; i ++) {
            for (int j = 0; j < colSize; j++) {
                System.out.print(StringUtils.rightPad(data[i * colSize + j], maxLengths[j]));
                if (j != colSize - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}