package gr.ntua.cslab.services.deployment;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetConf
 */
@WebServlet("/GetConf")
public class GetConf extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTMLDescription desc;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetConf() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Get configuration");
        this.desc.setRetValue("JSON String describing the configuration");
        this.desc.setType("GET");
        this.desc.setDescription("Call used to get the configuration of a deployment for a specific timestamp.<br/>"
        		+ "The expected arguments are:"
        		+ "<ul>"
        		+ "<li>Deployment Id</li>"
        		+ "<li>Timestamp</li>"
        		+ "</ul>");
        this.desc.addParameter("deploymentid", "Integer");
        this.desc.addParameter("timestamp", "String (or long expressed as seconds since unix EPOCH)");
        this.desc.setExample("Not ready yet.");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("info")!=null){
			response.getOutputStream().println(this.desc.toHTML());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
