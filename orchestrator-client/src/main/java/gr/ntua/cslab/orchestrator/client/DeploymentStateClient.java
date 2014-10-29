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
import java.util.HashMap;
import javax.xml.bind.JAXB;

/**
 *
 * @author Giannis Giannakopoulos
 */
public class DeploymentStateClient extends AbstractClient {

    public DeploymentStateClient() {
        super();
    }

    /**
     * Returns the deployment state -- a HashMap of <domain, IP> values
     *
     * @return
     * @throws IOException
     */
    public DeploymentState getDeploymentState() throws IOException {
        String outcome = this.issueRequest("GET", "state/", null);
        DeploymentState state = JAXB.unmarshal(new StringReader(outcome), DeploymentState.class);
        return state;
    }

    public static HashMap<String, String> getDiff(DeploymentState before, DeploymentState after) {
        HashMap<String, String> result = new HashMap<>();
        int difference = before.getIpAddress().size() - after.getIpAddress().size();
        if (difference > 0) {    // the resizing action reduced the cluster size
            for (String s : before.getIpAddress().keySet()) {
                if (!after.getIpAddress().containsKey(s)) {
                    result.put(s, before.getIpAddress().get(s));
                }
            }
        } else {        // the resizing action increased the cluster size
            for (String s : after.getIpAddress().keySet()) {
                if (!before.getIpAddress().containsKey(s)) {
                    result.put(s, after.getIpAddress().get(s));
                }
            }

        }
        return result;
    }

}
