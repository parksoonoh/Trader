package trader.trader.Repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.login.SignUpForm;

import java.sql.*;

@Slf4j
@Repository
public class SessionInfoRepository {
    public void save(String userId, String uuid) throws SQLException {
        String sql = "insert into SESSION_INFO(USER_ID, UUID, SDATE) values (?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setString(2, uuid);
            pstmt.setLong(3, System.currentTimeMillis());
            pstmt.executeUpdate();
            log.info("CREATE NEW SESSION id : " + userId + " UUID : " + uuid);
        } catch (SQLException e) {
            log.error("SessionInfoRepository save error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public void delete(String userId) throws SQLException {
        String sql = "delete from SESSION_INFO where USER_ID=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);
        } catch (SQLException e) {
            log.info("SessionInfoRepository delete error",e);
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
