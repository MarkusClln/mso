package mso.eventium.model;

public class Event {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private String name;
    private String host;

    public Event(String _name, String _host){
        this.name = _name;
        this.host = _host;
    }

    @Override
    public String toString(){
        return this.host + " hosts " + this.name;
    }
}
