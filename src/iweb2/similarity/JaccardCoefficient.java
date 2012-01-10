package iweb2.similarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculates Jaccard coefficient for two sets of items. 
 * 
 */
public class JaccardCoefficient implements SimilarityMeasure {

    private static final long serialVersionUID = -5051498381470492495L;

    public JaccardCoefficient() {
        // empty
    }

    public double similarity(String[] x, String[] y) {
        double sim = 0.0d;
        if ((x != null && y != null)) { // && (x.length > 0 || y.length > 0)) {
            if (x.length == 0) {
                throw new IllegalArgumentException("The arguments x and y must be not NULL and either x or y must be non-empty.");
            }

            if (y.length == 0) {
                throw new IllegalArgumentException("The arguments x and y must be not NULL and either x or y must be non-empty.");
            }

            sim = similarity(Arrays.asList(x), Arrays.asList(y));
        } else {
            throw new IllegalArgumentException("The arguments x and y must be not NULL and either x or y must be non-empty.");
        }
        return sim;
    }

    private double similarity(List<String> x, List<String> y) {
        if (x.size() == 0 || y.size() == 0) {
            return 0.0;
        }

        List<String> noNullX = new ArrayList<String>();
        List<String> noNullY = new ArrayList<String>();

        for (String theString : x) {
            if (theString != null) {
                noNullX.add(theString);
            }
        }

        for (String theString : y) {
            if (theString != null) {
                noNullY.add(theString);
            }
        }

        Set<String> unionXY = new HashSet<String>(noNullX);
        unionXY.addAll(noNullY);

        Set<String> intersectionXY = new HashSet<String>(noNullX);
        intersectionXY.retainAll(noNullY);

        return (double) intersectionXY.size() / (double) unionXY.size();
    }
}
