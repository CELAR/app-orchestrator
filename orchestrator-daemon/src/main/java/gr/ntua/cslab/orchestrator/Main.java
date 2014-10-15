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
package gr.ntua.cslab.orchestrator;

import at.ac.tuwien.dsg.csdg.inputProcessing.multiLevelModel.deploymentDescription.AssociatedVM;
import at.ac.tuwien.dsg.csdg.inputProcessing.multiLevelModel.deploymentDescription.DeploymentDescription;
import at.ac.tuwien.dsg.csdg.inputProcessing.multiLevelModel.deploymentDescription.DeploymentUnit;
import at.ac.tuwien.dsg.csdg.inputProcessing.multiLevelModel.deploymentDescription.ElasticityCapability;
import at.ac.tuwien.dsg.rSybl.client.SYBLControlClient;
import com.sixsq.slipstream.exceptions.ValidationException;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import gr.ntua.cslab.celar.server.beans.SlipStreamCredentials;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;
import gr.ntua.cslab.orchestrator.beans.ResizingAction;
import gr.ntua.cslab.orchestrator.beans.ResizingActionType;
import gr.ntua.cslab.orchestrator.cache.ResizingActionsCache;
import gr.ntua.cslab.orchestrator.shared.ServerStaticComponents;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXB;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import tools.CSARParser;

/**
 * Executor class, used as an endpoint to the jar package from the outside
 * world.
 *
 * @author Giannis Giannakopoulos
 */
public class Main {

    private static void loadProperties() throws IOException {
        InputStream stream = Main.class.getClassLoader().getResourceAsStream("orchestrator.properties");
        if (stream == null) {
            System.err.println("No orchestrator.properties file was found! Exiting...");
            System.exit(1);
        }
        ServerStaticComponents.properties = new Properties();
        ServerStaticComponents.properties.load(stream);
    }

