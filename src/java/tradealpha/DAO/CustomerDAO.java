package tradealpha.DAO;

import tradealpha.BEAN.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class CustomerDAO 
{
    public static void addCustomer(Connection con, Customer cust) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.customer(loginId, firstName, lastName, gender, dob) values (?,?,?,?,?)";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, cust.getLoginId());
            stmt.setString(2, cust.getFirstName());
            stmt.setString(3, cust.getLastName());
            stmt.setString(4, cust.getGender());
            stmt.setDate(5, java.sql.Date.valueOf(cust.getDob()));

            stmt.executeUpdate();
        }finally{
            con.close();
        }    
    }
    
    
    public static void addCustomerAccount(Connection con, CustomerAccount cust) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.customeraccount(custId, dateModified) values (?,?)";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, cust.getCustId());
            stmt.setDate(2, java.sql.Date.valueOf(cust.getDateModified()));

            stmt.executeUpdate();
            
        }finally{
            con.close();
        }    
    }
    
    public static void addCustomerAddress(Connection con, CustomerContact cust) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.customercontact(custId, phone, address, country) values (?,?,?,?)";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, cust.getCustId());
            stmt.setString(2, cust.getPhone());
            stmt.setString(3, cust.getAddress());
            stmt.setString(4, cust.getCountry());
            
            

            stmt.executeUpdate();
            
        }finally{
            con.close();
        }    
    }
    
    //edit Customer Contact account to database
    public static void editCustomerAddress(Connection con, CustomerContact custCont) throws SQLException{
        
        final String QUERY = "UPDATE `tradesal_alpha-trade`.customercontact SET address = ?, phone = ?, country = ? WHERE custId = ?";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setString(1, custCont.getAddress());
            stmt.setString(2, custCont.getPhone());
            stmt.setString(3, custCont.getCountry());
            stmt.setInt(4, custCont.getCustId());
            
            stmt.executeUpdate();            
        }finally{
            con.close();
        }    
    }
    
    
    
    public static Customer getCustomer(Connection con, Login login) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.customer WHERE loginId = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, login.getId());
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    Customer cust = new Customer();
                    cust.setId(rs.getInt("id"));
                    cust.setLoginId(rs.getInt("loginId"));
                    cust.setDob(rs.getDate("dob").toLocalDate());
                    cust.setGender(rs.getString("gender"));
                    cust.setFirstName(rs.getString("firstName"));
                    cust.setLastName(rs.getString("lastName"));                    

                    return cust;
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
    
    public static int getLoginIdFromCustomerTable(Connection con, int custId) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.customer WHERE id = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, custId);
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("loginId");
                }
            }finally{
                con.close();
            }
        }
        return 0;
    }
    
    public static CustomerContact getCustomerContact (Connection con, Customer customer) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.customercontact WHERE custId = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, customer.getId());
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    CustomerContact cust = new CustomerContact();
                    cust.setId(rs.getInt("id"));
                    cust.setCustId(rs.getInt("custId"));
                    cust.setPhone(rs.getString("phone"));
                    cust.setAddress(rs.getString("address"));
                    cust.setCountry(rs.getString("country"));                    

                    return cust;
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
    
    public static CustomerAccount getCustomerAccount (Connection con, Customer customer) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.customeraccount WHERE custId = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, customer.getId());
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    CustomerAccount cust = new CustomerAccount();
                    cust.setId(rs.getInt("id"));
                    cust.setCustId(rs.getInt("custId"));
                    cust.setBalance(rs.getFloat("balance"));
                    cust.setDateModified(rs.getDate("dateModified").toLocalDate());                    
                    
                    return cust;
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
        
    
    public static boolean addToNewsletter(Connection con, String email) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.newsletter(email, dateCreated) values (?,?)";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setString(1, email);
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));

            return stmt.executeUpdate() > 0;
            
        }finally{
            con.close();
        }    
    }
    
}
