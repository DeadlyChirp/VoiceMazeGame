package com.VocalMaze;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.text.BadLocationException;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.VocalMaze.Menus.StartMenu;
import com.VocalMaze.ModeleUtils.Direction;
import com.VocalMaze.ViewUtils.SoundEffects;
import com.sun.tools.javac.Main;


public class GameView extends JPanel implements KeyListener{
    public Controller controller ; 
    private LabyrintheView labyrintheView ;
    private PopUP popUP ; 
    private static final Dimension TAILLE_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();
    private int [] nbLocM_F ; 
    private int timeMs ; 
    private boolean multi ;
    private boolean isRecording , isRecordingTime ;
    static GraphicsDevice device;
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private SoundEffects soundEffects ; 

    //desactiver les logs
    static {
        Logger.getLogger("LiumUtil").setLevel(Level.OFF);
        Logger.getLogger("java.awt.Component").setLevel(Level.OFF);
        Logger.getLogger("java.awt.Container").setLevel(Level.OFF);
        Logger.getLogger("java.awt.KeyboardFocusManager").setLevel(Level.OFF);
        Logger.getLogger("javax.swing.DefaultKeyboardFocusManager").setLevel(Level.OFF);
    }
    
    public GameView(String pseudo , int nbMaleTotal , int nbFemelleTotal, boolean multi) throws IOException {
        setSize(TAILLE_ECRAN);
        setLayout(new BorderLayout());
        this.multi = multi;
        controller = new Controller(new GameModel(pseudo, nbMaleTotal, nbFemelleTotal, multi), this) ; 
        labyrintheView = new LabyrintheView() ; 
        labyrintheView.decoupeImage();
        //labyrintheView.setLocation(250, 100);
        add(labyrintheView) ;
        labyrintheView.setVisible(true);
        setVisible(true);

        popUP = new PopUP("- Grand master : Salutations, mes chers aventuriers! Préparez-vous à trembler de terreur. Maintenant, commençons le jeu. Vous devez trouver la sortie avant que je ne vous trouve. Ahahaha... Vous êtes à moi maintenant..Osez-vous relever le défi ? Hahahaha!\n\n" +
        "- Mystérieux guide : Dans un premier temps , vous devez parler chacun votre tour afin de vous reconnaitre , ainsi gagner du temps de parole . Appuyez sur R pour commencer l'enregistrement , puis appuyez sur S quand vous avez fini !\n\n") ;
        add(popUP, BorderLayout.EAST);
        nbLocM_F = new int[2] ;
        nbLocM_F[0] = 0 ; // nbLocM
        nbLocM_F[1] = 0 ; // nbLocF
        timeMs = -1 ; 
        addKeyListener(this) ;  
        soundEffects = new SoundEffects() ; 
    }

    public String step2 (int nbLocM , int nbLocF) {
        Random rm = new Random() ; 
        String hommeFemme = nbLocF > 1 ? "femmes" : "femme";
        String hommeHomme = nbLocM > 1 ? "hommes" : "homme";
        if (nbLocF == 0 && nbLocM == 0) {
            switch(rm.nextInt(2)) {
                case 0:
                    return "- Grand Maître : Le silence peut être aussi déchirant que les cris... Personne ne s'échappera d'ici.\n\n";
                case 1:
                    return "- Grand Maître : Vous semblez trembler comme un chihuahua dans le grand Nord... Si vous ne trouvez pas le courage de parler, votre destin sera aussi funeste qu'un gâteau brûlé...\n\n";
            }
        }
        switch(rm.nextInt(6)) {
            case 0:
                return "- Grand Maître : À ce que j'entends, " + nbLocM + " " + hommeHomme + " et " + nbLocF + " " + hommeFemme + " sont piégés ici, à attendre leur triste sort.\n\n";
            case 1:
                return "- Grand Maître : Vous êtes seulement " + nbLocF + " " + hommeFemme + " et " + nbLocM + " " + hommeHomme + "? Je pensais avoir attiré plus de victimes dans mon piège...\n\n";
            case 2:
                return "- Grand Maître : " + nbLocM + " " + hommeHomme + " et " + nbLocF + " " + hommeFemme + " s'agitant comme des poissons hors de l'eau... quel spectacle divertissant!\n\n";
            case 3:
                return "- Grand Maître : Seulement " + nbLocF + " " + hommeFemme + " et " + nbLocM + " " + hommeHomme + "? C'est plus calme qu'un dimanche après-midi chez ma grand-mère.\n\n";
            case 4:
                return "- Grand Maître : " + nbLocM + " " + hommeHomme + " et " + nbLocF + " " + hommeFemme + "... Rien de tel que des cris collectifs pour réchauffer l'ambiance, pas vrai?\n\n";
            case 5 : 
                return "- Grand Master : Tang de personnes , Mootivés pour s'échapper , c'est Bel et bien " + nbLocF + "femmes et " + nbLocM + "hommes que j'entend...\n\n" ; 
        }
        return "" ; 
    }

