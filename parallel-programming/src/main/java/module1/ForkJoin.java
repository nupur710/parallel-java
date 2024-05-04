package module1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

//Use fork/join framework to calculate sum of integers in array
public class ForkJoin extends RecursiveTask<Integer> {

    private int arr[];
    private int start;
    private int end;

    public ForkJoin(int[] arr, int start, int end) {
        this.arr= arr;
        this.start= start;
        this.end= end;
    }

    @Override
    protected Integer compute() {
        //if there is only element in array; return that element otherwise return 0
        if(end - start <= 1) {
            return (start < end) ? arr[start] : 0;
        } else {
            int mid= (start + end) / 2;
            ForkJoin leftTask= new ForkJoin(arr, start, mid);
            ForkJoin rightTask= new ForkJoin(arr, mid+1, end);
            leftTask.fork();
            int rightResult= rightTask.compute();
            int leftResult= leftTask.join();
            return leftResult+rightResult;
        }
    }

    public static void main(String[] args) {
        int[] arr= {1,2,3,4,5,6,7,8,9,10};
        ForkJoin task= new ForkJoin(arr, 0, arr.length);
        ForkJoinPool pool= new ForkJoinPool();
        int sum= pool.invoke(task);
        System.out.println("Sum is " + sum);
    }
}
