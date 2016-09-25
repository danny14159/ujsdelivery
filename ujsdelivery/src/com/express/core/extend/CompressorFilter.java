package com.express.core.extend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CompressorFilter implements Filter{

	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse)resp);
		
		chain.doFilter(req, resp);
		
		String result = wrapper.getResult();
		System.out.println(result);
		
		PrintWriter out = resp.getWriter();
		out.write(result);
		out.flush();
		out.close();
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}