package jetons;

import java.io.File;

import functions.Affichage;
import items.Player;

public class JetonMaconnerie extends Jeton{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/jetons/";

    public JetonMaconnerie() {
        this.image = Affichage.makeImageView("jetons/maconnerie.png");
        this.smallImage = Affichage.makeImageView("jetons/maconneriep.png");
    }

    @Override
    public void PlayJeton(Player p) {
        p.jetons[4] = true;
        if (p.jetons[5]){
            p.points += 3;
        }
        p.playersJetons.add(this);
    }
    
}
