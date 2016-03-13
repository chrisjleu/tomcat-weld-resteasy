package resource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import domain.Command;
import jms.JmsMessageProducer;

/**
 * A Restful API for issuing a "command" that results in sending a JMS message. Consumes JSON message bodies.
 */
@Named("Command Resource")
@Path("command")
@RequestScoped
public class CommandResource {

	@Inject
	private JmsMessageProducer jmsMessageProducer;

	/**
	 * Send command.
	 * 
	 * <pre>
	 * curl -v -X POST -H "Content-Type: application/json --data "@/cygdrive/c/Temp/command.json" http://localhost:8889/api/command"
	 * 
	 * {
	 *   "issuer": "1234567",
	 *   "type": "Chat",
	 *   "payload": "Hello World!"
	 * }
	 * </pre>
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response issue(Command command) {
		if (command == null) {
			return Response.status(Status.BAD_REQUEST).entity("Missing command").build();
		}

		try {
			jmsMessageProducer.sendCommandMessage(command);
			return Response.status(201).entity(command.toString()).build();
		} catch (JMSException e) {
			return Response.status(Status.BAD_GATEWAY).entity(e.getMessage()).build();
		}
	}

}
