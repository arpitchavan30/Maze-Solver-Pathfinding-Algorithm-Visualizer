package com.example;

public class Cell {
    int row, col;
    boolean isWall;
    boolean visited;
    boolean inPath;
    int distance = Integer.MAX_VALUE; // For Dijkstra

    public Cell(int row, int col, boolean isWall) {
        this.row = row;
        this.col = col;
        this.isWall = isWall;
    }
}
