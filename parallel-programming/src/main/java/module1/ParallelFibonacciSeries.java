package module1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelFibonacciSeries extends RecursiveTask<Integer> {
    static int THRESHOLD= 5;
    private int n;
    public ParallelFibonacciSeries(int n) {
        this.n= n;
    }

    @Override
    protected Integer compute() {
        if(n <= THRESHOLD) {
            return fibonacci(n);
        }
        ParallelFibonacciSeries left= new ParallelFibonacciSeries(n-1);
        ParallelFibonacciSeries right= new ParallelFibonacciSeries(n-2);
        left.fork();
        return right.compute() + left.join();
    }

    private int fibonacci(int n) {
        if(n <= 1) return n;
        return fibonacci(n-1) + fibonacci(n-2);
    }

    public static void main(String[] args) {
        ParallelFibonacciSeries parallelfib= new ParallelFibonacciSeries(60);
        ForkJoinPool pool= new ForkJoinPool();
        int result= pool.invoke(parallelfib);
        System.out.println(result);
    }
}
