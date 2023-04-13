package com.VocalMaze;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.VocalMaze.ModeleUtils.AudioAnalyser;
import com.VocalMaze.ModeleUtils.Direction;

public class GameView extends JPanel{
    private Controller controller ; 
    private LabyrintheView labyrintheView ;
    private static final Dimension TAILLE_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();
    private int [] nbLocM_F ; 
    private int timeMs ; 
    private boolean isRecording , isRecordingTime ;
    private InfoTextArea infoTextArea;
    private class PopUp1 {
        public PopUp1() {
            showPopUp();
        }
        private void showPopUp() {
            JDialog instructionsDialog = new JDialog();
            instructionsDialog.setTitle("Instructions");
            instructionsDialog.setSize(300, 100);
            instructionsDialog.setLocationRelativeTo(null);
            instructionsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            JLabel instructionsLabel = new JLabel("Press R to start recording, and S to stop.", SwingConstants.CENTER);
            instructionsDialog.add(instructionsLabel);
            // TODO : A MODIFIER QUAND ON LANCE LE JEU APRES LE MENU SETTING

            Timer launchDialogTimer = new Timer(6000, e -> {
                instructionsDialog.setVisible(true);

                // ca se ferme tout seul apres 5 secondes
                Timer closeDialogTimer = new Timer(5000, e1 -> instructionsDialog.dispose());
                closeDialogTimer.setRepeats(false);
                closeDialogTimer.start();
            });
            launchDialogTimer.setRepeats(false);
            launchDialogTimer.start();
        }
    }
    //Pop up fenetre avec le nbr de joueur classe interne
//    private class PopUp2 {
//        public PopUp2() {
//            showPopUp();
//        }
//
//        private void showPopUp() {
//            AudioAnalyser audioAnalyser = new AudioAnalyser();
//            int[] maleFemaleCounts = audioAnalyser.analyse2();
//            String message = "Hello, I have found that you guys are " + maleFemaleCounts[1] + " males and " + maleFemaleCounts[0] + " females.";
//
//            JOptionPane.showMessageDialog(null, message, "Males and Females", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
    private class InfoTextArea extends JTextArea {
        private final String fullText;
        private int textIndex = 0;

        public InfoTextArea(String text) {
            super("");
            fullText = text;
            setEditable(false);
            setOpaque(true);
            setFont(new Font("Arial", Font.PLAIN, 14));
            setForeground(Color.BLACK);
            setPreferredSize(new Dimension(200, 80)); // Set the preferred size of the text area
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            )); // Add a border to make it look like a chat dialog
            setAlignmentX(Component.RIGHT_ALIGNMENT); // Align the text area to the right
            setAlignmentY(Component.TOP_ALIGNMENT); // Align the text area to the top
            setPreferredSize(new Dimension(200, 200));
            setMaximumSize(new Dimension(200, 200));
            setLineWrap(true);
            setWrapStyleWord(true);

