package com.VocalMaze.ModeleUtils;

import com.VocalMaze.ModeleUtils.AnalyseVocal.LiumUtils;
import com.VocalMaze.ModeleUtils.AnalyseVocal.SegAnalyser;
import com.VocalMaze.ModeleUtils.AnalyseVocal.Terminal;
import com.VocalMaze.ModeleUtils.AnalyseVocal.TranscriberV3;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Arrays;

import static com.VocalMaze.ModeleUtils.AnalyseVocal.LiumUtils.executeForResult;

public class AudioAnalyser {
    private LiumUtils liumUtils ; 
    private Terminal terminal ;
    private SegAnalyser segAnalyser ;
    private TranscriberV3 transcriber ;

    public AudioAnalyser() {
        liumUtils = new LiumUtils() ; 
        terminal = new Terminal() ;
        segAnalyser = new SegAnalyser() ; 
        transcriber = new TranscriberV3() ; 
    }

    public int [] analyse1 () {
        terminal.execCommand("/usr/bin/java -Xmx2024m -jar ./LIUM_SpkDiarization-8.4.1.jar "+
        "--finputDesc=audio16Khz2sphinx:sphinx,1:1:0:0:0:0,18,0:0:0:0 " +
        "--fInputMask=./src/main/java/com/VocalMaze/Records/Audio.wav " +
        "--sOutputMask=/src/main/java/com/VocalMaze/Records/Audio.wav --doCEClustering Audio");
        try {
            segAnalyser.analysis("src/main/java/com/VocalMaze/Analysis/Audio.seg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int [] res = {segAnalyser.getNbLocMale() , segAnalyser.getNbLocFemale()} ; 
        return res ; 
    }

    public Direction [] transcrire () {
        return transcriber.transcription() ; 
    }

    public static int[] analyse2(String audioFile) throws Exception {
        if (audioFile == null) {
            throw new IllegalArgumentException("audioFile must not be null");
        }

        File file = new File(audioFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("audioFile is not exists");
        }

        AudioFileFormat format = AudioSystem.getAudioFileFormat(file);
        if (!format.getType().equals(AudioFileFormat.Type.WAVE)) {
            throw new IllegalArgumentException(format.getType() + " is not supported");
        }

        LiumUtils.Result result = executeForResult(Arrays.toString(new String[]{"--fInputMask=" + audioFile}));
        int[] analysis = new int[2];
        analysis[0] = result.female;
        analysis[1] = result.male;

        return analysis;
    }

    public int[] analyse3() {
        try {
            LiumUtils.Result result = LiumUtils.executeForResult("./src/main/java/com/VocalMaze/Records/Audio.wav");
            int[] res = {result.female, result.male};
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{0, 0};
    }


}
