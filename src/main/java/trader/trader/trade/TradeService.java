package trader.trader.trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import trader.trader.form.HasForm;
import trader.trader.form.LoginForm;
import trader.trader.form.SignUpForm;
import trader.trader.form.TradeForm;
import trader.trader.repository.*;

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
    private final HasRepository hasRepository;
    private final UserInfoRepository userInfoRepository;

    public ResponseEntity<String> sell(TradeForm tradeForm) throws SQLException, IOException {
        int[] restPQ = hasRepository.findById(tradeForm.getUserId(), tradeForm.getCompanyId());
        restPQ[0] *= restPQ[1];
        restPQ[1] -= tradeForm.getQuantity();

        ArrayList<TradeForm> tradeForms = buyRepository.findRightNowTrade(tradeForm);

        for (TradeForm trade : tradeForms){
            String buyMessage = "BUY" + trade.getCompanyId() + trade.getPrice() + trade.getQuantity();
            userInfoRepository.updateMoney(trade.getUserId(), userInfoRepository.findMoneyById(trade.getUserId()) - (trade.getPrice() * trade.getQuantity()));
            HasForm hasForm = new HasForm();
            hasForm.setUserId(trade.getUserId());
            hasForm.setCompanyId(trade.getCompanyId());
            int[] PQ = hasRepository.findById(trade.getUserId(), trade.getCompanyId());
            if (PQ[1] == 0) {
                hasForm.setHasPrice(trade.getPrice());
                hasForm.setQuantity(trade.getQuantity());
                hasRepository.save(hasForm);
            }
            else{
                hasForm.setHasPrice((int)((PQ[0] * PQ[1] + trade.getQuantity() * trade.getPrice()) / (trade.getQuantity() + PQ[1])));
                hasForm.setQuantity(trade.getQuantity() + PQ[1]);
                hasRepository.update(hasForm);
            }
            webSocketChatHandler.sendById(trade.getUserId(), buyMessage);


            String sellMessage = "SELL" + trade.getCompanyId() + trade.getPrice() + trade.getQuantity();
            userInfoRepository.updateMoney(tradeForm.getUserId(), userInfoRepository.findMoneyById(tradeForm.getUserId()) + (trade.getPrice() * trade.getQuantity()));
            webSocketChatHandler.sendById(tradeForm.getUserId(), sellMessage);

            restPQ[0] -= trade.getPrice() * trade.getQuantity();
        }

        if (tradeForm.getQuantity() > 0){
            sellRepository.save(tradeForm);
        }
        restPQ[1] += tradeForm.getQuantity();

        if (restPQ[1] == 0){
            hasRepository.delete(tradeForm.getUserId(), tradeForm.getCompanyId());
        }else{
            HasForm hasForm = new HasForm();
            hasForm.setUserId(tradeForm.getUserId());
            hasForm.setCompanyId(tradeForm.getCompanyId());
            hasForm.setHasPrice((int)(restPQ[0]/restPQ[1]));
            hasForm.setQuantity(restPQ[1]);
            hasRepository.update(hasForm);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public ResponseEntity<String> buy(TradeForm tradeForm) throws SQLException, IOException {
        int[] initQP = hasRepository.findById(tradeForm.getUserId(), tradeForm.getCompanyId());
        initQP[0] *= initQP[1];
        int tempQ = 0;
        ArrayList<TradeForm> tradeForms = sellRepository.findRightNowTrade(tradeForm);

        for (TradeForm trade : tradeForms){
            String sellMessage = "SELL" + trade.getCompanyId() + trade.getPrice() + trade.getQuantity();

            userInfoRepository.updateMoney(trade.getUserId(), userInfoRepository.findMoneyById(trade.getUserId()) + (trade.getPrice() * trade.getQuantity()));

            int[] PQ = hasRepository.findById(trade.getUserId(), trade.getCompanyId());
            if(PQ[1] - trade.getQuantity() == 0){
                hasRepository.delete(trade.getUserId(), trade.getCompanyId());
            }else{
                HasForm hasForm = new HasForm();
                hasForm.setUserId(trade.getUserId());
                hasForm.setCompanyId(trade.getCompanyId());
                hasForm.setHasPrice((int)((PQ[0] * PQ[1] - trade.getPrice() * trade.getQuantity()) / (PQ[1] - trade.getQuantity())));
                hasForm.setQuantity(PQ[1] - trade.getQuantity());
                hasRepository.update(hasForm);
            }

            webSocketChatHandler.sendById(trade.getUserId(), sellMessage);

            String buyMessage = "BUY" + trade.getCompanyId() + trade.getPrice() + trade.getQuantity();
            userInfoRepository.updateMoney(tradeForm.getUserId(), userInfoRepository.findMoneyById(tradeForm.getUserId()) - (trade.getPrice() * trade.getQuantity()));
            initQP[0] += trade.getQuantity() * trade.getPrice();
            tempQ += trade.getQuantity();
            webSocketChatHandler.sendById(tradeForm.getUserId(), buyMessage);
        }

        if (tradeForm.getQuantity() > 0){
            buyRepository.save(tradeForm);
        }

        if (initQP[1] == 0 && tempQ != 0){
            HasForm hasForm = new HasForm();
            hasForm.setUserId(tradeForm.getUserId());
            hasForm.setCompanyId(tradeForm.getCompanyId());
            hasForm.setHasPrice((int)(initQP[0] / tempQ));
            hasForm.setQuantity(tempQ);
            hasRepository.save(hasForm);
        } else if (initQP[1] != 0) {
            HasForm hasForm = new HasForm();
            hasForm.setUserId(tradeForm.getUserId());
            hasForm.setCompanyId(tradeForm.getCompanyId());
            hasForm.setHasPrice((int)(initQP[0] / (initQP[1]+tempQ)));
            hasForm.setQuantity(initQP[1] + tempQ);
            hasRepository.update(hasForm);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
