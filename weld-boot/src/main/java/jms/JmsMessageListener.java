package jms;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Listens to a JMS Queue for incoming messages. Normally this would be a
 * Message-Driven Bean in a fully-fledged JavaEE container.
 */
@ApplicationScoped
public class JmsMessageListener {

    private static final boolean TRANSACTIONAL = false;

    @Resource(mappedName = "java:comp/env/jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:comp/env/jms/CommandQueue")
    private Queue commandQueue;

    /**
     * Listens to a JMS Queue for incoming messages. This needs to be invoked by
     * CDI when the bean is constructed.
     * 
     * @param command
     * @throws JMSException
     */
    @PostConstruct
    public void registerListeners() throws JMSException {
        System.out.println("STARTING"); 
        Connection connection = null;
        Session session = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(TRANSACTIONAL, Session.AUTO_ACKNOWLEDGE);

            session.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        if (!(message instanceof TextMessage))
                            throw new RuntimeException("no text message");
                        TextMessage tm = (TextMessage) message;
                        System.out.println(tm.getText()); // print message
                    } catch (JMSException e) {
                        System.err.println("Error reading message");
                    }
                }
            });

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
