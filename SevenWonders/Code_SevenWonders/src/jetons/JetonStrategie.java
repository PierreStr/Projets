package jetons;

import java.io.File;

import functions.Affichage;
import items.Player;

public class JetonStrategie extends Jeton{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/jetons/";

    public JetonStrategie() {
        this.image = Affichage.makeImageView("jetons/strategie.png");
        this.smallImage = Affichage.makeImageView("jetons/strategiep.png");
    }

    @Override
    public void PlayJeton(Player p) {
        p.jetons[7] = true;
        if (p.jetons[5]){
            p.points += 3;
        }
        p.playersJetons.add(this);
    }
    
}
