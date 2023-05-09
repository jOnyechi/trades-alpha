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
import tradealpha.BEAN.*;
import tradealpha.DAO.*;

@WebServlet(name="EditMemberServlet", urlPatterns={"/site/members-area/edit-customer", "/site/members-area/change-wallets"})
public class EditMemberServlet extends HttpServlet 
{
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        Login login = (Login) session.getAttribute("loggedInUser");        
        
        if(servletPath.matches("/site/members-area/edit-customer")){

            String address = request.getParameter("address");
            String phone = request.getParameter("phone-number");
            String country = request.getParameter("country");
            String[] form = new String[]{address, phone, country};
            
            if(validateForm(form)){
                CustomerContact newCustomerContact = new CustomerContact();
                
                //create new customer contact
                newCustomerContact.setCustId(login.getCustomer().getId());
                System.out.println(login.getCustomer().getId());
                newCustomerContact.setAddress(address);
                newCustomerContact.setCountry(country);
                newCustomerContact.setPhone(phone);
                
                try{
                    //edits the database
                    CustomerDAO.editCustomerAddress(getConnection(), newCustomerContact);
                    System.out.println("customer edited in database");
                    //sets the new constructor in the loggedInCustomer
                    login.getCustomer().setCustCont(CustomerDAO.getCustomerContact(getConnection(), login.getCustomer()));
                    
                    session.setAttribute("detailChange", true);                    
                    session.setAttribute("loggedInUser", login);
                    response.sendRedirect("members-area.jsp?a=as");
                    
                }catch(SQLException | IllegalArgumentException | NullPointerException e){
                    System.out.println("Error occured: " + e);
                    session.setAttribute("dbError", true);                    
                    response.sendRedirect("members-area.jsp?a=as");
                }
            }else{
                session.setAttribute("fieldError", true);               
                response.sendRedirect("members-area.jsp?a=as");
            }
        }else if(servletPath.matches("/site/members-area/change-wallets")){                       
            String btc = request.getParameter("bitcoin-wallet");
            String eth = request.getParameter("ethereum-wallet");                        
            
            if(btc.equals("")){
                btc = "N/A";
            }else if(eth.equals("")){
                eth = "N/A";
            }
            
            BtcAccount btcAcc = new BtcAccount();                
            EthereumAccount ethAcc = new EthereumAccount();
                        
            //create the address constructors
            btcAcc.setCustomerId(login.getCustomer().getId());
            btcAcc.setAddress(btc);                
            btcAcc.setDateModified(LocalDate.now());                                     
                   
            ethAcc.setCustomerId(login.getCustomer().getId());
            ethAcc.setAddress(eth);                
            ethAcc.setDateModified(LocalDate.now());
                                        
                
            try{
                //Adds the various coin account wallets to the database
                TransactionDAO.editBTC(getConnection(), btcAcc);
                System.out.println("btc edited");                    
                TransactionDAO.editETH(getConnection(), ethAcc);
                System.out.println("eth edited");
       
                login.getCustomer().getCustAcc().setBtcAccount(TransactionDAO.getBTC(getConnection(), login.getCustomer()));
                System.out.println("set btc account");
                login.getCustomer().getCustAcc().setEthAccount(TransactionDAO.getETH(getConnection(), login.getCustomer()));
                System.out.println("set eth account");
                           
                    
                session.setAttribute("detailChange", true);                    
                session.setAttribute("loggedInUser", login);
                response.sendRedirect("members-area.jsp?a=as");
                          
            }catch(SQLException | IllegalArgumentException | NullPointerException e){
                System.err.println("Error occured" + e);
                session.setAttribute("dbError", true);                                                
                response.sendRedirect("members-area.jsp?a=as");
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
    
    //Method to check if the password's match.
    //returns true if the passwords matches
    private boolean checkPasswordMatch(String password, String retypePassword) {

        if (password.matches("") || retypePassword.matches("")) {
            return false;
        }
        return password.matches(retypePassword);
    }
}
