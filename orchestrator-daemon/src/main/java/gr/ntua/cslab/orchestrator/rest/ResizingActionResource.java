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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;

/**
 * Resizing action API.
 * 
 * @author Giannis Giannakopoulos
 */
@Path("/resizing/")
public class ResizingActionResource {
	static Logger logger = Logger.getLogger(ResizingActionResource.class.getName());
	static SlipStreamSSService ssService = ServerStaticComponents.service;
	private final static String deploymentId = ServerStaticComponents.properties
			.getProperty("slipstream.deployment.id");

	public ResizingActionResource() throws ValidationException {
	}

	@GET
	public ResizingActionList listResizingActions() {
		return new ResizingActionList(ResizingActionsCache.getAvailalbeResizingActions());
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ExecutedResizingAction executeResizingAction(@QueryParam("action_id") int actionId, Parameters params)
			throws ValidationException, Exception {

		ResizingAction a = ResizingActionsCache.getResizingActionById(actionId);
		int multiplicity = 1;

		for (Parameter p : params.getParameters()) {
			if (p.getKey().equals("multiplicity"))
				multiplicity = new Integer(p.getValue());
		}

		ExecutedResizingAction exec = new ExecutedResizingAction();
		// States foo =
		// ServerStaticComponents.service.getDeploymentState(deploymentId);
		exec.setExecutionStatus(States.Executing);
		exec.setResizingAction(ResizingActionsCache.getResizingActionById(actionId));
		String uid = UUID.randomUUID().toString();
		exec.setUniqueId(uid);
		exec.setParameters(params);
		exec.setTimestamp(System.currentTimeMillis());
		exec.setBeforeState(new DeploymentStateOrch(deploymentId,
				ServerStaticComponents.service.getAllRuntimeParams(deploymentId)));

		String connectorName = ServerStaticComponents.properties.getProperty("slipstream.connector.name");

		if (a.getType() == ResizingActionType.SCALE_OUT || a.getType() == ResizingActionType.SCALE_DIAGONALLY_UP) {
			Integer cores=-1, ram=-1, disk=-1;
			for(Parameter p : params.getParameters()) {
				if(p.getKey().equals("cores"))
					cores = new Integer(p.getValue());
				else if(p.getKey().equals("ram"))
					ram = new Integer(p.getValue());
				else if(p.getKey().equals("disk"))
					disk = new Integer(p.getValue());
			}
			if(cores!=-1 && ram != -1 && disk!=-1) {
				ssService.addVM(deploymentId, a.getModuleName(), multiplicity, cores, ram, disk);
			} else {
				ssService.addVM(deploymentId, a.getModuleName(), multiplicity);
			}
		} else if (a.getType() == ResizingActionType.SCALE_IN || a.getType() == ResizingActionType.SCALE_DIAGONALLY_DOWN) {
			// ServerStaticComponents.service.removeVM(deploymentId,
			// a.getModuleName(), multiplicity);
			List<String> ids = ssService.removeVMIDs(deploymentId, a.getModuleName(), multiplicity);
			logger.info("Remove IDs: " + ids);
			List<String> ips = ssService.translateIPs(deploymentId, ids);
			logger.info("Remove IPs: " + ips);
			for (String ip : ips) {
				logger.info("Executing script on " + ip);
				if (connectorName.contains("okeanos")) {
					String scriptFile = ssService.writeToFile("cat /tmp/script.sh\n" + a.getScript());
					String[] command = new String[] { "scp", "-o", "StrictHostKeyChecking=no", scriptFile,
							"root@" + ip + ":/tmp/" };
					ssService.executeCommand(command);
					command = new String[] { "ssh", "-o", "StrictHostKeyChecking=no", "root@" + ip,
							"/bin/bash -s < /tmp/script.sh &> /tmp/resize_actions.log" };
					ssService.executeCommand(command);
				} else {
					String scriptFile = ssService.writeToFile("sudo su -\n cat /tmp/script.sh\n" + a.getScript());
					String[] command = new String[] { "scp", "-o", "StrictHostKeyChecking=no", scriptFile,
							"ubuntu@" + ip + ":/tmp/" };
					ssService.executeCommand(command);
					command = new String[] { "ssh", "-o", "StrictHostKeyChecking=no", "ubuntu@" + ip,
							"/bin/bash -s < /tmp/script.sh &> /tmp/resize_actions.log" };
					ssService.executeCommand(command);
				}
			}
			ssService.removeVMswithIDs(deploymentId, ids, a.getModuleName());
		} else if (a.isExecutesScript()) {
			HashMap<String, String> ips = ssService.getDeploymentIPs(deploymentId);
			for (Entry<String, String> ip : ips.entrySet()) {
				if (ip.getKey().contains(a.getModuleName())) {
					exec.addIP(ip.getValue());
					logger.info("Executing script on " + ip.getValue());
					if (connectorName.contains("okeanos")) {
						String scriptFile = ssService.writeToFile("cat /tmp/script.sh\necho Executing > /tmp/" + uid
								+ "_state\n" + a.getScript() + "\necho Ready > /tmp/" + uid + "_state\n");
						String[] command = new String[] { "scp", "-o", "StrictHostKeyChecking=no", scriptFile,
								"root@" + ip.getValue() + ":/tmp/" };
						ssService.executeCommand(command);
						command = new String[] { "ssh", "-o", "StrictHostKeyChecking=no", "root@" + ip.getValue(),
								"/bin/bash -s < /tmp/script.sh &> /tmp/resize_actions.log &" };
						ssService.executeCommand(command);
					} else {
						String scriptFile = ssService
								.writeToFile("sudo su -\ncat /tmp/script.sh\necho Executing > /tmp/" + uid + "_state\n"
										+ a.getScript() + "\necho Ready > /tmp/" + uid + "_state\n");
						String[] command = new String[] { "scp", "-o", "StrictHostKeyChecking=no", scriptFile,
								"ubuntu@" + ip.getValue() + ":/tmp/" };
						ssService.executeCommand(command);
						command = new String[] { "ssh", "-o", "StrictHostKeyChecking=no", "ubuntu@" + ip.getValue(),
								"/bin/bash -s < /tmp/script.sh &> /tmp/resize_actions.log &" };
						ssService.executeCommand(command);
					}
				}
			}
		} else if (a.getType() == ResizingActionType.ATTACH_DISK) {
			int diskSize = 0;
			String vmIP = "1";
			for (Parameter p : params.getParameters()) {
				if (p.getKey().equals("disk_size"))
					diskSize = new Integer(p.getValue());
				if (p.getKey().equals("vm_ip")) {
					vmIP = p.getValue();
				}
			}			
			ssService.attachDisk(deploymentId, a.getModuleName(), translateIPtoVMid(vmIP, exec) + "", diskSize);
			
		} else if (a.getType() == ResizingActionType.DETTACH_DISK) {
			String vmIP = "", diskID = "";
			for (Parameter p : params.getParameters()) {
				if (p.getKey().equals("vm_ip"))
					vmIP = p.getValue();
				if (p.getKey().equals("disk_id"))
					diskID = p.getValue();
			}
			ssService.detachDisk(deploymentId, a.getModuleName(), translateIPtoVMid(vmIP, exec), diskID);
		} else if (a.getType() == ResizingActionType.SCALE_VERTICALLY_DOWN || a.getType()==ResizingActionType.SCALE_VERTICALLY_UP) {
			String vmIP = "";
			Integer cpu = null, ram = null;
			for (Parameter p : params.getParameters()) {
				if (p.getKey().equals("vm_ip"))
					vmIP = p.getValue();
//				if (p.getKey().equals("flavor_id"))
//					flavor = p.getValue();
				if (p.getKey().equals("cores"))
					cpu = new Integer(p.getValue());
				if (p.getKey().equals("ram"))
					ram = new Integer(p.getValue());
			}
//			ssService.scaleVM(deploymentId, a.getModuleName(), translateIPtoVMid(vmIP, exec), flavor);
			ssService.scaleVM(deploymentId, a.getModuleName(), translateIPtoVMid(vmIP, exec), cpu, ram);
		}

		ResizingActionsCache.addExecutedResizingAction(exec);
		return exec;
	}

	@GET
	@Path("status/")
	public ExecutedResizingAction getResizingActionStatus(@QueryParam("unique_id") String actionId) throws Exception {
		ExecutedResizingAction a = ResizingActionsCache.getExecutedResizingActionByUniqueId(actionId);
		if (a == null)
			return null;
		// if it's already Ready, no need to search other stuff
		if (a.getExecutionStatus() == States.Ready)
			return a;
		States currentStatus;
		String connectorName = ServerStaticComponents.properties.getProperty("slipstream.connector.name");
		if (a.getResizingAction().getType() == ResizingActionType.GENERIC_ACTION
				|| a.getResizingAction().getType() == ResizingActionType.BALANCE) {
			logger.info("Getting status for script action");
			boolean ready = true;
			for (String ip : a.getIPs()) {
				if (connectorName.contains("okeanos")) {
					String[] command = new String[] { "ssh", "-o", "StrictHostKeyChecking=no", "root@" + ip,
							"cat /tmp/" + a.getUniqueId() + "_state" };
					String out = ssService.executeCommand(command).get("output");
					if (!out.contains("Ready"))
						ready = false;
				} else {
					String[] command = new String[] { "ssh", "-o", "StrictHostKeyChecking=no", "ubuntu@" + ip,
							"cat /tmp/" + a.getUniqueId() + "_state" };
					String out = ssService.executeCommand(command).get("output");
					if (!out.contains("Ready"))
						ready = false;
				}
			}
			if (ready)
				currentStatus = States.Ready;
			else
				currentStatus = States.Executing;
		} else {
			currentStatus = ServerStaticComponents.service.getDeploymentState(deploymentId);
		}

		if (a.getAfterState() == null && currentStatus == States.Ready) {
			a.setAfterState(new DeploymentStateOrch(deploymentId,
					ServerStaticComponents.service.getAllRuntimeParams(deploymentId)));
			// logger.info(diffTwoStates(a.getBeforeState(),
			// a.getAfterState()).toString());
			DeploymentStateDiff diff = diffTwoStates(a.getBeforeState(), a.getAfterState());
			List<String> vmIPs;
			switch (a.getResizingAction().getType()) {
			case ATTACH_DISK:
				a.setDiskIdAttached(diff.getDiskID());
				break;
			case DETTACH_DISK:
				a.setDiskIdDetached(diff.getDiskID());
				break;
			case SCALE_OUT:
				vmIPs = new LinkedList<>();
				for(String s: diff.getVMIDs()) 
					vmIPs.add(a.getAfterState().getIPs().get(s+":hostname"));
				a.setIpAddressesAdded(vmIPs);
				break;
			case SCALE_IN:
				vmIPs = new LinkedList<>();
				for(String s: diff.getVMIDs()) 
					vmIPs.add(a.getBeforeState().getIPs(true).get(s+":hostname"));
				a.setIpAddressesRemoved(vmIPs);
				break;
			default:
				break;
			}

			if (statesIdentical(a.getBeforeState(), a.getAfterState())) {
				a.setAfterState(null);
			}
		}
		a.setExecutionStatus(currentStatus);

		return a;
	}

	public static boolean statesIdentical(DeploymentStateOrch before, DeploymentStateOrch after) {
		if (before.getIPs().size() != before.getIPs().size())
			return false;
		for (Map.Entry<String, String> befEntry : before.getIPs().entrySet()) {
			if (!after.getIPs().containsKey(befEntry.getKey()))
				return false;
			if (!befEntry.getValue().equals(after.getIPs().get(befEntry.getKey())))
				return false;
		}
		return false;
	}

	public static DeploymentStateDiff diffTwoStates(DeploymentStateOrch before, DeploymentStateOrch after) {
		DeploymentStateDiff diffList = new DeploymentStateDiff();
		for (String s : before.getProperties().keySet()) {
			if (!after.getProperties().containsKey(s))
				diffList.getEntries().add(new DeploymentStateDiffEntry(s, before.getProperties().get(s), ""));
		}
		for (String s : after.getProperties().keySet()) {
			if (!before.getProperties().containsKey(s))
				diffList.getEntries().add(new DeploymentStateDiffEntry(s, "", after.getProperties().get(s)));
		}

		List<String> keysWithDifferentValues = new LinkedList<>();
		for (Entry<String, String> kv : before.getProperties().entrySet()) {
			String key = kv.getKey();
			if (!before.getProperties().get(key).equals(after.getProperties().get(key))) {
				keysWithDifferentValues.add(key);
			}
		}
		for (String key : keysWithDifferentValues)
			diffList.getEntries().add(
					new DeploymentStateDiffEntry(key, before.getProperties().get(key), after.getProperties().get(key)));
		logger.info("Disk id:" + diffList.getDiskID() + ", VM added/removed" + diffList.getVMIDs());
		return diffList;
	}
	
	private String translateIPtoVMid(String ip, ExecutedResizingAction exec) {
//		logger.info(exec.getBeforeState().getIPs().toString());
		String ident = "";
		for(Entry<String, String> kv : exec.getBeforeState().getIPs().entrySet()) {
			if(kv.getValue().equals(ip)) {
				ident = kv.getKey();
			}
		}
		String vmId=ident.split(":")[0].split("\\.")[1];
		return vmId;

	}
	
}