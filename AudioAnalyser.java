import java.io.File;
import Exceptions.ErrorLoadingFileException;

public class AudioAnalyser {
    private File segFile ;
    private int nbLocMale ;
    private int nbLocFemale ;

    public void reset () {
        // TODO
        // la fonction va reset tous les champs
        // a null pour les objets
        // car on va utiliser le meme truc puour tous les audios
    }

    public int getNbLocFemale() {
        return nbLocFemale;
    }

    public int getNbLocMale() {
        return nbLocMale;
    }

    public void loadFile (String path) throws ErrorLoadingFileException {
        // TODO
        // si jamais c'est pas possible de load le file.seg avec le path
        // il faut lancer l'exception
    }

    public void analysis () {
        // TODO
        // tu parse le champs File
    }
    
}