    public String step1 () {
        Random rm = new Random() ; 
        switch(rm.nextInt(5)) {
            case 0 : return "- Grand Master : Vous etes toujours pas sortis d'ici ? Vous courez à votre perte...\n\n" ;
            case 1 : return "- Grand Master : Personne a su s'échapper de ce dédale sans fin , vous serez pas les premiers à vous enfuir .\n\n" ;
            case 2 : return "- Grand Master : Prenez à gauche la prochaine fois , croyez moi ça vous portera chance...\n\n" ;
            case 3 : return "- Grand Maître : Vous semblez aussi perdus qu'un pingouin dans un désert... Un petit coup de pouce, peut-être ?\n\n";
            case 4 : return "- Grand Maître : Si vous tenez à la vie autant que vous tenez à vos chaussettes, un peu de réflexion ne vous ferait pas de mal...\n\n";
        }
        return "" ; 
    }

    @Override
    public void keyTyped(KeyEvent e) {
        Thread th = new Thread(new Runnable() {
            public void run() {
                switch(e.getKeyChar()) {
                    case 'r' : {                
                        if (isRecording) break ; 
                        StartMenu.clip.stop();
                        popUP.appendMessage("- Mystérieux guide : Chuchote-moi tes pensées... Je guiderai ton chemin...\\n\\n\n\n");
                        if (timeMs == -1) {
                            soundEffects.soundStartRec();
                            controller.startRecord();
                            isRecording = true ;
                        }else{
                            soundEffects.soundStartRec();
                            controller.startRecord(timeMs);
                            isRecordingTime = true ; 
                            try {
                                Thread.sleep(timeMs);
                                isRecordingTime = false ; 
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            soundEffects.soundStopRec(timeMs);
                            StartMenu.clip.start();
                            // Faire apparaitre un petit sablier qui tourne (un gif) qui dit transcription en cours
                            //  si besoin mais c'est optionnel , ou bien des petites images qui donnent des conseils
                            // comme dans les menus de chargement des jeux a voir 
                            Direction [] directions = controller.transcrire() ; 
                            int fin = controller.play(directions) ; 
                            if (fin == 1 || fin == 2) {
                                endGame(fin);
                                return ; 
                            } 
                            if(multi)controller.getGameModel().changeTour();
                            popUP.appendMessage(step1()+"- Mystérieux guide : Appuyez sur R pour commencer l'enregistrement, et sur S pour arrêter. Utilisez judicieusement votre temps de parole...\n\n");
                            timeMs = -1 ; 
                        }
                        break ; 
                    }

                    case 's' :{
                        if (!isRecording || isRecordingTime) break ;
                        soundEffects.soundStopRec();
                        controller.stopRecord();
                        StartMenu.clip.start();
                        //Analyse du vocal
                        nbLocM_F = controller.analyse2() ;
                        //Entre 5 et 7.5 par Homme, et entre 6 et 9 par Femme
                        timeMs = nbLocM_F[0]*(5000+(new Random()).nextInt(2501)) + nbLocM_F[1]*(6000+(new Random()).nextInt(3001)) ;
                        popUP.appendMessage(step2(nbLocM_F[0], nbLocM_F[1])+((timeMs > 0)?("- Jeu : Vous disposez de " + (timeMs/1000) + " secondes pour " + 
                        "donner les directions à suivre pour s'enfuire . L'enregistrement se finira au bout de ce temps .\n\n"):("- Jeu : Dommage , vous avez loupé votre chance , Réessayez encore une fois , essayez de parler un peu plus fort cette fois.\n\n")));
                        isRecording = false ;
                        if (timeMs == 0) timeMs = -1 ;  
                        break ;
                    }
                    
                    default : break ; 
                }
        }
    });
    th.start();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        return ;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        return ;
    }

    public void endGame (int fin) {
        //TODO qui fera apparaire l'ecran de fin du jeu en fonction si il y a 2 équipes
    }

    public void movePlayer (Direction dir , int steps) {
        labyrintheView.movePlayer(dir, steps);
    }

    public class PopUP extends JPanel {
        private ZoneTexteInfo zoneTexteInfo;

        PopUP(String message) {
            zoneTexteInfo = new ZoneTexteInfo(message);
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.anchor = GridBagConstraints.NORTH;

            add(zoneTexteInfo, gbc);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
            setMaximumSize(new Dimension(600, 400));
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        }

        public void appendMessage(String message) {
            System.out.println("Appending message: " + message);
            zoneTexteInfo.appendMessage(message);
        }

        private class ZoneTexteInfo extends JTextArea {
            private final String texteComplet;
            private int indexTexte = 0;

            public ZoneTexteInfo(String texte) {
                super("");
                texteComplet = texte;
                setEditable(false);
                setOpaque(false);

                setBorder(BorderFactory.createCompoundBorder(new BordArrondi(15, 5), BorderFactory.createEmptyBorder(15, 15, 15, 15)));

                try {
                    File fichierPolice = new File("src/main/java/com/VocalMaze/Audio&Visuel/Font-police/PressStart2P-Regular.ttf");
                    Font pressStart2P = Font.createFont(Font.TRUETYPE_FONT, fichierPolice).deriveFont(12f);
                    Font pressStart2PWithSpacing = creerPoliceAvecEspaceLigne(pressStart2P, 3f);
                    setFont(pressStart2PWithSpacing);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }

                setForeground(new Color(0, 0, 0));
                setPreferredSize(new Dimension(600, 400));
                setLineWrap(true);
                setWrapStyleWord(true);
                setRows(6);
                setMargin(new Insets(10, 10, 10, 10));

                int delai = 50;
                ActionListener actionEcrire = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (indexTexte < texteComplet.length()) {
                            setText(texteComplet.substring(0, indexTexte));
                            indexTexte++;
                        } else {
                            ((Timer) evt.getSource()).stop();
                        }
                    }
                };
                new Timer(delai, actionEcrire).start();
            }

            private class BordArrondi extends AbstractBorder {
                private final int largeurArc;
                private final int hauteurArc;
                private final int epaisseur;

                public BordArrondi(int largeurArc, int epaisseur) {
                    this.largeurArc = largeurArc;
                    this.hauteurArc = largeurArc;
                    this.epaisseur = epaisseur;
                }

                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    g.setColor(Color.BLACK);
                    for (int i = 0; i < epaisseur; i++) {
                        g.drawRoundRect(x + i, y + i, width - 1 - (i * 2), height - 1 - (i * 2), largeurArc, hauteurArc);
                    }
                }
            }

