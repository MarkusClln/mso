package mso.eventium.model;

public class Bonus {

    private String name;

    public String getUsedOnDate() {
        return usedOnDate;
    }

    public void setUsedOnDate(String usedOnDate) {
        this.usedOnDate = usedOnDate;
    }

    private String usedOnDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    private String cost;

    public Bonus(String _name, String _description, String _cost, String _usedOnDate){
        this.name = _name;
        this.description = _description;
        this.cost = _cost;
        this.usedOnDate = _usedOnDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        this.name = firstName;
    }
}
