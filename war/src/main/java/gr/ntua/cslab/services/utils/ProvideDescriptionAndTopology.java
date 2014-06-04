package gr.ntua.cslab.services.utils;

import gr.ntua.cslab.bash.Command;
import gr.ntua.cslab.utils.ApplicationXMLs;
import gr.ntua.cslab.utils.HostsReader;
import gr.ntua.cslab.utils.RSYBLClient;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProvideDescriptionAndTopology
 */
@WebServlet("/ProvideDescriptionAndTopology")
public class ProvideDescriptionAndTopology extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProvideDescriptionAndTopology() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationXMLs topology = new ApplicationXMLs();
		HostsReader reader = new HostsReader();
		topology.setYcsb(reader.getIPs("ycsb"));
		List<String> cassandra = new LinkedList<String>();
//		cassandra.addAll(reader.getIPs("cassandra"));
//		cassandra.add(0, reader.getIPs("seed").get(0));
		
		Command command = new Command("/usr/local/bin/activeNodes.sh");
		command.waitFor();
		for(String s : command.getStdout().split("\n")){
			cassandra.add(s);
		}
		
		topology.setCassandra(cassandra);
		topology.generateXML();
		String deploymentXML = topology.getXML();
		String appDescXML = topology.getApplicationDescriptionXML();
		System.out.println(appDescXML);
		RSYBLClient.sendXML(RSYBLClient.APP_DEPLOYMENT_URL,  deploymentXML);
		RSYBLClient.sendXML(RSYBLClient.APP_DESCRIPTION_URL, appDescXML);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
