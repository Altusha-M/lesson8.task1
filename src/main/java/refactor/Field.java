package refactor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.String.format;

public class Field {
    public final int ITERATIONS;
    public final int FIELD_SIZE;
    public int currentIteration = 0;
    public boolean[][] currentField;
    public boolean[][] newField;

    public Field(int field_size, int iterations) {
        FIELD_SIZE = field_size;
        ITERATIONS = iterations;
        currentField = new boolean[FIELD_SIZE][FIELD_SIZE];
        newField = new boolean[FIELD_SIZE][FIELD_SIZE];
    }

    private int countNeighbors(int x, int y) {
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

    private void processOfLife(int stX, int stY, int endX, int endY) {
        for (int x = stX; x < endX; x++) {
            for (int y = stY; y < endY; y++) {
                int count = countNeighbors(x, y);
                newField[x][y] = currentField[x][y];
                newField[x][y] = (count == 3) ? true : newField[x][y];
                newField[x][y] = ((count < 2) || (count > 3)) ? false : newField[x][y];
            }
        }
    }

    public void reset(boolean[][] tmp) {
        for (int x = 0; x < FIELD_SIZE; x++) {
            System.arraycopy(tmp[x], 0, currentField[x], 0, FIELD_SIZE);
        }
        newField = new boolean[FIELD_SIZE][FIELD_SIZE];
        currentIteration = 0;
    }

    /**
     * Executes one thread computing of my Game of Life realization
     */
    public void oneThreadCompute() {
        while (currentIteration < ITERATIONS) {
            processOfLife(0, 0, FIELD_SIZE, FIELD_SIZE);
            currentIteration++;
            System.out.println("new Iteration");
            draw();
        }
    }


    /**
     * Executes multithreading computing of my Game of Life realization
     * print all of the iterations and time of computations
     * @param s initial time in milliseconds
     */
    public void multithreadingCompute(long s) {
        CyclicBarrier cd = new CyclicBarrier(4, () -> {
            currentIteration++;
            for (int x = 0; x < FIELD_SIZE; x++) {
                System.arraycopy(newField[x], 0, currentField[x], 0, FIELD_SIZE);
            }
            draw();
            if (currentIteration == ITERATIONS - 1) {
                long f = System.currentTimeMillis() - s;
                System.out.println("many thread time " + f);
            }
        });

        new Thread(() ->{
            while (currentIteration < ITERATIONS) {
                processOfLife(0, 0, FIELD_SIZE/2, FIELD_SIZE/2);
                try {
                    cd.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() ->{
            while (currentIteration < ITERATIONS) {
                processOfLife(0, FIELD_SIZE/2, FIELD_SIZE/2, FIELD_SIZE);
                try {
                    cd.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() ->{
            while (currentIteration < ITERATIONS) {
                processOfLife(FIELD_SIZE/2, 0, FIELD_SIZE, FIELD_SIZE/2);
                try {
                    cd.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() ->{
            while (currentIteration < ITERATIONS) {
                processOfLife(FIELD_SIZE/2, FIELD_SIZE/2, FIELD_SIZE, FIELD_SIZE);
                try {
                    cd.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void createField() {
        for (int i = 0; i < currentField.length; i++) {
            for (int j = 0; j < currentField.length; j++) {
                currentField[i][j] = (new Random().nextInt(100) > 55);
            }
        }
        draw();
        System.out.println("Initial field");
    }

    public void draw() {
        for (int i = 0; i < currentField.length; i++) {
            for (int j = 0; j < currentField.length; j++) {
                System.out.print((currentField[i][j] ? 'o' : "_") + "  ");
            }
            System.out.println();
        }
        System.out.println("___________________________________________________");
    }

    public void createTestField() {
        currentField = new boolean[][]{
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}};
        draw();
        System.out.println("Initial TEST field");
    }


}
