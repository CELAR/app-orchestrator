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
package gr.ntua.cslab.orchestrator.client.cli.formatter;

import gr.ntua.cslab.orchestrator.beans.ExecutedResizingAction;
import gr.ntua.cslab.orchestrator.beans.ExecutedResizingActionList;
import gr.ntua.cslab.orchestrator.beans.ResizingAction;
import gr.ntua.cslab.orchestrator.beans.ResizingActionList;

/**
 * Class used to print the Java beans into the CLI in a pretty manner. The provided
 * methods are static.
 * @author Giannis Giannakopoulos
 */
public class CLIPrettyFormatter {
    
    public static String format(ResizingActionList list) {
        StringBuilder builder = new StringBuilder();
        if(list == null  || list.getResizingActions() == null)
            return null;
        for(ResizingAction action :  list.getResizingActions()) {
            builder.append("ID:\t\t").append(action.getId());
            builder.append("\n");
            builder.append("Name:\t\t").append(action.getName());
            builder.append("\n");
            builder.append("Type:\t\t").append(action.getType());
            builder.append("\n");
            builder.append("Module:\t\t").
                    append(action.getModuleName()).
                    append(" [").
                    append(action.getModuleId()).
                    append("]");
            builder.append("\n");
            builder.append("Parameters:\t").append(action.getApplicablePatameters());
            builder.append("\n\n");
        } 
        return builder.toString();
    }
    
    public static String format(ExecutedResizingActionList list) {
        StringBuilder builder = new StringBuilder();
        if( list == null || list.getExecutedResizingActions() == null)
            return null;
        
        for(ExecutedResizingAction action : list.getExecutedResizingActions()) {
            builder.append("UUID:\t\t").append(action.getUniqueId());
            builder.append("\n");
            builder.append("Action:\t\t").append(action.getResizingAction().getId());
            builder.append("\n");
            builder.append("Status:\t\t").append(action.getExecutionStatus());
            builder.append("\n\n");
        }
        return builder.toString();
    }
    
}
