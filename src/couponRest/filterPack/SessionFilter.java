package couponRest.filterPack;

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

@WebFilter("/sessionFilter")
public class SessionFilter implements Filter {

	public SessionFilter() {

	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
				HttpServletRequest req = (HttpServletRequest) request;
				HttpServletResponse res = (HttpServletResponse) response;
				HttpSession session = req.getSession(false);
		
				System.out.println(req.getRequestURI());
				
				if (session == null) {
					session = req.getSession();
					System.out.println("No Session");
					res.sendRedirect("http://localhost:8080/EKLWS/index.html");
					return;
				} else {
					System.out.println("Session O.K");
					chain.doFilter(request, response);
				}
		
			}
		
			/**
			 * @see Filter#init(FilterConfig)
			 */
			public void init(FilterConfig fConfig) throws ServletException {
				// TODO Auto-generated method stub
			}

}
