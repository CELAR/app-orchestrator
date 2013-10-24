package gr.ntua.cslab.services.iaas;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserQuotas
 */
@WebServlet("/iaas/quotas")
public class UserQuotas extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private HTMLDescription desc;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserQuotas() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Get User Quotas");
        this.desc.setDescription("This method returns the user's quotas.");
        this.desc.setRetValue("User quotas -JSON-");
        this.desc.setExample("Not ready yet");
        this.desc.setType("GET");
        this.desc.addParameter("userid", "String");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("info")!=null){
			response.getOutputStream().print(this.desc.toHTML());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
