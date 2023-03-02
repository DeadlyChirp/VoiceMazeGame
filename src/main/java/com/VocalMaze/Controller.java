package com.VocalMaze;

import com.VocalMaze.ModeleUtils.Direction;

public class Controller {
    private GameModel model ; 
    private GameView gameView ; 

    public Controller(GameModel model, GameView gameView){
        this.model = model;
        this.gameView = gameView;
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


}
