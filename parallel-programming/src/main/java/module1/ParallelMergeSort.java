package module1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelMergeSort {

    static class MergeSortTask extends RecursiveTask<int[]> {

        private final int[] arr;
        private final int start;
        private final int end;

        public MergeSortTask(int[] arr, int start, int end) {
            this.arr= arr;
            this.start= start;
            this.end= end;
        }

        @Override
        protected int[] compute() {
            if(end - start <= 1) {
                if(end - start == 1 && arr[start] > arr[end]) {
                    int temp= arr[start];
                    arr[start]= arr[end];
                    arr[end]= temp;
                }
                return arr;
            }
            int mid= (start + end)/2;
            MergeSortTask leftTask= new MergeSortTask(arr, start, mid);
            MergeSortTask rightTask= new MergeSortTask(arr, mid+1, end);
            leftTask.fork();
            int[] rightResult= rightTask.compute();
            int[] leftResult= leftTask.join();
            return merge(arr, start, mid, end);
        }

        private int[] merge(int[] array, int start, int mid, int end) {
            int[] temp = new int[end - start + 1];
            int i = start, j = mid + 1, k = 0;

            while (i <= mid && j <= end) {
                if (array[i] <= array[j]) {
                    temp[k++] = array[i++];
                } else {
                    temp[k++] = array[j++];
                }
            }

            while (i <= mid) {
                temp[k++] = array[i++];
            }

            while (j <= end) {
                temp[k++] = array[j++];
            }

            System.arraycopy(temp, 0, array, start, temp.length);
            return array;
        }
    }

    public static void main(String[] args) {
        int[] arr= {12, 11, 13, 5, 6, 7};
        ForkJoinPool forkJoinPool= new ForkJoinPool();
        MergeSortTask task= new MergeSortTask(arr, 0, arr.length-1);
        forkJoinPool.invoke(task);
    }

}
