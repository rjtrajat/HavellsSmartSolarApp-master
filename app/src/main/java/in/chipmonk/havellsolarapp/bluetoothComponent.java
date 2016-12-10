package in.chipmonk.havellsolarapp;

/**
 * Created by Rajat on 14/08/16.
 */



import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.View;
import android.util.*;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import  android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import android.widget.TextView;
import android.os.Parcel;
import android.app.Activity;
import android.content.pm.PackageManager;
import java.util.ArrayList;
import java.util.*;
import android.os.Build;
import android.bluetooth.BluetoothGatt;
import android.widget.Button;
import android.bluetooth.*;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.widget.EditText;
import java.util.List;
import java.util.UUID;
import android.bluetooth.le.*;

import in.chipmonk.havellsolarapp.loggedIn;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.os.Parcelable;





public class bluetoothComponent extends AppCompatActivity {

    static public boolean refreshcheck = true;
    static  public  BluetoothGatt gatt;
    static public BluetoothLeScanner mLeScanner_bluetooth ;
    static public  BluetoothAdapter mBluetoothAdapter_bluetooth;
    static public  BluetoothDevice device;
    static public int scanflag=0;
    static public BluetoothGatt gatt_connection_check;
    static public int connectflag=0;
    static public long SCAN_PERIOD = 5000;
    static public Handler mHandler_bluetooth;
    static public UUID UART_UUID = UUID.fromString("4914dd84-8b51-42fd-884b-06bc9e5b96af");
    static public UUID TX_UUID = UUID.fromString("05daea97-20d6-46ce-8bcd-4f3e1efe2208");
    static public String Battery_status_voltage;
    static public String Battery_status_current;
    static public String Solar_panel_staus_voltage ;
    static public String Solar_panel_staus_current;
    static public String load_status_voltage;
    static public String load_status_current;
    static public String  System_status_watt;
    static public String  Solar_status_watt;
    static public String Battery_capacity;
    static public String light_status="LUMINAIRE ON";
    static public List<BluetoothDevice> Devicelist = null;
    static public BluetoothDevice connected_device;
    static public String Maplocationlat="";
    static public String Maplocationlong="";
    static public String Maplocationname="";
    static public boolean canrefreshsearch=true;
    static public String sday1="0",sday2="0",sday3="0",lday1="0",lday2="0",lday3="0";
    static public boolean graphcanload=true;
    static public boolean graphdrawcheck=false;


    static public boolean command_send=true;
    static public UUID NAME_UUID=UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    static public UUID NAME_CHAR_UUID=UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");


    static public UUID NAME_UUID_LIGHT=UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    static public UUID NAME_CHAR_UUID_LIGHT=UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
    static public BluetoothGattCharacteristic tx;
    static public boolean readName=false;
    static public String DeviceName = "";
    static public Activity currentActivity = null;




   static public int scanLeDevice(final boolean enable, BluetoothLeScanner mLEScanner,BluetoothAdapter mBluetoothAdapter) {
       System.out.println("result check called now");
       mLeScanner_bluetooth=mLEScanner;
       mBluetoothAdapter_bluetooth = mBluetoothAdapter;

       if(mLEScanner==null)
           System.out.println("check mLeScanner in bluetooth null");
       else
           System.out.println("check mLeScanner in bluetooth not null");


       mHandler_bluetooth = new Handler();
        if (enable) {
//            mHandler_bluetooth.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (Build.VERSION.SDK_INT < 21) {
//
//                    } else {
//                        if(scanflag!=1){
//                        scanflag=2;
//                        mLeScanner_bluetooth.stopScan(mScanCallback);}
//
//                    }
//                }
//            }, 4000);
            if (Build.VERSION.SDK_INT < 21) {
                System.out.println("Here I am coming less");

                mBluetoothAdapter_bluetooth.startLeScan(mLeScanCallback);

            } else {
System.out.println("result check before scan");
                mLeScanner_bluetooth.startScan(
                        mScanCallback

                );
                System.out.println("result check after scan");

            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter_bluetooth.stopLeScan(mLeScanCallback);


            } else {
                System.out.println("result check before stop");
                mLeScanner_bluetooth.stopScan(mScanCallback);
                System.out.println("result check after stop");
return 0;
            }
        }
       return 0;
    }



