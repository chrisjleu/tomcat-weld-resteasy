package websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Command;

@Named("Chat websocket")
@ServerEndpoint(value = "/chat", encoders = { JsonEncoder.class })
public class ChatWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);

    private static final String GUEST_PREFIX = "Guest-";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<ChatWebSocket> connections = new CopyOnWriteArraySet<>();

    private final String nickname;
    private Session session;

    public ChatWebSocket() {
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }

    @OnOpen
    public void start(Session session, @PathParam("user") String user) {
        this.session = session;
        connections.add(this);
        String message = String.format("* %s %s", nickname, "has joined.");
        broadcast(message);
    }

    @OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("* %s %s", nickname, "has disconnected.");
        broadcast(message);
    }

    @OnMessage
    public void incoming(String message) {
        String filteredMessage = String.format("%s: %s", nickname, message);
        broadcast(filteredMessage);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.error("Chat Error: " + t.toString(), t);
    }

    // TODO Use if JSON is the payload
    String constructResponse(String jsonCommand) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Convert JSON to object
            Command command = mapper.readValue(jsonCommand, Command.class);

            // Build JSON like this
            JsonObject event = Json.createObjectBuilder()
                    .add("text", command.getPayload())
                    .add("user", command.getIssuer())
                    .add("timestamp", now())
                    .build();

            return event.toString();
        } catch (IOException e) {
            throw new RuntimeException("Big problem", e);
        }
    }

    String now() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    private static void broadcast(String msg) {
        for (ChatWebSocket client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                logger.debug("Chat Error: Failed to send message to client", e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("* %s %s", client.nickname, "has been disconnected.");
                broadcast(message);
            }
        }
    }
}
