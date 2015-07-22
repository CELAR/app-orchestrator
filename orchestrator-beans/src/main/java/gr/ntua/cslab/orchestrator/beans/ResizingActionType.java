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

/**
 * Enum holding the resizing action types
 * @author Giannis Giannakopoulos
 */
public enum ResizingActionType {
    SCALE_IN,       // remove nodes from cluster
    SCALE_OUT,      // add nodes to cluster
    SCALE_UP,       // increase number of cores/RAM
    SCALE_DOWN,     // decrease cores/RAM 
    ATTACH_DISK,    // create and attach disk
    DETTACH_DISK,   // detach disk from VM
    VM_RESIZE,   // detach disk from VM
    BALANCE,        // execut balance action
    GENERIC_ACTION  // just run a script
}
