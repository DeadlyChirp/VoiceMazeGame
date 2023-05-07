package com.VocalMaze.Menus;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import com.VocalMaze.ModeleUtils.AudioAnalyser;
import com.VocalMaze.ModeleUtils.AnalyseVocal.Recorder;
import com.VocalMaze.ViewUtils.ImagePanel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class StartMenu extends JFrame{

    //Pour le 1er Menu
    private  JButton play ; 
    private JButton quitter;
    private  JButton mute;
    //Pour le 2ème Menu
    private JButton onep;
    private JButton twop;
    //Pour le 3eme Menu
    private  JButton Record ; 
    private JButton exit;
    private  JButton stop;
    //Pour le 4eme Menu 
    private int homme, femme;
    private JButton playB;
    private JButton retryB;

    static boolean sonON;
    static GraphicsDevice device ;
	File file = new File("src/main/java/com/VocalMaze/Audio&Visuel/Musiques/Lost Voice OP - BigJay.wav");
    public static File file2 = new File("chemin a renseigné"); //Chemin de la musique en plein jeu
    ImagePanel background = new ImagePanel("src/main/java/com/VocalMaze/Images/Menu Lost Voice.png");
    ImagePanel back2 = new ImagePanel("src/main/java/com/VocalMaze/Images/back2.png");
    ImagePanel back3 = new ImagePanel("src/main/java/com/VocalMaze/Images/back3.png");
    AudioInputStream ais = AudioSystem.getAudioInputStream(file);
    public static AudioInputStream game;
    private static final Dimension TAILLE_ECRAN = Toolkit.getDefaultToolkit().getScreenSize();
    // partie audio6
    private Recorder recorder ; 
    private AudioAnalyser audioAnalyser ; 


    public static Clip clip;

/*************************************************Fonction Principale*************************************************************** */
    //Menu Principale
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
        mute.setBounds(1750, 50, m.getIconWidth(), m.getIconHeight());
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
        
        recorder = new Recorder() ; 
        audioAnalyser = new AudioAnalyser() ; 
    }

    //Menu 2
    public void setActionButtons(){
        play.addActionListener(ev -> {
            remove(play);
            ImageIcon one = new ImageIcon("src/main/java/com/VocalMaze/Images/1p.png");
            ImageIcon two = new ImageIcon("src/main/java/com/VocalMaze/Images/2p.png");
            //Init des boutons
            onep = new JButton(one);
            twop = new JButton(two);

            //Init de onep
            onep.setBorderPainted(false);
            onep.setBackground(new Color(0, 0, 0, 0));
            onep.setBounds(460, 957, one.getIconWidth(), one.getIconHeight());

            //Init de twop
            twop.setBorderPainted(false);
            twop.setBackground(new Color(0, 0, 0, 0));
            twop.setBounds(1431, 957, two.getIconWidth(), two.getIconHeight());

            //Init du back 
            background.add(onep);
            background.add(twop);
            pack();
            setVisible(true);
            setActionButtons1();
        });
    }
    
    //Menu 3
    public void setActionButtons1(){
        onep.addActionListener(ev -> {

            //Remove des anciens boutons/images
            remove(play);
            remove(mute);
            remove(background);

            //Resize et ajout du background
            back2.setSize(TAILLE_ECRAN);
            add(back2);
           
            //Init du panel de texte
            CustomPanel panel = new CustomPanel();
            String[] paragraphs = {
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum eget massa quis risus bibendum luctus in non quam. Suspendisse posuere, enim sed hendrerit laoreet, tellus nunc lobortis diam, vitae pretium sapien nunc ac odio.",
                "Etiam et accumsan libero. Integer in felis ipsum. Duis molestie, sem a feugiat tristique, sapien ex dictum mauris, vel interdum quam tortor sit amet magna. Duis imperdiet enim id aliquam fringilla. Aliquam at convallis elit.",
                "Donec pharetra nulla vel sodales dapibus. Nullam vulputate felis ut turpis tempus, in facilisis tortor pulvinar. Nunc vel purus id mauris bibendum dictum. Sed eget erat faucibus, consectetur nisl non, accumsan justo.",
                "Sed sagittis enim quis nibh maximus auctor. Aliquam sit amet sem ac massa tempor rhoncus non eu ex. Suspendisse ultricies enim vitae quam egestas, at blandit dolor efficitur. Donec et tristique lectus. Aliquam erat volutpat."
            };
            for (int i = 0; i < paragraphs.length; i++) {
                panel.addParagraph(paragraphs[i]);
            }

            
            //Init des boutons
            ImageIcon r = new ImageIcon("src/main/java/com/VocalMaze/Images/rec.png");
            ImageIcon s = new ImageIcon("src/main/java/com/VocalMaze/Images/stop.png");
            ImageIcon q = new ImageIcon("src/main/java/com/VocalMaze/Images/quitter.png");
            //Init de rec
            Record = new JButton(r);
            Record.setBorderPainted(false);
            Record.setBackground(new Color(0, 0, 0, 0));
            //Init de stop
            stop = new JButton(s);
            stop.setBorderPainted(false);
            stop.setBackground(new Color(0, 0, 0, 0));
            //Init de quitter
            exit = new JButton(q);
            exit.setBorderPainted(false);
            exit.setBackground(new Color(0, 0, 0, 0));
            Record.setBounds(460, 957, r.getIconWidth(), r.getIconHeight());
            exit.setBounds(1431, 957, q.getIconWidth(), q.getIconHeight());
            stop.setBounds(955, 957, s.getIconWidth(), s.getIconHeight());
            

            //Ajout du panel de texte et des boutons
            back2.add(panel);
            panel.setLocation(100, (int) TAILLE_ECRAN.getWidth()/100);
            back2.add(Record);
            back2.add(stop);
            back2.add(exit);
            pack();
            panel.setVisible(true);
            setVisible(true);
            setActionButtonsBis();
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
            mute.removeAll();
			mute.setIcon(icon2);
		});

        // quitter.addActionListener(ev->{
        //     System.exit(0);
        // });
    }

    //Menu 4
    public void setActionButtonsBis(){

        Record.addActionListener(ev->{
            //Stop de la musique avant de rec
            clip.stop();
            sonON=true;
            recorder.startRecord();
        });

        stop.addActionListener(ev->{
            //Reprise de la musique
            clip.start();
            sonON = false;
            recorder.stopRecording();

            int [] nbLocH_F = audioAnalyser.analyse1() ; 
            homme = nbLocH_F[0] ; 
            femme = nbLocH_F[1] ; 

            //Cleaning du menu 3
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
            femmeLabel.setSize(60,60);
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

            ImageIcon r = new ImageIcon("src/main/java/com/VocalMaze/Images/retryB.png");
            retryB = new JButton(r);
            retryB.setBorderPainted(false);
            retryB.setBackground(new Color(0, 0, 0, 0));
            retryB.setBounds(1130, 892, r.getIconWidth(), r.getIconHeight());

            // Ajout du JPanel chiffresPanel au back3
            back3.add(ScoreH);
            ScoreH.setVisible(true);
            back3.add(ScoreF);
            ScoreF.setVisible(true);
            back3.add(playB);
            back3.add(retryB);
            pack();
            setVisible(true);

        });

        exit.addActionListener(ev->{
            //316,504  et 1582et504
        });
    }


