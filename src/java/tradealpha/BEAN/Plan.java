package tradealpha.BEAN;

import java.time.LocalDate;

public class Plan 
{
    private int id;
    private String title;
    private double dailyIncrease;
    private LocalDate dateCreated;

    public Plan(int id, String title, double dailyIncrease, LocalDate dateCreated) {
        this.id = id;
        this.title = title;
        this.dailyIncrease = dailyIncrease;
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getDailyIncrease() {
        return dailyIncrease;
    }

    public void setDailyIncrease(double dailyIncrease) {
        this.dailyIncrease = dailyIncrease;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    
}
