package gr.ntua.cslab.orchestrator.client;
import gr.ntua.cslab.orchestrator.beans.ProvidedResourcesList;
import gr.ntua.cslab.orchestrator.beans.ResourceInfo;
import static gr.ntua.cslab.celar.server.beans.SimpleReflectiveEntity.unmarshalGeneric;
import gr.ntua.cslab.orchestrator.client.conf.ClientConfiguration;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBException;

/**
 *
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
     * @return
     * @throws IOException
     * @throws JAXBException 
     */
    public List<ResourceInfo> getFlavors() throws IOException, JAXBException {
        String outcome = this.issueRequest("GET", "resources/flavors/", null);
        ProvidedResourcesList rv;
        rv = (ProvidedResourcesList) unmarshalGeneric(ProvidedResourcesList.class, outcome); 
        return rv;
    }
    
     /**
     * Returns a list of ProvidedResourceInfo with the provided resources available 
     * @return
     * @throws IOException
     * @throws JAXBException 
     */
    public List<ResourceInfo>getImages() throws IOException, JAXBException {
        String outcome = this.issueRequest("GET", "resources/images/", null);
        ProvidedResourcesList rv;
        rv = (ProvidedResourcesList) unmarshalGeneric(ProvidedResourcesList.class, outcome); 
        return rv;
    }
    
    
}
