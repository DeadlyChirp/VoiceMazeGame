package com.VocalMaze;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.stream.events.EndDocument;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.VocalMaze.ModeleUtils.Direction;
import com.VocalMaze.ModeleUtils.Joueur;
import com.VocalMaze.ModeleUtils.Labyrinthe;
import com.VocalMaze.ViewUtils.LabyrintheView;
import com.VocalMaze.ViewUtils.SlideBar;

public class GameView extends JPanel implements Runnable  {
    private Controller controller;
    private Labyrinthe labyrinthe; //Vue pour le labyrinth
    private SlideBar slidebar; //Vue pour les mouvements effectuées precedemment
    private Joueur joueurs;
    private JButton mute; //Poru le son
    private boolean sonON = false; //Pour le son    
    private boolean enDeplacement = true;
    private BufferedImage[][] sprites;
    private BufferedImage[][] porteLabyrinthe;
    private int currentFrame = 0;
    private long lastTime = 0;
    private BufferedImage imagePorte;
    private BufferedImage imagePassage;
    private BufferedImage imageSprite;
    private Direction dirAnim;
    private int caseX, ancienCaseX;
    private int caseY, ancienCaseY;
    private int stepsAnim;
  

    public GameView() throws IOException {
        imageSprite = ImageIO.read(new File("/home/ismael/Cours/L2/S4/PI4/2022-sb2-g2-lost_voices/src/main/java/com/VocalMaze/Images/professor_walk_cycle_no_hat.png"));
        sprites = new BufferedImage[4][9];
        imagePorte = ImageIO.read(new File("/home/ismael/Cours/L2/S4/PI4/2022-sb2-g2-lost_voices/src/main/java/com/VocalMaze/Images/doors.png"));
        imagePassage = ImageIO.read(new File("/home/ismael/Cours/L2/S4/PI4/2022-sb2-g2-lost_voices/src/main/java/com/VocalMaze/Images/M484ShmupTileset1.png"));
        porteLabyrinthe = new BufferedImage[30][30];
        caseX = 0;
        caseY = 0;
    }  

   public void decoupeImage() {
    for (int i = 0; i < sprites.length; i++) {
      for (int j = 0; j < sprites[i].length; j++) {
          sprites[i][j] = imageSprite.getSubimage(j*64, i*64, 64, 64);
      }
    }

    for (int i = 0; i < porteLabyrinthe.length; i++) {
        for (int j = 0; j < porteLabyrinthe[i].length; j++) {
            if (controller.getGameModel().getLabyrinthe().getPlateau()[i][j].getOuvert()) {
                porteLabyrinthe[i][j] = imagePassage.getSubimage(1000, 70, 20, 20);
            } else {
                porteLabyrinthe[i][j] = imagePorte.getSubimage(0, 0, 20, 20);
            }
        }
    }
  }




  public void run() {
    while (true) {
      if (enDeplacement && dirAnim != null) {
        update();
        repaint();
      } else {
        movePlayer(Direction.BAS, 5); //ecrire une méthode qui attend la prochaine instruction.
      }
      try {
        Thread.sleep(50);
      } catch (InterruptedException ex) {
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

  public static void main(String[] args) throws IOException {
    JFrame frame = new JFrame();
    frame.setPreferredSize(new Dimension(800, 800));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GameView gameView = new GameView();
    gameView.controller = new Controller(new GameModel("test", 2, 2), gameView);
    gameView.decoupeImage();
    frame.add(gameView);
    frame.pack();
    frame.setVisible(true);
    Thread thread = new Thread(gameView);
    thread.start();
    System.out.println("aaa");
  }
}
