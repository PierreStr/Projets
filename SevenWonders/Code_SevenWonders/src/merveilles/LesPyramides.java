package merveilles;

import java.io.File;

import functions.Affichage;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;

public class LesPyramides extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LesPyramides() {
        super("lespyramides", Affichage.makeImageView("merveille/lespyramidesVN.png", "lespyramides"), new Ressource(1, "argile"), new Ressource(3, "pierre"));
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.points += 9;
        if (p.jetons[8]){
            p.rejouer = true;
        }
    }
    
}
