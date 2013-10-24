package gr.ntua.cslab.services.action;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetActionHistory
 */
@WebServlet("/GetActionHistory")
public class GetHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private HTMLDescription desc;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetHistory() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Get action history");
        this.desc.setRetValue("JSON format string representing the action history");
        this.desc.setType("GET");
        this.desc.setDescription("This call is used to fetch the action history of specifc decision. The exact functionality is not"
        		+ " yet finalized.");
        this.desc.addParameter("resizeactionid", "integer");
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
