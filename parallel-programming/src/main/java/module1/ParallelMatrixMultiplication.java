package module1;

import java.util.concurrent.RecursiveTask;

public class ParallelMatrixMultiplication extends RecursiveTask<int[][]> {



    public ParallelMatrixMultiplication(int[][] one, int[][] two, int startRow, int endRow, int startCol, int endCol) {

    }

    @Override
    protected int[][] compute() {
        return new int[0][];
    }
}
