package tradealpha.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.security.SecureRandom;
import javax.servlet.http.HttpSession;
import tradealpha.BEAN.*;

public class LoginDAO 
{
    //verifies if LOGIN exists and sets the verified boolean variable to true is,
    //else sets it to false and returns the constructor
    public static Login verifyLogin(Connection con, Login login, PBKDF2Hasher hasher) throws SQLException {

        final String QUERY = "SELECT * from `tradesal_alpha-trade`.login WHERE email = ?";
        final String QUERY2 = "SELECT * from `tradesal_alpha-trade`.tradesignalsregister WHERE loginId = ?";
        final String QUERY1 = "SELECT * from `tradesal_alpha-trade`.loginblock WHERE loginId = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, login.getEmail());

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){ 
                    System.out.println(rs.getString("password"));
                    if(hasher.checkPassword(login.getPassword().toCharArray(), rs.getString("password"))){                        
                        login.setId(rs.getInt("id"));
                        System.out.println("hashed password matches");
                        login.setIsVerified(true);
                    }else{
                        System.out.println("hashed password doesn't match");
                        login.setIsVerified(false);
                    }
                }else{
                    System.err.println("No login with these details");
                    login.setIsVerified(false);
                    
                }
            }
            
            //verifies if LOGIN is added to the trade Signals list and sets the verifiedTradeSignal boolean variable to true is,
            //else sets it to false and returns the constructor
            try (PreparedStatement stmt2 = con.prepareStatement(QUERY2)) {
                stmt2.setInt(1, login.getId());

                try (ResultSet rs = stmt2.executeQuery()) {
                    if(rs.next()){
                        if(rs.getString("status").matches("PENDING")){
                            login.setVerifiedTrade(false);
                        }else{
                            login.setVerifiedTrade(true);                        
                            login.setTradeSignalType(rs.getString("signalType"));                        
                        }                        
                    }else{
                        login.setVerifiedTrade(false);
                    }
                }
            }
            
            System.out.println(login.getId());
            //verifies if LOGIN is added to the blocked list and sets the blocked boolean variable to true is,
            //else sets it to false and returns the constructor
            try (PreparedStatement stmt1 = con.prepareStatement(QUERY1)) {
                stmt1.setInt(1, login.getId());

                try (ResultSet rs = stmt1.executeQuery()) {
                    if(rs.next()){
                        login.setBlocked(true);
                    }else{
                        login.setBlocked(false);
                    }
                }
            }
            
                                    
            finally {
                con.close();
            }
        } 
        
        return login;

    }
    
    
    //Verifies if the login is blocked by checking the boolean e value
    //if yes unblocks the login Id by removing it from the block table in database
    //if no it blocs the login Id by ading it to the blocked database
    public static boolean isBlocked(Connection con, Login login) throws SQLException {
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.loginblock(loginId, dateBlocked) values (?,?)";
        final String QUERY1 = "DELETE from `tradesal_alpha-trade`.loginblock WHERE loginId = ?";
        
        if(login.isBlocked()){
            try (PreparedStatement stmt = con.prepareStatement(QUERY1)) {
                stmt.setInt(1, login.getId());
                                
                return stmt.executeUpdate() > 0;
                
            } finally {
                con.close();
            }
        }else{            
            try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
                stmt.setInt(1, login.getId());
                stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                
                return stmt.executeUpdate() > 0;
                
            } finally {
                con.close();
            }
            
        }
    }
    
    //returns the entire login constructor 
    public static String checkRole(Connection con, Login login) throws SQLException {

        final String QUERY = "SELECT role from `tradesal_alpha-trade`.login WHERE email = ?";
        
        
        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, login.getEmail());            

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
            }
        }finally{
            con.close();
        }
        return null;
        }
    }
    
    //returns the entire login constructor 
    public static Login getLogin(Connection con, Login login, PBKDF2Hasher hasher) throws SQLException {

        final String QUERY = "SELECT * from `tradesal_alpha-trade`.login WHERE email = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, login.getEmail());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    if(hasher.checkPassword(login.getPassword().toCharArray(), rs.getString("password"))){
                        login.setId(rs.getInt("id"));
                        login.setEmail(rs.getString("email"));
                        login.setPassword(rs.getString("password"));
                        login.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                        login.setDateModified(rs.getDate("dateModified").toLocalDate());                                        
                        login.setRefCode(rs.getString("refCode"));
                        login.setNoOfRefs(rs.getInt("noOfRefs"));
                        login.setNoOfActiveRefs(rs.getInt("noOfActiveRefs"));
                        login.setRefCommission(rs.getFloat("refCommission"));
                        login.setRole(rs.getString("role"));
                        login.setAccStatus(rs.getString("accStatus"));
                        login.setLastLogin(rs.getDate("lastLogin").toLocalDate());
                    }else{
                        return null;
                    }
                }
            } finally {
                con.close();
            }
        }

        return login;
    }
    
    //returns the entire login constructor 
    public static Login getLoginById(Connection con, int loginId) throws SQLException {

        final String QUERY = "SELECT * from `tradesal_alpha-trade`.login WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setInt(1, loginId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Login login = new Login();
                    login.setId(rs.getInt("id"));
                    login.setEmail(rs.getString("email"));
                    login.setPassword(rs.getString("password"));
                    login.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                    login.setDateModified(rs.getDate("dateModified").toLocalDate());                                        
                    login.setRefCode(rs.getString("refCode"));
                    login.setNoOfRefs(rs.getInt("noOfRefs"));
                    login.setNoOfActiveRefs(rs.getInt("noOfActiveRefs"));
                    login.setRefCommission(rs.getFloat("refCommission"));
                    login.setRole(rs.getString("role"));
                    login.setAccStatus(rs.getString("accStatus"));
                    login.setLastLogin(rs.getDate("lastLogin").toLocalDate());
                    return login;
                }
            } finally {
                con.close();
            }
        }

        return null;
    }
    
    //adds a login
    public static void addLogin(Connection con, Login login, PBKDF2Hasher hasher) throws SQLException, IllegalArgumentException {
        final String QUERY = "INSERT into `tradesal_alpha-trade`.login(email, password, dateCreated, dateModified, refCode, lastLogin, role) values (?,?,?,?,?,?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, login.getEmail());
            stmt.setString(2, hasher.hash(login.getPassword().toCharArray()));
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(5, login.getRefCode());
            stmt.setDate(6, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(7, "CUSTOMER");

            stmt.executeUpdate();
        } finally {
            con.close();
        }
    }
    
    //delete a login
    public static void deleteLogin(Connection con, Login login) throws SQLException, IllegalArgumentException {
        final String QUERY = "DELETE FROM `tradesal_alpha-trade`.login WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setInt(1, login.getId());
            stmt.executeUpdate();
        } finally {
            con.close();
        }
    }
    
    
    public static void updateDateModified(Connection con, Login login) throws SQLException, IllegalArgumentException {

        final String QUERY = "UPDATE `tradesal_alpha-trade`.login SET dateModified = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setInt(2, login.getId());

            stmt.executeUpdate();
            
        } finally {
            con.close();
        }
    }
    
    public static void updatePassword(Connection con, Login login) throws SQLException, IllegalArgumentException {

        final String QUERY = "UPDATE `tradesal_alpha-trade`.login SET password = ?, dateModified = ? WHERE email = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, login.getPassword());            
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));            
            stmt.setString(3, login.getEmail());

            stmt.executeUpdate();

        } finally {
            con.close();
        }
    }
    
    public static String createVerificationCode() {
        SecureRandom random = new SecureRandom();
        int randomValue = 100000 + random.nextInt(99999);

        return String.valueOf(randomValue);
    }
     
    public static String checkVerificationCode(Connection con, HttpSession session, String randomValue) {

        final String QUERY = "SELECT refCode FROM `tradesal_alpha-trade`.login";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString("refCode").matches(randomValue)) {
                        randomValue = createVerificationCode();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error occured: " + e);
            session.setAttribute("dbError", true);
        }
        return randomValue;
    }
    
    //gets all the orders of all customers
    public static ArrayList<Login> getLoginList(Connection con) throws SQLException, NullPointerException{
      
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.login WHERE role = 'CUSTOMER'";
        final String QUERY1 = "SELECT * FROM `tradesal_alpha-trade`.customer WHERE loginId = ?";
        final String QUERY2 = "SELECT * FROM `tradesal_alpha-trade`.customeraccount WHERE custId = ?";
        final String QUERY3 = "SELECT * FROM `tradesal_alpha-trade`.customercontact WHERE custId = ?";
        final String QUERY4 = "SELECT * FROM `tradesal_alpha-trade`.loginblock WHERE loginId = ?";
        ArrayList<Login> list = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement(QUERY);        
        try(ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                Login login = new Login();
                login.setId(rs.getInt("id"));
                login.setEmail(rs.getString("email"));
                login.setPassword(rs.getString("password"));
                login.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                login.setDateModified(rs.getDate("dateModified").toLocalDate());                                    
                login.setRefCode(rs.getString("refCode"));
                login.setNoOfRefs(rs.getInt("noOfRefs"));
                login.setNoOfActiveRefs(rs.getInt("noOfActiveRefs"));
                login.setRefCommission(rs.getFloat("refCommission"));
                login.setRole(rs.getString("role"));
                login.setLastLogin(rs.getDate("lastLogin").toLocalDate());
                login.setAccStatus(rs.getString("accStatus"));
                System.out.println("Login constructor filled");
                
                try (PreparedStatement stmt4 = con.prepareStatement(QUERY4)) {
                    stmt4.setInt(1, login.getId());

                    try (ResultSet rs4 = stmt4.executeQuery()) {
                        if(rs4.next()){
                            login.setBlocked(true);
                        }else{
                            System.out.println("Login Id " + login.getId() + " is blocked is " + login.isBlocked());
                            login.setBlocked(false);
                        }
                    }
                }                

                try (PreparedStatement stmt1 = con.prepareStatement(QUERY1)) {
                    stmt1.setInt(1, login.getId());

                    try (ResultSet rs1 = stmt1.executeQuery()) {
                        while(rs1.next()){
                            Customer cust = new Customer();
                            cust.setId(rs1.getInt("id"));
                            cust.setLoginId(rs1.getInt("loginId"));
                            cust.setDob(rs1.getDate("dob").toLocalDate());
                            cust.setGender(rs1.getString("gender"));
                            cust.setFirstName(rs1.getString("firstName"));
                            cust.setLastName(rs1.getString("lastName"));

                            login.setCustomer(cust);
                            System.out.println("Customer constructor filled");
                        }
                    }
                }

                try (PreparedStatement stmt2 = con.prepareStatement(QUERY2)) {
                    stmt2.setInt(1, login.getCustomer().getId());

                    try (ResultSet rs2 = stmt2.executeQuery()) {
                        while(rs2.next()){
                            CustomerAccount cust = new CustomerAccount();
                            cust.setId(rs2.getInt("id"));
                            cust.setCustId(rs2.getInt("custId"));
                            cust.setBalance(rs2.getFloat("balance"));
                            cust.setDateModified(rs2.getDate("dateModified").toLocalDate());

                            login.getCustomer().setCustAcc(cust);
                            System.out.println("Customer Account constructor filled");
                        }
                    }
                }

                try (PreparedStatement stmt3 = con.prepareStatement(QUERY3)) {
                    stmt3.setInt(1, login.getCustomer().getId());

                    try (ResultSet rs3 = stmt3.executeQuery()) {
                        while(rs3.next()){
                            CustomerContact cust = new CustomerContact();
                            cust.setId(rs3.getInt("id"));
                            cust.setCustId(rs3.getInt("custId"));
                            cust.setPhone(rs3.getString("phone"));
                            cust.setAddress(rs3.getString("address"));
                            cust.setCountry(rs3.getString("country"));

                            login.getCustomer().setCustCont(cust);
                            System.out.println("Customer Contact constructor filled");
                        }
                    }
                }

                System.out.println("Login " + login.getId() + " added to 1");

                list.add(login);
            }
            
            
        }
        finally{
            con.close();
        }
        return list;
        
    }
    
    //gets all the orders of all customers
    public static ArrayList<Login> getLoggedInUsers(Connection con) throws SQLException, NullPointerException{
      
        final String QUERY = "SELECT id FROM `tradesal_alpha-trade`.login WHERE loggedIn = 1";
        final String QUERY1 = "SELECT firstName, lastName FROM `tradesal_alpha-trade`.customer WHERE loginId = ?";
        int i = 1;
        ArrayList<Login> list = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement(QUERY);        
        try(ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                Login login = new Login();
                login.setId(rs.getInt("id"));                

                try (PreparedStatement stmt1 = con.prepareStatement(QUERY1)) {
                    stmt1.setInt(1, login.getId());

                    try (ResultSet rs1 = stmt1.executeQuery()) {
                        while(rs1.next()){
                            Customer cust = new Customer();
                            cust.setId(i);
                            
                            //increments the value of i everytime
                            i++;
                            
                            cust.setFirstName(rs1.getString("firstName"));
                            cust.setLastName(rs1.getString("lastName"));

                            login.setCustomer(cust);
                            System.out.println("Customer constructor filled");
                        }
                    }
                }
                System.out.println("Login " + login.getId() + " added");
                list.add(login);
            }
        }
        finally{
            con.close();
        }
        return list;
        
    }
    
    public static void modifyLastLogin(Connection con, Login login) throws SQLException, NullPointerException{

        final String QUERY = "UPDATE `tradesal_alpha-trade`.login SET lastLogin = ? WHERE email = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {            
            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(2, login.getEmail());            
            
            stmt.executeUpdate();
            
        } finally {
            con.close();
        }
    }
    
    public static void logIn(Connection con, Login login) throws SQLException, NullPointerException{

        final String QUERY = "UPDATE `tradesal_alpha-trade`.login SET loggedIn = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {            
            stmt.setInt(1, 1);
            stmt.setInt(2, login.getId());            
            
            stmt.executeUpdate();
            
        } finally {
            con.close();
        }
    }
    
    public static void logOut(Connection con, Login login) throws SQLException, NullPointerException{

        final String QUERY = "UPDATE `tradesal_alpha-trade`.login SET loggedIn = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {            
            stmt.setInt(1, 0);
            stmt.setInt(2, login.getId());            
            
            stmt.executeUpdate();
            
        } finally {
            con.close();
        }
    }
    
}
