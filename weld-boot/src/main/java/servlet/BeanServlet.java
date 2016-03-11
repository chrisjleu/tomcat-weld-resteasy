package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that demonstrates CDI injection by displaying all the objects
 * managed by the {@link BeanManager}.
 */
@WebServlet(name = "BeanServlet", urlPatterns = { "/servlet/beans" })
public class BeanServlet extends HttpServlet {

	private static final long serialVersionUID = -6573690394116111626L;

	private static final String LINE_BREAK = System.lineSeparator();

	@SuppressWarnings("serial")
	private static final AnnotationLiteral<Any> ANY_ANNOTATION = new AnnotationLiteral<Any>() {
	};

	private static final Class<Object> OBJECT_CLASS = Object.class;

	@Inject
	private BeanManager beanManager;

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {

		String responseContent = formatBeanList(this.beanManager);

		response.setContentType("text/plain");
		response.getWriter().append(responseContent);
	}

	/**
	 * Format nicely the set of beans the that {@link BeanManager} knows about.
	 * 
	 * @param bm
	 * @return
	 */
	String formatBeanList(BeanManager bm) {
		final StringBuilder responseBuilder = new StringBuilder();

		// List header
		responseBuilder.append(bm.toString()).append(":").append(LINE_BREAK);

		// Sort all the beans by class
		final List<Bean<?>> sortedBeanList = sortByClass(bm.getBeans(OBJECT_CLASS, ANY_ANNOTATION));

		// Create blocks of beans by organization
		String previousOrg = null;
		for (final Bean<?> bean : sortedBeanList) {
			String currentOrg = extractOrgNameFromPackageName(bean.getBeanClass().getPackage().getName());
			if (!currentOrg.equals(previousOrg)) {
				// Break up the beans by organization
				responseBuilder.append(LINE_BREAK);
				previousOrg = currentOrg;
			}
			responseBuilder.append(bean.getBeanClass().getName()).append(" (").append(bean.getName()).append(")")
					.append(LINE_BREAK);
		}
		return responseBuilder.toString();
	}

	/**
	 * Extract just the "organization" - com.company for example - from the
	 * given package or fully qualified class name.
	 * 
	 * @param packageName
	 * @return
	 */
	String extractOrgNameFromPackageName(String packageName) {
		int dot1 = packageName.indexOf(".");
		int dot2 = packageName.indexOf(".", dot1 + 1);
		if (dot1 == -1) {
			return packageName;
		} else if (dot2 == -1) {
			// No second dot
			return packageName.substring(0, dot1);
		} else {
			return packageName.substring(0, dot2);
		}
	}

	/**
	 * Sort a set of beans by class name.
	 * 
	 * @param beans
	 * @return
	 */
	List<Bean<?>> sortByClass(Set<Bean<?>> beans) {
		List<Bean<?>> sorted = new ArrayList<>();
		sorted.addAll(beans);
		Collections.sort(sorted, new Comparator<Bean<?>>() {
			public int compare(Bean<?> a, Bean<?> b) {
				return a.getBeanClass().getName().compareTo(b.getBeanClass().getName());
			}
		});

		return sorted;
	}
}
