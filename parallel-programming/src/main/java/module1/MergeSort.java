package module1;

public class MergeSort {

    public static void main(String[] args) {
        int[] arr= {12, 11, 13, 5, 6, 7};
        sort(arr, 0, arr.length-1);
    }

    public static void sort(int[] arr, int left, int right) {
        if(left < right) {
            int mid= (left + right)/2;
            sort(arr, left, mid);
            sort(arr, mid+1, right);
        }
    }

    public static void merge(int[] arr, int left, int right, int mid) {
        int n1= mid-left+1;
        int n2= right - mid;
        int[] leftArr= new int[n1];
        int[] rightArr= new int[n2];
        for(int i= 0; i< n1; i++) {
            leftArr[i]= arr[left+i];
        }
        for(int j= 0; j< n2; j++) {
            rightArr[j]= arr[mid+1+j];
        }
        int i= 0, j= 0;
        int k= left;
        while(i < n1 && j < n2) {
            if(leftArr[i] <= rightArr[j]) {
                arr[k]= leftArr[i];
                i++;
            } else {
                arr[k]= rightArr[j];
                j++;
            }
            k++;
        }
        while(i<n1) {
            arr[k]= leftArr[i];
            i++;
            k++;
        }
        while(j<n2) {
            arr[k]= rightArr[j];
            j++;
            k++;
        }
    }
}
