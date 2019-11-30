package mso.eventium.model;

import java.util.List;

public class User {

    private String firstName;
    private String lastName;
    private List<Event> hostedEvents;

    public User(String firstName, String lastName, List<Event> hostedEvents) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hostedEvents = hostedEvents;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Event> getHostedEvents() {
        return hostedEvents;
    }

    public void setHostedEvents(List<Event> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }


}
