package example.com.ioa.Util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.util.ArraySet;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import example.com.ioa.Pojo.CaptureData;
import example.com.ioa.Pojo.CellInfoData;
import example.com.ioa.Pojo.GpsData;
import example.com.ioa.Pojo.IOAData;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Gunjan.K.Kumar on 28-05-2017.
 */
public class Utils {

    private static final String PREFNCES ="pref" ;

    public static final String NOTFICATION_TIME_KEY ="nfctime",PHONE_NUMBER="phone_number",NOTIFICATION_ON_OFF="notification_on_off",SELECTED_DAY="selected_day",START_TIME="start_time",END_TIME="end_time",CAPTURE_KEY= "capture_key";
    private static String rsrp,rsrq,cqi,rssnr,rssi,sinr,ss,rxlevel,ta,wifi_json;


    private static List<ScanResult> scanList;
    public static void sendDataToserver(Context context){
        Intent i = new Intent(context, SyncService.class);
        context.startService(i);
    }


    public static boolean isNetworkConnected(Context contetx) {
        ConnectivityManager cm = (ConnectivityManager) contetx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public static GpsData getAllGpsData(Context context){

        GpsData gpsdata=new GpsData();
        GPSTracker gpsTracker = new GPSTracker(context);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            gpsdata.setLattitude(stringLatitude);

            String stringLongitude = String.valueOf(gpsTracker.longitude);
            gpsdata.setLongitude(stringLongitude);

            String country = gpsTracker.getCountryName(context);
            gpsdata.setCountry(country);

            String city = gpsTracker.getLocality(context);
            gpsdata.setCity(city);

            String postalCode = gpsTracker.getPostalCode(context);
            gpsdata.setPincode(postalCode);

            String addressLine = gpsTracker.getAddressLine(context);
            gpsdata.setAddress(addressLine);
        }
        return gpsdata;
    }

    public static boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static String getMobileNumber(Context context){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        final String phoneNumber = tm.getLine1Number();
        return phoneNumber;
    }


    public static String getDeviceid(Context context){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        final String deviceId = tm.getDeviceId();
        return deviceId;
    }




    public static CellInfoData getAllCellInfo(Context ctx){
        CellInfoData cellinfo = new CellInfoData();
        TelephonyManager tel = (TelephonyManager) ctx.getSystemService(TELEPHONY_SERVICE);

        JSONArray cellList = new JSONArray();

// Type of the network
        int phoneTypeInt = tel.getPhoneType();
        String phoneType = null;
        phoneType = phoneTypeInt == TelephonyManager.PHONE_TYPE_GSM ? "gsm" : phoneType;
        phoneType = phoneTypeInt == TelephonyManager.PHONE_TYPE_CDMA ? "cdma" : phoneType;

        //from Android M up must use getAllCellInfo
     /*   if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {



            List<NeighboringCellInfo> neighCells = tel.getNeighboringCellInfo();
            for (int i = 0; i < neighCells.size(); i++) {
                try {
                    JSONObject cellObj = new JSONObject();
                    NeighboringCellInfo thisCell = neighCells.get(i);
                    cellObj.put("cellId", thisCell.getCid());
                    cellObj.put("lac", thisCell.getLac());
                    cellObj.put("rssi", thisCell.getRssi());
                    cellList.put(cellObj);
                } catch (Exception e) {
                }
            }

        } else {*/
        List<CellInfo> infos = tel.getAllCellInfo();
        for (int i = 0; i<infos.size(); ++i) {
            try {
                JSONObject cellObj = new JSONObject();
                CellInfo info = infos.get(i);
                if (info instanceof CellInfoGsm){
                    CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                    CellIdentityGsm identityGsm = ((CellInfoGsm) info).getCellIdentity();
                    cellObj.put("cellId", identityGsm.getCid());
                    cellObj.put("lac", identityGsm.getLac());
                    cellObj.put("dbm", gsm.getDbm());
                    Log.v("data1",gsm.toString());
                    rssi=gsm.getDbm()+"";

                    cellList.put(cellObj);
                } else if (info instanceof CellInfoLte) {
                    cellObj.put("simnetwork", "LTE");
                    CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                    CellIdentityLte identityLte = ((CellInfoLte) info).getCellIdentity();
                    cellObj.put("cellId", identityLte.getCi());
                    cellObj.put("tac", identityLte.getTac());
                    cellObj.put("dbm", lte.getDbm());
                    cellObj.put("asulevel",lte.getAsuLevel());
                    cellObj.put("level",lte.getLevel());

                    Log.v("data2",lte.toString());

                    rssi=lte.getDbm()+"";

                    String lte_data=lte.toString();
                    String[] separated_data = lte_data.split(" ");
                    for(int k=0;k<6;k+=1)
                    {
                        Log.d("lte_data"+k,separated_data[k]);
                        if(k==1)
                        {
                            String[] ss_seperated=separated_data[k].split("=");
                            if(ss_seperated[0].trim().equalsIgnoreCase("rsrp"))
                            {
                                ss=ss_seperated[1].trim();
                                rxlevel=ss;
                            }
                        }
                        if(k==2)
                        {
                            String[] rsrp_seperated=separated_data[k].split("=");
                            if(rsrp_seperated[0].trim().equalsIgnoreCase("rsrp"))
                            {
                                rsrp=rsrp_seperated[1].trim();
                            }
                        }
                        else if(k==3)
                        {
                            String[] rsrq_seperated=separated_data[k].split("=");
                            if(rsrq_seperated[0].trim().equalsIgnoreCase("rsrq"))
                            {
                                rsrq=rsrq_seperated[1].trim();
                            }
                        }
                        else if(k==4)
                        {
                            String[] rssnr_seperated=separated_data[k].split("=");
                            if(rssnr_seperated[0].trim().equalsIgnoreCase("rssnr"))
                            {
                                rssnr=rssnr_seperated[1].trim();
                                sinr=rssnr;
                            }
                        }
                        else if(k==5)
                        {
                            String[] cqi_seperated=separated_data[k].split("=");
                            if(cqi_seperated[0].trim().equalsIgnoreCase("cqi"))
                            {
                                cqi=cqi_seperated[1].trim();
                            }
                        }
                        else if(k==6)
                        {
                            String[] ta_seperated=separated_data[k].split("=");
                            if(ta_seperated[0].trim().equalsIgnoreCase("ta"))
                            {
                                ta=ta_seperated[1].trim();
                            }
                        }
                    }
                    //ss=31 rsrp=-85 rsrq=-20 rssnr=2147483647 cqi=2147483647 ta=2147483647
                    cellList.put(cellObj);
                }


            } catch (Exception ex) {

            }
        }
        //}
        cellinfo.setCellInfoArray(cellList.toString());
        return cellinfo;
    }


