
package edu.jsu.mcis.cs310.tas_fa21;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Absenteeism {
    private String badgeid;
    private LocalDateTime payperiod;
    private double percentage;
    
    public Absenteeism(Badge badgeid, LocalDate payperiod, double percentage){
        this.badgeid = badgeid.getId();
        this.percentage = percentage;
        this.payperiod = setToStartOfWeek(payperiod).atTime(0, 0, 0);
    }
    public Absenteeism(String badgeid, LocalDate payperiod, double percentage){
        this.badgeid = badgeid;
        this.percentage = percentage;
        this.payperiod = setToStartOfWeek(payperiod).atTime(0, 0, 0);
    }
    private LocalDate setToStartOfWeek(LocalDate day){
     
        switch(day.getDayOfWeek().getValue()){
            case 1:
                day = day.minusDays(1);
                break;
            case 2:
                day = day.minusDays(2);
                break;
            case 3:
                day = day.minusDays(3);
                break;
            case 4:
                day = day.minusDays(4);
                break;
            case 5:
                day = day.minusDays(5);
                break;    
            case 6:
                day = day.minusDays(6);
                break;
            case 7:
                day = day;
                break;
        }
        return day;
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

    public void setPayperiod(LocalDate payperiod) {
        this.payperiod = setToStartOfWeek(payperiod).atTime(0, 0, 0);
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
