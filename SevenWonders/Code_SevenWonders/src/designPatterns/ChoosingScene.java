package designPatterns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cartes.Carte;
import functions.Affichage;
import functions.FonctionUtile;
import functions.Lancement;
import items.Commandant;
import items.Defausse;
import items.PlateauMilitaire;
import items.Player;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import jetons.Jeton;

public class ChoosingScene {

    static String PATH = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/images/";

    public Scene scene;
    public ArrayList<Carte> cardsList;
    public ArrayList<Jeton> jetonsList;
    public String type;

    static double factorL = Lancement.L/1600;
    static double factorl = Lancement.l/900;

    public ChoosingScene(List<Carte> list, String type){

        this.type = type;
        this.cardsList = (ArrayList<Carte>) list;

        Pane pane = new Pane();
        Stage stage = SingletonPrymaryStage.getPrymaryStage();
        ImageView backgroundImageView = Affichage.makeImageView("fondChoixCartes.png", false);
        backgroundImageView.fitWidthProperty().bind(stage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(stage.heightProperty());
        pane.getChildren().add(backgroundImageView);
        
        if (type == "defausse" || type == "pasdefausse"){
            FonctionUtile.DisableCards(true);
        }
        disposeAndSetActionCarte(dispatchCartes(list), pane, type);
        
        this.scene = new Scene(pane, 1319.7, 700);
    }

    public ChoosingScene(List<Jeton> list){

        this.jetonsList = (ArrayList<Jeton>) list;

        Pane pane = new Pane();
        Stage stage = SingletonPrymaryStage.getPrymaryStage();
        ImageView backgroundImageView = Affichage.makeImageView("fondChoixCartes.png", false);
        backgroundImageView.fitWidthProperty().bind(stage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(stage.heightProperty());
        pane.getChildren().add(backgroundImageView);
        
        FonctionUtile.DisableCards(true);
        disposeAndSetActionJeton(pane);
        
        this.scene = new Scene(pane, 400, 212.17);
    }


    public ChoosingScene(){
        Pane pane = new Pane();
        Stage mystage = SingletonPrymaryStage.getPrymaryStage();
        ImageView backgroundImageView = Affichage.makeImageView("fondChoixCartes.png", false);
        backgroundImageView.fitWidthProperty().bind(mystage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(mystage.heightProperty());
        pane.getChildren().add(backgroundImageView);
        
        ImageView parchemin = Affichage.makeImageView("boutons/parchemin.png", false);
        parchemin.setLayoutX(204.92);
        parchemin.setLayoutY(20);

        HBox hb = new HBox(100);

        Button button1 = new Button();
        button1.setGraphic(Affichage.makeImageView("boutons/oui.png"));
        button1.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button1.setOnAction(e -> {
            Stage stage = (Stage) button1.getScene().getWindow();
            stage.close();
            FonctionUtile.DisableCards(false);
        });

        Button button2 = new Button();
        button2.setGraphic(Affichage.makeImageView(PATH+"boutons/non.png"));
        button2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button2.setOnAction(e -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> Commandant.play());
            pause.play();
            Stage stage = (Stage) button2.getScene().getWindow();
            stage.close();
            FonctionUtile.DisableCards(false);
        });

        hb.getChildren().addAll(button1, button2);
        hb.setLayoutX(115);
        hb.setLayoutY(250);

        pane.getChildren().addAll(parchemin, hb);
        this.scene = new Scene(pane, 659.85, 350);
    }


    public Carte[][] dispatchCartes (List<Carte> list){

        Carte[][] matrix = new Carte[4][4];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new Carte();
            }
        }

        if (list.size() < 5){

            for (int i = 0; i < list.size(); i++) {
                matrix[1][i] = list.get(i);
            }

        } else if (list.size() < 9){
            for (int i = 0; i < 4; i++) {
                matrix[0][i] = list.get(i);
            }
            for (int i = 4; i < list.size(); i++) {
                matrix[2][i-4] = list.get(i);   
            }
        } else {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < Math.min(4, list.size()-(i*4)); j++) {
                    matrix[i][j] = list.get((i*4) + j);
                }
            }
        }

        return matrix;  
    }


    public void disposeAndSetActionCarte (Carte[][] matrix, Pane pane, String type){

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if(matrix[i][j].name != null){
                    ImageView myImageView = Affichage.makeImageView("cartes/"+matrix[i][j].name+"g.png", false);

                    myImageView.setLayoutX(60 + j*315);
                    myImageView.setLayoutY(59 + i*100);

                    if (type == "defausse"){
                        actionForConstructCard(myImageView, matrix[i][j]);
                    } else if (type == "pasdefausse"){
                        actionForDestroyCard(myImageView, matrix[i][j]);
                    }

                    pane.getChildren().add(myImageView);
                }
            }
        }
    }


    public void disposeAndSetActionJeton(Pane pane){

        Jeton[] listJetons = new Jeton[3];

        listJetons[0] = FonctionUtile.hasardElement(UpdateParameters.getParameters().jetonsRestants);
        listJetons[1] = FonctionUtile.hasardElement(UpdateParameters.getParameters().jetonsRestants);
        listJetons[2] = FonctionUtile.hasardElement(UpdateParameters.getParameters().jetonsRestants);

        for (int i = 0; i < listJetons.length; i++) {
            ImageView myImageView = listJetons[i].image;

            myImageView.setLayoutX((40 + i*112)*factorL);
            myImageView.setLayoutY(66.8*factorl);

            actionForPlayJeton(myImageView, listJetons[i]);

            pane.getChildren().add(myImageView);
        }
    }


    public void actionForConstructCard(ImageView myimageview, Carte carte){

        myimageview.setOnMouseClicked(event -> {

            carte.playCarte(Player.getPlayer(), Commandant.getCommandant(), PlateauMilitaire.getPlateauMilitaire());
            Defausse.getDefausse().remove(carte);
            Affichage.updateAll(UpdateParameters.getParameters());
            Stage stage = (Stage) myimageview.getScene().getWindow();
            stage.close();
            FonctionUtile.DisableCards(false);

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

    public void actionForDestroyCard(ImageView myimageview, Carte carte){

        myimageview.setOnMouseClicked(event -> {

            carte.DestroyCarte();

            Affichage.updateAll(UpdateParameters.getParameters());
            Stage stage = (Stage) myimageview.getScene().getWindow();
            stage.close();
            FonctionUtile.DisableCards(false);

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

    public void actionForPlayJeton(ImageView myimageview, Jeton jeton){

        myimageview.setOnMouseClicked(event -> {

            jeton.PlayJeton(Player.getPlayer());

            Affichage.displaySmallJetons(jeton, UpdateParameters.getParameters().VBjetonsP);

            Affichage.updateAll(UpdateParameters.getParameters());
            Stage stage = (Stage) myimageview.getScene().getWindow();
            stage.close();
            FonctionUtile.DisableCards(false);

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


    public static void createChoosingScene(){
        
        Stage newStage = new Stage();

        // Affiche la scène dans une nouvelle fenêtre
        newStage.setScene(new ChoosingScene().scene);
        
        newStage.setTitle("Choisir qui commence");
        newStage.initOwner(SingletonPrymaryStage.getPrymaryStage());
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();
    }

}
