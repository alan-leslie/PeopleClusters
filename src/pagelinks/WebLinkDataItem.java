package pagelinks;

//import com.alag.ci.blog.search.RetrievedDataEntry;
//import com.alag.ci.textanalysis.TagMagnitudeVector;
import java.util.List;
import java.util.Map;

public interface WebLinkDataItem {
//    public Map<String, String> getAttributeMap();
    public String getSource();
    public int numLinks();
    public String getLinkAt(int index);
//    public RetrievedDataEntry getData();
//    public TagMagnitudeVector getTagMagnitudeVector() ;    
    public List<String> getLinks() ;
//    public Integer getClusterId();
//    public void setClusterId(Integer clusterId); 
//    public void setCiRelated(boolean ciRelated);

    public LinkMagnitudeVector getLinkMagnitudeVector();

    public Integer getClusterId();

    public void setClusterId(Integer clusterId);
}
