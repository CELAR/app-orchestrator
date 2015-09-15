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

import static gr.ntua.cslab.database.EntityTools.store;
//import gr.ntua.cslab.orchestrator.beans.DeploymentState;
import gr.ntua.cslab.orchestrator.shared.ServerStaticComponents;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import gr.ntua.cslab.celar.server.beans.DeploymentState;
import gr.ntua.cslab.celar.server.beans.structured.ProvidedResourceInfo;
import static gr.ntua.cslab.database.EntitySearchers.searchProvidedResourceSpecs;
import gr.ntua.cslab.orchestrator.beans.DeploymentStateOrch;
import java.util.List;
import static java.util.logging.Level.*;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Service used to return the IP addresses of the VMs for the deployment.
 *
 * @author Giannis Giannakopoulos
 */
@Path("myresources/")
public class DeploymentStateResource1 {
    static Logger logger = Logger.getLogger(DeploymentStateResource1.class.getName());
    
    private final static String deploymentId = ServerStaticComponents.properties.getProperty("slipstream.deployment.id");
    
    @GET
    public static DeploymentStateOrch writeDeploymentState() {
        try {
//            openConnection(ServerStaticComponents.properties);
            Map<String,String> propsMap  = ServerStaticComponents.service.getAllRuntimeParams(deploymentId);
            logger.log(INFO, "Got state map ({0} entries)", propsMap.size());
            DeploymentState depState = new DeploymentState(propsMap, deploymentId);
            store(depState);
            logger.log(INFO, "stored Deployment State");
            propsMap.put("timestamp", ""+depState.timestamp);
            
            return new DeploymentStateOrch(depState.deployment_id, propsMap);
        } catch (Exception ex) {
            Logger.getLogger(DeploymentStateResource1.class.getName()).log(Level.SEVERE, null, ex);
        }
           return null;
    }
    
    @Path("test/")
    @GET
    public static String getFlavors(){
        try {
            List<ProvidedResourceInfo> l= searchProvidedResourceSpecs("VM_FLAVOR");
            return l.toString();
        } catch (Exception ex) {
            Logger.getLogger(DeploymentStateResource1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    

}
