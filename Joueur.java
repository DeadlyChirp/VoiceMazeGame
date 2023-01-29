public class Joueur {
    
    private String pseudo;
    private int x; //position du joueur dans le labyrinthe 
    private int y;

    Joueur(String pseudo, int x, int y) {
        this.pseudo = pseudo;
        this.x = x;
        this.y = y;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
