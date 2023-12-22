package trader.trader.form;

import lombok.Data;

@Data
public class OrderForm {
    private long date;
    private int price;
    private int quantity;

    public OrderForm() {
    }

    public OrderForm(long date, int price, int quantity) {
        this.date = date;
        this.price = price;
        this.quantity = quantity;
    }
}
