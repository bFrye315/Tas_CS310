package edu.jsu.mcis.cs310.tas_fa21;

import java.sql.*;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.text.SimpleDateFormat;


public class TASDatabase {
    private Connection conn = null;
    private String query;
    private PreparedStatement prstSelect = null, prstUpdate = null;
    private ResultSet resultSet = null;
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
            if(resultSet != null){try{resultSet.close(); resultSet = null;} catch(SQLException e){}}
            if(prstSelect != null){try{prstSelect.close(); prstSelect = null;} catch(SQLException e){}}
        }
    }
}
