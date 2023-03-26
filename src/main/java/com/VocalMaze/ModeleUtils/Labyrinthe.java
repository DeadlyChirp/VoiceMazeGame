package com.VocalMaze.ModeleUtils;
import java.util.ArrayList;
import java.util.Random;

public class Labyrinthe {
    
    private Case[][] plateau;
    private Joueur joueur;
    private Case pointArrivee;


    public Labyrinthe(Joueur joueur) {
        this.joueur = joueur;
        plateau = new Case[30][30];
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
        
        verifPoints();
    }

    private void verifPoints() { // place le point de depart et d'arrivée.
        boolean caseTrouvee = false;

        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j].getOuvert()) {
                    joueur.setX(i);
                    joueur.setY(j);
                    caseTrouvee = true;
                    break;
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
            if (caseTrouvee) break;
        }
    }

    public boolean possible(Direction dir , int steps) { //True si le deplacement est possible false sinon.
        int sens = 0; // 
        switch(dir) {
            case HAUT : sens = -1; break;
            case BAS : sens = 1; break;
            case GAUCHE : sens = -1; break;
            default : sens = 1; break;
        }
        if (dir == Direction.HAUT || dir == Direction.BAS) {
            for (int i = 0; i < steps; i++) {
                if (joueur.getX()+sens < 0 || joueur.getX()+sens >= plateau.length || !plateau[joueur.getX()+sens][joueur.getY()].getOuvert()) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < steps; i++) {
                if (joueur.getY()+sens < 0 || joueur.getY()+sens >= plateau[joueur.getX()].length || !plateau[joueur.getX()][joueur.getY()+sens].getOuvert()) {
                    return false;
                }
            }
        }
        return true;
    }

    // void possible2(Direction dir , int steps) {
    //     int cpt = 0;
    //     switch(dir) {
    //         case HAUT : for(int i = joueur.getX()-1; i>=0;i--) {
    //             if(plateau[i][joueur.getY()].getOuvert()) {
    //                 cpt++;
    //             } else { 
    //                 break;
    //             }
    //         }
    //             break;
    //         case BAS : for(int i = joueur.getX()+1; i<plateau.length;i++) {
    //             if(plateau[i][joueur.getY()].getOuvert()) {
    //                 cpt++;
    //             } else { 
    //                 break;
    //             }
    //         }
    //             break;
    //         case GAUCHE : for(int i = joueur.getY()-1; i>=0;i--) {
    //             if(plateau[joueur.getX()][i].getOuvert()) {
    //                 cpt++;
    //             } else { 
    //                 break;
    //             }
    //         }
    //             break;
    //         default : for(int i = joueur.getY()+1; i<plateau[joueur.getX()].length;i++) {
    //             if(plateau[i][joueur.getX()].getOuvert()) {
    //                 cpt++;
    //             } else { 
    //                 break;
    //             }
    //         }
    //             break;
    //     }
    //     if(cpt >= steps){
    //         joueur.move(dir, steps);
    //     } else {
    //         joueur.move(dir, cpt);
    //     }
    // }

    public boolean endGame() { // verifie si la partie est terminé.
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
                     } else {
                        System.out.print(" ");
                     }
                } else System.out.print("▓");
            }
            System.out.println("▓");
        }
       
        for (int i = 0; i< plateau[0].length+2; i++) {
            System.out.print("▓");
        }

        System.out.println();

    }

    public static void main(String[] args) {
        Joueur j = new Joueur("Bawww");
        Labyrinthe l = new Labyrinthe(j);
        l.genereLabyrinthe();
        l.printLabyrinthe();
        System.out.println(l.joueur.getX() + " " + l.joueur.getY());
        // l.possible2(Direction.BAS, 90);
        // l.printLabyrinthe();
        //System.out.println(l.joueur.getX() + " " + l.joueur.getY());
    }


}
