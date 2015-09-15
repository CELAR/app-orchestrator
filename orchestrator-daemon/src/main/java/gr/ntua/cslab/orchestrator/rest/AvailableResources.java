package gr.ntua.cslab.orchestrator.rest;

import gr.ntua.cslab.celar.server.beans.structured.ProvidedResourceInfo;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import static gr.ntua.cslab.database.EntitySearchers.searchProvidedResourceSpecs;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author cmantas
 */
@Path("/resources/")
public class AvailableResources {
    
    @GET
    @Path("flavors/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public static REList<ProvidedResourceInfo> getFlavors() throws Exception {
          return new REList(searchProvidedResourceSpecs("VM_FLAVOR"));
    }
    
    @GET
    @Path("images/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public static REList<ProvidedResourceInfo>  getImages() throws  Exception {
        return new REList(searchProvidedResourceSpecs("VM_IMAGE"));
    }
    
}
