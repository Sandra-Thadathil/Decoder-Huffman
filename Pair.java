

// This class creates Pair object to store letters and probability
public class Pair implements Comparable<Pair> {
    private char value;
    private double prob;

    // constructor to initialize the instance variables
    public Pair (char value, double prob) {
        this.value = value;
        this.prob = prob;
    }

    // getters
    public char getValue() {
        return value;
    }

    public double getProb() {
        return prob;
    }

    //setters
    public void setValue(char value) {
        this.value = value;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    // toString method
    public String toString() {
        return (value + " " + prob);
    }

    /*
     * compareTo method - overrides the compareTo method of the Comparable interface.
     * @param - Pair p
     * @return - int
     */

    @Override
    public int compareTo(Pair p){
        return Double.compare(this.getProb(), p.getProb());
    }
}
