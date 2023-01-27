import javax.sound.sampled.*;
import javax.swing.JLabel;

import java.io.*;
 

public class Recorder {
    JLabel j = new JLabel();

    //Durée du record en long 
    static final long RECORD_TIME = 6000;  // 5 secondes
 
    // Path du fichier de départ (car on crée un objet File uniquement avec un fichier déja présent)
    private File wavFile = new File("Lost Voices (Test)/Test/Audio.wav");

    // the line from which audio data is captured
    private TargetDataLine line;
 
    //Format audio. 
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;//Fréquence d'échantillonage du son, CAD la compression du nombre de 0 ou 1 du signal binaire 
        int sampleSizeInBits = 8; //Bitrate du fichier (Nombre de bit par seconde du fichier)
        int channels = 2; //Fichier stéréo
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return format;
    }

    public void record () {
        Thread threadStopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(Recorder.RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                finishRecording();
            }
        });

        Thread threadRecorder = new Thread(new Runnable() {
            public void run () {
                startRecording();
            }
        }) ; 
        threadRecorder.start();
        threadStopper.start();
        /* IMPORTANT
            Chose a savoir :
                startRecording() , va interrompre l'execution du Thread actuel
                    (le programme actuel en gros) , et le fera concentrer sur lui meme
                    jusqu'a sa fin , puis il redonnera la main pour faire continuer le thread actuel
                    sur la suite des evenements
                    c'est pour cela qu'il est mis dans un autre Thread que le principale qui execute le programme
            Avec cette façon de faire le programme va continuer son execution
            et n'attendra pas la fin du record pour continuer de faire la suite
            peut etre un probleme ?
            si c'est le cas, la soluce est :
                    Effacer le threadRecorder et son start
                    ajouter la ligne : startRecording() ; 
                        apres la commande : threadStopper.start() ;  
                Ou bien
                    Garder les deux Thread , mais ajouter un wait(6000)
                    pour attendre la fin du record , et analyser le vocal 
                    par la suite
            A voir plus tard         
        */
    }
 
    private void startRecording() {
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
            line.open(format); // ready to capture
            line.start();   // start capturing
 
            System.out.println("Captation du son en cours...");
            j.setText("Captation du son en cours...");
 
            AudioInputStream ais = new AudioInputStream(line);
 
            System.out.println("Enregistrement du son en cours...");
            j.setText("Enregistrement du son en cours...");
 
            //Ici on va simplement prendre tout les bits qu'on a capturé juste avant et on va les écrire sur le fichier AUDIO
            AudioSystem.write(ais,AudioFileFormat.Type.WAVE, wavFile);
 
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
 
    private void finishRecording() {
        //ici on ferme le dateline pour finaliser le traitement du son et finaliser l'enregistrement. 
        line.stop(); // arrete de capturer le son
        line.close(); // libere les ressources audio que le systeme utilise
        System.out.println("Enregistrement fini, Consultez le fichier Audio dans le Dossier TEST");
        j.setText("Enregistrement fini, Consultez le fichier Audio dans le Dossier TEST");

    }

}