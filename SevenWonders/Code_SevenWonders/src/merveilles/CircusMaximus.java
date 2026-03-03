package merveilles;

import java.io.File;

import designPatterns.ChoosingScene;
import designPatterns.SingletonPrymaryStage;
import functions.Affichage;
import items.Commandant;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CircusMaximus extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public CircusMaximus() {
        super("circusmaximus", Affichage.makeImageView("merveille/circusmaximusVN.png", "circusmaximus"), new Ressource(1, "verre"), new Ressource(1, "bois"), new Ressource(2, "pierre"));
        
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire){

        plateauMilitaire.AvanceeMilitaire(p, a, 1);
        p.points += 3;

        if (p.jetons[8]){
            p.rejouer = true;
        }

        if (!(p instanceof Commandant) && Commandant.getCommandant().deckRessourceSecondaire.size() != 0){
            Stage newStage = new Stage();

            ChoosingScene circusMaximusscene = new ChoosingScene(Commandant.getCommandant().deckRessourceSecondaire, "pasdefausse");
            // Affiche la scène dans une nouvelle fenêtre
            newStage.setScene(circusMaximusscene.scene);
            
            newStage.setTitle("Circus Maximus");
            newStage.initOwner(SingletonPrymaryStage.getPrymaryStage());
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.show();
        }
    }
}
