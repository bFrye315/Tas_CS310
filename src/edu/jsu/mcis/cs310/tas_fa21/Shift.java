/*
Author: Tyler, Brandon
Date: 10/1/21
 */
package edu.jsu.mcis.cs310.tas_fa21;

import java.time.*;
import java.util.Calendar;
import java.util.HashMap;

public class Shift {
    private final int MINPERHOUR = 60;
    private String description ;   
    private int shiftid, dailyscheduleid, lunchduration, shiftduration;    
    private DailySchedule defaultschedule;   
    private HashMap<Integer, DailySchedule> schedule;
    
    public Shift(ShiftParameters params) {
        this.description = params.getDescription();
        this.shiftid = params.getShiftid();
        this.dailyscheduleid = params.getShiftdailyscheduleid();
        this.defaultschedule = new DailySchedule(params);
        
        
        schedule = new HashMap<>();
        for (int i = Calendar.MONDAY; i <= Calendar.FRIDAY; i++){           
            schedule.put(i ,defaultschedule);  
        }
        
       
        
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
        return defaultschedule.getStart();
    }
    
    public LocalTime getStart(int day) {       
        return schedule.get(day).getStart();
    }
    
    public LocalTime getStop() {
        return defaultschedule.getStop();
    }
    
    public LocalTime getStop(int day) {       
        return schedule.get(day).getStop();
    }
    
    public int getInterval() { 
        return defaultschedule.getInterval();
    }
    
    public int getInterval(int day) { 
        return schedule.get(day).getInterval();
    }
    
    public int getGraceperiod() { 
        return defaultschedule.getGraceperiod();
    }
    
    public int getGraceperiod(int day) { 
        return schedule.get(day).getGraceperiod();
    }
    
    public int getDock() {
        return defaultschedule.getDock();
    }
    
    public int getDock(int day) {
        return schedule.get(day).getDock();
    }
    
    public LocalTime getLunchstart() {   
        return defaultschedule.getLunchstart();
    }
    
    public LocalTime getLunchstart(int day) {   
        return schedule.get(day).getLunchstart();
    }
    
    public LocalTime getLunchstop() {
        return defaultschedule.getLunchstop();
    }
    
    public LocalTime getLunchstop(int day) {
        return schedule.get(day).getLunchstop();
    }
    
    public int getLunchdeduct() {
        return defaultschedule.getLunchdeduct();
    }
    
    public int getLunchdeduct(int day) {
        return schedule.get(day).getLunchdeduct();
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

    public HashMap<Integer, DailySchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(HashMap<Integer, DailySchedule> schedule) {
        this.schedule = schedule;
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
    public void makeWeek(ShiftParameters params, Punch p){
        schedule = new HashMap<>();
        for (int i = Calendar.MONDAY; i <= Calendar.FRIDAY; i++){           
            schedule.put(i ,defaultschedule);  
        }
        
    }
    @Override
    public String toString(){
        //"Shift 1: 07:00 - 15:30 (510 minutes); Lunch: 12:00 - 12:30 (30 minutes)"
        StringBuilder s = new StringBuilder();
        s.append(description).append(": ").append(defaultschedule.getStart()).append(" - ").append(defaultschedule.getStop()).append(" (")
                .append(shiftduration).append(" minutes); Lunch: ").append(defaultschedule.getLunchstart()).append(" - ")
                .append(defaultschedule.getLunchstop()).append(" (").append(lunchduration).append(" minutes)");
        return s.toString();    
    }
}
