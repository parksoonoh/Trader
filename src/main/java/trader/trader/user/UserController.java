package trader.trader.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trader.trader.form.*;

import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/api/v1/user/info")
    public UserInfoForm info(@RequestParam("userId") String userId) throws SQLException {
        return userService.info(userId);
    }
    @GetMapping("/api/v1/user/favorite")
    public ArrayList<CompanyForm> favorite(@RequestParam("userId") String userId) throws SQLException {
        return userService.favorite(userId);
    }

    @GetMapping("/api/v1/user/has")
    public ArrayList<HasForm> has(@RequestParam("userId") String userId) throws SQLException {
        return userService.has(userId);
    }


}
