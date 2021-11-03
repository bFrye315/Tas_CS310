
package edu.jsu.mcis.cs310.tas_fa21;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Absenteeism {
    private String badgeid;
    private LocalDate payperiod;
    private double percentage;
    
    public Absenteeism(Badge badgeid, LocalDate payperiod, double percentage){
        this.badgeid = badgeid.getId();
        this.percentage = percentage;
        this.payperiod = payperiod;
    }
     public Absenteeism(String badgeid, LocalDate payperiod, double percentage){
        this.badgeid = badgeid;
        this.percentage = percentage;
        this.payperiod = payperiod;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public LocalDate getPayperiod() {
        return payperiod;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setBadgeid(String badgeid) {
        this.badgeid = badgeid;
    }

    public void setPayperiod(LocalDate payperiod) {
        this.payperiod = payperiod;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
    @Override
    public String toString(){//#28DC3FB8 (Pay Period Starting 09-02-2018): 2.50%
        DateTimeFormatter format = DateTimeFormatter.ofPattern("LL-dd-uuuu");
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuilder s = new StringBuilder();
        
        s.append("#").append(badgeid).append(" (Pay Period Starting ").append(payperiod.format(format)).append("): ").append(df.format(percentage)).append("%");
        
        return s.toString();
    }
}
