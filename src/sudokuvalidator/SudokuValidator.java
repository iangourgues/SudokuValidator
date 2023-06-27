//Ian Gourgues
//igourg1@lsu.edu
//PA-1 (Multithreading)
//Feng Chen
//cs4103-sp23
//cs410395

package sudokuvalidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SudokuValidator {

    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    private static int[][] grid;

    public static void main(String[] args) throws IOException {
        String filename = "sudoku.txt";
        grid = readFile(filename);

        // Thread pool created
        ExecutorService executor = Executors.newFixedThreadPool(27);

        // Checks each row of the sudoku grid
        for (int i = 0; i < SIZE; i++) {
            Runnable rowChecker = new Checker("Row " + (i + 1), grid[i]);
            executor.execute(rowChecker);
        }

        // Checks each column of the sudoku grid
        for (int j = 0; j < SIZE; j++) {
            int[] column = new int[SIZE];
            for (int i = 0; i < SIZE; i++) {
                column[i] = grid[i][j];
            }
            Runnable columnChecker = new Checker("Column " + (j + 1), column);
            executor.execute(columnChecker);
        }

        // Checks each sub-grid of the sudoku grid
        for (int i = 0; i < SIZE; i += SUBGRID_SIZE) {
            for (int j = 0; j < SIZE; j += SUBGRID_SIZE) {
                int[] subGrid = new int[SIZE];
                int index = 0;
                for (int k = i; k < i + SUBGRID_SIZE; k++) {
                    for (int l = j; l < j + SUBGRID_SIZE; l++) {
                        subGrid[index++] = grid[k][l];
                    }
                }
                Runnable subGridChecker = new Checker("Sub-grid (" + (i/SUBGRID_SIZE + 1) + ", " + (j/SUBGRID_SIZE + 1) + ")", subGrid);
                executor.execute(subGridChecker);
            }
        }

        // Thread pool closed
        executor.shutdown();
    }

    private static int[][] readFile(String filename) throws IOException {
        int[][] puzzle = new int[SIZE][SIZE];
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        for (int i = 0; i < SIZE; i++) {
            String[] values = reader.readLine().split("\\s+");
            for (int j = 0; j < SIZE; j++) {
                puzzle[i][j] = Integer.parseInt(values[j]);
            }
        }
        reader.close();
        return puzzle;
    }

    private static class Checker implements Runnable {
        private String name;
        private int[] values;

        public Checker(String name, int[] values) {
            this.name = name;
            this.values = values;
        }

        @Override
        public void run() {
            boolean valid = true;
            for (int i = 0; i < SIZE; i++) {
                for (int j = i + 1; j < SIZE; j++) {
                    if (values[i] == values[j]) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) {
                    break;
                }
            }
            String result = valid ? "valid" : "invalid";
            System.out.println("[Thread " + Thread.currentThread().getId() + "] " + name + ": " + result);
  
            }
            
        }
    }
            

