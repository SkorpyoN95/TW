package asynchronized;

public class Buffer {
    private boolean[] elems;

    public Buffer(int n) {
        elems = new boolean[n];
    }

    public void setElem(int i){
        elems[i] = true;
    }

    public void getElem(int i){
        elems[i] = false;
    }

    public int getSize(){
        return elems.length;
    }
}
