package tradealpha.SERVLET;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.jdbc.pool.DataSource;
import tradealpha.BEAN.*;
import tradealpha.DAO.*;


@WebServlet(name="AdminServlet", urlPatterns={"/site/admin/confirm-transaction", "/site/admin/createTradeSignal", "/site/admin/block-customer", "/site/admin/reg-trade"})
public class AdminServlet extends HttpServlet 
{ 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();                
        
        if(servletPath.matches("/site/admin/createTradeSignal")){
            
            
            System.out.println("in the ct servlet");
            String signalType = request.getParameter("signal-type");
            String currencyPair = request.getParameter("currency-pair");
            String orderType = request.getParameter("order-type");
            System.out.println(orderType);
            String entryPoint = request.getParameter("entry-point");
            String stopLoss = request.getParameter("stop-loss");
            String tp1 = request.getParameter("tp-1");
            String tp2 = request.getParameter("tp-2");
            String form[] = {currencyPair, entryPoint, stopLoss, tp1};
            
            if(validateForm(form)){
                
                //create trade signals constructor 
                TradeSignals ts = new TradeSignals();
                ts.setSignalType(signalType);
                ts.setCurrencyPair(currencyPair);
                ts.setOrderType(orderType);
                System.out.println(ts.getOrderType());
                ts.setEntryPoint(entryPoint);
                ts.setStopLoss(stopLoss);
                ts.setTp1(tp1);
                ts.setTp2(tp2);
                
                try{
                    if(TradeSignalsDAO.addTradeSignal(getConnection(), ts)){
                        session.setAttribute("confirm", true); 
                        //redirect to the proper page
                        response.sendRedirect("admin-area.jsp?b=ts");
                    }
                }catch(SQLException | IllegalArgumentException | NullPointerException e){
                    System.err.println("Error occured" + e);
                    session.setAttribute("dbError", true);                    
                    response.sendRedirect("admin-area.jsp?b=ts");
                }
            }else{                
                session.setAttribute("fieldError", true);                    
                response.sendRedirect("admin-area.jsp?b=ts");
            }
        }
        
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        
        if(servletPath.matches("/site/admin/block-customer")){
            
            
            int id = Integer.parseInt(request.getParameter("c"));
            Boolean blocked = Boolean.valueOf(request.getParameter("d"));
            Admin admin = (Admin) session.getAttribute("admin");
            
            
            try{
                Login login = LoginDAO.getLoginById(getConnection(), id);
                login.setBlocked(blocked);
                
                if(login.isBlocked()){
                    if(LoginDAO.isBlocked(getConnection(), login)){                        
                        //send to members area with the new list of logins
                        admin.setLoginList(LoginDAO.getLoginList(getConnection()));
                        admin.setLoggedInUsers(LoginDAO.getLoggedInUsers(getConnection()));
                        
                        session.setAttribute("admin", admin);
                        response.sendRedirect("admin-area.jsp");
                    }else{
                        //send error
                        session.setAttribute("dbError", true);
                        response.sendRedirect("admin-area.jsp");
                    }
                }else{
                    if(LoginDAO.isBlocked(getConnection(), login)){
                        //send to members area with the new list of logins                        
                        admin.setLoginList(LoginDAO.getLoginList(getConnection()));
                        admin.setLoggedInUsers(LoginDAO.getLoggedInUsers(getConnection()));
                        
                        session.setAttribute("admin", admin);
                        response.sendRedirect("admin-area.jsp");
                    }else{
                        //send error
                        session.setAttribute("dbError", true);
                        response.sendRedirect("admin-area.jsp");
                    }
                }                
            }catch(SQLException | IllegalArgumentException | NullPointerException e){
                //send error database error
                System.err.println("Error occured" + e);
                session.setAttribute("dbError", true);    
                //send to admin page
                response.sendRedirect("admin-area.jsp");
            }  
        }
        