            private Font creerPoliceAvecEspaceLigne(Font police, float espacement) {
                Map<TextAttribute, Object> attributs = new HashMap<>(police.getAttributes());
                attributs.put(TextAttribute.SIZE, police.getSize() + espacement);
                return police.deriveFont(attributs);
            }

            public void appendMessage(String message) {
                int delai = 50; // milliseconds
                int[] indexMessage = new int[]{0};

                ActionListener actionAjouter = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (indexMessage[0] < message.length()) {
                            append(String.valueOf(message.charAt(indexMessage[0])));
                            indexMessage[0]++;

                            SwingUtilities.invokeLater(() -> {
                                int maxLignes = 6;
                                int nbLignes = getLineCount();
                                if (nbLignes > maxLignes) {
                                    try {
                                        int debut = getLineStartOffset(0);
                                        int fin = getLineEndOffset(nbLignes - maxLignes);
                                        replaceRange("", debut, fin);
                                        setCaretPosition(getDocument().getLength());
                                    } catch (BadLocationException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            ((Timer) evt.getSource()).stop();
                        }
                    }
                };
                new Timer(delai, actionAjouter).start();
            }
        }
    }

    private class LabyrintheView extends JPanel {
        private boolean enDeplacement ;
        private boolean enDeplacement2 ;
        private BufferedImage[][] sprites;
        private BufferedImage[][] porteLabyrinthe;
        private BufferedImage[][] sprites2;
        private int currentFrame ;
        private long lastTime ;
        private BufferedImage imagePorte;
        private BufferedImage imagePassage;
        private BufferedImage imageSprite;
        private BufferedImage imageSprite2;
        private Direction dirAnim ;
        private Direction dirAnim2 ;
        private int caseX, ancienCaseX;
        private int caseY, ancienCaseY;
        private int caseX2, ancienCaseX2;
        private int caseY2, ancienCaseY2;
        private int stepsAnim;
        private int pourcentTailleEcranX, pourcentTailleEcranY;

