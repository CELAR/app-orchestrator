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
import gr.ntua.cslab.orchestrator.beans.DeploymentStateOrch;
import static java.util.logging.Level.*;

/**
 * Service used to return the IP addresses of the VMs for the deployment.
 *
 * @author Giannis Giannakopoulos
 */
@Path("state/")
public class DeploymentStateResource {
    static Logger logger = Logger.getLogger(DeploymentStateResource.class.getName());
    
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
            Logger.getLogger(DeploymentStateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
           return null;
    }
    
    @Path("string/")
    @GET
    public static Map<String, String> writeDeploymentState2() {
        try {
//            openConnection(ServerStaticComponents.properties);
            Map<String, String> test = ServerStaticComponents.service.getAllRuntimeParams(deploymentId);
            logger.log(INFO, "Got state map ({0} entries)", test.size());
            DeploymentState depState = new DeploymentState(test, deploymentId);
            store(depState);
            logger.log(INFO, "stored Deployment State");
//            String rv = "Got deployment state and updated CelarDB\n";
//            rv += "State: " + test.get("state") + "\n";
//            rv += test;
//            return rv;
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            depState.marshal(baos);
//            String s  = new String( baos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8 );
//            return s;
            depState.deployment_state.put("timestamp", depState.timestamp);
            return depState.deployment_state;

        } catch (Exception ex) {
            Logger.getLogger(DeploymentStateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
