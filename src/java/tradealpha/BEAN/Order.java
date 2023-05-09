package tradealpha.BEAN;
import java.time.LocalDate;

public class Order 
{
    private int id;
    private String type;
    private String mop;
    private String amount;
    private LocalDate dateCreated;    
    private LocalDate dateConfirmed;    
    private String plan;
    private String status;
    private int loginId;
    

    public Order(int id, String type, String mop, String plan, LocalDate dateCreated, String status) {
        this.id = id;        
        this.type = type;
        this.mop = mop;
        this.dateCreated = dateCreated;
        this.status = status;
        this.plan = plan;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMop() {
        return mop;
    }

    public void setMop(String mop) {
        this.mop = mop;
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

    public LocalDate getDateConfirmed() {
        return dateConfirmed;
    }

    public void setDateConfirmed(LocalDate dateConfirmed) {
        this.dateConfirmed = dateConfirmed;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
    
    
    
    
}
