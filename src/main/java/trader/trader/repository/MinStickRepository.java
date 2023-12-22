package trader.trader.repository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.form.StickForm;
import trader.trader.form.TradeForm;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Repository
public class MinStickRepository {

    @PostConstruct
    public void clear() throws SQLException {
        String sql = "DELETE FROM MIN_STICK CASCADE";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            log.info("MinStickRepository clear");
        }catch (SQLException e){
            log.error("MinStickRepository clear error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public String save(StickForm stickForm) throws SQLException {
        String sql = "insert into MIN_STICK(MDATE, COMPANY_ID, START_PRICE, END_PRICE, HIGH_PRICE, LOW_PRICE) " +
                "values (?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, System.currentTimeMillis());
            pstmt.setString(2, stickForm.getCompanyId());
            pstmt.setInt(3, stickForm.getStartPrice());
            pstmt.setInt(4, stickForm.getEndPrice());
            pstmt.setInt(5, stickForm.getHighPrice());
            pstmt.setInt(6, stickForm.getLowPrice());
            pstmt.executeUpdate();
            log.info("New MinStick insert id = {}", stickForm.getCompanyId());
            return "1";
        } catch (SQLException e) {
            log.error("MinStickRepository save error", e);
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
