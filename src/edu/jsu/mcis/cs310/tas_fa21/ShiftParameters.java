package edu.jsu.mcis.cs310.tas_fa21;

import java.time.*;

public class ShiftParameters {
    
    private int shiftid, overrideid, interval, graceperiod, day, dock, lunchdeduct, shiftdailyscheduleid, overrideschedule;
    private LocalTime start, stop, lunchstart, lunchstop;
    private LocalDate overridestart, overrideend;
    private String description, badgeid;

    public int getShiftid() {
        return shiftid;
    }

    public void setShiftid(int shiftid) {
        this.shiftid = shiftid;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getGraceperiod() {
        return graceperiod;
    }

    public void setGraceperiod(int graceperiod) {
        this.graceperiod = graceperiod;
    }

    public int getDock() {
        return dock;
    }

    public void setDock(int dock) {
        this.dock = dock;
    }

    public int getLunchdeduct() {
        return lunchdeduct;
    }

    public void setLunchdeduct(int lunchdeduct) {
        this.lunchdeduct = lunchdeduct;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getStop() {
        return stop;
    }

    public void setStop(LocalTime stop) {
        this.stop = stop;
    }

    public LocalTime getLunchstart() {
        return lunchstart;
    }

    public void setLunchstart(LocalTime lunchstart) {
        this.lunchstart = lunchstart;
    }

    public LocalTime getLunchstop() {
        return lunchstop;
    }

    public void setLunchstop(LocalTime lunchstop) {
        this.lunchstop = lunchstop;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getShiftdailyscheduleid() {
        return shiftdailyscheduleid;
    }

    public void setShiftdailyscheduleid(int dailyscheduleid) {
        this.shiftdailyscheduleid = dailyscheduleid;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getOverrideschedule() {
        return overrideschedule;
    }

    public void setOverrideschedule(int overrideschedule) {
        this.overrideschedule = overrideschedule;
    }

    public int getOverrideid() {
        return overrideid;
    }

    public void setOverrideid(int overrideid) {
        this.overrideid = overrideid;
    }

    public LocalDate getOverridestart() {
        return overridestart;
    }

    public void setOverridestart(LocalDate overridestart) {
        this.overridestart = overridestart;
    }

    public LocalDate getOverrideend() {
        return overrideend;
    }

    public void setOverrideend(LocalDate overrideend) {
        this.overrideend = overrideend;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public void setBadgeid(String badgeid) {
        this.badgeid = badgeid;
    }
    
}
