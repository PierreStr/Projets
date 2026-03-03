package functions;

import cartes.Carte;
import designPatterns.CardsScene;
import designPatterns.PrincipalPane;
import designPatterns.Pyramide;
import designPatterns.SingletonPrymaryStage;
import designPatterns.UpdateParameters;
import designPatterns.ChoosingScene;
import items.Commandant;
import items.Defausse;
import items.PlateauMilitaire;
import items.Player;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import jetons.Jeton;
import merveilles.Merveille;

public class ButtonFunctions {
    
    public static Button createButton(String graphics, double layoutx, double layouty){

        Button myButton = new Button();
        myButton.setGraphic(Affichage.makeImageView(graphics));

        myButton.setLayoutX(layoutx);
        myButton.setLayoutY(layouty);

        PrincipalPane.getPrincipalPane().getChildren().add(myButton);

        return myButton;
    }

    public static void createCardStage(String title, Carte carte){

        Stage newStage = new Stage();

        CardsScene cardsscene = new CardsScene(carte);
        // Affiche la scène dans une nouvelle fenêtre
        newStage.setScene(cardsscene.scene);
        
        newStage.setTitle(title);
        newStage.initOwner(SingletonPrymaryStage.getPrymaryStage());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();    
    }


    public static void createDefausseStage(){

        Stage newStage = new Stage();

        ChoosingScene defausseScene = new ChoosingScene(Defausse.getDefausse().cartes, "defausse");
        // Affiche la scène dans une nouvelle fenêtre
        newStage.setScene(defausseScene.scene);
        
        newStage.setTitle("Défausse");
        newStage.initOwner(SingletonPrymaryStage.getPrymaryStage());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show(); 
    }


    public static void setActionPlayCardButton(Button button, Carte carte){
        button.setOnAction(event -> {

            Player player = Player.getPlayer();
            player.rejouer = false;
            Commandant comm = Commandant.getCommandant();
            PlateauMilitaire plateau = PlateauMilitaire.getPlateauMilitaire();
            int cout = Carte.canConstructCarte(player, comm, carte);

            if ( cout > -1 && player.nbr_pieces >= cout){
                
                carte.playCarte(player, comm, plateau);
                player.nbr_pieces -= cout;

                FonctionUtile.removecard(carte);
                Affichage.updateAll(UpdateParameters.getParameters());
                Stage stage = (Stage) button.getScene().getWindow();
                stage.close();
                if (Pyramide.getCurrentAgePyramide().isNull()){
                    if (Pyramide.getCurrentAgePyramide() == Pyramide.getThirdAgePyramide()){
                        FonctionUtile.updatePointsAndDecideWinner();
                    } else {
                        Pyramide.upgradeAgePyramide();
                        Pyramide.printpyramide(PrincipalPane.getPrincipalPane(), Pyramide.getCurrentAgePyramide());
                        PauseTransition pause = new PauseTransition(Duration.seconds(1));
                        pause.setOnFinished(e -> ChoosingScene.createChoosingScene());
                        pause.play();
                    } 
                } else{
                    FonctionUtile.DisableCards(true);
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> Commandant.play());
                    pause.play(); 
                }
            }
        });
    }


    public static void setActionConstructMerveilleButton(Button button, Carte carte){
        button.setOnAction(event -> {

            Player player = Player.getPlayer();
            player.rejouer = false;
            Commandant comm = Commandant.getCommandant();
            boolean canConstructMerveille = false;

            for (Merveille merv : player.merveillesAConstruire) {
                if(Merveille.canConstructMerveille(player, comm, merv) > -1){
                    canConstructMerveille = true;
                }
            }
            if (canConstructMerveille){

                FonctionUtile.DisableCards(true);

                for (Merveille merveille : player.merveillesAConstruire) {
                    merveille.image.setDisable(false);
                    Merveille.setActionOnMerveilleClicked(merveille, carte);
                } 

                Stage stage = (Stage) button.getScene().getWindow();
                stage.close();
            }
        });
    }


    public static void setActionDefausseButton(Button button, Carte carte){
        button.setOnAction(event -> {

            Player player = Player.getPlayer();

            player.nbr_pieces += 2 + Player.getPlayer().deckCommerce.size();
            player.rejouer = false;
            Defausse.getDefausse().add(carte);
            
            FonctionUtile.removecard(carte);
            Affichage.updateAll(UpdateParameters.getParameters());
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();

            if (Pyramide.getCurrentAgePyramide().isNull()){
                if (Pyramide.getCurrentAgePyramide() == Pyramide.getThirdAgePyramide()){
                    FonctionUtile.updatePointsAndDecideWinner();
                } else {
                    Pyramide.upgradeAgePyramide();
                    Pyramide.printpyramide(PrincipalPane.getPrincipalPane(), Pyramide.getCurrentAgePyramide());
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> ChoosingScene.createChoosingScene());
                    pause.play();
                } 
            } else {
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> Commandant.play());
                pause.play();
            }
        });
    }


    public static void setJetonsAction(Jeton jeton, StackPane SP){

        jeton.image.setOnMouseClicked(event -> {

            jeton.PlayJeton(Player.getPlayer());
            Player.getPlayer().canTakeJeton = false;
            UpdateParameters params = UpdateParameters.getParameters();
            jeton.removeFrom(params.jetonsList, SP);
            Affichage.updateAll(params);

            Affichage.displaySmallJetons(jeton, params.VBjetonsP);

            FonctionUtile.DisableCards(false);

            for (Jeton myjeton : Jeton.jetonsList) {
                myjeton.image.setDisable(true);
            }

        });
    }
}
