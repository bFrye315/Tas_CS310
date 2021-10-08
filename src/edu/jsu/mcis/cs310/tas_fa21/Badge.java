
package edu.jsu.mcis.cs310.tas_fa21;

public class Badge {
        //Properties
    
    private String id;
    private String description;
    
    //Constructer
    public Badge(String badgeID, String badgeDescription){
        this.id = badgeID;
        this.description = badgeDescription;
    }
        //ID Getter
    public String getId() {
        return id;
    }
        // ID setter
    public void setID(String newID){
            this.id = newID;
}
        //Description Getter
    public String getDescription() {
        return description;
    }
       //Desciption Setter
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
    @Override
    public String toString(){
        return "#" + id + " " + description;
    }
}
