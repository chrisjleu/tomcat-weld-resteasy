package launch;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.jboss.weld.environment.se.StartMain;

/**
 * Run the main method and that's it. For how Weld recommends starting
 * applications outside a JEE container, see {@link StartMain}.
 *
 * @see StartMain
 */
public class Main extends BaseLauncher {

	public Main(String[] commandLineArgs) throws ServletException, LifecycleException {
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
			new Main(args).startWeld();
		} catch (ServletException | LifecycleException e) {
			e.printStackTrace();
		}
	}
}
