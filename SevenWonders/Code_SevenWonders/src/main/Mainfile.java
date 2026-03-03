package main;

import java.io.File;
import java.io.InputStream;

import designPatterns.PrincipalPane;
import designPatterns.SingletonPrymaryStage;
import functions.Affichage;
import functions.FonctionUtile;
import functions.Lancement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class Mainfile extends Application{

    static double factorL = Lancement.L/1600;
    static double factorl = Lancement.l/900;
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        
        SingletonPrymaryStage.setPrymaryStage(primaryStage);
        
        Pane firstPane = new Pane();
        Scene firstScene = new Scene(firstPane);
        PrincipalPane.setScene(firstScene);
        PrincipalPane.setFirstPane(firstPane);

        // définition du fond 
        InputStream input = Affichage.class.getResourceAsStream("/images/premierFond.png");

        Image newImage = new Image(input);
        // Utilisez l'image dans votre application
        ImageView backgroundImageView = new ImageView(newImage);

        backgroundImageView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(primaryStage.heightProperty());
        firstPane.getChildren().add(backgroundImageView);

        VBox vb = new VBox(50*factorl);

        Button button1 = new Button();
        button1.setGraphic(Affichage.makeImageView("boutons/jouer.png"));
        button1.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button1.setOnAction(e -> {
            firstScene.setRoot(Lancement.ChoisirCommandants());
        });

        Button button2 = new Button();
        button2.setGraphic(Affichage.makeImageView("boutons/règles.png"));
        button2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button2.setOnAction(e -> {
            // Spécifiez le chemin du fichier PDF que vous souhaitez ouvrir
            String filePath = new File(System.getProperty("user.dir")).getParent().replace("\\", "/")+"/Code_SevenWonders/src/resources/règles du jeu.pdf";
            File file = new File(filePath);

            // Vérifiez si le fichier existe
            if (file.exists()) {
                // Ouvrir le fichier PDF avec l'application par défaut du système
                FonctionUtile.openPDF(file);
            } else {
                System.out.println("Le fichier spécifié n'existe pas.");
            }
        });

        vb.getChildren().addAll(button1, button2);
        vb.setLayoutX(687.5*factorL);
        vb.setLayoutY(400*factorl);

        firstPane.getChildren().addAll(vb);

        primaryStage.setScene(firstScene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setTitle("Seven Wonders");
        primaryStage.show();

        firstScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q || event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close(); 
            } else if (event.getCode() == KeyCode.M){
                firstScene.setRoot(firstPane);
            }
        });
    }
}




