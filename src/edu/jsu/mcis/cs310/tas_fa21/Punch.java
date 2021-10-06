package edu.jsu.mcis.cs310.tas_fa21;

import java.time.*;
import java.sql.*;



public class Punch {
    
    private int terminalid;
    private String badgeid;
    private int punchtypeid;
    private LocalDateTime originaltimestamp;
    
    private String punchtype;
    
    
    public Punch(int terminalid, Badge badge, int punchtypeid){
    this.terminalid = terminalid;
    this.badgeid = badge.getId();
    this.punchtypeid = punchtypeid;
    }
    
    public Punch(){
        
    }

    public int getTerminalid() {
        return terminalid;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public int getPunchtypeid() {
        return punchtypeid;
    }

    public void setOriginaltimestamp(LocalDateTime originaltimestamp) {
        this.originaltimestamp = originaltimestamp;
    }

    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }
    
   

    public void adjustmenttype(){
        
    }
    
    public String printOriginal(){
        return "#" + badgeid + " " + punchtypeid + ": " ;
    }
    
}
