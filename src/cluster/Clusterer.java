package cluster;

import pagelinks.WebLinkDataItem;
import java.util.List;

public interface Clusterer {
    public void setDataSet(List<WebLinkDataItem> dataSet);
    public List<WebLinkCluster> cluster();
}
