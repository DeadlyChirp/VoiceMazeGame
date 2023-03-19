package com.VocalMaze;

import com.VocalMaze.ModeleUtils.Direction;
import com.VocalMaze.ModeleUtils.Joueur;
import com.VocalMaze.ModeleUtils.Labyrinthe;
import com.VocalMaze.ModeleUtils.AnalyseurVocal.Recorder;

public class GameModel {
    
    private Labyrinthe labyrinthe;
    private Joueur joueur;
    private Recorder recorder ; 
    private int nbMaleTotal ; 
    private int nbFemellesTotal ; 

    public GameModel (String pseudo , int nbMaleTotal , int nbFemellesTotal){
        joueur = new Joueur(pseudo) ; 
        this.labyrinthe = new Labyrinthe(joueur);
        this.nbMaleTotal = nbMaleTotal ; 
        this.nbFemellesTotal = nbFemellesTotal ; 
        recorder = new Recorder() ; 
    }

    public Labyrinthe getLabyrinthe(){
        return this.labyrinthe ; 
    }

    public void movePlayer (Direction dir , int steps) {
        joueur.move(dir, steps);
    }

    public boolean possible(Direction dir, int steps) {
        return labyrinthe.possible(dir, steps) ; 
    }

    public boolean endGame () {
        return labyrinthe.endGame() ; 
    }

    public void startRecording () {
        recorder.startRecord();
    }

    public void stopRecording () {
        recorder.stopRecording();
    }

}

