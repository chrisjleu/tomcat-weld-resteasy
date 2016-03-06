package resource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import jms.JmsMessageProducer;

/**
 * A Restful API for sending a JMS mesage. Consumes JSON message bodies.
 */
@Path("command")
@RequestScoped
public class MessagingResource {

	@Inject
	private JmsMessageProducer jmsMessageProducer;

	/**
	 * Send simple command as URL path parameter.
	 * 
	 * <pre>
	 * curl -X POST http://localhost:8080/api/messaging/send/HELLO
	 * </pre>
	 */
	@POST
	@Path("/simple/{instruction}")
	public void simpleCommand(@PathParam("instruction") String instruction) {
		try {
			jmsMessageProducer.sendJmsMessage(instruction);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send command.
	 * 
	 * <pre>
	 * curl -X POST --data "@Temp/command.json" http://localhost:8889/api/command/detailed -v -H "Content-Type: application/json"
	 * 
	 * {
	 *   "id": "3434ADFED54D63564SJFNW",
	 *   "user": "1234567",
	 *   "machine": "TS62IJFMY83J",
	 *   "instruction": "switch-tab"
	 * }
	 * </pre>
	 */
	@POST
	@Path("/detailed")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response complexCommand(Command command) {
		try {
			jmsMessageProducer.sendJmsMessage(command.toString());
			return Response.status(201).entity(command.toString()).build();
		} catch (JMSException e) {
			return Response.status(Status.BAD_GATEWAY).entity(e.getMessage()).build();
		}
	}

}
