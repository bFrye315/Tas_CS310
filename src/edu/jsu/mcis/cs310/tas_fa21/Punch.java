package edu.jsu.mcis.cs310.tas_fa21;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Punch {
    
    private int terminalid;
    private Badge badge;
    private PunchType punchtype;
    private LocalDateTime originaltimestamp;
    private LocalDateTime adjustedtimestamp;
    private String adjustmenttype;

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
    
    public LocalDateTime getAdjustedtimestamp() {
        return adjustedtimestamp;
    }
    
    public String printOriginal(){//"#D2C39273 CLOCK IN: WED 09/05/2018 07:00:07"
        DateTimeFormatter format = DateTimeFormatter.ofPattern("LL/dd/uuuu HH:mm:ss");
        StringBuilder s = new StringBuilder();
        s.append("#").append(badge.getId()).append(" ").append(punchtype).append(": ").
                append(originaltimestamp.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US).toUpperCase()).
                append(" ").append(originaltimestamp.format(format));
                
        return s.toString();
    }
      
    public String printAdjusted(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("LL/dd/uuuu HH:mm:ss");
        StringBuilder s = new StringBuilder();
        s.append("#").append(badge.getId()).append(" ").append(punchtype).append(": ").
                append(adjustedtimestamp.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US).toUpperCase()).
                append(" ").
                append(adjustedtimestamp.format(format)).
                append(" (").
                append(adjustmenttype).
                append(")");
                
        return s.toString();
    }
    
    public void adjust(Shift s){
        LocalTime punchTime = originaltimestamp.toLocalTime();
        final int ZERO = 0;
        LocalTime zeroPT = punchTime.withSecond(0).withNano(0);
        
        LocalTime adjustedPT = null;
        
        LocalTime shiftStart = s.getStart();
        LocalTime shiftStop = s.getStop();
        LocalTime lunchStart = s.getLunchstart();
        LocalTime lunchStop = s.getLunchstop();
        
        int interval = s.getInterval();
        int gracePeriod = s.getGraceperiod();
        int dock = s.getDock();
        
        
       
        int min = punchTime.getMinute();
        int sec = punchTime.getSecond();
       
        
       
        
        // on time
        if(zeroPT.equals(shiftStart) || zeroPT.equals(shiftStop) || zeroPT.equals(lunchStart) || zeroPT.equals(lunchStop)){
            if (zeroPT.equals(shiftStart)){
                adjustedPT = shiftStart;
                this.adjustmenttype = "None";
                
            }
            else if(zeroPT.equals(shiftStop)){
                adjustedPT = shiftStop;
                this.adjustmenttype = "None";
            }
            else if(zeroPT.equals(lunchStart)){
                adjustedPT = lunchStart;
                this.adjustmenttype = "None";
            }
            else{
                adjustedPT = lunchStop;
                this.adjustmenttype = "None";
            }
            
        }
        // punch in late start
        else if ((punchTime.isAfter(shiftStart)) && (punchTime.isBefore(shiftStart.plusMinutes(interval)) || punchTime.equals(shiftStart.plusMinutes(interval))) && (punchtype != PunchType.CLOCK_OUT) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY))){
            
            if(punchTime.isBefore(shiftStart.plusMinutes(gracePeriod))){
                adjustedPT = shiftStart;
                this.adjustmenttype = "Shift Start";
            }
            else{
                adjustedPT = shiftStart.plusMinutes(dock);
                this.adjustmenttype = "Shift Dock";
            }

        }
        // punch in early start
        else if((punchTime.isBefore(shiftStart)) && (punchTime.isAfter(shiftStart.minusMinutes(interval)) || punchTime.equals(shiftStart.minusMinutes(interval))) && (punchtype != PunchType.CLOCK_OUT) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY)) ){
                adjustedPT = shiftStart;
                this.adjustmenttype = "Shift Start";
        }
       
        
        //punch out early stop
        else if((punchTime.isBefore(shiftStop)) && (punchTime.isAfter(shiftStop.minusMinutes(interval)) || punchTime.equals(shiftStop.minusMinutes(interval))) && (punchtype != PunchType.CLOCK_IN) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY))){
            if(punchTime.isAfter(shiftStop.minusMinutes(gracePeriod))){
                adjustedPT = shiftStop;
                this.adjustmenttype = "Shift Stop";
            }
            else{
                adjustedPT = shiftStop.minusMinutes(dock);
                this.adjustmenttype = "Shift Dock";
             }        
        }
        
        // punch out late stop
        else if((punchTime.isAfter(shiftStop)) && (punchTime.isBefore(shiftStop.plusMinutes(interval)) || punchTime.equals(shiftStop.plusMinutes(interval))) && (punchtype != PunchType.CLOCK_IN) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY))){
            adjustedPT = shiftStop;
            this.adjustmenttype = "Shift Stop";
        }
        
        // punch in late lunchstart
        else if((punchTime.isAfter(lunchStart)) && (punchTime.isBefore(lunchStop)) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY))){
            if(punchtype.equals(PunchType.CLOCK_OUT)){
                adjustedPT = lunchStart;
                this.adjustmenttype = "Lunch Start";
            }
            else if(punchtype.equals(PunchType.CLOCK_IN)){
                adjustedPT = lunchStop;
                this.adjustmenttype = "Lunch Stop";
            }
            
        }
        
        // interval round
        else if ((!(min == interval)) && (!(min == (interval * 2))) && (!(min == (interval * 3))) && (!(min == ZERO))){
            int mod = min % 15;
            if(mod < 8){
                if(sec < 30){
                    adjustedPT = punchTime.minusMinutes(mod).withSecond(0);
                    this.adjustmenttype = "Interval Round";   
                }
                else{
                    adjustedPT = punchTime.plusMinutes(15 - mod).withSecond(0);
                    this.adjustmenttype = "Interval Round";
                }
                
            }
            else if(mod > 8){
                adjustedPT = punchTime.plusMinutes(15 - mod).withSecond(0);
                this.adjustmenttype = "Interval Round";
            }
           
           
            
        }
        else{
            adjustedPT = punchTime.withSecond(ZERO).withNano(ZERO);
            this.adjustmenttype = "None";
        }

        this.adjustedtimestamp = LocalDateTime.of(originaltimestamp.toLocalDate(), adjustedPT);
    }
    
    
    public String toString(){
        return "#" + badge.getId() + " " + punchtype + ": " + originaltimestamp;
    }
}
