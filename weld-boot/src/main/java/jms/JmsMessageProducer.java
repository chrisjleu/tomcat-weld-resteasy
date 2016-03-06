package jms;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Sends JMS messages to a Queue.
 */
public class JmsMessageProducer {

	private static final boolean TRANSACTIONAL_ON = true;

	@Resource(mappedName = "java:comp/env/jms/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:comp/env/jms/EventInQueue")
	private Queue eventInQueue;

	/**
	 * Drop a JMS message onto a queue. Opens and closes the connection per
	 * invocation of this method.
	 * 
	 * @param message
	 * @throws JMSException
	 */
	public void sendJmsMessage(String message) throws JMSException {
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(TRANSACTIONAL_ON, Session.AUTO_ACKNOWLEDGE);

			final MessageProducer producer = session.createProducer(this.eventInQueue);
			final TextMessage newMessage = session.createTextMessage();
			newMessage.setText(message);
			producer.send(newMessage);
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
