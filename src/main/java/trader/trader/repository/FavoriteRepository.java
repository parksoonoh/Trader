package trader.trader.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.form.CompanyForm;
import trader.trader.form.SignUpForm;
import trader.trader.form.TradeForm;
import trader.trader.form.UserInfoForm;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Repository
public class FavoriteRepository {
    public String save(SignUpForm signUpForm) throws SQLException {
        String sql = "insert into USER_INFO(USER_ID, PASSWORD, NICKNAME, MONEY) values (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, signUpForm.getId());
            pstmt.setString(2, signUpForm.getPassword());
            pstmt.setString(3, signUpForm.getNickname());
            pstmt.setLong(4, 0L);
            pstmt.executeUpdate();

            return "1";
        } catch (SQLException e) {
            log.error("UserInfoRepository save error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public void delete(TradeForm tradeForm) throws SQLException {
        String sql = "DELETE FROM BUY WHERE BDATE = ? AND COMPANY_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, tradeForm.getDate());
            pstmt.setString(2, tradeForm.getCompanyId());
            pstmt.executeUpdate();
            log.info("BUY deleted Id = " + tradeForm.getCompanyId());
        }catch (SQLException e){
            log.error("BuyRepository delete error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public ArrayList<CompanyForm> findById(String userId) throws SQLException {
        String sql = "select * from FAVORITE where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            ArrayList<CompanyForm> companys = new ArrayList<>();
            while (rs.next()){
                CompanyForm company = new CompanyForm();
                company.setCompanyId(rs.getString("COMPANY_ID"));
                companys.add(company);
            }
            return companys;
        }catch (SQLException e){
            log.error("FavoriteRepository findById error",e);
            throw e;
        }finally {

            close(con, pstmt, rs);
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
