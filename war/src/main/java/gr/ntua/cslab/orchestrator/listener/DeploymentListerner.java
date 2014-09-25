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
package gr.ntua.cslab.orchestrator.listener;

import gr.ntua.cslab.orchestrator.beans.ResizingAction;
import gr.ntua.cslab.orchestrator.beans.ResizingActionType;
import gr.ntua.cslab.orchestrator.cache.ResizingActionsCache;
import java.util.LinkedList;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This Deployment listener is responsible for the internal configuration of the
 * CELAR Orchestrator. The initialization method is will create the available resizing
 * actions and after that other modules may interact with the orchestrator.
 * @author Giannis Giannakopoulos
 */
@WebListener
public class DeploymentListerner implements ServletContextListener{

    /**
     * Initialization method. This method is used to set the available resizing
     * actions during the boot time of the orchestrator.
     * @param sce 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LinkedList<String> params = new LinkedList<>();
        params.add("multiplicity");
        params.add("something else");
        ResizingActionsCache.allocate();
        ResizingActionsCache.addAvailableResizingAction(new ResizingAction(1, "add cassandra node", ResizingActionType.SCALE_OUT, 1, params));
        ResizingActionsCache.addAvailableResizingAction(new ResizingAction(2, "remove cassandra node", ResizingActionType.SCALE_IN, 1, params));
        ResizingActionsCache.addAvailableResizingAction(new ResizingAction(3, "increase RAM to web server", ResizingActionType.SCALE_UP, 2, new LinkedList()));
        ResizingActionsCache.addAvailableResizingAction(new ResizingAction(3, "decrease RAM from web server", ResizingActionType.SCALE_DOWN, 2, new LinkedList()));
        System.out.println("Available resizing actions:\t"+ResizingActionsCache.getAvailalbeResizingActions());
    }

    /**
     * Destruction method.
     * @param sce 
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("paparia mantoles alla auti ti fora katastrefomai");
    }
    
}
