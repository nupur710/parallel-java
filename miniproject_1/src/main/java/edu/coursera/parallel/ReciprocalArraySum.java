package edu.coursera.parallel;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public final class ReciprocalArraySum {

    private ReciprocalArraySum() {
    }

    protected static double seqArraySum(final double[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += 1 / input[i];
        }
        return sum;
    }

    private static int getChunkSize(final int nChunks, final int nElements) {
        // Integer ceil
        return (nElements + nChunks - 1) / nChunks;
    }

    private static int getChunkStartInclusive(final int chunk, final int nChunks, final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        return chunk * chunkSize;
    }

    private static int getChunkEndExclusive(final int chunk, final int nChunks, final int nElements) {
        final int chunkSize = getChunkSize(nChunks, nElements);
        final int end = (chunk + 1) * chunkSize;
        if (end > nElements) {
            return nElements;
        } else {
            return end;
        }
    }

    private static class ReciprocalArraySumTask extends RecursiveAction {

        private final int startIndexInclusive;
        private final int endIndexExclusive;
        private final double[] input;
        private double value;

        ReciprocalArraySumTask(final int setStartIndexInclusive,
                               final int setEndIndexExclusive, final double[] setInput) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = setInput;
        }

        public double getValue() {
            return value;
        }
        //invoke calls compute
        @Override
        protected void compute() {
            if(endIndexExclusive - startIndexInclusive <= 1){
                for(int i=startIndexInclusive;i<endIndexExclusive;i++){
                    value += 1/input[i];
                }
            } else {
                int mid = (endIndexExclusive+startIndexInclusive)/2 + 1;
                ReciprocalArraySumTask left = new ReciprocalArraySumTask(startIndexInclusive, mid, input);
                ReciprocalArraySumTask right = new ReciprocalArraySumTask(mid, endIndexExclusive, input);
                left.fork();
                right.compute();
                left.join();
                value= left.getValue()+ right.getValue();
            }
        }
    }


    protected static double parArraySum(final double[] input) {
        assert input.length % 2 == 0;
        double sum= 0;
        ReciprocalArraySumTask task= new ReciprocalArraySumTask(0, input.length, input);
        ForkJoinPool.commonPool().invoke(task);
        sum= task.getValue();
        return sum;
    }

    //instead of just 2 tasks- left and right; here we will run many tasks- each
    //calculating reciprocal sum over various chunks of array
    protected static double parManyTaskArraySum(final double[] input, final int numTasks) {
        double sum = 0;
        ArrayList<ReciprocalArraySumTask> list = new ArrayList<>();
        for(int i=0; i<numTasks; i++){
            int start = getChunkStartInclusive(i, numTasks, input.length);
            int end = getChunkEndExclusive(i, numTasks, input.length);
            list.add(new ReciprocalArraySumTask(start, end, input));
        }
        for(int i= 0; i <= list.size()/2; i++) {
            list.get(i).fork();
        }
        ForkJoinTask.invokeAll(list);
        for(ReciprocalArraySumTask i : list){
            sum += i.getValue();
        }
        return sum;
    }
}