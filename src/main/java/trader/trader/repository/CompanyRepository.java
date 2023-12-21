package trader.trader.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import trader.trader.connection.DBConnectionUtil;
import trader.trader.form.CompanyForm;
import trader.trader.form.HasForm;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Repository
public class CompanyRepository {
    /*
    @PostConstruct
    public void init() throws SQLException {
        save(new CompanyForm(UUID.randomUUID().toString(), "A전자", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));
        save(new CompanyForm(UUID.randomUUID().toString(), "B철강", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));
        save(new CompanyForm(UUID.randomUUID().toString(), "C화학", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));
        save(new CompanyForm(UUID.randomUUID().toString(), "D제약", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));
        save(new CompanyForm(UUID.randomUUID().toString(), "E뷰티", new Random().nextInt(50000) + 10000, new Random().nextInt(50000) + 10000));

    }
    */
    public String save(CompanyForm companyForm) throws SQLException {
        String sql = "insert into COMPANY(COMPANY_ID, NAME, STOCK_PRICE, BEFORE_PRICE) values (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, companyForm.getCompanyId());
            pstmt.setString(2, companyForm.getName());
            pstmt.setInt(3, companyForm.getStockPrice());
            pstmt.setInt(4, companyForm.getBeforePrice());
            pstmt.executeUpdate();

            return "1";
        } catch (SQLException e) {
            log.error("CompanyRepository save error", e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public ArrayList<CompanyForm> findAllCompany() throws SQLException {
        String sql = "select * from COMPANY";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<CompanyForm> companys = new ArrayList<>();
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                CompanyForm company = new CompanyForm();
                company.setCompanyId(rs.getString("COMPANY_ID"));
                company.setName(rs.getString("NAME"));
                company.setStockPrice(rs.getInt("STOCK_PRICE"));
                company.setBeforePrice(rs.getInt("BEFORE_PRICE"));
                companys.add(company);
            }
            return companys;
        }catch (SQLException e){
            log.error("UserInfoRepository IsUniqueId error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public void update(CompanyForm companyForm) throws SQLException {
        String sql = "UPDATE COMPANY SET STOCK_PRICE = ?, BEFORE_PRICE = ? WHERE COMPANY_ID = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, companyForm.getStockPrice());
            pstmt.setInt(2, companyForm.getBeforePrice());
            pstmt.setString(3, companyForm.getCompanyId());
            pstmt.executeUpdate();
            log.info("Company Update Id = " + companyForm.getCompanyId());
        }catch (SQLException e){
            log.error("CompanyRepository Update error",e);
            throw e;
        }finally {
            close(con, pstmt, null);
        }
    }

    public CompanyForm findById(CompanyForm company) throws SQLException {
        String sql = "select * from COMPANY WHERE COMPANY_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,company.getCompanyId());
            rs = pstmt.executeQuery();
            if (rs.next()){
                company.setName(rs.getString("NAME"));
                company.setStockPrice(rs.getInt("STOCK_PRICE"));
                company.setBeforePrice(rs.getInt("BEFORE_PRICE"));
            }
            return company;
        }catch (SQLException e){
            log.error("CompanyRepository findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public HasForm findHasInfoById(HasForm has) throws SQLException {
        String sql = "select * from COMPANY WHERE COMPANY_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,has.getCompanyId());
            rs = pstmt.executeQuery();
            if (rs.next()){
                has.setCompanyName(rs.getString("NAME"));
                has.setNowPrice(rs.getInt("STOCK_PRICE"));
            }
            return has;
        }catch (SQLException e){
            log.error("CompanyRepository findById error",e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    public int findStockPriceById(String companyId) throws SQLException {
        String sql = "select * from COMPANY WHERE COMPANY_ID = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,companyId);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getInt("STOCK_PRICE");
            }
            return 0;
        }catch (SQLException e){
            log.error("CompanyRepository findStockPriceById error",e);
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
