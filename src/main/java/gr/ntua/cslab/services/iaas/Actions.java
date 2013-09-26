package gr.ntua.cslab.services.iaas;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IaasActions
 */
@WebServlet("/IaaSActions")
public class Actions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTMLDescription desc;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Actions() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Get IaaS's provided actions");
        this.desc.setRetValue("JSON string -representing the supported actions-");
        this.desc.setType("GET");
        this.desc.setDescription("This calls returns the resizing action supported by IaaS in JSON format");
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
