package com.VocalMaze.ModeleUtils.AnalyseVocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Terminal {
    private String []command ; 
    private ProcessBuilder processBuilder ; 
    private Process process ; 
    private BufferedReader output ; 

    public Terminal () {
        command = new String[1] ;
        command[0] = "" ; 
        processBuilder = new ProcessBuilder(command) ; 
    }

    // Transforme la commande en String []
    private static String [] getCommand (String command) {return command.trim().split("\\s+");}

    // Affiche le resultat de la commande sur la Sortie de commande
    private void showOutput () throws IOException {
        String line ; 
        while ((line = output.readLine()) != null) {
            System.out.println(line);
        }
    }

    // Execute une commande Shell
    public void execCommand (String command) {
        processBuilder.command(getCommand(command)) ; 
        processBuilder.directory(null) ;
        try {
            process = processBuilder.start() ; 
            output = new BufferedReader(new InputStreamReader(process.getInputStream())) ;
            showOutput(); 
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

}
