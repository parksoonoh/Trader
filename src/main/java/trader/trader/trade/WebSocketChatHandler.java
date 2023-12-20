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
import java.util.List;

@Slf4j
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); //현재 사용자 저장
        log.info("connect with session id :{}, total Session : {}", session.getId(), sessions.size());
    }

    public void sendAll(String companyId, int price) throws IOException {
        TextMessage textMessage = new TextMessage(companyId + price);
        for (WebSocketSession connected : sessions){
            connected.sendMessage(textMessage);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        log.info("Received payload: " + payload);

        for (WebSocketSession connected : sessions){
            connected.sendMessage(message);
        }

        // JSON 문자열로 변환할 데이터 객체 생성
        String serverMessage = "your message successfully send to all user.";

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(serverMessage);

        // 변환된 JSON 문자열을 WebSocket 세션에 전송
        session.sendMessage(new TextMessage(jsonMessage));
    }

    @Override
    //WebSocket 연결이 종료 되었을때
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("closed with session id :{}, total Session : {}", session.getId(), sessions.size());
        sessions.remove(session); //세션을 삭제한다.
    }
}
