import java.util.Scanner;

/**
 * Created by douwz on 2/3/2017.
 */
public class Primefactors {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            double num = scan.nextDouble();
            double i = 2;
            while (num != 1) {
                if (num % i == 0) {
                    num /= i;
                    System.out.print(i + " ");
                    i--;
                }
                i++;
            }
            System.out.println();
        }
    }
}
