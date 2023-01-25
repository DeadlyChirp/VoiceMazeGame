import javax.sound.sampled.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.io.*;
 

public class Recorder {
    
    static JLabel j = new JLabel();

    //Durée du record en long 
    static final long RECORD_TIME = 6000;  // 5 secondes
 
    // Path du fichier de départ (car on crée un objet File uniquement avec un fichier déja présent)
    File wavFile = new File("Lost Voices (Test)/Test/Audio.wav");
 
    //Typage du fichier 
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
 
    // the line from which audio data is captured
    TargetDataLine line;
 
    //Format audio. 
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;//Fréquence d'échantillonage du son, CAD la compression du nombre de 0 ou 1 du signal binaire 
        int sampleSizeInBits = 8; //Bitrate du fichier (Nombre de bit par seconde du fichier)
        int channels = 2; //Fichier stéréo
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return format;
    }
 
    void record() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
 
            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("DataLine incorrecte ou non supporté");
                j.setText("DataLine incorrecte ou non supporté");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing
 
            System.out.println("Captation du son en cours...");
            j.setText("Captation du son en cours...");
 
            AudioInputStream ais = new AudioInputStream(line);
 
            System.out.println("Enregistrement du son en cours...");
            j.setText("Enregistrement du son en cours...");
 
            //Ici on va simplement prendre tout les bits qu'on a capturé juste avant et on va les écrire sur le fichier AUDIO
            AudioSystem.write(ais, fileType, wavFile);
 
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
 
    void finish() {
        //ici on ferme le dateline pour finaliser le traitement du son et finaliser l'enregistrement. 
        line.stop();
        line.close();
        System.out.println("Enregistrement fini, Consultez le fichier Audio dans le Dossier TEST");
        j.setText("Enregistrement fini, Consultez le fichier Audio dans le Dossier TEST");

    }
 
    
}