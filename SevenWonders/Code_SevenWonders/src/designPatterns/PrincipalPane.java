package designPatterns;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PrincipalPane {

    private static Pane firstPane = new Pane();
    private static Pane principalPane = new Pane();
    private static Scene scene;
    private static ImageView imageViewDecision;

    public static Pane getPrincipalPane() {
        return principalPane;
    }

    public static void setPrincipalPane(Pane pane) {
        principalPane = pane;
    }


    public static Scene getScene(){
        return scene;
    }

    public static void setScene(Scene myscene){
        scene = myscene;
    }


    public static ImageView getImageViewDecision(){
        return imageViewDecision;
    }

    public static void setImageViewDecision(ImageView im){
        imageViewDecision = im;
    }


    public static Pane getFirstPane(){
        return firstPane;
    }

    public static void setFirstPane(Pane pane){
        firstPane = pane;
    }
}
