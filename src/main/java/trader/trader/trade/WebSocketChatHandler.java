package trader.trader.trade;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); //현재 사용자 저장
        log.info("connect with session id :{}, total Session : {}", session.getId(), sessions.size());
    }

    public void sendAll(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession connected : sessions){
            connected.sendMessage(textMessage);
        }
    }

    public void sendById(String userId, String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession connected : sessions){
            connected.sendMessage(textMessage);
        }
    }

    @Override
    //WebSocket 연결이 종료 되었을때
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("closed with session id :{}, total Session : {}", session.getId(), sessions.size());
        sessions.remove(session); //세션을 삭제한다.
    }
}
