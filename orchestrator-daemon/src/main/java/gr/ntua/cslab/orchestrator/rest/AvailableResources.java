/*
 * Copyright 2014 CELAR.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
@Path("resources/")
public class AvailableResources {
    
    
    @Path("flavors/")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public static REList<ProvidedResourceInfo> getFlavors() throws Exception {
        return new REList(searchProvidedResourceSpecs("VM_FLAVOR"));
    }
    
    @Path("images/")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public static REList<ProvidedResourceInfo>  getImages() throws  Exception {
        return new REList(searchProvidedResourceSpecs("VM_IMAGE"));
    }
    
}
