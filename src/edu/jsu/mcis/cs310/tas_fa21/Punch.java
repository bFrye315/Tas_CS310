package edu.jsu.mcis.cs310.tas_fa21;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class Punch {
    
    private int terminalid;
    private Badge badge;
    private PunchType punchtype;
    private LocalDateTime originaltimestamp;

    // the 3 param constructor now sets the originaltimestamp to the current
    // time when it is made, the setOriginaltimestamp method can still change it
    // after the fact, as seen in the getPunch method in TASDatabase, but i figured 
    // that that's what was needed after looking at how the test for feature 2 was
    // written. thats the part im not sure of
    
    public Punch(int terminalid, Badge badge, int punchtypeid){
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = PunchType.values()[punchtypeid];
        this.originaltimestamp = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public void setOriginaltimestamp(LocalDateTime originaltimestamp) {
        this.originaltimestamp = originaltimestamp;
    }
    
    public Punch(int terminalid, Badge badge, int punchtypeid, LocalDateTime originaltimestamp){
    this.terminalid = terminalid;
    this.badge = badge;
    this.punchtype = PunchType.values()[punchtypeid];
    this.originaltimestamp = originaltimestamp;
    }
    
    public Punch(){
        
    }

    public int getTerminalid() {
        return terminalid;
    }

    public Badge getBadge() {
        return badge;
    }

    public PunchType getPunchtype() {
        return punchtype;
    }

    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }
    
    public void adjustmenttype(){
        
    }
    
    public String printOriginal(){//"#D2C39273 CLOCK IN: WED 09/05/2018 07:00:07"
        DateTimeFormatter format = DateTimeFormatter.ofPattern("LL/dd/uuuu HH:mm:ss");
        StringBuilder s = new StringBuilder();
        s.append("#").append(badge.getId()).append(" ").append(punchtype).append(": ").
                append(originaltimestamp.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US).toUpperCase()).
                append(" ").append(originaltimestamp.format(format));
                
        return s.toString();
    }
    
    //Feature 3
    public void adjust(Shift s){
        
    }
    
}
