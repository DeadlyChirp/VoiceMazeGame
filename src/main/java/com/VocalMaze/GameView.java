package com.VocalMaze;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


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
    private BufferedImage[][] sprites;
    private BufferedImage[][] porteLabyrinthe;
    private int currentFrame = 0;
    private long lastTime = 0;
    BufferedImage imagePorte;
    BufferedImage imagePassage;
    BufferedImage imageSprite;
  

    public GameView() throws IOException {
        imageSprite = ImageIO.read(new File("/home/ismael/Téléchargements/professor_walk_cycle_no_hat.png"));
        sprites = new BufferedImage[4][9];
        imagePorte = ImageIO.read(new File("/home/ismael/Téléchargements/doors.png"));
        imagePassage = ImageIO.read(new File("/home/ismael/Téléchargements/M484ShmupTileset1.png"));
        porteLabyrinthe = new BufferedImage[30][30];
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
      update();
      repaint();
      try {
        Thread.sleep(30);
      } catch (InterruptedException ex) {
      }
    }
  }
  
  private void update() {
    long currentTime = System.currentTimeMillis();
    if (currentTime - lastTime > 100) {
      currentFrame++;
      if (currentFrame >= sprites[3].length) {
        currentFrame = 0;
      }
      lastTime = currentTime;
    }
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (int i = 0; i < porteLabyrinthe.length; i++) {
        for (int j = 0; j < porteLabyrinthe[i].length; j++) {
            g.drawImage(porteLabyrinthe[i][j], 20+i*20, 20+j*20, null);
        }
    }
    g.drawImage(sprites[3][currentFrame], currentFrame*3, 0, null);
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
  }
}
