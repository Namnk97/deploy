package googleservlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import accessgoogle.GooglePojo;
import accessgoogle.GoogleUtils;

@WebServlet("/login-google")
public class LoginGoogle extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginGoogle() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code");
		if (code == null || code.isEmpty()) {
			RequestDispatcher dis = request.getRequestDispatcher("/views/login.jsp");
			dis.forward(request, response);
		} else {
			String token = GoogleUtils.getToken(code);
			GooglePojo googlePojo = GoogleUtils.getUserInfo(token);
			request.setAttribute("id", googlePojo.getId());
			request.setAttribute("name", googlePojo.getName());
			request.setAttribute("email", googlePojo.getEmail());
			RequestDispatcher dis = request.getRequestDispatcher("/views/index.jsp");
			dis.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//doGet(request, response);
	}

}
