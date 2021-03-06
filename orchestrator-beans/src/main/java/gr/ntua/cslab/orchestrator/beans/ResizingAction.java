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

import java.util.LinkedList;
import java.util.List;
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
    private String moduleName;
    private List<String> applicablePatameters;
    private String script;
    private boolean executesScript;

    public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public boolean isExecutesScript() {
		return executesScript;
	}

	public void setExecutesScript(boolean executesScript) {
		this.executesScript = executesScript;
	}

	public ResizingAction() {
        this.applicablePatameters = new LinkedList<>();
    }

    public ResizingAction(int id, String name, ResizingActionType type, int moduleId, String moduleName, List<String> applicablePatameters) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.applicablePatameters = applicablePatameters;
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

    public List<String> getApplicablePatameters() {
        return applicablePatameters;
    }

    public void setApplicablePatameters(List<String> applicablePatameters) {
        this.applicablePatameters = applicablePatameters;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
    
}
