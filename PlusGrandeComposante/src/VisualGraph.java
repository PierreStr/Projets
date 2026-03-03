import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VisualGraph {
    
    public Node[] node_list;
    public Edge[] edge_list;
    public static int count;
    public ArrayList<Node> blue_list = new ArrayList<>();
    public ArrayList<Node> red_list = new ArrayList<>();
    public Pane pane; 

    double width = 1400;
    double height = 700;
    double area = width * height;
    double k;
    double temperature = width / 10;


    public VisualGraph(int[] nodes, Tuple[] edges, int radius, Pane pane){

        this.pane = pane;

        edge_list = new Edge[edges.length];
        node_list = new Node[nodes.length];

        Random random = new Random();
        int x = random.nextInt(1600 - 2 * radius) + radius;
        int y = random.nextInt(900 - 2 * radius) + radius;

        Set<Circle> circle_set = new HashSet<>(); 
        Circle newCircle = new Circle(x, y, radius);
        circle_set.add(newCircle);

        for (Integer node : nodes){
            int[] positions = Node.findPosition(circle_set, radius);
            x = positions[0];  
            y = positions[1];  

            Circle circle = new Circle(x, y, radius);
            circle_set.add(circle);

            node_list[node-1] = new Node(circle, node);
            node_list[node-1].makeNodeClicable(this);
        }      
        
        int i = 0;
        for (Tuple edge : edges){

            int start = edge.l[0];
            int end = edge.l[1];

            Node node_start = node_list[start - 1];
            Node node_end = node_list[end - 1];

            edge_list[i] = new Edge(node_start, node_end);
            i += 1;
        }


        k = Math.sqrt(area / node_list.length);

    }


    void step() {
        // Reset displacements
        for (Node v : node_list) {
            v.dx = 0;
            v.dy = 0;
        }

        // Calculate repulsion
        for (Node v : node_list) {
            for (Node u : node_list) {
                if (v == u) continue;
                double dx = v.getX() - u.getX();
                double dy = v.getY() - u.getY();
                double dist = Math.sqrt(dx * dx + dy * dy) + 0.01;
                double force = (k * k) / dist;
                v.dx += (dx / dist) * force;
                v.dy += (dy / dist) * force;
            }
        }

        // Calculate attraction
        for (Edge e : edge_list) {
            Node v = e.start, u = e.end;
            double dx = v.getX() - u.getX();
            double dy = v.getY() - u.getY();
            double dist = Math.sqrt(dx * dx + dy * dy) + 0.01;
            double force = (dist * dist)*2 / k;
            double fx = (dx / dist) * force;
            double fy = (dy / dist) * force;
            v.dx -= fx; v.dy -= fy;
            u.dx += fx; u.dy += fy;
        }

        // Update positions
        for (Node v : node_list) {
            double dx = v.dx;
            double dy = v.dy;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist > 0) {
                double limitedDist = Math.min(dist, temperature);
                v.moveTo(v.getX() + (dx / dist) * limitedDist, v.getY() + (dy / dist) * limitedDist);
            }

            // Keep within bounds
            v.moveTo(Math.min(width, Math.max(0, v.getX())), Math.min(height, Math.max(0, v.getY())));
        }

        // Cool down
        temperature *= 0.95;
    }


    public void fixPositions(){
        for (int i = 0; i < 150; i++) {
            this.step();
        }

        for (Node n : node_list){
            n.moveTo(n.getX() + 100, n.getY() + 100);
        }
    }


    public void makeLinePosition(){

        int nbr = node_list.length;

        for (int i = 0; i < nbr; i++){
            node_list[i].moveTo(200 + i*(1200/(nbr-1)), 450);
        }
    }


    public void makeCirclePosition(){

        int nbr = node_list.length;

        for (int i = 0; i < nbr; i++){
            int r = 400;
            double x = r * Math.cos(i*Math.toRadians(36));
            double y = r * Math.sin(i*Math.toRadians(36));
            node_list[i].moveTo(x+800, y+450);
        }
    }


    public void makeTreePosition(int nbr_couche){

        double nbr_node, radius, x, y, theta;
        int counter = 0;

        for (int i = 1; i < nbr_couche + 1; i++) {
            
            if (i == 1){
                nbr_node = 1;
            } else {
                nbr_node =  3 * ((int)Math.pow(2, i-2));
            }

            radius = 75*(i-1);

            for (double j = 1; j < nbr_node+1; j++) {
                theta = 360.0/nbr_node * (j - 1.0/2.0);
                x = radius*Math.cos(Math.toRadians(theta));
                y = radius*Math.sin(Math.toRadians(theta));
            
                node_list[counter].moveTo(x + 800, y + 450);
                counter++;
            }
        }
    }


    public void makeRiskPosition(){

        double scale = 100;

        node_list[0].moveTo(-6.6 * scale, 3.45 * -scale);     // Alaska
        node_list[1].moveTo(-5.35 * scale, 2.85 * -scale);     // Alberta
        node_list[2].moveTo(-4.5 * scale, 1.3 * -scale);        // Central America
        node_list[3].moveTo(-4 * scale, 2 * -scale);     // Eastern US
        node_list[4].moveTo(-2 * scale, 4 * -scale);        // Greenland
        node_list[5].moveTo(-5.2 * scale, 3.5 * -scale);    // Northwest territory
        node_list[6].moveTo(-4.5 * scale, 2.8 * -scale);       // Ontario
        node_list[7].moveTo(-3.45 * scale, 2.8 * -scale);          // Quebec
        node_list[8].moveTo(-5 * scale, 2.2 * -scale);    // Western US

        // South America
        node_list[9].moveTo(-3.2 * scale, -1 * -scale);       // Argentina
        node_list[10].moveTo(-2.5 * scale, 0 * -scale);      // Brazil
        node_list[11].moveTo(-3.4 * scale, -0.1 * -scale);   // Peru
        node_list[12].moveTo(-3.3 * scale, 0.7 * -scale);     // Venezuela

        // Europe
        node_list[13].moveTo(-0.55 * scale, 2.8 * -scale);     // Great Britain
        node_list[14].moveTo(-1.2 * scale, 3.45 * -scale);    // Iceland
        node_list[15].moveTo(0.1 * scale, 2.7 * -scale);    // Northern Europe
        node_list[16].moveTo(0.35 * scale, 3.5 * -scale);     // Scandinavia
        node_list[17].moveTo(0.4 * scale, 2.3 * -scale);       // Southern Europe
        node_list[18].moveTo(1.1 * scale, 2.9 * -scale);        // Ukraine
        node_list[19].moveTo(-0.5 * scale, 2.15 * -scale);   // Western Europe

        // Africa
        node_list[20].moveTo(0.45 * scale, 0.45 * -scale);    // Congo
        node_list[21].moveTo(1.05 * scale, 0.9 * -scale);      // East Africa
        node_list[22].moveTo(0.55 * scale, 1.55 * -scale);    // Egypt
        node_list[23].moveTo(1.5 * scale, -0.25 * -scale);      // Madagascar
        node_list[24].moveTo(-0.5 * scale, 1.3 * -scale);       // North Africa
        node_list[25].moveTo(0.5 * scale, -0.4 * -scale);   // South Africa

        // Asia
        node_list[26].moveTo(2.2 * scale, 2.4 * -scale);      // Afghanistan
        node_list[27].moveTo(3.75 * scale, 1.85 * -scale);     // China
        node_list[28].moveTo(2.75 * scale, 1.45 * -scale);   // India
        node_list[29].moveTo(4.15 * scale, 2.85 * -scale);    // Irkutsk
        node_list[30].moveTo(5.25 * scale, 2 * -scale);    // Japan
        node_list[31].moveTo(6 * scale, 3.5 * -scale);     // Kamchatka
        node_list[32].moveTo(1.3 * scale, 1.75 * -scale);       // Middle East
        node_list[33].moveTo(4.5 * scale, 2.4 * -scale);      // Mongolia
        node_list[34].moveTo(3.8 * scale, 1.1 * -scale);   // Siam
        node_list[35].moveTo(3.4 * scale, 3.4 * -scale);       // Siberia
        node_list[36].moveTo(2.3 * scale, 3.2 * -scale);        // Ural
        node_list[37].moveTo(4.65 * scale, 3.55 * -scale);       // Yakutsk

        // Australia
        node_list[38].moveTo(5.5 * scale, -0.65 * -scale);   // Eastern Australia
        node_list[39].moveTo(4.15 * scale, 0.5 * -scale);      // Indonesia
        node_list[40].moveTo(5.35 * scale, 0.35 * -scale);         // New Guinea
        node_list[41].moveTo(4.75 * scale, -0.5 * -scale);         // Western Australia

        for (Node node : node_list){
            node.moveTo(node.getX() + 800, node.getY() + 550);
        }

    }


    public void display(Pane pane){

        pane.setStyle("-fx-background-color: #1e1e1e;");

        System.err.println("Arrêtes du jeu :");
        for (Edge edge : edge_list){
            edge.display(pane);
            System.err.println(edge.start.num + "--" + edge.end.num);
        }
        for (Node node : node_list){
            node.display(pane);
        }

        count = 0;
    }

    public void display(Pane pane, Boolean risk){

        System.err.println("Arrêtes du jeu :");
        for (Edge edge : edge_list){
            System.err.println(edge.start.num + "--" + edge.end.num);
        }

        for (int i = 0; i < node_list.length; i++){

            // North America: 0 à 8
            if (i >= 0 && i < 9) {
                node_list[i].circle.setStroke(Color.web("crimson")); 
            }
            // South America: 9 à 12
            else if (i >= 9 && i < 13) {
                node_list[i].circle.setStroke(Color.web("dodgerblue"));
            }
            // Europe: 13 à 19
            else if (i >= 13 && i < 20) {
                node_list[i].circle.setStroke(Color.BLUE);
            }
            // Africa: 20 à 25
            else if (i >= 20 && i < 26) {
                node_list[i].circle.setStroke(Color.web("mediumseagreen")); 
            }
            // Asia: 26 à 38
            else if (i >= 26 && i < 38) {
                node_list[i].circle.setStroke(Color.GOLDENROD);
            }
            // Australia: 39 à 42
            else if (i >= 38 && i < 43) {
                node_list[i].circle.setStroke(Color.web("purple"));
            }

            node_list[i].circle.setStrokeWidth(2);

            node_list[i].display(pane);
        }
        count = 0;
    }

    ArrayList<Integer> getNeighbors(ArrayList<Node> nodes, int n){

        ArrayList<Integer> neighbors = new ArrayList<>();

        for (Edge edge : edge_list){
            if (n == edge.start.num-1 && nodes.contains(edge.end)){
                neighbors.add(edge.end.num-1);
            } else if (n == edge.end.num-1 && nodes.contains(edge.start)){
                neighbors.add(edge.start.num-1);
            }
        }

        return neighbors;
    }


    void dfs(ArrayList<Node> nodes, int v, boolean[] visited, List<Integer> component) {
        visited[v] = true;
        component.add(v);

        for (int neighbor : getNeighbors(nodes, v)) {
            if (!visited[neighbor]) {
                dfs(nodes, neighbor, visited, component);
            }
        }
    }


    public int getBiggestComponents(String color) {

        ArrayList<Node> nodes;
        if (color == "red"){
            nodes = red_list;
        } else {
            nodes = blue_list;
        }

        boolean[] visited = new boolean[node_list.length];
        List<List<Integer>> components = new ArrayList<>();

        for (Node node : nodes){
            int v = node.num - 1;

            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                dfs(nodes, v, visited, component);
                components.add(component);
            }
        }

        int biggest = 0;

        for (List<Integer> component : components){
            if (biggest < component.size()){
                biggest = component.size();
            }
        }
        return biggest;
    }


    public Boolean isEndGame(){
        return red_list.size() + blue_list.size() == node_list.length;
    }

}
