package com.VocalMaze;

public class Controller {
    
    private GameModel model ; 
    private GameView gameView ; 


    public Controller(GameModel model, GameView gameView){
        this.model = model;
        this.gameView = gameView;
    }

    public void poser(int x, int y){
        //fait le lien entre poser du modele et poser de la vue
    }

    public void abandonne(){
        //Fait l'abandon quand le joueur veut abandonner
        //On abandonne avec le model. 
        //model.getJoueurActuel().abandonne()

    }

    public void perso(int x, int y,  int id){
        //En fontion des coordonnée, on prend la case en question et on regarde si ya quelqu'un dedans.
        /* INSPIRATION 
        TuileC tmp = ((TuileC)model.getCase(x, y)) ; 
        if (tmp == null) return ; 
        if (tmp.getPion() == -1 && model.getJoueurActuel().hasPions()) {
            tmp.setPion(id);
            model.getJoueurActuel().enlevePion();
            gameView.updatePlateauView(x, y, tmp.giveView(), model.getLimite());
            return ; 
        }
        if (tmp.getPion() == id) {
            tmp.setPion(-1);
            model.getJoueurActuel().addPion();
            gameView.updatePlateauView(x, y, tmp.giveView(), model.getLimite());
        }
        */
    }


}
