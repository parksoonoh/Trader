package trader.trader.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import trader.trader.repository.SessionInfoRepository;
import trader.trader.repository.UserInfoRepository;
import trader.trader.form.LoginForm;
import trader.trader.form.SignUpForm;

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
    public ResponseEntity<String> signUp(SignUpForm signUpForm) throws SQLException {
        if (userInfoRepository.findPasswordById(signUpForm.getId()) == null){
            signUpForm.setPassword(Hashing(signUpForm.getPassword()));

            userInfoRepository.save(signUpForm);
            log.info("INSERT NEW USER id : " + signUpForm.getId());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        log.info("USER ID Redundancy Error id : " + signUpForm.getId());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> login(LoginForm loginForm) throws SQLException {
        String findPassword = userInfoRepository.findPasswordById(loginForm.getId());

        if (findPassword == null){
            log.info("Id Not Exist id : " + loginForm.getId());
            return new ResponseEntity<>("1",HttpStatus.BAD_REQUEST);
        }

        else if (findPassword.equals(Hashing(loginForm.getPassword()))) {
            log.info("USER LOGIN id : " + loginForm.getId());
            sessionInfoRepository.delete(loginForm.getId());
            String httpSession = UUID.randomUUID().toString();
            sessionInfoRepository.save(loginForm.getId(), httpSession);

            return new ResponseEntity<>('"'+httpSession+'"', HttpStatus.ACCEPTED);
        }

        else {
            log.info("PASSWORD Error id : " + loginForm.getId());
            return new ResponseEntity<>("2", HttpStatus.BAD_REQUEST);
        }
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
