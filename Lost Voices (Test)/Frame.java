import java.awt.*;
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

public class Frame extends JFrame{
    private JButton record = new JButton("Record");
    private JButton play = new JButton("Play");
    private JButton stop = new JButton("Stop");
    File file = new File("Test/Audio.wav");
    public static Clip clip;
    static boolean sonON = true;
    AudioInputStream ais;


    Frame() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        super();
        ais = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(ais);
        setTitle("Recorder Test");
        setSize(600, 600);
        setResizable(false);
        setLayout(null);
        record.setBounds(200, 150, 170, 70 );
        play.setBounds(200, 250, 170, 70 );
        stop.setBounds(200, 350, 170, 70 );
        add(record);
        add(play);
        add(stop);
        setVisible(true);
        setActionButtons();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setActionButtons(){

        play.addActionListener(ev -> {

                clip.start();
                sonON = false;
            
        });

        record.addActionListener(ev -> {
            Recorder recorder = new Recorder();

        
            Thread stopper = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(recorder.RECORD_TIME);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    recorder.finish();
                }
            });
     
            stopper.start();
            recorder.j.setBounds(100, 50, 500, 20);
            add(recorder.j);
            recorder.j.setVisible(true);
            //Ligne de commencement de l'enregistrement. 
            recorder.record();  
            
            try {
                ais = AudioSystem.getAudioInputStream(file);
            } catch (UnsupportedAudioFileException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                clip.open(ais);
            } catch (LineUnavailableException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        stop.addActionListener(ev -> {
            clip.stop();
            sonON = true;
        });

    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        Frame a = new Frame();
    }
}
