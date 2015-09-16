package gr.ntua.cslab.orchestrator.client;
import gr.ntua.cslab.orchestrator.beans.BeanList;
import gr.ntua.cslab.orchestrator.beans.ResourceInfo;
import static gr.ntua.cslab.celar.server.beans.SimpleReflectiveEntity.unmarshalGeneric;
import gr.ntua.cslab.orchestrator.client.conf.ClientConfiguration;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBException;

/**
 * A client fetching ProvidedResources from an orchestrator's REST API
 * @author cmantas
 */
public class ProvidedResourcesClient extends AbstractClient{
    
    public ProvidedResourcesClient(){super();}
    
     public ProvidedResourcesClient(ClientConfiguration conf){
         super();
         this.setConfiguration(conf);
     }
    
    /**
     * Returns a list of ProvidedResourceInfo with the flavors available 
     * @return a list of ResourceInfo
     * @throws IOException
     * @throws JAXBException 
     */
    public List<ResourceInfo> getFlavors() throws IOException, JAXBException {
        String outcome = this.issueRequest("GET", "resources/flavors/", null);
        BeanList rv;
        rv = (BeanList) unmarshalGeneric(BeanList.class, outcome); 
        return rv;
    }
    
     /**
     * Returns a list of ProvidedResourceInfo with the images available 
     * @return a list of ResourceInfo
     * @throws IOException
     * @throws JAXBException 
     */
    public List<ResourceInfo>getImages() throws IOException, JAXBException {
        String outcome = this.issueRequest("GET", "resources/images/", null);
        BeanList rv;
        rv = (BeanList) unmarshalGeneric(BeanList.class, outcome); 
        return rv;
    }
    
    
}
