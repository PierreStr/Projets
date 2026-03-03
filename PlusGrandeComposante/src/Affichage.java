import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;


public class Affichage {


    public static Pane root;
    public static Scene scene;
    public static VisualGraph graph;

    
    // return une imageView de l'image à l'emplacement filePath
    public static ImageView makeImageView(String filePath, String ID){

        InputStream input = Affichage.class.getResourceAsStream("/images/"+filePath);

        if (input != null) {
            Image newImage = new Image(input);
            ImageView imageView = new ImageView(newImage);
            imageView.setId(ID);

            return imageView;
        }

        return null;
    }


    public static ImageView makeImageView(String filePath){

        InputStream input = Affichage.class.getResourceAsStream("/images/"+filePath);

        if (input != null) {
            Image newImage = new Image(input);
            ImageView imageView = new ImageView(newImage);
            return imageView;
        }

        return null;
    }


    public static Pane LancerPartieAleatoire(int nbr_node, double densite){

        int[] nodes = new int[nbr_node];
        for (int i = 0; i < nbr_node; i++) {
            nodes[i] = i + 1;
        }

        ArrayList<Tuple> temp = new ArrayList<>(); 
        Random random = new Random();

        for (int i = 1; i < nbr_node + 1; i++) {
            for (int j = i+1; j < nbr_node + 1; j++) {
                int rand = random.nextInt(100);

                if (rand < densite*100){
                    temp.add(new Tuple(i, j));
                }
            }
        }
        
        Tuple[] edges = new Tuple[temp.size()];
        for (int i = 0; i < edges.length; i++) {
            edges[i] = temp.get(i);
        }

        Pane pane = new Pane();

        graph = new VisualGraph(nodes, edges, 20, pane);
        graph.fixPositions();
        graph.display(pane);

        return pane;
    }




    public static Pane LancerPartieLigne(int nbr_node){

        int[] nodes = new int[nbr_node];
        for (int i = 0; i < nbr_node; i++) {
            nodes[i] = i + 1;
        }

        Tuple[] edges = new Tuple[nbr_node - 1];

        for (int i = 0; i < edges.length; i++) {
            edges[i] = new Tuple(i+1, i+2);
        }

        Pane pane = new Pane();

        graph = new VisualGraph(nodes, edges, 20, pane);

        graph.makeLinePosition();
        graph.display(pane);

        return pane;
    }


    public static Pane LancerPartieCercle(int nbr_node){

        int[] nodes = new int[nbr_node];
        for (int i = 0; i < nbr_node; i++) {
            nodes[i] = i + 1;
        }

        Tuple[] edges = new Tuple[nbr_node];

        for (int i = 0; i < edges.length; i++) {
            edges[i] = new Tuple(i+1, i+2);
        }

        edges[nbr_node-1] = new Tuple(1, nbr_node);

        Pane pane = new Pane();

        graph = new VisualGraph(nodes, edges, 20, pane);

        graph.makeCirclePosition();
        graph.display(pane);

        return pane;
    }


    public static Pane LancerPartieArbre(int nbr_couche) {

        int nbr_node = 1;
        int nbr_node_start_for_edge = 1;

        for (int i = 0; i < nbr_couche - 1; i++){
            nbr_node += 3 * Math.pow(2, i);

            if (i < nbr_couche - 2){
                nbr_node_start_for_edge += 3 * Math.pow(2, i);
            }

        }
        
        int[] nodes = new int[nbr_node];
        for (int i = 0; i < nbr_node; i++) {
            nodes[i] = i + 1;
        }


        Tuple[] edges = new Tuple[nbr_node-1];
        int iter = 0;
        for (int i = 1; i < nbr_node_start_for_edge+1; i++) {
            if (i == 1){
                edges[iter] = new Tuple(1, 2);
                edges[iter+1] = new Tuple(1, 3);
                edges[iter+2] = new Tuple(1, 4);
                iter += 3;
            } else {
                edges[iter] = new Tuple(i, 2*i+1);
                edges[iter + 1] = new Tuple(i, 2*i+2);
                iter += 2;
            }
        }


        Pane pane = new Pane();

        graph = new VisualGraph(nodes, edges, 20, pane);
        graph.makeTreePosition(nbr_couche);
        graph.display(pane);

        return pane;
    }


    public static Pane LancerPartieRisk(){

        int[] nodes = new int[42];
        for (int i = 0; i < 42; i++) {
            nodes[i] = i + 1;
        }

        Tuple[] edges = Edge.makeRiskEdges();

        Pane pane = new Pane();

        ImageView fondRisk = makeImageView("plateaurisk.png");
        fondRisk.setFitWidth(1600);
        fondRisk.setFitHeight(900);
        pane.getChildren().addAll(fondRisk);

        graph = new VisualGraph(nodes, edges, 20, pane);
        graph.makeRiskPosition();
        graph.display(pane, true);

        return pane;
    }

    public static Object[] finPartieAnimation(String color, VisualGraph graph){

    // ImageView de la carte
        ImageView imageView = makeImageView(color + "won.png");
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);
        imageView.setLayoutX(400);
        imageView.setLayoutY(150);
        imageView.setOpacity(0);
        imageView.setScaleX(0.5);
        imageView.setScaleY(0.5);
        imageView.setRotate(0);


        // Boutons à placer
        Button nodeButton1 = new Button();
        nodeButton1.setGraphic(makeImageView("bouttonrejouer"+color+".png"));
        nodeButton1.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        nodeButton1.setLayoutX(700);
        nodeButton1.setLayoutY(475);
        nodeButton1.setOpacity(0);

        Button nodeButton2 = new Button();
        nodeButton2.setGraphic(makeImageView("bouttonmenu"+color+".png"));
        nodeButton2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        nodeButton2.setLayoutX(700);
        nodeButton2.setLayoutY(575);
        nodeButton2.setOpacity(0);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(2), imageView);
        scale.setToX(1);
        scale.setToY(1);

        FadeTransition fadeImg = new FadeTransition(Duration.seconds(2), imageView);
        fadeImg.setToValue(1);

        ParallelTransition imageAnim = new ParallelTransition(scale, fadeImg);

        // Animation des boutons : fade-in
        FadeTransition fadeBtn1 = new FadeTransition(Duration.seconds(3), nodeButton1);
        fadeBtn1.setToValue(1);

        FadeTransition fadeBtn2 = new FadeTransition(Duration.seconds(3), nodeButton2);
        fadeBtn2.setToValue(1);

        ParallelTransition fadeButtons = new ParallelTransition(fadeBtn1, fadeBtn2);


        nodeButton1.setOnAction(e -> {
            for (Node node : graph.node_list){
                node.circle.setMouseTransparent(false);
                node.circle.setFill(Color.LIGHTGRAY);
                node.text.setFill(Color.BLACK);
                graph.red_list.clear();
                graph.blue_list.clear();
                graph.pane.getChildren().removeAll(imageView, nodeButton1, nodeButton2);
            }
        });


        nodeButton2.setOnAction(e -> {
            scene.setRoot(root);
        });

        return new Object[]{imageView, nodeButton1, nodeButton2, imageAnim, fadeButtons};
    }

}