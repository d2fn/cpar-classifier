import java.io.FileNotFoundException;
import java.util.List;

/**
 * Tester
 * @author Dietrich Featherston
 */
public class Tester {

    public Tester(String trainingFile, String testFile) {
        try {
            List<Transaction> trainingSet = TransactionDAO.fromFile(trainingFile,true);
            List<Transaction> testSet = TransactionDAO.fromFile(testFile,true);
            Classifier c = new Classifier(trainingSet);
            ClassifierPerformance perf = c.test(testSet);
            System.out.println(perf.toString());
        } catch (FileNotFoundException e) {
            System.out.println("File not found:"+e.getMessage());
            System.exit(1);
        }
    }
}
