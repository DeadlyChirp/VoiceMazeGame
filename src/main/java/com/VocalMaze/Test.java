package com.VocalMaze;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, URISyntaxException {
        Scanner sc = new Scanner(System.in) ; 
        String res ; 
        
        Recorder rec = new Recorder() ; 
        Terminal term = new Terminal() ; 

        System.out.print("Voici les commandes dispo :\na) '1' : pour commencer a enregistrer un vocal\n");
        System.out.println("b) '0' : pour arreter l'enregistrement du vocal");
        System.out.println("c) 'exec' : pour pouvoir executer la commande voulu");
        System.out.println("d) 'transcrire' : pour transcrire le fichier audio enregistré dans le dossier Records");
        System.out.println("e) 'exit' : pour terminer le programme");
        System.out.println("\nREMARQUE !!!! : La commande doit être écrite comme si on l'ecrivait sur un terminal, pas d'espace au debut ni a la fin");
        

        do {
            System.out.println("\nQue voulez-vous faire ?");
            res = sc.nextLine() ; 
            switch(res) {
                case "1" :
                    rec.startRecord();
                    break ; 
                case "0" :
                    rec.stopRecording();
                    break ;
                case "exec" :
                    System.out.println("Veuillez entrer la commande a executer :");
                    String cmd = sc.nextLine() ; 
                    try {
                        term.execCommand(cmd);
                    } catch (Exception e) {
                        System.out.println("Une erreur a eu lieu lors de l'execution de la commande");
                        e.printStackTrace();
                    }
                    break ;       
                
                case "transcrire" : 
                    TranscriberV3 test = new TranscriberV3("src/main/java/com/VocalMaze/Records/Audio.wav");
                    System.out.println(test.result);
                    break;
            }
        }while (!res.equals("exit"));
    }

}
