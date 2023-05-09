package tradealpha.BEAN;

import java.time.LocalDate;

public class Withdraw 
{
    private int id;
    private int custId;
    private float amount;
    private String mop;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private String status;

    public Withdraw(int id, int custId, float amount, LocalDate dateCreated, String status) {
        this.id = id;
        this.custId = custId;
        this.amount = amount;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public Withdraw() {
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public String getMop() {
        return mop;
    }

    public void setMop(String mop) {
        this.mop = mop;
    }
    
    
    
}
