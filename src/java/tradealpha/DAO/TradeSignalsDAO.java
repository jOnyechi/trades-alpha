package tradealpha.DAO;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import tradealpha.BEAN.*;


public class TradeSignalsDAO 
{
    public static boolean addTradeSignal(Connection con, TradeSignals tradeSignal) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.tradesignals(entryPoint, stopLoss, tp1, tp2, currencyPair, orderType, signalType, dateCreated) values (?,?,?,?,?,?,?,?)";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setFloat(1, Float.parseFloat(tradeSignal.getEntryPoint()));
            stmt.setFloat(2, Float.parseFloat(tradeSignal.getStopLoss()));
            stmt.setFloat(3, Float.parseFloat(tradeSignal.getTp1()));            
            stmt.setFloat(4, Float.parseFloat(tradeSignal.getTp2()));
            stmt.setString(5, tradeSignal.getCurrencyPair());
            stmt.setString(6, tradeSignal.getOrderType());
            stmt.setString(7, tradeSignal.getSignalType());
            stmt.setDate(8, java.sql.Date.valueOf(LocalDate.now()));

            return stmt.executeUpdate() > 0;
            
        }finally{
            con.close();
        }    
    }
    
    public static String getSignalTypeOfUser(Connection con, int loginId) throws SQLException, NullPointerException{
        
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.tradesignalsregister WHERE loginId = ?";        
        System.out.println("created query string");
        PreparedStatement stmt = con.prepareStatement(QUERY);
        stmt.setInt(1, loginId);
        
        try(ResultSet rs = stmt.executeQuery()){
            if(rs.next()){
               System.out.println("gotten signal type");
               return rs.getString("signalType");                 
            }
        }
        
        return "N/A";
    }
    
    public static ArrayList<TradeSignals> getTradeSignals(Connection con, String type) throws SQLException, NullPointerException{
      
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.tradesignals WHERE signalType = ?";        
        
        ArrayList<TradeSignals> list = new ArrayList<>();
        System.out.println("created all objects needed for the get tradeSignals");
        
        PreparedStatement stmt = con.prepareStatement(QUERY); 
        stmt.setString(1, type);
                
        System.out.println("set the signal type for the trades registered for");
                
        try(ResultSet rs = stmt.executeQuery()){
            System.out.println("inside the result set for the gettting the trade signals");
            
            while(rs.next()){
                TradeSignals ts = new TradeSignals();
                ts.setId(rs.getInt("id"));
                System.out.println("Trade signal id = " + ts.getId());
                ts.setEntryPoint(String.valueOf(rs.getFloat("entryPoint")));
                ts.setStopLoss(String.valueOf(rs.getFloat("stopLoss")));
                ts.setTp1(String.valueOf(rs.getFloat("tp1")));
                ts.setTp2(String.valueOf(rs.getFloat("tp2")));
                ts.setCurrencyPair(rs.getString("currencyPair"));
                ts.setOrderType(rs.getString("orderType"));
                ts.setSignalType(rs.getString("signalType"));
                ts.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                
                list.add(ts);
                System.out.println("added trade to list");
                
            }
                    
             System.out.println("all trades have been added to the list");
                
            
            return list;
        }finally{
            con.close();
        }
    }
    
    public static ArrayList<TradeSignals> getTradeSignalsBOTH(Connection con) throws SQLException, NullPointerException{
      
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.tradesignals";
         
        PreparedStatement stmt = con.prepareStatement(QUERY);        
        try(ResultSet rs = stmt.executeQuery()){
            ArrayList<TradeSignals> list = new ArrayList<>();
            while(rs.next()){
                TradeSignals ts = new TradeSignals();
                ts.setId(rs.getInt("id"));
                System.out.println("Trade signal id = " + ts.getId());
                ts.setEntryPoint(String.valueOf(rs.getFloat("entryPoint")));
                ts.setStopLoss(String.valueOf(rs.getFloat("stopLoss")));
                ts.setTp1(String.valueOf(rs.getFloat("tp1")));
                ts.setTp2(String.valueOf(rs.getFloat("tp2")));
                ts.setCurrencyPair(rs.getString("currencyPair"));
                ts.setOrderType(rs.getString("orderType"));
                ts.setSignalType(rs.getString("signalType"));
                ts.setDateCreated(rs.getDate("dateCreated").toLocalDate());

                list.add(ts);
            }
            return list;
        }finally{
            con.close();
        }
    }
    
    public static void regTradeSignal(Connection con, Login login) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.tradesignalsregister(loginId, dateCreated, renewalLength, signalType) values (?, ?, ?, ?)";        
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, login.getId());
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(3, login.getRenewalLength());
            stmt.setString(4, login.getTradeType());
            
            stmt.executeUpdate();                      
            
            System.out.println("registered trade signal");
        }finally{
            con.close();
        }  
    }
    
    
    public static Login verifyLoginTradeSignal(Connection con, Login login) throws SQLException, NullPointerException{

        final String QUERY = "Select * FROM `tradesal_alpha-trade`.tradesignalsregister WHERE loginId = ?";

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setInt(1, login.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    login.setVerifiedTrade(true);
                    login.setTradeSignalType(rs.getString("signalType"));
                }else{
                    login.setVerifiedTrade(false);
                }
            } finally {
                con.close();
            }
        }
        
        return login;
    }
    
    public static String createEventTitle() {
        SecureRandom random = new SecureRandom();
        int randomValue = 1000000000 + random.nextInt(999999999);

        return String.valueOf(randomValue);
    }
}
