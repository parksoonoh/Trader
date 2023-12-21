package trader.trader.trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import trader.trader.form.TradeForm;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    @PostMapping("/api/v1/trade/sell")
    public ResponseEntity<String> sell(@RequestBody TradeForm tradeForm) throws SQLException, IOException {
        return tradeService.sell(tradeForm);
    }

    @PostMapping("/api/v1/trade/buy")
    public ResponseEntity<String> buy(@RequestBody TradeForm tradeForm) throws SQLException, IOException {
        return tradeService.buy(tradeForm);
    }

}
