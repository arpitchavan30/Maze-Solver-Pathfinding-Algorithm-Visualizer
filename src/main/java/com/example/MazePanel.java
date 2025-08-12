package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class MazePanel extends JPanel {
    private final int rows = 20;
    private final int cols = 20;
    private final int cellSize = 25;

    private int[][] grid;

    private final int startRow = 0;
    private final int startCol = 0;
    private int targetRow = rows - 1;
    private int targetCol = cols - 1;

    public MazePanel() {
        generateMaze();
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / cellSize;
                int row = e.getY() / cellSize;

                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    // Only allow setting target if clicked cell is not a wall and not the start cell
                    if (grid[row][col] == 0 && !(row == startRow && col == startCol)) {
                        setTargetCell(row, col);
                    }
                }
            }
        });
    }

    public void setTargetCell(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            // Clear old target cell if it was empty
            if (grid[targetRow][targetCol] == 0) {
                grid[targetRow][targetCol] = 0;
            }
            targetRow = row;
            targetCol = col;
            grid[targetRow][targetCol] = 0; // Ensure target cell is empty
            repaint();
        }
    }

    public void generateMaze() {
        grid = new int[rows][cols];
        Random rand = new Random();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = (rand.nextDouble() < 0.25) ? 1 : 0;
            }
        }

        grid[startRow][startCol] = 0;
        grid[targetRow][targetCol] = 0;

        repaint();
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetCol() {
        return targetCol;
    }

    public void markVisited(int row, int col, String algorithm) {
        switch (algorithm.toUpperCase()) {
            case "DFS" -> grid[row][col] = 2;
            case "BFS" -> grid[row][col] = 3;
            case "DIJKSTRA" -> grid[row][col] = 4;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == startRow && c == startCol) {
                    g.setColor(Color.GREEN);
                } else if (r == targetRow && c == targetCol) {
                    g.setColor(Color.RED);
                } else {
                    switch (grid[r][c]) {
                        case 1 -> g.setColor(Color.BLACK);
                        case 2 -> g.setColor(Color.YELLOW);
                        case 3 -> g.setColor(Color.CYAN);
                        case 4 -> g.setColor(Color.MAGENTA);
                        default -> g.setColor(Color.WHITE);
                    }
                }
                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }
    }
}