   static private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {


            scanflag=1;
            System.out.println("result check flag blue is "+bluetoothComponent.scanflag);
            System.out.println("device is "+device);

            boolean found=false;

            if(currentActivity!=null){
                if(currentActivity instanceof  lamps){

                    if(Devicelist!=null)
                    {
                        for(BluetoothDevice tempdevice : Devicelist){

                            System.out.print("temp and dev "+tempdevice+" "+device);

                            if(tempdevice.toString().equals(device.toString())){
                                System.out.println("found ");
                                found=true;
                                break;
                            }
                        }}
                    else
                        Devicelist=new ArrayList<>();

                    if(found==false){
                        System.out.println("Here I am entering");
                        Devicelist.add(device);
                        lamps presentActivity = (lamps) currentActivity;
                        presentActivity.refreshUI();

                    }

                }
            }


            // scanLeDevice(false,mLeScanner_bluetooth );




        }
    };



    static public ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result) {


            device = result.getDevice();
          // System.out.println( "device is "+resulzt.getDevice().getName());
            scanflag=1;
            System.out.println("result check flag blue is "+bluetoothComponent.scanflag);
            System.out.println("device is "+device);

            boolean found=false;

            if(currentActivity!=null){
                if(currentActivity instanceof  lamps){

if(Devicelist!=null)
                    {
                    for(BluetoothDevice tempdevice : Devicelist){

                        System.out.print("temp and dev "+tempdevice+" "+device);

                        if(tempdevice.toString().equals(device.toString())){
                            System.out.println("found ");
                            found=true;
                            break;
                        }
                    }}
                    else
Devicelist=new ArrayList<>();

if(found==false){
    System.out.println("Here I am entering");
    Devicelist.add(device);
                    lamps presentActivity = (lamps) currentActivity;
                    presentActivity.refreshUI();

}

                }
            }


          // scanLeDevice(false,mLeScanner_bluetooth );


        }

      /*  @Override
        public void onBatchScanResults(List<ScanResult> results){
            System.out.println("called this time");
            for(ScanResult result : results){
                System.out.println("device using batch is "+result.getDevice());

            }

        }
*/




    };


     public void connectdevice_bluetooth(){

         System.out.println("At least inside");

if(gatt!=null)
    System.out.println("gatt is not null");
        if(gatt==null) {

            System.out.println("gatt null I am ");
            System.out.println("gatt Here new println is "+device);

            BluetoothDevice  newdevice;
            newdevice = device;
            device.connectGatt(getApplicationContext(), false, callbacknew);
            System.out.println("gatt Here after  println is "+device);
        }

    }


    static public BluetoothGattCallback callbacknew = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            super.onConnectionStateChange(gatt, status, newState);
