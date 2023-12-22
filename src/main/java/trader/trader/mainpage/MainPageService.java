package trader.trader.mainpage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import trader.trader.form.CompanyForm;
import trader.trader.form.LoginForm;
import trader.trader.form.SignUpForm;
import trader.trader.repository.CompanyRepository;
import trader.trader.repository.SessionInfoRepository;
import trader.trader.repository.UserInfoRepository;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainPageService {
    private final CompanyRepository companyRepository;
    public ArrayList<CompanyForm> allcompany() throws SQLException {
        return companyRepository.findAllCompany() ;
    }

}
