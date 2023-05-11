package com.VocalMaze.ModeleUtils.AnalyseVocal;

import javax.sound.sampled.*;
import java.io.*;
 
public class Recorder {
    private TargetDataLine line;// the line from which audio data is captured
    private volatile boolean recording = false;
    //Format audio. 
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;//Fréquence d'échantillonage du son, CAD la compression du nombre de 0 ou 1 du signal binaire 
        int sampleSizeInBits = 16; //Bitrate du fichier (Nombre de bit par seconde du fichier)
        int channels = 1; //Fichier mono
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void startRecord () {
        Thread threadRecorder = new Thread(new Runnable() {
            public void run () {
                record();
            }
        }) ; 
        threadRecorder.start();
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

    private void record() {
        recording = true ; 
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
 
            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("DataLine incorrecte ou non supporté");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format); // ready to capture
            line.start();   // start capturing
 
            System.out.println("Captation du son en cours...");
            //Ici on va simplement prendre tout les bits qu'on a capturé juste avant et on va les écrire sur le fichier AUDIO
            try (AudioInputStream ais = new AudioInputStream(line)) {
                System.out.println("Enregistrement du son en cours...");
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("src/main/java/com/VocalMaze/Records/Audio.wav"));
            }
        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        } finally { //finally dans la méthode pour appeler stopRecording() lorsque la méthode se termine,
            // garantissant la libération des ressources en cas d'exceptions.
            stopRecording();
        }
    }
 
    public void startRecord (int timeMs) {
        Thread threadRecorder = new Thread(new Runnable() {
            @Override
            public void run () {
                record();
            }
        }) ; 
        Thread threadTimer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeMs+1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }                
                stopRecording();
            }
        }) ;
        threadRecorder.setDaemon(true);
        threadTimer.setDaemon(true);
        threadRecorder.start();
        threadTimer.start();
    }

    public void stopRecording() {
        //ici on ferme le dateline pour finaliser le traitement du son et finaliser l'enregistrement. 
        if (recording) {
        line.stop(); // arrete de capturer le son
        line.close(); // libere les ressources audio que le systeme utilise
        recording = false;
        }
    }

}