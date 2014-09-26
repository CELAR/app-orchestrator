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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * CLI client 
 * 
 * TODO
 * @author Giannis Giannakopoulos
 */
public class CLIClient {
    
    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        Option help = new Option("h", "help", false, "Help message");
        Option host = new Option("H", "host", true, "The host where CELAR Orchestrator runs");
        
        
        options.addOption(help);
        options.addOption(host);
        
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse(options, args);
        
        if(cmd.hasOption("H")) {
            System.out.println("Received host");
        }
        
        if(cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("celar-orchestrator-client", options);
            System.exit(0);
        }
    }
    
}
