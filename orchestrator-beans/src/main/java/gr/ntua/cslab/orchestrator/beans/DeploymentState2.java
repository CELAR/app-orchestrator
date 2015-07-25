package gr.ntua.cslab.orchestrator.beans;

import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmantas
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeploymentState2 {
    public String deploymentId;
    public Map<String,String> props;

    public DeploymentState2() {
    }

    public DeploymentState2(String deoploymentId, Map props) {
        this.deploymentId = deoploymentId;
        this.props = props;
    }
    
    
    
}
