package tradealpha.BEAN;

import java.util.ArrayList;

public class Admin 
{
    ArrayList<Login> loginList = new ArrayList<>();
    ArrayList<Login> loggedInUsers = new ArrayList<>();
    ArrayList<Order> orderList = new ArrayList<>();
    ArrayList<Order> mailingList = new ArrayList<>();
    ArrayList<Order> regTradeList = new ArrayList<>();

    public ArrayList<Login> getLoginList() {
        return loginList;
    }

    public void setLoginList(ArrayList<Login> loginList) {
        this.loginList = loginList;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    public ArrayList<Order> getMailingList() {
        return mailingList;
    }

    public void setMailingList(ArrayList<Order> mailingList) {
        this.mailingList = mailingList;
    }

    public ArrayList<Login> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void setLoggedInUsers(ArrayList<Login> loggedInUsers) {
        this.loggedInUsers = loggedInUsers;
    }

    public ArrayList<Order> getRegTradeList() {
        return regTradeList;
    }

    public void setRegTradeList(ArrayList<Order> regTradeList) {
        this.regTradeList = regTradeList;
    }
    
}
