package jms;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import resource.Command;

/**
 * Sends JMS messages to a Queue. The JMS connection and session are kept alive
 * for the lifetime of the application.
 */
@ApplicationScoped
public class JmsMessageProducer {

    private static final boolean TRANSACTIONAL = false;

    @Resource(mappedName = "java:comp/env/jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:comp/env/jms/CommandQueue")
    private Queue commandQueue;

    Connection connection = null;
    Session session = null;

    @PostConstruct
    void establishConnection() throws JMSException {
        this.connection = connectionFactory.createConnection();
        this.session = connection.createSession(TRANSACTIONAL, Session.AUTO_ACKNOWLEDGE);
        // No MDBs so must resort to this
        session.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                try {
                    if (!(message instanceof TextMessage))
                        throw new RuntimeException("no text message");
                    TextMessage tm = (TextMessage) message;
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                    System.out.println(tm.getText()); // print message
                } catch (JMSException e) {
                    System.err.println("Error reading message");
                }
            }
        });

    }

    @PreDestroy
    void closeConnection() throws JMSException {
        if (connection != null) {
            if (session != null) {
                session.close();
            }
            connection.stop();
            connection.close();
        }
    }

    /**
     * Send a command via JMS.
     * 
     * <p>
     * Note: Opens and closes the connection per invocation of this method
     * which, when run outside a JavaEE container, is not very efficient.
     * </p>
     * 
     * @param command
     * @throws JMSException
     */
    public void sendCommandMessage(Command command) throws JMSException {
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
    }

}
