package tradealpha.FILTER;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tradealpha.BEAN.*;

@WebFilter(filterName="MemberNavigationFilter", urlPatterns={"/site/admin/admin-area.jsp", "/site/members-area/members-area.jsp"})
public class MemberCheckFilter implements Filter {

    private FilterConfig filterConfig = null;

    @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
	throws IOException, ServletException {
            
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            
            HttpSession session = httpRequest.getSession();
            String servletPath = httpRequest.getServletPath();
            
          
            if(servletPath.equals("/site/members-area/members-area.jsp")){
                Login loggedInUser = (Login) session.getAttribute("loggedInUser");
            
                if(loggedInUser == null){
                    httpResponse.sendError(403);
                }else{
                    chain.doFilter(request, response);
                } 
               
            }else if(servletPath.equals("/site/admin/admin-area.jsp")){
                Admin admin = (Admin) session.getAttribute("admin");
                
                if(admin == null){
                    httpResponse.sendError(403);
                }else{
                    chain.doFilter(request, response);
                }
            }

    }
    
    @Override
    public void init(FilterConfig filterConfig) { 
	this.filterConfig = filterConfig;
    }
    
    @Override
    public void destroy() {
        
    }

}
