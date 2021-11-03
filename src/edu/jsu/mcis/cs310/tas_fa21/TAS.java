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
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        
        
        
        ArrayList<Punch> payperiod = db.getPayPeriodPunchList(b, p.getOriginaltimestamp().toLocalDate(), s);
        StringBuilder sb = new StringBuilder();
        for(Punch p1 : payperiod){
            sb.append(p1.printAdjusted());
        }
        System.out.println(sb.toString());
        
        System.out.println(calculateAbsenteeism(payperiod, s));
    }
    
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchList, Shift shift) {
        
        int totalTime = 0;
        
        for (int i = 0; i < dailypunchList.size(); i++) {
            //System.out.println(dailypunchList.get(i));
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
                //System.out.println(dailypunchList.get(i).getAdjustedtimestamp());
                //System.out.println(dailypunchList.get(i + 1).getAdjustedtimestamp());
                int totalMinutes = (int)duration.toMinutes();
                totalTime = totalTime + totalMinutes;
                
                System.out.println(totalTime);
                //System.out.println(" ");
            }
            
            boolean lunchClockOut = false;
            for(Punch p : dailypunchList){
                if(p.getAdjustedtimestamp().toLocalTime().equals(shift.getLunchstart())){
                    lunchClockOut = true;
                    break;
                }
                
            }
            if(!lunchClockOut && totalTime > shift.getLunchdeduct()){
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
        double totalWeeklyMinutes = 0;
        final double FORTYHRWEEK = 2400;
        final double TOP_PERCENTAGE = 100;
        
        for(int i = 0; i < punchlist.size(); i+= 2){
             Duration duration = Duration.between(punchlist.get(i).getAdjustedtimestamp(), punchlist.get(i + 1).getAdjustedtimestamp());
             totalWeeklyMinutes += duration.toMinutes();
        }
        int sat = 0, sun = 0, mon = 0, tue = 0, wed = 0, thur = 0, fri = 0;
        for(Punch p : punchlist){
            switch(p.getAdjustedtimestamp().getDayOfWeek().getValue()){
                case 1:
                    mon +=1;
                    break;
                case 2:
                    tue +=1;
                    break;
                case 3:
                    wed += 1;
                    break;
                case 4:
                    thur += 1;
                    break;
                case 5:
                    fri += 1;
                    break;
                case 6:
                    sat += 1;
                    break;
                case 7:
                    sun += 1;
                    break;
            }
            
        }
        System.out.println(mon + "\n" + tue + "\n" + wed + "\n" + thur + "\n" + fri + "\n" + sat + "\n" + sun);
        //totalWeeklyMinutes = calculateTotalMinutes(punchlist, s);
        
        percentage = (totalWeeklyMinutes/FORTYHRWEEK) * TOP_PERCENTAGE;
        
        percentage = TOP_PERCENTAGE - percentage;
        
        return percentage;
    }
}