        public LabyrintheView () throws IOException{
            setPreferredSize(TAILLE_ECRAN);
            imageSprite = ImageIO.read(new File("src/main/java/com/VocalMaze/Images/professor_walk_cycle_no_hat.png"));
            sprites = new BufferedImage[4][9];
            imagePorte = ImageIO.read(new File("src/main/java/com/VocalMaze/Images/doors.png"));
            imagePassage = ImageIO.read(new File("src/main/java/com/VocalMaze/Images/M484ShmupTileset1.png"));
            porteLabyrinthe = new BufferedImage[25][26];
            currentFrame = 0 ;
            lastTime = 0 ;
            dirAnim = Direction.BAS ;
            enDeplacement = false ;
            pourcentTailleEcranX = (int) (2.18 * TAILLE_ECRAN.getWidth()/100); // proportion que doit occuper une case.
            pourcentTailleEcranY = (int) (3.91 * TAILLE_ECRAN.getHeight()/100);
            caseX = (int) -(0.85 * TAILLE_ECRAN.getWidth()/100) + pourcentTailleEcranX * controller.getGameModel().getLabyrinthe().getPointDepart().getY(); //place l'animation sur le point de départ.
            caseY =  15;
            if(multi){
                sprites2 = new BufferedImage[4][9];
                imageSprite2= ImageIO.read(new File("src/main/java/com/VocalMaze/Images/professor_walk_cycle_no_hat.png"));
                dirAnim2 = Direction.BAS;
                caseX2 = (int) -(0.85 * TAILLE_ECRAN.getWidth()/100) + pourcentTailleEcranX * controller.getGameModel().getLabyrinthe().getPointDepart2().getY();
                caseY2 = 15;
                enDeplacement2 = false;
            }
        }

        private void decoupeImage() {
            for (int i = 0; i < sprites.length; i++) {
                for (int j = 0; j < sprites[i].length; j++) {
                    sprites[i][j] = imageSprite.getSubimage(j*64, i*64, 64, 64);
                    if(multi) sprites2[i][j] = imageSprite2.getSubimage(j*64, i*64, 64, 64);
                }
            }

            for (int i = 0; i < porteLabyrinthe.length; i++) {
                for (int j = 0; j < porteLabyrinthe[i].length; j++) {
                    if (controller.getGameModel().getLabyrinthe().estPointArrivee(i, j)) {
                        porteLabyrinthe[i][j] = imagePorte.getSubimage(100, 70, pourcentTailleEcranX, pourcentTailleEcranY);
                    }else if (controller.getOuvert(i, j)) {
                        porteLabyrinthe[i][j] = imagePassage.getSubimage(1000, 70, pourcentTailleEcranX, pourcentTailleEcranY);
                    }else {
                        porteLabyrinthe[i][j] = imagePorte.getSubimage(0, 0, pourcentTailleEcranX, pourcentTailleEcranY);
                    }
                    if (multi) {
                       if (controller.getGameModel().getLabyrinthe().estPointArrivee2(i, j)) {
                          porteLabyrinthe[i][j] = imagePorte.getSubimage(200, 200, pourcentTailleEcranX, pourcentTailleEcranY);
                       }
                    }
                }
            }
      }

