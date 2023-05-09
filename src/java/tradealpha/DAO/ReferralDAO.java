package tradealpha.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import tradealpha.BEAN.*;


public class ReferralDAO 
{
    
    // goes through the login table to check if refCode exists
    public static boolean checkRefCode(Connection con,  String randomValue) throws SQLException, IllegalArgumentException {

        final String QUERY = "SELECT refCode FROM `tradesal_alpha-trade`.login WHERE refCode = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, randomValue);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return true;
                }else{
                    return false;
                }
            }
        } finally {
            con.close();
        }        
    }
    
    //adds a login
    public static boolean addReferral(Connection con, Referral referral) throws SQLException, IllegalArgumentException {
        final String QUERY = "INSERT into `tradesal_alpha-trade`.referrals(refTo, refBy, dateCreated) values (?,?,?)";        

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, referral.getRefTo());
            stmt.setString(2, referral.getRefBy());
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            
            return stmt.executeUpdate() > 0;
            
        } finally {
            con.close();
        }
    }
    
    public static String getReferredBy(Connection con, Login login) throws SQLException, IllegalArgumentException {

        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.referrals WHERE refTo = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, login.getRefCode());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString("refTo").matches(login.getRefCode())) {
                        return rs.getString("refBy");
                    }else{
                        return "N/A";
                    }
                }
            }
        } finally {
            con.close();
        }
        return "N/A";
    }
    
    //adds the create event trigger
    public static boolean plusOneRef(Connection con, Referral referral) throws SQLException, IllegalArgumentException {
        final String QUERY = "UPDATE `tradesal_alpha-trade`.login SET noOfRefs = noOfRefs + 1 WHERE refCode = ?";       

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            
            stmt.setString(1, referral.getRefBy());

            return stmt.executeUpdate() > 0;
            
        } finally {
            con.close();
        }
    }
    
    public static boolean plusOneActiveRef(Connection con, String refCode) throws SQLException, IllegalArgumentException {
        final String QUERY = "UPDATE `tradesal_alpha-trade`.login SET noOfActiveRefs = noOfActiveRefs + 1 WHERE refCode = ?";       

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {            
            stmt.setString(1, refCode);

            return stmt.executeUpdate() > 0;
            
        } finally {
            con.close();
        }
    }
    
    
    public static boolean referralCommission(Connection con, Deposit dep, String ref) throws SQLException, IllegalArgumentException {
        final String QUERY = "UPDATE `tradesal_alpha-trade`.login SET refCommission = refCommission + ? WHERE refCode = ?";       

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {            
            stmt.setDouble(1, (0.1 * dep.getAmount()));
            stmt.setString(2, ref);
            
            return stmt.executeUpdate() > 0;
            
        } finally {
            con.close();
        }
    }
   
    
}
