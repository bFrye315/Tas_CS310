package edu.jsu.mcis.cs310.tas_fa21;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class Punch {
    
    private int terminalid;
    private String badgeid;
    private PunchType punchtypeid;
    private LocalDateTime originaltimestamp;

    
    public Punch(int terminalid, Badge badge, int punchtypeid, LocalDateTime originaltimestamp){
    this.terminalid = terminalid;
    this.badgeid = badge.getId();
    this.punchtypeid = PunchType.values()[punchtypeid];
    this.originaltimestamp = originaltimestamp;
    }
    
    public Punch(){
        
    }

    public int getTerminalid() {
        return terminalid;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public PunchType getPunchtypeid() {
        return punchtypeid;
    }

    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }
    
    public void adjustmenttype(){
        
    }
    
    public String printOriginal(){//"#D2C39273 CLOCK IN: WED 09/05/2018 07:00:07"
        DateTimeFormatter format = DateTimeFormatter.ofPattern("E LL/dd/uuuu HH:mm:ss");
        StringBuilder s = new StringBuilder();
        s.append("#").append(badgeid).append(" ").append(punchtypeid).append(": ").append(originaltimestamp.format(format));
                //append(" ").append(originaltimestamp.getMonthValue()).append("/").append(originaltimestamp.getDayOfMonth()).
                //append("/").append(originaltimestamp.getYear()).append(" ").append(originaltimestamp.getHour()).append(originaltimestamp.for));
        return s.toString();
    }
    
}
