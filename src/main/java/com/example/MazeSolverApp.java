package com.example;

import javax.swing.*;
import java.util.Arrays;


public class MazeSolverApp {
    private final JFrame frame;
    private final MazePanel mazePanel;
    private final JComboBox<String> algorithmSelector;
    private final JButton startButton;
    private final JButton resetButton;
    private final JLabel timerLabel;

    private int[][] initialGrid;

    private Timer timer;
    private long startTime;

    public MazeSolverApp() {
        frame = new JFrame("Maze Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mazePanel = new MazePanel();
        initialGrid = copyGrid(mazePanel.getGrid());

        String[] algorithms = {"DFS", "BFS", "Dijkstra"};
        algorithmSelector = new JComboBox<>(algorithms);

        startButton = new JButton("Start");
        resetButton = new JButton("Reset");
        timerLabel = new JLabel("Time: 0 ms");

        setupUI();
        setupListeners();
        setupTimer();
    }

    private void setupUI() {
        JPanel controls = new JPanel();
        controls.add(new JLabel("Algorithm:"));
        controls.add(algorithmSelector);
        controls.add(startButton);
        controls.add(resetButton);
        controls.add(timerLabel);

        frame.add(mazePanel, "Center");
        frame.add(controls, "South");
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void setupTimer() {
        timer = new Timer(100, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            timerLabel.setText("Time: " + elapsed + " ms");
        });
    }

    private void setupListeners() {
        startButton.addActionListener(e -> {
            resetGridToInitial();
            timerLabel.setText("Time: 0 ms");
            startTime = System.currentTimeMillis();
            timer.start();

            new Thread(() -> {
                MazeSolverLogic solver = new MazeSolverLogic(
                        mazePanel.getGrid(),
                        mazePanel::repaint,
                        mazePanel.getStartRow(),
                        mazePanel.getStartCol(),
                        mazePanel.getTargetRow(),
                        mazePanel.getTargetCol()
                );

                String selectedAlgo = (String) algorithmSelector.getSelectedItem();

                if ("DFS".equalsIgnoreCase(selectedAlgo)) {
                    solver.dfs(mazePanel.getStartRow(), mazePanel.getStartCol());
                } else if ("BFS".equalsIgnoreCase(selectedAlgo)) {
                    solver.bfs();
                } else if ("Dijkstra".equalsIgnoreCase(selectedAlgo)) {
                    solver.dijkstra();
                }

                timer.stop();
            }).start();
        });

        resetButton.addActionListener(e -> {
            mazePanel.generateMaze();
            initialGrid = copyGrid(mazePanel.getGrid());
            timerLabel.setText("Time: 0 ms");
        });

        algorithmSelector.addActionListener(e -> {
            resetGridToInitial();
            timerLabel.setText("Time: 0 ms");
        });
    }

    private void resetGridToInitial() {
        int[][] currentGrid = mazePanel.getGrid();
        for (int r = 0; r < initialGrid.length; r++) {
            System.arraycopy(initialGrid[r], 0, currentGrid[r], 0, initialGrid[r].length);
        }
        mazePanel.repaint();
    }

    private int[][] copyGrid(int[][] source) {
        int[][] copy = new int[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return copy;
    }

    public void show() {
        frame.setVisible(true);
    }
}