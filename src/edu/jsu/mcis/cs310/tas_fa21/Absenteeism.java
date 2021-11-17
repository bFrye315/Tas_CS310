
package edu.jsu.mcis.cs310.tas_fa21;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Absenteeism {
    private String badgeid;
    private LocalDateTime payperiod;
    private double percentage;
    private Badge badge;
    
    public Absenteeism(Badge badgeid, LocalDate payperiod, double percentage){
        this.badgeid = badgeid.getId();
        this.percentage = percentage;
        
        this.payperiod = payperiod.atTime(0, 0, 0);
        this.badge = badgeid;
    }
    public Absenteeism(String badgeid, LocalDate payperiod, double percentage){
        this.badgeid = badgeid;
        this.percentage = percentage;
        this.payperiod = payperiod.atTime(0, 0, 0);
       
    }
   
    public Badge getBadge() {
        return badge;
    }
    
    public String getBadgeid() {
        return badgeid;
    }

    public LocalDateTime getPayperiod() {
        return payperiod;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setBadgeid(String badgeid) {
        this.badgeid = badgeid;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }
    
    @Override
    public String toString(){//#28DC3FB8 (Pay Period Starting 09-02-2018): 2.50%
        DateTimeFormatter format = DateTimeFormatter.ofPattern("LL-dd-uuuu");
        StringBuilder s = new StringBuilder();
        
        s.append("#").append(badgeid).append(" (Pay Period Starting ").append(payperiod.format(format)).append("): ").append(String.format("%.2f" , percentage)).append("%");
        
        return s.toString();
    }
}
