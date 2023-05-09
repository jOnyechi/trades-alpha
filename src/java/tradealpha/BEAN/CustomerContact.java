package tradealpha.BEAN;


public class CustomerContact 
{
    private int id;
    private int custId;
    private String phone;
    private String address;
    private String country;

    public CustomerContact(int id, int custId, String phone, String address, String country) {
        this.id = id;
        this.custId = custId;
        this.phone = phone;
        this.address = address;
        this.country = country;
    }

    public CustomerContact() {
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


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    
}
