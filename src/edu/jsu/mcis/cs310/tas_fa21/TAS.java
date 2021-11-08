/**
 * Author: Kyle Glenn Daniel Safsn
 *         Montell Norman
 *         Brandon Frye 
 *         Tyler Nichols
 *         James Mollica
 */
package edu.jsu.mcis.cs310.tas_fa21;
import java.text.DecimalFormat;
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
       
        //4943
        //1087
        //
        Punch p = db.getPunch(3634);
        Badge b = p.getBadge();
        Shift s = db.getShift(b);
        LocalDateTime ts = p.getOriginaltimestamp();
        
        
        
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
        getPunchListAsJSON(payperiod);
        System.out.println(sb.toString());
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println(df.format(calculateAbsenteeism(payperiod, s)));
        double percentage = calculateAbsenteeism(payperiod, s);
        Absenteeism a = db.getAbsenteeism(b, ts.toLocalDate());
        System.out.println("badgeid: " + a.getBadgeid());
        System.out.println("payperiod: " +a.getPayperiod());
        System.out.println("percentage: " + a.getPercentage());
        System.out.println();
    }
    
    
    public static int calculateTotalMinutes(ArrayList<Punch> punchList, Shift shift) {
        
        int totalTime = 0;
        ArrayList<Punch> monPun = new ArrayList<>();
        ArrayList<Punch> tuePun = new ArrayList<>();
        ArrayList<Punch> wedPun = new ArrayList<>();
        ArrayList<Punch> thurPun = new ArrayList<>();
        ArrayList<Punch> friPun = new ArrayList<>();
        ArrayList<Punch> satPun = new ArrayList<>();
        ArrayList<Punch> sunPun = new ArrayList<>();
        
        ArrayList<ArrayList<Punch>> listOfPunchByDay = new ArrayList<>();

        for(Punch p : punchList){
            switch(p.getAdjustedtimestamp().getDayOfWeek().getValue()){
                case 1:
                    monPun.add(p);
                    
                    break;
                case 2:
                    tuePun.add(p);
                    
                    break;
                case 3:
                    wedPun.add(p);
                    
                    break;
                case 4:
                    thurPun.add(p);
                    
                    break;
                case 5:
                    friPun.add(p);
                    
                    break;
                case 6:
                    satPun.add(p);
                    
                    break;
                case 7:
                    sunPun.add(p);
                   
                    break;
            }
            
        }
        if(satPun.size() > 0){
           listOfPunchByDay.add(satPun); 
        }
        if(sunPun.size() > 0){
           listOfPunchByDay.add(sunPun);
        }
        if(friPun.size() > 0){
            listOfPunchByDay.add(friPun);
        }
        if(thurPun.size() > 0){
            listOfPunchByDay.add(thurPun);
        }
        if(wedPun.size() > 0){
            listOfPunchByDay.add(wedPun);
        }
        if(tuePun.size() > 0){
            listOfPunchByDay.add(tuePun);
        }
        if(monPun.size() > 0){
            listOfPunchByDay.add(monPun);
        }

        for(int i = 0; i < listOfPunchByDay.size(); i++){
            System.out.println("size: " + listOfPunchByDay.size());
            System.out.println("size of inner array: " + listOfPunchByDay.get(i).size());
            ArrayList<Punch> day = listOfPunchByDay.get(i);
            try {

                for (int j = 0; j < day.size(); j+=2) {
                    Duration duration = Duration.between(day.get(j).getAdjustedtimestamp(), day.get(j + 1).getAdjustedtimestamp());
                    int totalMinutes = (int)duration.toMinutes();
                    totalTime = totalTime + totalMinutes;

                }
                boolean lunchClockOut = false;
                for(Punch punch : day){
                    if(punch.getAdjustedtimestamp().toLocalTime().equals(shift.getLunchstart())){
                        lunchClockOut = true;
                        break;
                    }    
                }
                if(!lunchClockOut && totalTime >= shift.getLunchdeduct()){
                    totalTime = totalTime - shift.getLunchduration();
                }
                System.out.println(totalTime);

            }
            catch(IndexOutOfBoundsException e1) {
                System.out.println("Odd number of punches");
            }
            catch(Exception e) {
                e.printStackTrace();
            }
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
        
      
        totalWeeklyMinutes = calculateTotalMinutes(punchlist, s);
        
        percentage = (totalWeeklyMinutes/FORTYHRWEEK) * TOP_PERCENTAGE;
        
        percentage = TOP_PERCENTAGE - percentage;
        percentage = Math.round(percentage * TOP_PERCENTAGE)/TOP_PERCENTAGE;
        
        return percentage;
    }
    
    
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift s){
        DecimalFormat df = new DecimalFormat("0.00");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE" + " LL/dd/uuuu HH:mm:ss");
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        HashMap<String, String> timePercentage = new HashMap<>();
        
        for(Punch punch: punchlist){
            
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

        timePercentage.put("totalminutes", String.valueOf(calculateTotalMinutes(punchlist, s)));
        timePercentage.put("absenteeism", String.valueOf(df.format(calculateAbsenteeism(punchlist, s)) + "%"));
        jsonData.add(timePercentage);
        String json = JSONValue.toJSONString(jsonData);
        return json;
    }
}
