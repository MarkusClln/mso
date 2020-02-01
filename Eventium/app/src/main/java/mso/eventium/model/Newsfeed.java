package mso.eventium.model;

public class Newsfeed {
    public Newsfeed(String _title, String _description, boolean _isAdd){
        this.title = _title;
        this.description = _description;
        this.isAdd = _isAdd;
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

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    private boolean isAdd;


}
