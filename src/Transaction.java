import java.util.*;

public class Transaction extends HashSet<Integer> {

    public int id;
    private String label;
    public double weight = 1.0;

    public static Transaction buildFromUnlabeledTransaction(String ... fields) {
        int[] items = new int[fields.length];
        for(int i = 0; i < fields.length; i++) {
            items[i] = Integer.parseInt(fields[i]);
        }
        return new Transaction(null,items);
    }

    public static Transaction buildFromLabeledTransaction(String ... fields) {
        String label = fields[fields.length-1];
        int[] items = new int[fields.length-1];
        for(int i = 0; i < fields.length-1; i++) {
            items[i] = Integer.parseInt(fields[i]);
        }
        return new Transaction(label,items);
    }

    public Transaction(String label, int ... items) {
        super(items.length);
        this.label = label;
        for(int item : items) {
            super.add(item);
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isLabeled() {
        return label != null;
    }

    public List<Integer> orderedItems() {
        List<Integer> items = new ArrayList<Integer>(this.size());
        items.addAll(this);
        Collections.sort(items);
        return items;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        if(isLabeled()) {
            sb.append("label: ").append(label).append("\n");
        }
        else {
            sb.append("(unlabeled)\n");
        }

        sb.append(transactionString());
        return sb.toString();
    }

    private String transactionString() {
        StringBuilder sb = new StringBuilder();
        List<Integer> items = new ArrayList<Integer>(this.size());
        items.addAll(this);
        sb.append("(").append(items.get(0));
        for(int i = 1; i < items.size(); i++) {
            sb.append(",").append(items.get(i));
        }
        sb.append(")");
        return sb.toString();
    }
}
