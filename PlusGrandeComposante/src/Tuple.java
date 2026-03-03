public class Tuple {
    
    public int elem1;
    public int elem2;
    public int[] l;

    public Tuple(int elem1, int elem2){

        this.elem1 = elem1;
        this.elem2 = elem2;

        l = new int[]{elem1, elem2};
    }
}
