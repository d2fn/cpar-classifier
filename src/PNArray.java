import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PNArray {

    private Rule rule;
    private Map<Integer,PNMatch> examples;
    private Set<Integer> uniqueItems;
    private Database db;

    public PNArray() {}

    public PNArray(Rule rule, Set<Integer> uniqueItems, Database db) {

        this();

        this.rule = rule;
        this.uniqueItems = uniqueItems;
        this.db = db;

        calculate();
    }

    private void calculate() {
        examples = new HashMap<Integer,PNMatch>(uniqueItems.size());
        for(Integer item : uniqueItems) {
//            if(!rule.hasPredicate(item)) {
                PNMatch PN = rule.addPredicate(item).runRule(db.data());
                PN.item = item;
                examples.put(item,PN);
//            }
        }
    }

    public Classifier.Gain calculateBestGain(double P, double N) {

        double oldGain = Math.log(P/(P+N));

        Classifier.Gain best = null;

        for(PNMatch m : examples.values()) {
            double newGain = Math.log(m.P/(m.P+m.N));
            Classifier.Gain literalGain = new Classifier.Gain();
            literalGain.gain = m.P * (newGain - oldGain);
            literalGain.item = m.item;
            if(best == null) {
                best = literalGain;
            }
            else if(literalGain.gain > best.gain) {
                best = literalGain;
            }
        }

        if(best.gain.isInfinite() || best.gain.isNaN()) {
            best.gain = 0.0;
        }

        return best;
    }

    public void remove(Transaction t) {
        db.remove(t);
        for(Integer i : t) {
            PNMatch m = examples.get(i);
            if(t.getLabel().equals(rule.label)) {
                m.P -= t.weight;
            }
            else {
                m.N -= t.weight;
            }
        }
    }

    public double getP(Integer item) {
        return examples.get(item).P;
    }

    public double getN(Integer item) {
        return examples.get(item).N;
    }

    public PNArray deepCopy() {
        PNArray c = new PNArray();
        c.rule = rule.deepCopy();
        c.uniqueItems = uniqueItems;
        c.db = db.copy();
        c.examples = new HashMap<Integer,PNMatch>(uniqueItems.size());
        for(Integer item : examples.keySet()) {
            c.examples.put(item,examples.get(item).copy());
        }
        return c;
    }
}
