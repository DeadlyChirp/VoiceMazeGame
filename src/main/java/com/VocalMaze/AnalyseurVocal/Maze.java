package com.VocalMaze.AnalyseurVocal;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.VocalMaze.Recorder;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class Maze {

    public static final int SIZE = 8;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;

    private static JFrame frame = new JFrame("Maze");

    Recorder recorder = new Recorder();
    boolean isRunning = false;

    public Maze(int size) {
        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame("Maze");

        final Board board = new Board(size);
        final Joueur player = new Joueur();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(board, BorderLayout.CENTER);
        mainPanel.setFocusable(true);
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        frame.add(bottomPanel, BorderLayout.SOUTH);

        JButton test = new JButton("Select a wav file");
        test.setFocusable(false);
        bottomPanel.add(test, BorderLayout.NORTH);

        JButton auto = new JButton("Auto");
        auto.setFocusable(false);
        bottomPanel.add(auto, BorderLayout.SOUTH);

        JButton start = new JButton("Start");
        start.setFocusable(false);
        bottomPanel.add(start, BorderLayout.CENTER);

        test.addActionListener(ae -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Wav", "wav");
            chooser.setCurrentDirectory(new File("."));
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                auto.setEnabled(false);
                start.setEnabled(false);
                test.setEnabled(false);

                System.out.println("File: " + chooser.getSelectedFile().getAbsolutePath());
                new Thread(() -> {
                    try {
                        LiumUtils.Result result = LiumUtils.executeForResult(chooser.getSelectedFile().getAbsolutePath());
                        int step = result.male + result.female * 2;
                        System.out.println(result + " -> " + step);
                        player.move(board, step);

                        auto.setEnabled(true);
                        start.setEnabled(true);
                        test.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        start.addActionListener(ae -> {
            if (isRunning) {
                start.setText("Start");
                isRunning = false;
                recorder.stopRecording();

                auto.setEnabled(false);
                start.setEnabled(false);
                test.setEnabled(false);

                new Thread(() -> {
                    try { 
                        String segment = "test.seg";
                        LiumUtils.Result result = LiumUtils.resultFromSegment(segment);
                        int step = result.male + result.female * 2;
                        System.out.println(result + " -> " + step);
                        player.move(board, step);

                        new File(segment).delete();

                        auto.setEnabled(true);
                        start.setEnabled(true);
                        test.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                start.setText("Stop");
                isRunning = true;
                recorder.startRecord ();
            }
        });

        auto.addActionListener(ae -> {
            test.setEnabled(false);
            start.setEnabled(false);
            auto.setEnabled(false);
            new Thread(() -> {
                player.move(board, 9999);
                auto.setEnabled(true);
                start.setEnabled(true);
                test.setEnabled(true);
            }).start();
        });

        mainPanel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) player.moveLeft(board);
                if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) player.moveRight(board);
                if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) player.moveUp(board);
                if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) player.moveDown(board);
            }
        });

        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBackground(Color.green);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        new Maze(SIZE);
    }
}
