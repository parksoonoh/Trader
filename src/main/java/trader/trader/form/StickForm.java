package trader.trader.form;

import lombok.Data;

@Data
public class StickForm {
    private String companyId;
    private int startPrice;
    private int endPrice;
    private int highPrice;
    private int lowPrice;

    public StickForm() {
    }

    public StickForm(String companyId, int startPrice, int endPrice, int highPrice, int lowPrice) {
        this.companyId = companyId;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
    }
}
