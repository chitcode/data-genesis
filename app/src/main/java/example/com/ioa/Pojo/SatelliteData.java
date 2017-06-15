package example.com.ioa.Pojo;

/**
 * Created by VIVEK on 14-06-2017.
 */

public class SatelliteData {

    // " + satellite.getPrn() + "," + satellite.usedInFix() + "," + satellite.getSnr() + "," + satellite.getAzimuth() + "," + satellite.getElevation()+ "\n\n";
    private String prn,snr,azimuth,elevation,usedinfix;

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public String getSnr() {
        return snr;
    }

    public void setSnr(String snr) {
        this.snr = snr;
    }

    public String getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(String azimuth) {
        this.azimuth = azimuth;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getUsedinfix() {
        return usedinfix;
    }

    public void setUsedinfix(String usedinfix) {
        this.usedinfix = usedinfix;
    }
}
