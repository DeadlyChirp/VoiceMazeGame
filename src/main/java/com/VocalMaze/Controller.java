package com.VocalMaze;

import com.VocalMaze.ModeleUtils.Direction;

public class Controller {
    private GameModel model ;
    private GameView gameView ; 

    public Controller(GameModel model, GameView gameView){
        this.model = model;
        this.gameView = gameView;
    }

    public boolean getOuvert (int x , int y) {
        return model.getLabyrinthe().getPlateau()[x][y].getOuvert() ;
    }

    public GameModel getGameModel() {
        return model;
    }

    public void movePlayer (Direction dir , int steps) {
        model.movePlayer(dir, steps);
    }

    public boolean endGame1 () {
        return model.endGame1() ; 
    }

    public boolean endGame2 () {
        return model.endGame2();
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

    public Direction [] transcrire () {
        return model.transcrire() ; 
    }

    public int play(Direction [] directions) {
        if (directions.length == 0) return 0 ; 
        int i = 1;
        int occ = 1;
        Direction d = directions[0];
        if (i == directions.length) {
            if (possible(d, occ)) {
                movePlayer(d, occ);
                gameView.movePlayer(d, occ);
                if (model.endGame1()) return 1;
                if(getGameModel().getMulti()){
                     if (model.endGame2())  return 2;
                }
            }
        }
        while (i < directions.length) { //Si la même direction est répétée successivement, alors on compte les occurences et 
            if (directions[i] == d) occ++;//on appel une fois movePlayer pour cette même direction.
            if (directions[i] != d || i+1 == directions.length) {
                if (possible(d, occ)) {
                    movePlayer(d, occ);
                    gameView.movePlayer(d, occ);
                    if (model.endGame1()) return 1;
                    if(getGameModel().getMulti()){
                        if (model.endGame2())  return 2;
                   }
                }
                d = directions[i];
                occ = 1;
            }
            i++;
        }
        return 0 ; 
    }

}