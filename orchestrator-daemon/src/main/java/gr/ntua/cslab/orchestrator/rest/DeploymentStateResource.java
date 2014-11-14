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

import gr.ntua.cslab.orchestrator.beans.DeploymentState;
import gr.ntua.cslab.orchestrator.shared.ServerStaticComponents;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Service used to return the IP addresses of the VMs for the deployment.
 *
 * @author Giannis Giannakopoulos
 */
@Path("state/")
public class DeploymentStateResource {

    private final static String deploymentId = ServerStaticComponents.properties.getProperty("slipstream.deployment.id");
    @GET
    public DeploymentState getDeploymentState() {
        HashMap<String,String> ipAddresses = null;
        try {
            ipAddresses=ServerStaticComponents.service.getDeploymentIPs(deploymentId);
        } catch (Exception ex) {
            Logger.getLogger(DeploymentStateResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new DeploymentState(ipAddresses);
    }
}