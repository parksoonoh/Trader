package trader.trader.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import trader.trader.form.*;
import trader.trader.repository.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserInfoRepository userInfoRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final FavoriteRepository favoriteRepository;
    private final CompanyRepository companyRepository;
    private final HasRepository hasRepository;
    public UserInfoForm info(String userId) throws SQLException {
        return userInfoRepository.findInfoById(userId);
    }

    public ArrayList<CompanyForm> favorite(String userId) throws SQLException {
        ArrayList<CompanyForm> companys = favoriteRepository.findById(userId);
        for (CompanyForm company : companys){
            companyRepository.findById(company);
        }
        return companys;
    }

    public ArrayList<HasForm> has(String userId) throws SQLException {
        ArrayList<HasForm> allHas = hasRepository.findById(userId);
        for (HasForm has : allHas){
            companyRepository.findHasInfoById(has);
        }
        return allHas;
    }
}
