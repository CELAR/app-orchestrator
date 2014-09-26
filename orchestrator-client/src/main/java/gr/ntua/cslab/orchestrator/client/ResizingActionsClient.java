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

import gr.ntua.cslab.orchestrator.beans.ExecutedResizingAction;
import gr.ntua.cslab.orchestrator.beans.Parameters;
import gr.ntua.cslab.orchestrator.beans.ResizingActionList;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXB;

/**
 * Resizing Actions client. Use this client to interact with the Resizing API
 * of the CELAR Orchestrator.
 * @author Giannis Giannakopoulos
 */
public class ResizingActionsClient extends AbstractClient {

    public ResizingActionsClient() {
        super();
    }
    
    /**
     * Lists the <b>available</b> resizing actions for the current applications.
     * @return The list of the applicable resizing actions
     * @throws IOException 
     */
    public ResizingActionList listResizingActions() throws IOException {
        String output=this.issueRequest("GET", "resizing/", null);
        ResizingActionList ret = JAXB.unmarshal(new StringReader(output), ResizingActionList.class);
        return ret;
    }
    
    /**
     * Execute a new resizing action. A set of parameters can be defined in the 
     * form of key-value tuples.
     * @param actionId The action to be enforced
     * @param parameters The parameters for the execution.
     * @return The ExecutuedResizingAction object
     * @throws IOException 
     */
    public ExecutedResizingAction executeResizingAction(int actionId, Parameters parameters) throws IOException {
        parameters = (parameters==null?new Parameters():parameters);
        StringWriter writer = new StringWriter();
        JAXB.marshal(parameters, writer);
        String paramsMarshalled=writer.toString();
        String output = this.issueRequest("POST", "resizing/?action_id="+actionId, paramsMarshalled);
        ExecutedResizingAction action = JAXB.unmarshal(new StringReader(output), ExecutedResizingAction.class);
        return action;
    }
    
    /**
     * Returns the status of a previously submitted resizing action.
     * @param uniqueId The unique id of the resizing action 
     * @return The ExecutuedResizingAction object
     * @throws IOException 
     */
    public ExecutedResizingAction getActionStatus(String uniqueId) throws IOException {
        String output=this.issueRequest("GET", "resizing/status/?unique_id="+uniqueId, null);
        ExecutedResizingAction ret = JAXB.unmarshal(new StringReader(output), ExecutedResizingAction.class);
        return ret;
    }
}
