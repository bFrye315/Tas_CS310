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
import java.time.temporal.ChronoUnit;

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
    
}

//Feature 4
//public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift)
