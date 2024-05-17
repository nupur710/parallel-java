package module1;

import java.io.IOException;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelMatrixMultiplication extends RecursiveTask<int[][]>  {

    private int[][] one;
    private int[][] two;
    private int row1;
    private int row2;
    private int col1;
    private int col2;

    public ParallelMatrixMultiplication(int[][] one, int[][] two, int row1, int row2, int col1, int col2) {
        this.one = one;
        this.two = two;
        this.row1 = row1;
        this.row2 = row2;
        this.col1 = col1;
        this.col2 = col2;
    }

        @Override
        protected int[][] compute() {
            int row1 = 5;
            int row2 = 5;
            int col1 = 5;
            int col2 = 5;
            int[][] matrix1 = fill(row1, col1);
            int[][] matrix2 = fill(row2, col2);

            ParallelMatrixMultiplication m1 = new ParallelMatrixMultiplication(matrix1, matrix2, row1, row2, col1, col2);
            int[][] res;
            if (row1 < 5 && row2 < 5 && col1 < 5 && col2 < 5) {
                try {
                    res = result(matrix1, matrix2, row1, col1, col2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                int midRow = (row1 + row2) / 2;
                int midCol = (col1 + col2) / 2;

                ParallelMatrixMultiplication upperLeft = new ParallelMatrixMultiplication(matrix1, matrix2, row1, midRow, col1, midCol);
                ParallelMatrixMultiplication upperRight = new ParallelMatrixMultiplication(matrix1, matrix2, row1, midRow, midCol, col2);
                ParallelMatrixMultiplication lowerLeft = new ParallelMatrixMultiplication(matrix1, matrix2, midRow, row2, col1, midCol);
                ParallelMatrixMultiplication lowerRight = new ParallelMatrixMultiplication(matrix1, matrix2, midRow, row2, midCol, col2);
                upperLeft.fork();
                upperRight.fork();
                lowerLeft.fork();
                int[][] d= lowerRight.compute();

                int[][] a= upperLeft.join();
                int[][] b= upperRight.join();
                int[][] c= lowerLeft.join();

                res= combineResults(a, b, c, d);
            }
            return res;

    }

    private int[][] combineResults(int[][] upperLeft, int[][] upperRight, int[][] lowerLeft, int[][] lowerRight) {
        int[][] result = new int[upperLeft.length + lowerLeft.length][upperLeft[0].length + upperRight[0].length];
        for (int i = 0; i < upperLeft.length; i++) {
            System.arraycopy(upperLeft[i], 0, result[i], 0, upperLeft[0].length);
            System.arraycopy(upperRight[i], 0, result[i], upperLeft[0].length, upperRight[0].length);
        }
        for (int i = 0; i < lowerLeft.length; i++) {
            System.arraycopy(lowerLeft[i], 0, result[i + upperLeft.length], 0, lowerLeft[0].length);
            System.arraycopy(lowerRight[i], 0, result[i + upperLeft.length], lowerLeft[0].length, lowerRight[0].length);
        }
        return result;
    }




    public static int[][] result(int[][] one, int[][] two, int row1, int col1, int col2) throws IOException {
        if(row1 != col2) throw new IllegalArgumentException("Array cannot be multiplied");
        int[][] res= new int[row1][col2];
        for(int i= 0; i < row1; i++) {
            for(int j= 0; j< col2; j++) {
                for(int k= 0; k< col1; k++) {
                    res[i][j] += one[i][k] * two[k][j];
                }
            }
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        int row1= 50; int row2= 50; int col1= 50; int col2= 50;
        int[][] matrix1= fill(row1, col1);
        int[][] matrix2= fill(row2,col2);
        ParallelMatrixMultiplication m= new ParallelMatrixMultiplication(matrix1, matrix2, row1, row2, col1, col2);
        ForkJoinPool pool= new ForkJoinPool();

        long start= System.nanoTime();
        int[][] res= pool.invoke(m);
        long end= System.nanoTime();
        System.out.println("Time: " + (end - start));

    }

    public static int[][] fill(int row, int col) {
        int[][] arr= new int[row][col];
        for(int i= 0; i < row; i++) {
            for(int j= 0; j < col; j++) {
                int x = (int) (Math.random() * 100);
                arr[i][j]= x;
            }
        } return arr;
    }
}
