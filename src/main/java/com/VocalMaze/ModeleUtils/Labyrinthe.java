package com.VocalMaze.ModeleUtils;
import java.util.ArrayList;
import java.util.Random;

public class Labyrinthe {
    private Case[][] plateau;
    private Joueur joueur;
    private Joueur joueur2;
    private Case pointArrivee;
    private Case pointArrivee2;
    private Case pointDepart;
    private Case pointDepart2;
    private boolean multi;

    public Labyrinthe(Joueur joueur, boolean multi) {
        this.joueur = joueur;
        this.joueur2 = new Joueur("");
        plateau = new Case[17][29];
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                plateau[i][j] = new Case(i, j);
            }
        }
        genereLabyrinthe();
    }

    public Labyrinthe(Joueur joueur,Joueur joueur2, boolean multi) {
        this.joueur = joueur;
        this.joueur2 = joueur2;
        this.multi = multi;
        plateau = new Case[17][29];
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                plateau[i][j] = new Case(i, j);
            }
        }
        genereLabyrinthe();
    }

    public Case[][] getPlateau() {
        return plateau;
    }

    public Case getPointDepart() {
        return pointDepart;
    }

    public Case getPointDepart2() {
        return pointDepart2;
    }

    public boolean estPointArrivee(int i, int j) {
        return pointArrivee.getX() == i && pointArrivee.getY() == j;
    }

    public boolean estPointArrivee2(int i, int j) {
        return pointArrivee2.getX() == i && pointArrivee2.getY() == j;
    }

    private void ajouteCaseFrontiere(ArrayList<Case> caseFrontiere, int x, int y) {
        for (int i = -2; i <= 2; i +=4) {
            if (i+x < plateau.length && i+x >= 0 && !plateau[i+x][y].getOuvert()) {
                caseFrontiere.add(plateau[i+x][y]);
            } 
            if (i+y < plateau[x].length && i+y >= 0  && !plateau[x][i+y].getOuvert()) {
                caseFrontiere.add(plateau[x][i+y]);
            }
        }
    }

    private ArrayList<Case> voisin(Case c) {
        ArrayList<Case> voisin = new ArrayList<Case>();
        for (int i = -2; i <= 2; i += 4) {
            if ( i+c.getX() < plateau.length && i+c.getX() >= 0 && plateau[i+c.getX()][c.getY()].getOuvert()) {
                voisin.add(plateau[i+c.getX()][c.getY()]);
            } else if (i+c.getY() < plateau[c.getX()].length && i+c.getY() >= 0 && plateau[c.getX()][i+c.getY()].getOuvert()) {
                voisin.add(plateau[c.getX()][i+c.getY()]);
            }
        }
        return voisin;
    }

    private void ajouteLiensEntreCase(Case c) {
        ArrayList<Case> listeVoisin = voisin(c);
        Case caseVoisin;
        Random rd = new Random();
        if (listeVoisin.size() > 1 ) caseVoisin = listeVoisin.get(rd.nextInt(0, listeVoisin.size()));
        else caseVoisin = listeVoisin.get(0);
        if (caseVoisin.getX() == c.getX() -2 && caseVoisin.getY() == c.getY()) {
            plateau[caseVoisin.getX()][caseVoisin.getY()].setOuvert(true);
            plateau[caseVoisin.getX()+1][caseVoisin.getY()].setOuvert(true);
        }
        else if (caseVoisin.getX() == c.getX() +2 && caseVoisin.getY() == c.getY()) {
            plateau[caseVoisin.getX()][caseVoisin.getY()].setOuvert(true);
            plateau[caseVoisin.getX()-1][caseVoisin.getY()].setOuvert(true);
        }
        else if (caseVoisin.getX() == c.getX() && caseVoisin.getY() == c.getY() -2) {
            plateau[caseVoisin.getX()][caseVoisin.getY()].setOuvert(true);
            plateau[caseVoisin.getX()][caseVoisin.getY()+1].setOuvert(true);
        }
        else if (caseVoisin.getX() == c.getX() && caseVoisin.getY() == c.getY() +2) {
            plateau[caseVoisin.getX()][caseVoisin.getY()].setOuvert(true);
            plateau[caseVoisin.getX()][caseVoisin.getY()-1].setOuvert(true);
        }
    }
        
    public void fermeBordsHautBas() {
        int i = 0;
        int a = 0;
        boolean premierPassage = true;
        while(i < plateau[0].length) {
            if (plateau[a][i].getOuvert()) {
                for (int j = 0; j < plateau[0].length; j++) {
                    plateau[a][j].setOuvert(false);
                }
                i = -1;
                a = plateau.length - 1;
                if (premierPassage == false) break;
                premierPassage = false;
            }
            if (i == plateau[0].length - 1 && premierPassage) {
                i = -1;
                a = plateau.length -1;
                premierPassage = false;
            }
            i++;
        }
    }

    public void fermeBordsCotes() {
        int i = 0;
        int a = 0;
        boolean premierPassage = true;
        while(i < plateau.length) {
            if (plateau[i][a].getOuvert()) {
                for (int j = 0; j < plateau.length; j++) {
                    plateau[j][a].setOuvert(false);
                }
                i = -1;
                a = plateau[0].length - 1;
                if (premierPassage == false) break;
                premierPassage = false;
            }
            if (i == plateau.length - 1 && premierPassage) {
                i = -1;
                a = plateau[0].length -1;
                premierPassage = false;
            }
            i++;
        }
    }
    
    private void genereLabyrinthe() { //genere un labyrinthe aleatoire avec l'algorithme de Prim.
        ArrayList<Case> ensembleCaseTraitée = new ArrayList<Case>();
        ArrayList<Case> caseFrontiere = new ArrayList<Case>();
        Case c;
        Random rd = new Random();
        int xCase = rd.nextInt(0, plateau.length);
        int yCase = rd.nextInt(0, plateau.length);
        plateau[xCase][yCase].setOuvert(true);
        ensembleCaseTraitée.add(plateau[xCase][yCase]);
        ajouteCaseFrontiere(caseFrontiere, xCase, yCase);

        while (!caseFrontiere.isEmpty()) {
            c = caseFrontiere.get(rd.nextInt(0, caseFrontiere.size()));
            c.setOuvert(true);
            ajouteCaseFrontiere(caseFrontiere, c.getX(), c.getY());
            caseFrontiere.remove(c);
            ajouteLiensEntreCase(c);
        }
        fermeBordsHautBas();
        fermeBordsCotes();
        verifPoints();
    }

    private void verifPoints() { // place le point de depart et d'arrivée.
        boolean caseTrouvee = false;

        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j].getOuvert()) {
                    joueur.setX(i);
                    joueur.setY(j);
                    pointDepart = plateau[i][j];
                    caseTrouvee = true;
                    break;
                }
            }
            if(multi) {
                for (int j = plateau[i].length -1 ; j >= 0; j--) {
                    if (plateau[i][j].getOuvert()) {
                        joueur2.setX(i);
                        joueur2.setY(j);
                        pointDepart2 = plateau[i][j];
                        break;
                    }
                }
            }
            if (caseTrouvee) break;
        }
        caseTrouvee = false;
        for (int i = plateau.length-1; i >= 0; i--) {
            for (int j = plateau[i].length-1; j >= 0; j--){
                if (plateau[i][j].getOuvert()){
                    pointArrivee = plateau[i][j];
                    caseTrouvee = true;
                    break;
                }
            }
            if(multi){
                for (int j = 0; j < plateau[i].length; j++){
                    if (plateau[i][j].getOuvert()){
                        pointArrivee2 = plateau[i][j];
                        break;
                    }
                }    
            }
            if (caseTrouvee) break;
        }
    }

    public boolean possible(Direction dir , int steps, boolean deuxieme) { //True si le deplacement est possible false sinon.
        int sens = 0; // 
        switch(dir) {
            case HAUT : sens = -1; break;
            case BAS : sens = 1; break;
            case GAUCHE : sens = -1; break;
            default : sens = 1; break;
        }
        if (dir == Direction.HAUT || dir == Direction.BAS) {
            for (int i = 1; i <= steps; i++) {
                if(deuxieme){
                    if (joueur2.getX()+i*sens < 0 || joueur2.getX()+i*sens >= plateau.length || !plateau[joueur2.getX()+i*sens][joueur2.getY()].getOuvert()) {
                        return false;
                    }
                } else {
                    if (joueur.getX()+i*sens < 0 || joueur.getX()+i*sens >= plateau.length || !plateau[joueur.getX()+i*sens][joueur.getY()].getOuvert()) {
                     return false;
                    }
                }
            }
        } else {
            for (int i = 0; i < steps; i++) {
                if(deuxieme){
                    if (joueur2.getY()+i*sens < 0 || joueur2.getY()+i*sens >= plateau[joueur2.getX()].length || !plateau[joueur2.getX()][joueur2.getY()+i*sens].getOuvert()) {
                        return false;
                    }
                } else {
                    if (joueur.getY()+i*sens < 0 || joueur.getY()+i*sens >= plateau[joueur.getX()].length || !plateau[joueur.getX()][joueur.getY()+i*sens].getOuvert()) {
                       return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean endGame1() { // verifie si la partie est terminé par le joueur 1.
        return joueur.getX() == pointArrivee.getX() && joueur.getY() == pointArrivee.getY();
    }

    public boolean endGame2() { // verifie si la partie est terminé par le joueur 2.
        return joueur.getX() == pointArrivee.getX() && joueur.getY() == pointArrivee.getY();
    }

    public void printLabyrinthe() {
        Case c;
        for (int i = 0; i< plateau[0].length+2; i++) {
            System.out.print("▓");
        }
        System.out.println();
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                c = plateau[i][j];
                if (j == 0) System.out.print("▓");
                if (c.getOuvert()) {
                    if (joueur.getX() == i && joueur.getY() == j) {
                        System.out.print("|");
                    } else if (c == pointArrivee) {
                        System.out.print("A");
                    } else if (multi){
                        if (joueur2.getX() == i && joueur2.getY() == j) {
                            System.out.print("x");
                        } else if (c == pointArrivee2) {
                            System.out.print("P");
                        } else {
                            System.out.print(" ");
                        }
                    }else {
                        System.out.print(" ");
                    }
                }else System.out.print("▓");
            }
            System.out.println("▓");
        }
       
        for (int i = 0; i< plateau[0].length+2; i++) {
            System.out.print("▓");
        }
        System.out.println();
    }

}