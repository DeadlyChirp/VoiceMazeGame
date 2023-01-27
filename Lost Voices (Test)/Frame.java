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
    private File file = new File("Lost Voices (Test)/Test/Audio.wav");
    private Clip clip;
    private AudioInputStream ais;


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

    public void refresh () {
        try {
            clip = AudioSystem.getClip() ;
        } catch (Exception e) {
        }
    }

    public void setActionButtons(){
        play.addActionListener(ev -> {
            try {
                ais = AudioSystem.getAudioInputStream(file);
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            try {
                clip.open(ais);
            } catch (LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
            clip.start();
        });

        record.addActionListener(ev -> {
            Recorder recorder = new Recorder();
            recorder.record();
            recorder.j.setBounds(100, 50, 500, 20);
            add(recorder.j);
            recorder.j.setVisible(true);
        });

        stop.addActionListener(ev -> {
            clip.stop();
        });
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        @SuppressWarnings("unused")
        Frame a = new Frame();
    }
}
