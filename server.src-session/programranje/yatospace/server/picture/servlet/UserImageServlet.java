package programranje.yatospace.server.picture.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import programiranje.yi.user.app.services.client.UserImageAdapter;
import programranje.yatospace.server.basic.bean.UserBean;
import programranje.yatospace.server.basic.controller.SessionController;
import programranje.yatospace.server.basic.util.WebBeanGenerator;
import programranje.yatospace.server.basic.web.listener.WebSessionListener;

/**
 * Сервлет којим се преслеђује корисничка слика. 
 * @author mirko
 * @version 1.0
 */
@WebServlet("/UserImageServlet")
public class UserImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UserImageServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("image");
		
		SessionController controller = WebSessionListener.getSessionController(request.getSession());
		UserImageAdapter adapter = controller.getUserImageAdapter(); 
		UserBean userBean = WebBeanGenerator.generateOrGetUserBean(request.getSession());
		
		userBean.init(request.getSession());
		
		adapter.transferImage(userBean.getUserPictureAddress(), response.getOutputStream());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
