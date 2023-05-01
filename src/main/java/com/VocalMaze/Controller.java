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
        return model.getLabyrinthe().getPlateau()[x][y].getOuvert() ;
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

    public Direction [] transcrire () {
        return model.transcrire() ; 
    }

    public boolean play(Direction [] directions) {
        if (directions.length == 0) return false ; 
        int i = 1;
        int occ = 1;
        Direction d = directions[0];
        while (i < directions.length) { //Si la même direction est répétée successivement, alors on compte les occurences et 
            if (directions[i] == d) occ++;//on appel une fois movePlayer pour cette même direction.
            if (directions[i] != d || i+1 == directions.length) {
                if (possible(d, occ)) {
                    movePlayer(d, occ);
                    gameView.movePlayer(d, occ);
                    if (model.endGame()) return true;
                }
                d = directions[i];
                occ = 1;
            }
            i++;
        }
        return false ; 
    }

}
