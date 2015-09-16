package gr.ntua.cslab.orchestrator.client;

import gr.ntua.cslab.orchestrator.beans.ResourceInfo;
import gr.ntua.cslab.orchestrator.client.conf.ClientConfiguration;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBException;

/**
 *
 * @author cmantas
 */
public class ProvidedResourcesClientTest {
    
    public ProvidedResourcesClientTest() {
    }

    public static void main(String args[]) throws IOException, JAXBException{
        ClientConfiguration conf = new ClientConfiguration("83.212.116.63", 80);
        ProvidedResourcesClient client = new ProvidedResourcesClient(conf);
        
        //we are going to be populating a list of "ResourceInfo"
        List<ResourceInfo> flavors;
        //get the flavors from the client
        flavors= client.getFlavors();
        
        // we could iterate the list, etc.
//        for(ResourceInfo flavor: flavors){
//          System.out.println(flavor);
//        }
        
        //let's see what's in a ResourceInfo containing a flavor
        ResourceInfo someFlavor = flavors.get(0);
        System.out.println(someFlavor);
        
        //each ResourceInfo has a list of 'specs'
        ResourceInfo.ResourceSpec someSpec = someFlavor.specs.get(0);
        //each 'ResourceSpec' has a property and value
        System.out.println(someSpec.property+" -> "+someSpec.value);
        System.out.println();
        
        //repeat the process for images
        List<ResourceInfo> images = client.getImages();
        
        ResourceInfo someImage = images.get(0);
        System.out.println(someImage);
        ResourceInfo.ResourceSpec someSpec2 = someImage.specs.get(0);
        System.out.println(someSpec2.property+" -> "+someSpec2.value);
        System.out.println();
        
//        for(ResourceInfo image: images){
//          System.out.println(image);
//        }
    }
    
}
