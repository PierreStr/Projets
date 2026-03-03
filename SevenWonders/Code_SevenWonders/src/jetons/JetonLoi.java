package jetons;

import java.io.File;

import functions.Affichage;
import items.Player;

public class JetonLoi extends Jeton {

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/jetons/";

    public JetonLoi() {
        this.image = Affichage.makeImageView("jetons/loi.png");
        this.smallImage = Affichage.makeImageView("jetons/loip.png");
    }

    @Override
    public void PlayJeton(Player p) {
        
        p.AvanceeScientifique.add("balance");
        p.jetons[3] = true;
        if (p.jetons[5]){
            p.points += 3;
        }
        p.playersJetons.add(this);
    }
}
