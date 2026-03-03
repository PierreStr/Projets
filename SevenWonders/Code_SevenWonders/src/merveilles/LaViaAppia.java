package merveilles;

import java.io.File;

import functions.Affichage;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;

public class LaViaAppia extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LaViaAppia() {
        super("laviaappia", Affichage.makeImageView("merveille/laviaappiaVN.png", "laviaappia"), new Ressource(1, "papyrus"), new Ressource(2, "argile"), new Ressource(2, "pierre"));
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.nbr_pieces += 3;
        a.nbr_pieces = Math.max(0, a.nbr_pieces - 3);
        p.points += 3;
        p.rejouer = true;
    }
}