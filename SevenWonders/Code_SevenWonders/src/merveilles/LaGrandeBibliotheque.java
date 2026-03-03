package merveilles;

import java.io.File;

import designPatterns.ChoosingScene;
import designPatterns.SingletonPrymaryStage;
import designPatterns.UpdateParameters;
import functions.Affichage;
import items.Commandant;
import items.PlateauMilitaire;
import items.Player;
import items.Ressource;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LaGrandeBibliotheque extends Merveille{

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/merveille/";

    public LaGrandeBibliotheque() {
        super("lagrandebibliotheque", Affichage.makeImageView("merveille/lagrandebibliothequeVN.png", "lagrandebibliotheque"), new Ressource(1, "verre"), new Ressource(3, "bois"));
    }

    @Override
    public void playMerveille(Player p, Player a, PlateauMilitaire plateauMilitaire) {
        p.points += 4;
        if (p.jetons[8]){
            p.rejouer = true;
        }

        if (!(p instanceof Commandant)){
            Stage newStage = new Stage();

            ChoosingScene lagrandebibliotheque = new ChoosingScene(UpdateParameters.getParameters().jetonsRestants);
            // Affiche la scène dans une nouvelle fenêtre
            newStage.setScene(lagrandebibliotheque.scene);
            
            newStage.setTitle("La grande bibliothèque");
            newStage.initOwner(SingletonPrymaryStage.getPrymaryStage());
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.show();
        }
    }
}