package edu.jsu.mcis.cs310.tas_fa21;

import java.time.LocalTime;


public class DailySchedule {
    private int interval;
    private int graceperiod;
    private int dock;
    private int lunchdeduct;
    
    private LocalTime start;
    private LocalTime stop;
    private LocalTime lunchstart; 
    private LocalTime lunchstop;
    
    public DailySchedule(ShiftParameters params) {
        this.interval = params.getInterval();
        this.graceperiod = params.getGraceperiod();
        this.dock = params.getDock();
        this.lunchdeduct = params.getLunchdeduct();
 
    
        this.start = params.getStart();
        this.stop = params.getStop();
        this.lunchstart = params.getLunchstart();
        this.lunchstop = params.getLunchstop();
    }

    //Getter 
    public int getInterval() {
        return interval;
    }

    public int getGraceperiod() {
        return graceperiod;
    }

    public int getDock() {
        return dock;
    }

    public int getLunchdeduct() {
        return lunchdeduct;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getStop() {
        return stop;
    }

    public LocalTime getLunchstart() {
        return lunchstart;
    }

    public LocalTime getLunchstop() {
        return lunchstop;
    }

    //Setter
    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setGraceperiod(int graceperiod) {
        this.graceperiod = graceperiod;
    }

    public void setDock(int dock) {
        this.dock = dock;
    }

    public void setLunchdeduct(int lunchdeduct) {
        this.lunchdeduct = lunchdeduct;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public void setStop(LocalTime stop) {
        this.stop = stop;
    }

    public void setLunchstart(LocalTime lunchstart) {
        this.lunchstart = lunchstart;
    }

    public void setLunchstop(LocalTime lunchstop) {
        this.lunchstop = lunchstop;
    }
    
}

