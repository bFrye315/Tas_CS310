package edu.jsu.mcis.cs310.tas_fa21;

import java.sql.*;
import java.time.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

//Feature 1
public class TASDatabase {
    private Connection conn = null;
    private String query;
    private PreparedStatement prstSelect = null, prstUpdate = null;
    private int updateCount;
    
    public TASDatabase(){
        try{
            //This is used for to identify the server
            String server = ("jdbc:mysql://localhost/tas_fa21_v1");
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
        
        try{
            
            query = "SELECT * FROM shift WHERE id = ?";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setInt(1, shiftid);
            
            boolean hasResults = prstSelect.execute();
            
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();
                    
                ShiftParameters params = new ShiftParameters();
                    
                params.setDescription(resultsSet.getString("description"));
                params.setStart(LocalTime.parse(resultsSet.getString("start")));
                params.setStop(LocalTime.parse(resultsSet.getString("stop")));
                params.setInterval(resultsSet.getInt("interval"));
                params.setGraceperiod(resultsSet.getInt("graceperiod"));
                params.setDock(resultsSet.getInt("dock"));
                params.setLunchstart(LocalTime.parse(resultsSet.getString("lunchstart")));
                params.setLunchstop(LocalTime.parse(resultsSet.getString("lunchstop")));
                params.setLunchdeduct(resultsSet.getInt("lunchdeduct"));
                params.setId(shiftid);
                    
                outputShift = new Shift(params);
            }
        }
        catch(Exception e){e.printStackTrace();}
        
        return outputShift;
        
    }
    
    public Shift getShift(Badge badge){ 
        Shift outputShift = null;
        try{ // reverted this back to the original query for the most part, 
            //but changed a few things so it would still work as needed
            
            query = "SELECT * FROM shift WHERE id = (SELECT shiftid FROM employee WHERE badgeid = ?)";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setString(1, badge.getId());
            
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();
                
                      
                ShiftParameters params = new ShiftParameters();
                    
                params.setDescription(resultsSet.getString("description"));
                params.setStart(LocalTime.parse(resultsSet.getString("start")));
                params.setStop(LocalTime.parse(resultsSet.getString("stop")));
                params.setInterval(resultsSet.getInt("interval"));
                params.setGraceperiod(resultsSet.getInt("graceperiod"));
                params.setDock(resultsSet.getInt("dock"));
                params.setLunchstart(LocalTime.parse(resultsSet.getString("lunchstart")));
                params.setLunchstop(LocalTime.parse(resultsSet.getString("lunchstop")));
                params.setLunchdeduct(resultsSet.getInt("lunchdeduct"));
                params.setId(resultsSet.getInt("id"));
                    
                outputShift = new Shift(params);
            }
        }
        catch(Exception e) { e.printStackTrace(); }
        
        return outputShift;
    }
    
    //Feature 2 
    public int insertPunch(Punch p){
            
            int results = 0;
            
            
            // using a localdatetime and timestamp conversion since timestamps are what
            // is stored in the database
            LocalDateTime time = p.getOriginaltimestamp();
            
            
            
            String badgeid = p.getBadge().getId(); 
            int terminalid = p.getTerminalid(); 
            PunchType punchtypeid = p.getPunchtype(); 
          

         try{
             query = "INSERT INTO tas_fa21_v1.punch (terminalid, badgeid, originaltimestamp, punchtypeid) VALUES (?, ?, ?, ?)"; 
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
            prstSelect.setDate(2, java.sql.Date.valueOf(payperiod));
            
            //Executing the query
            boolean hasResults = prstSelect.execute();
            
            
                if(hasResults){
                    ResultSet resultsSet = prstSelect.getResultSet();
                    resultsSet.next();
                    
                    double percentage = resultsSet.getDouble("percentage");

                    outputAbsenteeism = new Absenteeism(badge, payperiod, percentage);
                    
     
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
        
        LocalDate beginOfWeek = payperiod;
        switch(payperiod.getDayOfWeek().getValue()){
            case 1:
                beginOfWeek = payperiod.minusDays(1);
                break;
            case 2:
                beginOfWeek = payperiod.minusDays(2);
                break;
            case 3:
                beginOfWeek = payperiod.minusDays(3);
                break;
            case 4:
                beginOfWeek = payperiod.minusDays(4);
                break;
            case 5:
                beginOfWeek = payperiod.minusDays(5);
                break;    
            case 6:
                beginOfWeek = payperiod.minusDays(6);
                break;
            case 7:
                beginOfWeek = payperiod;
                break;
        }
        

        LocalDate punchDate = beginOfWeek;
        for (int i = 0; i < DayOfWeek.SUNDAY.getValue(); i++){
            list.addAll(getAdjustedDailyPunchList(badge, punchDate, s));
            
            punchDate = punchDate.plusDays(1);
        }
        
        
        
        return list;
     }
    
    public void insertAbsenteeism(Absenteeism absenteeism){
        
    }
}
