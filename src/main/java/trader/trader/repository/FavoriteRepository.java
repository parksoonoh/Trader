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
    public void save(String userId, String companyId) throws SQLException {
        String sql = "insert into FAVORITE(USER_ID, COMPANY_ID) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setString(2, companyId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("FavoriteRepository save error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public void delete(String userId, String companyId) throws SQLException {
        String sql = "DELETE FROM FAVORITE WHERE USER_ID = ? AND COMPANY_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, companyId);
            pstmt.executeUpdate();
            log.info("Favorite deleted Id = " + userId);
        }catch (SQLException e){
            log.error("FavoriteRepository delete error",e);
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

    public boolean isExist(String userId, String companyId) throws SQLException {
        String sql = "select * from FAVORITE where user_id = ? AND COMPANY_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, companyId);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return true;
            }
            return false;
        }catch (SQLException e){
            log.error("FavoriteRepository isExist error",e);
            throw e;
        }finally {

            close(con, pstmt, rs);
        }
    }

    public void clear() throws SQLException {
        String sql = "DELETE FROM FAVORITE CASCADE";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            log.info("FAVORITERepository clear");
        }catch (SQLException e){
            log.error("FAVORITERepository clear error",e);
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
