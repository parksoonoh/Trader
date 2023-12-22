package trader.trader.trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import trader.trader.repository.SessionInfoRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Map<String, WebSocketSession> httpWeb = new HashMap<>();
    private final Map<WebSocketSession, String> webHttp = new HashMap<>();
    private final SessionInfoRepository sessionInfoRepository;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String httpSession = session.getUri().getQuery().split("=")[1];
        if (sessionInfoRepository.isValidHttpSession(httpSession) == false){
            log.info("Invalid Session");
            session.sendMessage(new TextMessage("Invalid Session"));
            session.close();
        }
        else {
            sessions.add(session);
            httpWeb.put(httpSession, session);
            webHttp.put(session, httpSession);
            log.info("connect with session id :{}, total Session : {}", session.getId(), sessions.size());
        }
    }

    public void sendAll(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession connected : sessions){
            connected.sendMessage(textMessage);
        }
    }

    public void sendById(String userId, String message) throws IOException, SQLException {
        TextMessage textMessage = new TextMessage(message);
        String userSession = sessionInfoRepository.getHttpSessionByUserId(userId);
        httpWeb.get(userSession).sendMessage(textMessage);
    }

    @Override
    //WebSocket 연결이 종료 되었을때
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("closed with session id :{}, total Session : {}", session.getId(), sessions.size());
        sessions.remove(session); //세션을 삭제한다.
        String httpSession = webHttp.get(session);
        httpWeb.remove(httpSession);
        webHttp.remove(session);
    }
}