            // Create the timer for the typing effect
            int delay = 50; //milliseconds
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (textIndex < fullText.length()) {
                        append(String.valueOf(fullText.charAt(textIndex)));
                        textIndex++;
                    } else {
                        ((Timer) evt.getSource()).stop();
                    }
                }
            };
            new Timer(delay, taskPerformer).start();
        }
    }

    public GameView(String pseudo , int nbMaleTotal , int nbFemelleTotal) throws IOException {
        setSize(TAILLE_ECRAN);
        setLayout(new BorderLayout());
        controller = new Controller(new GameModel(pseudo, nbMaleTotal, nbFemelleTotal), this) ; 
        labyrintheView = new LabyrintheView() ; 
        labyrintheView.decoupeImage();
        //labyrintheView.setLocation(250, 100);
        add(labyrintheView) ; 
        labyrintheView.setVisible(true);
        setVisible(true);

        // STEP 1 class interne
        /*
         * TODO 
         * il y aura deux STEPS
         * STEP 1 :
                La premiere par defaut quand le jeu se lance , il y aura une fenetre qui doit dire au joueur ce qu'il doit faire
                appuyer sur R pour commencer le record , afin de determiner le temps de parole du 2eme enregistrement 
                en gros c'est un truc comme ça
         */
        //STEP 1 TEST
        new PopUp1();
        /*
         * STEP 2 :
                Faire apparaitre une fenetre qui annonce le nombre de locuteurs trouvés 
                ainsi que le temps de parole pour le prochain enregistrement        
         */
        AudioAnalyser audioAnalyser = new AudioAnalyser();
        int[] maleFemaleCounts = audioAnalyser.analyse2();
        String message = "Salutations, mes chers aventuriers ! Préparez-vous à trembler de terreur, car je vois que votre équipe est composée de  " + maleFemaleCounts[1] + (maleFemaleCounts[1] < 2 ? " homme" : " hommes") + " et " + maleFemaleCounts[0] + (maleFemaleCounts[0] < 2 ? " femme " : " femmes") + "Maintenant, commençons le jeu. Vous devez trouver la sortie avant que je ne vous trouve. Ahahaha... Vous êtes à moi maintenant..Osez-vous relever le défi ? Hahahaha !";
        infoTextArea = new InfoTextArea(message);
        infoTextArea.setMaximumSize(new Dimension(350, 50));
        infoTextArea.setBackground(Color.RED);

// Create a wrapper panel for the InfoTextArea
        JPanel infoWrapper = new JPanel();
        infoWrapper.setLayout(new BoxLayout(infoWrapper, BoxLayout.PAGE_AXIS));
        infoWrapper.add(infoTextArea);
        infoWrapper.add(Box.createVerticalGlue());

        add(infoWrapper, BorderLayout.EAST);

        // Par defaut il y aura STEP 1 ici dans le constructeur
        nbLocM_F = new int[2] ;
        nbLocM_F[0] = 0 ; 
        nbLocM_F[1] = 0 ; 
        timeMs = -1 ; 
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("testttt");
                switch(e.getKeyChar()) {
                    case 'r' : {
                        if (isRecording) break ; 
                        if (timeMs == -1) {
                            controller.startRecord();
                        }else{
                            controller.startRecord(timeMs);
                            isRecordingTime = true ; 
                            try {
                                Thread.sleep(timeMs);
                                isRecordingTime = false ; 
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            // Faire apparaitre un petit sablier qui tourne (un gif) qui dit transcription en cours
                            //  si besoin mais c'est optionnel , ou bien des petites images qui donnent des conseils
                            // comme dans les menus de chargement des jeux a voir 
                            boolean fin = controller.transcrireAndPlay() ; 
                            if (fin) {
                                endGame();
                                return ; 
                            }
                            //TODO
                            //Faire apparaitre STEP 1
                            timeMs = -1 ; 
                        }
                        break ; 
                    }
                
                    case 's' :{
                        if (!isRecording || isRecordingTime) break ;
                        if (timeMs == -1) {
                            controller.stopRecord();
                            //Analyse du vocal
                            nbLocM_F = controller.analyse2() ; 
                            //Entre 5 et 7.5 par Homme, et entre 6 et 9 par Femme
                            timeMs = nbLocM_F[0]*(5000+(new Random()).nextInt(2501)) + nbLocM_F[1]*(6000+(new Random()).nextInt(3001)) ; 
                            // TODO faire apparaitre STEP 2
                        }
                        break ; 
                    }
                    
                    default : break ; 
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
            
            @Override
            public void keyTyped(KeyEvent e) {
                
            }
        });
        
    }

    public void endGame () {
        //TODO qui fera apparaire l'ecran de fin du jeu
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
                    if (controller.getGameModel().getLabyrinthe().estPointArrivee(i, j)) {
                        porteLabyrinthe[i][j] = imagePorte.getSubimage(100, 70, 20, 20);
                    }
                    else if (controller.getOuvert(j, i)) {
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
