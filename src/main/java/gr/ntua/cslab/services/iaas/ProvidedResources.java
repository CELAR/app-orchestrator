package gr.ntua.cslab.services.iaas;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProvidedResources
 */
@WebServlet("/ProvidedResources")
public class ProvidedResources extends HttpServlet {
	private static final long serialVersionUID = 1L;
     private HTMLDescription desc;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProvidedResources() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Get Provided Resources");
        this.desc.setType("GET");
        this.desc.setRetValue("Provided Resources - JSON -");
        this.desc.addParameter("type", "String");
        this.desc.setDescription("This method is called to provide information about the provided resources.<br/>The expected parameter is the type of resources:<br/>" +
        		"e.g: disk, images, etc.");
        this.desc.setExample("Not ready yet: PLEASE come back later :)");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("info")!=null)
			response.getOutputStream().print(this.desc.toHTML());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
