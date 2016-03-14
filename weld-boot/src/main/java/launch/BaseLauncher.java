package launch;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * The default launcher starts CDI (Weld) and configures a {@link Tomcat}
 * instance with a web application.
 */
public class BaseLauncher {

	protected static final String DEFAULT_PORT = "8889";

	private static String[] PARAMETERS;

	protected static String[] getParameters() {
		final String[] copy = new String[BaseLauncher.PARAMETERS.length];
		System.arraycopy(BaseLauncher.PARAMETERS, 0, copy, 0, BaseLauncher.PARAMETERS.length);
		return copy;
	}

	public BaseLauncher(final String[] commandLineArgs) throws ServletException, LifecycleException {
		BaseLauncher.PARAMETERS = commandLineArgs;
		final Tomcat tomcat = LaunchUtilities.getConfiguredTomcatInstance();
		tomcat.setPort(Integer.valueOf(DEFAULT_PORT));
		configure(tomcat, PARAMETERS);
		tomcat.start();
		tomcat.getServer().await();
	}

	/**
	 * Mutate {@link Tomcat} to configure it on startup.
	 * 
	 * @param tomcat
	 * @param commandLineArgs
	 */
	protected void configure(Tomcat tomcat, String[] commandLineArgs) {
	}

	protected WeldContainer startWeld() {
		final Weld weld = new Weld();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(weld));
		final WeldContainer container = weld.initialize(); // Fires events
		return container;
	}

	private static class ShutdownHook extends Thread {
		private final Weld weld;

		ShutdownHook(final Weld weld) {
			this.weld = weld;
		}

		@Override
		public void run() {
			this.weld.shutdown();
		}
	}

}
