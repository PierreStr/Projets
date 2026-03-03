package jetons;

import java.io.File;

import functions.Affichage;
import items.Player;

public class JetonPhilosophie extends Jeton {

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/jetons/";

    public JetonPhilosophie() {
        this.image = Affichage.makeImageView("jetons/philosophie.png");
        this.smallImage = Affichage.makeImageView("jetons/philosophiep.png");
    }

    @Override
    public void PlayJeton(Player p) {
        p.points += 7;
        p.jetons[6] = true;
        if (p.jetons[5]){
            p.points += 3;
        }
        p.playersJetons.add(this);
    }
    
}