/*************************************************Fonction ANNEXE************************************************************ */
    public class CustomPanel extends JPanel {
        
        private ArrayList<String> paragraphs;
    
        public CustomPanel() {
            super();
            setSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.9), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.66)));
            paragraphs = new ArrayList<String>();
        }
    
        public void addParagraph(String paragraph) {
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
                y += 30; // ajuster la position verticale pour le paragraphe suivant
            }
        }
    
        private Color getColorForParagraph(int index) {
            switch (index % 3) { // changer la couleur toutes les trois paragraphes
                case 0:
                    return Color.RED;
                case 1:
                    return Color.GREEN;
                case 2:
                    return Color.BLUE;
                default:
                    return Color.BLACK;
            }
        }
    }

    private static class TextPanel extends JPanel {
        private final String text;
        private final Color color;
    
        public TextPanel(String text, Color color, int x, int y, int width, Font font) {
            this.text = text;
            this.color = color;
            setBounds(x, y, width, 0);
            setFont(font);
        }
    
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            g.drawString(text, 0, g.getFontMetrics().getHeight());
            setPreferredSize(new Dimension(getWidth(), g.getFontMetrics().getHeight()));
        }
    }
    
/****************************************************************************************************************************** */
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        @SuppressWarnings("unused")
        StartMenu background = new StartMenu();
    }
}
