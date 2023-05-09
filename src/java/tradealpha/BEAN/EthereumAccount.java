package tradealpha.BEAN;

import java.time.LocalDate;


public class EthereumAccount 
{
    private int id;
    private int customerId;
    private String address;
    private LocalDate dateCreated;
    private LocalDate dateModified;

    public EthereumAccount (int id, int customerId, String address, LocalDate dateCreated, LocalDate dateModified) {
        this.id = id;
        this.customerId = customerId;
        this.address = address;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    public EthereumAccount () {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }
    
    
}
