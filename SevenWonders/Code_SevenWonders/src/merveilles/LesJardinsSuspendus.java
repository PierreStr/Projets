package merveilles;

import java.io.File;

import functions.Affichage;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;

public class LesJardinsSuspendus extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LesJardinsSuspendus() {
        super("lesjardinssuspendus", Affichage.makeImageView("merveille/lesjardinssuspendusVN.png", "lesjardinssuspendus"), new Ressource(1, "papyrus"), new Ressource(1, "verre"), new Ressource(2, "bois"));
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.nbr_pieces += 6;
        p.points += 3;
        p.rejouer = true;
    }
    
}
