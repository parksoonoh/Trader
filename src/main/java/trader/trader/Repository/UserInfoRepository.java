package trader.trader.Repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.login.SignUpForm;

import java.sql.*;

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

    public String findById(String user_id) throws SQLException {
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
                return "true";
            }
        }catch (SQLException e){
            log.error("UserInfoRepository IsUniqueId error",e);
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
