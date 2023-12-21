package trader.trader.form;

import lombok.Data;

@Data
public class TradeForm {
    private long date;
    private String userId;
    private String companyId;
    private int price;
    private int quantity;

    public TradeForm() {
    }

    public TradeForm(long date, String userId, String companyId, int price, int quantity) {
        this.date = date;
        this.userId = userId;
        this.companyId = companyId;
        this.price = price;
        this.quantity = quantity;
    }
}
