package com.VocalMaze;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.VocalMaze.ModeleUtils.Direction;

public class GameView extends JPanel{
    private Controller controller ; 
    private LabyrintheView labyrintheView ; 
    private static final Dimension TAILLE_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();
    
    public GameView(String pseudo , int nbMaleTotal , int nbFemelleTotal) throws IOException {
        setSize(TAILLE_ECRAN);
        controller = new Controller(new GameModel(pseudo, nbMaleTotal, nbFemelleTotal), this) ; 
        labyrintheView = new LabyrintheView() ; 
        labyrintheView.decoupeImage();
        //labyrintheView.setLocation(250, 100);
        add(labyrintheView) ; 
        labyrintheView.setVisible(true);
        setVisible(true);
        
    }  

    public void movePlayer (Direction dir , int steps) {
        labyrintheView.movePlayer(dir, steps);
    }

    private class LabyrintheView extends JPanel {
        private boolean enDeplacement ;
        private BufferedImage[][] sprites;
        private BufferedImage[][] porteLabyrinthe;
        private int currentFrame ;
        private long lastTime ;
        private BufferedImage imagePorte;
        private BufferedImage imagePassage;
        private BufferedImage imageSprite;
        private Direction dirAnim ;
        private int caseX, ancienCaseX;
        private int caseY, ancienCaseY;
        private int stepsAnim;

        public LabyrintheView () throws IOException{
            setPreferredSize(TAILLE_ECRAN);
            imageSprite = ImageIO.read(new File("src/main/java/com/VocalMaze/Images/professor_walk_cycle_no_hat.png"));
            sprites = new BufferedImage[4][9];
            imagePorte = ImageIO.read(new File("src/main/java/com/VocalMaze/Images/doors.png"));
            imagePassage = ImageIO.read(new File("src/main/java/com/VocalMaze/Images/M484ShmupTileset1.png"));
            porteLabyrinthe = new BufferedImage[30][30];
            caseX = 0;
            caseY = 0;
            currentFrame = 0 ; 
            lastTime = 0 ; 
            dirAnim = Direction.BAS ; 
            enDeplacement = false ; 
        }
        
        private void decoupeImage() {
            for (int i = 0; i < sprites.length; i++) {
                for (int j = 0; j < sprites[i].length; j++) {
                    sprites[i][j] = imageSprite.getSubimage(j*64, i*64, 64, 64);
                }
            }

    
            for (int i = 0; i < porteLabyrinthe.length; i++) {
                for (int j = 0; j < porteLabyrinthe[i].length; j++) {
                    if (controller.getOuvert(j, i)) {
                        porteLabyrinthe[i][j] = imagePassage.getSubimage(1000, 70, 20, 20);
                    }else {
                        porteLabyrinthe[i][j] = imagePorte.getSubimage(0, 0, 20, 20);
                    }
                }
            }
      }
    
        private void animateMovement () {
            while(true) {
                if (enDeplacement) {
                    update();
                    repaint();
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    break ; 
                }
            }
        }
    
        private void update() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > 500) {
                if (enDeplacement) currentFrame++;
                if (currentFrame >= sprites[3].length) {
                    switch(dirAnim) {
                        case DROITE : {
                            caseX += currentFrame * stepsAnim;
                            if (caseX + currentFrame * stepsAnim >= ancienCaseX + 20 * stepsAnim) enDeplacement = false;
                            break;
                        }
    
                        case GAUCHE : {
                            caseX -= currentFrame * stepsAnim;
                            if (caseX - currentFrame * stepsAnim <= ancienCaseX - 20 * stepsAnim) enDeplacement = false;
                            break;
                        }
    
                        case BAS : {
                            caseY += currentFrame * stepsAnim;
                            if (caseY + currentFrame * stepsAnim >= ancienCaseY + 20 * stepsAnim) enDeplacement = false;
                            break;
                        }
    
                        case HAUT : {
                            caseY -= currentFrame * stepsAnim;
                            if (caseY - currentFrame * stepsAnim <= ancienCaseY - 20 * stepsAnim) enDeplacement = false;
                            break;
                        }
                    }
                    currentFrame = 0;
                    lastTime = currentTime;
                }
            }
        }
    
        public void movePlayer(Direction dir, int steps) {
            enDeplacement = true;
            dirAnim = dir;
            stepsAnim = steps;
            ancienCaseX = caseX;
            ancienCaseY = caseY;
            animateMovement();
        }
      
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < porteLabyrinthe.length; i++) {
                for (int j = 0; j < porteLabyrinthe[i].length; j++) {
                    g.drawImage(porteLabyrinthe[i][j], 20+i*20, 40+j*20, null);
                }
            }
            switch(dirAnim) {
            case DROITE : {
                if (enDeplacement) g.drawImage(sprites[dirAnim.ordinal()][currentFrame], caseX + currentFrame * stepsAnim, caseY, null);
                else g.drawImage(sprites[dirAnim.ordinal()][0], caseX + currentFrame * stepsAnim, caseY, null);
                break;
            }
            case GAUCHE : {
                if (enDeplacement) g.drawImage(sprites[dirAnim.ordinal()][currentFrame], caseX - currentFrame * stepsAnim, caseY, null);
                else g.drawImage(sprites[dirAnim.ordinal()][0], caseX - currentFrame * stepsAnim, caseY, null);
                break;
            }
            case HAUT : {
                if (enDeplacement) g.drawImage(sprites[dirAnim.ordinal()][currentFrame], caseX, caseY - currentFrame * stepsAnim, null);
                else g.drawImage(sprites[dirAnim.ordinal()][0], caseX + currentFrame*2, caseY - currentFrame * stepsAnim, null);
                break;
            }
            case BAS : {
                if (enDeplacement) g.drawImage(sprites[dirAnim.ordinal()][currentFrame], caseX, caseY + currentFrame * stepsAnim, null);
                else g.drawImage(sprites[dirAnim.ordinal()][0], caseX + currentFrame*2, caseY + currentFrame * stepsAnim, null);
                break;
            }
            }
        }
    }

  public static void main(String[] args) throws IOException {
    JFrame frame = new JFrame();
    frame.getContentPane().setLayout(null);
    frame.setPreferredSize(TAILLE_ECRAN);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GameView gameView = new GameView("test" , 2 , 2);
    frame.add(gameView);
    frame.pack();
    frame.setVisible(true);
    gameView.movePlayer(Direction.BAS, 5);

    gameView.movePlayer(Direction.DROITE, 5);

    gameView.movePlayer(Direction.DROITE, 5);

    gameView.movePlayer(Direction.BAS , 10);

  }
}
