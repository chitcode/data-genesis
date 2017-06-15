package example.com.ioa.Pojo;

/**
 * Created by VIVEK on 13-06-2017.
 */

public class WifiData {

    String ssid,bssid,capablities,frequency,level,timestamp,distance;

    public WifiData(String ssid,String bssid,String capablities,String frequency,String level,String timestamp)
    {
        setBssid(bssid);
        setSsid(ssid);
        setCapablities(capablities);
        setFrequency(frequency);
        setLevel(level);
        setTimestamp(timestamp);
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }


    public String getCapablities() {
        return capablities;
    }

    public void setCapablities(String capablities) {
        this.capablities = capablities;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {

    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
