package trader.trader.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.form.CompanyForm;
import trader.trader.form.OrderForm;
import trader.trader.form.TradeForm;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Repository
public class BuyRepository {
    public String save(TradeForm tradeForm) throws SQLException {
        String sql = "insert into BUY(BDATE, USER_ID, COMPANY_ID, BUY_PRICE, QUANTITY) values (?, ?, ?, ?, ?)";

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
            log.info("New BUY insert");
            return "1";
        } catch (SQLException e) {
            log.error("BuyRepository save error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public ArrayList<TradeForm> findRightNowTrade(TradeForm tradeForm) throws SQLException {
        String sql = "select * from BUY where COMPANY_ID = ? AND BUY_PRICE <= ? ORDER BY BUY_PRICE";

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
                int buyQuantity = rs.getInt("QUANTITY");

                TradeForm trade = new TradeForm();
                trade.setUserId(rs.getString("USER_ID"));
                trade.setCompanyId(rs.getString("COMPANY_ID"));
                trade.setPrice(rs.getInt("BUY_PRICE"));
                trade.setQuantity(buyQuantity);
                if (buyQuantity <= tradeForm.getQuantity()){
                    tradeForm.setQuantity(tradeForm.getQuantity() - buyQuantity);
                    delete(trade);
                }else{
                    trade.setQuantity(buyQuantity - tradeForm.getQuantity());
                    update(trade);
                    trade.setQuantity(tradeForm.getQuantity());
                    tradeForm.setQuantity(0);
                }
                log.info("trading {} to {} price : {} quantity : {}", tradeForm.getUserId(), trade.getUserId(), trade.getPrice(), trade.getQuantity());
                tradeForms.add(trade);
            }
            return tradeForms;
        }catch (SQLException e){
            log.error("BuyRepository rightNowTrade error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
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

    public void update(TradeForm tradeForm) throws SQLException {
        String sql = "UPDATE BUY SET QUANTITY = ? WHERE BDATE = ? AND COMPANY_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, tradeForm.getQuantity());
            pstmt.setLong(2, tradeForm.getDate());
            pstmt.setString(3, tradeForm.getCompanyId());
            pstmt.executeUpdate();
            log.info("BUY Update Id = " + tradeForm.getCompanyId());
        }catch (SQLException e){
            log.error("BuyRepository Update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public int findCostById(String userId) throws SQLException {
        String sql = "select * from BUY where USER_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            int total = 0;
            while (rs.next()){
                total += rs.getInt("BUY_PRICE") * rs.getInt("QUANTITY");
            }
            return total;
        }catch (SQLException e){
            log.error("BuyRepository rightNowTrade error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public void findOrderAllById(String userId, ArrayList<OrderForm> orderForms) throws SQLException {
        String sql = "select * from BUY where USER_ID = ?";

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
                orderForm.setDate(rs.getLong("BDATE"));
                orderForm.setPrice(rs.getInt("BUY_PRICE"));
                orderForm.setQuantity(rs.getInt("QUANTITY"));
                orderForms.add(orderForm);
            }
        }catch (SQLException e){
            log.error("BuyRepository findOrderAllById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public void findOrderById(String userId, String companyId, ArrayList<OrderForm> orderForms) throws SQLException {
        String sql = "select * from BUY where USER_ID = ? AND COMPANY_ID = ?";

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
                orderForm.setDate(rs.getLong("BDATE"));
                orderForm.setPrice(rs.getInt("BUY_PRICE"));
                orderForm.setQuantity(rs.getInt("QUANTITY"));
                orderForms.add(orderForm);
            }
        }catch (SQLException e){
            log.error("BuyRepository findOrderById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public void clear() throws SQLException {
        String sql = "DELETE FROM BUY CASCADE";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();
            log.info("BUYRepository clear");
        }catch (SQLException e){
            log.error("BUYRepository clear error",e);
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
