import java.util.LinkedList;
import java.util.Random;

public class Proof {
    public static void main(String[] args) {
        LinkedList<Integer> l = new LinkedList<Integer>();
        l.push(2);
        l.push(3);
        l.push(2);
        l.push(4);
        System.out.println(l.toString());
        System.out.println(l.peekFirst());
        System.out.println(l.peekLast());
        l.push(55);
        System.out.println(l.toString());
        Random random = new Random();
        System.out.println(random.nextInt(5));
    }
}
