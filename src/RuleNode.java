import java.util.List;
import java.util.Set;

public class RuleNode {

    private Rule rule;
    private Integer P,N;
    private PNArray pnarray;

    private List<RuleNode> children;

    public RuleNode(Rule rule, Set<Integer> uniqueItems, Database db) {
        pnarray = new PNArray(rule,uniqueItems,db);
    }

    
}
