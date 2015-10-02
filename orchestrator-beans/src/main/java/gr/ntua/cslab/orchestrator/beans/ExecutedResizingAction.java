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
package gr.ntua.cslab.orchestrator.beans;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sixsq.slipstream.statemachine.States;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Giannis Giannakopoulos
 */
@XmlRootElement(name = "resizingAction")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecutedResizingAction {
    
    private ResizingAction resizingAction;
    private String uniqueId;
    private Long timestamp;
    private States executionStatus;
    private Parameters parameters;
    private DeploymentStateOrch beforeState;
    private DeploymentStateOrch afterState = null;
    private List<String> ips;
    
    // ggian
    private String diskIdAttached;
    private String diskIdDetached;
    private List<String> ipAddressesAdded;
    private List<String> ipAddressesRemoved;

    public ExecutedResizingAction() {
    }
    
    public ExecutedResizingAction(String uniqueId, ResizingAction resizingAction, Long timestamp, States status, Parameters parameters) {
        this.uniqueId = uniqueId;
        this.resizingAction = resizingAction;
        this.timestamp = timestamp;
        this.executionStatus = status;
        this.parameters = parameters;
        this.ips = new ArrayList<String>();
        
        this.ipAddressesAdded = new LinkedList<>();
        this.ipAddressesRemoved = new LinkedList<>();
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public ResizingAction getResizingAction() {
        return resizingAction;
    }

    public void setResizingAction(ResizingAction resizingAction) {
        this.resizingAction = resizingAction;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public States getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(States executionStatus) {
        this.executionStatus = executionStatus;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public DeploymentStateOrch getBeforeState() {
        return beforeState;
    }

    public void setBeforeState(DeploymentStateOrch beforeState) {
        this.beforeState = beforeState;
    }

    public DeploymentStateOrch getAfterState() {
        return afterState;
    }

    public void setAfterState(DeploymentStateOrch afterState) {
        this.afterState = afterState;
    }

	public void addIP(String ip) {
		if(ips==null)
			ips = new ArrayList<String>();
		ips.add(ip);
	}
    
	public List<String> getIPs(){
		return ips;
	}

	public String getDiskIdAttached() {
		return diskIdAttached;
	}

	public void setDiskIdAttached(String diskIdAttached) {
		this.diskIdAttached = diskIdAttached;
	}

	public String getDiskIdDetached() {
		return diskIdDetached;
	}

	public void setDiskIdDetached(String diskIdDetached) {
		this.diskIdDetached = diskIdDetached;
	}

	public List<String> getIpAddressesAdded() {
		return ipAddressesAdded;
	}

	public void setIpAddressesAdded(List<String> ipAddressedAdded) {
		this.ipAddressesAdded = ipAddressedAdded;
	}

	public List<String> getIpAddressesRemoved() {
		return ipAddressesRemoved;
	}

	public void setIpAddressesRemoved(List<String> ipAddressedRemoved) {
		this.ipAddressesRemoved = ipAddressedRemoved;
	}
}
