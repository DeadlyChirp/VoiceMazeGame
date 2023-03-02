package com.VocalMaze.AnalyseurVocal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Board extends JPanel {

    private final Type[][] board;
    private final int size;
    private final int scale;
    private int unVisited;
    private final LinkedList<Position> positionList = new LinkedList<>();

    public Board(int size) {
        unVisited = (size * size);
        size = size * 2 + 1;
        scale = size;
        board = new Type[size][size];
        this.size = size;

        generateBoard();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int n = (int) (Maze.WIDTH / (scale + 0.5));
        int offset = (Maze.WIDTH - n * size) / 4;

        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                if ((board[i][k] == Type.WALL)) {
                    g.setColor(Color.black);
                    g.fillRect(offset + i * n, k * n, n, n);
                } else if (board[i][k] == Type.BLOCK) {
                    g.setColor(Color.black);
                    g.fillRect(offset + i * n, k * n, n, n);
                } else if (board[i][k] == Type.DESTINATION) {
                    g.setColor(Color.red);
                    g.fillRect(offset + i * n, k * n, n, n);
                } else if (board[i][k] == Type.PLAYER) {
                    g.setColor(Color.blue);
                    g.fillRect(offset + i * n, k * n, n, n);
                } else if (board[i][k] == Type.VISITED) {
                    g.setColor(Color.PINK);
                    g.fillRect(offset + i * n, k * n, n, n);
                }
            }
        }
    }

    public void generateBoard() {
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                board[i][k] = Type.EMPTY;
            }
        }

        for (int i = 0; i < size; i += 2) {
            for (int k = 0; k < size; k++) {
                board[i][k] = Type.BLOCK;
                board[k][i] = Type.BLOCK;
            }
        }

        for (int i = 0; i < size; i++) {
            board[i][0] = Type.WALL;
            board[0][i] = Type.WALL;
            board[size - 1][i] = Type.WALL;
            board[i][size - 1] = Type.WALL;
        }

        generateMaze(1, 1);
    }

    public Type get(int x, int y) {
        return board[x][y];
    }

    public void set(int x, int y, Type value) {
        board[x][y] = value;
        repaint();
    }

    public void printBoard() {
        System.out.println("\n");
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                System.out.print(board[k][i].get());
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public Type[] updateDirection(Position cell) {
        Type[] direction = new Type[]{Type.NONE, Type.NONE, Type.NONE, Type.NONE};

        if (get(cell.getX(), cell.getY() - 1) != Type.WALL) {
            direction[0] = get(cell.getX(), cell.getY() - 2);
        }

        if (get(cell.getX(), cell.getY() + 1) != Type.WALL) {
            direction[1] = get(cell.getX(), cell.getY() + 2);
        }

        if (get(cell.getX() + 1, cell.getY()) != Type.WALL) {
            direction[2] = get(cell.getX() + 2, cell.getY());
        }

        if (get(cell.getX() - 1, cell.getY()) != Type.WALL) {
            direction[3] = get(cell.getX() - 2, cell.getY());
        }

        return direction;
    }

    public void generateMaze(int posX, int posY) {
        Position cell = new Position(posX, posY);
        set(cell.getX(), cell.getY(), Type.ROAD);
        unVisited -= 1;

        Type[] direction = updateDirection(cell);
        while (unVisited != 0) {
            int free = 0;
            if ((direction[0] == Type.EMPTY) || (direction[1] == Type.EMPTY) || (direction[2] == Type.EMPTY) || (direction[3] == Type.EMPTY)) {
                free = 1;
            }

            Random generator = new Random();
            int random = generator.nextInt(4);
            set(cell.getX(), cell.getY(), Type.ROAD);

            if ((random == 0) && (direction[0] == Type.EMPTY)) {
                if (get(cell.getX(), cell.getY() - 1) != Type.WALL) {
                    set(cell.getX(), cell.getY() - 1, Type.ROAD);
                    cell = new Position(cell.getX(), cell.getY() - 2);
                    positionList.push(cell);
                    direction = updateDirection(cell);
                    unVisited--;
                }
            } else if ((random == 1) && (direction[1] == Type.EMPTY)) {
                if (get(cell.getX(), cell.getY() + 1) != Type.WALL) {
                    set(cell.getX(), cell.getY() + 1, Type.ROAD);
                    cell = new Position(cell.getX(), cell.getY() + 2);
                    positionList.push(cell);
                    direction = updateDirection(cell);
                    unVisited--;
                }
            } else if ((random == 2) && (direction[2] == Type.EMPTY)) {
                if (get(cell.getX() + 1, cell.getY()) != Type.WALL) {
                    set(cell.getX() + 1, cell.getY(), Type.ROAD);
                    cell = new Position(cell.getX() + 2, cell.getY());
                    positionList.push(cell);
                    direction = updateDirection(cell);
                    unVisited--;
                }
            } else if ((random == 3) && (direction[3] == Type.EMPTY)) {
                if (get(cell.getX() - 1, cell.getY()) != Type.WALL) {
                    set(cell.getX() - 1, cell.getY(), Type.ROAD);
                    cell = new Position(cell.getX() - 2, cell.getY());
                    positionList.push(cell);
                    direction = updateDirection(cell);
                    unVisited--;
                }
            } else {
                if (free == 0 && positionList.size() != 0) {
                    cell = positionList.get(positionList.size() - 1);
                    positionList.remove(positionList.size() - 1);
                    direction = updateDirection(cell);
                }
            }
        }

        set(1, 1, Type.PLAYER);
        set(cell.getX(), cell.getY(), Type.DESTINATION);
    }

    public static class Position {

        private int x;
        private int y;

        public Position() {
            this.x = 0;
            this.y = 0;
        }

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public enum Type {

        NONE('\0'),
        PLAYER('P'),
        DESTINATION('D'),
        WALL('0'),
        EMPTY('E'),
        ROAD('-'),
        BLOCK('B'),
        VISITED('V'),
        CLOSED('C');

        public char get() {
            return type;
        }

        private final char type;

        Type(char c) {
            this.type = c;
        }
    }
}
