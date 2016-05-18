package minesweeperPackage;

public class Sort {
  Sort() {}

  public void quickSort(int[] mas, int low, int high) {
    int i = low;
    int j = high;
    int pivot = mas[(low + high) / 2];
    while (i <= j) {
      while (mas[i] < pivot) {
        i++;
      }
      while (mas[j] > pivot) {
        j--;
      }

      if (i <= j) {
        int temp = mas[i];
        mas[i] = mas[j];
        mas[j] = temp;
        i++;
        j--;
      }
    }
    if (low < j) {
      quickSort(mas, low, j);
    }
    if (high > i) {
      quickSort(mas, i, high);
    }
  }
}
