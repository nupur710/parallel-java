package module1;

import java.io.IOException;

public class MatrixMultiplication {

    public static class Matrix {
        private int[][] one;
        private int[][] two;
        private int row1;
        private int row2;
        private int col1;
        private int col2;

        public Matrix(int[][] one, int[][] two, int row1, int row2, int col1, int col2) {
            this.one = one;
            this.two = two;
            this.row1 = row1;
            this.row2 = row2;
            this.col1 = col1;
            this.col2 = col2;
        }
    }

    public static int[][] result(Matrix m) throws IOException {
        if(m.row1 != m.col2) throw new IllegalArgumentException("Array cannot be multiplied");
        int[][] res= new int[m.row1][m.col2];
        for(int i= 0; i < m.row1; i++) {
            for(int j= 0; j< m.col2; j++) {
                for(int k= 0; k< m.col1; k++) {
                    res[i][j] += m.one[i][k] * m.two[k][j];
                }
            }
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        int row1= 5; int row2= 5; int col1= 5; int col2= 5;
        int[][] matrix1= fill(row1, col1);
        int[][] matrix2= fill(row2,col2);

        Matrix m1= new Matrix(matrix1, matrix2, row1, row2, col1, col2);
        long start= System.nanoTime();
        result(m1);
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
