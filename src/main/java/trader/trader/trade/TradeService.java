package trader.trader.trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import trader.trader.form.LoginForm;
import trader.trader.form.SignUpForm;
import trader.trader.form.TradeForm;
import trader.trader.repository.BuyRepository;
import trader.trader.repository.SellRepository;
import trader.trader.repository.SessionInfoRepository;
import trader.trader.repository.UserInfoRepository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {
    private final SellRepository sellRepository;
    private final BuyRepository buyRepository;
    private final WebSocketChatHandler webSocketChatHandler;
    public ResponseEntity<String> sell(TradeForm tradeForm) throws SQLException, IOException {
        ArrayList<TradeForm> tradeForms = buyRepository.findRightNowTrade(tradeForm);

        for (TradeForm trade : tradeForms){
            String buyMessage = "BUY" + trade.getCompanyId() + trade.getPrice() + trade.getQuantity();
            webSocketChatHandler.sendById(trade.getUserId(), buyMessage);
            String sellMessage = "SELL" + trade.getCompanyId() + trade.getPrice() + trade.getQuantity();
            webSocketChatHandler.sendById(tradeForm.getUserId(), sellMessage);
        }

        if (tradeForm.getQuantity() > 0){
            sellRepository.save(tradeForm);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public ResponseEntity<String> buy(TradeForm tradeForm) throws SQLException, IOException {
        ArrayList<TradeForm> tradeForms = sellRepository.findRightNowTrade(tradeForm);

        for (TradeForm trade : tradeForms){
            String sellMessage = "SELL" + trade.getCompanyId() + trade.getPrice() + trade.getQuantity();
            webSocketChatHandler.sendById(trade.getUserId(), sellMessage);
            String buyMessage = "BUY" + trade.getCompanyId() + trade.getPrice() + trade.getQuantity();
            webSocketChatHandler.sendById(tradeForm.getUserId(), buyMessage);
        }

        if (tradeForm.getQuantity() > 0){
            buyRepository.save(tradeForm);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
