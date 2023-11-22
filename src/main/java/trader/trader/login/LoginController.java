package trader.trader.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/api/v1/auth/signUp")
    public String SignUp(@RequestBody SignUpForm signUpForm) throws SQLException {
        return loginService.signUp(signUpForm);
    }

    @PostMapping("/api/v1/auth/login")
    public String login(@RequestBody LoginForm loginForm) throws SQLException {
        return loginService.login(loginForm);
    }
}
