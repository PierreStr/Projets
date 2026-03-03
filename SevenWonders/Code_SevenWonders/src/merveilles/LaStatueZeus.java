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

public class LaStatueZeus extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LaStatueZeus() {
        super("lastatuezeus", Affichage.makeImageView("merveille/lastatuezeusVN.png", "lastatuezeus"), new Ressource(2, "papyrus"), new Ressource(1, "argile"), new Ressource(1, "bois"), new Ressource(1, "pierre"));
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        plateauMilitaire.AvanceeMilitaire(p, a, 1);
        p.points += 3;
        if (p.jetons[8]){
            p.rejouer = true;
        }

        if (!(p instanceof Commandant) && Commandant.getCommandant().deckRessourcePrimaire.size() != 0){
            Stage newStage = new Stage();

            ChoosingScene circusMaximusscene = new ChoosingScene(Commandant.getCommandant().deckRessourcePrimaire, "pasdefausse");
            // Affiche la scène dans une nouvelle fenêtre
            newStage.setScene(circusMaximusscene.scene);
            
            newStage.setTitle("La statue de Zeus");
            newStage.initOwner(SingletonPrymaryStage.getPrymaryStage());
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.show();
        }
    }
}
