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
    private ResizingExecutionStatus executionStatus;
    private Parameters parameters;

    public ExecutedResizingAction() {
    }
    
    public ExecutedResizingAction(String uniqueId, ResizingAction resizingAction, Long timestamp, ResizingExecutionStatus status, Parameters parameters) {
        this.uniqueId = uniqueId;
        this.resizingAction = resizingAction;
        this.timestamp = timestamp;
        this.executionStatus = status;
        this.parameters = parameters;
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

    public ResizingExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(ResizingExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
}
