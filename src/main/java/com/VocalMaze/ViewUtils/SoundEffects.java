package com.VocalMaze.ViewUtils;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEffects {
    private AudioInputStream audioInputStream; 
    private Clip clip ; 

    public SoundEffects () {
        try {
            clip = AudioSystem.getClip() ;
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } 
    }

    public void soundStartRec () {
        reset();
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/java/com/VocalMaze/Audio&Visuel/Musiques/record-sound-fx-1-in.wav")) ;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return ; 
        } catch (IOException e) {
            e.printStackTrace();
            return ; 
        }
        try {
            clip.open(audioInputStream);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return ; 
        } catch (IOException e) {
            e.printStackTrace();
            return ; 
        }
        clip.setMicrosecondPosition(0);
        clip.start(); 
    }

    public void soundStopRec () {
        reset();
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/java/com/VocalMaze/Audio&Visuel/Musiques/record-sound-fx-1-out.wav")) ;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return ; 
        } catch (IOException e) {
            e.printStackTrace();
            return ; 
        }
        try {
            clip.open(audioInputStream);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return ; 
        } catch (IOException e) {
            e.printStackTrace();
            return ; 
        }
        clip.setFramePosition(0);
        clip.start();         
    }

    public void soundStopRec (int timeMs) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                soundStopRec();
            }
        }) ; 
        th.start();
    }

    private void reset () {
        clip.close();
    }

}
