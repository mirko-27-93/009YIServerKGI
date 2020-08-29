package programranje.yatospace.server.basic.bean;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import programranje.yatospace.server.basic.util.WebBeanGenerator;
import programranje.yatospace.server.basic.web.listener.WebSessionListener;

/**
 * Зрно које се односи информације и податке о бази података. 
 * @author MV
 * @version 1.0
 */
public class DatabaseBean implements Serializable{
	private static final long serialVersionUID = -3612716177918108346L;
	
	private boolean databaseUser;

	public boolean isDatabaseUser() {
		return databaseUser;
	}

	public void setDatabaseUser(boolean databaseUser) {
		this.databaseUser = databaseUser;
	} 
	
	public DatabaseBean synchronizeObjectFoo(HttpSession session) {
		try {
			databaseUser = WebSessionListener.getDatabaseController(session).isDeclaredDBUser()
					    || WebSessionListener.getDatabaseController(session).getDatabaseSession().isLogged();
			return this;
		}catch(Exception ex) {
			return this; 
		}
	}
	
	public String synchronizeStringFoo(HttpSession session) {
		databaseUser = WebSessionListener.getDatabaseController(session).isDeclaredDBUser()
				    || WebSessionListener.getDatabaseController(session).getDatabaseSession().isLogged();
		return ""; 
	}
	
	public boolean isDBUserStrict(HttpSession session) {
		return WebSessionListener.getDatabaseController(session).isDeclaredDBUser(); 
	}
	
	public boolean isDBUser(HttpSession session) {
		try {
			return WebSessionListener.getDatabaseController(session).isDeclaredDBUser()
				|| WebSessionListener.getDatabaseController(session).getDatabaseSession().isLogged();
		}catch(Exception ex){
			return WebSessionListener.getDatabaseController(session).getDatabaseSession().isLogged();
		}
	}
	
	public boolean isDBAdmin(HttpSession session) {
		SessionBean sess = WebBeanGenerator.generateOrGetSessionBean(session); 
		if(!sess.isLogged(session)) return false; 
		return WebSessionListener.getDatabaseController(session).isAdminDBUser(sess.getInfo().getUserName())
				|| WebSessionListener.getDatabaseController(session).getDatabaseSession().isDBAdmin();
	}
	
	public boolean isDBRoot(HttpSession session) {
		SessionBean sess = WebBeanGenerator.generateOrGetSessionBean(session); 
		if(!sess.isLogged(session)) return false; 
		return WebSessionListener.getDatabaseController(session).isRootDBUser(sess.getInfo().getUserName())
			    || WebSessionListener.getDatabaseController(session).getDatabaseSession().isDBRoot();
	}
}
