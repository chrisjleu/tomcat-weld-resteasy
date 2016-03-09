package jms;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import resource.Command;

/**
 * Sends JMS messages to a Queue.
 */
public class JmsMessageProducer {

	private static final boolean TRANSACTIONAL = false;

	@Resource(mappedName = "java:comp/env/jms/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:comp/env/jms/CommandQueue")
	private Queue commandQueue;

	/**
	 * Send a command via JMS.
	 * 
	 * <p>
	 * Note: Opens and closes the connection per invocation of this method which,
	 * when run outside a JavaEE container, is not very efficient.
	 * </p>
	 * 
	 * @param command
	 * @throws JMSException
	 */
	public void sendCommandMessage(Command command) throws JMSException {
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(TRANSACTIONAL, Session.AUTO_ACKNOWLEDGE);

			// Construct a JMS "TextMessage"
			final TextMessage newMessage = session.createTextMessage();
			newMessage.setStringProperty("issuer", command.getIssuer());
			newMessage.setStringProperty("type", command.getType());
			newMessage.setText(command.getPayload());

			// Send the message
			final MessageProducer producer = session.createProducer(this.commandQueue);
			producer.send(newMessage);

			if (TRANSACTIONAL) {
				// JavaEE containers would manage this
				session.commit();
			}
		} finally {
			if (connection != null) {
				try {
					if (session != null) {
						session.close();
					}
					connection.stop();
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
