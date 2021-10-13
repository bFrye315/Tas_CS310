package edu.jsu.mcis.cs310.tas_fa21;

import java.sql.*;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
            String server = ("jdbc:mysql://localhost/tas"); //Might got this wrong, but its trial and error on the server part
            String userName = "tasuser";
            String passWord = "bteam";
            
            //Load MySQL Driver
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //Similar to line 22 might got it wrong, but will change later
            
            //Opens the connection
            conn = DriverManager.getConnection(server, userName, passWord);
            
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
        Punch outputPunch;
        try{
            //Prepares the query
            query = "SELECT * FROM tas.punch WHERE id = " + punchid;

            prstSelect = conn.prepareStatement(query);
            
            //Executing the query
            hasResults = prstSelect.execute();
            
            while(hasResults || prstSelect.getUpdateCount() != -1){
                if(hasResults){
                    resultsSet = prstSelect.getResultSet();
                    resultsSet.next();
                    
                    int terminalid = resultsSet.getInt("terminalId");
                    String badge = resultsSet.getString("badgeid");
                    Timestamp originaltimestamp = resultsSet.getTimestamp("originaltimestamp");
                    int punchtypeid = resultsSet.getInt("punchTypeId");
                    

                    outputPunch = new Punch(resultsSet.getInt("terminalId"), getBadge("badgeid"), 
                            resultsSet.getInt("punchTypeId"));

                    outputPunch.setOriginaltimestamp(originaltimestamp);
                    
                    return outputPunch;
                }
            }
        }
        catch(SQLException e){System.out.println(e);}
        return null;
    }
    
    public Badge getBadge(String id){
        Badge outputBadge;
        try{
            query = "SELECT * FROM tas.badge WHERE id = \"" + id +
                    "\"";
            prstSelect = conn.prepareStatement(query);
            
            hasResults = prstSelect.execute();
            
            while(hasResults || prstSelect.getUpdateCount() != -1){
                if(hasResults){
                    resultsSet = prstSelect.getResultSet();
                    resultsSet.next();
                    
                    outputBadge = new Badge(resultsSet.getString("badgeID"), resultsSet.getString("badgeDescription"));
                    return outputBadge;
                }
            }
        }
        catch(SQLException e){System.out.println("Error in getBadge");}
        return null;
    }
    
    public Shift getShift(int shiftid){
        Shift outputShift;
        try{
            query = "SELECT * FROM tas.shift WHERE id = " + shiftid;
            prstSelect = conn.prepareStatement(query);
            
            hasResults = prstSelect.execute();
            
            while(hasResults || prstSelect.getUpdateCount() != -1){
                if(hasResults){
                    resultsSet = prstSelect.getResultSet();
                    resultsSet.next();
                    
                    String description = resultsSet.getString("description");
                    LocalTime start = LocalTime.parse(resultsSet.getString("start"));
                    LocalTime stop = LocalTime.parse(resultsSet.getString("stop"));
                    String interval = resultsSet.getString("interval");
                    String graceperiod = resultsSet.getString("graceperiod");
                    String dock = resultsSet.getString("dock");
                    LocalTime lunchstart = LocalTime.parse(resultsSet.getString("lunchstart"));
                    LocalTime lunchstop = LocalTime.parse(resultsSet.getString("lunchstop"));
                    String lunchdeduct = resultsSet.getString("lunchdeduct");
                    
                    outputShift = new Shift(shiftid, description, start, stop, interval, 
                            graceperiod, dock, lunchstart, lunchstop, lunchdeduct);
                    return outputShift;
                }
            }
        }
        catch(SQLException e){System.out.println(e);}
        return null;
    }
    
    public Shift getShift(Badge badge){ //For James: Okay so basically this is exactly similar to the previous line of code so the blueprint is there for you
        try{
            query = "SELECT * (Note: Blank for now ~ Montell N.) WHERE id = " + badge.getId() + " ";
            prstSelect = conn.prepareStatement(query);
            
            hasResults = prstSelect.execute();
            while(hasResults || prstSelect.getUpdateCount() != -1){
                if(hasResults){
                    resultsSet = prstSelect.getResultSet();
                    resultsSet.next();
                }
            }
        }
        catch(SQLException e){System.out.println(e);}
        return null;
    }
    
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
        
        try{
             query = "INSERT INTO tas.punch(terminalid, badgeid, originaltimestamp,"
                     + "punchtypeid) VALUES('" + terminalid +
                     "', '" + badgeid + "', '" + formats +
                     "', '" + punchtypeid + "')";
             
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
        ArrayList lists = new ArrayList();
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTimeInMillis(date); //Might be wrong
        java.util.Date datesTocheck = gCal.getTime();
        
        try{
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
    }
}