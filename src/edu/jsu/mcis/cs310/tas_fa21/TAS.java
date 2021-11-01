/**
 * Author: Kyle Glenn Daniel Safsn
 *         Montell Norman
 *         Brandon Frye 
 *         Tyler Nichols
 *         James Mollica
 */
package edu.jsu.mcis.cs310.tas_fa21;
import java.util.ArrayList;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import org.json.simple.*; 


public class TAS {

    public static void main(String[] args) {
        TASDatabase db;
        db = new TASDatabase(); 
       
        Punch p = db.getPunch(3634);
        Badge b = p.getBadge();
        Shift s = db.getShift(b);
        
        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginaltimestamp().toLocalDate());
        
        for (Punch punch : dailypunchlist) {
            punch.adjust(s);
        } 
        calculateTotalMinutes(dailypunchlist,s);
    }
    
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchList, Shift shift) {
        
        int totalTime = 0;
        
        for (int i = 0; i < dailypunchList.size(); i++) {
            System.out.println(dailypunchList.get(i));
        }
                        //needs change
       // if (dailypunchList.size()==2){
            //if (dailypunchList.get(1).getAdjustedtimestamp().isAfter(shift.getLunchstop())){
                //totalTime = totalTime - 30;
            //}
        //}
        try {
            
            for (int i = 0; i < dailypunchList.size(); i+=2) {
                Duration duration = Duration.between(dailypunchList.get(i).getAdjustedtimestamp(), dailypunchList.get(i + 1).getAdjustedtimestamp());
                System.out.println(dailypunchList.get(i).getAdjustedtimestamp());
                System.out.println(dailypunchList.get(i + 1).getAdjustedtimestamp());
                int totalMinutes = (int)duration.toMinutes();
                totalTime = totalTime + totalMinutes;
                
                System.out.println(totalTime);
                System.out.println(" ");
            }
            
            boolean lunchClockOut = false;
            for(Punch p : dailypunchList){
                if(p.getAdjustedtimestamp().toLocalTime().equals(shift.getLunchstart())){
                    lunchClockOut = true;
                    break;
                }
                
            }
            if(!lunchClockOut){
                totalTime = totalTime - shift.getLunchduration();
            }
            
        }
        catch(IndexOutOfBoundsException e1) {
            System.out.println("Odd number of punches");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        System.out.println(totalTime);
        return totalTime;

    }
    
    //Feature 5
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE" + " LL/dd/uuuu HH:mm:ss");
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        //"[{\"originaltimestamp\":\"TUE 09\\/18\\/2018 11:59:33\",
        //\"badgeid\":\"08D01475\",
        //\"adjustedtimestamp\":\"TUE 09\\/18\\/2018 12:00:00\",
        //\"adjustmenttype\":\"Shift Start\",
        //\"terminalid\":\"104\",
        //\"id\":\"4943\",
        //\"punchtype\":\"CLOCK IN\"}
        for(Punch punch: dailypunchlist){
            
            HashMap<String, String> punchData = new HashMap<>();
            
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", String.valueOf(punch.getBadge().getId()));
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchData.put("punchtype", String.valueOf(punch.getPunchtype()));
            punchData.put("adjustmenttype", String.valueOf(punch.getAdjustmenttype()));
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginaltimestamp().format(format).toUpperCase()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.getAdjustedtimestamp().format(format).toUpperCase()));
            jsonData.add(punchData);
            
        }
        
        String json = JSONValue.toJSONString(jsonData);

        
        
        
        
        System.out.println(json);
        return json;
    }
    
    public static double calculateAbsenteeism(ArrayList<Punch> punchlist, Shift s){
        double percentage = 0;
        int totalWeeklyMinutes = 0;
        final int TOTALWEEKINMIN = 2400;
        for(Punch p : punchlist){
            totalWeeklyMinutes += calculateTotalMinutes(dailypunchlist, s);
        }
        percentage = ((double)TOTALWEEKINMIN / (double)totalWeeklyMinutes) * 100;
        
        return percentage;
    }
}
