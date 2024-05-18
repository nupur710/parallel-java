package module1;

public class FibonacciSeries {
    //Iterative
    private static void iterativeFibonacci(int n, int x) {
        int a= 0;
        int b= 1;
        int counter= 2;
        for(int i= 0; i< n; i++) {
            int c= a+b;
            if(counter == x) System.out.print(c);
            a= b;
            b= c;
            counter++;
        }
    }

    //Time complexity is O(2^n), does not compute
    private static int recursiveFibonacci(int n) {
        if(n <= 1) return n;
        return recursiveFibonacci(n-1) + recursiveFibonacci(n-2);
    }

    public static void main(String[] args) {
        iterativeFibonacci(36, 5);
        System.out.println("\n"+recursiveFibonacci(5));
    }
}
