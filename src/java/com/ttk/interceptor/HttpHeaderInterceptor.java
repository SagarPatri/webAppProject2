package com.ttk.interceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class HttpHeaderInterceptor implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException,ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("Cache-Control", "no-cache");
		resp.setHeader("Pragma", "no-cache");
		resp.addHeader("X-Frame-Options", "SAMEORIGIN");
		resp.addHeader("X-Content-Type-Options", "nosniff");
		resp.addHeader("X-XSS-Protection","1;mode=block");
		resp.addHeader("Set-Cookie", "httponly;SameSite=Strict"); //modified
		chain.doFilter(request, response);
		
	}
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
