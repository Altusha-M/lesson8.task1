package homework;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Hello world!
 */
public class GameOfLife {
    public static final int NUMBER_OF_ITERATIONS = 5;
    public static final int FIELD_SIZE = 16;
    public static int currentIteration = 0;
    public static volatile boolean[][] currentField = new boolean[FIELD_SIZE][FIELD_SIZE];
    public static volatile boolean[][] newField = new boolean[FIELD_SIZE][FIELD_SIZE];


    static class Execute {
        private final int a = 1;

        int countNeighbors(int x, int y) {
            int count = 0;
            for (int dx = -1; dx < 2; dx++) {
                for (int dy = -1; dy < 2; dy++) {
                    int nX = x + dx;
                    int nY = y + dy;
                    nX = (nX < 0) ? FIELD_SIZE - 1 : nX;
                    nY = (nY < 0) ? FIELD_SIZE - 1 : nY;
                    nX = (nX > FIELD_SIZE - 1) ? 0 : nX;
                    nY = (nY > FIELD_SIZE - 1) ? 0 : nY;
                    count += (currentField[nX][nY]) ? 1 : 0;
                }
            }
            if (currentField[x][y]) {
                count--;
            }
            ArrayList<Integer> al = new ArrayList<Integer>();

            return count;
        }

        void processOfLife(int stX, int stY, int endX, int endY) {
            for (int x = stX; x < endX; x++) {
                for (int y = stY; y < endY; y++) {
                    int count = countNeighbors(x, y);
                    newField[x][y] = currentField[x][y];
                    newField[x][y] = (count == 3) ? true : newField[x][y];
                    newField[x][y] = ((count < 2) || (count > 3)) ? false : newField[x][y];
                }
            }
        }

    }

    public static void reset(boolean[][] tmp) {
        for (int x = 0; x < FIELD_SIZE; x++) {
            System.arraycopy(tmp[x], 0, currentField[x], 0, FIELD_SIZE);
        }
        newField = new boolean[FIELD_SIZE][FIELD_SIZE];
        currentIteration = 0;
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        createField();
        String s1 = "wqer";
        Integer int2 = new Integer (15);
        boolean[][] tmp = new boolean[FIELD_SIZE][FIELD_SIZE];
        for (int x = 0; x < FIELD_SIZE; x++) {
            System.arraycopy(currentField[x], 0, tmp[x], 0, FIELD_SIZE);
        }
        long s = System.currentTimeMillis();
        oneThreadCompute();
        long f = System.currentTimeMillis() - s;
//        draw();
        System.out.println("one thread time " + f);
        draw();
//
//
//        reset(tmp);
//        s = System.currentTimeMillis();
//        multithreadingCompute(s);

    }


    static void oneThreadCompute() {
        while (currentIteration < NUMBER_OF_ITERATIONS) {
            processOfLife();
//            try {
//                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
//                draw();
//                Thread.sleep(120);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            currentIteration++;
        }
    }

    static void multithreadingCompute(long s) {
        Execute th1 = new Execute();
        Execute th2 = new Execute();
        Execute th3 = new Execute();
        Execute th4 = new Execute();
        CyclicBarrier cd = new CyclicBarrier(4, () -> {
            currentIteration++;
            for (int x = 0; x < FIELD_SIZE; x++) {
                System.arraycopy(newField[x], 0, currentField[x], 0, FIELD_SIZE);
            }
            if (currentIteration == NUMBER_OF_ITERATIONS - 1) {
                long f = System.currentTimeMillis() - s;
                System.out.println("many thread time " + f);
//                try {
                    System.out.println(f);
//                    draw();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            }
        });

        Thread t1 = new Thread(() -> {
            while (currentIteration < NUMBER_OF_ITERATIONS) {
                th1.processOfLife(0, 0, FIELD_SIZE / 2, FIELD_SIZE / 2);
                try {
                    cd.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }, "thread 1");


        Thread t2 = new Thread(() -> {
            while (currentIteration < NUMBER_OF_ITERATIONS) {
                th2.processOfLife(FIELD_SIZE / 2, 0, FIELD_SIZE, FIELD_SIZE / 2);
                try {
                    cd.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }, "thread 2");


        Thread t3 = new Thread(() -> {
            while (currentIteration < NUMBER_OF_ITERATIONS) {
                th3.processOfLife(0, FIELD_SIZE / 2, FIELD_SIZE / 2, FIELD_SIZE);
                try {
                    cd.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }, "thread 3");


        Thread t4 = new Thread(() -> {
            while (currentIteration < NUMBER_OF_ITERATIONS) {
                th4.processOfLife(FIELD_SIZE / 2, FIELD_SIZE / 2, FIELD_SIZE, FIELD_SIZE);
                try {
                    cd.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }, "thread 4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    public static void createField() {
//        for (int i = 0; i < currentField.length; i++) {
//            for (int j = 0; j < currentField.length; j++) {
//                currentField[i][j] = (new Random().nextInt(100) > 55);
//            }
//        }
        currentField     = new boolean[][]{
                { false, false, false, false, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, true, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, true, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, true, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, true, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, true, false, false, false, false,  false, false, false, false, false, false, false, false },
                { false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false }};
        try {
            System.out.println("Initial field");
//            draw();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void draw() throws InterruptedException {
        for (int i = 0; i < currentField.length; i++) {
            for (int j = 0; j < currentField.length; j++) {
                System.out.print((currentField[i][j] ? 'o' : "_") + "  ");
            }
            System.out.println();
        }
    }


    static int countNeighbors(int x, int y) {
        int count = 0;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                int nX = x + dx;
                int nY = y + dy;
                nX = (nX < 0) ? FIELD_SIZE - 1 : nX;
                nY = (nY < 0) ? FIELD_SIZE - 1 : nY;
                nX = (nX > FIELD_SIZE - 1) ? 0 : nX;
                nY = (nY > FIELD_SIZE - 1) ? 0 : nY;
                count += (currentField[nX][nY]) ? 1 : 0;
            }
        }
        if (currentField[x][y]) {
            count--;
        }
        return count;
    }



    static void processOfLife() {
        for (int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                int count = countNeighbors(x, y);
                newField[x][y] = currentField[x][y];
                newField[x][y] = (count == 3) ? true : newField[x][y];
                newField[x][y] = ((count < 2) || (count > 3)) ? false : newField[x][y];
            }
        }
        for (int x = 0; x < FIELD_SIZE; x++) {
            System.arraycopy(newField[x], 0, currentField[x], 0, FIELD_SIZE);
        }
    }

}
