package gr.ntua.cslab.orchestrator;

import com.sixsq.slipstream.exceptions.ValidationException;
import gr.ntua.cslab.celar.server.beans.DeploymentState;
import gr.ntua.cslab.celar.server.beans.User;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;
import static gr.ntua.cslab.database.DBConnectable.openConnection;
import java.util.Properties;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import gr.ntua.cslab.orchestrator.shared.ServerStaticComponents;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;



/**
 *
 * @author cmantas
 */
public class DBTest {
    
        static SlipStreamSSService ssService;
    
        private static void loadProperties() throws IOException {
        InputStream stream = Main.class.getClassLoader().getResourceAsStream("orchestrator.properties");
        if (stream == null) {
            System.err.println("No orchestrator.properties file was found! Exiting...");
            System.exit(1);
        }
        ServerStaticComponents.properties = new Properties();
        ServerStaticComponents.properties.load(stream);
    }
    
            
    
    public static void initSSClient() throws ValidationException{
            String 
                    username  = "celar",
                    password  = "celar2015",
                    slipstreamHost  = "https://83.212.102.166",
                    connectorName  = "okeanos";
            
             ssService  = new SlipStreamSSService(username, password, slipstreamHost, connectorName);
    }

    public static void main(String args[]) throws Exception{
        loadProperties();
        initSSClient();
        
        // Celar-DB host has the same value as celar.server.host
        ServerStaticComponents.properties.setProperty("postgresql.host", "localhost");
        
        openConnection(ServerStaticComponents.properties);
        
        User user = new User("takis", "dummy_cred");
        store(user);
        delete(user);
        
        //get and store deployment props
        String deploymentId="e7054d0e-cfc0-4372-b9d6-076f56f62501";
        Map<String,String> test  = ssService.getAllRuntimeParams(deploymentId);
        
        DeploymentState depState = new DeploymentState(test, deploymentId);
        
//        store(depState); //this will fail with 
                            //Key (deployment_id)=(e7054d0e-cfc0-4372-b9d6-076f56f62501) is not present in table "deployment
    }
    
}
