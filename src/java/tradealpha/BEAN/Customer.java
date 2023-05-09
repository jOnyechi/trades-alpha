package tradealpha.BEAN;

import java.time.LocalDate;

public class Customer 
{
    private int id;
    private int loginId;
    private LocalDate dob;
    private String gender;
    private String firstName;
    private String lastName;
    private CustomerAccount custAcc;
    private CustomerContact custCont;
    

    public Customer(int id, int loginId, LocalDate dob, String gender, String firstName, String lastName) {
        this.id = id;
        this.loginId = loginId;
        this.dob = dob;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public CustomerAccount getCustAcc() {
        return custAcc;
    }

    public void setCustAcc(CustomerAccount custAcc) {
        this.custAcc = custAcc;
    }

    public CustomerContact getCustCont() {
        return custCont;
    }

    public void setCustCont(CustomerContact custCont) {
        this.custCont = custCont;
    }
    
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    
    
}
