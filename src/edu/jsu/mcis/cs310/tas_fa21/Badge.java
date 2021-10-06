
package edu.jsu.mcis.cs310.tas_fa21;

public class Badge {
    
    private String id;
    private String description;
    
    public Badge(){
        
    }
        
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString(){
        return "#" + id + " " + description;
    }
}
