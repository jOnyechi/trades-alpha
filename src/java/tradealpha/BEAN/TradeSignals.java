package tradealpha.BEAN;

import java.time.*;

public class TradeSignals 
{
    private int id;
    private String signalType;
    private String currencyPair;
    private String orderType;
    private String entryPoint;
    private String stopLoss;
    private String tp1;
    private String tp2;
    private LocalDate dateCreated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public String getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(String stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getTp1() {
        return tp1;
    }

    public void setTp1(String tp1) {
        this.tp1 = tp1;
    }

    public String getTp2() {
        return tp2;
    }

    public void setTp2(String tp2) {
        this.tp2 = tp2;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getSignalType() {
        return signalType;
    }

    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }

    
    
}
