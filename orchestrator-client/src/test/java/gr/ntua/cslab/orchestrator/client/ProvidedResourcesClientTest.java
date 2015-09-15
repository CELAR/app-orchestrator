package gr.ntua.cslab.orchestrator.client;

import gr.ntua.cslab.celar.server.beans.SimpleReflectiveEntity;
import gr.ntua.cslab.orchestrator.beans.ResourceInfo;
import gr.ntua.cslab.orchestrator.client.conf.ClientConfiguration;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cmantas
 */
public class ProvidedResourcesClientTest {
    
    public ProvidedResourcesClientTest() {
    }

    public static void main(String args[]) throws IOException, JAXBException{
        ClientConfiguration conf = new ClientConfiguration("83.212.116.63", 80);
        ProvidedResourcesClient client = new ProvidedResourcesClient();
        client.setConfiguration(conf);
        
        List<ResourceInfo> flavors = client.getFlavors();
        for(ResourceInfo flavor: flavors){
          System.out.println(flavor);
        }
        
        List<ResourceInfo> images = client.getImages();
        for(ResourceInfo image: images){
          System.out.println(image);
        }
    }
    
}