    public static void performActionOnButtonClick(Context context,String btnName){
        new IOADBUtil(context).addIOAData(createJsonData(context,btnName));
        if(Utils.isNetworkConnected(context))
            Utils.sendDataToserver(context);
    }
    private static String createJsonData(Context context,String btnName){

        getWifiNetworksList(context);
        Gson gson =new Gson();
        CellInfoData cellInfo=Utils.getAllCellInfo(context);
        GpsData gpsdata=Utils.getAllGpsData(context);
        IOAData Iodata=new IOAData();
        CaptureData captureData=new CaptureData();
        captureData.setBtntype(btnName);
        captureData.setUserid(Utils.getDeviceid(context));
        captureData.setGpsStatus(Utils.isGPSEnabled(context)+"");
        captureData.setCellInfo(cellInfo.getCellInfoArray());
        captureData.setDeviceId(Utils.getDeviceid(context));
        captureData.setLatency("");
        captureData.setLongitude(gpsdata.getLongitude());
        captureData.setLattitude(gpsdata.getLattitude());
        captureData.setAddress(gpsdata.getAddress());
        captureData.setPostalcode(gpsdata.getPincode());
        captureData.setCity(gpsdata.getCity());
        captureData.setRssrp(rsrp);
        captureData.setRxLevel(rxlevel);
        captureData.setSinr(sinr);
        captureData.setRssi(rssi);
        captureData.setTa(ta);
        captureData.setCqi(cqi);
        captureData.setWifijson(wifi_json);

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        captureData.setSending_timestamp(ts);

        captureData.setChargerConnected(isConnected(context)+"");
        captureData.setMobileNumber(Utils.getDataFromSharedPref(context,Utils.PHONE_NUMBER));
        captureData.setImei(getIMEI(context));

        Gson gson1=new Gson();
        String cData=gson1.toJson(captureData, CaptureData.class);
        Iodata.setJsonData(cData);

        String data=gson.toJson(Iodata, IOAData.class);
        return data;

    }

    private static void getWifiNetworksList(Context context){
        try
        {
        final String[] ssid = new String[1];
        final String[] bssid = new String[1];
        final String[] capablities = new String[1];
        final String[] frequency = new String[1];
        final String[] level = new String[1];
        final String[] timestamp = new String[1];
        final String distance;
        final JSONArray jsonArray=new JSONArray();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        final WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.startScan();

            scanList = wifiManager.getScanResults();

            Log.d("wifi length",scanList.size()+"");

            HashSet<String> scanResultHashSet=new HashSet<>();

            //sb.append("\n  Number Of Wifi connections :" + " " +scanList.size()+"\n\n");
                for(int i = 0; i < scanList.size(); i++){
                    bssid[0] =scanList.get(i).BSSID;
                    ssid[0] =scanList.get(i).SSID;
                    capablities[0]=scanList.get(i).capabilities;
                    frequency[0] =scanList.get(i).frequency+"";
                    level[0] =scanList.get(i).level+"";
                   // timestamp[0] =scanList.get(i).timestamp+"";
                    Log.d("wifi details",scanList.get(i).toString());

                     JSONObject object=new JSONObject();
                    try {
                        object.put("ssid", ssid[0]);
                        object.put("bssid",bssid[0]);
                        object.put("capabilities",capablities[0]);
                        object.put("frequency",frequency[0]);
                        object.put("level",level[0]);
                       // if(scanResultHashSet.add(ssid[0])==true) {
                            jsonArray.put(object);
                        //}

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                wifi_json=jsonArray.toString();

                Log.d("wifi json",jsonArray.length()+wifi_json);
        }
        catch(Exception e)
        {

        }
    }
    public static void saveDataInPref(Context context,String data,String key){
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREFNCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key,data);
        editor.commit();
    }



    public static String getDataFromSharedPref(Context context,String key){
        SharedPreferences sharedpreferences = context.getSharedPreferences(PREFNCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key,null);
    }
    public static boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }

    public  static String getPhoneNumber(Context context)
    {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        return number;
    }

    public static String getIMEI(Context context)
    {
        TelephonyManager mngr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }


}
