package com.packtpub.springsecurity.web.configuration;

import com.packtpub.springsecurity.configuration.DataSourceConfig;
import com.packtpub.springsecurity.configuration.JavaConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


/**
 * Replaces web.xml.txt in Servlet v.3.0+
 *
 * @see
 */
public class WebAppInitializer
		extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * Get root config classes class [ ].
	 *
	 * @return the class [ ]
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { JavaConfig.class, DataSourceConfig.class };
	}

	/**
	 * Get servlet config classes class [ ].
	 *
	 * @return the class [ ]
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebMvcConfig.class };
	}

	/**
	 * Get servlet mappings string [ ].
	 *
	 * @return the string [ ]
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/*" };
	}

	/**
	 * On startup.
	 *
	 * @param servletContext the servlet context
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void onStartup(final ServletContext servletContext)
			throws ServletException {

		// Register DispatcherServlet
		super.onStartup(servletContext);

		// Register H2 Admin console:
		ServletRegistration.Dynamic h2WebServlet = servletContext.addServlet("h2WebServlet",
				new org.h2.server.web.JakartaWebServlet());
		h2WebServlet.addMapping("/admin/h2/*");
		h2WebServlet.setInitParameter("webAllowOthers", "true");

	}

} 
