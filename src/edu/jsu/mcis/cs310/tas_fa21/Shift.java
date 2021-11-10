/*
Problem Definition:
Badge , Shift , and Punch: Implement a new Java class for each of these business objects. 
Objects of the Badge class will contain information from the database about a single employee badge, 
and objects of the Shift class will contain information from the database about a single shift ruleset 
(that is, the starting time, stopping time, and other parameters of a single shift).
Author: Tyler, Brandon
Date: 10/1/21
 */
package edu.jsu.mcis.cs310.tas_fa21;


import java.time.*;

// in shift I removed the original constructor that was just commented out as well as imports that were unused
public class Shift {
    private final int MINPERHOUR = 60;

    
    private String description;
   
    private int shiftid;
    private int lunchduration;
    private int shiftduration; 
    
    private DailySchedule ds;
    
    public Shift(ShiftParameters params) {
        this.description = params.getDescription();

        this.shiftid = params.getId();
        
        this.ds = new DailySchedule(params);
        
        setShiftduration(params.getStart(), params.getStop());
        setLunchduration(params.getLunchstart(), params.getLunchstop());
    }
     public Shift(Badge badgeid){
         
    }

     
    public String getDescription() {
        return description;
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
