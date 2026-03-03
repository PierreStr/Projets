import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge {
    
    public Node start;
    public Node end;

    public Edge(Node start, Node end){
        this.start = start;
        this.end = end;
    }


    public void display(Pane pane){
        
        Line line = new Line(start.circle.getCenterX(), start.circle.getCenterY(), end.circle.getCenterX(), end.circle.getCenterY());
        line.setStroke(Color.WHITE);

        pane.getChildren().add(line);
    }

    public static Tuple[] makeRiskEdges(){

        Tuple[] edges = new Tuple[82];

        edges[0] = new Tuple(1, 6);    // Alaska - Northern Territory
        edges[1] = new Tuple(5, 6);    // Northern Territory - Greenland
        edges[2] = new Tuple(1, 2);    // Alaska - Alberta
        edges[3] = new Tuple(6, 7);    // Northern Territory - Ontario
        edges[4] = new Tuple(5, 8);    // Greenland - Quebec
        edges[5] = new Tuple(2, 6);    // Northern Territory - Alberta
        edges[6] = new Tuple(5, 7);    // Greenland - Ontario
        edges[7] = new Tuple(2, 7);    // Alberta - Ontario
        edges[8] = new Tuple(7, 8);    // Ontario - Quebec
        edges[9] = new Tuple(2, 9);    // Alberta - Western US
        edges[10] = new Tuple(4, 7);   // Ontario - Eastern US
        edges[11] = new Tuple(7, 9);   // Ontario - Western US
        edges[12] = new Tuple(4, 8);   // Quebec - Eastern US
        edges[13] = new Tuple(4, 9);   // Eastern US - Western US
        edges[14] = new Tuple(3, 9);   // Central America - Western US
        edges[15] = new Tuple(3, 4);   // Central America - Eastern US

        // South America
        edges[16] = new Tuple(11, 13); // Venezuela - Brazil
        edges[17] = new Tuple(12, 13); // Venezuela - Peru
        edges[18] = new Tuple(10, 11); // Brazil - Argentina
        edges[19] = new Tuple(11, 12); // Brazil - Peru
        edges[20] = new Tuple(10, 12); // Argentina - Peru

        // Europe
        edges[21] = new Tuple(15, 17); // Iceland - Scandinavia
        edges[22] = new Tuple(14, 15); // Iceland - Great Britain
        edges[23] = new Tuple(14, 17); // Scandinavia - Great Britain
        edges[24] = new Tuple(16, 17); // Scandinavia - Northern Europe
        edges[25] = new Tuple(17, 19); // Scandinavia - Ukraine
        edges[26] = new Tuple(14, 16); // Great Britain - Northern Europe
        edges[27] = new Tuple(14, 20); // Great Britain - Western Europe
        edges[28] = new Tuple(16, 20); // Northern Europe - Western Europe
        edges[29] = new Tuple(16, 18); // Northern Europe - Southern Europe
        edges[30] = new Tuple(18, 20); // Western Europe - Southern Europe
        edges[31] = new Tuple(16, 19); // Northern Europe - Ukraine
        edges[32] = new Tuple(18, 19); // Southern Europe - Ukraine

        // Africa
        edges[33] = new Tuple(23, 25); // North Africa - Egypt
        edges[34] = new Tuple(21, 25); // Congo - North Africa
        edges[35] = new Tuple(22, 25); // North Africa - East Africa
        edges[36] = new Tuple(22, 23); // Egypt - East Africa
        edges[37] = new Tuple(21, 22); // Congo - East Africa
        edges[38] = new Tuple(21, 26); // Congo - South Africa
        edges[39] = new Tuple(22, 26); // South Africa - East Africa
        edges[40] = new Tuple(22, 24); // East Africa - Madagascar
        edges[41] = new Tuple(24, 26); // South Africa - Madagascar

        // Asia
        edges[42] = new Tuple(36, 37); // Ural - Siberia
        edges[43] = new Tuple(36, 38); // Siberia - Yakutsk
        edges[44] = new Tuple(30, 36); // Siberia - Irkutsk
        edges[45] = new Tuple(30, 32); // Irkutsk - Kamchatka
        edges[46] = new Tuple(32, 38); // Yakutsk - Kamchatka
        edges[47] = new Tuple(30, 38); // Yakutsk - Irkutsk
        edges[48] = new Tuple(27, 37); // Afghanistan - Ural
        edges[49] = new Tuple(28, 37); // China - Ural
        edges[50] = new Tuple(28, 36); // China - Siberia
        edges[51] = new Tuple(34, 36); // Siberia - Mongolia
        edges[52] = new Tuple(30, 34); // Irkutsk - Mongolia
        edges[53] = new Tuple(32, 34); // Kamchatka - Mongolia
        edges[54] = new Tuple(31, 32); // Kamchatka - Japan
        edges[55] = new Tuple(27, 28); // Afghanistan - China
        edges[56] = new Tuple(27, 29); // Afghanistan - India
        edges[57] = new Tuple(27, 33); // Afghanistan - Middle East
        edges[58] = new Tuple(29, 33); // Middle East - India
        edges[59] = new Tuple(28, 29); // India - China
        edges[60] = new Tuple(29, 35); // India - Siam
        edges[61] = new Tuple(28, 35); // China - Siam
        edges[62] = new Tuple(28, 34); // China - Mongolia
        edges[63] = new Tuple(31, 34); // Mongolia - Japan

        // Australia
        edges[64] = new Tuple(40, 41); // Indonesia - New Guinea
        edges[65] = new Tuple(40, 42); // Indonesia - Western Australia
        edges[66] = new Tuple(41, 42); // Western Australia - New Guinea
        edges[67] = new Tuple(39, 42); // Western Australia - Eastern Australia
        edges[68] = new Tuple(39, 41); // Eastern Australia - New Guinea

        // Intercontinental
        edges[69] = new Tuple(5, 15);  // Greenland - Iceland
        edges[70] = new Tuple(3, 13);  // Central America - Venezuela
        edges[71] = new Tuple(11, 25); // Brazil - North Africa
        edges[72] = new Tuple(20, 25); // Western Europe - North Africa
        edges[73] = new Tuple(18, 25); // Southern Europe - North Africa
        edges[74] = new Tuple(18, 23); // Southern Europe - Egypt
        edges[75] = new Tuple(19, 37); // Ukraine - Ural
        edges[76] = new Tuple(19, 27); // Ukraine - Afghanistan
        edges[77] = new Tuple(19, 33); // Ukraine - Middle East
        edges[78] = new Tuple(18, 33); // Southern Europe - Middle East
        edges[79] = new Tuple(23, 33); // Egypt - Middle East
        edges[80] = new Tuple(35, 40); // Siam - Indonesia
        edges[81] = new Tuple(1, 32);  // Alaska - Kamchatka

        return edges;
    }
}