    private static void configureServer() throws Exception {
        ServerStaticComponents.server = new Server();
        int plainPort = -1, sslPort = -1;
        String keystorePath = null, keystorePassword = null;
        ServerConnector connector = null, sslConnector = null;
        
        if (ServerStaticComponents.properties.getProperty("server.plain.port") != null) {
            plainPort = new Integer(ServerStaticComponents.properties.getProperty("server.plain.port"));
        }
        if (ServerStaticComponents.properties.getProperty("server.ssl.port") != null) {
            sslPort = new Integer(ServerStaticComponents.properties.getProperty("server.ssl.port"));
            keystorePath = ServerStaticComponents.properties.getProperty("server.ssl.keystore.path");
            keystorePassword = ServerStaticComponents.properties.getProperty("server.ssl.keystore.password");
        }

        if (plainPort != -1) {
            connector = new ServerConnector(ServerStaticComponents.server);
            connector.setPort(plainPort);
        }

        if (sslPort != -1) {
            HttpConfiguration https = new HttpConfiguration();
            https.addCustomizer(new SecureRequestCustomizer());
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setKeyStorePath(keystorePath);
            sslContextFactory.setKeyStorePassword(keystorePassword);
            sslContextFactory.setKeyManagerPassword(keystorePassword);

            sslConnector = new ServerConnector(
                    ServerStaticComponents.server,
                    new SslConnectionFactory(sslContextFactory, "http/1.1"),
                    new HttpConnectionFactory(https));
            sslConnector.setPort(sslPort);

        }
        if(sslConnector!=null && connector!=null) {
            ServerStaticComponents.server.setConnectors(new Connector[]{connector, sslConnector});
        } else if(connector!=null) {
            ServerStaticComponents.server.setConnectors(new Connector[]{connector});
        } else if(sslConnector!=null) {
            ServerStaticComponents.server.setConnectors(new Connector[]{sslConnector});
        } else {
            System.err.println("Please choose one of the plain and SSL connections!");
            System.exit(1);
        }

        ServletHolder holder = new ServletHolder(ServletContainer.class);
        holder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        holder.setInitParameter("com.sun.jersey.config.property.packages",
                "gr.ntua.cslab.orchestrator.rest;"
                + "org.codehaus.jackson.jaxrs");//Set the package where the services reside
        holder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        holder.setInitParameter("com.sun.jersey.config.feature.Formatted", "true");
//        
        holder.setInitOrder(1);
//
//        ServerStaticComponents.server = new Server();
        ServletContextHandler context = new ServletContextHandler(ServerStaticComponents.server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(holder, "/*");
        Logger.getLogger(Main.class.getName()).info("Server configured");
        
        ResizingActionsCache.allocate();
    }

    private static void configureLogger() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        InputStream logPropertiesStream = Main.class.getClassLoader().getResourceAsStream("log4j.properties");
        PropertyConfigurator.configure(logPropertiesStream);
        Logger.getLogger(Main.class.getName()).info("Logger configured");
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Logger.getLogger(Main.class.getName()).info("Server is shutting down");
                    ServerStaticComponents.server.stop();
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));

    }

    private static void creatDirs() {
        File csarDir = new File("/tmp/csar/");
        
        if(!csarDir.exists()) {
            csarDir.mkdir();
        }
    }
    
    // fetch tosca from the CELAR Server
    private static void fetchTosca(String outputPath) throws MalformedURLException, IOException {
        String celarServerHost = ServerStaticComponents.properties.getProperty("celar.server.host");
        String celarServerPort = ServerStaticComponents.properties.getProperty("celar.server.port");
        String deploymentId = ServerStaticComponents.properties.getProperty("slipstream.deployment.id");
        
        URL url = new URL("http://"+celarServerHost+":"+
                celarServerPort+"/deployment/"+
                deploymentId+"/tosca/");
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-type", "application/octet-stream");
        InputStream in = con.getInputStream();
        
        FileOutputStream file = new FileOutputStream(outputPath);
        byte[] buffer = new  byte[1024];
        int count;
        while((count = in.read(buffer))!=-1){
            file.write(buffer, 0, count);
        }
        file.flush();
        file.close();
        in.close();
        
        Logger.getLogger(Main.class.getName()).info("CSAR Archive successfully received");
    }
    
    // sets the resizing actions, according to the userProvided tosca
    private static void setResizingActions(String toscaFilePath) throws Exception {
        CSARParser parser = new CSARParser(toscaFilePath);
        List<String> modules = parser.getModules();
        int i=1;
        List<String> params = new ArrayList<>();
        params.add("multiplicity");
        for (String s : modules) {
            ResizingAction action = new ResizingAction();
            action.setId(i++);
            action.setModuleId(-1);
            action.setModuleName(s);
            action.setName("Add vm on "+s);
            action.setType(ResizingActionType.SCALE_OUT);
            action.setApplicablePatameters(params);
            
            ResizingActionsCache.addAvailableResizingAction(action);
            
            action = new ResizingAction();
            action.setId(i++);
            action.setModuleId(-1);
            action.setModuleName(s);
            action.setName("Remove vm from "+s);
            action.setType(ResizingActionType.SCALE_IN);
            action.setApplicablePatameters(params);
            
            ResizingActionsCache.addAvailableResizingAction(action);
        }
        Logger.getLogger(Main.class.getName()).info("Resizing actions configured");
    }
    
    // initialize the Decision Module providing the TOSCA file
    // and the 
    private static void initDecisionModule() throws Exception {
        String deploymentId = ServerStaticComponents.properties.getProperty("slipstream.deployment.id");
        // wait until the deployment reach a "READY" state
        ServerStaticComponents.service.waitForReadyState(deploymentId);
        
        // extract tosca from CSAR and send it to the DM
        CSARParser parser = new CSARParser(ServerStaticComponents.toscaFile);
        String toscaContent = parser.getToscaContents();
        String rSyblHost = ServerStaticComponents.properties.getProperty("rsybl.host"),
                rSyblPort = ServerStaticComponents.properties.getProperty("rsybl.port");
        String rSyblURL = "http://"+rSyblHost+":"+rSyblPort+"/rSYBL/restWS";
        SYBLControlClient client = new SYBLControlClient(rSyblURL);
        client.setApplicationDescription(deploymentId, toscaContent);
        Logger.getLogger(Main.class.getName()).info("TOSCA file sent to the Decision Module");

        // provide IPs of the deployed VMs
        HashMap<String, String> ipAddresses = ServerStaticComponents.service.getDeploymentIPs(deploymentId);
        
        
        DeploymentDescription description = new DeploymentDescription();
        description.setAccessIP("localhost");
        description.setCloudServiceID(parser.getAppName());
        List<DeploymentUnit> deploymentUnits = new LinkedList<>();
        for(String s : parser.getModules()) {       //repeat for each module
            DeploymentUnit unit = new DeploymentUnit();     // new deployment unit
            unit.setServiceUnitID(s);           
            List<AssociatedVM> vms = new LinkedList<>();
            for(Map.Entry<String, String> kv : ipAddresses.entrySet()) {
                if(kv.getKey().startsWith(s)) { //this VM belongs to the 
                    AssociatedVM vm = new AssociatedVM();
                    vm.setIp(kv.getValue());
                    vm.setUuid(UUID.randomUUID().toString());
                    vms.add(vm);
                }
            }
            unit.setAssociatedVMs(vms);
            
            List<ElasticityCapability> capabilities = new LinkedList<>();
            ElasticityCapability scaleOut = new ElasticityCapability();
            scaleOut.setPrimitiveOperations("scaleout");
            scaleOut.setName("scaleOut");
            
            ElasticityCapability scaleIn = new ElasticityCapability();
            scaleIn.setPrimitiveOperations("scalein");
            scaleIn.setName("scaleIn");
            
            capabilities.add(scaleOut);
            capabilities.add(scaleIn);
            
            unit.setElasticityCapabilities(capabilities);
            
            deploymentUnits.add(unit);
        }
        description.setDeployments(deploymentUnits);
        
        String deploymentDescriptionXML = new String();
        JAXB.marshal(description, deploymentDescriptionXML);
        client.setApplicationDeployment(deploymentId, deploymentDescriptionXML);
        Logger.getLogger(Main.class.getName()).info("Application deployment XML submitted");

    }
    
    private static void initializeSSclient() throws ValidationException, MalformedURLException, IOException {
        // initialize the SlipStreamService client
        String  username = ServerStaticComponents.properties.getProperty("slipstream.username"),
                password = "",
                hostname = "https://"+ServerStaticComponents.properties.getProperty("slipstream.server.host"),
                celarServerHost = ServerStaticComponents.properties.getProperty("celar.server.host"),
                celarServerPort = ServerStaticComponents.properties.getProperty("celar.server.port");
        // fetch password from CELAR Server
        URL url = new URL("http://"+celarServerHost+":"+celarServerPort+"/user/credentials/?user="+username);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        InputStream in = con.getInputStream();
        int count;
        byte[] buffer = new byte[1024];
        StringBuilder strBuilder = new StringBuilder();
        
        while((count=in.read(buffer))!=-1) {
            strBuilder.append(new String(buffer, 0, count));
        }
        String content =  strBuilder.toString();
        SlipStreamCredentials credentials = JAXB.unmarshal(new StringReader(content), SlipStreamCredentials.class);
        password = credentials.getPassword();
        ServerStaticComponents.service = new SlipStreamSSService(username, password, hostname);
    }
    // orchestrator bootstrapping processes 
    private static void configureOrchestrator() throws MalformedURLException, IOException, Exception {
        initializeSSclient();
        try{
            fetchTosca(ServerStaticComponents.toscaFile);
            setResizingActions(ServerStaticComponents.toscaFile);
            initDecisionModule();
        } catch (Exception e) {
            Logger.getLogger(Main.class.getCanonicalName()).log(Level.SEVERE, e.getMessage());
        }
    }
    
    public static void main(String[] args) throws Exception {
        configureLogger();
        loadProperties();
        creatDirs();
        addShutdownHook();
        configureServer();
        configureOrchestrator();

        ServerStaticComponents.server.start();
        Logger.getLogger(Main.class.getName()).info("Server is started");

    }
}