System.out.println("connection coming here");

            if (newState == BluetoothGatt.STATE_CONNECTED) {

                System.out.println("connect Connected!");

                if (!gatt.discoverServices()) {
                    System.out.println("Failed to start discovering services!");
                }



            }
            else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                System.out.println("connect Disconnected!");
                gatt_connection_check=null;
            connectflag=2;
            }
            else {

                System.out.println("Connection state changed.  New state: " + newState);
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            gatt_connection_check=gatt;

            if (status == BluetoothGatt.GATT_SUCCESS) {
                System.out.println("Service discovery completed!");
            }
            else {
                System.out.println("Service discovery failed with status: " + status);
            }
            connectflag=1;

            List<BluetoothGattCharacteristic> characteristics=null;
            List<BluetoothGattService> services = gatt.getServices();
            for (final BluetoothGattService service : services) {
                characteristics = service.getCharacteristics();
                System.out.println("serchar service :"+service.getUuid());

                for(final BluetoothGattCharacteristic character :characteristics){
                    if(character!=null){
                        //  System.out.println("serchar property "+character.getProperties());

                        gatt.setCharacteristicNotification(character, true);
                        System.out.println("serchar charc :"+character.getUuid());




                    }
                }

            }

            BluetoothGattCharacteristic tempchar  = gatt.getService(UART_UUID).getCharacteristic(TX_UUID);

            gatt.setCharacteristicNotification(tempchar, true);

            for (BluetoothGattDescriptor descriptor : tempchar.getDescriptors()) {


                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);




            }






        }

        // Called when a remote characteristic changes (like the RX characteristic).
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);




            try{
                String s = new String(characteristic.getValue(), "US-ASCII");
                System.out.println("response is "+s);

                if(s.contains("BATv:")){
                    s=s.replace("BATv: ","");
                    Battery_status_voltage = s;
                }
                else if(s.contains("PANv:")){
                    s=s.replace("PANv:","");
                    Solar_panel_staus_voltage=s;
                }
                else if(s.contains("LODv:")){
                    s=s.replace("LODv:","");
                    load_status_voltage=s;
                }

                else if(s.contains("BATi:")){
                    s=s.replace("BATi:","");
                    Battery_status_current=s;
                }
                else if(s.contains("PANi:")){
                    s=s.replace("PANi:","");
                    Solar_panel_staus_current=s;
                }
                else if(s.contains("LODi:")){
                    s=s.replace("LODi:","");
                    load_status_current=s;
                }
                else if(s.contains("SYSw:")){
                    s=s.replace("SYSw:","");
                    System_status_watt=s;
                }

                else if(s.contains("SOLw:")){
                    s=s.replace("SOLw:","");
                    Solar_status_watt=s;
                }

                else if(s.contains("LD1:")){
                    s=s.replace("LD1:","");
                    lday1=s.trim();
                }
                else if(s.contains("LD2:")){
                    s=s.replace("LD2:","");
                    lday2=s.trim();
                }
                else if(s.contains("LD3:")){
                    s=s.replace("LD3:","");
                    lday3=s.trim();
                }
                else if(s.contains("SD1:")){
                    s=s.replace("SD1:","");
                    sday1=s.trim();
                }
                else if(s.contains("SD2:")){
                    s=s.replace("SD2:","");
                    sday2=s.trim();
                }
                else if(s.contains("SD3:")){
                    graphdrawcheck=true;
                    s=s.replace("SD3:","");
                    sday3=s.trim();
                }
                else if(s.contains("Cty:")){
                    s=s.replace("Cty:","");
                    Battery_capacity=s.trim();
                }


                if (currentActivity != null)
                {
                    if (currentActivity instanceof loggedIn) {
                        //currentActivity = new loggedIn();
                        loggedIn presentActivity = (loggedIn) currentActivity;
                        presentActivity.refreshUI();
                    }
                    if (currentActivity instanceof battery_status) {
                        //currentActivity = new loggedIn();
                        battery_status presentActivity = (battery_status) currentActivity;
                        presentActivity.refreshUI();
                    }
                    if (currentActivity instanceof solar_panel_live_voltage) {
                        //currentActivity = new loggedIn();
                        solar_panel_live_voltage presentActivity = (solar_panel_live_voltage) currentActivity;
                        presentActivity.refreshUI();
                    }



//                    if (currentActivity instanceof light_control) {
//                        //currentActivity = new loggedIn();
//                        light_control presentActivity = (light_control) currentActivity;
//                        presentActivity.refreshUI();
//                    }


                }


            }
            catch(Exception e){

            }






            System.out.println("oncharacteristicchange");
        }
        @Override
        public void  onCharacteristicRead (BluetoothGatt gatt,
                                           final  BluetoothGattCharacteristic characteristic,
                                           int status){
            try{
                String s = new String(characteristic.getValue(), "US-ASCII");
                System.out.println("oncharacteristicread "+s);

                if(readName==true)
                    DeviceName=s;
            }
            catch(Exception e){

            }




        }
        @Override
        public void onDescriptorWrite (BluetoothGatt gatt,
                                BluetoothGattDescriptor descriptor,
                                int status){
            System.out.println("get descriptor inside called");
        }



    //    @Override
//        public void  onCharacteristicWrite (BluetoothGatt gatt,
//                                            final  BluetoothGattCharacteristic characteristic,
//                                            int status){
//
//
//                                                try{
//                                                String s = new String(characteristic.getValue(), "US-ASCII");
//                                                    System.out.println("oncharacteristicwrite "+s);
//
//
//
//
//                                                }
//                                                catch(Exception e){
//
//                                                }
//
//
//        }

    };



    static public void write(){
        System.out.println("oncharacteristicbeforewrite");
       gatt.writeCharacteristic(tx);
        tx = gatt.getService(NAME_UUID).getCharacteristic(NAME_CHAR_UUID);

        System.out.println("oncharacteristicbeafterwrite");}


static public void read(){
    System.out.println("oncharacteristicbeforeread");
if(gatt.readCharacteristic(tx))
    System.out.println("oncharacteristicreadable");
    else
System.out.println("oncharacteristicnotreadable");
    ;
    System.out.println("oncharacteristicbeafterread");


}

    static public void readName(){
        readName=true;
        System.out.println("oncharacteristicbeforeread");
        if(gatt.readCharacteristic(tx))
            System.out.println("oncharacteristicreadable");
        else
            System.out.println("oncharacteristicnotreadable");
        ;
        System.out.println("oncharacteristicbeafterread");


    }


}
