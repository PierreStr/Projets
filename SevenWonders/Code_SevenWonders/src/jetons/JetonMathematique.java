package jetons;

import java.io.File;

import functions.Affichage;
import items.Player;

public class JetonMathematique extends Jeton{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/jetons/";

    public JetonMathematique() {
        this.image = Affichage.makeImageView("jetons/mathematiques.png");
        this.smallImage = Affichage.makeImageView("jetons/mathematiquesp.png");
    }

    @Override
    public void PlayJeton(Player p) {
        p.jetons[5] = true;
        for (Boolean jeton : p.jetons) {
            if (jeton){
                p.points += 3;
            }
        }
        p.playersJetons.add(this);
    }
    
}
