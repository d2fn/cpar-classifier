import java.util.*;

public class Rule {

    private Set<Integer> itemset;
    public String label;
    private Double laplaceAccuracy;

    public Rule(String label, Set<Integer> itemset) {
        this.label = label;
        this.itemset = itemset;
    }

    public Rule(String label) {
        this.label = label;
        itemset = new HashSet<Integer>(0);
    }

    public boolean satisfiesBody(Transaction t) {
        if(itemset.isEmpty()) {
            return true;
        }
        Set<Integer> test = new HashSet<Integer>(itemset);
        return !test.retainAll(t);
    }


    /**
     * @param data - training data/transactions
     * @return an int array with element 0 representing positive matches in this rule, and the
     *         element 1 representing the negative matches
     */
    public PNMatch runRule(Collection<Transaction> data) {
        double P = 0;
        double N = 0;
        for(Transaction t : data) {
            if(satisfiesBody(t)) {
                if(t.getLabel().equals(label)) {
                    P += t.weight;
                }
                else {
                    N += t.weight;
                }
            }
        }
        return new PNMatch(P,N);
    }

    public List<Integer> items() {
        List<Integer> sortedItems = new ArrayList<Integer>(itemset.size());
        sortedItems.addAll(itemset);
        Collections.sort(sortedItems);
        return sortedItems;
    }

    /**
     * @param item - the new predicate to append to the rule
     * @return a new rule with the given predicate appended to the end
     */
    public Rule addPredicate(Integer item) {
        return new Rule(label,SetMath.append(item,itemset));
    }

    /**
     * @param candidate - candidate item predicate to create more specific rule
     * @param data - all training data
     * @return the information gained by adding the candidate to this rule's list of predicates
     */
    public double calculateGain(Integer candidate, Collection<Transaction> data) {

        PNMatch m = runRule(data);
        double P = m.P;
        double N = m.N;

        // create a new rule with the given candidate added as a predicate
        Rule r = addPredicate(candidate);

        m = r.runRule(data);
        double Pstar = m.P;
        double Nstar = m.N;

        if(Pstar == 0 || Nstar == 0) {
            return 0.0;
        }

        return Pstar * (
                Math.log(Pstar/(Pstar+Nstar))
                    -
                Math.log(P/(P+N)) );
    }

    public boolean hasPredicate(Integer item) {
        return itemset.contains(item);
    }

    public Rule deepCopy() {
        return new Rule(label,new HashSet<Integer>(itemset));
    }

    public String toString() {
        return "items: "+ SetMath.mkstr(items(),"{",",","}") + ", label: "+label+ ", laplaceAccuracy: "+ laplaceAccuracy;
    }

    public boolean empty() {
       return itemset.isEmpty();
    }

    public Double getAccuracy() {
        return laplaceAccuracy;
    }

    public void calculateAccuracy(Database db) {

        // count the # of transactions that satisfy the rule's body
        int bodySatisfied = 0;
        // among those transactions that satisfy the rule body, which are of the correct class
        int correct = 0;
        // number of classes (1/0)
        int k = 0;

        for(Transaction t : db.data()) {
            if(satisfiesBody(t)) {
                bodySatisfied++;
                if(t.getLabel().equals(label)) {
                    correct++;
                }
            }
        }

        laplaceAccuracy = (double)(correct+1) / (double)(bodySatisfied+k);
    }

   public boolean hasSameBodyAs(Rule rule) {
       Set<Integer> body = new HashSet<Integer>(itemset);
       body.removeAll(rule.itemset);
       return body.isEmpty();
    }
}
