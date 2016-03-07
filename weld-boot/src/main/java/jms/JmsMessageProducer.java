package jms;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import resource.Command;

/**
 * Sends JMS messages to a Queue.
 */
public class JmsMessageProducer {

	private static final boolean TRANSACTIONAL_ON = true;

	@Resource(mappedName = "java:comp/env/jms/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "java:comp/env/jms/CommandQueue")
	private Queue commandQueue;

	/**
	 * Send a command via JMS. Opens and closes the connection per invocation of
	 * this method.
	 * 
	 * @param command
	 * @throws JMSException
	 */
	public void sendCommandMessage(Command command) throws JMSException {
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(TRANSACTIONAL_ON, Session.AUTO_ACKNOWLEDGE);
			final MessageProducer producer = session.createProducer(this.commandQueue);
			final TextMessage newMessage = session.createTextMessage();
			newMessage.setText(command.getInstruction());
			newMessage.setStringProperty("user", command.getUser());
			newMessage.setStringProperty("type", command.getType());
			producer.send(newMessage);
			session.commit();
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

	/**
	 * Drop a JMS message onto a queue. Opens and closes the connection per
	 * invocation of this method.
	 * 
	 * @param text
	 * @throws JMSException
	 */
	public void sendTextMessage(String text) throws JMSException {
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(TRANSACTIONAL_ON, Session.AUTO_ACKNOWLEDGE);

			final MessageProducer producer = session.createProducer(this.commandQueue);
			final TextMessage newMessage = session.createTextMessage();
			newMessage.setText(text);
			producer.send(newMessage);
			session.commit();
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
