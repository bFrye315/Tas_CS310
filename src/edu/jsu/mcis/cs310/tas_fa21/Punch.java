package edu.jsu.mcis.cs310.tas_fa21;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Punch {
    
    private int terminalid, id;
    private Badge badge;
    private PunchType punchtype;
    private LocalDateTime originaltimestamp, adjustedtimestamp;
    private String adjustmenttype;
    
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
                append(" (").append(adjustmenttype).append(")");
                
        return s.toString();
    }
    
    public void adjust(Shift s){
        LocalDateTime punchTime = originaltimestamp;
        final int ZERO = 0;
        LocalDateTime zeroPT = punchTime.withSecond(ZERO).withNano(ZERO);
        
        LocalDateTime adjustedPT = null;
        int day = punchTime.toLocalDate().getDayOfWeek().getValue();
        
        LocalDateTime shiftStart = s.getStart().atDate(originaltimestamp.toLocalDate());
        LocalDateTime shiftStop = s.getStop().atDate(originaltimestamp.toLocalDate());
        LocalDateTime lunchStart = s.getLunchstart().atDate(originaltimestamp.toLocalDate());
        LocalDateTime lunchStop = s.getLunchstop().atDate(originaltimestamp.toLocalDate());

        int interval = s.getInterval();
        int gracePeriod = s.getGraceperiod();
        int dock = s.getDock();
        if(day >= Calendar.MONDAY && day <= Calendar.FRIDAY){
            shiftStart = s.getStart(day).atDate(originaltimestamp.toLocalDate());
            shiftStop = s.getStop(day).atDate(originaltimestamp.toLocalDate());
            lunchStart = s.getLunchstart(day).atDate(originaltimestamp.toLocalDate());
            lunchStop = s.getLunchstop(day).atDate(originaltimestamp.toLocalDate());

            interval = s.getInterval(day);
            gracePeriod = s.getGraceperiod(day);
            dock = s.getDock(day);
        }
        
        

        int min = punchTime.getMinute();
        int sec = punchTime.getSecond();

        // on time
        if(zeroPT.equals(shiftStart) || zeroPT.equals(shiftStop) || zeroPT.equals(lunchStart) || zeroPT.equals(lunchStop)){
            if (zeroPT.equals(shiftStart)){
                adjustedPT = shiftStart;
                this.adjustmenttype = "Shift Start";                
            }
            else if(zeroPT.equals(shiftStop)){
                adjustedPT = shiftStop;
                this.adjustmenttype = "Shift Stop";
            }
            else if(zeroPT.equals(lunchStart)){
                adjustedPT = lunchStart;
                this.adjustmenttype = "Lunch Start";
            }
            else{
                adjustedPT = lunchStop;
                this.adjustmenttype = "Lunch Stop";
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
            final double HALFINTERVAL = interval / 2.0;
           
            if(mod < Math.ceil(HALFINTERVAL)){
                if(mod == Math.floor(HALFINTERVAL) && sec < interval + interval){
                    adjustedPT = punchTime.minusMinutes(mod).withSecond(0);
                    this.adjustmenttype = "Interval Round";   
                }
                else if (mod == Math.floor(HALFINTERVAL) && sec >= interval + interval) {
                    adjustedPT = punchTime.plusMinutes(interval - mod).withSecond(0);
                    this.adjustmenttype = "Interval Round";
                }
                else{
                    adjustedPT = punchTime.minusMinutes(mod).withSecond(0);
                    this.adjustmenttype = "Interval Round";
                }                
            }
            else if(mod >= Math.ceil(HALFINTERVAL)){
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
