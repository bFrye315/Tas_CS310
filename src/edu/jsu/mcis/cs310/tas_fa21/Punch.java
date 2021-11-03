package edu.jsu.mcis.cs310.tas_fa21;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Punch {
    
    private int terminalid;
    private int id;
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

    public Punch(int terminalid, Badge badge, int punchtypeid, LocalDateTime originaltimestamp){
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = PunchType.values()[punchtypeid];
        this.originaltimestamp = originaltimestamp;
        
    }
     public void setOriginaltimestamp(LocalDateTime originaltimestamp) {
        this.originaltimestamp = originaltimestamp;
    }

    public int getId() {
        return id;
    }

    public String getAdjustmenttype() {
        return adjustmenttype;
    }

    public void setId(int id) {
        this.id = id;
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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE " +"LL/dd/uuuu HH:mm:ss");
        StringBuilder s = new StringBuilder();
        s.append("#").append(badge.getId()).append(" ").append(punchtype).append(": ").
                append(originaltimestamp.format(format).toUpperCase());
                
        return s.toString();
    }
      
    public String printAdjusted(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE " + "LL/dd/uuuu HH:mm:ss");
        StringBuilder s = new StringBuilder();
        s.append("#").append(badge.getId()).append(" ").append(punchtype).append(": ").
                append(adjustedtimestamp.format(format).toUpperCase()).
                append(" (").
                append(adjustmenttype).
                append(")");
                
        return s.toString();
    }
    
    public void adjust(Shift s){
        LocalDateTime punchTime = originaltimestamp;
        final int ZERO = 0;
        LocalDateTime zeroPT = punchTime.withSecond(ZERO).withNano(ZERO);
        
        LocalDateTime adjustedPT = null;
        
        LocalDateTime shiftStart = s.getStart().atDate(originaltimestamp.toLocalDate());
        LocalDateTime shiftStop = s.getStop().atDate(originaltimestamp.toLocalDate());
        LocalDateTime lunchStart = s.getLunchstart().atDate(originaltimestamp.toLocalDate());
        LocalDateTime lunchStop = s.getLunchstop().atDate(originaltimestamp.toLocalDate());
        
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
        else if ((punchTime.isAfter(shiftStart)) && (punchTime.isBefore(shiftStart.plusMinutes(interval)) 
                || punchTime.equals(shiftStart.plusMinutes(interval))) && (punchtype != PunchType.CLOCK_OUT) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY))){
            
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
        else if((punchTime.isBefore(shiftStart)) && (punchTime.isAfter(shiftStart.minusMinutes(interval)) 
                || punchTime.equals(shiftStart.minusMinutes(interval))) && (punchtype != PunchType.CLOCK_OUT) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY)) ){
                adjustedPT = shiftStart;
                this.adjustmenttype = "Shift Start";
        }
       
        
        //punch out early stop
        else if((punchTime.isBefore(shiftStop)) && (punchTime.isAfter(shiftStop.minusMinutes(interval)) 
                || punchTime.equals(shiftStop.minusMinutes(interval))) && (punchtype != PunchType.CLOCK_IN) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY))){
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
        else if((punchTime.isAfter(shiftStop)) && (punchTime.isBefore(shiftStop.plusMinutes(interval)) 
                || punchTime.equals(shiftStop.plusMinutes(interval))) && (punchtype != PunchType.CLOCK_IN) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY))){
            adjustedPT = shiftStop;
            this.adjustmenttype = "Shift Stop";
        }
        
        // punch in late lunchstart
        else if((punchTime.isAfter(lunchStart)) && (punchTime.isBefore(lunchStop)) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SATURDAY)) 
                && (!originaltimestamp.getDayOfWeek().equals(DayOfWeek.SUNDAY))){
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
        else if ((!(min == interval)) && (!(min == (interval  + interval))) && (!(min == (interval + interval + interval))) && (!(min == ZERO))){
            int mod = min % interval;
            
            if(mod < Math.ceil(interval / 2.0)){
                if(mod == 7 && sec < interval + interval){
                    adjustedPT = punchTime.minusMinutes(mod).withSecond(0);
                    this.adjustmenttype = "Interval Round";   
                }
                else if (mod == 7 && sec >= interval + interval) {
                    adjustedPT = punchTime.plusMinutes(interval - mod).withSecond(0);
                    this.adjustmenttype = "Interval Round";
                }
                else{
                    adjustedPT = punchTime.minusMinutes(mod).withSecond(0);
                    this.adjustmenttype = "Interval Round";
                }
                
            }
            else if(mod > 8){
                adjustedPT = punchTime.plusMinutes(interval - mod).withSecond(0);
                this.adjustmenttype = "Interval Round";
            }
           
           
            
        }
        else{
            adjustedPT = punchTime.withSecond(ZERO).withNano(ZERO);
            this.adjustmenttype = "None";
        }

        this.adjustedtimestamp = adjustedPT;
    }
}
