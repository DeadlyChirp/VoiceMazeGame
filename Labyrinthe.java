import java.util.ArrayList;
import java.util.Random;

public class Labyrinthe {
    
    private Case[][] plateau;

    Labyrinthe() {
        plateau = new Case[20][20];
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                plateau[i][j] = new Case(i, j);
            }
        }
    }

    // public void ajouteCaseFrontiere(ArrayList<Case> caseFrontiere, ArrayList<Case> ensembleCaseTraitée, int x, int y) {
    //     for (int i = -2; i <= 2; i++) {
    //         if (i+x < plateau.length && i+x >= 0 && !ensembleCaseTraitée.contains(plateau[i+x][y])) {
    //             caseFrontiere.add(plateau[i+x][y]);

    //         } 
    //         if (i+y < plateau.length && i+y >= 0  && !ensembleCaseTraitée.contains(plateau[x][i+y])) {
    //             caseFrontiere.add(plateau[x][i+y]);
    //         }
    //     }
    // }

    public void ajouteCaseFrontiere(ArrayList<Case> caseFrontiere, int x, int y) {
        for (int i = -2; i <= 2; i +=4) {
            if (i+x < plateau.length && i+x >= 0 && !plateau[i+x][y].getOuvert()) {
                caseFrontiere.add(plateau[i+x][y]);
            } 
            if (i+y < plateau.length && i+y >= 0  && !plateau[x][i+y].getOuvert()) {
                caseFrontiere.add(plateau[x][i+y]);
            }
        }
    }

    // public void ajouteLiensEntreCase(ArrayList<Case> ensembleCaseTraitée, Case c, int x, int y) {
    //     for (int i = -1; i <= 1; i++) {
    //         if ( i+x < plateau.length && i+x >= 0 && ensembleCaseTraitée.contains(plateau[i+x][y])) {
    //             if (i == -1) c.setHaut(true);
    //             if (i == 1) c.setBas(true);
    //         }
    //         if (i+y < plateau.length && i+y >= 0  && ensembleCaseTraitée.contains(plateau[x][i+y])) {
    //             if (i == -1) c.setGauche(true);
    //             if (i == 1) c.setDroite(true);
    //         }

    //     }
    // }

    public ArrayList<Case> voisin(Case c) {
        ArrayList<Case> voisin = new ArrayList<>();
        for (int i = -2; i <= 2; i += 4) {
            if ( i+c.getX() < plateau.length && i+c.getX() >= 0 && plateau[i+c.getX()][c.getY()].getOuvert()) {
                voisin.add(plateau[i+c.getX()][c.getY()]);
            } else if (i+c.getY() < plateau.length && i+c.getY() >= 0 && plateau[c.getX()][i+c.getY()].getOuvert()) {
                voisin.add(plateau[c.getX()][i+c.getY()]);
            }
        }
        return voisin;
    }

    public void ajouteLiensEntreCase(Case c) {
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
        
        // for (int i = -2; i <= 2; i += 4) {
        //     if ( i+x < plateau.length && i+x >= 0 && ensembleCaseTraitée.contains(plateau[i+x][y])) {
        //         if (i == -2) {
        //           //  caseFrontiere.remove(plateau[i+x][y]);
        //           //  ensembleCaseTraitée.add(plateau[i+x][y]);
        //            // caseFrontiere.remove(plateau[i+x+1][y]);
        //            // ensembleCaseTraitée.add(plateau[i+x+1][y]);
        //             plateau[i+x+1][y].setOuvert(true);
        //         } else {
        //            // caseFrontiere.remove(plateau[i+x][y]);
        //          //   ensembleCaseTraitée.add(plateau[i+x][y]);
        //             //caseFrontiere.remove(plateau[i+x-1][y]);
        //           //  ensembleCaseTraitée.add(plateau[i+x-1][y]);
        //             plateau[i+x-1][y].setOuvert(true);
        //         }
        //         plateau[i+x][y].setOuvert(true);
        //     }
        //     if (i+y < plateau.length && i+y >= 0  && ensembleCaseTraitée.contains(plateau[x][i+y])) {
        //         if (i == -2) {
        //             //caseFrontiere.remove(plateau[x][i+y]);
        //            // ensembleCaseTraitée.add(plateau[x][i+y]);
        //            // caseFrontiere.remove(plateau[x][i+y+1]);
        //           //  ensembleCaseTraitée.add(plateau[x][i+y+1]);
        //             plateau[x][i+y+1].setOuvert(true);
        //         } else {
        //             //caseFrontiere.remove(plateau[x][i+y]);
        //            // ensembleCaseTraitée.add(plateau[x][i+y]);
        //           //  caseFrontiere.remove(plateau[x][i+y-1]);
        //            // ensembleCaseTraitée.add(plateau[x][i+y-1]);
        //             plateau[x][i+y-1].setOuvert(true);
        //         }
        //         plateau[x][i+y].setOuvert(true);
        //     }

        // }
    }

    // public void genereLabyrinthe() {
    //     ArrayList<Case> ensembleCaseTraitée = new ArrayList<>();
    //     ArrayList<Case> caseFrontiere = new ArrayList<>();
    //     Case c;
    //     Random rd = new Random();
    //     int xCase = rd.nextInt(0, plateau.length);
    //     int yCase = rd.nextInt(0, plateau.length);
    //     ensembleCaseTraitée.add(plateau[xCase][yCase]);
    //     ajouteCaseFrontiere(caseFrontiere, ensembleCaseTraitée, xCase, yCase);

    //     while (ensembleCaseTraitée.size() != plateau.length*plateau.length) {
    //         c = caseFrontiere.get(rd.nextInt(0, caseFrontiere.size()));
    //         caseFrontiere.remove(c);
    //         ensembleCaseTraitée.add(c);
    //         System.out.println(ensembleCaseTraitée.size());
    //         ajouteLiensEntreCase(ensembleCaseTraitée, c, c.getX(), c.getY());
    //         ajouteCaseFrontiere(caseFrontiere, ensembleCaseTraitée, c.getX(), c.getY());
    //     }
    // }

    public void genereLabyrinthe() {
        ArrayList<Case> ensembleCaseTraitée = new ArrayList<>();
        ArrayList<Case> caseFrontiere = new ArrayList<>();
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
    }

    public void printLabyrinthe() {
        Case c;
        for (int i = 0; i< plateau.length+2; i++) {
            System.out.print("▓");
        }

        System.out.println();
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                c = plateau[i][j];
               if (j == 0) System.out.print("▓");
                if (c.getOuvert()) System.out.print(" ");
                else System.out.print("▓");
            }
            System.out.println("▓");
        }
       
        for (int i = 0; i< plateau.length+2; i++) {
            System.out.print("▓");
        }

        System.out.println();

    }

    public static void main(String[] args) {
        Labyrinthe l = new Labyrinthe();
        l.genereLabyrinthe();
        l.printLabyrinthe();
    }


}
