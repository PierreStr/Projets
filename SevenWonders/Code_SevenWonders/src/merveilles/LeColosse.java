package merveilles;

import java.io.File;

import functions.Affichage;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;

public class LeColosse extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LeColosse() {
        super("lecolosse", Affichage.makeImageView("merveille/lecolosseVN.png", "lecolosse"), new Ressource(1, "verre"), new Ressource(3, "argile"));
        
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        plateauMilitaire.AvanceeMilitaire(p, a, 2);
        p.points += 3;
        if (p.jetons[8]){
            p.rejouer = true;
        }
    }
    
}
