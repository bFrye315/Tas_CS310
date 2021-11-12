/*
Author: Tyler, Brandon
Date: 10/1/21
 */
package edu.jsu.mcis.cs310.tas_fa21;

import java.time.*;

public class Shift {
    private final int MINPERHOUR = 60;
    private String description ;   
    private int shiftid, dailyscheduleid, lunchduration, shiftduration;    
    private DailySchedule ds;
    
    public Shift(ShiftParameters params) {
        this.description = params.getDescription();
        this.shiftid = params.getId();
        this.dailyscheduleid = params.getDailyscheduleid();
        this.ds = new DailySchedule(params);
        
        setShiftduration(params.getStart(), params.getStop());
        setLunchduration(params.getLunchstart(), params.getLunchstop());
    }
 
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalTime getStart() {       
        return ds.getStart();
    }

    public LocalTime getStop() {
        return ds.getStop();
    }

    public int getInterval() { 
        return ds.getInterval();
    }

    public int getGraceperiod() { 
        return ds.getGraceperiod();
    }

    public int getDock() {
        return ds.getDock();
    }

    public LocalTime getLunchstart() {   
        return ds.getLunchstart();
    }

    public LocalTime getLunchstop() {
        return ds.getLunchstop();
    }

    public int getLunchdeduct() {
        return ds.getLunchdeduct();
    }
    
    public int getShiftid() {
        return shiftid;
    }

    public void setShiftid(int shiftid) {
        this.shiftid = shiftid;
    }

    
    public int getDailyscheduleid() {
        return dailyscheduleid;
    }

    public void setDailyscheduleid(int dailyscheduleid) {
        this.dailyscheduleid = dailyscheduleid;
    }
    
    
    public int getLunchduration() {
        return lunchduration;
    }

    public int getShiftduration() {
        return shiftduration;
    }
    
    private void setShiftduration(LocalTime start, LocalTime stop){
        int startmin = start.getHour() * MINPERHOUR + start.getMinute();
        int stopmin = stop.getHour() * MINPERHOUR + stop.getMinute();
      
        this.shiftduration = stopmin - startmin; 
    }
    
    private void setLunchduration(LocalTime lunchstart, LocalTime lunchstop){
        int startmin = (lunchstart.getHour() * MINPERHOUR) + lunchstart.getMinute();
        int stopmin = (lunchstop.getHour() * MINPERHOUR) + lunchstop.getMinute();
       
        this.lunchduration = stopmin - startmin;      
    }
    
    @Override
    public String toString(){
        //"Shift 1: 07:00 - 15:30 (510 minutes); Lunch: 12:00 - 12:30 (30 minutes)"
        StringBuilder s = new StringBuilder();
        s.append(description).append(": ").append(ds.getStart()).append(" - ").append(ds.getStop()).append(" (")
                .append(shiftduration).append(" minutes); Lunch: ").append(ds.getLunchstart()).append(" - ")
                .append(ds.getLunchstop()).append(" (").append(lunchduration).append(" minutes)");
        return s.toString();    
    }
}
