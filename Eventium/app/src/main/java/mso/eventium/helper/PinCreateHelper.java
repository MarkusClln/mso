package mso.eventium.helper;

public class PinCreateHelper {

    public PinCreateHelper(String pinName, String pinId) {
        this.pinName = pinName;
        this.pinId = pinId;
    }

    public String getPinName() {
        return pinName;
    }

    public void setPinName(String pinName) {
        this.pinName = pinName;
    }

    public String getPinId() {
        return pinId;
    }

    public void setPinId(String pinId) {
        this.pinId = pinId;
    }

    private String pinName;
    private String pinId;
}
