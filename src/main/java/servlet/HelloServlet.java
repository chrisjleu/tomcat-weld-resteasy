package servlet;

import java.io.IOException;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that demonstrates CDI in operation.
 */
@WebServlet(name = "MyServlet", urlPatterns = { "/hello" })
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = -6573690394116111626L;

    @Inject
    private BeanManager beanManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().append("Hello from your " + beanManager);
    }

}
