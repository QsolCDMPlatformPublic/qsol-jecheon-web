package qsol.qsoljecheonweb.util;


import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import qsol.qsoljecheonweb.manager.model.ManagerInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class QsolHttpSessionUtil {

	public static String getManagerId() {
	    return getManagerInfo().getManagerId();
	}
	
	public static String getManagerId(boolean nullable) {
		try {
			return getManagerInfo().getManagerId();
		} catch (RuntimeException ex) {
		}
		return null;
	}

	private static ManagerInfo getManagerInfo() {
		return Optional.ofNullable((ManagerInfo) getSession().getAttribute("manager-info")).orElseThrow(RuntimeException::new);
	}
	
	private static HttpSession getSession() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
	    ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
	    HttpServletRequest request = attributes.getRequest();

	    return request.getSession();
	}
}
