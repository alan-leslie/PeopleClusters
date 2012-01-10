/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pagelinks;

import pagelinks.LinkMagnitude;
import pagelinks.LinkMagnitudeVector;
import java.util.*;

/**
 *
 * @author al
 */
public class LinkMagnitudeVectorImpl implements LinkMagnitudeVector {

    private Map<String, LinkMagnitude> tagMagnitudesMap = null;

    public LinkMagnitudeVectorImpl(List<LinkMagnitude> tagMagnitudes) {
        normalize(tagMagnitudes);
    }

    private void normalize(List<LinkMagnitude> tagMagnitudes) {
        if ((tagMagnitudes == null) || (tagMagnitudes.isEmpty())) {
            return;
        }

        Map<String, LinkMagnitude> notNormLinkMagnitudesMap = new HashMap<String, LinkMagnitude>();
        for (LinkMagnitude tm : tagMagnitudes) {
            LinkMagnitude otherTm = notNormLinkMagnitudesMap.get(tm.getLink());
            double magnitude = tm.getMagnitude();
            if (otherTm != null) {
                magnitude = magnitude + otherTm.getMagnitude();
            }
            LinkMagnitude newNotNormalizedTm = new LinkMagnitudeImpl(tm.getLink(),
                    magnitude);
            notNormLinkMagnitudesMap.put(tm.getLink(), newNotNormalizedTm);
        }

        double sumSqd = 0.;
        for (LinkMagnitude tm : notNormLinkMagnitudesMap.values()) {
            sumSqd += tm.getMagnitudeSqd();
        }
        if (sumSqd == 0.) {
            sumSqd = 1. / notNormLinkMagnitudesMap.size();
        }

        tagMagnitudesMap = new HashMap<String, LinkMagnitude>();
        double normFactor = Math.sqrt(sumSqd);
        for (LinkMagnitude tm : notNormLinkMagnitudesMap.values()) {
            LinkMagnitude otherTm = this.tagMagnitudesMap.get(tm.getLink());
            double magnitude = tm.getMagnitude();
            if (otherTm != null) {
                magnitude = mergeMagnitudes(magnitude,
                        otherTm.getMagnitude() * normFactor);
            }
            LinkMagnitude normalizedTm = new LinkMagnitudeImpl(tm.getLink(),
                    (magnitude / normFactor));
            tagMagnitudesMap.put(tm.getLink(), normalizedTm);
        }
    }

    public List<LinkMagnitude> getLinkMagnitudes() {
        List<LinkMagnitude> sortedLinkMagnitudes = new ArrayList<LinkMagnitude>();
        if (tagMagnitudesMap != null) {
            sortedLinkMagnitudes.addAll(tagMagnitudesMap.values());
            Collections.sort(sortedLinkMagnitudes);
        }
        return sortedLinkMagnitudes;
    }

    public Map<String, LinkMagnitude> getLinkMagnitudeMap() {
        Map<String, LinkMagnitude> theMap = new HashMap<String, LinkMagnitude>();
        theMap.putAll(tagMagnitudesMap);
        return theMap;
    }

    private double mergeMagnitudes(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    public double dotProduct(LinkMagnitudeVector o) {
        Map<String, LinkMagnitude> otherMap = o.getLinkMagnitudeMap();
        double dotProduct = 0.;
        for (String tag : this.tagMagnitudesMap.keySet()) {
            LinkMagnitude otherTm = otherMap.get(tag);
            if (otherTm != null) {
                LinkMagnitude tm = this.tagMagnitudesMap.get(tag);
                dotProduct += tm.getMagnitude() * otherTm.getMagnitude();
            }
        }
        return dotProduct;
    }

    public LinkMagnitudeVector add(LinkMagnitudeVector o) {
        Map<String, LinkMagnitude> otherMap = o.getLinkMagnitudeMap();
        Map<String, String> uniqueTags = new HashMap<String, String>();
        for (String tag : this.tagMagnitudesMap.keySet()) {
            uniqueTags.put(tag, tag);
        }
        for (String tag : otherMap.keySet()) {
            uniqueTags.put(tag, tag);
        }
        List<LinkMagnitude> tagMagnitudesList = new ArrayList<LinkMagnitude>(uniqueTags.size());
        for (String tag : uniqueTags.keySet()) {
            LinkMagnitude tm = mergeLinkMagnitudes(this.tagMagnitudesMap.get(tag),
                    otherMap.get(tag));
            tagMagnitudesList.add(tm);
        }
        return new LinkMagnitudeVectorImpl(tagMagnitudesList);
    }

    public LinkMagnitudeVector add(Collection<LinkMagnitudeVector> tmList) {
        Map<String, Double> uniqueTags = new HashMap<String, Double>();
        if(tagMagnitudesMap != null){
        for (LinkMagnitude tagMagnitude : this.tagMagnitudesMap.values()) {
            uniqueTags.put(tagMagnitude.getLink(),
                    new Double(tagMagnitude.getMagnitudeSqd()));
        }
        }
        for (LinkMagnitudeVector tmv : tmList) {
            Map<String, LinkMagnitude> tagMap = tmv.getLinkMagnitudeMap();
            for (LinkMagnitude tm : tagMap.values()) {
                Double sumSqd = uniqueTags.get(tm.getLink());
                if (sumSqd == null) {
                    uniqueTags.put(tm.getLink(), tm.getMagnitudeSqd());
                } else {
                    sumSqd = new Double(sumSqd.doubleValue() + tm.getMagnitudeSqd());
                    uniqueTags.put(tm.getLink(), sumSqd);
                }
            }
        }
        List<LinkMagnitude> newList = new ArrayList<LinkMagnitude>();
        for (String tag : uniqueTags.keySet()) {
            newList.add(new LinkMagnitudeImpl(tag, Math.sqrt(uniqueTags.get(tag))));
        }
        return new LinkMagnitudeVectorImpl(newList);
    }

    private LinkMagnitude mergeLinkMagnitudes(LinkMagnitude a, LinkMagnitude b) {
        if (a == null) {
            if (b == null) {
                return null;
            }
            return b;
        } else if (b == null) {
            return a;
        } else {
            double magnitude = mergeMagnitudes(a.getMagnitude(), b.getMagnitude());
            return new LinkMagnitudeImpl(a.getLink(), magnitude);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<LinkMagnitude> sortedList = getLinkMagnitudes();
        double sumSqd = 0.;
        for (LinkMagnitude tm : sortedList) {
            sb.append(tm);
            sumSqd += tm.getMagnitude() * tm.getMagnitude();
        }
        // sb.append("\nSumSqd = " + sumSqd);
        return sb.toString();
    }
}

