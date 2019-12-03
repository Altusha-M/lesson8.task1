package refactor;

public class Main {
    public static void main(String[] args) {
        Field f1 = new Field(16, 500);
        f1.createField();
//        f1.oneThreadCompute();
////        f1.draw();
//        int i = 0;
//        f1.createField();
        f1.multithreadingCompute(System.currentTimeMillis());
    }
}
