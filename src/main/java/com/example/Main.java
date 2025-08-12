package com.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        System.out.print("Hello and welcome!");

        SwingUtilities.invokeLater(() -> {
            MazeSolverApp mazeSolverApp = new MazeSolverApp();
            mazeSolverApp.show();
        });

    }
}