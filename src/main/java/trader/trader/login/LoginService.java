package trader.trader.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import trader.trader.Repository.SessionInfoRepository;
import trader.trader.Repository.UserInfoRepository;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserInfoRepository userInfoRepository;
    private final SessionInfoRepository sessionInfoRepository;

    public String signUp(SignUpForm signUpForm) throws SQLException {
        if (userInfoRepository.findById(signUpForm.getId()).equals("true")){
            signUpForm.setPassword(Hashing(signUpForm.getPassword()));

            userInfoRepository.save(signUpForm);
            log.info("INSERT NEW USER id : " + signUpForm.getId());
            return "새로운 유저 생성됨";
        }
        log.info("USER ID Redundancy Error id : " + signUpForm.getId());
        return "아이디 중복 오류";
    }

    public String login(LoginForm loginForm) throws SQLException {
        if (userInfoRepository.findById(loginForm.getId()).equals(Hashing(loginForm.getPassword()))) {
            log.info("USER LOGIN id : " + loginForm.getId());
            sessionInfoRepository.delete(loginForm.getId());
            String uuid = UUID.randomUUID().toString();
            sessionInfoRepository.save(loginForm.getId(), uuid);
            return uuid;
        }
        log.info("PASSWORD Error id : " + loginForm.getId());
        return "False";
    }

    private String Hashing(String before){
        String sha256 = "";
        try{
            MessageDigest mdSHA256 = MessageDigest.getInstance("SHA-256");
            mdSHA256.update(before.getBytes("UTF-8"));
            byte[] sha256Hash = mdSHA256.digest();

            StringBuffer hexSHA256hash = new StringBuffer();
            for(byte b : sha256Hash){
                String hexString = String.format("%02x",b);
                hexSHA256hash.append(hexString);
            }
            sha256 = hexSHA256hash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return sha256;
    }
}
