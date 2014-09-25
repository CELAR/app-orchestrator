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
package gr.ntua.cslab.beans;

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
    private String uniqueId;
    private ResizingAction resizingAction;
    private Long timestamp;
    private ResizingStatusExecutionStatus executionStatus;

    public ExecutedResizingAction() {
    }
    
    public ExecutedResizingAction(String uniqueId, ResizingAction resizingAction, Long timestamp, ResizingStatusExecutionStatus status) {
        this.uniqueId = uniqueId;
        this.resizingAction = resizingAction;
        this.timestamp = timestamp;
        this.executionStatus = status;
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

    public ResizingStatusExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(ResizingStatusExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }
}
