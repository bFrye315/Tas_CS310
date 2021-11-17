package edu.jsu.mcis.cs310.tas_fa21;

import java.sql.*;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;

//Feature 1
public class TASDatabase {
    private Connection conn = null;
    private String query;
    private PreparedStatement prstSelect = null, prstUpdate = null;
    private int updateCount;
    
    public TASDatabase(){
        try{
            //This is used for to identify the server
            String server = ("jdbc:mysql://localhost/tas_fa21_v2");
            String userName = "tasuser";
            String passWord = "bteam";
            
            //Opens the connection
            this.conn = DriverManager.getConnection(server, userName, passWord);
            
            if(!conn.isValid(0)){
                throw new SQLException();
            }
        }
        catch(SQLException e){System.out.println("");}
        catch(Exception e){}
    }
    
    public void close(){
        try{
            conn.close();
        }
        catch(SQLException e){}
        finally{
            if(prstSelect != null){try{prstSelect.close(); prstSelect = null;} catch(SQLException e){}}
        }
    }
    
    public Punch getPunch(int punchid){
        Punch outputPunch = null;
        try{
            //Prepares the query
            query = "SELECT * FROM punch WHERE id = ?";

            prstSelect = conn.prepareStatement(query);
            prstSelect.setInt(1, punchid);
            
            //Executing the query
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();

                int terminalid = resultsSet.getInt("terminalId");
                String badgeid = resultsSet.getString("badgeid");
                Timestamp timestamp = resultsSet.getTimestamp("originaltimestamp");
                LocalDateTime localstamp = timestamp.toLocalDateTime();
                int punchtypeid = resultsSet.getInt("punchTypeId");

                outputPunch = new Punch(terminalid, getBadge(badgeid), punchtypeid, localstamp);
                outputPunch.setId(resultsSet.getInt("id"));
            }            
        }
        catch(Exception e){e.printStackTrace();}
        return outputPunch;
    }
    
    public Badge getBadge(String id){
        Badge outputBadge = null;
        try{
            query = "SELECT * FROM badge WHERE id = ?";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setString(1, id);
            
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();

                outputBadge = new Badge(resultsSet.getString("id"), resultsSet.getString("description"));        
            }
        }
        catch(Exception e) { e.printStackTrace(); }
        return outputBadge;    
    }

    public Shift getShift(int shiftid) {    
        Shift outputShift = null;
        ShiftParameters ds = new ShiftParameters();
        try{
            query = "SELECT * FROM shift, dailyschedule WHERE shift.id = ? AND dailyschedule.id = (SELECT dailyscheduleid FROM shift WHERE id = ?) ";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setInt(1, shiftid);
            prstSelect.setInt(2, shiftid);
            
            boolean hasResults = prstSelect.execute(); 
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();

                ds.setDescription(resultsSet.getString("description"));
                ds.setShiftid(shiftid);
                ds.setStart(LocalTime.parse(resultsSet.getString("start")));
                ds.setStop(LocalTime.parse(resultsSet.getString("stop")));
                ds.setInterval(resultsSet.getInt("interval"));
                ds.setGraceperiod(resultsSet.getInt("graceperiod"));
                ds.setDock(resultsSet.getInt("dock"));
                ds.setLunchstart(LocalTime.parse(resultsSet.getString("lunchstart")));
                ds.setLunchstop(LocalTime.parse(resultsSet.getString("lunchstop")));
                ds.setLunchdeduct(resultsSet.getInt("lunchdeduct"));
                ds.setShiftdailyscheduleid(resultsSet.getInt("dailyscheduleid"));
                
                outputShift = new Shift(ds);              
            }
        }
        catch(Exception e){e.printStackTrace();}
        return outputShift;
    }
    public Shift getShift(Badge badge){ 
        Shift outputShift = null;
        ShiftParameters ds = new ShiftParameters();
        try{
            query = "SELECT * FROM shift, dailyschedule WHERE shift.id = (SELECT shiftid FROM employee WHERE badgeid = ?) AND dailyschedule.id = (SELECT dailyscheduleid FROM shift WHERE id = (SELECT shiftid FROM employee WHERE badgeid = ?))";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setString(1, badge.getId());
            prstSelect.setString(2, badge.getId());
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();

                ds.setDescription(resultsSet.getString("description"));
                ds.setShiftid(resultsSet.getInt("shift.id"));
                ds.setStart(LocalTime.parse(resultsSet.getString("start")));
                ds.setStop(LocalTime.parse(resultsSet.getString("stop")));
                ds.setInterval(resultsSet.getInt("interval"));
                ds.setGraceperiod(resultsSet.getInt("graceperiod"));
                ds.setDock(resultsSet.getInt("dock"));
                ds.setLunchstart(LocalTime.parse(resultsSet.getString("lunchstart")));
                ds.setLunchstop(LocalTime.parse(resultsSet.getString("lunchstop")));
                ds.setLunchdeduct(resultsSet.getInt("lunchdeduct"));
                ds.setShiftdailyscheduleid(resultsSet.getInt("dailyscheduleid"));
                
                outputShift = new Shift(ds);
            }
        }
        catch(Exception e) { e.printStackTrace(); }        
        return outputShift;
    }
    //Feature 2 
    public int insertPunch(Punch p){
        int results = 0;
        LocalDateTime time = p.getOriginaltimestamp();

        String badgeid = p.getBadge().getId(); 
        int terminalid = p.getTerminalid(); 
        PunchType punchtypeid = p.getPunchtype(); 
         try{
            query = "INSERT INTO tas_fa21_v2.punch (terminalid, badgeid, originaltimestamp, punchtypeid) VALUES (?, ?, ?, ?)"; 
            prstUpdate = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); 

            prstUpdate.setInt(1, terminalid);
            prstUpdate.setString(2, badgeid);
            prstUpdate.setTimestamp(3, java.sql.Timestamp.valueOf(time)); // this is now a set to timestamp instead of a string
            prstUpdate.setInt(4, punchtypeid.ordinal());

            updateCount = prstUpdate.executeUpdate();

            if(updateCount > 0){
                ResultSet resultset = prstUpdate.getGeneratedKeys(); 
                if (resultset.next()){
                    results = resultset.getInt(1);
                }
            }                
         }
         catch(Exception e){ e.printStackTrace();} // changed these to output differently and just print the full stack
         return results;    
    }
    
    public ArrayList<Punch> getDailyPunchList(Badge badge, LocalDate date){
        ArrayList<Punch> alist = null;        
        try {
            query = "SELECT * FROM punch WHERE badgeid=? AND DATE(originaltimestamp)=?";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setString(1, badge.getId());
            prstSelect.setDate(2, java.sql.Date.valueOf(date));
            
            boolean hasResults = prstSelect.execute();
            if(hasResults){    
                alist = new ArrayList<>(); 
                ResultSet resultsSet = prstSelect.getResultSet();
                while(resultsSet.next()) {
                    int terminalid = resultsSet.getInt("terminalid");
                    String badgeid = resultsSet.getString("badgeid");
                    LocalDateTime originaltimestamp = resultsSet.getTimestamp("originaltimestamp").toLocalDateTime();
                    int punchtypeid = resultsSet.getInt("punchTypeId");

                    Punch punch = new Punch(terminalid, getBadge(badgeid), punchtypeid, originaltimestamp);
                    punch.setId(resultsSet.getInt("id"));
                    
                    alist.add(punch);                    
                }
            }                  
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return alist;           
    }
    
    public Absenteeism getAbsenteeism(Badge badge, LocalDate payperiod){
        Absenteeism outputAbsenteeism = null;
        try{
            //Prepares the query
            query = "SELECT * FROM absenteeism WHERE badgeid = ? AND payperiod = ?";

            prstSelect = conn.prepareStatement(query);
            prstSelect.setString(1, badge.getId());
            prstSelect.setDate(2, java.sql.Date.valueOf(payperiod.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))));
            
            //Executing the query
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();
                
                double percentage = resultsSet.getDouble("percentage");

                outputAbsenteeism = new Absenteeism(badge.getId(), payperiod.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)), percentage);
            }           
        }
        catch(Exception e){e.printStackTrace();}
        return outputAbsenteeism;
    }
    
    public ArrayList<Punch> getAdjustedDailyPunchList(Badge badge, LocalDate date, Shift s){        
        ArrayList<Punch> alist = null;       
        try {
            query = "SELECT * FROM punch WHERE badgeid=? AND DATE(originaltimestamp)=?";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setString(1, badge.getId());
            prstSelect.setDate(2, java.sql.Date.valueOf(date));
            
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                
                alist = new ArrayList<>();
                
                ResultSet resultsSet = prstSelect.getResultSet();

                while(resultsSet.next()) {
                    int terminalid = resultsSet.getInt("terminalid");
                    String badgeid = resultsSet.getString("badgeid");
                    LocalDateTime originaltimestamp = resultsSet.getTimestamp("originaltimestamp").toLocalDateTime();
                    int punchtypeid = resultsSet.getInt("punchTypeId");

                    Punch punch = new Punch(terminalid, getBadge(badgeid), punchtypeid, originaltimestamp);
                    punch.setId(resultsSet.getInt("id"));
                    punch.adjust(s);
                    
                    alist.add(punch);                    
                }
            }                 
        }
        catch (Exception e) { e.printStackTrace(); }        
        return alist;           
    }
    public ArrayList<Punch> getPayPeriodPunchList(Badge badge, LocalDate payperiod, Shift s){
        ArrayList<Punch> list = new ArrayList<>();        
        LocalDate beginOfWeek = payperiod.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));        
        LocalDate punchDate = beginOfWeek;
        for (int i = 0; i < DayOfWeek.SUNDAY.getValue(); i++){
            list.addAll(getAdjustedDailyPunchList(badge, punchDate, s));
            
            punchDate = punchDate.plusDays(1);
        }
        return list;
     }
    
    public void insertAbsenteeism(Absenteeism absenteeism){        
        // Get badge id, payperiod, and percentage from absenteeism object
        String badgeid = absenteeism.getBadgeid();
        LocalDateTime payperiod = absenteeism.getPayperiod();
        Double percentage = absenteeism.getPercentage();
        
        try {
            if(getAbsenteeism(absenteeism.getBadge(), payperiod.toLocalDate()) == null){
                query = "INSERT INTO tas_fa21_v2.absenteeism (badgeid, payperiod, percentage) VALUES (?, ?, ?)";
            }
            else{
                query = "UPDATE tas_fa21_v2.absenteeism SET badgeid = ? WHERE payperiod = ? AND percentage = ?";
            }
            prstUpdate = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);            
            prstUpdate.setString(1, badgeid);
            prstUpdate.setTimestamp(2, java.sql.Timestamp.valueOf(payperiod));
            prstUpdate.setDouble(3, percentage);           
            // Execute the query
            updateCount = prstUpdate.executeUpdate();
            
        }
        catch(Exception e){e.printStackTrace();}
    }
    

    public Shift getShift(Badge badge, LocalDate date){ 
        Shift outputShift = null;
        ShiftParameters ds = new ShiftParameters();
        DailySchedule daily;
        HashMap<Integer, DailySchedule> schedule;
        try{
            query = "SELECT * FROM shift, dailyschedule WHERE shift.id = (SELECT shiftid FROM employee WHERE badgeid = ?) AND dailyschedule.id = (SELECT dailyscheduleid FROM shift WHERE id = (SELECT shiftid FROM employee WHERE badgeid = ?))";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setString(1, badge.getId());
            prstSelect.setString(2, badge.getId());
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();
                // shift
                ds.setDescription(resultsSet.getString("description"));
                ds.setShiftid(resultsSet.getInt("shift.id"));
                ds.setShiftdailyscheduleid(resultsSet.getInt("dailyscheduleid"));
                // dailyschedule
                ds.setStart(LocalTime.parse(resultsSet.getString("start")));
                ds.setStop(LocalTime.parse(resultsSet.getString("stop")));
                ds.setInterval(resultsSet.getInt("interval"));
                ds.setGraceperiod(resultsSet.getInt("graceperiod"));
                ds.setDock(resultsSet.getInt("dock"));
                ds.setLunchstart(LocalTime.parse(resultsSet.getString("lunchstart")));
                ds.setLunchstop(LocalTime.parse(resultsSet.getString("lunchstop")));
                ds.setLunchdeduct(resultsSet.getInt("lunchdeduct"));
                
                outputShift = new Shift(ds);
            }
        }
        catch(Exception e) { e.printStackTrace(); }
        
        // override
        schedule = outputShift.getSchedule();
        try{
            query = "SELECT * FROM scheduleoverride";
            prstSelect = conn.prepareStatement(query);
            
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                while(resultsSet.next()){
                    ds.setOverrideschedule(resultsSet.getInt("dailyscheduleid"));
                    ds.setDay(resultsSet.getInt("day"));
                    
                    if(resultsSet.getString("badgeid") == null){
                        ds.setBadgeid(null);
                    }
                    else{
                        ds.setBadgeid(resultsSet.getString("badgeid"));
                    }
                    
                    ds.setOverridestart(resultsSet.getDate("start").toLocalDate());
                    
                    if(resultsSet.getTimestamp("end") == null){
                        ds.setOverrideend(null);
                    }
                    else{
                        ds.setOverrideend(resultsSet.getDate("end").toLocalDate());
                    }
                    
                    ds.setOverrideid(resultsSet.getInt("id"));
                    boolean check = false;
                    if(!(ds.getBadgeid() == null) && !(ds.getOverrideend() == null)){
                        if((date.isEqual(ds.getOverridestart()) || date.isAfter(ds.getOverridestart()) && date.isBefore(ds.getOverrideend()) || ds.getOverrideend().isEqual(date)) && badge.getId().equals(ds.getBadgeid())){
                            daily = getDailyschedule(ds, ds.getOverrideschedule());
                            schedule.replace(ds.getDay(), daily);
                            
                            check = true;                                                     
                        }
                    }
                    else if (ds.getBadgeid() == null && !(ds.getOverrideend() == null)){
                        if(date.isEqual(ds.getOverridestart()) || date.isAfter(ds.getOverridestart()) && date.isBefore(ds.getOverrideend()) || ds.getOverrideend().isEqual(date)){
                            daily = getDailyschedule(ds, ds.getOverrideschedule());
                            schedule.replace(ds.getDay(), daily);
                            
                            check = true;                           
                        }
                    }
                    else if (!(ds.getBadgeid() == null) && ds.getOverrideend() == null){
                        if((date.isEqual(ds.getOverridestart()) || date.isAfter(ds.getOverridestart()))  && badge.getId().equals(ds.getBadgeid())){                         
                            daily = getDailyschedule(ds, ds.getOverrideschedule());
                            schedule.replace(ds.getDay(), daily);

                            check = true;                                                       
                        }
                    }
                    else if(ds.getBadgeid() == null && ds.getOverrideend() == null){
                        if(date.isEqual(ds.getOverridestart()) || date.isAfter(ds.getOverridestart())){
                            daily = getDailyschedule(ds, ds.getOverrideschedule());
                            schedule.replace(ds.getDay(), daily);

                            check = true;                           
                        }
                    }
                    if(check){
                        break;
                    }
                }                
            }
        }
        catch(Exception e) { e.printStackTrace(); }

        outputShift.setSchedule(schedule);
        return outputShift;
    }
    private DailySchedule getDailyschedule(ShiftParameters params, int override){
        ShiftParameters ds = new ShiftParameters();
        DailySchedule daily = null;
        try{
            query = "SELECT * FROM dailyschedule WHERE id = ?";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setInt(1, override);
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();

                ds.setStart(LocalTime.parse(resultsSet.getString("start")));
                ds.setStop(LocalTime.parse(resultsSet.getString("stop")));
                ds.setInterval(resultsSet.getInt("interval"));
                ds.setGraceperiod(resultsSet.getInt("graceperiod"));
                ds.setDock(resultsSet.getInt("dock"));
                ds.setLunchstart(LocalTime.parse(resultsSet.getString("lunchstart")));
                ds.setLunchstop(LocalTime.parse(resultsSet.getString("lunchstop")));
                ds.setLunchdeduct(resultsSet.getInt("lunchdeduct"));
                daily = new DailySchedule(ds);
                

            }
        }
        catch(Exception e) { e.printStackTrace(); }
        return daily;
    }
}