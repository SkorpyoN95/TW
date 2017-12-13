package asynchronized;

public class Buffer {
    private int[] elems;

    public Buffer(int n) {
        elems = new int[n];
    }

    public void setElem(int i, int a){
        elems[i] = a;
    }

    public int getElem(int i){
        return elems[i];
    }

    public int getSize(){
        return elems.length;
    }
}
