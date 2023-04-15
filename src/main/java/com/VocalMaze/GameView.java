package com.VocalMaze;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.VocalMaze.ModeleUtils.Direction;


public class GameView extends JPanel implements KeyListener{
    private Controller controller ; 
    private LabyrintheView labyrintheView ;
    private PopUP popUP ; 
    private static final Dimension TAILLE_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();
    private int [] nbLocM_F ; 
    private int timeMs ; 
    private boolean isRecording , isRecordingTime ;
    
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
        /*
         * STEP 2 :
                Faire apparaitre une fenetre qui annonce le nombre de locuteurs trouvés 
                ainsi que le temps de parole pour le prochain enregistrement        
         */
        popUP = new PopUP("- Grand master : Salutations, mes chers aventuriers! Préparez-vous à trembler de terreur. Maintenant, commençons le jeu. Vous devez trouver la sortie avant que je ne vous trouve. Ahahaha... Vous êtes à moi maintenant..Osez-vous relever le défi ? Hahahaha!\n" + 
        "- Jeu : Dans un premier temps , vous devez parler chacun votre tour afin de vous reconnaitre , ainsi gagner du temps de parole . Appuyez sur R pour commencer l'enregistrement , puis appuyez sur S quand vous avez fini !") ;
        add(popUP, BorderLayout.EAST);
        nbLocM_F = new int[2] ;
        nbLocM_F[0] = 0 ; 
        nbLocM_F[1] = 0 ; 
        timeMs = -1 ; 
        addKeyListener(this) ;    
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch(e.getKeyChar()) {
            case 'r' : {
                if (isRecording) break ; 
                if (timeMs == -1) {
                    controller.startRecord();
                    isRecording = true ;
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
                    Direction [] directions = controller.transcrire() ; 
                    boolean fin = controller.play(directions) ; 
                    if (fin) {
                        endGame();
                        return ; 
                    }
                    //TODO
                    // faire apparaitre step1
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
    public void keyPressed(KeyEvent e) {
        return ; 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        return ; 
    }
    
    public void endGame () {
        //TODO qui fera apparaire l'ecran de fin du jeu
    }

    public void movePlayer (Direction dir , int steps) {
        labyrintheView.movePlayer(dir, steps);
    }

    public class PopUP extends JPanel {
        PopUP(String message) {
            InfoTextArea infoTextArea = new InfoTextArea(message);
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            add(infoTextArea);
            add(Box.createVerticalGlue());
        }

        private class InfoTextArea extends JTextArea {
            private final String fullText;
            private int textIndex = 0;
            private ImageIcon imageIcon;

            public InfoTextArea(String text) {
                super("");
                fullText = text;
                setEditable(false);
                setOpaque(false);

                try {
                    File fontFile = new File("src/main/java/com/VocalMaze/Audio&Visuel/Font-police/PressStart2P-Regular.ttf");
                    Font pressStart2P = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);
                    setFont(pressStart2P);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }

                setForeground(new Color(255, 255, 255)); // Set the text color to white
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                setPreferredSize(new Dimension(500, 300));
                setLineWrap(true);
                setWrapStyleWord(true);

                // Load the image
                imageIcon = new ImageIcon("src/main/java/com/VocalMaze/Audio&Visuel/ImagesTextBox/IMG_4182 (1) (1).jpg");

                // Create the timer for the typing effect
                int delay = 50; //milliseconds
                ActionListener taskPerformer2 = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (textIndex < fullText.length()) {
                            setText(fullText.substring(0, textIndex));
                            textIndex++;
                        } else {
                            ((Timer) evt.getSource()).stop();
                        }
                    }
                };
                new Timer(delay, taskPerformer2).start();

                // Listen for size changes
                addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                if (imageIcon != null) {
                    Image image = imageIcon.getImage();
                    int width = this.getWidth();
                    int height = this.getHeight();
                    int imgWidth = imageIcon.getIconWidth();
                    int imgHeight = imageIcon.getIconHeight();
                    double widthRatio = (double) width / imgWidth;
                    double heightRatio = (double) height / imgHeight;
                    double ratio = Math.min(widthRatio, heightRatio);

                    int newWidth = (int) (imgWidth * ratio);
                    int newHeight = (int) (imgHeight * ratio);

                    g.drawImage(image, 0, 0, newWidth, newHeight, this);
                }
                super.paintComponent(g);
            }
        }
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
       // private int pourcentTailleEcranX, pourcentTailleEcranY; 

        public LabyrintheView () throws IOException{
            setPreferredSize(TAILLE_ECRAN);
            imageSprite = ImageIO.read(new File("src/main/java/com/VocalMaze/Images/professor_walk_cycle_no_hat.png"));
            sprites = new BufferedImage[4][9];
            imagePorte = ImageIO.read(new File("src/main/java/com/VocalMaze/Images/doors.png"));
            imagePassage = ImageIO.read(new File("src/main/java/com/VocalMaze/Images/M484ShmupTileset1.png"));
            porteLabyrinthe = new BufferedImage[23][29];
            caseX = 0;
            caseY = 0;
            currentFrame = 0 ; 
            lastTime = 0 ; 
            dirAnim = Direction.BAS ; 
            enDeplacement = false ; 
            // pourcentTailleEcranX = (int) (2.18 * TAILLE_ECRAN.getWidth()/100);
            // pourcentTailleEcranY = (int) (3.91 * TAILLE_ECRAN.getHeight()/100);
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
                        porteLabyrinthe[i][j] = imagePorte.getSubimage(100, 70, 30, 30);
                    }
                    else if (controller.getOuvert(i, j)) {
                        porteLabyrinthe[i][j] = imagePassage.getSubimage(1000, 70, 30, 30);
                    }else {
                        porteLabyrinthe[i][j] = imagePorte.getSubimage(0, 0, 30, 30);
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
                    g.drawImage(porteLabyrinthe[i][j], j * 30 , i * 30, null);
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
    JFrame frame = new JFrame() ; 
    frame.getContentPane().setLayout(null);
    frame.setPreferredSize(TAILLE_ECRAN);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GameView gameView = new GameView("test" , 2 , 2);
    frame.add(gameView);
    frame.addKeyListener(gameView); // important
    frame.pack();
    frame.setFocusable(true);// tres tres important 
    frame.setVisible(true);
    gameView.movePlayer(Direction.BAS, 5);

    gameView.movePlayer(Direction.DROITE, 5);

    gameView.movePlayer(Direction.DROITE, 5);

    gameView.movePlayer(Direction.BAS , 10);
    gameView.controller.getGameModel().getLabyrinthe().printLabyrinthe();
    System.out.println(TAILLE_ECRAN);

  }

}
