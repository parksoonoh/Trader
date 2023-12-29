package trader.trader.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.connection.GameConst;
import trader.trader.form.CompanyForm;
import trader.trader.form.OrderForm;
import trader.trader.form.TradeForm;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Repository
public class SellRepository {
    public String save(TradeForm tradeForm) throws SQLException {
        String sql = "insert into SELL(SDATE, USER_ID, COMPANY_ID, SELL_PRICE, QUANTITY) values (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, System.currentTimeMillis());
            pstmt.setString(2, tradeForm.getUserId());
            pstmt.setString(3, tradeForm.getCompanyId());
            pstmt.setInt(4, tradeForm.getPrice());
            pstmt.setInt(5, tradeForm.getQuantity());
            pstmt.executeUpdate();
            log.info("New SELL insert");
            return "1";
        } catch (SQLException e) {
            log.error("SellRepository save error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public ArrayList<TradeForm> findRightNowTrade(TradeForm tradeForm) throws SQLException {
        String sql = "select * from SELL where COMPANY_ID = ? AND SELL_PRICE <= ? ORDER BY SELL_PRICE";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, tradeForm.getCompanyId());
            pstmt.setInt(2, tradeForm.getPrice());
            rs = pstmt.executeQuery();
            ArrayList<TradeForm> tradeForms = new ArrayList<>();
            while (rs.next() && tradeForm.getQuantity() > 0){
                int sellQuantity = rs.getInt("QUANTITY");

                TradeForm trade = new TradeForm();
                trade.setUserId(rs.getString("USER_ID"));
                trade.setCompanyId(rs.getString("COMPANY_ID"));
                trade.setPrice(rs.getInt("SELL_PRICE"));
                trade.setQuantity(sellQuantity);
                if (sellQuantity <= tradeForm.getQuantity()){
                    tradeForm.setQuantity(tradeForm.getQuantity() - sellQuantity);
                    delete(trade);
                }else{
                    trade.setQuantity(sellQuantity - tradeForm.getQuantity());
                    update(trade);
                    trade.setQuantity(tradeForm.getQuantity());
                    tradeForm.setQuantity(0);
                }
                log.info("trading {} to {} price : {} quantity : {}", trade.getUserId(), tradeForm.getUserId(), trade.getPrice(), trade.getQuantity());
                tradeForms.add(trade);
            }
            return tradeForms;
        }catch (SQLException e){
            log.error("SellRepository rightNowTrade error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }


    public void delete(TradeForm tradeForm) throws SQLException {
        String sql = "DELETE FROM SELL WHERE BDATE = ? AND COMPANY_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, tradeForm.getDate());
            pstmt.setString(2, tradeForm.getCompanyId());
            pstmt.executeUpdate();
            log.info("SELL deleted Id = " + tradeForm.getCompanyId());
        }catch (SQLException e){
            log.error("SELLRepository delete error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public void findOrderAllById(String userId, ArrayList<OrderForm> orderForms) throws SQLException {
        String sql = "select * from SELL where USER_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()){
                OrderForm orderForm = new OrderForm();
                orderForm.setDate(rs.getLong("SDATE"));
                orderForm.setPrice(rs.getInt("SELL_PRICE"));
                orderForm.setQuantity(rs.getInt("QUANTITY"));
                orderForms.add(orderForm);
            }
        }catch (SQLException e){
            log.error("SellRepository findOrderAllById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public void findOrderById(String userId, String companyId, ArrayList<OrderForm> orderForms) throws SQLException {
        String sql = "select * from SELL where USER_ID = ? AND COMPANY_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, companyId);
            rs = pstmt.executeQuery();
            while (rs.next()){
                OrderForm orderForm = new OrderForm();
                orderForm.setDate(rs.getLong("SDATE"));
                orderForm.setPrice(rs.getInt("SELL_PRICE"));
                orderForm.setQuantity(rs.getInt("QUANTITY"));
                orderForms.add(orderForm);
            }
        }catch (SQLException e){
            log.error("SellRepository findOrderById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }


    public void update(TradeForm tradeForm) throws SQLException {
        String sql = "UPDATE SELL SET QUANTITY = ? WHERE BDATE = ? AND COMPANY_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, tradeForm.getQuantity());
            pstmt.setLong(2, tradeForm.getDate());
            pstmt.setString(3, tradeForm.getCompanyId());
            pstmt.executeUpdate();
            log.info("SELL Update Id = " + tradeForm.getCompanyId());
        }catch (SQLException e){
            log.error("SELLRepository Update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public void updateInitStock(String companyId, int price) throws SQLException {
        String sql = "UPDATE SELL SET SELL_PRICE = ? WHERE COMPANY_ID = ? AND USER_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, price);
            pstmt.setString(2, companyId);
            pstmt.setString(3, GameConst.managerId);
            pstmt.executeUpdate();
            log.info("SELLInitStock Update Completed");
        }catch (SQLException e){
            log.error("SELLRepository SELLInitStock Update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }
    public void clear() throws SQLException {
        String sql = "DELETE FROM SELL CASCADE";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            log.info("SellRepository clear");
        }catch (SQLException e){
            log.error("SellRepository clear error",e);
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
