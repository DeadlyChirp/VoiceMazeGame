package com.VocalMaze.AnalyseurVocal;

import javax.swing.*;

public class Joueur {

    private int xPos;
    private int yPos;

    public Joueur() {
        xPos = 1;
        yPos = 1;
    }

    public void move(Board board, int step) {
        int[] directionX = new int[]{0, 0, -1, 1};
        int[] directionY = new int[]{-1, 1, 0, 0};

        for (int i = 0; i < step; i++) {
            if (i != 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            boolean flag = false;

            for (int dir = 0; dir < 4; dir++) {
                Board.Type cell = board.get(xPos + directionX[dir], yPos + directionY[dir]);
                if (cell == Board.Type.DESTINATION) {
                    board.set(xPos, yPos, Board.Type.VISITED);
                    GameOver();
                    return;
                }

                if (cell == Board.Type.ROAD) {
                    flag = true;

                    board.set(xPos, yPos, Board.Type.VISITED);
                    xPos += directionX[dir];
                    yPos += directionY[dir];
                    board.set(xPos, yPos, Board.Type.PLAYER);
                    break;
                }
            }

            if (flag) {
                continue;
            }

            for (int dir = 0; dir < 4; dir++) {
                Board.Type cell = board.get(xPos + directionX[dir], yPos + directionY[dir]);
                if (cell == Board.Type.VISITED) {
                    board.set(xPos, yPos, Board.Type.CLOSED);
                    xPos += directionX[dir];
                    yPos += directionY[dir];
                    board.set(xPos, yPos, Board.Type.PLAYER);
                    break;
                }
            }
        }
    }

    public void moveLeft(Board board) {
        if ((board.get(xPos - 1, yPos) != Board.Type.WALL) && (board.get(xPos - 1, yPos) != Board.Type.BLOCK)) {
            board.set(xPos, yPos, Board.Type.VISITED);
            if (board.get(xPos -= 1, yPos) == Board.Type.DESTINATION) GameOver();
            else board.set(xPos, yPos, Board.Type.PLAYER);
        }
    }

    public void moveRight(Board board) {
        if ((board.get(xPos + 1, yPos) != Board.Type.WALL) && (board.get(xPos + 1, yPos) != Board.Type.BLOCK)) {
            board.set(xPos, yPos, Board.Type.VISITED);
            if (board.get(xPos += 1, yPos) == Board.Type.DESTINATION) GameOver();
            else board.set(xPos, yPos, Board.Type.PLAYER);
        }
    }

    public void moveUp(Board board) {
        if ((board.get(xPos, yPos - 1) != Board.Type.WALL) && (board.get(xPos, yPos - 1) != Board.Type.BLOCK)) {
            board.set(xPos, yPos, Board.Type.VISITED);
            if (board.get(xPos, yPos -= 1) == Board.Type.DESTINATION) GameOver();
            else board.set(xPos, yPos, Board.Type.PLAYER);
        }
    }

    public void moveDown(Board board) {
        if ((board.get(xPos, yPos + 1) != Board.Type.WALL) && (board.get(xPos, yPos + 1) != Board.Type.BLOCK)) {
            board.set(xPos, yPos, Board.Type.VISITED);
            if (board.get(xPos, yPos += 1) == Board.Type.DESTINATION) GameOver();
            else board.set(xPos, yPos, Board.Type.PLAYER);
        }
    }

    public void GameOver() {
        JOptionPane.showMessageDialog(null, "Gave Over!!!", "Maze", JOptionPane.INFORMATION_MESSAGE);
        new Maze(Maze.SIZE);
    }

    public void setX(int i) {
        xPos = i;
    }

    public void setY(int j) {
        yPos = j;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }
}
