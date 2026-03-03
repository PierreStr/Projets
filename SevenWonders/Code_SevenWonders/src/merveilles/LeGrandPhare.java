package merveilles;

import java.io.File;

import functions.Affichage;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;

public class LeGrandPhare extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LeGrandPhare() {
        super("legrandphare", Affichage.makeImageView("merveille/legrandphareVN.png", "legrandphare"), new Ressource(2, "papyrus"), new Ressource(1, "pierre"), new Ressource(1, "bois"));

    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.points += 4;
        p.jokerprimairemerveille = true;
        if (p.jetons[8]){
            p.rejouer = true;
        }
    }
}
