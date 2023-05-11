package com.VocalMaze.ModeleUtils;

public class Case {
    private boolean ouvert;
    private int x;
    private int y;

    Case(boolean haut, int x, int y) {
        this.ouvert = haut;
        this.x = x;
        this.y = y;
    }

    Case(int x, int y) {
        this(false, x, y);
    }
  
    public boolean getOuvert() {
        return ouvert;
    }

    public void setOuvert(boolean ouvert) {
        this.ouvert = ouvert;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}