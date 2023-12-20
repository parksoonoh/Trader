package trader.trader.form;

import lombok.Data;

@Data
public class CompanyForm {
    private String companyId;
    private String name;
    private int stockPrice;
    private int beforePrice;

    public CompanyForm(){

    }

    public CompanyForm(String companyId, String name, int stockPrice, int beforePrice) {
        this.companyId = companyId;
        this.name = name;
        this.stockPrice = stockPrice;
        this.beforePrice = beforePrice;
    }
}
