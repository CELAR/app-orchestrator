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
package gr.ntua.cslab.orchestrator.client;

import gr.ntua.cslab.orchestrator.beans.DeploymentState;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.bind.JAXB;

/**
 *
 * @author Giannis Giannakopoulos
 */
public class DeploymentStateClient extends AbstractClient {

    public DeploymentStateClient() {
        super();
    }
    
    public DeploymentState getDeploymentState() throws IOException {
        String outcome = this.issueRequest("GET", "state/", null);
        DeploymentState state = JAXB.unmarshal(new StringReader(outcome), DeploymentState.class);
        return state;
    }
    
}
