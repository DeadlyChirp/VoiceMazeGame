package com.VocalMaze;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.VocalMaze.ModeleUtils.Joueur;
import com.VocalMaze.ModeleUtils.Labyrinthe;

public class GameView extends JPanel{
    private Controller controller;
    private LabyrintheView labyrintheV; //Vue pour le labyrinth
    private SlideBar slidebar; //Vue pour les mouvements effectuées precedemment
    private Joueur[] joueurs;
    private JButton mute; //Poru le son
    private boolean sonON = false; //Pour le son    


    public GameView(){
        
    }
}
