package gr.ntua.cslab.services.probes;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Upload
 */
@WebServlet("/Upload")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private HTMLDescription desc;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Upload custome probes");
        this.desc.setRetValue("true/false");
        this.desc.setType("GET");
        this.desc.setDescription("This call is used to upload and configure a custom probe. The return value is boolean "
        		+ "indicating the status of the upload.");
        this.desc.addParameter("probe", "??what form??");
        this.desc.addParameter("application components", "JSON string");
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
