package com.ttk.action;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TTKFilter implements Filter{

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String requestedCompleteUrl=httpServletRequest.getRequestURL().toString();
		String requestedUrl=requestedCompleteUrl.substring(requestedCompleteUrl.lastIndexOf("/"));
		if(("/AlKootWebService".equals(requestedUrl) || "/AlkootPreapprovalWebService".equals(requestedUrl) || "/AlkootClaimWebService".equals(requestedUrl)))
		filterChain.doFilter(request, response);
		else{
			httpServletRequest.getRequestDispatcher("/unauthorizedaccess").forward(httpServletRequest, httpServletResponse);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
