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

import com.VocalMaze.ModeleUtils.Direction;
import com.sun.tools.javac.Main;


public class GameView extends JPanel implements KeyListener{
    private Controller controller ; 
    private LabyrintheView labyrintheView ;
    private PopUP popUP ; 
    private static final Dimension TAILLE_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();
    private int [] nbLocM_F ; 
    private int timeMs ; 
    private boolean isRecording , isRecordingTime ;
    static GraphicsDevice device;
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    //deactiver les logs
    static {
        Logger.getLogger("LiumUtil").setLevel(Level.OFF);
        Logger.getLogger("java.awt.Component").setLevel(Level.OFF);
        Logger.getLogger("java.awt.Container").setLevel(Level.OFF);
        Logger.getLogger("java.awt.KeyboardFocusManager").setLevel(Level.OFF);
        Logger.getLogger("javax.swing.DefaultKeyboardFocusManager").setLevel(Level.OFF);
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

        popUP = new PopUP("- Grand master : Salutations, mes chers aventuriers! Préparez-vous à trembler de terreur. Maintenant, commençons le jeu. Vous devez trouver la sortie avant que je ne vous trouve. Ahahaha... Vous êtes à moi maintenant..Osez-vous relever le défi ? Hahahaha!\n\n" +
        "- Jeu : Dans un premier temps , vous devez parler chacun votre tour afin de vous reconnaitre , ainsi gagner du temps de parole . Appuyez sur R pour commencer l'enregistrement , puis appuyez sur S quand vous avez fini !\n\n") ;
        add(popUP, BorderLayout.EAST);
        nbLocM_F = new int[2] ;
        nbLocM_F[0] = 0 ; // nbLocM
        nbLocM_F[1] = 0 ; // nbLocF
        timeMs = -1 ; 
        addKeyListener(this) ;    
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
                    return "- Grand Maître : Vous tremblez de peur, n'est-ce pas? Si vous ne trouvez pas le courage de parler, la mort sera votre seule échappatoire...\n\n";
            }
        }
        switch(rm.nextInt(5)) {
            case 0:
                return "- Grand Maître : À ce que j'entends, " + nbLocM + " " + hommeHomme + " et " + nbLocF + " " + hommeFemme + " sont piégés ici, à attendre leur triste sort.\n\n";
            case 1:
                return "- Grand Maître : Vous êtes seulement " + nbLocF + " " + hommeFemme + " et " + nbLocM + " " + hommeHomme + "? Je pensais avoir attiré plus de victimes dans mon piège...\n\n";
            case 2:
                return "- Grand Maître : " + nbLocM + " " + hommeHomme + " et " + nbLocF + " " + hommeFemme + " luttant désespérément pour échapper à mon antre macabre... quelle délicieuse agonie. \n\n";
            case 3:
                return "- Grand Maître : Seulement " + nbLocF + " " + hommeFemme + " et " + nbLocM + " " + hommeHomme + "? Vos cris de terreur ne suffisent pas à apaiser mon appétit insatiable.\n\n";
            case 4:
                return "- Grand Maître : " + nbLocM + " " + hommeHomme + " et " + nbLocF + " " + hommeFemme + "... Les cris de terreur résonnent mieux lorsqu'ils sont partagés en groupe, n'est-ce pas?\n\n";
            case 5 : return "- Grand Master : Tang de personnes , Mootivés pour s'échapper , c'est Bel et bien " + nbLocF + "femmes et " + nbLocM + "hommes que j'entend...\n\n" ;
        }
        return "" ;
    }

    public String step1 () {
        Random rm = new Random() ; 
        switch(rm.nextInt(5)) {
            case 0 : return "- Grand Master : Vous etes toujours pas sortis d'ici ? Vous courez à votre perte...\n\n" ;
            case 1 : return "- Grand Master : Personne a su s'échapper de ce dédale sans fin , vous serez pas les premiers à vous enfuir .\n\n" ;
            case 2 : return "- Grand Master : Prenez à gauche la prochaine fois , croyez moi ça vous portera chance...\n\n" ; 
            case 3 : return "- Grand Master : Vous avez du mal pour de jeunes explorateurs...Vous voulez un peu d'aide peut être un peu d'aide ?\n\n" ; 
            case 4 : return "- Grand Master : Refléchir vous sera d'une grande utilité si vous tenez autant à vos vies comme vous le prétendez .\n\n" ;  
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
                        popUP.appendMessage("- Jeu : Enregistrement en cours...\n\n");
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
                            popUP.appendMessage(step1()+"- Jeu : Appuyez sur R pour vous enregistrer et S pour arrêter afin de gagner du temps de parole .\n\n");
                            timeMs = -1 ; 
                        }
                        break ; 
                    }

                    case 's' :{
                        if (!isRecording || isRecordingTime) break ;
                        controller.stopRecord();
                        //Analyse du vocal
                        nbLocM_F = controller.analyse2() ;
                        //Entre 5 et 7.5 par Homme, et entre 6 et 9 par Femme
                        timeMs = nbLocM_F[0]*(5000+(new Random()).nextInt(2501)) + nbLocM_F[1]*(6000+(new Random()).nextInt(3001)) ;
                        popUP.appendMessage(step2(nbLocM_F[0], nbLocM_F[1])+ "- Jeu : Vous disposez de " + (timeMs/1000) + " secondes pour " + 
                        "donner les directions à suivre pour s'enfuire . L'enregistrement se finira au bout de ce temps .\n\n");
                        isRecording = false ; 
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

    public void endGame () {
        //TODO qui fera apparaire l'ecran de fin du jeu
    }

    public void movePlayer (Direction dir , int steps) {
        labyrintheView.movePlayer(dir, steps);
    }

    public class PopUP extends JPanel {
        private InfoTextArea infoTextArea;

        PopUP(String message) {

            infoTextArea = new InfoTextArea(message);
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints(); // for placing components
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.anchor = GridBagConstraints.NORTH; // align the text at the top

            add(infoTextArea, gbc);
            setBackground(new Color(0, 0, 0, 0));
            setOpaque(false);
            setMaximumSize(new Dimension(600, 400)); // Adjust the maximum size of the PopUP panel
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        }

        public void appendMessage(String message) {
            System.out.println("Appending message: " + message); // Add this line to check if appendMessage in PopUP is called
            infoTextArea.appendMessage(message);
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

                int paddingTop = 15;
                int paddingBottom = 15;
                int paddingLeft = 15;
                int paddingRight = 15;

                setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(15, 5), BorderFactory.createEmptyBorder(paddingTop, paddingLeft, paddingBottom, paddingRight)));

                try {
                    File fontFile = new File("src/main/java/com/VocalMaze/Audio&Visuel/Font-police/PressStart2P-Regular.ttf");
                    Font pressStart2P = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(12f);
                    Font pressStart2PWithSpacing = createFontWithIncreasedLineSpacing(pressStart2P, 3f);
                    setFont(pressStart2PWithSpacing);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }

                setForeground(new Color(0, 0, 0)); // Set the text color to white
                setPreferredSize(new Dimension(600, 400));
                setLineWrap(true);
                setWrapStyleWord(true);
                // Add space between lines
                setRows(6);

                // Add some space around the text inside the JTextArea
                setMargin(new Insets(10, 10, 10, 10));

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

                //reset chat box to blank

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
                    g.setColor(new Color(0, 0, 0, 200));
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                }
                super.paintComponent(g);
            }

            private class RoundedBorder extends AbstractBorder {
                private final int arcWidth;
                private final int arcHeight;
                private final int thickness;

                public RoundedBorder(int arcWidth, int thickness) {
                    this.arcWidth = arcWidth;
                    this.arcHeight = arcWidth;
                    this.thickness = thickness;
                }

                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    g.setColor(Color.BLACK);
                    for (int i = 0; i < thickness; i++) {
                        g.drawRoundRect(x + i, y + i, width - 1 - (i * 2), height - 1 - (i * 2), arcWidth, arcHeight);
                    }
                }
            }

            private Font createFontWithIncreasedLineSpacing(Font font, float spacing) {
                Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
                attributes.put(TextAttribute.SIZE, font.getSize() + spacing);
                return font.deriveFont(attributes);
            }

            public void appendMessage(String message) {
                int delay = 50; // milliseconds
                int[] messageIndex = new int[]{0};

                ActionListener taskPerformer = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (messageIndex[0] < message.length()) {
                            append(String.valueOf(message.charAt(messageIndex[0])));
                            messageIndex[0]++;

                            // Add the following code to monitor and update the text area
                            SwingUtilities.invokeLater(() -> {
                                int maxLines = 6;
                                int lineCount = getLineCount();
                                if (lineCount > maxLines) {
                                    try {
                                        int start = getLineStartOffset(0);
                                        int end = getLineEndOffset(lineCount - maxLines);
                                        replaceRange("", start, end);
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
                new Timer(delay, taskPerformer).start();
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
            caseX = (int) -(1.24 * TAILLE_ECRAN.getWidth()/100) + pourcentTailleEcranX * controller.getGameModel().getLabyrinthe().getPointDepart().getY(); //place l'animation sur le point de départ.
            caseY =  0;
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
                        porteLabyrinthe[i][j] = imagePorte.getSubimage(100, 70, pourcentTailleEcranX, pourcentTailleEcranY);
                    }
                    else if (controller.getOuvert(i, j)) {
                        porteLabyrinthe[i][j] = imagePassage.getSubimage(1000, 70, pourcentTailleEcranX, pourcentTailleEcranY);
                    }else {
                        porteLabyrinthe[i][j] = imagePorte.getSubimage(0, 0, pourcentTailleEcranX, pourcentTailleEcranY);
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
        }
    
        public void movePlayer(Direction dir, int steps) {
            if (caseX < 0 || caseY < 0) {
                caseY += controller.getGameModel().getLabyrinthe().getPointDepart().getX() * pourcentTailleEcranX;
                caseX += controller.getGameModel().getLabyrinthe().getPointDepart().getY() * pourcentTailleEcranY;
            }
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
    GameView gameView = new GameView("test" , 2 , 2);
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
