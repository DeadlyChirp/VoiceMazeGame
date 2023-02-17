import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in) ; 
        String res ; 
        
        Recorder rec = new Recorder() ; 
        Terminal term = new Terminal() ; 

        System.out.print("voici les commandes dispo :\na) '1' : pour commencer a enregistrer un vocal\n");
        System.out.println("b) '0' : pour arreter l'enregistrement du vocal");
        System.out.println("c) 'exec' : pour pouvoir executer la commande voulu");
        System.out.println("REMARQUE !!!! : la commande doir etre ecrite comme si on l'ecrivait sur un terminal pas d'espace au debut ni a la fin");
        System.out.println("d) 'Exit' : pour terminer le programme");

        do {
            System.out.println("Que voulez-vous faire ?");
            res = sc.nextLine() ; 
            switch(res) {
                case "1" :
                    rec.startRecord();
                    break ; 
                case "0" :
                    rec.startRecord();
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
            }
        }while (!res.equals("Exit"));
    }

}
