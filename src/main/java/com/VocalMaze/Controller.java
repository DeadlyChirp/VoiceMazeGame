package com.VocalMaze;

import com.VocalMaze.ModeleUtils.Direction;

public class Controller {
    private GameModel model ; 
    @SuppressWarnings("unused")
    private GameView gameView ; 

    public Controller(GameModel model, GameView gameView){
        this.model = model;
        this.gameView = gameView;
    }

    public boolean getOuvert (int x , int y) {
        return model.getLabyrinthe().getPlateau()[y][x].getOuvert() ;
    }

    public GameModel getGameModel() {
        return model;
    }

    public void movePlayer (Direction dir , int steps) {
        model.movePlayer(dir, steps);
    }

    public boolean endGame () {
        return model.endGame() ; 
    }

    public boolean possible(Direction dir, int steps) {
        return model.possible(dir, steps) ; 
    }

    public void startRecord () {
        model.startRecord();
    }

    public void stopRecord () {
        model.stopRecord();
    }

    public void startRecord(int timeMs) {
        model.startRecord(timeMs);
    }

    public int [] analyse1 () {
        return model.analyse1() ; 
    }

    public int [] analyse2 () {
        return model.analyse2() ; 
    }

    public boolean transcrireAndPlay() {
        //TODO
        /*
         * Cette methode va transcrire l'audio enregistré 
         * Recuperer les directions
         * Faire bouger le modele si possible 
         * Faire correspondre la vue avec la vue si possible avec la fonctions movePlayer(Direction , int) de GameView
         */

        //TODO en cas de fin de jeu renvoyer true  , false sinon
        return false ; 
    }

}
