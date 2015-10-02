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
import gr.ntua.cslab.orchestrator.beans.Parameter;
import gr.ntua.cslab.orchestrator.beans.Parameters;
import gr.ntua.cslab.orchestrator.beans.ResizingActionList;
import gr.ntua.cslab.orchestrator.client.conf.ClientConfiguration;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import javax.xml.bind.JAXB;

import com.sixsq.slipstream.statemachine.States;

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
   
    /**
     * Returns the difference in IP/hostnames as inserted by a resizing action.
     * 
     * @param uniqueId
     * @return  
     * @throws java.io.IOException  
     */
    public HashMap<String,String> getActionEffect(String uniqueId) throws IOException {
        ExecutedResizingAction action = this.getActionStatus(uniqueId);
        if(action.getAfterState()==null) {
            return null;
        }
        return DeploymentStateClient.getDiff(action.getBeforeState(), action.getAfterState());
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
		ResizingActionsClient client = new ResizingActionsClient();
		client.setConfiguration(new ClientConfiguration("83.212.118.41", 80));
		Parameters params = new Parameters();
		params.addParameter(new Parameter("multiplicity", "1"));
		ExecutedResizingAction a = client.executeResizingAction(3, params);
		
		ExecutedResizingAction n;
		while(true) {
			n = client.getActionStatus(a.getUniqueId());
			System.err.println(n.getExecutionStatus());
			if(n.getExecutionStatus()==States.Ready)
				break;
			Thread.sleep(10000);
		}
		System.err.println(n.getIpAddressesRemoved());
		
	}
}
