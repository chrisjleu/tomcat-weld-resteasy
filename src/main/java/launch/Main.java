package launch;

import java.io.File;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 * Run the main method and that's it.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        setListeningPort(tomcat);

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File("src/main/webapp/").getAbsolutePath());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(
                resources, 
                "/WEB-INF/classes",
                new File("target/classes").getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }

    private static void setListeningPort(Tomcat tomcat) {
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        tomcat.setPort(Integer.valueOf(webPort));
    }

}
