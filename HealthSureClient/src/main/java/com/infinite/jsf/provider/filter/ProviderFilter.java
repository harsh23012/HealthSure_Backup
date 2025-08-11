package com.infinite.jsf.provider.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
 
public class ProviderFilter implements Filter {
 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Optional: Initialization logic
	}
 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {

	    HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
	    HttpSession session = req.getSession(true);

	    Object providerUser = (session != null) ? session.getAttribute("loggedInProvider") : null;

	    String loginPage = req.getContextPath() + "/provider/Login.jsf";
	    String uri = req.getRequestURI();

	    boolean isLoginRequest = uri.endsWith("/provider/Login.jsf");
	    boolean isResourceRequest = uri.contains("javax.faces.resource");
	    boolean isSignUpRequest = uri.endsWith("/provider/SignUp.jsf") || uri.endsWith("/provider/VerifyOtp.jsf") || uri.endsWith("/provider/GeneratePassword.jsf") || uri.endsWith("/provider/Success.jsf");

	    if (providerUser != null || isLoginRequest || isResourceRequest || isSignUpRequest) {
	        // Logged in or accessing login/resources: allow
	        chain.doFilter(request, response);
	    } else {
	        // Not logged in and not accessing allowed pages: redirect to login
	        uri = uri.substring(req.getContextPath().length()).split(";")[0];
	        System.out.println(uri);
	        session.setAttribute("requestUri", uri);
	        res.sendRedirect(loginPage);
	    }
	}

	@Override
	public void destroy() {
		// Optional: Cleanup
	}
}
