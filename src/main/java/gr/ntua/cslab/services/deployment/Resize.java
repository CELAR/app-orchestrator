package gr.ntua.cslab.services.deployment;

import gr.ntua.cslab.bash.Command;
import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Resize
 */
@WebServlet("/Resize")
public class Resize extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTMLDescription desc;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Resize() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Perform a resizing action");
        this.desc.setRetValue("<del>true/false</del><br/>"
        		+ "JSON message about the status of the cluster now after the resizing action. The message returned"
        		+ "contains the stdout and stderr outputs for the processes which were raised to serve the request. <br/>"
        		+ "(total processes = multiplicity)");
        this.desc.setType("GET");
        this.desc.setDescription("Call used to perform a resizing action to a deployment.<br/>"
        		+ "The expected arguments are:"
        		+ "<ul>"
        		+ "<del><li>Deployment Id</li>"
        		+ "<li>Resizing action -JSON String describing the actions to be applied- </li></del>"
        		+ "<li>action (addvm, removevm, cleanup)</li>"
        		+ "<li>multiplicity (1,2,..etc.) --does not run in parallel--</li>"
        		+ "</ul>"
        		+ "Default multiplicity is 1.<br/>"
        		+ "<span style='color:red;font-weight:bold'>multiplicity is not running in parallel!!</span>");
        this.desc.addParameter("deploymentid", "Integer");
        this.desc.addParameter("action", "JSON string describing the actions");
        this.desc.setExample("http://83.212.117.112/celar-orchestrator/deployment/resize/?action=addvm<br/>"
        		+ "http://83.212.117.112/celar-orchestrator/deployment/resize/?action=removevm<br/>"
        		+ "http://83.212.117.112/celar-orchestrator/deployment/resize/?action=cleanup<br/>");
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
		int multi=1;
		if(request.getParameter("multiplicity")!=null){
			multi = new Integer(request.getParameter("multiplicity"));
		}
		PrintWriter out = new PrintWriter(response.getOutputStream());
		List<Command> procs = new LinkedList<Command>();
		for(int i=0;i<multi;i++){
			Command com = null;
			if(action.equals("addvm"))
				com = new Command("addNode.sh");
			else if(action.equals("removevm"))
				com = new Command("removeNode.sh");
			else if (action.equals("cleanup"))
				com = new Command("cleanup.sh");
			com.waitFor();
			procs.add(com);
		}
		out.println("{");
		int i=1;
		for(Command c:procs){
			out.print("\t\""+i+"\":"+c.getOutputsAsJSONString());
			i++;
			if(i<=multi)out.println(", ");
		}
		out.println("}");
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
