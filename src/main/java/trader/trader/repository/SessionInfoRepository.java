package trader.trader.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.form.OrderForm;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Repository
public class SessionInfoRepository {
    public void save(String userId, String httpSession) throws SQLException {
        String sql = "insert into SESSION_INFO(USER_ID, HTTP_SESSION, WEB_SESSION, SDATE) values (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setString(2, httpSession);
            pstmt.setString(3, null);
            pstmt.setLong(4, System.currentTimeMillis());
            pstmt.executeUpdate();
            log.info("CREATE NEW SESSION id : " + userId + " HTTP SESSION : " + httpSession);
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

    public String getHttpSessionByUserId(String userId) throws SQLException {
        String sql = "select * from SESSION_INFO where USER_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString("HTTP_SESSION");
            }
            return null;
        }catch (SQLException e){
            log.error("SessionInfoRepository getHttpSessionByUserId error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public String getUserIdByHttpSession(String session) throws SQLException {
        String sql = "select * from SESSION_INFO where HTTP_SESSION = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, session);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getString("USER_ID");
            }
            return null;
        }catch (SQLException e){
            log.error("SessionInfoRepository getUserIdByHttpSession error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public boolean isValidHttpSession(String session) throws SQLException {
        String sql = "select * from SESSION_INFO where HTTP_SESSION = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, session);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return true;
            }
            return false;
        }catch (SQLException e){
            log.error("SessionInfoRepository isValidHttpSession error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public void clear() throws SQLException {
        String sql = "DELETE FROM SESSION_INFO CASCADE";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            log.info("SessionInfoRepository clear");
        }catch (SQLException e){
            log.error("SessionInfoRepository clear error",e);
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
