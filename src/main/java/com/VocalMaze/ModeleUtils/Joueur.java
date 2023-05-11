package com.VocalMaze.ModeleUtils;
public class Joueur {
    @SuppressWarnings("unused")
    private String pseudo;
    private int x; //position du joueur dans le labyrinthe (Ordonnée = x, Abssices = y)
    private int y;

    public Joueur(String pseudo) {
        this.pseudo = pseudo;
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

    public void move(Direction dir , int steps){
        switch(dir){
            case HAUT : x -= steps; break;
            case BAS : x += steps; break;
            case DROITE : y += steps; break;
            case GAUCHE : y -= steps; break;
        }
    }

}