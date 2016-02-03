package launch;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * The presence of a class that extends {@link Application} causes the REST
 * machinery to come to life. It is a good idea to specify an attribute other
 * than just "/" in case you have other services running. Perhaps you have
 * index.jsp as your welcome page for instance.
 */
@ApplicationPath("/api")
public class RestApplicationConfig extends Application {
}
