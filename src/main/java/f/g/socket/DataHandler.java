package f.g.socket;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author gf01867832
 * @since 2024/11/27
 */
@Component
public class DataHandler extends TextWebSocketHandler {

    @Getter
    private LinkedBlockingDeque<String> QUEUE = new LinkedBlockingDeque<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("建立连接");
        QUEUE.clear();
        while (true) {
            try {
                String data = QUEUE.take();
                TextMessage textMessage = new TextMessage(data.getBytes(StandardCharsets.UTF_8));
                session.sendMessage(textMessage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("断开连接");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("接收到消息：" + payload);

        Random random = new Random();
        TextMessage textMessage = new TextMessage(("hello" + random.nextInt(100)).getBytes(StandardCharsets.UTF_8));
        session.sendMessage(textMessage);
    }



}
