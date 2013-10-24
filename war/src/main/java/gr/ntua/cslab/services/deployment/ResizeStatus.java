package gr.ntua.cslab.services.deployment;

import java.io.IOException;

import gr.ntua.cslab.bash.Command;
import gr.ntua.cslab.descriptor.HTMLDescription;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Resize
 */
@WebServlet("/ResizeStatus")
public class ResizeStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTMLDescription desc;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResizeStatus() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Poll about the status of a resizing action");
        this.desc.setRetValue("JSON message ");
        this.desc.setType("GET");
        this.desc.setDescription("This service is used to provide info about the status of the resizing action pending.<br/>"
        		+ "");
        this.desc.addParameter("ip", "IPv4 address (mandatory for addvm, removevm type, not needed for cleanup)");
        this.desc.addParameter("action", "String (one of addvm, removevm, cleanup)");
        this.desc.setExample("http://83.212.117.112/celar-orchestrator/deployment/resizestatus/?action=addvm&ip=127.0.0.1<br/>"
        		+ "http://83.212.117.112/celar-orchestrator/deployment/resizestatus/?action=removevm&ip=127.0.0.1<br/>"
        		+ "http://83.212.117.112/celar-orchestrator/deployment/resizestatus/?action=cleanup<br/>"
        		+ "Returned JSON message format:</br>"
        		+ "{ \"finished\":\"true\"} self describing, isn't it? :)<br/>"
        		+ "{ \"finished\":\"false\"} <br/>");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("info")!=null){
			response.getOutputStream().println(this.desc.toHTML());
			return;
		}
		String action=request.getParameter("action");
		if(action.equals("cleanup")){
			Command com = new Command("cleanup.sh finished");
			com.waitFor();
			response.getOutputStream().println(this.outputJSON(com.getStdout().equals("true")));
		} else {
			String ip = request.getParameter("ip");
			Command com = new Command("activeNodes.sh");
			com.waitFor();
			String out = com.getStdout();
			if(action.equals("addvm")){
				response.getOutputStream().println(this.outputJSON(out.contains(ip)));
			} else if(action.equals("removevm")){
				response.getOutputStream().println(this.outputJSON(!out.contains(ip)));
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private String outputJSON(boolean status){
		return "{\"finished\":\""+(status?"true":"false")+"\"} ";
	}

}