        if(servletPath.matches("/site/admin/confirm-transaction")){
            
            String orderType = request.getParameter("type");            
            int orderId = Integer.parseInt(request.getParameter("id"));             
            Admin admin = (Admin) session.getAttribute("admin");
            
            
            try{
                if(orderType.matches("WITHDRAW")){
                    Withdraw withdraw = new Withdraw();
                    withdraw.setId(orderId);

                    withdraw = TransactionDAO.getWithdraw(getConnection(), withdraw);
                    if(TransactionDAO.decreaseBalance(getConnection(), withdraw)){
                        if(TransactionDAO.confirmTransaction(getConnection(), orderId, orderType)){
                            System.out.println("transaction confirmed");
                            session.setAttribute("success", true);
                            admin.setOrderList(TransactionDAO.getAllOrders(getConnection()));
                            admin.setLoggedInUsers(LoginDAO.getLoggedInUsers(getConnection()));
                            
                            session.setAttribute("admin", admin);
                            response.sendRedirect("admin-area.jsp?b=oc");                    
                        }else{
                            System.out.println("couldn't confirm transaction");
                            session.setAttribute("dbError", true);
                            response.sendRedirect("admin-area.jsp?b=oc");
                        }
                    }else{
                        System.out.println("Couldn't decrease the balance");
                        session.setAttribute("dbError", true);
                        response.sendRedirect("admin-area.jsp?b=oc");
                    } 
                }else if(orderType.matches("DEPOSIT")){                    
                    //ALGORITHM FOR THE DEPOSIT
                    
                    Deposit deposit = new Deposit();
                    deposit.setId(orderId);
                    System.out.println("Order Id " + deposit.getId());
                    //gets the full deposit
                    deposit = TransactionDAO.getDeposit(getConnection(), deposit);
                    System.out.println("Customer Id = " + deposit.getCustId());
                    System.out.println("Price = " + deposit.getAmount());
                    
                    //trys to increment the account balance of the customer affected
                    if(TransactionDAO.incrementBalance(getConnection(), deposit)){
                        //creaes the event for the daily incrementing 
                        if(!(TransactionDAO.dailyIncrement(getConnection(), deposit))){
                            System.out.println("check ref part of method");
                            //gets the loginId of the customer that created the deposit
                            //after which uses the loginId to get the full login
                            int loginId = CustomerDAO.getLoginIdFromCustomerTable(getConnection(), deposit.getCustId());                            
                            Login login = LoginDAO.getLoginById(getConnection(), loginId);
                            
                            //gets the ref code of the customer who referred the depositing customer if available
                            //returns N/A if there is no referredBy available
                            String refBy = ReferralDAO.getReferredBy(getConnection(), login);
                            
                            //checks if refBy returns 'N/A'
                            //if no it added plusOneActiveRef, referralCommission, then confirms transaction
                            if (!(refBy.matches("N/A"))){
                                System.out.println("ref found");
                                if(ReferralDAO.plusOneActiveRef(getConnection(), refBy)){
                                    if(ReferralDAO.referralCommission(getConnection(), deposit, refBy)){
                                        if(TransactionDAO.confirmTransaction(getConnection(), orderId, orderType)){
                                            System.out.println("deposit added");
                                            session.setAttribute("success", true);
                                            admin.setOrderList(TransactionDAO.getAllOrders(getConnection()));
                                            admin.setLoggedInUsers(LoginDAO.getLoggedInUsers(getConnection()));
                                            
                                            session.setAttribute("admin", admin);
                                            response.sendRedirect("admin-area.jsp?b=oc");
                                        }else{
                                            System.out.println("Couldn't change order status");
                                            session.setAttribute("dbError", true);
                                            response.sendRedirect("admin-area.jsp?b=oc");
                                        }                                        
                                    }else{
                                        System.out.println("Couldn't add ref commision to database for ref-code: " + refBy);
                                        session.setAttribute("dbError", true);                                                                                    
                                        response.sendRedirect("admin-area.jsp?b=oc");
                                    }
                                }else{
                                    System.out.println("Couldn't increment active ref for ref-code: " + refBy);
                                    session.setAttribute("dbError", true);                                                                            
                                    response.sendRedirect("admin-area.jsp?b=oc");
                                }
                            }else{
                                System.out.println("no ref found");
                                if(TransactionDAO.confirmTransaction(getConnection(), orderId, orderType)){
                                    System.out.println("transaction confirmed");
                                    System.out.println("deposit added");
                                    session.setAttribute("success", true);
                                    admin.setOrderList(TransactionDAO.getAllOrders(getConnection()));
                                    admin.setLoggedInUsers(LoginDAO.getLoggedInUsers(getConnection()));
                                    
                                    session.setAttribute("admin", admin);
                                    response.sendRedirect("admin-area.jsp?b=oc");
                                }else{
                                    System.out.println("Couldn't change order status");
                                    session.setAttribute("dbError", true);
                                    response.sendRedirect("admin-area.jsp?b=oc");
                                }
                            }
                        }else{
                            System.out.println("Couldn't create a scheduled event");
                            session.setAttribute("dbError", true);                                                           
                            //send to memnbers area with deposit page showing 
                            response.sendRedirect("admin-area.jsp?b=oc");
                        }
                    }else{
                        System.out.println("Couldn't increment balance");
                        session.setAttribute("dbError", true);                                                      
                        //send to memnbers area with deposit page showing
                        response.sendRedirect("admin-area.jsp?b=oc");
                    }
                }
            }catch(SQLException | IllegalArgumentException | NullPointerException e){
                System.out.println("Error occured: " + e);
                System.out.println("transaction error");
                session.setAttribute("dbError", true);
                response.sendRedirect("admin-area.jsp?b=oc");
            }
        }
        
        if(servletPath.matches("/site/admin/reg-trade")){

            int orderId = Integer.parseInt(request.getParameter("id"));
            int loginId = Integer.parseInt(request.getParameter("loginId"));
            Admin admin = (Admin) session.getAttribute("admin");
            
            try{                
                if(!(TransactionDAO.confirmRegTrade(getConnection(), orderId, loginId))){
                    admin.setRegTradeList(TransactionDAO.getRegisteredTradeSignalCustomers(getConnection()));
                    session.setAttribute("admin", admin);
                    response.sendRedirect("admin-area.jsp?b=trsc");
                }else{
                    System.out.println("transaction error");
                    session.setAttribute("dbError", true);
                    response.sendRedirect("admin-area.jsp?b=trsc");
                }
            }catch(SQLException | IllegalArgumentException | NullPointerException e){
                System.out.println("Error occured: " + e);
                System.out.println("transaction error");
                session.setAttribute("dbError", true);
                response.sendRedirect("admin-area.jsp?b=trsc");
            }
        }
    
    }
    
    private Connection getConnection() throws SQLException {
        Connection connection = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://mysql3000.mochahost.com/tradesal_alpha-trade?serverTimezone=Africa/Lagos", "tradesal_alpha", "q0?EKOwD?K=e");
        }catch(Exception e){
            System.err.println(e);
        }
        
        return connection;
    }
    
    //Validating form
    //returns false if the string variable is empty
    private boolean validateForm(String[] form) {
        boolean valid = true;
        for (String form1 : form) {
            if (form1.matches("")) {
                return false;
            }
        }
        return valid;
    }
    
    private boolean validateWalletData(String[] form) {
        boolean valid = true;
        for (String form1 : form) {
            if (form1.matches("")) {
               form1 = "N/A";
            }
        }
        return valid;
    }
}
