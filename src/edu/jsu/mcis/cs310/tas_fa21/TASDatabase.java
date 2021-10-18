package edu.jsu.mcis.cs310.tas_fa21;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

//Feature 1
public class TASDatabase {
    private Connection conn = null;
    private String query;
    private PreparedStatement prstSelect = null, prstUpdate = null;
    private ResultSet resultsSet = null;
    private boolean hasResults;
    private int currentCount;
    
    public TASDatabase(){
        try{
            //This is used for to identify the server
            String server = ("jdbc:mysql://localhost/tas_fa21_v1"); //Might got this wrong, but its trial and error on the server part
            String userName = "tasuser";
            String passWord = "bteam";
            
            //Load MySQL Driver
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //Similar to line 22 might got it wrong, but will change later
            
            //Opens the connection
            this.conn = DriverManager.getConnection(server, userName, passWord);
            
            if(!conn.isValid(0)){
                throw new SQLException();
            }
        }
        catch(SQLException e){System.out.println("");}
        catch(ClassNotFoundException e){System.out.println("");}
        catch(Exception e){}
    }
    
    public void close(){
        try{
            conn.close();
        }
        catch(SQLException e){}
        finally{
            if(resultsSet != null){try{resultsSet.close(); resultsSet = null;} catch(SQLException e){}}
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
            hasResults = prstSelect.execute();
            
            
                if(hasResults){
                    resultsSet = prstSelect.getResultSet();
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
            
            hasResults = prstSelect.execute();
            
            if(hasResults){
                
                resultsSet = prstSelect.getResultSet();
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
            
            query = "SELECT * FROM shift WHERE id = " + shiftid;
            prstSelect = conn.prepareStatement(query);
            
            hasResults = prstSelect.execute();
            
            while(hasResults || prstSelect.getUpdateCount() != -1){
                if(hasResults){
                    resultsSet = prstSelect.getResultSet();
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
        }
        catch(SQLException e){System.out.println(e);}
        
        return outputShift;
        
    }
    
    public Shift getShift(Badge badge){ //For James: Okay so basically this is exactly similar to the previous line of code so the blueprint is there for you
        Shift outputShift = null;
        try{
            
            query = "SELECT * FROM shift WHERE shiftid = (SELECT shiftid FROM employee WHERE badgeid = " + badge.getId() + ")";
            prstSelect = conn.prepareStatement(query);
            
            hasResults = prstSelect.execute();
           
            }
        
        catch(Exception e) { e.printStackTrace(); }
        return outputShift;
    }
  /**  
    //Feature 2 
    public int insertPunch(Punch p){
        int terminalid = p.getTerminalid();
        String badgeid = p.getBadgeid();
        LocalDateTime originaltimestamps = p.getOriginaltimestamp();
        PunchType punchtypeid = p.getPunchtypeid();
        
        //This part will convert from TimeStamp to String
        Timestamp originaltimestamp = new Timestamp(originaltimestamps);
        String date = "YYYY-MM-DD";
        SimpleDateFormat simpDate = new SimpleDateFormat(date);
        String formats = simpDate.format(originaltimestamp);
        
        try{ //Rework this
             query = "";
             
             System.out.println(query);
             prstSelect = conn.prepareStatement(query);
             System.out.println("Executing query...");
             hasResults = prstSelect.execute();
             System.out.println("Punch has been inserted.");
        }
        catch(SQLException e){System.out.println(e);}
        return -1;
    }
    
    public ArrayList<Punch> getDailyPunchList(Badge badge, LocalDateTime date){
        
        try {
            query = "SELECT badgeid, terminalid, punchtypeid, originaltimestamp,"
                    + "punchtypeid FROM tas.punch WHERE badgeid = '" +
                    badge.getId() + "' AND originaltimestamp LIKE '%"
                    + date + "%'";
            
            hasResults = prstSelect.execute();
            while(hasResults || prstSelect.getUpdateCount() != -1){
                if(hasResults){
                    resultsSet = prstSelect.getResultSet();
                    
                    while(resultsSet.next()){
                        int terminalid = resultsSet.getInt("terminalid");
                        int punchtypeid = resultsSet.getInt("punchtypeid");
                        
                        
                    }
                }
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }*/
}
