package items;

public class Ressource {
    private int quantite;
    private String type;

    public Ressource(int quantite, String type) {
        this.quantite = quantite;
        this.type = type;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}