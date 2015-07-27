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

import com.sixsq.slipstream.exceptions.ValidationException;
import com.sixsq.slipstream.statemachine.States;

import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;
import gr.ntua.cslab.orchestrator.beans.*;
import gr.ntua.cslab.orchestrator.cache.ResizingActionsCache;
import gr.ntua.cslab.orchestrator.shared.ServerStaticComponents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resizing action API.
 * @author Giannis Giannakopoulos
 */
@Path("/resizing/")
public class ResizingActionResource {
	static Logger logger = Logger.getLogger(ResizingActionResource.class.getName());
	static SlipStreamSSService ssService = ServerStaticComponents.service;
    private final static String
            deploymentId = ServerStaticComponents.properties.getProperty("slipstream.deployment.id");

    public ResizingActionResource() throws ValidationException {
    }
    
    

    @GET
    public ResizingActionList listResizingActions(){
       return new ResizingActionList(ResizingActionsCache.getAvailalbeResizingActions());
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ExecutedResizingAction executeResizingAction(@QueryParam("action_id") int actionId, Parameters params) throws ValidationException, Exception{
        

        
        ResizingAction a = ResizingActionsCache.getResizingActionById(actionId);
        int multiplicity = 1;
        
        for(Parameter  p : params.getParameters()) {
            if(p.getKey().equals("multiplicity")) 
                multiplicity = new Integer(p.getValue());
        }

        ExecutedResizingAction exec = new ExecutedResizingAction();
        //States foo =  ServerStaticComponents.service.getDeploymentState(deploymentId);
        exec.setExecutionStatus(States.Executing);
        exec.setResizingAction(ResizingActionsCache.getResizingActionById(actionId));
        String uid = UUID.randomUUID().toString();
        exec.setUniqueId(uid);
        exec.setParameters(params);
        exec.setTimestamp(System.currentTimeMillis());
        exec.setBeforeState(new DeploymentStateOrch(deploymentId, ServerStaticComponents.service.getAllRuntimeParams(deploymentId)));
        
        String connectorName = ServerStaticComponents.properties.getProperty("slipstream.connector.name");
        
        if(a.getType() == ResizingActionType.SCALE_OUT) {
        	ssService.addVM(deploymentId, a.getModuleName(),multiplicity);
        } else if (a.getType() ==  ResizingActionType.SCALE_IN) {
            //ServerStaticComponents.service.removeVM(deploymentId, a.getModuleName(), multiplicity);
            List<String> ids = ssService.removeVMIDs(deploymentId, a.getModuleName(), multiplicity);
            logger.info("Remove IDs: "+ids);
            List<String> ips = ssService.translateIPs(deploymentId, ids);
            logger.info("Remove IPs: "+ips);
            for(String ip : ips){
    			logger.info("Executing script on "+ip);
    			if(connectorName.contains("okeanos")){
    				String scriptFile = ssService.writeToFile("cat /tmp/script.sh\n"+a.getScript());
	    			String[] command = new String[] {"scp", "-o", "StrictHostKeyChecking=no", scriptFile, "root@"+ip+":/tmp/"}; 
	    			ssService.executeCommand(command);
	    			command = new String[] {"ssh", "-o", "StrictHostKeyChecking=no", "root@"+ip, "/bin/bash -s < /tmp/script.sh &> /tmp/resize_actions.log"};    			
	    			ssService.executeCommand(command);
    			}
    			else{
	    			String scriptFile = ssService.writeToFile("sudo su -\n cat /tmp/script.sh\n"+a.getScript());
	    			String[] command = new String[] {"scp", "-o", "StrictHostKeyChecking=no", scriptFile, "ubuntu@"+ip+":/tmp/"}; 
	    			ssService.executeCommand(command);
	    			command = new String[] {"ssh", "-o", "StrictHostKeyChecking=no", "ubuntu@"+ip, "/bin/bash -s < /tmp/script.sh &> /tmp/resize_actions.log"};    			
	    			ssService.executeCommand(command);
    			}
            }
            ssService.removeVMswithIDs(deploymentId, ids, a.getModuleName());
        } else if(a.isExecutesScript()){
        	HashMap<String, String> ips = ssService.getDeploymentIPs(deploymentId);
        	for(Entry<String, String> ip : ips.entrySet()){
        		if(ip.getKey().contains(a.getModuleName())){
        			exec.addIP(ip.getValue());
        			logger.info("Executing script on "+ip.getValue());
        			if(connectorName.contains("okeanos")){
        				String scriptFile = ssService.writeToFile("cat /tmp/script.sh\necho Executing > /tmp/"+uid+"_state\n"+a.getScript()+"\necho Ready > /tmp/"+uid+"_state\n");
	        			String[] command = new String[] {"scp", "-o", "StrictHostKeyChecking=no", scriptFile, "root@"+ip.getValue()+":/tmp/"}; 
	        			ssService.executeCommand(command);
	        			command = new String[] {"ssh", "-o", "StrictHostKeyChecking=no", "root@"+ip.getValue(), "/bin/bash -s < /tmp/script.sh &> /tmp/resize_actions.log &"};    			
	        			ssService.executeCommand(command);
        			}
        			else{
	        			String scriptFile = ssService.writeToFile("sudo su -\ncat /tmp/script.sh\necho Executing > /tmp/"+uid+"_state\n"+a.getScript()+"\necho Ready > /tmp/"+uid+"_state\n");
	        			String[] command = new String[] {"scp", "-o", "StrictHostKeyChecking=no", scriptFile, "ubuntu@"+ip.getValue()+":/tmp/"}; 
	        			ssService.executeCommand(command);
	        			command = new String[] {"ssh", "-o", "StrictHostKeyChecking=no", "ubuntu@"+ip.getValue(), "/bin/bash -s < /tmp/script.sh &> /tmp/resize_actions.log &"};    			
	        			ssService.executeCommand(command);
        			}
        		}
        	}
        } 
        else if (a.getType() ==  ResizingActionType.ATTACH_DISK) {
        	int diskSize=0;
        	String vmId="1";
        	for(Parameter  p : params.getParameters()) {
                if(p.getKey().equals("disk_size")) 
                	diskSize = new Integer(p.getValue());
                if(p.getKey().equals("vm_id")) 
                	vmId = p.getValue();
            }
        	ssService.attachDisk(deploymentId, a.getModuleName(), vmId+"", diskSize);
        } 
        else if (a.getType() ==  ResizingActionType.DETTACH_DISK) {
        	String vmId="", diskID="";
        	for(Parameter  p : params.getParameters()) {
                if(p.getKey().equals("vm_id")) 
                	vmId = p.getValue();
                if(p.getKey().equals("disk_id")) 
                	diskID = p.getValue();
            }
        	ssService.detachDisk(deploymentId, a.getModuleName(), vmId, diskID);
        }
        else if (a.getType() ==  ResizingActionType.VM_RESIZE) {
        	String vmId="", flavor="", cpu="", ram ="";
        	for(Parameter  p : params.getParameters()) {
                if(p.getKey().equals("vm_id")) 
                	vmId = p.getValue();
                if(p.getKey().equals("flavor_id")) 
                	flavor = p.getValue();
                if(p.getKey().equals("CPU")) 
                	cpu = p.getValue();
                if(p.getKey().equals("RAM")) 
                	ram = p.getValue();
            }
//        	if(ssService.getConnectorName().equals("okeanos")){
        		ssService.scaleVM(deploymentId, a.getModuleName(), vmId, flavor);
//        	}
//        	else{
//
//        		ssService.scaleVM(deploymentId, a.getModuleName(), vmId, Integer.parseInt(cpu), Integer.parseInt(ram));
//        	}
        }
        
		ResizingActionsCache.addExecutedResizingAction(exec); 
        return exec;
    }
    
    @GET
    @Path("status/")
    public ExecutedResizingAction getResizingActionStatus(@QueryParam("unique_id") String actionId) throws Exception{
        ExecutedResizingAction a = ResizingActionsCache.getExecutedResizingActionByUniqueId(actionId);
        if(a==null)
            return null;
        States currentStatus;
        String connectorName = ServerStaticComponents.properties.getProperty("slipstream.connector.name");
        if(a.getResizingAction().getType()== ResizingActionType.GENERIC_ACTION || a.getResizingAction().getType()== ResizingActionType.BALANCE){
        	logger.info("Getting status for script action");
        	boolean ready = true;
        	for(String ip : a.getIPs()){
    			if(connectorName.contains("okeanos")){
    				String[] command = new String[] {"ssh", "-o", "StrictHostKeyChecking=no", "root@"+ip, "cat /tmp/"+a.getUniqueId()+"_state"};    			
	        		String out = ssService.executeCommand(command).get("output");
	        		if(!out.contains("Ready"))
	        			ready=false;
    			}
    			else{
	        		String[] command = new String[] {"ssh", "-o", "StrictHostKeyChecking=no", "ubuntu@"+ip, "cat /tmp/"+a.getUniqueId()+"_state"};    			
	        		String out = ssService.executeCommand(command).get("output");
	        		if(!out.contains("Ready"))
	        			ready=false;
    			}
        	}
        	if(ready)
        		currentStatus = States.Ready;
        	else
        		currentStatus = States.Executing;
        }
        else{
        	currentStatus = ServerStaticComponents.service.getDeploymentState(deploymentId);
        }
        if( a.getAfterState() == null && currentStatus==States.Ready ) {
            a.setAfterState(new DeploymentStateOrch(deploymentId, ServerStaticComponents.service.getAllRuntimeParams(deploymentId)));
            if(statesIdentical(a.getBeforeState(), a.getAfterState())) {
                a.setAfterState(null);
            }
        }
        a.setExecutionStatus(currentStatus);
        
        if(a!=null)
            return a;
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    
    
        
    public static boolean statesIdentical(DeploymentStateOrch before, DeploymentStateOrch after) {
        if(before.getIPs().size()!= before.getIPs().size())
            return false;
        for(Map.Entry<String, String> befEntry : before.getIPs().entrySet()) {
            if(!after.getIPs().containsKey(befEntry.getKey()))
                return false;
            if(!befEntry.getValue().equals(after.getIPs().get(befEntry.getKey())))
                return false;
        }
        return false;
    }
}