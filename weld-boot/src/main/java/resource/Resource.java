package resource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Named("Bean Resource")
@Path("resource")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RequestScoped
public class Resource {

	private static final AnnotationLiteral<Any> ANY_ANNOTATION = new AnnotationLiteral<Any>() {
		private static final long serialVersionUID = 7447018663992004855L;
	};

	@Inject
	private BeanManager bm;

	/**
	 * curl http://localhost:8080/api/resource/beans/
	 *
	 * @return Details of all beans managed by CDI.
	 */
	@GET
	@Path("/beans")
	public List<String> fetchAll() {
		final Set<Bean<?>> beans = this.bm.getBeans(Object.class, Resource.ANY_ANNOTATION);
		return beans.stream().map(bean -> bean.getBeanClass().getName()).collect(Collectors.toList());
	}
}
