package designPatterns;

import cartes.Carte;
import functions.Affichage;
import functions.ButtonFunctions;
import functions.Lancement;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CardsScene {

    static double factorl = Lancement.l/900;
    public Scene scene;

    public CardsScene(Carte carte){

        StackPane pane = new StackPane();

        ImageView backgroundImageView = Affichage.makeImageView("cartes/"+ carte.name + "g.png");
        
        VBox VBbutton = new VBox(15);
        VBbutton.setTranslateY(20);

        Button button1 = new Button();
        button1.setGraphic(Affichage.makeImageView("boutons/boutoncarte.png", false));
        ButtonFunctions.setActionPlayCardButton(button1, carte);

        Button button2 = new Button();
        button2.setGraphic(Affichage.makeImageView("boutons/boutonmerveille.png", false));
        ButtonFunctions.setActionConstructMerveilleButton(button2, carte);

        Button button3 = new Button();
        button3.setGraphic(Affichage.makeImageView("boutons/boutondefausse.png", false));
        ButtonFunctions.setActionDefausseButton(button3, carte);

        button1.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button3.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");


        VBbutton.getChildren().addAll(button1, button2, button3);
        VBbutton.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(backgroundImageView, VBbutton);

        this.scene = new Scene(pane);
    }
}