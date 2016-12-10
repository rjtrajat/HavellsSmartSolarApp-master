package in.chipmonk.havellsolarapp;


import android.media.Image;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.util.*;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import  android.content.Context;
import android.os.Handler;
import android.widget.ImageView;
import android.support.v7.app.ActionBar.*;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.List;
import java.util.UUID;
import android.bluetooth.le.*;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.os.Parcelable;
import java.lang.reflect.*;
import in.chipmonk.havellsolarapp.bluetoothComponent;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.Manifest;
import android.support.v7.app.*;
import  android.content.DialogInterface;
import android.annotation.TargetApi;


/**
 * Created by Rajat on 05/08/16.
 */
public class lamps extends AppCompatActivity {


    static public boolean can_connect;
    public  BluetoothGatt gatt;
    public TextView on_off_msg;
    public BluetoothAdapter mBluetoothAdapter;
    public Handler mHandler;
    public Handler UiHandler;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public Animation  rotate;
    public BluetoothDevice device_global;
    public boolean check = false;
    public TextView messages;
    public BluetoothLeScanner mLEScanner;
    public static final int REQUEST_ENABLE_BT = 1;
    public static final long SCAN_PERIOD = 10000;
    public android.bluetooth.le.ScanSettings settings;
    public List<android.bluetooth.le.ScanFilter> filters;
    public BluetoothGattCharacteristic tx;
    public Button connectbutton;
    public Button onofflamp;
    public Button brightbutton;
    public static TextView connectedview;
    public RelativeLayout lampconnectedimage;
    public RelativeLayout search_circle;
    public  BluetoothDevice device;
    public static UUID UART_UUID = UUID.fromString("4914dd84-8b51-42fd-884b-06bc9e5b96af");
    public static UUID TX_UUID = UUID.fromString("605daea97-20d6-46ce-8bcd-4f3e1efe2208");
    public  int screenHeight ;
    public  int screenWidth;
    public  AlertDialog.Builder alertDialogBuilder ;
    public Timer timer;
    public AlertDialog alertDialog;


    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);


        getSupportActionBar().hide();
        setContentView(R.layout.lamps);

        on_off_msg=(TextView) findViewById(R.id.onoff_id) ;

        can_connect=true;
        bluetoothComponent.Devicelist = null;

        mHandler = new Handler();
        UiHandler = new Handler();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManagernew =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManagernew.getAdapter();
        bluetoothComponent.currentActivity=lamps.this;

        //temporary pairing register

/*

if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){


    if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_DENIED){

        System.out.println("Marshmallow inside");

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This app needs location access");
        builder.setMessage("Please grant location access so this app can detect beacons .");
        builder.setPositiveButton(android.R.string.ok,null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
            public void onDismiss(DialogInterface dialog){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }

                });
        builder.show();
            }
        }*/




        //This is done for marshmallow

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons .");
                builder.setPositiveButton(android.R.string.ok,null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    public void onDismiss(DialogInterface dialog){
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();

        }


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

         screenHeight = displaymetrics.heightPixels;
         screenWidth = displaymetrics.widthPixels;

        alertDialogBuilder = new AlertDialog.Builder(this);
        timer = new Timer();

    }



    public void refreshUI(){

        System.out.println("came in list view");

        TableLayout tablelayout = (TableLayout)findViewById(R.id.tablelayout_id);


        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));




        TextView image = new TextView(this);
        image.setBackgroundResource(R.drawable.lamp);
        tr.addView(image);
        TextView devicename = new TextView(this);

        devicename.setWidth((screenWidth*33)/100);

        if(bluetoothComponent.device.getName()==null)
        devicename.setText(bluetoothComponent.device.toString());
        else{
            devicename.setText(bluetoothComponent.device.getName().toString());

        }
      //  System.out.println("Device name is address"+bluetoothComponent.device.getName().toString());

        devicename.setTextColor(Color.WHITE);
        devicename.setTextSize(13);
        devicename.setPadding(10,0,10,0);


        tr.addView(devicename);

        TextView connect_device = new TextView(this);
        connect_device.setWidth((screenWidth*24)/100);

        connect_device.setText("Connect");


        connect_device.setTextColor(Color.WHITE);
        connect_device.setBackgroundColor(Color.parseColor("#036c96"));
        connect_device.setPadding(20,10,20,10);
       connect_device.setTextSize(13);
        connect_device.setTag("connect_device"+bluetoothComponent.device.toString());
        connect_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectdevice(v);
            }
        });

        tr.addView(connect_device);
        TextView disconnect_device = new TextView(this);

        disconnect_device.setWidth((screenWidth*24)/100);
        disconnect_device.setText("Disconnect");
        disconnect_device.setBackgroundColor(Color.parseColor("#024864"));
        disconnect_device.setPadding(20,10,20,10);
        disconnect_device.setTextColor(Color.WHITE);
        disconnect_device.setTextSize(13);
        disconnect_device.setTag("disconnect_device"+bluetoothComponent.device.toString());
        disconnect_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectdevice(v);
            }
        });

        tr.addView(disconnect_device);




