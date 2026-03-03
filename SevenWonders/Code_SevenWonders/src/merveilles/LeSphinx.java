package merveilles;

import java.io.File;

import functions.Affichage;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;

public class LeSphinx extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LeSphinx() {
        super("lesphinx", Affichage.makeImageView("merveille/lesphinxVN.png", "lesphinx"), new Ressource(2, "verre"), new Ressource(1, "argile"), new Ressource(1, "pierre"));
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.points += 6;
        p.rejouer = true;
    }
    
}
