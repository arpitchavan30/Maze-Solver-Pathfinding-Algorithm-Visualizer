package com.example;

import com.example.Cell;

import java.util.*;
import java.util.function.Consumer;

public class MazeSolverLogic {
    private final int[][] grid;
    private final int rows, cols;
    private final Consumer<Void> repaintCallback;
    private final boolean[][] visited;
    private final int targetRow, targetCol;
    private final int startRow, startCol;

    public MazeSolverLogic(int[][] grid, Runnable repaintCallback,
                           int startRow, int startCol,
                           int targetRow, int targetCol) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.repaintCallback = v -> repaintCallback.run();
        this.visited = new boolean[rows][cols];
        this.startRow = startRow;
        this.startCol = startCol;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }

    public boolean dfs(int r, int c) {
        if (!isValid(r, c) || visited[r][c]) return false;
        visited[r][c] = true;
        grid[r][c] = 2;
        repaintCallback.accept(null);
        sleep();

        if (r == targetRow && c == targetCol) return true;

        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : dirs) {
            if (dfs(r + d[0], c + d[1])) return true;
        }
        return false;
    }

    public void bfs() {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            grid[r][c] = 3;
            repaintCallback.accept(null);
            sleep();

            if (r == targetRow && c == targetCol) return;

            int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (isValid(nr, nc) && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc});
                }
            }
        }
    }

    public void dijkstra() {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.dist));
        Map<String, String> parent = new HashMap<>();
        int[][] dist = new int[rows][cols];

        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[startRow][startCol] = 0;

        pq.add(new Node(startRow, startCol, 0));
        parent.put(startRow + "," + startCol, null);

        while (!pq.isEmpty()) {
            Node cur = pq.poll();

            if (visited[cur.r][cur.c]) continue; // Skip already processed
            visited[cur.r][cur.c] = true;

            grid[cur.r][cur.c] = 4;
            repaintCallback.accept(null);
            sleep();

            if (cur.r == targetRow && cur.c == targetCol) {
                highlightPath(parent, cur.r, cur.c);
                return;
            }

            int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] d : dirs) {
                int nr = cur.r + d[0], nc = cur.c + d[1];
                if (isValid(nr, nc)) {
                    int newDist = dist[cur.r][cur.c] + 1;
                    if (newDist < dist[nr][nc]) {
                        dist[nr][nc] = newDist;
                        pq.add(new Node(nr, nc, newDist));
                        parent.put(nr + "," + nc, cur.r + "," + cur.c);
                    }
                }
            }
        }
    }

    private void highlightPath(Map<String, String> parent, int endR, int endC) {
        String cur = endR + "," + endC;
        while (cur != null) {
            String[] parts = cur.split(",");
            int r = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);
            grid[r][c] = 3; // mark path with BFS color or you can add a separate value
            repaintCallback.accept(null);
            sleep();
            cur = parent.get(cur);
        }
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c] != 1;
    }

    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }
    }

    private static class Node {
        int r, c, dist;

        Node(int r, int c, int dist) {
            this.r = r;
            this.c = c;
            this.dist = dist;
        }
    }
}