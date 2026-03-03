package merveilles;

import java.io.File;

import functions.Affichage;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;

public class LePiree extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LePiree() {
        super("lepiree", Affichage.makeImageView("merveille/lepireeVN.png", "lepiree"), new Ressource(1, "argile"), new Ressource(1, "pierre"), new Ressource(2, "bois"));
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.jokersecondairemerveille = true;
        p.points += 2;
        p.rejouer = true;
    }
    
}
