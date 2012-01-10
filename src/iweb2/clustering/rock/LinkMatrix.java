package iweb2.clustering.rock;

import cluster.WebLinkCluster;
import pagelinks.WebLinkDataItem;
import peopleclustrs.utils.ObjectToIndexMapping;
import iweb2.similarity.SimilarityMeasure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Calculates number of links between data points.
 */
public class LinkMatrix {

    private double th;
    double[][] pointSimilarityMatrix;
    int[][] pointNeighborMatrix;
    int[][] pointLinkMatrix;
    private ObjectToIndexMapping<WebLinkDataItem> objToIndexMapping;
    public static int TOP_N_TERMS = 20;
    
    
    public LinkMatrix(List<WebLinkDataItem> points, SimilarityMeasure pointSim, double th) {
        double[][] similarityMatrix = calculatePointSimilarities(points, pointSim);
        init(points, similarityMatrix, th);
    }
    
//    public LinkMatrix(WebLinkDataItem[] points, double[][] similarityMatrix, double th) {
//        init(points, similarityMatrix, th);
//    }

    private void init(List<WebLinkDataItem> points, double[][] similarityMatrix, double th) {
    
        this.th = th;
        
        objToIndexMapping = new ObjectToIndexMapping<WebLinkDataItem>();
        
        // Create WebLinkDataItem <-> Index mapping.
        for(WebLinkDataItem point : points) {
            objToIndexMapping.getIndex(point);
        }
        
        pointSimilarityMatrix = similarityMatrix;
        
        // Identify neighbors: a[i][j] == 1 if (i,j) are neighbors and 0 otherwise.
        int n = points.size();
        
        pointNeighborMatrix = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                if( pointSimilarityMatrix[i][j] >= th ) {
                    pointNeighborMatrix[i][j] = 1;
                }
                else {
                    pointNeighborMatrix[i][j] = 0;
                }
                pointNeighborMatrix[j][i] = pointNeighborMatrix[i][j];
            }
            pointNeighborMatrix[i][i] = 1;
        }
        
        // Calculate number of links between points
        pointLinkMatrix = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = i; j < n; j++) {
                pointLinkMatrix[i][j] = 
                    nLinksBetweenPoints(pointNeighborMatrix, i, j);
                pointLinkMatrix[j][i] = pointLinkMatrix[i][j]; 
            }
        }
        
    }
    
    
    public int getLinks(WebLinkDataItem p1, WebLinkDataItem p2) {
        int i = objToIndexMapping.getIndex(p1);
        int j = objToIndexMapping.getIndex(p2);
        return pointLinkMatrix[i][j];
    }
    
    /**
     * Calculates number of links between two clusters. Number of links between 
     * two clusters is the sum of links between all point pairs( p1, p2) where   
     * p1 belongs to the first cluster and p2 belongs to the other cluster.
     * 
     * @param clusterX
     * @param clusterY
     * 
     * @return link count between two clusters.
     */

    public int getLinks(WebLinkCluster clusterX, WebLinkCluster clusterY) {
        Set<WebLinkDataItem> itemsX = clusterX.getElements();
        Set<WebLinkDataItem> itemsY = clusterY.getElements();
        
        int linkSum = 0;
        
        for(WebLinkDataItem x : itemsX) {
            for(WebLinkDataItem y : itemsY) {
                linkSum += getLinks(x, y);
            }
        }
        return linkSum;
    }
    
    private int nLinksBetweenPoints(int[][] neighbors, int indexX, int indexY) {
        int nLinks = 0;
        for(int i = 0, n = neighbors.length; i < n; i++) {
            nLinks += neighbors[indexX][i] * neighbors[i][indexY];
        }
        return nLinks;
    }
    
    /*
     * Calculates similarity matrix for all points.
     */
    private double[][] calculatePointSimilarities(
            List<WebLinkDataItem> points, SimilarityMeasure pointSim) {
        List<String[]> theTags = new ArrayList<String[]>();
        
        int n = points.size();
        double[][] simMatrix = new double[n][n];
        
        for(int i = 0; i < n; i++) {
            WebLinkDataItem itemX = points.get(i);
            String[] attributesX = itemX.getLinks().toArray(new String[0]);
            theTags.add(attributesX);
        }
        
        for(int i = 0; i < n; i++) {
            String[] attributesX = theTags.get(i);
            for(int j = i + 1; j < n; j++) {
                String[] attributesY = theTags.get(j);
                simMatrix[i][j] = pointSim.similarity(
                        attributesX, attributesY);
                simMatrix[j][i] = simMatrix[i][j];
            }
            simMatrix[i][i] = 1.0;
        }

        return simMatrix;
    }
 
    public void printSimilarityMatrix() {
      System.out.println("Point Similarity matrix:");
      for(int i = 0; i < pointSimilarityMatrix.length; i++) {
          System.out.println(Arrays.toString(pointSimilarityMatrix[i]));
      }
    }

    public void printPointNeighborMatrix() {
        System.out.println("Point Neighbor matrix (th=" + String.valueOf(th) + "):");
        for(int i = 0; i < pointNeighborMatrix.length; i++) {
            System.out.println(Arrays.toString(pointNeighborMatrix[i]));
        }
     }

    public void printPointLinkMatrix() {
        System.out.println("Point Link matrix (th=" + String.valueOf(th) + "):");
        for(int i = 0; i < pointLinkMatrix.length; i++) {
            System.out.println(Arrays.toString(pointLinkMatrix[i]));
        }
     }  
}
