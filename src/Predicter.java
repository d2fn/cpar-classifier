import java.io.FileNotFoundException;
import java.util.List;

public class Predicter {
    public Predicter(String trainingFile, String unlabeledFile, String outputFile) {
        try {
            List<Transaction> trainingSet  = TransactionDAO.fromFile(trainingFile,true);
            List<Transaction> unlabeledSet =  TransactionDAO.fromFile(unlabeledFile, false);
            Classifier c = new Classifier(trainingSet);
            c.label(unlabeledSet);
            TransactionDAO.writeLabels(outputFile,unlabeledSet);
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found:"+e.getMessage());
            System.exit(1);
        }
    }
}
