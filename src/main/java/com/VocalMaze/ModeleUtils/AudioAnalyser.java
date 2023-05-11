package com.VocalMaze.ModeleUtils;

import com.VocalMaze.ModeleUtils.AnalyseVocal.LiumUtils;
import com.VocalMaze.ModeleUtils.AnalyseVocal.SegAnalyser;
import com.VocalMaze.ModeleUtils.AnalyseVocal.Terminal;
import com.VocalMaze.ModeleUtils.AnalyseVocal.TranscriberV3;

public class AudioAnalyser {
    private Terminal terminal ;
    private SegAnalyser segAnalyser ;
    private TranscriberV3 transcriber ;

    public AudioAnalyser() {
        terminal = new Terminal() ;
        segAnalyser = new SegAnalyser() ; 
        transcriber = new TranscriberV3() ; 
    }

    public int [] analyse1 () {
        terminal.execCommand("/usr/bin/java -Xmx2024m -jar ./LIUM_SpkDiarization-8.4.1.jar "+
        "--fInputDesc=audio16Khz2sphinx:sphinx,1:1:0:0:0:0,18,0:0:0:0 " +
        "--fInputMask=./src/main/java/com/VocalMaze/Records/Audio.wav " +
        "--sOutputMask=./src/main/java/com/VocalMaze/Analysis/Audio.seg --doCEClustering Audio");
        try {
            segAnalyser.analysis("src/main/java/com/VocalMaze/Analysis/Audio.seg");
            terminal.execCommand("rm ./src/main/java/com/VocalMaze/Analysis/Audio.seg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[] {segAnalyser.getNbLocMale() , segAnalyser.getNbLocFemale()} ;
    }

    public Direction [] transcrire () {
        return transcriber.transcription() ; 
    }

    public int[] analyse2() {
        System.setProperty("java.util.logging.config.file", "src/main/java/logging.properties");
        try {
            LiumUtils.Result result = LiumUtils.executeForResult("src/main/java/com/VocalMaze/Records/Audio.wav");
            int[] res = {result.male, result.female};
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{0, 0};
    }

}