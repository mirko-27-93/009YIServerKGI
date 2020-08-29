package programranje.yatospace.server.basic.bean;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import programranje.yatospace.server.basic.controller.DatabaseController;
import programranje.yatospace.server.basic.controller.SessionController;
import programranje.yatospace.server.basic.model.SessionInfo;
import programranje.yatospace.server.basic.util.MessageType;
import programranje.yatospace.server.basic.util.SessionInfoDefaultGenerator;
import programranje.yatospace.server.basic.util.WebBeanGenerator;
import programranje.yatospace.server.basic.web.listener.WebSessionListener;

/**
 * Зрно помоћу ког се барата са сесијом, односно пријавом. 
 * @author mirko
 * @version 1.0
 */
public class SessionBean implements Serializable{
	private static final long serialVersionUID = -3720950147670680673L;
	private final SessionInfo nullInfo = SessionInfoDefaultGenerator.generateDefaultSessionInfo(); 
	
	private SessionInfo info = null;
	private String password = ""; 
	
	public SessionInfo getInfo() {
		if(info==null) return nullInfo; 
		return info;
	}
	public void setInfo(SessionInfo info) {
		this.info = info;
	} 
	
	public String login(HttpSession session) { 
		boolean success = false; 
		updateBean(session); 
		
		SessionController controller = WebSessionListener.getSessionController(session);
		MessageBean info = WebBeanGenerator.generateOrGetMessageBean(session); 
		
		
		try {
			controller.login(session, getInfo(), password); 
	
			info.setException(null);
			if(controller.isLogged(session)) {
				info.setMessage("Корисник је успјешно пријављен.");
				info.setType(MessageType.SUCCESS);
				success = true;
			}else {
				info.setMessage("Пријава корисника није успјела. Провјерити параметре.");
				info.setType(MessageType.ERROR);
			}
			setInfo(controller.session());
		}catch(Exception ex) {
			info.setMessage("Пријава корисника није успјела. Провјерити параметре.");
			info.setType(MessageType.ERROR);
		}
		
		
		try {
			DatabaseController dbController = WebSessionListener.getDatabaseController(session); 
			try { dbController.login(getInfo(), password); }
			catch(Exception ex) {dbController.loginAsDBUserOnly(getInfo().getUserName(), password, getInfo().getDataBaseSvcName(), getInfo().getDataBase());}
			info.setMessage("Корисник је успјешно пријављен.");
			info.setType(MessageType.SUCCESS);
		}catch(Exception ex) {
			if(!success) {
				info.setMessage("Пријава корисника није успјела. Провјерити параметре.");
				info.setType(MessageType.ERROR);
			}
		}
	
		
		password = "";
		return ""; 
	}
	
	public String logout(HttpSession session) {
		updateBean(session);
		
		SessionController controller = WebSessionListener.getSessionController(session);
		DatabaseController dbController = WebSessionListener.getDatabaseController(session); 
		
		controller.logout(session); 
		dbController.logout();
		
		MessageBean info = WebBeanGenerator.generateOrGetMessageBean(session); 
		info.setException(null);
		
		if(!controller.isLogged(session)) {
			info.setMessage("Корисник је успјешно одјављен.");
			info.setType(MessageType.SUCCESS);
		}else {
			info.setMessage("Одјава корисника није успјела. Провјерити параметре.");
			info.setType(MessageType.ERROR);
		}
		
		setInfo(null);
		return ""; 
	}
	
	public boolean isLogged(HttpSession session) {
		SessionController controller = WebSessionListener.getSessionController(session);
		return controller.isLogged(session);
	}
	public boolean isLoggedUserProfile(HttpSession session) {
		SessionController controller = WebSessionListener.getSessionController(session);
		return controller.isLoggedViaUserProfile();
	}
	public boolean isLoggedDatabaseProfile(HttpSession session) {
		SessionController controller = WebSessionListener.getSessionController(session);
		return controller.isLoggedViaDatabaseProfile(session);
	}
	
	public String getPassword() {
		if(password==null) password = ""; 
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String resetPassword() {
		this.password = "";
		return ""; 
	}
	
	public String updateBean(HttpSession session) {
		SessionController controller = WebSessionListener.getSessionController(session);
		setInfo(controller.session());
		return ""; 
	}
	
	public String userProfileSessionId(HttpSession session) {
		if(!isLoggedUserProfile(session)) return ""; 
		SessionController controller = WebSessionListener.getSessionController(session);
		return controller.userProfileSessionId(); 
	}
}
