package localization.processor;

import java.util.List;

/**
 * Created by Brad on 5/23/2017.
 */
public abstract class ClusteringAlgorithm {

    public abstract void cluster();

    public abstract List<Cluster> getClusters();
}
