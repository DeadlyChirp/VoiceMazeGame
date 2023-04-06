package com.VocalMaze.Menus;
import java.awt.*;
import java.awt.event.MouseEvent;

import com.VocalMaze.ViewUtils.ImagePanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class StartMenu extends JFrame{

    private  JButton play ; 
    private JButton quitter;
    private  JButton mute;
    static boolean sonON;
    static GraphicsDevice device ;
	File file = new File("src/main/java/com/VocalMaze/Sounds/Lost Voice OP - BigJay.wav");
    public static File file2 = new File("chemin a renseigné"); //Chemin de la musique en plein jeu
    ImagePanel background = new ImagePanel("src/main/java/com/VocalMaze/Images/Menu Lost Voice.png");
    AudioInputStream ais = AudioSystem.getAudioInputStream(file);
    public static AudioInputStream game;
    private static final Dimension TAILLE_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();


    public static Clip clip;

    public StartMenu() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        super();
        
        //Partie Audio
        clip = AudioSystem.getClip();
        if(clip.isActive() || clip.isRunning()){
            clip.stop();
            sonON = true;
        }else{
            clip.open(ais);
            sonON = false;
        }

        //Init de la Frame
        setTitle("Lost Voice");
        getContentPane().setLayout(null);

        //Init du plein écran
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(this);
        } else {
            System.err.println("Le mode plein ecran n'est pas disponible");
        }

        //Début du son
        clip.start();

        //Init des boutons
        ImageIcon p = new ImageIcon("src/main/java/com/VocalMaze/Images/play.png");
        // ImageIcon q = new ImageIcon("Pictures/quitter.png");
        ImageIcon m = new ImageIcon("src/main/java/com/VocalMaze/Images/mute1.png");

        //Init de play
        play = new JButton(p);
        play.setBorderPainted(false);
        play.setBackground(new Color(0, 0, 0, 0));
        // quitter = new JButton(q);

        //Init de Mute
        mute = new JButton(m);
        mute.setBorderPainted(false);
        mute.setBackground(new Color(0, 0, 0, 0));

        play.setBounds(870, 661, p.getIconWidth(), p.getIconHeight());
        // quitter.setBounds(405, 627, q.getIconWidth(), q.getIconHeight());
        mute.setBounds(1000, 50, m.getIconWidth(), m.getIconHeight());
        // add(quitter);

        //Init du background
        background.setSize(TAILLE_ECRAN);
        add(mute);
        add(play);
        add(background);
        

        pack();
        //init de la Frame
        setVisible(true);

    

        //Init de la Frame 2
        setActionButtons();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

    }


    public void setActionButtons(){
        play.addActionListener(ev -> {
            this.dispose();
            // try {
            //     @SuppressWarnings("unused")
            //     //MenuInterne a = new MenuInterne(background, sonON, mute);
            // }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            //     e.printStackTrace();
            // }
        });

        mute.addActionListener(ev ->{
			ImageIcon icon2;
			if (sonON == true) {
				icon2 = new ImageIcon("src/main/java/com/VocalMaze/Images/mute1.png");
				clip.start();
				sonON = false;
			}else {
				icon2 = new ImageIcon("src/main/java/com/VocalMaze/Images/mute2.png");
				clip.stop();
				sonON = true;
			}
			mute.setIcon(icon2);
		});

        quitter.addActionListener(ev->{
            System.exit(0);
        });
    }
    
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        @SuppressWarnings("unused")
        StartMenu background = new StartMenu();
    }
}
