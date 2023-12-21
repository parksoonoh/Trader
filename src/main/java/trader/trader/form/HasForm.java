package trader.trader.form;

import lombok.Data;

@Data
public class HasForm {
    private String userId;
    private String companyId;
    private String companyName;
    private int nowPrice;
    private int hasPrice;
    private int quantity;

    public HasForm() {
    }

    public HasForm(String userId, String companyId, String companyName, int nowPrice, int hasPrice, int quantity) {
        this.userId = userId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.nowPrice = nowPrice;
        this.hasPrice = hasPrice;
        this.quantity = quantity;
    }
}
