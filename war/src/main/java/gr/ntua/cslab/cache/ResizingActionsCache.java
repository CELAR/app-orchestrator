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
package gr.ntua.cslab.cache;

import gr.ntua.cslab.beans.ExecutedResizingAction;
import gr.ntua.cslab.beans.ResizingAction;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used as a cache for the available and the executed resizing actions.
 * @author Giannis Giannakopoulos
 */
public class ResizingActionsCache {
    
    private static final List<ResizingAction> availabeResizingActions = new LinkedList<>();
    private static final List<ExecutedResizingAction> executedResizingActions = new LinkedList<>();
    
    
    /**
     * Returns a list of the available resizing actions.
     * @return 
     */
    public static List<ResizingAction> getAvailalbeResizingActions(){
        return ResizingActionsCache.availabeResizingActions;
    };
    
    /**
     * Returns a list of all the executed resizing actions.
     * @return 
     */
    public static List<ExecutedResizingAction> getExecutedResizingActions(){
        return ResizingActionsCache.executedResizingActions;
    };
    
    /**
     * Adds a newly executed resizing action  
     * @param action
     */
    public static void addExecutedResizingAction(ExecutedResizingAction action){
        executedResizingActions.add(action);
    };
    
    /**
     * Adds a new available resizing action: only used by the platform internally.
     * @param action
     */
    public static void addAvailableResizingAction(ResizingAction action){
        availabeResizingActions.add(action);
    };
    
}
