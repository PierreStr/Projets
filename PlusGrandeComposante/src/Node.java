import java.util.Random;
import java.util.Set;

import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Node {
    
    public Circle circle;
    public int num;
    public Text text;
    public int dx = 0;
    public int dy = 0;

    public Node(Circle circle, int num){
        this.circle = circle;
        this.num = num;

        circle.setFill(Color.LIGHTGRAY);

        text = new Text(String.valueOf(num));

        // Placer le texte au centre du cercle
        text.setX(circle.getCenterX());  
        text.setY(circle.getCenterY());   

        // Modifier le style du texte
        text.setFill(Color.BLACK);  // Couleur du texte en blanc
        text.setStyle("-fx-font-size: 20px;");

        text.setMouseTransparent(true);

        double translateX = 0;
        double translateY = 0;

        if (num > 9){
            translateX = -text.getLayoutBounds().getWidth() / 2 - 5;
            translateY = text.getLayoutBounds().getHeight() / 2;
        } else {
            translateX = -text.getLayoutBounds().getWidth() / 2 - 2;
            translateY = text.getLayoutBounds().getHeight() / 2;
        }

        text.setTranslateX(translateX);
        text.setTranslateY(translateY);
    }


    public double getX(){
        return circle.getCenterX();
    }

    public double getY(){
        return circle.getCenterY();
    }

    public void setX(double x){
        circle.setCenterX(x);
    }

    public void setY(double y){
        circle.setCenterY(y);
    }

    public void moveTo(double x, double y){
        this.setX(x);
        this.setY(y);

        text.setX(x);  
        text.setY(y);

        double translateX = 0;
        double translateY = 0;

        if (num > 9){
            translateX = -text.getLayoutBounds().getWidth() / 2 - 5;
            translateY = text.getLayoutBounds().getHeight() / 2;
        } else {
            translateX = -text.getLayoutBounds().getWidth() / 2 - 2;
            translateY = text.getLayoutBounds().getHeight() / 2;
        }

        text.setTranslateX(translateX);
        text.setTranslateY(translateY);
    }


    public void display(Pane pane){
        pane.getChildren().addAll(circle, text);
    }


    public static boolean circleIntersection(Set<Circle> circle_set, int x, int y, int radius){

        for (Circle c1 : circle_set){
            double x_1 = c1.getCenterX();
            double y_1 = c1.getCenterY();
    
            double dist = Math.sqrt(Math.pow(x - x_1, 2) + Math.pow(y - y_1, 2));
            
            if (dist > c1.getRadius() + radius + 20){
                return true;
            }
        }

        return false;
    }


    public static int[] findPosition(Set<Circle> circle_set, int radius){

        int screenWidth = 1600;
        int screenHeight = 900; 
        
        Random random = new Random();
        int x = random.nextInt(screenWidth - 100) + 50;
        int y = random.nextInt(screenHeight - 100) + 50;

        while(!circleIntersection(circle_set, x, y, radius)){
            x = random.nextInt(screenWidth - 100) + 50;
            y = random.nextInt(screenHeight - 100) + 50;
        }

        return new int[]{x, y};
    }

    public void makeNodeClicable(VisualGraph graph){
       
        circle.setOnMouseClicked(e->{
            if (VisualGraph.count % 2 == 0){
                circle.setFill(Color.BLUE);
                graph.blue_list.add(this);
            } else {
                circle.setFill(Color.RED);
                graph.red_list.add(this);
            }
            VisualGraph.count ++;
            text.setFill(Color.WHITE);
            circle.setMouseTransparent(true);

            if (graph.isEndGame()){

                int score_red = graph.getBiggestComponents("red");
                int score_blue = graph.getBiggestComponents("blue");
                
                Object[] anims = null;
                
                if (score_red > score_blue){
                    anims = Affichage.finPartieAnimation("red", graph);
                    graph.pane.getChildren().addAll((ImageView)anims[0], (Button)anims[1], (Button)anims[2]);
                    ParallelTransition first = (ParallelTransition)anims[3];
                    ParallelTransition second = (ParallelTransition)anims[4];
                    first.play();
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> {second.play();});
                    pause.play();
                } else if (score_red < score_blue){
                    anims = Affichage.finPartieAnimation("blue", graph);
                    graph.pane.getChildren().addAll((ImageView)anims[0], (Button)anims[1], (Button)anims[2]);
                    ParallelTransition first = (ParallelTransition)anims[3];
                    ParallelTransition second = (ParallelTransition)anims[4];
                    first.play();
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> {second.play();});
                    pause.play();
                } else {
                    anims = Affichage.finPartieAnimation("tie", graph);
                    graph.pane.getChildren().addAll((ImageView)anims[0], (Button)anims[1], (Button)anims[2]);
                    ParallelTransition first = (ParallelTransition)anims[3];
                    ParallelTransition second = (ParallelTransition)anims[4];
                    first.play();
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> {second.play();});
                    pause.play();
                }

                
            }
        });
    }
}


