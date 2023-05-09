package tradealpha.DAO;


import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import tradealpha.BEAN.*;



public class TransactionDAO 
{
    //gets plan title
    public static String getPlan(Connection con, int id) throws SQLException{
        
        final String QUERY = "SELECT title FROM `tradesal_alpha-trade`.plan WHERE id = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, id);
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getString("title");
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
    
    
    //adds a deposit to the database
    public static boolean addDeposit(Connection con, Deposit deposit) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.deposit(custId, planId, lengthOfContract, amount, modeOfPayment, dateCreated, dateModified) values (?,?,?,?,?,?,?)";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, deposit.getCustId());
            stmt.setInt(2, deposit.getPlanId());
            stmt.setInt(3, deposit.getLengthOfContract());
            stmt.setDouble(4, deposit.getAmount());
            stmt.setString(5, deposit.getMop());
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.setDate(7, Date.valueOf(LocalDate.now()));
            
            return stmt.executeUpdate() > 0;
            
        }finally{
            con.close();
        }    
    }
    
    //gets deposit by ID
    public static Deposit getDeposit(Connection con, Deposit deposit) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.deposit WHERE id = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, deposit.getId());
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    Deposit newDeposit = new Deposit();
                    newDeposit.setId(rs.getInt("id"));
                    newDeposit.setCustId(rs.getInt("custId"));
                    newDeposit.setPlanId(rs.getInt("planId"));
                    newDeposit.setAmount(rs.getFloat("amount"));
                    newDeposit.setLengthOfContract(rs.getInt("lengthOfContract"));
                    newDeposit.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                    newDeposit.setStatus(rs.getString("status"));
                    
                    return newDeposit;
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
    
    //edit btc account to database
    public static void confirmDeposit(Connection con, Deposit deposit) throws SQLException{
        
        final String QUERY = "UPDATE `tradesal_alpha-trade`.deposit SET status = ? WHERE custId = ?";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setString(1, "CONFIRMED");
            stmt.setInt(2, deposit.getCustId());            
            
            stmt.executeUpdate();            
        }finally{
            con.close();
        }    
    }
    
    //gets deposit by customerId
    public static Deposit getDeposit(Connection con, Customer customer) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.deposit WHERE customerId = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, customer.getId());
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    Deposit newDeposit = new Deposit();
                    newDeposit.setId(rs.getInt("id"));
                    newDeposit.setCustId(rs.getInt("custId"));
                    newDeposit.setPlanId(rs.getInt("planId"));
                    newDeposit.setLengthOfContract(rs.getInt("lengthOfContract"));
                    newDeposit.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                    newDeposit.setStatus(rs.getString("status"));
                    
                    return newDeposit;
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
    
    //adds a withdrawal to the database
    public static boolean addWithdraw(Connection con, Withdraw withdraw) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.withdraw(custId, amount, modeOfPayment, dateCreated, dateModified) values (?,?,?,?,?)";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, withdraw.getCustId());
            stmt.setDouble(2, withdraw.getAmount());
            stmt.setString(3, withdraw.getMop());
            stmt.setDate(4, Date.valueOf(LocalDate.now()));            
            stmt.setDate(5, Date.valueOf(LocalDate.now()));
            
            return stmt.executeUpdate() > 0;
            
        }finally{
            con.close();
        }    
    }
    
    //gets withdraw by ID
    public static Withdraw getWithdraw(Connection con, Withdraw withdraw) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.withdraw WHERE id = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, withdraw.getId());
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    Withdraw newWithdraw = new Withdraw();
                    newWithdraw.setId(rs.getInt("id"));
                    newWithdraw.setCustId(rs.getInt("custId"));                  
                    newWithdraw.setAmount(rs.getFloat("amount"));  
                    newWithdraw.setMop(rs.getString("modeOfPayment"));
                    newWithdraw.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                    newWithdraw.setStatus(rs.getString("status"));
                    
                    return newWithdraw;
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
    
    //gets withdrawal by customerId
    public static Withdraw getWithdraw(Connection con, Customer customer) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.withdraw WHERE customerId = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, customer.getId());
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    Withdraw newWithdraw = new Withdraw();
                    newWithdraw.setId(rs.getInt("id"));
                    newWithdraw.setCustId(rs.getInt("custId"));                  
                    newWithdraw.setAmount(rs.getFloat("amount"));                  
                    newWithdraw.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                    newWithdraw.setStatus(rs.getString("status"));
                    
                    return newWithdraw;
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
    
    //edit btc account to database
    public static void confirmWithdraw(Connection con, Withdraw withdraw) throws SQLException{
        
        final String QUERY = "UPDATE `tradesal_alpha-trade`.withdraw SET status = ? WHERE custId = ?";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setString(1, "CONFIRMED");
            stmt.setInt(2, withdraw.getCustId());            
            
            stmt.executeUpdate();            
        }finally{
            con.close();
        }    
    }
    
    //add btc account to database
    public static void addBTC(Connection con, BtcAccount btc) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.btcaccount(customerId, address, dateCreated, dateModified) values (?,?,?,?)";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, btc.getCustomerId());
            stmt.setString(2, btc.getAddress());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setDate(4, Date.valueOf(LocalDate.now()));
            
            stmt.executeUpdate();
            
        }finally{
            con.close();
        }    
    }
    
    //gets btcaccount by customerId
    public static BtcAccount getBTC(Connection con, Customer customer) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.btcaccount WHERE customerId = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, customer.getId());
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    BtcAccount btc = new BtcAccount();
                    btc.setId(rs.getInt("id"));
                    btc.setCustomerId(rs.getInt("customerId"));                  
                    btc.setAddress(rs.getString("address"));                  
                    btc.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                    btc.setDateModified(rs.getDate("dateModified").toLocalDate());
                    
                    return btc;
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
    
    //edit btc account to database
    public static void editBTC(Connection con, BtcAccount btc) throws SQLException{
        
        final String QUERY = "UPDATE `tradesal_alpha-trade`.btcaccount SET address = ?, dateModified = ? WHERE customerId = ?";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setString(1, btc.getAddress());
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, btc.getCustomerId());            
            
            stmt.executeUpdate();            
        }finally{
            con.close();
        }    
    }
    
