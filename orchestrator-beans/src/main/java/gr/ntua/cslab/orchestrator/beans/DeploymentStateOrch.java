package gr.ntua.cslab.orchestrator.beans;

import java.util.Map;
import static java.util.Map.Entry;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmantas
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeploymentStateOrch {
    public String deploymentId;
    public Map<String,String> props;

    public DeploymentStateOrch() {
    }

    public DeploymentStateOrch(String deoploymentId, Map props) {
        this.deploymentId = deoploymentId;
        this.props = props;
    }
    
    public Map<String,String> getIPs(){
        Map<String,String> rv = new java.util.TreeMap<>();
        //hostname
        for (Entry<String, String> e : props.entrySet()) {
            String key = e.getKey(), value=e.getValue();
            if(key.contains("hostname"))
                rv.put(key, value);
        }
        
        return rv;
    }
    
    
    
}