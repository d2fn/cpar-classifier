/**
 * ClassifierPerformance
 */
public class ClassifierPerformance {

    public int tp,tn,fp,fn,u = 0;
    public int p,n = 0;

    public void tp() {
        tp++;
    }

    public void tn() {
        tn++;
    }

    public void fp() {
        fp++;
    }

    public void fn() {
        fn++;
    }

    public void u() {
        u++;
    }

    public void p() {
        p++;
    }

    public void n() {
        n++;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[")
          .append("P=").append(p)
          .append(",N=").append(n)
          .append("][")
          .append("TP=").append(tp)
          .append(",TN=").append(tn)
          .append(",FP=").append(fp)
          .append(",FN=").append(fn)
          .append(",U=").append(u)
          .append("]");
        return sb.toString();
    }
}
