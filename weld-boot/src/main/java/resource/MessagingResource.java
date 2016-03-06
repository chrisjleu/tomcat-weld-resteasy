package resource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import jms.JmsMessageProducer;

/**
 * A Restful API for sending a JMS mesage.
 */
@Path("messaging")
@RequestScoped
public class MessagingResource {

	@Inject
	private JmsMessageProducer jmsMessageProducer;

	/**
	 * Send a JMS message.
	 * 
	 * <pre>
	 * curl -X POST http://localhost:8080/api/messaging/send/HELLO
	 * </pre>
	 */
	@POST
	@Path("/send/{text}")
	public void sendMessage(@PathParam("text") String message) {
		try {
			jmsMessageProducer.sendJmsMessage(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
