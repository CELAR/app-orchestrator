package gr.ntua.cslab.services.deployment;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Deploy
 */
@WebServlet("/Deploy")
public class Deploy extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTMLDescription desc;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Deploy() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Deploy application");
        this.desc.setRetValue("true/false");
        this.desc.setType("GET");
        this.desc.setDescription("Call used to deploy an application.<br/>"
        		+ "This call will physically run into CELAR Server. The expected arguments are:"
        		+ "<ul>"
        		+ "<li>Deployment Configuration</li>"
        		+ "<li>Application id (identifies the application)</li>"
        		+ "</ul>");
        this.desc.addParameter("applicationid", "Integer");
        this.desc.addParameter("conf", "JSON string describing the configuration");
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
