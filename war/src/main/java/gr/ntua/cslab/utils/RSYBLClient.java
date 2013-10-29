/**
 *    Copyright 2013 Giannis Giannakopoulos
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package gr.ntua.cslab.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class RSYBLClient {

	protected static String REST_API_URL="http://localhost/rSYBL-analysis-engine-0.1-SNAPSHOT/restWS";
	public static String APP_DEPLOYMENT_URL="setApplicationDeploymentDescriptionCELAR";
	public static String APP_DESCRIPTION_URL="setApplicationDescriptionCELAR";

	public static void sendXML(String urlToSend, String content) throws IOException{
		URL url;
		HttpURLConnection connection;
		
        url = new URL(REST_API_URL + "/"+ urlToSend);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.setRequestProperty("Accept", "application/json");
        
        OutputStream os = connection.getOutputStream();
        os.write(content.getBytes(Charset.forName("UTF-8")));
        os.flush();
        os.close();
        System.out.println(REST_API_URL + "/"+ urlToSend);
        System.out.println(content);
        connection.disconnect();
        
        InputStream errorStream = connection.getErrorStream();
        if(errorStream!=null){
        	BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
        	while(reader.ready()){
        		System.err.println(reader.readLine());
        	}
        } else {
        	System.out.println("No errors running the command..");
        }
        
        InputStream inputStream = connection.getInputStream();
        if(errorStream!=null){
        	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        	while(reader.ready()){
        		System.err.println(reader.readLine());
        	}
        } else {
        	System.out.println("No errors running the command..");
        }
	}
	
	
}
