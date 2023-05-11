package com.VocalMaze;

import com.VocalMaze.ModeleUtils.*;
import com.VocalMaze.ModeleUtils.AnalyseVocal.Recorder;

public class GameModel {
    private Labyrinthe labyrinthe;
    private Joueur joueur;
    private Joueur joueur2;
    private boolean tour;
    private boolean multi;
    private Recorder recorder ; 
    private AudioAnalyser audioAnalyser ;
    @SuppressWarnings("unused")
    private int nbMaleTotal ; 
    @SuppressWarnings("unused")
    private int nbFemellesTotal ; 

    public GameModel (String pseudo , int nbMaleTotal , int nbFemellesTotal, boolean multi){
            joueur = new Joueur(pseudo); 
            tour = false;
            this.multi = multi;
            this.labyrinthe = new Labyrinthe(joueur, multi);
            this.nbMaleTotal = nbMaleTotal ; 
            this.nbFemellesTotal = nbFemellesTotal ; 
            recorder = new Recorder() ;
            audioAnalyser = new AudioAnalyser();
            if (multi) {
                joueur2 = new Joueur("Joueur2");
                this.labyrinthe = new Labyrinthe(joueur,joueur2, multi);
            }
    }

    public Labyrinthe getLabyrinthe(){
        return this.labyrinthe ; 
    }

    public boolean getTour(){
        return tour;
    }

    public boolean getMulti(){
        return multi;
    }

    public void changeTour(){
        tour = !tour;
    }

    public void movePlayer (Direction dir , int steps) {
        if (tour) {
            joueur2.move(dir, steps);
        } else {
            joueur.move(dir, steps);
        }
    }

    public boolean possible(Direction dir, int steps) {
        return labyrinthe.possible(dir, steps, tour) ; 
    }

    public boolean endGame1 () {
        return labyrinthe.endGame1() ; 
    }

     public boolean endGame2 () {
        return labyrinthe.endGame2() ; 
    }

    public void startRecord () {
        recorder.startRecord();
    }

    public void stopRecord () {
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

    public void startRecord(int timeMs) {
        recorder.startRecord(timeMs);
    }

}
