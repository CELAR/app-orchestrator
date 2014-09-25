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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Giannis Giannakopoulos
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResizingActionList {
    
    private List<ResizingAction> resizingActions;

    public ResizingActionList() {
    }

    public ResizingActionList(List<ResizingAction> resizingActions) {
        this.resizingActions = resizingActions;
    }

    public List<ResizingAction> getResizingActions() {
        return resizingActions;
    }

    public void setResizingActions(List<ResizingAction> resizingActions) {
        this.resizingActions = resizingActions;
    }
    
}
