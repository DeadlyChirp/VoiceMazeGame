package com.VocalMaze;

import org.lwjgl.system.CallbackI.J;

import com.VocalMaze.ModeleUtils.Direction;
import com.VocalMaze.ModeleUtils.Joueur;
import com.VocalMaze.ModeleUtils.Labyrinthe;

public class GameModel {
    
    private Labyrinthe labyrinthe;
    private Joueur joueur;
    private int nbMale ; 
    private int nbFemelles ; 

    public GameModel (String pseudo , int nbMale , int nbFemelles){
        joueur = new Joueur(pseudo) ; 
        this.labyrinthe = new Labyrinthe(joueur);
        this.nbFemelles = nbFemelles ; 
        this.nbMale = nbMale ; 
    }

    public Labyrinthe getLabyrinthe(){return null;}

    public void addScore(){}

    public boolean possible(Direction dir, int steps){
        return labyrinthe.possible(dir, steps) ; 
    }

    public boolean endGame () {
        return labyrinthe.endGame() ; 
    }

    public Joueur prochainJoueur(){return null;}

    public boolean finGame(){return false;}

    public void getLimite(){}

    public void pose(int x, int y){}

}

