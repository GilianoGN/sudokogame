package com.sudoko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.List;

import com.sudoko.service.Difficulty;

public class Generator {
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final Random random = new Random();
    private static int[][] grid = new int[GRID_SIZE][GRID_SIZE]; 

    private static boolean fillGrid(int row, int col){ 
        if (row == GRID_SIZE){
            return true;
        }
        int nextRow = (col == GRID_SIZE -1) ? row + 1 : row;
        int nextCol = (col == GRID_SIZE -1) ? 0 : col + 1;
        List<Integer> numbersToTry = new ArrayList<>();
        for (int i = 1; i <= GRID_SIZE; i++){
            numbersToTry.add(i);
        }
        Collections.shuffle(numbersToTry);
        for (int num : numbersToTry){
            if (isValid(row, col, num)){
                grid[row][col] = num;
                if (fillGrid(nextRow, nextCol)) {
                    return true;
                }
                grid[row][col] = 0;
            }
        }
        return false;
    }

    private static boolean isValid(int row, int col, int num){ 
        for (int c = 0; c < GRID_SIZE; c++){
            if (grid[row][c] == num){
                return false;
            }
        }
        for (int r = 0; r < GRID_SIZE; r++){
            if (grid[r][col] == num){
                return false;
            }
        }
        int startRow = row - (row % SUBGRID_SIZE);
        int startCol = col - (col % SUBGRID_SIZE);
        for (int r = 0; r < SUBGRID_SIZE; r++){
            for (int c = 0; c < SUBGRID_SIZE; c++){
                if (grid[startRow + r][startCol + c] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<String> generateGameBoard(Difficulty difficulty){ 
        List<String> gameBoardString = new ArrayList<>();
        int totalCells = GRID_SIZE * GRID_SIZE;
        int cellsToKeepFixed = (int) (totalCells * difficulty.getFillPercentage());
        int minCellsFixed = 25;
        cellsToKeepFixed = Math.max(cellsToKeepFixed, minCellsFixed);
        List<int[]> allCellCoords = new ArrayList<>();
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++){
                allCellCoords.add(new int[] {r , c});
            }
        }
        Collections.shuffle(allCellCoords);
        Set<String> fixedCellCoordinates = new HashSet<>();
        for (int i = 0; i < cellsToKeepFixed; i++){
            int[] coord = allCellCoords.get(i);
            fixedCellCoordinates.add(coord[0] + "," + coord[1]);
        }
        for (int r = 0; r < GRID_SIZE; r++){
            for (int c = 0; c < GRID_SIZE; c++){
                int value = grid[r][c];
                boolean isFixed = fixedCellCoordinates.contains(r + "," + c);
                gameBoardString.add(String.format("%d,%d;%d,%b", r, c, value, isFixed));
            }
        }
        return gameBoardString;
    }

    public String[] gameNew(Difficulty difficulty){
        grid = new int[GRID_SIZE][GRID_SIZE];
        if (!fillGrid(0,0)){
            System.err.println("Erro: Não foi possivel gerar a matriz");
            return new String[0];
        }
        List<String> gameBoard = generateGameBoard(difficulty);
        String[] gameConvert = gameBoard.toArray(new String[0]);
        return gameConvert;
    }
}