        private void animateMovement () {
            while(true) {
                if (enDeplacement || enDeplacement2) {
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
                if(!controller.getGameModel().getTour()){
                    if (enDeplacement) currentFrame++;
                    if (currentFrame >= sprites[3].length) {
                        switch(dirAnim) {
                            case DROITE : {
                                caseX += pourcentTailleEcranX;
                                if (caseX  >= ancienCaseX + pourcentTailleEcranX * stepsAnim) enDeplacement = false;
                                break;
                            }

                            case GAUCHE : {
                                caseX -= pourcentTailleEcranX;
                                if (caseX <= ancienCaseX - pourcentTailleEcranX * stepsAnim) enDeplacement = false;
                                break;
                            }

                            case BAS : {
                                caseY += pourcentTailleEcranY;
                                if (caseY  >= ancienCaseY + pourcentTailleEcranY * stepsAnim) enDeplacement = false;
                                break;
                            }

                            case HAUT : {
                                caseY -= pourcentTailleEcranY;
                                if (caseY <= ancienCaseY - pourcentTailleEcranY * stepsAnim) enDeplacement = false;
                                break;
                            }
                        }
                        currentFrame = 0;
                        lastTime = currentTime;
                    }
                }
                if(controller.getGameModel().getTour()){
                    if (enDeplacement2) currentFrame++;
                    if (currentFrame >= sprites2[3].length) {
                        switch(dirAnim2) {
                            case DROITE : {
                                caseX2 += pourcentTailleEcranX;
                                if (caseX2  >= ancienCaseX2 + pourcentTailleEcranX * stepsAnim) enDeplacement2 = false;
                                break;
                            }

                            case GAUCHE : {
                                caseX2 -= pourcentTailleEcranX;
                                if (caseX2 <= ancienCaseX2 - pourcentTailleEcranX * stepsAnim) enDeplacement2 = false;
                                break;
                            }

                            case BAS : {
                                caseY2 += pourcentTailleEcranY;
                                if (caseY2  >= ancienCaseY2 + pourcentTailleEcranY * stepsAnim) enDeplacement2 = false;
                                break;
                            }

                            case HAUT : {
                                caseY2 -= pourcentTailleEcranY;
                                if (caseY2 <= ancienCaseY2 - pourcentTailleEcranY * stepsAnim) enDeplacement2 = false;
                                break;
                            }
                        }
                        currentFrame = 0;
                        lastTime = currentTime;
                    }
                }
            }

        }

        public void movePlayer(Direction dir, int steps) {
            if(controller.getGameModel().getTour()){
                if (caseX < 0 || caseY < 0) {
                    caseY2 += controller.getGameModel().getLabyrinthe().getPointDepart2().getX() * pourcentTailleEcranX;
                    caseX2 += controller.getGameModel().getLabyrinthe().getPointDepart2().getY() * pourcentTailleEcranY;
                }
                enDeplacement2 = true;
                dirAnim2 = dir;
                ancienCaseX2 = caseX2;
                ancienCaseY2 = caseY2;
            } else {
                if (caseX < 0 || caseY < 0) {
                    caseY += controller.getGameModel().getLabyrinthe().getPointDepart().getX() * pourcentTailleEcranX;
                    caseX += controller.getGameModel().getLabyrinthe().getPointDepart().getY() * pourcentTailleEcranY;
                }
                enDeplacement = true;
                dirAnim = dir;
                ancienCaseX = caseX;
                ancienCaseY = caseY;
            }
            stepsAnim = steps;
            animateMovement();
            }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < porteLabyrinthe.length; i++) {
                for (int j = 0; j < porteLabyrinthe[i].length; j++) {
                    g.drawImage(porteLabyrinthe[i][j], j * pourcentTailleEcranX , i * pourcentTailleEcranY, null);
                }
            }
            switch(dirAnim) {
            case DROITE : {
                if (enDeplacement) g.drawImage(sprites[dirAnim.ordinal()][currentFrame], caseX + currentFrame * stepsAnim, caseY, null);
                else g.drawImage(sprites[dirAnim.ordinal()][0], caseX + currentFrame, caseY, null);
                break;
            }
            case GAUCHE : {
                if (enDeplacement) g.drawImage(sprites[dirAnim.ordinal()][currentFrame], caseX - currentFrame * stepsAnim, caseY, null);
                else g.drawImage(sprites[dirAnim.ordinal()][0], caseX - currentFrame, caseY, null);
                break;
            }
            case HAUT : {
                if (enDeplacement) g.drawImage(sprites[dirAnim.ordinal()][currentFrame], caseX, caseY - currentFrame * stepsAnim, null);
                else g.drawImage(sprites[dirAnim.ordinal()][0], caseX, caseY - currentFrame, null);
                break;
            }
            case BAS : {
                if (enDeplacement) g.drawImage(sprites[dirAnim.ordinal()][currentFrame], caseX, caseY + currentFrame * stepsAnim, null);
                else g.drawImage(sprites[dirAnim.ordinal()][0], caseX, caseY + currentFrame, null);
                break;
            }
            }
            if(multi){
                switch(dirAnim2) {
                    case DROITE : {
                        if (enDeplacement2) g.drawImage(sprites2[dirAnim2.ordinal()][currentFrame], caseX2 + currentFrame * stepsAnim, caseY2, null);
                        else g.drawImage(sprites2[dirAnim2.ordinal()][0], caseX2 + currentFrame, caseY2, null);
                        break;
                    }
                    case GAUCHE : {
                        if (enDeplacement2) g.drawImage(sprites2[dirAnim2.ordinal()][currentFrame], caseX2 - currentFrame * stepsAnim, caseY2, null);
                        else g.drawImage(sprites2[dirAnim2.ordinal()][0], caseX2 - currentFrame, caseY, null);
                        break;
                    }
                    case HAUT : {
                        if (enDeplacement2) g.drawImage(sprites2[dirAnim2.ordinal()][currentFrame], caseX2, caseY2 - currentFrame * stepsAnim, null);
                        else g.drawImage(sprites2[dirAnim2.ordinal()][0], caseX2, caseY2 - currentFrame, null);
                        break;
                    }
                    case BAS : {
                        if (enDeplacement2) g.drawImage(sprites2[dirAnim2.ordinal()][currentFrame], caseX2, caseY2 + currentFrame * stepsAnim, null);
                        else g.drawImage(sprites2[dirAnim2.ordinal()][0], caseX2, caseY2 + currentFrame, null);
                        break;
                    }
                }
            }
        }
    }
    
    private static void configureLogging() {
        try {
            FileInputStream configFile = new FileInputStream("src/main/java/logging.properties");
            LogManager.getLogManager().readConfiguration(configFile);
        } catch (IOException e) {
            LOGGER.warning("Could not load logging configuration file. Using default logging settings.");
        }

        // Disable specific loggers
        Logger.getLogger("java.awt.Component").setLevel(Level.OFF);
        Logger.getLogger("java.awt.Container").setLevel(Level.OFF);
        Logger.getLogger("java.awt.KeyboardFocusManager").setLevel(Level.OFF);
    }
    
    static class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }
    }
    public static void main(String[] args) throws IOException {
        configureLogging();
    JFrame frame = new JFrame() ;
    frame.getContentPane().setLayout(null);

    //Pour le plein écran
    device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    if(device.isFullScreenSupported()){
        device.setFullScreenWindow(frame);
    }else{
        System.err.println("Le mode plein écran n'est pas compatible");
    }
    frame.setPreferredSize(TAILLE_ECRAN);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GameView gameView = new GameView("test" , 2 , 2, true);
    frame.add(gameView);
    frame.addKeyListener(gameView); // important
    frame.pack();
    //frame.setFocusable(true);// pas tres important au final jsp a voir
    frame.setVisible(true);
    gameView.controller.getGameModel().getLabyrinthe().printLabyrinthe();
    System.out.println(TAILLE_ECRAN);

    //elever les logs de lium utils
      System.setProperty("java.util.logging.config.file", "src/main/java/logging.properties");
  }

}