package qsol.qsoljecheonweb.manager.interceptor;

import qsol.qsoljecheonweb.manager.model.ManagerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Manager;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class ManagerLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        ManagerInfo managerInfo = (ManagerInfo) session.getAttribute("manager-info");
        String requestUri = request.getRequestURI();
        if (managerInfo == null) {
            response.sendRedirect("/view/mobileWeb/security/Login");
            return false;
        }
        return true;
    }
}