tr.setGravity(Gravity.CENTER);


        tablelayout.addView(tr,new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothComponent.currentActivity=lamps.this;





        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
            }
            runOnUiThread(new Runnable(){
                @Override
                public void run(){

                    if(mLEScanner==null)
                        System.out.println("check mLeScanner in lamp null");
                    else
                        System.out.println("check mLeScanner in lamp not null");

                     bluetoothComponent.scanflag=0;
                     bluetoothComponent.connectflag=0;
                    bluetoothComponent.canrefreshsearch=false;
                 bluetoothComponent.scanLeDevice(true,mLEScanner,mBluetoothAdapter);
                    final TextView searchtext = (TextView) findViewById(R.id.searching_id);
                    searchtext.setText("Searching...");


bluetoothComponent.scanflag=0;

                                mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(bluetoothComponent.scanflag==1){
                        bluetoothComponent.canrefreshsearch=true;
                    }
                    else if(bluetoothComponent.scanflag==0){
                        bluetoothComponent.canrefreshsearch=true;
                        final TextView searchtext = (TextView)findViewById(R.id.searching_id);
                        searchtext.setText("No Device found");
                        bluetoothComponent.scanLeDevice(false,mLEScanner,mBluetoothAdapter);
                    }
                }
            },10000);


                }
            });


        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void gotolight_control(){

        if(bluetoothComponent.gatt_connection_check==null)
            Toast.makeText(this,"Please check connection",Toast.LENGTH_SHORT).show();

        else {

           final TextView searchtext = (TextView)findViewById(R.id.searching_id);
            searchtext.setText("Loading data...");

        final String message;

  message = "read rfrsh:";





            runOnUiThread(new Runnable() {

                @Override
                public void run() {


                    bluetoothComponent.tx = bluetoothComponent.gatt.getService(bluetoothComponent.UART_UUID).getCharacteristic(bluetoothComponent.TX_UUID);

                    if (bluetoothComponent.tx != null) {
                        bluetoothComponent.tx.setValue(message);

                        bluetoothComponent.write();


                    } else
                        System.out.println("tx is null");


                }
            });










mHandler.postDelayed(new Runnable() {
    @Override
    public void run() {
        searchtext.setText("");

        if(bluetoothComponent.connected_device!=null){
        Intent intent = new Intent(lamps.this,light_control.class);

        startActivity(intent);}
    }
},5000);



        }



    }
    public void onBacklamps(View view){


        final TextView viewToAnimate = (TextView) findViewById(R.id.back_arrow_lamps_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        Intent intent = new Intent(lamps.this,MainActivity.class);
        startActivity(intent);
    }



    public void refreshscan_lamps(View view){

//temporary


   //     Intent intent = new Intent(lamps.this,light_control.class);
     //   startActivity(intent);






        final ImageView viewToAnimate = (ImageView)findViewById(R.id.refresh_button_lamps_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        if(bluetoothComponent.canrefreshsearch) {

            can_connect = true;
            bluetoothComponent.Devicelist = null;
            TableLayout tablelayout = (TableLayout) findViewById(R.id.tablelayout_id);


//        getSupportActionBar().hide();
//        setContentView(R.layout.lamps);

            tablelayout.removeAllViews();

            final TextView searchtext = (TextView) findViewById(R.id.searching_id);
            searchtext.setText("Searching...");
            onResume();
        }
        else{
            alertDialogBuilder.setMessage("Already Searching...");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    //    timer.cancel();
                }
            },1500);

            //Toast.makeText(this,"Already Searching...",Toast.LENGTH_SHORT).show();
        }


    }



    public void connectdevice(final View view) {


        if (can_connect == true) {
            can_connect=false;
            String tagname = view.getTag().toString();
            bluetoothComponent.canrefreshsearch=true;

            tagname = tagname.replace("connect_device", "");

           BluetoothDevice tempdevice = null;
            for (BluetoothDevice eachdevice : bluetoothComponent.Devicelist) {


                if (tagname.equals(eachdevice.toString())) {

                    tempdevice = eachdevice;
                    break;
                }

            }

            final BluetoothDevice tempconnectdevice = tempdevice;

            bluetoothComponent.scanLeDevice(false, mLEScanner,mBluetoothAdapter);

            if(tempdevice==null){
                System.out.println("tempdevice is null");
            }




            tempdevice.createBond();


            bluetoothComponent.gatt = tempdevice.connectGatt(getApplicationContext(), false, bluetoothComponent.callbacknew);


            final BluetoothDevice tempdeviceforpair = device;

          //  tempdevice.createBond();





            ((TextView) view).setText("Connecting");
            final TextView searchtext = (TextView)findViewById(R.id.searching_id);
            searchtext.setText("");

            bluetoothComponent.connectflag = 0;

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (bluetoothComponent.connectflag == 1) {
bluetoothComponent.connected_device = tempconnectdevice;


                        //temporary begin for pair

//                        try {
//                            Method method = tempconnectdevice.getClass().getMethod("createBond", (Class[]) null);
//                            method.invoke(tempconnectdevice, (Object[]) null);
//                            System.out.println(" in pair");
//                        } catch (Exception e) {
//
//                            System.out.println("Problem in pair "+e);
//                            e.printStackTrace();
//                        }

                    //    tempdeviceforpair.createBond();



                        gotolight_control();
                        ((TextView) view).setText("Connected");
                        connectedview =(TextView) view;

                    } else if (bluetoothComponent.connectflag == 0||bluetoothComponent.connectflag==2) {
                        ((TextView) view).setText("Connect");
                        connectedview =(TextView) view;
                        can_connect=true;
                        alertDialogBuilder.setMessage("Please check Connection");
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                                //    timer.cancel();
                            }
                        },1500);

                    //    Toast.makeText(lamps.this, "Please check Connection", Toast.LENGTH_SHORT).show();

                    }

                }
            }, 4000);

        }
        else{
            alertDialogBuilder.setMessage("One Device is already in connection state");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    //    timer.cancel();
                }
            },1500);

          //  Toast.makeText(this,"One Device is already in connection state",Toast.LENGTH_SHORT).show();
        }

    }
    public void disconnectdevice(View view){
        String tagname = view.getTag().toString();

        tagname = tagname.replace("disconnect_device", "");
        if (bluetoothComponent.connected_device!=null&&tagname.equals(bluetoothComponent.connected_device.toString())) {

            //temporary begin for unpair
//
//            try {
//                Method method = bluetoothComponent.connected_device.getClass().getMethod("removeBond", (Class[]) null);
//                method.invoke(bluetoothComponent.connected_device, (Object[]) null);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            //temporary end

bluetoothComponent.gatt.disconnect();

            bluetoothComponent.connected_device=null;
            connectedview.setText("Connect");
            can_connect=true;

        }
        else{

            alertDialogBuilder.setMessage("Not Connected");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    //    timer.cancel();
                }
            },1500);
           // Toast.makeText(this,"Not Connected",Toast.LENGTH_SHORT).show();
        }

    }

/*

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state        = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState    = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                   Toast.makeText(lamps.this,"Paired",Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    Toast.makeText(lamps.this,"UnPaired",Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
*/


/*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }*/

}

