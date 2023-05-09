package tradealpha.BEAN;

import java.time.LocalDate;

public class Referral 
{
    private int id;
    private String refTo;
    private String refBy;
    private LocalDate dateCreated;

    public Referral(int id, String refTo, String refBy, LocalDate dateCreated) {
        this.id = id;
        this.refTo = refTo;
        this.refBy = refBy;
        this.dateCreated = dateCreated;
    }

    public Referral() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRefTo() {
        return refTo;
    }

    public void setRefTo(String refTo) {
        this.refTo = refTo;
    }

    public String getRefBy() {
        return refBy;
    }

    public void setRefBy(String refBy) {
        this.refBy = refBy;
    }
    
    


    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    
}
