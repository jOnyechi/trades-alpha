package tradealpha.SERVLET;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.jdbc.pool.DataSource;
import java.util.ArrayList;
import tradealpha.BEAN.*;
import tradealpha.DAO.*;

@WebServlet(name="TransactionServlet", urlPatterns={"/site/members-area/deposit", "/site/members-area/withdraw", "/newsletter"})
public class TransactionServlet extends HttpServlet 
{ 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        Login login = (Login) session.getAttribute("loggedInUser");
        
        
        if(servletPath.matches("/site/members-area/deposit")){

            String plan = request.getParameter("plan");
            String payment = request.getParameter("MOP");
            String length = request.getParameter("cont-length");
            String amountString = request.getParameter("Capital-investment");
            
            Float amount = Float.parseFloat(amountString);
            
            //creates the deposit constructor
            Deposit deposit = new Deposit();
            deposit.setCustId(login.getCustomer().getId()); 
            deposit.setPlanId(Integer.parseInt(plan));
            deposit.setMop(payment.toUpperCase());
            deposit.setAmount(amount);
            deposit.setLengthOfContract(Integer.parseInt(length));
                
                try{
                    if(TransactionDAO.addDeposit(getConnection(), deposit)){
                        //adds the deposit to the database
                        switch(payment){
                            case "bitcoin":
                                session.setAttribute("amount", amountString);
                                login.setOrderList(TransactionDAO.getOrdersCustId(getConnection(), login.getCustomer()));
                                session.setAttribute("loggedInUser", login);
                                response.sendRedirect("payment-page.jsp?type=btc");
                                break;                                                                                                
                            case "ethereum":
                                session.setAttribute("amount", amountString);
                                login.setOrderList(TransactionDAO.getOrdersCustId(getConnection(), login.getCustomer()));
                                session.setAttribute("loggedInUser", login);
                                response.sendRedirect("payment-page.jsp?type=eth");
                                break;
                            case "bitcoin-cash":
                                session.setAttribute("amount", amountString);
                                login.setOrderList(TransactionDAO.getOrdersCustId(getConnection(), login.getCustomer()));
                                session.setAttribute("loggedInUser", login);
                                response.sendRedirect("payment-page.jsp?type=btc-cash");
                                break;                                                    
                        }                                               
                    }else{
                        System.out.println("Couldn't add a deposit");
                        session.setAttribute("dbError", true);                                               
                         //send to memnbers area with deposit page showing
                        response.sendRedirect("members-area.jsp?a=deposit");
                    }
                }catch(SQLException | IllegalArgumentException e){
                    System.err.println("Error occured: " + e);
                    session.setAttribute("dbError", true);                                        
                     //send to memnbers area with deposit page showing
                    response.sendRedirect("members-area.jsp?a=deposit");
                }
            
        }else if(servletPath.matches("/site/members-area/withdraw")){            
            
            String currency = request.getParameter("currency");
            Float amount = Float.valueOf(request.getParameter("amount"));                       
          
                //checks if the withdrawal amount is more than the logged in Users balance
                if(amount > (login.getCustomer().getCustAcc().getBalance())){
                    session.setAttribute("insufficientBalance", true);                                   
                    //redirect to members page with withdraw page showing page
                    response.sendRedirect("members-area.jsp?a=withdrawal");
                }else{                
                    //creates the deposit constructor
                    Withdraw withdraw = new Withdraw();
                    withdraw.setCustId(login.getCustomer().getId());
                    withdraw.setAmount(amount);
                    withdraw.setMop(currency);
                    
                    try{
                        
                        if(TransactionDAO.addWithdraw(getConnection(), withdraw)){
                            System.out.println("withdraw added");
                            //retrieves the new list of orders and adds it as session attributes
                            login.setOrderList(TransactionDAO.getOrdersCustId(getConnection(), login.getCustomer()));
                                                        
                            session.setAttribute("loggedInUser", login);                    
                            session.setAttribute("success", true);
                            //redirect to members page with withdraw page showing page
                            response.sendRedirect("members-area.jsp?a=withdrawal");
                        }else{
                            session.setAttribute("dbError", true); 
                            //redirect to members page with withdraw page showing page
                            response.sendRedirect("members-area.jsp?a=withdrawal");
                        }                  
                    }catch(SQLException | IllegalArgumentException | NullPointerException e){
                        System.err.println("Error occured: " + e);
                        session.setAttribute("loggedInUser", login);                    
                        session.setAttribute("dbError", true); 
                        //redirect to members page with withdraw page showing page
                        response.sendRedirect("members-area.jsp?a=withdrawal");
                    }
                }                
            
        }else if(servletPath.matches("/newsletter")){
            
            //adds the email to the newsletter table in database 
            try{
                String email = request.getParameter("email-address");
                if(CustomerDAO.addToNewsletter(getConnection(), email)){                     
                    response.sendRedirect("index.jsp");
                }
            }catch(SQLException | NullPointerException| IllegalArgumentException e){
                System.err.println("Error occured: " + e);                               
                response.sendRedirect("index.jsp");
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
    
    
    //Method to check if the password's match.
    //returns true if the passwords matches
    private boolean checkPasswordMatch(String password, String retypePassword) {

        if (password.matches("") || retypePassword.matches("")) {
            return false;
        }
        return password.matches(retypePassword);
    }
}
