/*
Problem Definition:
Badge , Shift , and Punch: Implement a new Java class for each of these business objects. 
Objects of the Badge class will contain information from the database about a single employee badge, 
and objects of the Shift class will contain information from the database about a single shift ruleset 
(that is, the starting time, stopping time, and other parameters of a single shift).
Author: Tyler
Date: 10/1/21
 */
package edu.jsu.mcis.cs310.tas_fa21;

import java.util.*;
import java.time.*;
import java.sql.*;
import com.opencsv.*;
import org.json.simple.*;

public class Shift {
     private String description;
     private String start;
     private String stop;
     private String interval;
     private String graceperiod;
     private String dock;
     private String lunchstart;
     private String lunchstop;
     private String lunchdeduct;
     int time;

    public String getDescription() {
        return description;
    }

    public String getStart() {
        /*
        “Shift Start” and “Shift Stop” to refer to the regularly scheduled starting 
        and stopping times of the employee’s shift
        */
        
        return start;
    }

    public String getStop() {
        return stop;
    }

    public String getInterval() {
        /*
        The number of minutes beforethe startof a shift, and afterthe endof a shift, 
        in which an employee's early "clock in" and late "clock out" punches are 
        adjusted forwardto the scheduled start of their shift, or backwardto the 
        end of their shift, respectively.
        */
        return interval;
    }

    public String getGraceperiod() {
        /*
        The number of minutes afterthe startof a shift, and beforethe endof a shift, 
        in which an employee's late "clock in" punches or early "clock out" punches are "forgiven." 
        */
        return graceperiod;
    }

    public String getDock() {
        /*
        If a late "clock in" punch is made toolate after the start of the shift to 
        fall within the grace period, the punch is adjusted forward in time from 
        the start of the shiftby this amount (to discourage excess tardiness).
        */
        return dock;
    }

    public String getLunchstart() {
        /*
        “Lunch Start” and “Lunch Stop” to refer to the start and stop of the shift’s 
        scheduled lunch break.
        */
        return lunchstart;
    }

    public String getLunchstop() {
        return lunchstop;
    }

    public String getLunchdeduct() {
        return lunchdeduct;
    }
     
     
}
