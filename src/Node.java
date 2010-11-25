import java.util.ArrayList;
import java.util.List;

public class Node<T> {

    private T obj;

    private List<Node<T>> children;

    public Node(T obj) {
        this.obj = obj;
        children = new ArrayList<Node<T>>();
    }

    
}
