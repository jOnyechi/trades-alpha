package tradealpha.BEAN;

import java.time.LocalDate;
import sun.nio.cs.ext.TIS_620;

public class Deposit 
{
    private int id;
    private int custId;
    private int planId;
    private int lengthOfContract;
    private float amount;
    private String mop;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private String status;

    public Deposit(int id, int custId, int planId, int lengthOfContract, float amount, LocalDate dateCreated, String status, String mop) {
        this.id = id;
        this.custId = custId;
        this.planId = planId;
        this.lengthOfContract = lengthOfContract;
        this.amount = amount;
        this.dateCreated = dateCreated;
        this.status = status;
        this.mop = mop;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    
    
    public Deposit() {
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

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getLengthOfContract() {
        return lengthOfContract;
    }

    public void setLengthOfContract(int lengthOfContract) {
        this.lengthOfContract = lengthOfContract;
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

    public String getMop() {
        return mop;
    }

    public void setMop(String mop) {
        this.mop = mop;
    }
    
    
    
    
}
