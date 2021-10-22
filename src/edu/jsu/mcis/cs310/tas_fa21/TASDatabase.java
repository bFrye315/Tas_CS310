package edu.jsu.mcis.cs310.tas_fa21;

import java.sql.*;
import java.time.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;

//Feature 1
public class TASDatabase {
    private Connection conn = null;
    private String query;
    private PreparedStatement prstSelect = null, prstUpdate = null;
    private int updateCount;
    
    public TASDatabase(){
        try{
            //This is used for to identify the server
            String server = ("jdbc:mysql://localhost/tas_fa21_v1"); //Might got this wrong, but its trial and error on the server part
            String userName = "tasuser";
            String passWord = "bteam";
            
            //Load MySQL Driver
            //Class.forName("com.mysql.jdbc.Driver").newInstance(); //Similar to line 22 might got it wrong, but will change later
            
            //Opens the connection
            this.conn = DriverManager.getConnection(server, userName, passWord);
            
            if(!conn.isValid(0)){
                throw new SQLException();
            }
        }
        catch(SQLException e){System.out.println("");}
        //catch(ClassNotFoundException e){System.out.println("");}
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
        int shiftid = 0;
        Shift outputShift = null;
        String badgeid = badge.getId();
        try{
            
            query = "SELECT * FROM employee WHERE badgeid = ?";
            prstSelect = conn.prepareStatement(query);
            prstSelect.setString(1, badgeid);
            
            boolean hasResults = prstSelect.execute();
            if(hasResults){
                ResultSet resultsSet = prstSelect.getResultSet();
                resultsSet.next();
                
                shiftid = resultsSet.getInt("shiftid");
            }
        }
        catch(Exception e) { e.printStackTrace(); }
        
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
        catch(Exception e) { e.printStackTrace(); }
        
        return outputShift;
    }
    
    //Feature 2 
    public int insertPunch(Punch p){
            
            int results = 0;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           
          LocalDateTime originalTime = p.getOriginaltimestamp();
          String otsString = originalTime.format(dtf);
          System.err.println("New Punch Timestamp (from insertPunch(): " + otsString);
          String badgeid = p.getBadgeid(); 
          int terminalid = p.getTerminalid(); 
          PunchType punchtypeid = p.getPunchtypeid(); 
          

         try{
             query = "INSERT INTO tas_fa21_v1.punch (terminalid, badgeid, originaltimestamp, punchtypeid) VALUES (?, ?, ?, ?)"; 
             prstUpdate = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); 
             
             prstUpdate.setInt(1, terminalid);
             prstUpdate.setString(2, badgeid);
             prstUpdate.setString(3, otsString);
             prstUpdate.setInt(4, punchtypeid.ordinal());
             
             updateCount = prstUpdate.executeUpdate();
             
             if(updateCount > 0){
                 
                 ResultSet resultset = prstUpdate.getGeneratedKeys(); 
                 
                 if (resultset.next()){
                     results = resultset.getInt(1);
                 }
             }
                
         }
         catch(SQLException e){ System.out.println(e);}
         System.err.println("New Punch ID: " + results);
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
                    alist.add(punch);
                    
                }
            }
                  
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return alist;   
        
    }
    
}
