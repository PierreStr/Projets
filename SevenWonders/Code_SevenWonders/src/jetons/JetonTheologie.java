package jetons;

import java.io.File;

import functions.Affichage;
import items.Player;

public class JetonTheologie extends Jeton{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/jetons/";

    public JetonTheologie() {
        this.image = Affichage.makeImageView("jetons/theologie.png");
        this.smallImage = Affichage.makeImageView("jetons/theologiep.png");
    }

    @Override
    public void PlayJeton(Player p) {
        p.jetons[8] = true;
        if (p.jetons[5]){
            p.points += 3;
        }
        p.playersJetons.add(this);
    }  
}
