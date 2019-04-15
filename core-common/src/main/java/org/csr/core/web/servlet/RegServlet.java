package org.csr.core.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.license.LicenseTool;

public class RegServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		String snId=request.getParameter("snId");
		String remark=LicenseTool.getActivated(snId);
//		LicenseUtil.reg(snId, remark);
	}
}
