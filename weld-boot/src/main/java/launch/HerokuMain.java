package launch;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * The starting point for applications deployed to Heroku.
 */
public class HerokuMain extends BaseLauncher {

	private static final String HEROKU_PORT_ENV_VAR = "PORT";

	public HerokuMain(String[] commandLineArgs) throws ServletException, LifecycleException {
		super(commandLineArgs);
	}

	/**
	 * The main method called from the command line.
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String[] args) {
		try {
			new HerokuMain(args).startWeld();
		} catch (ServletException | LifecycleException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void configure(Tomcat tomcat, String[] commandLineArgs) {
		HerokuMain.setListeningPort(tomcat);
	}

	/**
	 * Heroku convention is to check the "PORT" environment variable for the
	 * HTTP listening port.
	 */
	static void setListeningPort(final Tomcat tomcat) {
		String webPort = System.getenv(HEROKU_PORT_ENV_VAR);
		if (webPort == null || webPort.isEmpty()) {
			webPort = DEFAULT_PORT;
		}
		tomcat.setPort(Integer.valueOf(webPort));
	}
}
