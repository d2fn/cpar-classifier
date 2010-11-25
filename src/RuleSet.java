import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RuleSet
 * Used to gather rules with bodies matching a given example transaction
 * Computes the best label based on the best average accuracy of the top k rules for each class
 * The class with the highest average accuracy is chosen
 * @author Dietrich Featherston
 */
public class RuleSet {

    private Transaction t;
    private Map<String, List<Rule>> rules;
    private int numRulesPerClass;

    public RuleSet(Transaction t, int numRulesPerClass) {
        this.t = t;
        this.rules = new HashMap<String,List<Rule>>();
        this.numRulesPerClass = numRulesPerClass;
    }

    public void testRule(Rule r) {

        if(!r.satisfiesBody(t)) return;

        if(!rules.containsKey(r.label)) {
            rules.put(r.label,new ArrayList<Rule>(numRulesPerClass));
        }

        List<Rule> classRules = rules.get(r.label);
        classRules.add(r);

        if(classRules.size() > numRulesPerClass) {
            removeWeakestRule(classRules);
        }
    }

    private void removeWeakestRule(List<Rule> rules) {
        Rule weak = null;
        for(Rule r : rules) {
            if(weak == null) {
                weak = r;
            }
            else {
                if(r.getAccuracy() < weak.getAccuracy()) {
                    weak = r;
                }
            }
        }
        rules.remove(weak);
    }

    private double averageAccuracy(List<Rule> rules) {
        double acc = 0.0;
        for(Rule r : rules) {
            acc += r.getAccuracy();
        }
        return acc / rules.size();
    }

    public String getBestClass() {

        double bestAccuracy = 0.0;
        String bestClass = "unk";

        for(String label : rules.keySet()) {
            if(averageAccuracy(rules.get(label)) > bestAccuracy) {
                bestClass = label;
            }
        }

        return bestClass;
    }
}
