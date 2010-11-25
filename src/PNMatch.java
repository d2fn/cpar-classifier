public class PNMatch {

    public int item;
    public double P; // + matches
    public double N; // - matches

    public PNMatch(double P, double N) {
        this.P = P;
        this.N = N;
    }

    public PNMatch copy() {
        PNMatch c = new PNMatch(P,N);
        c.item = item;
        return c;
    }
}
