package trader.trader.login;

import lombok.Data;

@Data
public class SignUpForm {
    private String id;
    private String password;
    private String nickname;

    public SignUpForm(){

    }

    public SignUpForm(String id, String password, String nickname) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
    }
}
