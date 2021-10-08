package edu.jsu.mcis.cs310.tas_fa21;

import java.sql.*;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;


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
    
    public Punch getPunch(String badgeid){ //Punch class haven't been made yet so until then there will be a error
        Punch outputPunch;
        try{
            //Prepares the query
            query = "SELECT * FROM tas.punch WHERE id = " + badgeid;
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
                    
                    outputPunch = new Punch(resultsSet.getInt("terminalId"), resultsSet.getString("badgeid"), 
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
    
    public Shift getShift(String id){ //Don't have the Shift class so all this is subject to change once Shift is implemented
        Shift outputShift;
        try{
            query = "SELECT * FROM tas.shift WHERE id = " + id;
            prstSelect = conn.prepareStatement(query);
            
            hasResults = prstSelect.execute();
            
            while(hasResults || prstSelect.getUpdateCount() != -1){
                if(hasResults){
                    resultsSet = prstSelect.getResultSet();
                    resultsSet.next();
                    
                    //Can't really add anything here without the shift class so it'll remain blank for the time being
                    
                    
                    outputShift = new Sift();
                    return outputShift;
                }
            }
        }
        catch(SQLException e){System.out.println(e);}
        return null;
    }
}
