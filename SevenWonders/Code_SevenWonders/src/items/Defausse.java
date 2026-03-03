package items;

import java.util.ArrayList;

import cartes.Carte;
import javafx.scene.control.Button;

public class Defausse {
    
    public static Defausse defausse = new Defausse();
    public ArrayList<Carte> cartes = new ArrayList<>();
    public Button button = new Button();

    public Defausse(){
        cartes = new ArrayList<Carte>();
    }

    public static Defausse getDefausse(){
        return defausse;
    }

    public static void setDefausse(Defausse mydefausse){
        defausse = mydefausse;
    }

    public void add(Carte carte){
        getDefausse().cartes.add(carte);
    }

    public void remove(Carte carte){
        getDefausse().cartes.remove(carte);
    }
}
