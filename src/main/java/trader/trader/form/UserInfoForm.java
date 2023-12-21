package trader.trader.form;

import lombok.Data;

@Data
public class UserInfoForm {
    private String userName;
    private int money;

    public UserInfoForm() {
    }

    public UserInfoForm(String userName) {
        this.userName = userName;
    }
}
