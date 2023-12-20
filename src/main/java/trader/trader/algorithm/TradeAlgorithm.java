package trader.trader.algorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import trader.trader.form.CompanyForm;
import trader.trader.repository.CompanyRepository;
import trader.trader.trade.WebSocketChatHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class TradeAlgorithm {
    private final WebSocketChatHandler webSocketChatHandler;
    private final CompanyRepository companyRepository;

    @Scheduled(fixedDelay = 1000)
    public void calStock() throws SQLException, IOException {
        ArrayList<CompanyForm> companys = companyRepository.findAllCompany();
        for (CompanyForm companyForm : companys){
            double rate = (100 + new Random().nextInt(61) - 30) / 100.0;
            int newBP = companyForm.getStockPrice();
            int newSP = (int)(companyForm.getStockPrice() * rate);
            companyForm.setStockPrice(newSP);
            companyForm.setBeforePrice(newBP);
            companyRepository.update(companyForm);
            webSocketChatHandler.sendAll(companyForm.getCompanyId(), newSP);
        }
    }
}
