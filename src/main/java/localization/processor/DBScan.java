package localization.processor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Brad on 7/12/2017.
 */
public class DBScan {

    private int range;
    private int minPoints;

    private final List<Point> points;
    private final List<Cluster> clusterList = new ArrayList<>();
    private boolean[] visited;

    /**
     *
     *
     * @param points The points to cluster
     * @param range The max range between points
     * @param minPoints Minimum number of points to form a cluster
     */
    public DBScan(List<Point> points, int range, int minPoints) {
        this.points = points;
        this.range = range;
        this.minPoints = minPoints;
        visited = new boolean[points.size()];
    }

    /**
     * Creates the clusters
     */
    public void cluster() {
        if(clusterList.size() > 0) // don't redo analysis
            return;
        int count = 0;
        for(Point p : points) {
            if(!visited[count]) {
                visited[count] = true;
                List<Integer> nearby = pointsInRange(p);

                Cluster c = new Cluster();
                clusterList.add(c);
                findPoints(p,c,nearby);
            }
            count++;
        }
    }
    private void findPoints(Point p, Cluster c, List<Integer> nearby) {
        c.addPoint(p);

        int count = nearby.size();
        for(int i = 0; i < count; i++) {
            Integer idx = nearby.get(i);
            Point point = points.get(idx);
            if(!visited[idx]) {
                visited[idx] = true;
                List<Integer> nl = pointsInRange(point);
                //if(nl.size() > minPoints) {
                nearby.addAll(nl);
                count = nearby.size();
                //}
            }

            if(!c.getPoints().contains(point)) {
                c.addPoint(point);
            }
        }
        if(nearby.size() < minPoints) {
            clusterList.remove(c);
        }
        for(Integer i : nearby) {
            visited[i] = true;
        }
    }

    /**
     * Returns the list of clusters;
     *
     * @return The list of clusters, or an empty list is cluster() has not been called.
     */
    public List<Cluster> getClusters() {
        return clusterList;
    }

    /**
     * Calculates the bounds of each cluster
     *
     * @return The list of rectangle bounds of each cluster
     */
    public List<Rectangle> getClusterBounds() {
        return clusterList.stream().map(Cluster::getBounds).collect(Collectors.toList());
    }
    private List<Integer> pointsInRange(Point p) {
        List<Integer> lst = new ArrayList<>();
        for(int i = 0; i < points.size(); i++ ) {
            Point c = points.get(i);
            double distance = Math.abs(Math.sqrt(Math.pow(p.x - c.x, 2) + Math.pow(p.y - c.y, 2)));
            if(distance < range) {
                lst.add(i);
            }
        }
        return lst;
    }
}
