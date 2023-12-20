package trader.trader.Repository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.form.CompanyForm;

import java.sql.*;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Repository
public class CompanyRepository {
    /*
    @PostConstruct
    public void init() throws SQLException {
        save(new CompanyForm(UUID.randomUUID().toString(), "A전자", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));
        save(new CompanyForm(UUID.randomUUID().toString(), "B철강", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));
        save(new CompanyForm(UUID.randomUUID().toString(), "C화학", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));
        save(new CompanyForm(UUID.randomUUID().toString(), "D제약", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));
        save(new CompanyForm(UUID.randomUUID().toString(), "E뷰티", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));

    }
    */
    public String save(CompanyForm companyForm) throws SQLException {
        String sql = "insert into COMPANY(COMPANY_ID, NAME, STOCK_PRICE, BEFORE_PRICE) values (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, companyForm.getCompanyId());
            pstmt.setString(2, companyForm.getName());
            pstmt.setInt(3, companyForm.getStockPrice());
            pstmt.setInt(4, companyForm.getBeforePrice());
            pstmt.executeUpdate();

            return "1";
        } catch (SQLException e) {
            log.error("UserInfoRepository save error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs){

        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error",e);
            }
        }

        if (stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null){
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error",e);
            }
        }
    }

    private Connection getConnection(){
        return DBConnectionUtil.getConnection();
    }
}
