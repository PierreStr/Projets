package jetons;

import java.io.File;

import functions.Affichage;
import items.Player;

public class JetonArchitecture extends Jeton {

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/jetons/";

    public JetonArchitecture() {
        this.image = Affichage.makeImageView("jetons/architecture.png");
        this.smallImage = Affichage.makeImageView("jetons/architecturep.png");
    }

    @Override
    public void PlayJeton(Player p) {
        p.jetons[1] = true;
        if (p.jetons[5]){
            p.points += 3;
        }
        p.playersJetons.add(this);
    }
    
}
