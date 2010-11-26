import java.io.FileNotFoundException;
import java.util.*;

public class Classifier {

    private Database P;
    private Database N;

    private Database P2;
    private Database N2;

    private PNArray A;
    private PNArray A2;

    private List<Rule> rules;

    private int numRulesPerClass = 6;
    private double totalWeightFactor = 0.001;
    private double decayFactor = 1.0/4.0;

    private Database db;

    private Set<Integer> uniqueItems;
    private Set<String> uniqueLabels;

    public Classifier(Collection<Transaction> trainingSet) throws FileNotFoundException {
        db = new Database();
        db.addAll(trainingSet);
        train();
    }

    private void train() {

        this.uniqueItems = new HashSet<Integer>(50);
        this.uniqueLabels = new HashSet<String>(2);

        // scan database once to get unique items and class labels
        for (Transaction x : db.data()) {

            uniqueItems.addAll(x);
            uniqueLabels.add(x.getLabel());

            if (!x.isLabeled()) {
                throw new RuntimeException("All training data must be labeled");
            }

        }

        rules = new ArrayList<Rule>();


//        buildRules("1");
//        buildRules("0");
//
//        build rules for each unique class label
        for (String label : uniqueLabels) {
            buildRules(label);
        }

//        while(!positiveExamples.isEmpty()) {
//            List<Transaction> pPrime = SetMath.cloneList(positiveExamples);
//            List<Transaction> nPrime = SetMath.cloneList(negativeExamples);
//
//
//        }


        System.out.println("done");
    }

    private void process(Rule r, Database db, PNArray pnarray) {
        List<Transaction> toremove = new ArrayList<Transaction>(db.size());
        for (Transaction t : db.data()) {
            if (!r.satisfiesBody(t)) {
                toremove.add(t);
            }
        }
        for (Transaction t : toremove) {
            db.remove(t);
            pnarray.remove(t);
        }
    }

    private double totalWeight(Database db) {
        double total = 0.0;
        for (Transaction t : db.data()) {
            total += t.weight;
        }
        return total;
    }

    private void buildRules(String label) {

        P = new Database();
        N = new Database();

        for (Transaction x : db.data()) {

            if (x.getLabel().equals(label)) {
                P.add(x);
            } else {
                N.add(x);
            }
        }


        A = new PNArray(new Rule(label), uniqueItems, db);
        double initialWeight = totalWeight(P);
        double totalWeightThreshold = totalWeightFactor * initialWeight;

        while (totalWeight(P) > totalWeightThreshold) {

            Rule r = new Rule(label);

            P2 = P.copy();
            N2 = N.copy();
            A2 = A.deepCopy();

            double P2weight = totalWeight(P2);
            double N2weight = totalWeight(N2);

            while (true) {
//                                Gain best = A2.calculateBestGain(P2weight,N2weight);
                Gain best = A2.calculateBestGain(P2.size(), N2.size());
                if (best.gain < 1.0) {
                    break;
                }

                r = r.addPredicate(best.item);

                // remove
                process(r, P2, A2);
                process(r, N2, A2);
            }

            r.calculateAccuracy(db);

            boolean added = addRule(r);
//            if(added) {
                for (Transaction t : P.data()) {
                    if (r.satisfiesBody(t)) {
                        t.weight = decayFactor * t.weight;
                    }
                }
//            }

            A = new PNArray(new Rule(label), uniqueItems, db);
        }

        resetweight(db);
    }

    private boolean addRule(Rule r) {
        if (!r.empty()) {
            if(!isDup(r)) {
                rules.add(r);
                System.out.println("Created new rule: " + r);
                return true;
            }
        }
        return false;
    }

    private boolean isDup(Rule rule) {
        for(Rule r : rules) {
            if(r.hasSameBodyAs(rule)) {
                return true;
            }
        }
        return false;
    }

    private void resetweight(Database db) {
        for(Transaction t : db.data()) {
            t.weight = 1.0;
        }
    }

    public ClassifierPerformance test(List<Transaction> testSet) {

        ClassifierPerformance perf = new ClassifierPerformance();

        for (Transaction t : testSet) {

            if(t.getLabel().equals("1")) {
                perf.p();
            }
            else {
                perf.n();
            }

            String guess = guessLabel(t);

            if(guess.equals("unk")) {
                perf.u();
                continue;
            }

            if (t.getLabel().equals(guess)) {
                if (guess.equals("1")) {
                    perf.tp();
                } else if(guess.equals("0")) {
                    perf.tn();
                }

            } else {
                if (guess.equals("1")) {
                    perf.fp();
                } else if(guess.equals("0")) {
                    perf.fn();
                }
            }
        }

        return perf;
    }

    public String guessLabel(Transaction t) {
        RuleSet ruleP = new RuleSet(t,numRulesPerClass);
        for (Rule r : rules) {
            ruleP.testRule(r);
        }
        return ruleP.getBestClass();
    }

    /**
     * after the classifier has been trained, write out labels to training set
     * @param unlabeledSet - unlabeled data which will be mutated by adding label data
     */
    public void label(List<Transaction> unlabeledSet) {
        for(Transaction t : unlabeledSet) {
            String guess = guessLabel(t);
            t.setLabel(guess);
        }
    }

    // data structure for calculating the gain of adding an item to a rule
    public static class Gain implements Comparable<Gain> {

        public Integer item;
        public Double gain;

        public int compareTo(Gain gain) {
            return this.gain.compareTo(gain.gain);
        }

        public double pctDropoff(Gain gain) {
            return gain.gain / this.gain;
        }
    }
}
