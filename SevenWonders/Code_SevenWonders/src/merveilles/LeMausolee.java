package merveilles;

import java.io.File;

import functions.Affichage;
import functions.ButtonFunctions;
import items.Commandant;
import items.Defausse;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;

public class LeMausolee extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LeMausolee() {
        super("lemausolee", Affichage.makeImageView("merveille/lemausoleeVN.png", "lemausolee"), new Ressource(1, "papyrus"), new Ressource(2, "verre"), new Ressource(2, "argile"));
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.points += 2;
        if (p.jetons[8]){
            p.rejouer = true;
        }

        if (!(p instanceof Commandant) && Defausse.getDefausse().cartes.size() != 0){

            ButtonFunctions.createDefausseStage();
        }
    }
}
