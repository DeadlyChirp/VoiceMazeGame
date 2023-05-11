package com.VocalMaze.Menus;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;

import com.VocalMaze.GameView;
import com.VocalMaze.ModeleUtils.AudioAnalyser;
import com.VocalMaze.ModeleUtils.AnalyseVocal.Recorder;
import com.VocalMaze.ViewUtils.ImagePanel;
import com.sun.tools.javac.Main;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class StartMenu extends JFrame {

    // Pour le 1er Menu
    private JButton play;
    private JButton mute;
    // Pour le 2ème Menu
    private JButton onep;
    private JButton twop;
    private boolean multi;
    // Pour le 3eme Menu
    private JButton Record;
    private JButton exit;
    private JButton stop;
    static boolean recording;
    // Pour le 4eme Menu
    private int homme, femme;
    private JButton playB;

    //Partie essentiel au jeu
    static boolean sonON;
    static GraphicsDevice device;
    File file = new File("src/main/java/com/VocalMaze/Audio&Visuel/Musiques/Lost Voice OP - BigJay.wav");
    public static File file2 = new File("chemin a renseigné"); // Chemin de la musique en plein jeu
    ImagePanel background = new ImagePanel("src/main/java/com/VocalMaze/Images/Menu Lost Voice.png");
    ImagePanel back2 = new ImagePanel("src/main/java/com/VocalMaze/Images/back2.png");
    ImagePanel back3 = new ImagePanel("src/main/java/com/VocalMaze/Images/back3.png");
    AudioInputStream ais = AudioSystem.getAudioInputStream(file);
    public static AudioInputStream game;
    private static final Dimension TAILLE_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();
    public static boolean muted = false ; 

    // partie audio
    private Recorder recorder;
    private AudioAnalyser audioAnalyser;
    public static Clip clip;
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /*************************************************Fonction Principale****************************************************************/

    // Menu Principale
    public StartMenu() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        super();

        // Partie Audio
        clip = AudioSystem.getClip();
        if (clip.isActive() || clip.isRunning()) {
            clip.stop();
            sonON = false;
        } else {
            clip.open(ais);
            sonON = true;
        }

        // Init de la Frame
        setTitle("Lost Voice");
        getContentPane().setLayout(null);

        // Init du plein écran
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(this);
        } else {
            System.err.println("Le mode plein ecran n'est pas disponible");
        }

        // Début du son
        clip.start();

        // Init des boutons
        ImageIcon p = new ImageIcon("src/main/java/com/VocalMaze/Images/play.png");
        // ImageIcon q = new ImageIcon("Pictures/quitter.png");
        ImageIcon m = new ImageIcon("src/main/java/com/VocalMaze/Images/mute1.png");

        // Init de play
        play = new JButton(p);
        play.setBorderPainted(false);
        play.setBackground(new Color(0, 0, 0, 0));
        // quitter = new JButton(q);

        // Init de Mute
        mute = new JButton(m);
        mute.setBorderPainted(false);
        mute.setBackground(new Color(0, 0, 0, 0));

        play.setBounds(870, 661, p.getIconWidth(), p.getIconHeight());
        // quitter.setBounds(405, 627, q.getIconWidth(), q.getIconHeight());
        mute.setBounds(1750, 50, m.getIconWidth(), m.getIconHeight());
        // add(quitter);

        // Init du background
        background.setSize(TAILLE_ECRAN);
        add(mute);
        add(play);
        add(background);

        pack();
        // init de la Frame
        setVisible(true);

        // Init de la Frame 2
        setActionButtons();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        recorder = new Recorder();
        audioAnalyser = new AudioAnalyser();
    }

    // Menu 2
    public void setActionButtons() {
        play.addActionListener(ev -> {
            remove(play);
            ImageIcon one = new ImageIcon("src/main/java/com/VocalMaze/Images/1p.png");
            ImageIcon two = new ImageIcon("src/main/java/com/VocalMaze/Images/2p.png");
            // Init des boutons
            onep = new JButton(one);
            twop = new JButton(two);

            // Init de onep
            onep.setBorderPainted(false);
            onep.setBackground(new Color(0, 0, 0, 0));
            onep.setBounds(460, 957, one.getIconWidth(), one.getIconHeight());

            // Init de twop
            twop.setBorderPainted(false);
            twop.setBackground(new Color(0, 0, 0, 0));
            twop.setBounds(1431, 957, two.getIconWidth(), two.getIconHeight());

            // Init du back
            background.add(onep);
            background.add(twop);
            pack();
            setVisible(true);
            setActionButtons();
            setActionButtons2();
        });

        mute.addActionListener(ev -> {
            ImageIcon icon2;
            if (sonON == true) {
                icon2 = new ImageIcon("src/main/java/com/VocalMaze/Images/mute1.png");
                clip.start();
                sonON = false;
            } else {
                icon2 = new ImageIcon("src/main/java/com/VocalMaze/Images/mute2.png");
                clip.stop();
                sonON = true;
            }
            mute.removeAll();
            mute.setIcon(icon2);
        });
    }

    // Menu 3
    public void menuRec(){
        // Remove des anciens boutons/images
        remove(play);
        remove(background);

        // Resize et ajout du background
        back2.setSize(TAILLE_ECRAN);
        add(back2);

        // Init du panel de texte
        CustomPanel panel = new CustomPanel();
        String[] paragraphs = {
                " ",
                "Les légendes mentionnent un lieu caché dans une citée antique qui date des anciens temps . Rares sont ",
                "les ouvrages mentionnant son existance , mais son trésor est connu de tous...\n\n",
                "Au fil des années son nom s'est perdu laissant ainsi dans la bouche de tous que son trésor qui regrouppe ",
                "tout ce qu'un homme peut souhaiter : Fortune , Gloire et pouvoir . Mais un tel trésor doit bien etre protégé ",
                "Contre toute éventuelle menace .C'est ainsi que les artisants de cette citée ont fait sortir leurs talents ",
                "de constructeurs , permettant ainsi d'établir des labyrinthes pour protéger l'accès à ce trésore dont la " , 
                "sortie de chacun donne sur un autre tout aussi grand. On raconte que plusieurs ouvriers se sont perdus " , 
                "en essayant de se rendre sur leur lieu de travail...Ce désir de protéction était si fort , qu'il ont ",
                "invoqué un Esprit qui sera chargé de faire perdre toute personne essayant de penétrer dans cette salle",
                " afin de la pillier . Par sa force il peut faire bouger les mures du labyrinthe...Ainsi, ce dernier n'est",
                " jamais le même peu importe combien de fois vous rentrez dedans . C'est ainsi que dans notre présent une ",
                "équipe d'archéologues ont pu retrouver la trace de cette citée perdue , entrés dans les labyrinthes sans ",
                "préparation les membres se perdent un à un , et la folie commença à s'emparer de chacun.. Ainsi, Vous devez ",
                "guider le dernier survivant vers la sortie , mais attention l'Esprit du labyrinthe <Grand Master> de son nom ,",
                " ne vous lissera pas faire , il essaira par tous les moyens de limiter votre contact avec le survivant . ",
                "C'est à vous de jouer !"
        };
        for (int i = 0; i < paragraphs.length; i++) {
            panel.addParagraph(paragraphs[i]);
        }

        // Init des boutons
        ImageIcon r = new ImageIcon("src/main/java/com/VocalMaze/Images/rec.png");
        ImageIcon s = new ImageIcon("src/main/java/com/VocalMaze/Images/stop.png");
        ImageIcon q = new ImageIcon("src/main/java/com/VocalMaze/Images/quitter.png");
        // Init de rec
        Record = new JButton(r);
        Record.setBorderPainted(false);
        Record.setBackground(new Color(0, 0, 0, 0));
        // Init de stop
        stop = new JButton(s);
        stop.setBorderPainted(false);
        stop.setBackground(new Color(0, 0, 0, 0));
        // Init de quitter
        exit = new JButton(q);
        exit.setBorderPainted(false);
        exit.setBackground(new Color(0, 0, 0, 0));
        Record.setBounds(460, 957, r.getIconWidth(), r.getIconHeight());
        exit.setBounds(1431, 957, q.getIconWidth(), q.getIconHeight());
        stop.setBounds(955, 957, s.getIconWidth(), s.getIconHeight());

        // Ajout du panel de texte et des boutons
        back2.add(panel);
        panel.setLocation(100, (int) TAILLE_ECRAN.getWidth() / 100);
        back2.add(Record);
        back2.add(stop);
        back2.add(exit);
        pack();
        recording = false;
        panel.setVisible(true);
        setVisible(true);
        setActionButtons();
        setActionButtons3();
        setActionButtons2();
    }

    public void setActionButtons2() {
        onep.addActionListener(ev -> {
            multi = false;
            menuRec();
        });

        mute.addActionListener(ev -> {
            ImageIcon icon2;
            if (sonON == true) {
                icon2 = new ImageIcon("src/main/java/com/VocalMaze/Images/mute1.png");
                clip.start();
                sonON = false;
            } else {
                icon2 = new ImageIcon("src/main/java/com/VocalMaze/Images/mute2.png");
                clip.stop();
                sonON = true;
            }
            mute.removeAll();
            mute.setIcon(icon2);
        });

        twop.addActionListener(ev->{
            multi = true;
            menuRec();
        });

        // quitter.addActionListener(ev->{
        // System.exit(0);
        // });
    }

    // Menu 4
    public void setActionButtons3() {

        Record.addActionListener(ev -> {
            // Stop de la musique avant de rec
            clip.stop();
            sonON = true;
            recording = true;
            recorder.startRecord();
        });

        stop.addActionListener(ev -> {
            // Reprise de la musique
            clip.start();
            sonON = false;
            if(recording){
                recorder.stopRecording();
            }
            int[] nbLocH_F = audioAnalyser.analyse1();
            homme = nbLocH_F[0];
            femme = nbLocH_F[1];

            // Cleaning du menu 3
            back2.remove(stop);
            back2.remove(Record);
            back2.remove(exit);
            remove(back2);
            back3.setSize(TAILLE_ECRAN);
            add(back3);
            back3.setVisible(true);

            // Init des Scores
            JPanel chiffresPanel = new JPanel();
            chiffresPanel.setLayout(null);
            JLabel hommeLabel = new JLabel(String.valueOf(homme));
            JLabel femmeLabel = new JLabel(String.valueOf(femme));
            Font font = new Font("Arial", Font.PLAIN, 40);
            hommeLabel.setFont(font);
            femmeLabel.setFont(font);
            hommeLabel.setSize(60, 60);
            femmeLabel.setSize(60, 60);
            JPanel ScoreH = new JPanel();
            ScoreH.add(hommeLabel);
            ScoreH.setSize(60, 60);
            ScoreH.setLocation(295, 504);
            JPanel ScoreF = new JPanel();
            ScoreF.add(femmeLabel);
            ScoreF.setSize(60, 60);
            ScoreF.setLocation(1550, 504);

            ImageIcon p = new ImageIcon("src/main/java/com/VocalMaze/Images/playB.png");
            playB = new JButton(p);
            playB.setBorderPainted(false);
            playB.setBackground(new Color(0, 0, 0, 0));
            playB.setBounds(755, 892, p.getIconWidth(), p.getIconHeight());


            // Ajout du JPanel chiffresPanel au back3
            back3.add(ScoreH);
            ScoreH.setVisible(true);
            back3.add(ScoreF);
            ScoreF.setVisible(true);
            back3.add(playB);
            pack();
            setVisible(true);
            setActionButtons4();
        });

        exit.addActionListener(ev -> {
            System.exit(0);
        });
    }

    //GameView
    public void setActionButtons4() {
        playB.addActionListener(ev -> {
            dispose();
            JFrame frame = new JFrame();
            frame.getContentPane().setLayout(null);

            // Pour le plein écran
            device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            if (device.isFullScreenSupported()) {
                device.setFullScreenWindow(frame);
            } else {
                System.err.println("Le mode plein écran n'est pas compatible");
            }

            frame.setPreferredSize(TAILLE_ECRAN);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GameView gameView;
            try {
                gameView = new GameView("test", homme, femme, multi);
                frame.add(gameView);
                frame.addKeyListener(gameView); // important
                frame.pack();
                frame.setFocusable(true);// pas tres important au final jsp a voir
                frame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // elever les logs de lium utils
            System.setProperty("java.util.logging.config.file", "src/main/java/logging.properties");

        });
    }

    public static void startMusic () {
        if (muted) return ; 
        clip.start();
    } 

    public static void stopMusic () {
        if (muted) return ; 
        clip.stop();
    }


    /************************************************* Fonction ANNEXE*************************************************************/

    public class CustomPanel extends JPanel {

        private ArrayList<String> paragraphs;

        public CustomPanel() {
            super();
            setSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.9),
                    (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.66)));
            paragraphs = new ArrayList<String>();
        }

        public void addParagraph(String paragraph) {
            repaint();
            paragraphs.add(paragraph);
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // dessiner le fond avec des coins arrondis
            int arc = 20;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));

            // afficher les paragraphes avec des couleurs différentes
            int x = 50;
            int y = 50;
            for (int i = 0; i < paragraphs.size(); i++) {
                g2d.setColor(getColorForParagraph(i));
                g2d.drawString(paragraphs.get(i), x, y);
                File fichierPolice = new File("src/main/java/com/VocalMaze/Audio&Visuel/Font-police/PressStart2P-Regular.ttf");
                try {
                    Font pressStart2P = Font.createFont(Font.TRUETYPE_FONT, fichierPolice).deriveFont(12f);
                    Font pressStart2PWithSpacing = creerPoliceAvecEspaceLigne(pressStart2P, 3f);
                    g2d.setFont(pressStart2PWithSpacing);
                } catch (FontFormatException | IOException e) {
                    e.printStackTrace();
                }
                y += 30; // ajuster la position verticale pour le paragraphe suivant
            }
        }

        private Font creerPoliceAvecEspaceLigne(Font police, float espacement) {
            Map<TextAttribute, Object> attributs = new HashMap<>(police.getAttributes());
            attributs.put(TextAttribute.SIZE, police.getSize() + espacement);
            return police.deriveFont(attributs);
        }

        private Color getColorForParagraph(int index) {
            switch (index % 3) { // changer la couleur toutes les trois paragraphes
                case 0:
                    return Color.RED;
                case 1:
                    return Color.BLACK;
                case 2:
                    return Color.BLUE;
                default:
                    return Color.CYAN;
            }
        }
    }

    //enlever les logs
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
    
    static {
        Logger.getLogger("LiumUtil").setLevel(Level.OFF);
        Logger.getLogger("java.awt.Component").setLevel(Level.OFF);
        Logger.getLogger("java.awt.Container").setLevel(Level.OFF);
        Logger.getLogger("java.awt.KeyboardFocusManager").setLevel(Level.OFF);
        Logger.getLogger("javax.swing.DefaultKeyboardFocusManager").setLevel(Level.OFF);
    }

    /****************************************************************************************************************************** */
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        @SuppressWarnings("unused")
        StartMenu background = new StartMenu();
        configureLogging();
        System.setProperty("java.util.logging.config.file", "src/main/java/logging.properties");
    }
}
