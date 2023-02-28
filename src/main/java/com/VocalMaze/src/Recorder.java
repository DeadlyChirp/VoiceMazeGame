package com.VocalMaze.src;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Recorder implements Runnable {

    private TargetDataLine line;
    private Thread thread;
    private String filename;

    public Recorder() {
        filename = "audio.wav";
    }

    public Recorder(String filename) {
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            AudioFormat format = initAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                throw new IllegalArgumentException("isLineSupported false");
            }

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            AudioInputStream ais = new AudioInputStream(line);
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        line.stop();
        line.close();

        thread.interrupt();
        thread = null;
    }

    private AudioFormat initAudioFormat() {
        float sampleRate = 44100.0f;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean bigEndian = true;
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, sampleSizeInBits, channels, (sampleSizeInBits / 8) * channels, sampleRate, bigEndian);
    }
}
