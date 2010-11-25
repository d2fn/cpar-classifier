import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private Map<Integer,Transaction> data;

    public Database() {
        data = new HashMap<Integer,Transaction>();
    }

    public void add(Transaction t) {
        data.put(t.id,t);
    }

    public void addAll(Collection<Transaction> ts) {
        for(Transaction t : ts) {
            add(t);
        }
    }

    public void removeAll(Collection<Transaction> ts) {
        for(Transaction t : ts) {
            remove(t);
        }
    }

    public void remove(Transaction t) {
        data.remove(t.id);
    }

    public Database copy() {
        Database c = new Database();
        c.data = new HashMap<Integer,Transaction>();
        c.data.putAll(data);
        return c;
    }

    public Collection<Transaction> data() {
        return data.values();
    }

    public int size() {
        return data.size();
    }
}
