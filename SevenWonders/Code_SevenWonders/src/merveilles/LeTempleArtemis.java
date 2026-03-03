package merveilles;

import java.io.File;

import functions.Affichage;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;

public class LeTempleArtemis extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LeTempleArtemis() {
        super("letempleartemis", Affichage.makeImageView("merveille/letempleartemisVN.png", "letempleartemis"), new Ressource(1, "papyrus"), new Ressource(1, "verre"), new Ressource(1, "pierre"), new Ressource(1, "bois"));
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.nbr_pieces += 12;
        p.rejouer = true;
    }
    
}
