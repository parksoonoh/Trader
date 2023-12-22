package trader.trader.algorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import trader.trader.form.CompanyForm;
import trader.trader.form.StickForm;
import trader.trader.repository.CompanyRepository;
import trader.trader.repository.MinStickRepository;
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
    private final MinStickRepository minStickRepository;
    private final ArrayList<StickForm> stickForms;
    private int count = 0;
    @Scheduled(fixedDelay = 1000)
    public void calStock() throws SQLException, IOException {
        ArrayList<CompanyForm> companys = companyRepository.findAllCompany();

        if (stickForms.size() == 0){
            for (CompanyForm companyForm : companys){
                log.info("company list Id = {}, Name = {}",companyForm.getCompanyId(), companyForm.getName());
                StickForm stickForm = new StickForm();
                stickForm.setCompanyId(companyForm.getCompanyId());
                stickForm.setStartPrice(0);
                stickForms.add(stickForm);
            }
        }
        for (int i = 0; i < companys.size(); i++){
            if (stickForms.get(i).getStartPrice() == 0){
                stickForms.get(i).setStartPrice(companys.get(i).getStockPrice());
                stickForms.get(i).setEndPrice(companys.get(i).getStockPrice());
                stickForms.get(i).setHighPrice(companys.get(i).getStockPrice());
                stickForms.get(i).setLowPrice(companys.get(i).getStockPrice());
            }
            double d = new Random().nextInt(61) - 30;
            double rate = (100 + d) / 100.0;
            int newBP = companys.get(i).getStockPrice();
            int newSP = (int)(companys.get(i).getStockPrice() * rate);
            companys.get(i).setStockPrice(newSP);
            companys.get(i).setBeforePrice(newBP);
            companyRepository.update(companys.get(i));
            if (d > 0){

            }
            String message = companys.get(i).getCompanyId() + " " + newSP + " " + d;
            webSocketChatHandler.sendAll(message);

            stickForms.get(i).setEndPrice(companys.get(i).getStockPrice());
            if (stickForms.get(i).getHighPrice() < companys.get(i).getStockPrice()) {
                stickForms.get(i).setHighPrice(companys.get(i).getStockPrice());
            }
            if (stickForms.get(i).getLowPrice() > companys.get(i).getStockPrice()) {
                stickForms.get(i).setLowPrice(companys.get(i).getStockPrice());
            }
        }

        count += 1;
        if (count % 60 == 0){
            insertMinStick();
        }
    }

    private void insertMinStick() throws SQLException, IOException {
        for(StickForm stickForm : stickForms){
            minStickRepository.save(stickForm);
            stickForm.setStartPrice(0);
        }
    }
}
