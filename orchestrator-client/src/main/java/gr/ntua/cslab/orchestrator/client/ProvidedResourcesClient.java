package gr.ntua.cslab.orchestrator.client;
import gr.ntua.cslab.celar.server.beans.structured.ProvidedResourceInfo;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.xml.bind.JAXBException;

/**
 *
 * @author cmantas
 */
public class ProvidedResourcesClient extends AbstractClient{
    
    public ProvidedResourcesClient(){super();}
    
    /**
     * Returns a list of ProvidedResourceInfo with the flavors available 
     * @return
     * @throws IOException
     * @throws JAXBException 
     */
    public List<ProvidedResourceInfo> getFlavors() throws IOException, JAXBException {
        String outcome = this.issueRequest("GET", "resources/flavors/", null);
        REList rv = new REList();
        rv.unmarshal(new ByteArrayInputStream(outcome.getBytes(StandardCharsets.UTF_8)));
        return rv;
    }
    
     /**
     * Returns a list of ProvidedResourceInfo with the provided resources available 
     * @return
     * @throws IOException
     * @throws JAXBException 
     */
    public REList<ProvidedResourceInfo> getImages() throws IOException, JAXBException {
        String outcome = this.issueRequest("GET", "resources/images/", null);
        REList rv = new REList();
        rv.unmarshal(new ByteArrayInputStream(outcome.getBytes(StandardCharsets.UTF_8)));
        return rv;
    }
    
    
}
