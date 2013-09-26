package gr.ntua.cslab.services.deployment;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

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
        this.desc.setRetValue("true/false");
        this.desc.setType("GET");
        this.desc.setDescription("Call used to perform a resizing action to a deployment.<br/>"
        		+ "The expected arguments are:"
        		+ "<ul>"
        		+ "<li>Deployment Id</li>"
        		+ "<li>Resizing action -JSON String describing the actions to be applied- </li>"
        		+ "</ul>");
        this.desc.addParameter("deploymentid", "Integer");
        this.desc.addParameter("action", "JSON string describing the actions");
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
