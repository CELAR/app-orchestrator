package gr.ntua.cslab.services.metrics;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Get
 */
@WebServlet("/Get")
public class Get extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTMLDescription desc;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Get() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Get metrics");
        this.desc.setRetValue("JSON format string representing the metrics");
        this.desc.setType("GET");
        this.desc.setDescription("This call returns a set of metrics previously inserted into the database.<br/>"
        		+ "The metrics are searched for <ul>"
        		+ "<li>a specific deployment  (identified by a unique id),</li>"
        		+ "<li>a specific metric type (identified by a unique metric id),</li>"
        		+ "<li>a timeframe (identified by a unique metric id),</li>"
        		+ "</ul> ");
        this.desc.addParameter("deploymentid", "integer");
        this.desc.addParameter("metricid", "integer");
        this.desc.addParameter("timeframe", "JSON string (?)");
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
