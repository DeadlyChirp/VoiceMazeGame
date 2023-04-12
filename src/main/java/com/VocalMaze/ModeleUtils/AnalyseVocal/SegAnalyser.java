package com.VocalMaze.ModeleUtils.AnalyseVocal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.VocalMaze.Exceptions.ErrorLoadingFileException;;

public class SegAnalyser {
    private File segFile ;
    private int nbLocMale ;
    private int nbLocFemale ;

    private void reset () {
        nbLocMale = 0 ;
        nbLocFemale = 0 ; 
        segFile = null ; 
    }

    public int getNbLocFemale() {
        return nbLocFemale;
    }

    public int getNbLocMale() {
        return nbLocMale;
    }

    private void loadFile (String path){
        segFile = new File(path) ; 
    }

    public void analysis(String path) throws ErrorLoadingFileException {
        reset();
        loadFile(path);
        try (Scanner sc = new Scanner(segFile)) {
            sc.useDelimiter(";;");
            while (sc.hasNext()) {
                String line = sc.next();
                if (line.contains(" M ")) {
                    nbLocMale++;
                }
                if (line.contains(" F ")) {
                    nbLocFemale++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new ErrorLoadingFileException();
        }
    }
    
}
