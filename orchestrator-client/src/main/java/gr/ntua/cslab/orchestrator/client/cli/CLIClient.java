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
package gr.ntua.cslab.orchestrator.client.cli;

import gr.ntua.cslab.orchestrator.beans.ExecutedResizingAction;
import gr.ntua.cslab.orchestrator.beans.ExecutedResizingActionList;
import gr.ntua.cslab.orchestrator.beans.Parameter;
import gr.ntua.cslab.orchestrator.beans.Parameters;
import gr.ntua.cslab.orchestrator.client.DeploymentStateClient;
import gr.ntua.cslab.orchestrator.client.HistoryClient;
import gr.ntua.cslab.orchestrator.client.ResizingActionsClient;
import gr.ntua.cslab.orchestrator.client.cli.formatter.CLIPrettyFormatter;
import gr.ntua.cslab.orchestrator.client.conf.ClientConfiguration;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * CLI client 
 * 
 * TODO
 * @author Giannis Giannakopoulos
 */
public class CLIClient {
    
    public static void main(String[] args) throws ParseException, IOException {
        Options options = new Options();
        
        Option help = new Option("h", "help", false, "Help message");
        Option version = new Option("v", "version", false, "Version of the specified client");
        Option host = new Option("H", "host", true, "The host where CELAR Orchestrator runs");
        Option port = new Option("P", "port", true, "Port where the CELAR Orchestrator listens to");        
        Option parameters = new Option("p", "parameters", true, "Comma separated key:value of parameters for the resizing action");
        
        options.addOption(help);
        options.addOption(version);
        options.addOption(host);
        options.addOption(port);
        options.addOption(parameters);
        
        
        OptionGroup availableActions = new OptionGroup();
        
        Option listAvailableResizingActions = new Option("la", "list-available", false, "Lists the available resizing actions");
        Option listExecutedResizingActions = new Option("le", "list-executed", false, "Lists all the executed resizing actions");
        Option execute = new Option("e", "execute", true, "Execute a new resizing action");
        Option getStatus = new Option("s", "get-status", true, "Get the status of a resizing action");
        Option getEffect = new Option("ge", "get-effect", true, "Get the effect of a resizing action");
        Option getDeploymentStatus = new Option("ds", "get-deployment-status", false, "Get the deployment status");

        
        availableActions.addOption(listAvailableResizingActions);
        availableActions.addOption(listExecutedResizingActions);
        availableActions.addOption(getStatus);
        availableActions.addOption(execute);
        availableActions.addOption(getDeploymentStatus);
        availableActions.addOption(getEffect);
        
        options.addOptionGroup(availableActions);
        
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse(options, args);
        
        if(cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("celar-orchestrator-client", options);
            System.exit(0);
        }
        
        ClientConfiguration config = null;
        
        if(cmd.hasOption("P") && cmd.hasOption("H")) {
            config = new ClientConfiguration(cmd.getOptionValue("H"), new Integer(cmd.getOptionValue("P")));
        } else {
            System.err.println("Please provide valid host and port to connect");
            System.exit(1);
        }
        
        if(cmd.hasOption("list-available")) {
            ResizingActionsClient client = new ResizingActionsClient();
            client.setConfiguration(config);
            System.out.println(CLIPrettyFormatter.format(client.listResizingActions()));
        } else if(cmd.hasOption("list-executed")) {
            HistoryClient client = new  HistoryClient();
            client.setConfiguration(config);
            
            ExecutedResizingActionList list = client.getExecutedActions();
            if(list == null || list.getExecutedResizingActions() == null) {
                System.out.println("No recorded actions");
            } else {
                System.out.println(CLIPrettyFormatter.format(list));
            }
            
        } else if(cmd.hasOption("execute")) {
            Parameters params = new Parameters();
            if(cmd.hasOption("p")) {
                String parametersString  =cmd.getOptionValue("p");
                for(String kvs:parametersString.split(",")) {
                    String[] kv = kvs.split(":");
                    params.addParameter(new Parameter(kv[0], kv[1]));
                }
            }
            ResizingActionsClient client  = new ResizingActionsClient();
            client.setConfiguration(config);
            client.executeResizingAction(new Integer(cmd.getOptionValue("e")), params);
            
        } else if(cmd.hasOption("get-status")) {
            ResizingActionsClient client = new ResizingActionsClient();
            client.setConfiguration(config);
            ExecutedResizingActionList dummy = new ExecutedResizingActionList();
            dummy.setExecutedResizingActions(new LinkedList<ExecutedResizingAction>());
            dummy.getExecutedResizingActions().add(client.getActionStatus(cmd.getOptionValue("get-status")));
            System.out.println(CLIPrettyFormatter.format(dummy));
        } else if(cmd.hasOption("get-deployment-status")){
            DeploymentStateClient client  = new DeploymentStateClient();
            client.setConfiguration(config);
            System.out.println(client.getDeploymentState());
        } else if(cmd.hasOption("get-effect")) {
            ResizingActionsClient client = new ResizingActionsClient();
            client.setConfiguration(config);
            String uid = cmd.getOptionValue("get-effect");
            System.out.println(client.getActionEffect(uid));
        }
    }
    
}
