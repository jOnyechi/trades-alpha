package tradealpha.SERVLET;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.jdbc.pool.DataSource;
import tradealpha.BEAN.*;
import tradealpha.DAO.*;

//The LoginDAO.logIn gets the customers that are currently loggedIn

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxRequestSize = 1024 * 1024 * 50)
@WebServlet(name="LoginServlet", urlPatterns={"/login", "/sign-up", "/register", "/changePassword", "/site/members-area/changePassword", "/site/members-area/delete-account", "/registerTradeSignal", "/site/members-area/logout", "/site/admin/logout"})
public class LoginServlet extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        String servletPath = request.getServletPath();
        PBKDF2Hasher hasher = new PBKDF2Hasher();
        
        if (servletPath.equals("/login")) {
            System.out.println("Connected to servlet");
            //users details are sent to the servlet
            //the input details are validated
            //once validated it check if the details are from the admin, if yes sends to admin area
            //if not admin, checks if details are available to in the login table
            //if !(login exists), return to login page with dbError
            //if login exists, check if the login is blocked, if blocked, send back to login page with db Error
            //if login is not blocked get the full login and customer constructor and send as a session attribute
            
            
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String[] form = new String[]{email, password};
            HttpSession session = request.getSession();

            // checking if any form inputs are empty
            if (validateForm(form)) {                    
                //sets temporary user constructor
                Login login = new Login();
                login.setEmail(email);
                login.setPassword(password);
                try {
                    //checks if the login and the password exists in database
                    LoginDAO.verifyLogin(getConnection(), login, hasher);
                    if(login.isVerified()){                                                        
                        if(login.isBlocked()){
                            System.out.println("user blocked");
                            session.setAttribute("userBlocked", true);
                            response.sendRedirect("login-signup.jsp?rf=no");
                        }else{
                            if(LoginDAO.checkRole(getConnection(), login).matches("ADMIN")){
                                try{
                                    //Get the list of customerLogins                    
                                    Admin admin = new Admin();
                                    //modifies the last login for the admin
                                    LoginDAO.modifyLastLogin(getConnection(), login);
                                    admin.setLoginList(LoginDAO.getLoginList(getConnection()));
                                    System.out.println("LOGINLIST ADDED");
                                    
                                    admin.setOrderList(TransactionDAO.getAllOrders(getConnection()));
                                    System.out.println("ORDERLIST ADDED");
                                    System.out.println(admin.getOrderList().isEmpty());

                                    admin.setMailingList(TransactionDAO.getEmailList(getConnection()));
                                    System.out.println("MAILING LIST");
                                    
                                    admin.setLoggedInUsers(LoginDAO.getLoggedInUsers(getConnection()));
                                    System.out.println("LoggedInUsers LIST CREATED");
                                    
                                    admin.setRegTradeList(TransactionDAO.getRegisteredTradeSignalCustomers(getConnection()));
                                    System.out.println("Registrered Trader LIST CREATED");

                                    session.setAttribute("admin", admin);
                                    //send to admin area
                                    response.sendRedirect("site/admin/admin-area.jsp");
                                }
                                catch(SQLException | IllegalArgumentException | NullPointerException e){
                                    System.err.println("Error occured" + e);
                                    System.out.println("Couldn't get customer arrayList");
                                    //send to error message
                                    session.setAttribute("dbError", true);
                                    response.sendRedirect("login-signup.jsp?rf=no");
                                } 
                            }else{
                                
                                //modifies the last login of the loggedInUser
                                LoginDAO.modifyLastLogin(getConnection(), login);
                                
                                login = LoginDAO.getLogin(getConnection(), login, hasher); //gets the entire login constructor
                                
                                if(login == null){
                                    System.err.println("Null login constructor");
                                    session.setAttribute("dbError", true);
                                    response.sendRedirect("login-signup.jsp?rf=no");
                                }else{
                                    
                                    //gets the enitire list of trade signals for customers who have registered for trades
                                    if(login.isVerifiedTrade()){
                                        System.out.println("login is verified to see trades");
                                        String type = TradeSignalsDAO.getSignalTypeOfUser(getConnection(), login.getId());
                                        
                                        switch(type){                                        
                                            case "FOREX":
                                                System.out.println("getting forex signals");
                                                login.setTsList(TradeSignalsDAO.getTradeSignals(getConnection(), type)); 
                                                break;
                                            case "INDICES":
                                                System.out.println("getting indices signals");
                                                login.setTsList(TradeSignalsDAO.getTradeSignals(getConnection(), type)); 
                                                break;
                                            case "BOTH":
                                                System.out.println("getting both signals");
                                                login.setTsList(TradeSignalsDAO.getTradeSignalsBOTH(getConnection())); 
                                                break;
                                            case "N/A":
                                                login.setVerifiedTrade(false);
                                                break;  
                                        }                                                                               
                                    }
                                    
                                    //Returns all the customer constructors
                                    Customer cust = CustomerDAO.getCustomer(getConnection(), login);
                                    CustomerAccount custAcc = CustomerDAO.getCustomerAccount(getConnection(), cust);
                                    CustomerContact custCont = CustomerDAO.getCustomerContact(getConnection(), cust);

                                    custAcc.setBtcAccount(TransactionDAO.getBTC(getConnection(), cust));                                
                                    custAcc.setEthAccount(TransactionDAO.getETH(getConnection(), cust));                                
                                    
                                    //adds the the ref commision and the account balance together
                                    custAcc.setBalance(custAcc.getBalance() + login.getRefCommission());
                                    //sets entire customer constructor and the login constructor                                                                        
                                    cust.setCustAcc(custAcc);
                                    cust.setCustCont(custCont);
                                    login.setCustomer(cust);
                                    
                                    //Gets the list of allcustomer orders
                                    login.setOrderList(TransactionDAO.getOrdersCustId(getConnection(), login.getCustomer())); 
                                    LoginDAO.logIn(getConnection(), login);
                                    session.setAttribute("loggedInUser", login); //stores user as session attribute                                                  
                                    response.sendRedirect("site/members-area/members-area.jsp"); //sends to dashboard
                                }
                            }                                
                        }                            
                    }else{
                        System.out.println("user not verified");
                        session.setAttribute("userNotAvailable", true);
                        response.sendRedirect("login-signup.jsp?rf=no");                        
                    }
                }catch (SQLException | IllegalArgumentException e) {
                    System.err.println("Error occured" + e);
                    session.setAttribute("dbError", true);
                    response.sendRedirect("login-signup.jsp?rf=no");
                }                
            }else {
                session.setAttribute("fieldError", true);
                System.out.println("Form not validated");
                response.sendRedirect("login-signup.jsp?rf=no");
            }
        } else if (servletPath.equals("/sign-up")) {
            
            String firstName = request.getParameter("first-name");
            String lastName = request.getParameter("last-name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String passwordRepeat = request.getParameter("retype-password");
            String[] form = new String[]{firstName, lastName, email, password, passwordRepeat};
            String refCode = request.getParameter("rf");

            HttpSession session = request.getSession();

            if (validateForm(form)) {
                Customer customer = new Customer();
                    customer.setFirstName(firstName);
                    customer.setLastName(lastName);// create the cust constructor 

                    //sets the password and email and the customer constructor to the login constructor.
                    Login login = new Login();
                    login.setEmail(email);
                    login.setPassword(password);
                    login.setCustomer(customer);
                    try {
                        //if validateLogin returns false then user doesn't exists, vice-versa
                        login = LoginDAO.verifyLogin(getConnection(), login, hasher);
                        if(!login.isVerified()){
                            //set remainder of the user constructor                            
                            login.setDateCreated(LocalDate.now());
                            login.setDateModified(LocalDate.now());
                            login.setLastLogin(LocalDate.now());
                            login.setIsVerified(true);

                            //create verification NO
                            //sets a verification number  
                            // CHECK THE CHECKVERIFICATIONCODENUMBER FUNCTION 
                            //
                            //
                            String verNo = LoginDAO.createVerificationCode();
                            login.setRefCode(verNo);
                            System.out.println("ref code created and added");

                            //Check if a referral code was queried to the link
                            //if yes add to database                            
                            System.out.println(refCode);
                                if(refCode.equals("no")){
                                    //add user to database without the referral constructor created
                                    //hashes password before inserting ointo database
                                    LoginDAO.addLogin(getConnection(), login, hasher);
                                    
                                    
                                    System.out.println("user added to db without a refferal");
                                    
                                    //gets the full user constructor with the id
                                    login = LoginDAO.getLogin(getConnection(), login, hasher);
                                    
                                    if(login == null){
                                        System.out.println("Null Login Constructor");
                                        session.setAttribute("dbError", true);
                                        response.sendRedirect("login-signup.jsp?rf=no");
                                    }else{
                                        System.out.println("full login constructor created");
                                        
                                        //session.setAttribute("signedUpCustomer", customer);
                                        session.setAttribute("loggedInUser", login);
                                        response.sendRedirect("bio-data-form.jsp");
                                    }
                                }else{
                                    try{
                                        System.out.println("In the try block");
                                        if(ReferralDAO.checkRefCode(getConnection(), refCode)){
                                            System.out.println("ref code sign up");
                                            //add user to database
                                            LoginDAO.addLogin(getConnection(), login, hasher);                            
                                            System.out.println("user added to db with ref code");
                                            
                                            //gets the full user constructor with the id
                                            //if the login constructor returns null then send to login page
                                            login = LoginDAO.getLogin(getConnection(), login, hasher);
                                            if(login == null){
                                                System.err.println("Null Login Constructor");
                                                session.setAttribute("dbError", true);
                                                response.sendRedirect("login-signup.jsp?rf=" + request.getParameter("rf"));
                                            }else{
                                                System.out.println("full login constructor created");
                                                //add referral to database
                                                Referral referral = new Referral();
                                                referral.setRefBy(request.getParameter("rf"));
                                                referral.setRefTo(verNo);
                                                referral.setDateCreated(LocalDate.now());
                                                if(ReferralDAO.addReferral(getConnection(), referral)){
                                                    System.out.println("ref added to db");

                                                    if(ReferralDAO.plusOneRef(getConnection(), referral)){
                                                        System.out.println("no of referrals incremented by 1 on db");
                                                        
                                                        session.setAttribute("loggedInUser", login);                                                    
                                                        response.sendRedirect("bio-data-form.jsp");
                                                    }else{
                                                        System.out.println("no of Referrals not incremented");
                                                        session.setAttribute("dbError", true);
                                                        response.sendRedirect("login-signup.jsprf=" + request.getParameter("rf"));
                                                    }                                                
                                                }else{
                                                    System.out.println("referral not added");
                                                    session.setAttribute("dbError", true);
                                                    response.sendRedirect("login-signup.jsp?rf=" + request.getParameter("rf"));
                                                }
                                            }                                             
                                        }else{
                                            response.sendError(404);
                                        }                                        
                                    }catch(SQLException | IllegalArgumentException | NullPointerException e){
                                        System.err.println("Error: " + e);
                                        session.setAttribute("dbError", true);
                                        response.sendRedirect("login-signup.jsp?rf=" + request.getParameter("rf"));
                                    }
                                }   
                        }else{
                            System.err.println("Login already exisits");
                            session.setAttribute("userExisitsError", true);
                            response.sendRedirect("login-signup.jsp?rf=no");
                        }   
                    } catch (SQLException | IllegalArgumentException | NullPointerException e) {
                        System.err.println("Error occured: " + e);
                        session.setAttribute("dbError", true);
                        response.sendRedirect("login-signup.jsp?rf=no");
                    }
            } else {
                System.out.println("Form is not filled properly");
                session.setAttribute("fieldError", true);
                response.sendRedirect("login-signup.jsp");
            }
        } else if (servletPath.equals("/register")) {
            String firstName = request.getParameter("first-name");
            String lastName = request.getParameter("last-name");            
            String gender = request.getParameter("gender");
            System.out.println(gender);
            String email = request.getParameter("registered-email");
            String dob = request.getParameter("dob");
            String address = request.getParameter("address");
            String country = request.getParameter("country");
            String phone = request.getParameter("phone-number");                                   
            String btc = request.getParameter("bitcoin-wallet");
            String eth = request.getParameter("ethereum-wallet");            
            String[] form = new String[]{firstName, lastName, email, address, gender, dob, country, phone};
            String[] form1 = new String[]{btc, eth};

            HttpSession session = request.getSession();
            
            //Original Session atrributes
            Login login = (Login) session.getAttribute("loggedInUser");            
            
            
                
                Customer cust = new Customer();
                CustomerContact custCont = new CustomerContact();
                CustomerAccount custAcc = new CustomerAccount();

                //creates the customer constructor
                cust.setLoginId(login.getId());
                cust.setFirstName(firstName);
                cust.setLastName(lastName);
                cust.setDob(LocalDate.parse(dob));
                cust.setGender(gender);

                try {
                    CustomerDAO.addCustomer(getConnection(), cust); //add the customer constructor to db
                    System.out.println("customer added");
                    cust = CustomerDAO.getCustomer(getConnection(), login); // return the full customer constructor including the id
                    System.out.println("full customer constructor gotten");
                    
                    // creating the customer account constructor;
                    custAcc.setCustId(cust.getId());
                    custAcc.setDateModified(LocalDate.now());
                    CustomerDAO.addCustomerAccount(getConnection(), custAcc);
                    System.out.println("Customer Account added to db");

                    //creating the customer contact address
                    custCont.setCustId(cust.getId());                                       
                    custCont.setPhone(phone);
                    custCont.setAddress(address);
                    custCont.setCountry(country);
                    CustomerDAO.addCustomerAddress(getConnection(), custCont);
                    System.out.println("Customer Contact Address added to db");

                    //sets the entire customer objects
                    cust.setCustAcc(custAcc);
                    cust.setCustCont(custCont);
                    
                    if(btc.matches("")){
                        btc = "N/A";
                    }
                    
                    if(eth.matches("")){
                        eth = "N/A";
                    }
                    
                    //creates a new crypto constructors for the loggedInUser
                    BtcAccount btcAcc = new BtcAccount();                    
                    EthereumAccount ethAcc = new EthereumAccount();
                        
                    //create the address constructors
                    btcAcc.setCustomerId(cust.getId());
                    btcAcc.setAddress(btc);
                    btcAcc.setDateCreated(LocalDate.now());
                    btcAcc.setDateModified(LocalDate.now());
                       
                    
                      
                    ethAcc.setCustomerId(cust.getId());
                    ethAcc.setAddress(eth);
                    ethAcc.setDateCreated(LocalDate.now());
                    ethAcc.setDateModified(LocalDate.now());
                      
                    
                     
                    try{
                        //Adds the various coin account wallets to the database
                        TransactionDAO.addBTC(getConnection(), btcAcc);
                        System.out.println("btc wallet added");
                        
                        TransactionDAO.addETH(getConnection(), ethAcc);
                        System.out.println("eth wallet added");
                        
                         
                        custAcc.setBtcAccount(TransactionDAO.getBTC(getConnection(), cust));                        
                        custAcc.setEthAccount(TransactionDAO.getETH(getConnection(), cust));
                        
                          
                        //sets custAccount constructor to the main constructor
                        cust.setCustAcc(custAcc);
                        login.setCustomer(cust);
                        
                        
                        LoginDAO.logIn(getConnection(), login);
                        session.setAttribute("loggedInUser", login);
                        response.sendRedirect("site/members-area/members-area.jsp");
                            
                    }catch(SQLException | IllegalArgumentException e){
                        System.err.println("Error occured" + e);
                        session.setAttribute("dbError", true);                        
                        session.setAttribute("loggedInUser", login);
                        response.sendRedirect("bio-data-form.jsp");
                    } 
                } catch (SQLException | IllegalArgumentException e) {
                    System.err.println(e);
                    session.setAttribute("dbError", true);                    
                    session.setAttribute("loggedInUser", login);
                    response.sendRedirect("bio-data-form.jsp");
                }
        }        
        else if(servletPath.equals("/changePassword") || servletPath.equals("/site/members-area/changePassword")){
            String email = request.getParameter("login-email");
            String password = request.getParameter("old-password");
            String newPassword = request.getParameter("new-password");
            String retypeNewPassword = request.getParameter("retype-new-password");
            Boolean i = Boolean.valueOf(request.getParameter("i"));
            
            HttpSession session = request.getSession();
            
            //checks if new passwords match
            

            Login newLogin = new Login();
            newLogin.setEmail(email);
            newLogin.setPassword(password);
            

            try{
                newLogin = LoginDAO.verifyLogin(getConnection(), newLogin, hasher);
                if(newLogin.isVerified()){
                       
                    //sets the new Password to the constructor
                    //hashes new password
                    System.out.println("New Password hashed");
                    newLogin.setPassword(hasher.hash(newPassword.toCharArray()));
                        
                    LoginDAO.updatePassword(getConnection(), newLogin);
                    System.out.println("password updated");
                        
                    if(i == true){
                        //sends to the dashboard with the acccount settings appearing
                        session.setAttribute("detailChange", true);
                        response.sendRedirect("members-area.jsp?a=as");
                    }else{
                        //send to changePassword page
                        session.setAttribute("confirmedPasswordChange", true);
                        response.sendRedirect("change-password.jsp");
                    }
                }else{
                    if(i == true){
                        //sends to the dashboard with the acccount settings appearing
                        session.setAttribute("fieldError", true);
                        response.sendRedirect("members-area.jsp?a=as");
                    }else{
                        //create an if statement to redirect it to the members area or change passowrd page
                        session.setAttribute("fieldError", true);
                        response.sendRedirect("change-password.jsp");
                    }                        
                }
            }catch(SQLException e){
                if(i == true){
                    //sends to the dashboard with the acccount settings appearing
                    System.err.println("Error occured: " + e);
                    session.setAttribute("dbError", true);
                    response.sendRedirect("members-area.jsp?a=as");
                }else{
                    //create an if statement to redirect it to the members area or change passowrd page
                    System.err.println("Error occured: " + e);
                    session.setAttribute("dbError", true);
                    response.sendRedirect("change-password.jsp");
                }                
            }            
        }else if (servletPath.equals("/registerTradeSignal")) {
            
            HttpSession session = request.getSession();
            String email = request.getParameter("trade-signal-address");
            String password = request.getParameter("trade-signal-password");
            String renewalLength = request.getParameter("trade-signal-renewal");
            String signalType = request.getParameter("trade-signal-type");
            String[] form = new String[]{email, password, renewalLength, signalType};
            String amountPrice;

            // checking if any form inputs are empty
            if (validateForm(form)) {            
                try {
                    Login newLogin = new Login();
                    newLogin.setEmail(email);
                    newLogin.setPassword(password);
                    newLogin.setTradeType(signalType);
                    newLogin.setRenewalLength(renewalLength);
                    
                    //checks if the login and the password exists in database
                    newLogin = LoginDAO.verifyLogin(getConnection(), newLogin, hasher);
                    
                    if(newLogin.isVerified()){
                        //gets the full login constructor of the register trade signal user
                        newLogin = LoginDAO.getLogin(getConnection(), newLogin, hasher);                        
                        if(newLogin == null){
                            System.out.println("Null login returned");
                            session.setAttribute("dbError", true);
                            response.sendRedirect("trade-signals.jsp");
                        }else{
                            if(!newLogin.isVerifiedTrade()){
                                //take login Id of loggedInUser and add to tradeSignals database                           
                                TradeSignalsDAO.regTradeSignal(getConnection(), newLogin);
                                
                                //checks for the price to send to the payment page as an attribute
                                if(renewalLength.matches("3")){
                                    switch(signalType){
                                        case "FOREX":
                                            amountPrice = "499";   
                                            session.setAttribute("amount", amountPrice); 
                                            break;
                                        case "INDICES":
                                            amountPrice = "399";   
                                            session.setAttribute("amount", amountPrice);
                                            break;
                                        case "BOTH":
                                            amountPrice = "699";   
                                            session.setAttribute("amount", amountPrice);
                                            break;
                                    }
                                }
                                
                                if(renewalLength.matches("7")){
                                    switch(signalType){
                                        case "FOREX":
                                            amountPrice = "699";   
                                            session.setAttribute("amount", amountPrice); 
                                            break;
                                        case "INDICES":
                                            amountPrice = "599";   
                                            session.setAttribute("amount", amountPrice);
                                            break;
                                        case "BOTH":
                                            amountPrice = "899";   
                                            session.setAttribute("amount", amountPrice);
                                            break;
                                    }
                                    
                                }
                                
                                if(renewalLength.matches("LIFETIME")){
                                    switch(signalType){
                                        case "FOREX":
                                            amountPrice = "1599";   
                                            session.setAttribute("amount", amountPrice); 
                                            break;
                                        case "INDICES":
                                            amountPrice = "1299";   
                                            session.setAttribute("amount", amountPrice); 
                                            break;
                                        case "BOTH":
                                            amountPrice = "1999";   
                                            session.setAttribute("amount", amountPrice);
                                            break;
                                    }
                                }                                
                                response.sendRedirect("payment-page.jsp"); //sends to payment page
                                
                            }else{
                                System.out.println("user already registerd");
                                session.setAttribute("userAlreadyExists", true);
                                response.sendRedirect("trade-signals.jsp");
                            }                                                  
                        }                        
                    }else{
                        System.out.println("user not verified");
                        session.setAttribute("userNotFound", true);
                        response.sendRedirect("trade-signals.jsp");                        
                    }
                } catch (SQLException | IllegalArgumentException e) {
                    System.err.println("Error occured" + e);
                    session.setAttribute("dbError", true);
                    response.sendRedirect("trade-signals.jsp");
                }
            } else {
                session.setAttribute("fieldError", true);
                response.sendRedirect("trade-signals.jsp");
            }
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        
        String servletPath = request.getServletPath();
        
        if(servletPath.equals("/site/members-area/delete-account")){
            
            HttpSession session = request.getSession();
            Login login = (Login) session.getAttribute("loggedInUser");
            
            try{                
                //Deletes the entire login from the login database
                LoginDAO.deleteLogin(getConnection(), login);
                session.invalidate();
                response.sendRedirect("../../index.jsp");
                
            }catch(SQLException | IllegalArgumentException | NullPointerException e){
                System.err.println("Error occured" + e);
                session.setAttribute("dbError", true);
                
                //direct members area with the account settings showing 
                response.sendRedirect("members-area.jsp?a=as");
            }
        
        }
        
        else if(servletPath.equals("/site/members-area/logout")){
            
            HttpSession session = request.getSession();
            Login login = (Login) session.getAttribute("loggedInUser");
            
            try{                
                //Deletes the entire login from the login database
                LoginDAO.logOut(getConnection(), login);
                session.invalidate();
                response.sendRedirect("../../index.jsp");
                
            }catch(SQLException | IllegalArgumentException | NullPointerException e){
                System.err.println("Error occured" + e);
                session.setAttribute("dbError", true);
                
                //direct members area with the account settings showing 
                response.sendRedirect("members-area.jsp?a=as");
            }
        
        }
        
        else if(servletPath.equals("/site/admin/logout")){
            
            HttpSession session = request.getSession();
            session.invalidate();           
            response.sendRedirect("../../index.jsp");
        
        }
    
    }

    //Connection to database
    private Connection getConnection() throws SQLException {
        Connection connection = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://mysql3000.mochahost.com/tradesal_alpha-trade?serverTimezone=Africa/Lagos", "tradesal_alpha", "Q6!beils=2w%");
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
            System.out.println(form1 + "is not empty");
        }
        return valid;
    }
    
    private String[] validateWalletData(String[] form) {
        
        for (String form1 : form) {
            if (form1.matches("")) {
               form1 = "N/A";
            }
        }
        return form;
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