//add btc account to database
    public static void addETH(Connection con, EthereumAccount eth) throws SQLException{
        
        final String QUERY = "INSERT into `tradesal_alpha-trade`.ethaccount(customerId, address, dateCreated, dateModified) values (?,?,?,?)";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, eth.getCustomerId());
            stmt.setString(2, eth.getAddress());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setDate(4, Date.valueOf(LocalDate.now()));
            
            stmt.executeUpdate();
            
        }finally{
            con.close();
        }    
    }
    
    //gets btcaccount by customerId
    public static EthereumAccount getETH(Connection con, Customer customer) throws SQLException{
    
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.ethaccount WHERE customerId = ?";
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setInt(1, customer.getId());
        
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    EthereumAccount eth = new EthereumAccount();
                    eth.setId(rs.getInt("id"));
                    eth.setCustomerId(rs.getInt("customerId"));                  
                    eth.setAddress(rs.getString("address"));                  
                    eth.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                    eth.setDateModified(rs.getDate("dateModified").toLocalDate());
                    
                    return eth;
                }
            }finally{
                con.close();
            }
        }
        return null;
    }
    
    //add btc account to database
    public static void editETH(Connection con, EthereumAccount eth) throws SQLException{
        
        final String QUERY = "UPDATE `tradesal_alpha-trade`.btcaccount SET address = ?, dateModified = ? WHERE customerId = ?";
        
        try(PreparedStatement stmt = con.prepareStatement(QUERY)){
            stmt.setString(1, eth.getAddress());
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, eth.getCustomerId());            
            
            stmt.executeUpdate();            
        }finally{
            con.close();
        }    
    }
    
    //gets all the orders ssociated with a customer Id
    public static ArrayList<Order> getOrdersCustId(Connection con, Customer cust) throws SQLException, NullPointerException{
      
        System.out.println("in the while loop");
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.withdraw WHERE custId = ?";
        ArrayList<Order> list = new ArrayList<>();
        
        PreparedStatement stmt = con.prepareStatement(QUERY);
        stmt.setInt(1, cust.getId());
        
        try(ResultSet rs = stmt.executeQuery()){            
            while(rs.next()){
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setType("WITHDRAW");
                order.setPlan("-");
                order.setMop(rs.getString("modeOfPayment").toUpperCase());
                order.setAmount(String.valueOf(rs.getFloat("amount")));
                order.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                order.setDateConfirmed(rs.getDate("dateModified").toLocalDate());
                order.setStatus(rs.getString("status"));
                
                list.add(order);
            }
            
        System.out.println("in the deposit method");
        final String QUERY1 = "SELECT * FROM `tradesal_alpha-trade`.deposit WHERE custId = ?";
        final String QUERY2 = "SELECT title FROM `tradesal_alpha-trade`.plan WHERE id = ?";
        
        
        PreparedStatement stmt1 = con.prepareStatement(QUERY1);
        stmt1.setInt(1, cust.getId());
        
            System.out.println("in the deposit prep statement");
            try(ResultSet rs1 = stmt1.executeQuery()){
                System.out.println("in the deposit result set");
                while(rs1.next()){
                    System.out.println("in the deposit while loop set");
                    //create the order
                    Order orderDep = new Order();
                    //fill the order constructor
                    orderDep.setId(rs1.getInt("id"));
                    orderDep.setType("DEPOSIT");

                    
                    //gets the plan name
                    int id = rs1.getInt("planId");                    
                    PreparedStatement stmt2 = con.prepareStatement(QUERY2);
                    stmt2.setInt(1, id);
                    
                    try(ResultSet rs2 = stmt2.executeQuery()){
                        while(rs2.next()){
                            orderDep.setPlan(rs2.getString("title"));
                        }
                    }

                    System.out.println(orderDep.getPlan());
                    orderDep.setAmount(String.valueOf(rs1.getFloat("amount")));
                    orderDep.setMop(rs1.getString("modeOfPayment"));
                    orderDep.setDateCreated(rs1.getDate("dateCreated").toLocalDate()); 
                    orderDep.setDateConfirmed(rs1.getDate("dateModified").toLocalDate());
                    orderDep.setStatus(rs1.getString("status"));
        
                    list.add(orderDep);
                    System.out.println("ADDED DEPOSIT ORDER TO LIST");
                }                
        }           
            
        }finally{
            con.close();
        }
        
        return list;
    }
    
    //gets all the orders of all customers
    public static ArrayList<Order> getAllOrders(Connection con) throws SQLException, NullPointerException{
        System.out.println("in the while loop");
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.withdraw";
        ArrayList<Order> list = new ArrayList<>();
        
        PreparedStatement stmt = con.prepareStatement(QUERY);
                
        try(ResultSet rs = stmt.executeQuery()){            
            while(rs.next()){
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setType("WITHDRAW");
                order.setPlan("-");
                order.setMop(rs.getString("modeOfPayment"));
                order.setAmount(String.valueOf(rs.getFloat("amount")));
                order.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                order.setStatus(rs.getString("status"));
                
                list.add(order);
            }
            
        System.out.println("in the deposit method");
        final String QUERY1 = "SELECT * FROM `tradesal_alpha-trade`.deposit";
        final String QUERY2 = "SELECT title FROM `tradesal_alpha-trade`.plan WHERE id = ?";
        
        
        PreparedStatement stmt1 = con.prepareStatement(QUERY1);
            System.out.println("in the deposit prep statement");
            try(ResultSet rs1 = stmt1.executeQuery()){
                System.out.println("in the deposit result set");
                while(rs1.next()){
                    //create the order
                    Order orderDep = new Order();
                    //fill the order constructor
                    orderDep.setId(rs1.getInt("id"));
                    orderDep.setType("DEPOSIT");

                    //gets the plan name
                    int id = rs1.getInt("planId");
                    PreparedStatement stmt2 = con.prepareStatement(QUERY2);
                    stmt2.setInt(1, id);
                    
                    try(ResultSet rs2 = stmt2.executeQuery()){
                        while(rs2.next()){
                            orderDep.setPlan(rs2.getString("title"));
                        }
                    }

                    orderDep.setMop(rs1.getString("modeOfPayment"));
                    orderDep.setAmount(String.valueOf(rs1.getFloat("amount")));
                    System.out.println(orderDep.getAmount());
                    orderDep.setDateCreated(rs1.getDate("dateCreated").toLocalDate());                    
                    orderDep.setStatus(rs1.getString("status"));
        
                    list.add(orderDep);
                    System.out.println("ADDED DEPOSIT ORDER TO LIST");
                }                
        }           
            
        }finally{
            con.close();
        }
        
        return list;

    }
    
    //gets all the orders of all customers
    public static ArrayList<Order> getRegisteredTradeSignalCustomers(Connection con) throws SQLException, NullPointerException{
        
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.tradesignalsregister";
        ArrayList<Order> list = new ArrayList<>();
        
        PreparedStatement stmt = con.prepareStatement(QUERY);
                
        try(ResultSet rs = stmt.executeQuery()){            
            while(rs.next()){
                System.out.println("in the while loop");
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setType(rs.getString("renewalLength"));
                order.setDateCreated(rs.getDate("dateCreated").toLocalDate());
                order.setMop("BTC");
                order.setStatus(rs.getString("status"));
                
                order.setPlan(String.valueOf(rs.getInt("loginId")));

                list.add(order);
            }
                               
            
        }finally{
            con.close();
        }
        
        return list;

    }
    
    //gets all the orders of all customers
    public static ArrayList<Order> getEmailList(Connection con) throws SQLException, NullPointerException{
      
        final String QUERY = "SELECT * FROM `tradesal_alpha-trade`.newsletter";        
        ArrayList<Order> list = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement(QUERY);
        try(ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                Order order = new Order();
                order.setId(rs.getInt("id"));
                //used to hold the customer email
                order.setType(rs.getString("email"));                             
                order.setDateCreated(rs.getDate("dateCreated").toLocalDate());                
                
                list.add(order);
            }    
        }
        finally{
            con.close();
        }
        
        return list;
    }
    
    
    //Adds the amount deposited into to the total amount of the customer placing the deposit
    public static boolean incrementBalance(Connection con, Deposit dep) throws SQLException, IllegalArgumentException {
        final String QUERY = "UPDATE `tradesal_alpha-trade`.customeraccount SET balance = balance + ?, dateModified = ? WHERE custId = ?";       

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {            
            stmt.setFloat(1, dep.getAmount());
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, dep.getCustId());
            
            return stmt.executeUpdate() > 0;
            
        } finally {
            con.close();
        }
    }
    
    //Adds the amount deposited into to the total amount of the customer placing the deposit
    public static boolean decreaseBalance(Connection con, Withdraw withdrawal) throws SQLException, IllegalArgumentException {
        final String QUERY = "UPDATE `tradesal_alpha-trade`.customeraccount SET balance = balance - ?, dateModified = ? WHERE custId = ?";       

        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {            
            stmt.setDouble(1, withdrawal.getAmount());
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, withdrawal.getCustId());
            
            
            return stmt.executeUpdate() > 0;
            
        } finally {
            con.close();
        }
    }
    
    //creates an event to add the percentage the total balance daily
    public static boolean dailyIncrement(Connection con, Deposit dep) throws SQLException, IllegalArgumentException {
        final String QUERY = "SELECT dailyIncrease FROM `tradesal_alpha-trade`.plan WHERE id = ?";
        final String QUERY1 = "CREATE EVENT E" + createEventTitle() + " " + 
                                    "ON SCHEDULE EVERY 1 DAY " +
                                    "STARTS CURRENT_TIMESTAMP + INTERVAL 1 DAY " +
                                    "ENDS CURRENT_TIMESTAMP + INTERVAL ? MONTH " +
                                    "DO " +                                    
                                        "UPDATE `tradesal_alpha-trade`.customeraccount SET balance = balance + (? * ?) WHERE custId = ?; " ;
        
        double percentageIncrease = 0.00;
        
        try (PreparedStatement stmt = con.prepareStatement(QUERY)) { 
            stmt.setInt(1, dep.getPlanId());
            
            //gets the dailyIncrease of the deposit created
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    percentageIncrease = rs.getDouble("dailyIncrease");
                }
                
                
                
                //creates the scheduled event for daily increment
                try (PreparedStatement stmt1 = con.prepareStatement(QUERY1)) {                 
                stmt1.setInt(1, dep.getLengthOfContract());
                stmt1.setDouble(2, percentageIncrease);
                stmt1.setDouble(3, dep.getAmount());
                stmt1.setInt(4, dep.getCustId());
                                
                
                return stmt1.execute();
                }
            }            
        }finally {
            con.close();
        }
    }
     
        //confirm transaction 
    public static boolean confirmTransaction(Connection con, int id, String type) throws SQLException{
        //for confirming withdrawal, checks the type of the transaction
        //if withdrawal - 
        //Gets the full withdrawal constructor,
        //Take the amount of the withdrawal constructor and subtracts it from the customer account balance
        
        //for confirming deposits
        //checks if the user
        
        final String QUERY = "UPDATE `tradesal_alpha-trade`.withdraw SET status = ?, dateModified = ? WHERE id = ?";        
        final String QUERY1 = "UPDATE `tradesal_alpha-trade`.deposit SET status = ?, dateModified = ? WHERE id = ?";
        
        if(type.matches("WITHDRAW")){
            
           try(PreparedStatement stmt = con.prepareStatement(QUERY)){
                    stmt.setString(1, "CONFIRMED");                
                    stmt.setDate(2, Date.valueOf(LocalDate.now()));
                    stmt.setInt(3, id);
                
                    return stmt.executeUpdate() > 0;          
            }finally{
                con.close();
            }            
        }else{
            
            try(PreparedStatement stmt1 = con.prepareStatement(QUERY1)){
                stmt1.setString(1, "CONFIRMED");                
                stmt1.setDate(2, Date.valueOf(LocalDate.now()));
                stmt1.setInt(3, id);            
                
                return stmt1.executeUpdate() > 0;          
            }
            finally{
                con.close();
            }
            
        }    

    }
    
    public static boolean confirmRegTrade(Connection con, int id, int loginId) throws SQLException{
        
        final String QUERY = "UPDATE `tradesal_alpha-trade`.tradesignalsregister SET status = ?, dateCreated = ? WHERE id = ?";
        final String QUERY2 = "SELECT * FROM `tradesal_alpha-trade`.tradesignalsregister WHERE loginId = ?";        
        final String QUERY1 = "CREATE EVENT ETS" + createEventTitle() + " " + 
                                    "ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL ? MONTH " +                                    
                                    "DO " +                                    
                                        "DELETE FROM `tradesal_alpha-trade`.tradesignalsregister WHERE id = ?; " ;
        
        
        try(PreparedStatement stmt2 = con.prepareStatement(QUERY2)){
            stmt2.setInt(1, loginId);
            System.out.println("loginId selected");
            
            
            //creates a new order object
            Order order = new Order();
            System.out.println("order constructor created");
            
            
            try(ResultSet rs2 = stmt2.executeQuery()){            
                System.out.println("in the loop for getting the registered trade info");
                if(rs2.next()){
                    order.setId(rs2.getInt("id"));
                    order.setType(rs2.getString("renewalLength"));
                    order.setDateCreated(rs2.getDate("dateCreated").toLocalDate());
                    order.setMop("BTC");
                    order.setStatus(rs2.getString("status"));

                    order.setPlan(String.valueOf(rs2.getInt("loginId")));
                    System.out.println("constructor filled completely");
                    
                    try(PreparedStatement stmt = con.prepareStatement(QUERY)){
                    
                        System.out.println("inside prep statement for updating tradesignalsregister table");
                        stmt.setString(1, "CONFIRMED");                
                        stmt.setDate(2, Date.valueOf(LocalDate.now()));
                        stmt.setInt(3, id);

                        if(stmt.executeUpdate() > 0){
                            System.out.println("executed statement");
                            if(order.getType().equalsIgnoreCase("3") || order.getType().equalsIgnoreCase("7")){
                                System.out.println("checking renewal length");
                                try(PreparedStatement stmt1 = con.prepareStatement(QUERY1)){
                                    System.out.println("inside prep statement to start event");
                                    stmt1.setInt(1, Integer.parseInt(order.getType()));
                                    stmt1.setInt(2, id);

                                    return stmt1.execute();
                                }   
                            }
                        }
                    }
                }                
            }

            return true;            
        }finally{
            con.close();
        }  
    }
       public static String createEventTitle() {
        SecureRandom random = new SecureRandom();
        int randomValue = 1000000000 + random.nextInt(999999999);

        return String.valueOf(randomValue);
    }
}
