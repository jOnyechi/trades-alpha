package tradealpha.BEAN;

import java.time.LocalDate;

public class CustomerAccount 
{
    private int id;
    private int custId;
    private float balance;
    private LocalDate dateModified;
    private BtcAccount btcAccount;    
    private EthereumAccount ethAccount;   

    public CustomerAccount(int id, int custId, float balance, LocalDate dateModified) {
        this.id = id;
        this.custId = custId;
        this.balance = balance;
        this.dateModified = dateModified;
    }

    public CustomerAccount() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public BtcAccount getBtcAccount() {
        return btcAccount;
    }

    public void setBtcAccount(BtcAccount btcAccount) {
        this.btcAccount = btcAccount;
    }

    public EthereumAccount getEthAccount() {
        return ethAccount;
    }

    public void setEthAccount(EthereumAccount ethAccount) {
        this.ethAccount = ethAccount;
    }
    
}
