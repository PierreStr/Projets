
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Mainfile extends Application{

    
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane root = new Pane();
        Affichage.root = root;
        root.setStyle("-fx-background-color: #1e1e1e;");
        Scene scene = new Scene(root);
        Affichage.scene = scene;

        Button button_aleatoire = new Button();
        button_aleatoire.setGraphic(Affichage.makeImageView("bouttonaleatoire.png"));
        button_aleatoire.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button_aleatoire.setLayoutX(900);
        button_aleatoire.setLayoutY(476);
        button_aleatoire.setOnAction(e -> {
            scene.setRoot(Affichage.LancerPartieAleatoire(10, 0.4));
        });

        Button button_line = new Button();
        button_line.setGraphic(Affichage.makeImageView("bouttonline.png"));
        button_line.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button_line.setLayoutX(900);
        button_line.setLayoutY(52);
        button_line.setOnAction(e -> {
            scene.setRoot(Affichage.LancerPartieLigne(10));
        });

        Button button_tree = new Button();
        button_tree.setGraphic(Affichage.makeImageView("bouttontree.png"));
        button_tree.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button_tree.setLayoutX(200);
        button_tree.setLayoutY(476);
        button_tree.setOnAction(e -> {
            scene.setRoot(Affichage.LancerPartieArbre(4));
        });

        Button button_risk = new Button();
        button_risk.setGraphic(Affichage.makeImageView("bouttonrisk.png"));
        button_risk.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button_risk.setLayoutX(200);
        button_risk.setLayoutY(52);
        button_risk.setOnAction(e -> {
            scene.setRoot(Affichage.LancerPartieRisk());
        });

        root.getChildren().addAll(button_aleatoire, button_line, button_tree, button_risk);

        // Afficher la fenêtre
        primaryStage.setTitle("Plus grande composante");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        // Action pour les touches
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q || event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close(); 
            }

            if (event.getCode() == KeyCode.R){
                primaryStage.setIconified(true);
            }

            if (event.getCode() == KeyCode.C){
                scene.setRoot(Affichage.LancerPartieCercle(10));
            }

            if (event.getCode() == KeyCode.M) {
                scene.setRoot(root);
            }
            if (event.getCode() == KeyCode.BACK_SPACE){

                Node last_node;

                if (Affichage.graph.red_list.size() == Affichage.graph.blue_list.size()){
                    last_node = Affichage.graph.red_list.remove(Affichage.graph.red_list.size() - 1);
                } else {
                    last_node = Affichage.graph.blue_list.remove(Affichage.graph.blue_list.size() - 1);
                }

                last_node.circle.setMouseTransparent(false);
                last_node.circle.setFill(Color.LIGHTGRAY);
                last_node.text.setFill(Color.BLACK);
                VisualGraph.count -= 1;
            }   
            if (event.getCode() == KeyCode.ENTER){
                for (Node node : Affichage.graph.node_list){
                    node.circle.setMouseTransparent(false);
                    node.circle.setFill(Color.LIGHTGRAY);
                    node.text.setFill(Color.BLACK);
                    Affichage.graph.red_list.clear();
                    Affichage.graph.blue_list.clear();
                }
            }
        });
    }
}