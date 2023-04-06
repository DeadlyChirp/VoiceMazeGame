package com.VocalMaze;

import com.VocalMaze.ModeleUtils.*;
import com.VocalMaze.ModeleUtils.AnalyseVocal.Recorder;


public class GameModel {
    
    private Labyrinthe labyrinthe;
    private Joueur joueur;
    private Recorder recorder ; 
    private AudioAnalyser audioAnalyser ;
    @SuppressWarnings("unused")
    private int nbMaleTotal ; 
    @SuppressWarnings("unused")
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

    public int [] analyse1 () {
        return audioAnalyser.analyse1() ; 
    }

    public int [] analyse2 () {
        return audioAnalyser.analyse2() ; 
    }

    public Direction [] transcrire () {
        return audioAnalyser.transcrire() ; 
    }

}

