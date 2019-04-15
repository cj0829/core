package org.csr.core.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang.StringUtils;
import org.csr.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.DispatcherServlet;

public final class PorpeDispatcherServlet extends HttpServlet {

	/** Unique Id for serialization. */
	private static final long serialVersionUID = 1L;

	/** Key under which we will store the exception in the ServletContext. */
	public static final String CAUGHT_THROWABLE_KEY = "exceptionCaughtByServlet";

	/** Instance of Commons Logging. */
	private static final Logger log = LoggerFactory.getLogger(PorpeDispatcherServlet.class);
	
	/** The actual DispatcherServlet to which we will delegate to. */
	private DispatcherServlet delegate = new DispatcherServlet();
	/** Boolean to determine if the application deployed successfully. */
	private boolean initSuccess = true;
	private boolean forceEncoding = false;
	private String encoding;
	private String imgPath="";
	private String jsPath="";
	private String cxt="";
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public void init(final ServletConfig config) {
		try {
			this.delegate.init(config);
			ServletContext context = config.getServletContext();
			imgPath = config.getInitParameter("imgPath");
			jsPath = config.getInitParameter("jsPath");
			cxt=context.getContextPath();
			//
			Constants.CXT = cxt;
			String force=config.getInitParameter("forceEncoding");
			
			if(StringUtils.isEmpty(imgPath)){
				imgPath=cxt;
			}
			if(StringUtils.isEmpty(jsPath)){
				jsPath=cxt;
			}
			if(StringUtils.isNotBlank(force)){
				if("true".equals(force)){
					forceEncoding=true;
				}
			}
			setEncoding( config.getInitParameter("encoding"));
			context.setAttribute("imgPath", imgPath);
			context.setAttribute("jsPath", jsPath);
			context.setAttribute("cxt", cxt);
		} catch (final Throwable t) {
			// let the service method know initialization failed.
			this.initSuccess = false;

			/*
			 * no matter what went wrong, our role is to capture this error and
			 * prevent it from blocking initialization of the servlet. logging
			 * overkill so that our deployer will find a record of this problem
			 * even if unfamiliar with Commons Logging and properly configuring
			 * it.
			 */

			final String message = "SafeDispatcherServlet: \n"
					+ "The Spring DispatcherServlet we wrap threw on init.\n"
					+ "But for our having caught this error, the servlet would not have initialized.";

			// log it via Commons Logging
			log.error(message, t);

			// log it to System.err
			System.err.println(message);
			t.printStackTrace();

			// log it to the ServletContext
			ServletContext context = config.getServletContext();
			context.log(message, t);
			/*
			 * record the error so that the application has access to later
			 * display a proper error message based on the exception.
			 */
			context.setAttribute(CAUGHT_THROWABLE_KEY, t);

		}
	}

	/**
	 * @throws ApplicationContextException
	 *             if the DispatcherServlet does not initialize properly, but
	 *             the servlet attempts to process a request.
	 */
	public void service(final ServletRequest request, final ServletResponse response)throws ServletException, IOException {
		if (this.initSuccess) {
			this.delegate.service(request, response);
		} else {
			throw new ApplicationContextException("Unable to initialize application context.");
		}
	}
}
