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
    
    private int interval;
    private int graceperiod;
    private int dock;
    private int lunchdeduct;
    private int shiftid;
    private int lunchduration;
    private int shiftduration; 
    
    private LocalTime start;
    private LocalTime stop;
    private LocalTime lunchstart; 
    private LocalTime lunchstop;
    
    public Shift(ShiftParameters params) {
        this.description = params.getDescription();
    
        this.interval = params.getInterval();
        this.graceperiod = params.getGraceperiod();
        this.dock = params.getDock();
        this.lunchdeduct = params.getLunchdeduct();
 
    
        this.start = params.getStart();
        this.stop = params.getStop();
        this.lunchstart = params.getLunchstart();
        this.lunchstop = params.getLunchstop();
        this.shiftid = params.getId();
        this.
        setShiftduration(params.getStart(), params.getStop());
        setLunchduration(params.getLunchstart(), params.getLunchstop());
    }
     public Shift(Badge badgeid){
         
     }

     
    public String getDescription() {
        return description;
    }

    public LocalTime getStart() {
        /*
        “Shift Start” and “Shift Stop” to refer to the regularly scheduled starting 
        and stopping times of the employee’s shift
        */
        
        return start;
    }

    public LocalTime getStop() {
        return stop;
    }

    public int getInterval() {
        /*
        The number of minutes beforethe startof a shift, and afterthe endof a shift, 
        in which an employee's early "clock in" and late "clock out" punches are 
        adjusted forwardto the scheduled start of their shift, or backwardto the 
        end of their shift, respectively.
        */
        return interval;
    }

    public int getGraceperiod() {
        /*
        The number of minutes afterthe startof a shift, and beforethe endof a shift, 
        in which an employee's late "clock in" punches or early "clock out" punches are "forgiven." 
        */
        return graceperiod;
    }

    public int getDock() {
        /*
        If a late "clock in" punch is made toolate after the start of the shift to 
        fall within the grace period, the punch is adjusted forward in time from 
        the start of the shiftby this amount (to discourage excess tardiness).
        */
        return dock;
    }

    public LocalTime getLunchstart() {
        /*
        “Lunch Start” and “Lunch Stop” to refer to the start and stop of the shift’s 
        scheduled lunch break.
        */
        return lunchstart;
    }

    public LocalTime getLunchstop() {
        return lunchstop;
    }

    public int getLunchdeduct() {
        return lunchdeduct;
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
        s.append(description).append(": ").append(start).append(" - ").append(stop).append(" (")
                .append(shiftduration).append(" minutes); Lunch: ").append(lunchstart).append(" - ")
                .append(lunchstop).append(" (").append(lunchduration).append(" minutes)");
        return s.toString();
        
        
    }
}
