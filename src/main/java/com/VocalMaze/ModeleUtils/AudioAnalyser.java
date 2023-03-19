package com.VocalMaze.ModeleUtils;

import com.VocalMaze.ModeleUtils.AnalyseVocal.LiumUtils;
import com.VocalMaze.ModeleUtils.AnalyseVocal.SegAnalyser;
import com.VocalMaze.ModeleUtils.AnalyseVocal.Terminal;
import com.VocalMaze.ModeleUtils.AnalyseVocal.TranscriberV3;

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

    public int [] analyse2 () {
        return null ;
        //TODO
        // version avec lium utils
    }

}
