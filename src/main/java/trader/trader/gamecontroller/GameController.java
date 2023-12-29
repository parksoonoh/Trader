package trader.trader.gamecontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import trader.trader.repository.*;
import trader.trader.trade.WebSocketChatHandler;

import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class GameController {
    private final UserInfoRepository userInfoRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final CompanyRepository companyRepository;
    private final MinStickRepository minStickRepository;
    private final HasRepository hasRepository;
    private final SellRepository sellRepository;
    private final BuyRepository buyRepository;
    private final FavoriteRepository favoriteRepository;
    private Boolean isStart;

    /*
    @Scheduled(cron = "* 0,30 * * * *")
    public void start(){
        System.out.println("each 30 min");
    }

    @Scheduled(cron = "* 20,50 * * * *")
    public void end(){
        System.out.println("each 10 sec");
    }
    */

    @Scheduled(cron = "0,15,30,45 * * * * *") // 15초에 한번씩 게임 시작 10초 동안 게임
    public void start(){
        isStart = Boolean.TRUE;
        System.out.println("start game");
    }

    @Scheduled(cron = "10,25,40,55 * * * * *") // 5초간 휴식
    public void end() throws SQLException {
        isStart = Boolean.FALSE;
        System.out.println("end game");
        sessionInfoRepository.clear();
        favoriteRepository.clear();
        sellRepository.clear();
        buyRepository.clear();
        hasRepository.clear();
        minStickRepository.clear();
        companyRepository.clear();
        // usererRanking update logic
        userInfoRepository.resetUserMoney();
        companyRepository.makeInitialCompany();
        // 거래량 생성기
    }

     public Boolean getIsStart(){
        return isStart;
     }
}
