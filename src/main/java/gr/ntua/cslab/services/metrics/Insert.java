package gr.ntua.cslab.services.metrics;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Insert
 */
@WebServlet("/Insert")
public class Insert extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTMLDescription desc;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Insert() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Insert metrics");
        this.desc.setRetValue("true or false");
        this.desc.setType("GET");
        this.desc.setDescription("This call accepts as a paramer a JSON string representing a metric (could be a set"
        		+ " of actual metrics) and stores it in the database. It returns a boolean value, representing the success or failure"
        		+ " of the insertion.");
        this.desc.addParameter("metrics", "JSON format String");
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
