package tradealpha.BEAN;

import java.time.LocalDate;
import java.util.Date; //how to use the date function when insering into a database
import java.util.ArrayList;

public class Login 
{
    private int id;
    private String email;
    private String password;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private LocalDate lastLogin;
    private String refCode;
    private String role;
    private String accStatus;
    private int noOfRefs;
    private int noOfActiveRefs;
    private float refCommission;
    private boolean verified;
    private boolean verifiedTrade;
    private String tradeSignalType;
    
    //for registering for trade signals 
    private String tradeType;
    private String renewalLength;
    
    
    private ArrayList<TradeSignals> tsList = new ArrayList<>();
    private ArrayList<Order> orderList = new ArrayList<>();
    private boolean blocked;
    private Customer customer;

    public Login(int id) {
        this.id = id;
    }

    public Login(int id, String email, String password, LocalDate dateCreated, LocalDate dateModified, String refCode, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;        
        this.refCode = refCode;
        this.role = role;
  
    }

    public Login() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    

    public boolean isVerified() {
        return verified;
    }

    public void setIsVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerifiedTrade() {
        return verifiedTrade;
    }

    public void setVerifiedTrade(boolean verifiedTrade) {
        this.verifiedTrade = verifiedTrade;
    }
    
    

    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getDateCreated() {
        java.util.Date date = java.sql.Date.valueOf(dateCreated);
        return date;
    }

    public Date getDateModified() {
        java.util.Date date = java.sql.Date.valueOf(dateModified);
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }
    

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }
    
    public LocalDate getDateCreated1() {
        return dateCreated;
    }

    public LocalDate getDateModified1() {
        return dateModified;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public int getNoOfRefs() {
        return noOfRefs;
    }

    public void setNoOfRefs(int noOfRefs) {
        this.noOfRefs = noOfRefs;
    }

    public int getNoOfActiveRefs() {
        return noOfActiveRefs;
    }

    public void setNoOfActiveRefs(int noOfActiveRefs) {
        this.noOfActiveRefs = noOfActiveRefs;
    }

    public float getRefCommission() {
        return refCommission;
    }

    public void setRefCommission(float refCommission) {
        this.refCommission = refCommission;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public ArrayList<TradeSignals> getTsList() {
        return tsList;
    }

    public void setTsList(ArrayList<TradeSignals> tsList) {
        this.tsList = tsList;
    }
    
    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getRenewalLength() {
        return renewalLength;
    }

    public void setRenewalLength(String renewalLength) {
        this.renewalLength = renewalLength;
    }

    public String getTradeSignalType() {
        return tradeSignalType;
    }

    public void setTradeSignalType(String tradeSignalType) {
        this.tradeSignalType = tradeSignalType;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }
    
    
    
    
}

