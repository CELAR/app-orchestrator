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
 * Resizing action
 * @author Giannis Giannakopoulos
 */

@XmlRootElement(name = "resizingAction")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResizingAction {
    
    private int id;
    private String name;
    private ResizingActionType type;
    private int moduleId;

    public ResizingAction() {
    }

    public ResizingAction(int id, String name, ResizingActionType type, int moduleId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.moduleId = moduleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResizingActionType getType() {
        return type;
    }

    public void setType(ResizingActionType type) {
        this.type = type;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }
}
