package example.com.ioa.Pojo;

import java.util.ArrayList;

/**
 * Created by Gunjan.K.Kumar on 28-05-2017.
 */
public class CaptureData {
    String userid;
    String rssrp;
    String rssi;
    String rsrq;
    String sinr;
    String cellid;
    String chargerConnected;
    String voltage,temprature;
    String gpsStatus;
    String lattitude;
    String longitude;
    String altitude,speed,accuracy;
    String latency;
    String rxLevel;
    String psc;
    String deviceId;
    String mobileNumber;
    String city;
    String postalcode;
    String address;
    String imei;
    String cqi,ta,asulevel,level;
    ArrayList<WifiData> wifijson;
    String wifijson_string;
    String sending_timestamp;
    String gpsSatelliteData;
    String audio_file_base64;
    String device_model,android_version;
    String operator_name;
    String airplane_mode;


    public String getWifijson_string() {
        return wifijson_string;
    }

    public void setWifijson_string(String wifijson_string) {
        this.wifijson_string = wifijson_string;
    }

    public String getRsrq() {
        return rsrq;
    }

    public void setRsrq(String rsrq) {
        this.rsrq = rsrq;
    }

    public String getAirplane_mode() {
        return airplane_mode;
    }

    public void setAirplane_mode(String airplane_mode) {
        this.airplane_mode = airplane_mode;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }


    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getTemprature() {
        return temprature;
    }

    public void setTemprature(String temprature) {
        this.temprature = temprature;
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getAudio_file_base64() {
        return audio_file_base64;
    }

    public void setAudio_file_base64(String audio_file_base64) {
        this.audio_file_base64 = audio_file_base64;
    }

    public String getGpsSatelliteData() {
        return gpsSatelliteData;
    }

    public void setGpsSatelliteData(String gpsSatelliteData) {
        this.gpsSatelliteData = gpsSatelliteData;
    }

    public String getSending_timestamp() {
        return sending_timestamp;
    }

    public void setSending_timestamp(String sending_timestamp) {
        this.sending_timestamp = sending_timestamp;
    }

    public void setWifijson(ArrayList<WifiData> wifijson) {
        this.wifijson = wifijson;
    }

    public ArrayList<WifiData> getWifijson() {
        return wifijson;
    }

    public String getCqi() {
        return cqi;
    }

    public void setCqi(String cqi) {
        this.cqi = cqi;
    }

    public String getTa() {
        return ta;
    }

    public void setTa(String ta) {
        this.ta = ta;
    }

    public String getAsulevel() {
        return asulevel;
    }

    public void setAsulevel(String asulevel) {
        this.asulevel = asulevel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    String cellInfo;

    public String getCellInfo() {
        return cellInfo;
    }

    public void setCellInfo(String cellInfo) {
        this.cellInfo = cellInfo;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    String lac;

    public String getBtntype() {
        return btntype;
    }

    public void setBtntype(String btntype) {
        this.btntype = btntype;
    }

    String btntype;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRssrp() {
        return rssrp;
    }

    public void setRssrp(String rssrp) {
        this.rssrp = rssrp;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getSinr() {
        return sinr;
    }

    public void setSinr(String sinr) {
        this.sinr = sinr;
    }

    public String getCellid() {
        return cellid;
    }

    public void setCellid(String cellid) {
        this.cellid = cellid;
    }

    public String getChargerConnected() {
        return chargerConnected;
    }

    public void setChargerConnected(String chargerConnected) {
        this.chargerConnected = chargerConnected;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public String getRxLevel() {
        return rxLevel;
    }


    public void setRxLevel(String rxLevel) {
        this.rxLevel = rxLevel;
    }
}
