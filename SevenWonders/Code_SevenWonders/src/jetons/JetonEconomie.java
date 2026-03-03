package jetons;

import java.io.File;

import functions.Affichage;
import items.Player;

public class JetonEconomie extends Jeton{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/jetons/";

    public JetonEconomie() {
        this.image = Affichage.makeImageView("jetons/economie.png");
        this.smallImage = Affichage.makeImageView("jetons/economiep.png");
    }

    @Override
    public void PlayJeton(Player p) {
        p.jetons[2] = true;
        if (p.jetons[5]){
            p.points += 3;
        }
        p.playersJetons.add(this);
    }
    
}
