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
package gr.ntua.cslab.orchestrator.client;

import gr.ntua.cslab.orchestrator.client.conf.ClientConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Abstract client class used as a base class for each client.
 * @author Giannis Giannakopoulos
 */
public class AbstractClient {
    
    private ClientConfiguration configuration;

    /**
     * Default constructor
     */
    public AbstractClient() {
    }
    
    /**
     * Returns the configuration object
     * @return 
     */
    public ClientConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the configuration object.
     * @param configuration 
     */
    public void setConfiguration(ClientConfiguration configuration) {
        this.configuration = configuration;
    }
    
    /**
     * Issues a new Request and returns a string with the response - if  any.
     * @param requestType
     * @param document
     * @param input
     * @return
     * @throws MalformedURLException
     * @throws IOException 
     */
    protected String issueRequest(String requestType, String document, String input) throws MalformedURLException, IOException {
        String urlString = "http://"+configuration.getHost()+":"+configuration.getPort()+"/celar-orchestrator-war/"+document;
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        con.setRequestMethod(requestType);
        con.setRequestProperty("accept", "application/xml");
        con.setRequestProperty("Content-type", "application/xml");
        
        switch (requestType) {
            case "GET":
                con.setDoInput(true);
                break;
            case "POST":
                con.setDoInput(true);
                con.setDoOutput(true);
                break;
        }
        
        if(input!=null) {
            OutputStream out = con.getOutputStream();
            out.write(input.getBytes());
        }
        
        
        int responseCode = con.getResponseCode();
//        System.out.println("Response code:\t"+responseCode);
        StringBuilder builder = new StringBuilder();
        
        try (InputStream in = con.getInputStream()) {
            byte[] buffer = new byte[1024];
            while(in.available()>0) {
                int read = in.read(buffer);
                builder.append(new String(buffer,0,read));
            }
        }
        return builder.toString();
        
    }
}
