package trader.trader.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.form.CompanyForm;
import trader.trader.form.SignUpForm;
import trader.trader.form.UserInfoForm;

import java.sql.*;

import static trader.trader.connection.GameConst.initialMoney;

@Slf4j
@Repository
public class UserInfoRepository {
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

    public String findPasswordById(String user_id) throws SQLException {
        String sql = "select * from USER_INFO where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString("password");
            }else{
                return null;
            }
        }catch (SQLException e){
            log.error("UserInfoRepository IsUniqueId error",e);
            throw e;
        }finally {

            close(con, pstmt, rs);
        }
    }


    public int findMoneyById(String user_id) throws SQLException {
        String sql = "select * from USER_INFO where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getInt("MONEY");
            }
            return 0;
        }catch (SQLException e){
            log.error("UserInfoRepository IsUniqueId error",e);
            throw e;
        }finally {

            close(con, pstmt, rs);
        }
    }
    public void updateMoney(String userId, int money) throws SQLException {
        String sql = "UPDATE USER_INFO SET MONEY = ? WHERE USER_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
            log.info("USER_INFO Update Id = " + userId);
        }catch (SQLException e){
            log.error("UserInfoRepository Update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public UserInfoForm findInfoById(String userId) throws SQLException {
        String sql = "select * from USER_INFO where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            UserInfoForm userInfoForm = new UserInfoForm();
            if (rs.next()){
                userInfoForm.setUserName(rs.getString("NICKNAME"));
                userInfoForm.setMoney(rs.getInt("MONEY"));
            }
            return userInfoForm;
        }catch (SQLException e){
            log.error("UserInfoRepository findInfoById error",e);
            throw e;
        }finally {

            close(con, pstmt, rs);
        }
    }

    public void resetUserMoney() throws SQLException {
        String sql = "UPDATE USER_INFO SET MONEY = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, initialMoney);
            pstmt.executeUpdate();
            log.info("USER Money Initialized");
        }catch (SQLException e){
            log.error("UserInfoRepository InitialMoney error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
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
