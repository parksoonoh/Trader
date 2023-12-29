package trader.trader.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.form.CompanyForm;
import trader.trader.form.HasForm;
import trader.trader.form.SignUpForm;
import trader.trader.form.TradeForm;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Repository
public class HasRepository {
    public String save(HasForm hasForm) throws SQLException {
        String sql = "insert into HAS(USER_ID, COMPANY_ID, HAS_PRICE, QUANTITY) values (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, hasForm.getUserId());
            pstmt.setString(2, hasForm.getCompanyId());
            pstmt.setInt(3, hasForm.getHasPrice());
            pstmt.setInt(4, hasForm.getQuantity());
            pstmt.executeUpdate();
            return "1";
        } catch (SQLException e) {
            log.error("HasRepository save error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public void delete(String userId, String companyId) throws SQLException {
        String sql = "DELETE FROM HAS WHERE USER_ID = ? AND COMPANY_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, companyId);
            pstmt.executeUpdate();
            log.info("HAS deleted Id = " + userId);
        }catch (SQLException e){
            log.error("HASRepository delete error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public void update(HasForm hasForm) throws SQLException {
        String sql = "UPDATE HAS SET QUANTITY = ? WHERE USER_ID = ? AND COMPANY_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, hasForm.getQuantity());
            pstmt.setString(2, hasForm.getUserId());
            pstmt.setString(3, hasForm.getCompanyId());
            pstmt.executeUpdate();
            log.info("HAS Update Id = " + hasForm.getUserId());
        }catch (SQLException e){
            log.error("HasRepository Update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public ArrayList<HasForm> findByUserId(String userId) throws SQLException {
        String sql = "select * from HAS where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            ArrayList<HasForm> allHas = new ArrayList<>();
            while (rs.next()){
                HasForm has = new HasForm();
                has.setUserId(userId);
                has.setCompanyId(rs.getString("COMPANY_ID"));
                has.setHasPrice(rs.getInt("HAS_PRICE"));
                has.setQuantity(rs.getInt("QUANTITY"));
                allHas.add(has);
            }
            return allHas;
        }catch (SQLException e){
            log.error("HasRepository findById error",e);
            throw e;
        }finally {

            close(con, pstmt, rs);
        }
    }

    public int[] findById(String userId, String companyId) throws SQLException {
        String sql = "select * from HAS where user_id = ? AND COMPANY_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(1, companyId);
            rs = pstmt.executeQuery();
            int[] PQ = new int[2];
            if (rs.next()){
                PQ[0] = rs.getInt("HAS_PRICE");
                PQ[0] = rs.getInt("QUANTITY");
            }
            return PQ;
        }catch (SQLException e){
            log.error("HasRepository findById error",e);
            throw e;
        }finally {

            close(con, pstmt, rs);
        }
    }

    public void clear() throws SQLException {
        String sql = "DELETE FROM HAS CASCADE";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            log.info("HASRepository clear");
        }catch (SQLException e){
            log.error("HASRepository clear error",e);
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
