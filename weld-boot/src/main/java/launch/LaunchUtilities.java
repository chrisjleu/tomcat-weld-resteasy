package launch;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.jboss.weld.environment.servlet.Listener;

public class LaunchUtilities {

	private static final String CONTEXT_PATH = "/";
	
	private static final String DOCUMENT_ROOT_PATH = "src/main/webapp/";
	
	static Tomcat getConfiguredTomcatInstance() throws ServletException {
		final Tomcat tomcat = new Tomcat();

		final StandardContext ctx = (StandardContext) tomcat.addWebapp(CONTEXT_PATH,
				new File(DOCUMENT_ROOT_PATH).getAbsolutePath());

		// Declare an alternative location for your "WEB-INF/classes" dir
		// Servlet 3.0 annotation will work
		final WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(
				new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
		ctx.setResources(resources);

		// Listen for startup events
		ctx.addApplicationListener(Listener.class.getName());

		// Enable JNDI InitialContext
		tomcat.enableNaming();
		
		return tomcat;
	}
}
