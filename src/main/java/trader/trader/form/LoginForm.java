package trader.trader.form;

import lombok.Data;

@Data
public class LoginForm {
    private String id;
    private String password;

    public LoginForm(){

    }

    public LoginForm(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
