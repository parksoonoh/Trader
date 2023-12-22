package trader.trader.mainpage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import trader.trader.form.CompanyForm;
import trader.trader.form.LoginForm;
import trader.trader.form.SignUpForm;
import trader.trader.login.LoginService;

import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;

    @GetMapping("/api/v1/mainpage/allcompany")
    public ArrayList<CompanyForm> allcompany() throws SQLException {
        return mainPageService.allcompany();
    }

}
