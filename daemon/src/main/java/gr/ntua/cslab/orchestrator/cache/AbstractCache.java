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
package gr.ntua.cslab.orchestrator.cache;

/**
 * This file represent the basic interface that each cache mechanism should
 * have. Basically each implementing subclass must have a mechanism to flush 
 * their data into the disk (a flat file or a database).
 * @author Giannis Giannakopoulos
 */
public interface AbstractCache {
    
    /**
     * This method serializes the cache object in the disk. This method is implemented 
     * differently by each subclass, according to the data that they hold. 
     * <br/>
     * The objective of this method is to free the RAM from the in-memory data.
     * 
     */
    public void flush();
}
