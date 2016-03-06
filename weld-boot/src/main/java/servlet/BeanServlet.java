package servlet;

import java.io.IOException;
import java.util.Set;

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
 * A servlet that demonstrates CDI in operation by displaying all the objects
 * managed by the {@link BeanManager}.
 */
@WebServlet(name = "BeanServlet", urlPatterns = { "/servlet/beans" })
public class BeanServlet extends HttpServlet {

	private static final long serialVersionUID = -6573690394116111626L;

	@SuppressWarnings("serial")
	private static final AnnotationLiteral<Any> ANY_ANNOTATION = new AnnotationLiteral<Any>() {
	};

	private static final Class<Object> OBJECT_CLASS = Object.class;

	@Inject
	private BeanManager bm;

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {

		final StringBuilder responseBuilder = new StringBuilder();
		responseBuilder.append(this.bm.toString()).append(":\n");

		// Iterate through all the beans
		final Set<Bean<?>> beans = this.bm.getBeans(OBJECT_CLASS, ANY_ANNOTATION);
		for (final Bean<?> bean : beans) {
			responseBuilder.append(bean.getName()).append(":").append((bean.getBeanClass().getName())).append("\n");
		}

		resp.setContentType("text/plain");
		resp.getWriter().append(responseBuilder.toString());
	}

}
