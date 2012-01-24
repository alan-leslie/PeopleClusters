package pagelinks;

/**
 *
 * @author al
 */
public class LinkMagnitudeImpl implements LinkMagnitude {

    private String tag = null;
    private double magnitude = 0.0;

    public LinkMagnitudeImpl(String tag, double magnitude) {
        this.tag = tag;
        this.magnitude = magnitude;
    }

    @Override
    public String getLink() {
        return this.tag;
    }

    @Override
    public double getMagnitude() {
        return this.magnitude;
    }

    @Override
    public double getMagnitudeSqd() {
        return this.magnitude * this.magnitude;
    }

    @Override
    public String toString() {
        return "[" + this.tag
                + ", " + this.getMagnitude() + "]";
    }

    @Override
    public int compareTo(LinkMagnitude o) {
        double diff = this.magnitude - o.getMagnitude();
        if (diff > 0) {
            return -1;
        } else if (diff < 0) {
            return 1;
        }
        return 0;
    }
}
