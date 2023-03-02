import java.io.File;
import java.util.Scanner;

import Exceptions.ErrorLoadingFileException;

public class AudioAnalyser {
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

    public void analysis (String path) throws ErrorLoadingFileException {
        reset();
        loadFile(path);
        Scanner sc ;
        String line ; 
        try {
            sc = new Scanner(segFile) ; 
        } catch (Exception e) {
           throw new ErrorLoadingFileException() ;
        }
        sc.useDelimiter(";;") ; 
        while(sc.hasNext()) {
            line = sc.next() ; 
            if (line.contains(" M ")) nbLocMale++ ;
            if (line.contains(" F ")) nbLocFemale++ ; 
        }
        sc.close();
    }
    
}
