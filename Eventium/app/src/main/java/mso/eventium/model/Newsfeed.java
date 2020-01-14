package mso.eventium.model;

public class Newsfeed {
    public Newsfeed(String _title, String _description){
        this.title = _title;
        this.description = _description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;


}
