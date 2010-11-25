import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * TransactionDAO
 */
public class TransactionDAO {

    public static List<Transaction> fromFile(String inputFile, boolean labeled) throws FileNotFoundException {
        List<Transaction> xlist = new ArrayList<Transaction>(100);
        Scanner scanner = new Scanner(new FileReader(inputFile));
        int id = 1;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] fields = line.split(" ");
            Transaction t;
            if(labeled) {
                t = Transaction.buildFromLabeledTransaction(fields);
            }
            else {
                t = Transaction.buildFromUnlabeledTransaction(fields);
            }
            t.id = id++;
            xlist.add(t);
        }
        return xlist;
    }

    public static void toFile(String outputFile, List<Transaction> data) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File(outputFile));
        for(Transaction t : data) {
            pw.println(SetMath.mkstr(t.orderedItems(),""," ","") + " " + t.getLabel());
        }
        pw.flush();
        pw.close();
    }

    public static void writeLabels(String outputFile, List<Transaction> data) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File(outputFile));
        for(Transaction t : data) {
            pw.println(t.getLabel());
        }
        pw.flush();
        pw.close();
    }
}

