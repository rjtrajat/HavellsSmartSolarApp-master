package in.chipmonk.havellsolarapp;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.*;
import in.chipmonk.havellsolarapp.bluetoothComponent;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import in.chipmonk.havellsolarapp.timer;


import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import in.chipmonk.havellsolarapp.lamps;


/**
 * Created by Rajat on 27/07/16.
 */
public class light_control extends AppCompatActivity implements Serializable {

    //  public static String Hourvalue="1",Minvalue="0",ON_OFF_State="ON",Wattvalue="",Brightvalue="bright 050:";

    public static String Hourvalue = "", Minvalue = "", ON_OFF_State = "OFF", Wattvalue = "", Brightvalue = "";
    public TextView textmsg;
    lamps lampvaiable;
    public Handler Handler_light;
    public RelativeLayout buttonbackground;
    public TextView offbutton;
    public TextView onbutton;
    public AlertDialog alertDialog;
    String lampname;
    static public TextView Timervalue_text;
    static public TextView Timer_on_off_text;
    static public TextView Power_value_text;
    static public TextView Map_location_text;
    static String devicenamestring = "";
    public NumberPicker perpicker_lightcontrol;
    public EditText Devicename;
    static public BluetoothGattCharacteristic devicename;
    public  AlertDialog.Builder alertDialogBuilder ;
    public  AlertDialog.Builder alertDialogBuildernew ;
    public Timer timervalue;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("value is on create");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.light_control);

        System.out.println("in light control");
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbarlight_control);


        //    bluetoothComponent.tx  = bluetoothComponent.gatt.getService(bluetoothComponent.NAME_UUID_LIGHT).getCharacteristic(bluetoothComponent.NAME_CHAR_UUID_LIGHT);

        //  bluetoothComponent.readName();

        Devicename = (EditText) findViewById(R.id.deviceName_id);
        Devicename.setText(devicenamestring);


        buttonbackground = (RelativeLayout) findViewById(R.id.lamp_on_button_background);
        Handler_light = new Handler();
        onbutton = (TextView) findViewById(R.id.lamp_on_button_new_id);
        offbutton = (TextView) findViewById(R.id.lamp_on_button_new_id_second);

//        Handler_light.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Devicename.setText(bluetoothComponent.DeviceName);
//
//
//
//            }
//        },2000);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        textmsg = (TextView) findViewById(R.id.onoff_id);
        textmsg.setText(bluetoothComponent.light_status);

        if (bluetoothComponent.light_status == "LUMINAIRE ON") {
            buttonbackground.setBackgroundResource(R.drawable.lamp_on_button);
            onbutton.setVisibility(View.INVISIBLE);
            offbutton.setVisibility(View.VISIBLE);
        } else {

            buttonbackground.setBackgroundResource(R.drawable.lamp_off_button);
            onbutton.setVisibility(View.VISIBLE);
            offbutton.setVisibility(View.INVISIBLE);


        }


        //  devicename=bluetoothComponent.gatt.getService(bluetoothComponent.NAME_UUID).getCharacteristic(bluetoothComponent.NAME_CHAR_UUID);

        //   System.out.println("Name is "+devicename.getValue());

        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
        View view = findViewById(R.id.control_button_id);
        int viewHeight = (int) (screenHeight * 35) / 100;
        int viewWidth = (int) (screenWidth * 55) / 100;
        // view.getLayoutParams().width=viewWidth;
        // view.getLayoutParams().height=viewHeight;


        viewHeight = (int) (screenHeight * 7) / 100;

        view = findViewById(R.id.lamp_name_id);

        view.getLayoutParams().height = viewHeight;
        view = findViewById(R.id.lamp_geo_location_id);

        view.getLayoutParams().height = viewHeight;
        view = findViewById(R.id.luminaire_power_id);

        view.getLayoutParams().height = viewHeight;
        view = findViewById(R.id.timer_id);

        view.getLayoutParams().height = viewHeight;
        view = findViewById(R.id.lamp_on_off_id_light_control);


        view.getLayoutParams().height = viewHeight;

        viewHeight = (int) (screenHeight * 28) / 100;


        view = findViewById(R.id.control_box_id);

        view.getLayoutParams().height = viewHeight;

        Timer_on_off_text = (TextView) findViewById(R.id.light_control_timer_on_off_id);


        Timer_on_off_text.setText(ON_OFF_State);


        Timervalue_text = (TextView) findViewById(R.id.light_control_timer_value_id);
        if (Hourvalue != "" && Minvalue != "")
            Timervalue_text.setText(Hourvalue + ":" + Minvalue);


        Power_value_text = (TextView) findViewById(R.id.luminaire_power_text_id);
        if (Wattvalue != "")
            Power_value_text.setText(Wattvalue + "W");

        Map_location_text = (TextView) findViewById(R.id.geolocation_text_id);
        if (bluetoothComponent.Maplocationlat != "" && bluetoothComponent.Maplocationlong != "")
            Map_location_text.setText(bluetoothComponent.Maplocationlong + "," + bluetoothComponent.Maplocationlat);


        //picker started for light control

        final String[] perPicker = new String[]{"0%", "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};

        final Toast myToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


        perpicker_lightcontrol = (NumberPicker) findViewById(R.id.per_picker_light_control);

        perpicker_lightcontrol.setMinValue(0);
        //set max value from length array string reduced 1
        perpicker_lightcontrol.setMaxValue(perPicker.length - 1);
        //implement array string to number picker
        perpicker_lightcontrol.setDisplayedValues(perPicker);
        //disable soft keyboard
        perpicker_lightcontrol.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //set wrap true or false, try it you will know the difference
        perpicker_lightcontrol.setWrapSelectorWheel(false);

        int count = perpicker_lightcontrol.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = perpicker_lightcontrol.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = perpicker_lightcontrol.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(perpicker_lightcontrol)).setColor(Color.WHITE);
                    ((EditText) child).setTextColor(Color.WHITE);
                    perpicker_lightcontrol.invalidate();

                } catch (Exception e) {
                    //Log.w("setNumberPickerTextColor", e);
                }

            }
        }


        perpicker_lightcontrol.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                // textupdate_per.setText("Light after switched ON will be dimmed "+perPicker[perpicker.getValue()]);

                if (ON_OFF_State == "ON") {
                    int brightind;

                    brightind = perpicker_lightcontrol.getValue() * 10;

                    final String newmessage;

                    if (brightind == 0)
                        newmessage = "bright 00" + Integer.toString(brightind) + ":";
                    else if (brightind != 100)
                        newmessage = "bright 0" + Integer.toString(brightind) + ":";
                    else
                        newmessage = "bright " + Integer.toString(brightind) + ":";

                    Brightvalue = newmessage;

                    myToast.setText("Brightness is set to " + brightind + "%");
                    myToast.show();
                    // Toast.makeText(light_control.this,"Brightness is set to "+brightind+"%",Toast.LENGTH_SHORT).show();
                } else {
                    myToast.setText("Please Set Timer");
                    myToast.show();
                    // Toast.makeText(light_control.this,"Please Set Timer",Toast.LENGTH_SHORT).show();
                }

            }
        });


        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuildernew = new AlertDialog.Builder(this);
        alertDialogBuilder.setNeutralButton(
                "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                      //  Toast.makeText(light_control.this,"You clicked OK Button",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        timervalue = new Timer();

    }

    /*
    public void refreshUI(final String timevalue,final String onoff){
        System.out.println("value is "+timevalue+" "+onoff);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                System.out.println("value is getting "+Timer_on_off_text.getText());
                System.out.println("value is getting "+Timervalue_text.getText());

        Timer_on_off_text.setText(onoff);
        Timervalue_text.setText(timevalue);
                System.out.println("value is getting "+Timer_on_off_text.getText());
                System.out.println("value is getting "+Timervalue_text.getText());
            }
        });

    }
    */
    public void onBacklight_control(View view) {



        final TextView viewToAnimate = (TextView) findViewById(R.id.light_control_back_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);


        Intent intent = new Intent(light_control.this,lamps.class);
        startActivity(intent);
    }

    public void gotoTimer(View view) {


        final ImageView viewToAnimate = (ImageView) findViewById(R.id.forward_timer_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);


        devicenamestring = Devicename.getText().toString();

        timer.light_control_activity = light_control.this;
        Intent intent = new Intent(light_control.this, timer.class);
        startActivity(intent);
    }

    public void gotoWatt(View view) {


        final ImageView viewToAnimate = (ImageView) findViewById(R.id.forward_power_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        devicenamestring = Devicename.getText().toString();
        timer.light_control_activity = light_control.this;
        Intent intent = new Intent(light_control.this, watt_setting.class);
        startActivity(intent);
    }



    public void onoff_fun(View view) {

        if (bluetoothComponent.gatt_connection_check == null){

            alertDialogBuildernew.setMessage("Please check connection");
            alertDialog = alertDialogBuildernew.create();
            alertDialog.show();
            timervalue.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                   // timervalue.cancel();
                }
            },1500);
           // Toast.makeText(this, "Please check connection", Toast.LENGTH_SHORT).show();
        }
        else {
            System.out.println("I am in on off entryentry ");
            textmsg = (TextView) findViewById(R.id.onoff_id);

            final String message;

            System.out.println("input message is " + textmsg.getText().toString());

            String check = "LUMINAIRE ON";

            if (check.equals(textmsg.getText().toString())) {
                System.out.println("input message c Enter in lamp on ");
                message = "read sloff:";
                bluetoothComponent.light_status = "LAMP OFF";
                textmsg.setText("LUMINAIRE OFF");


                buttonbackground.setBackgroundResource(R.drawable.lamp_off_button);
                onbutton.setVisibility(View.VISIBLE);
                offbutton.setVisibility(View.INVISIBLE);


            } else {
                System.out.println("input message  c Enter in lamp off");
                message = "read slonn:";
                textmsg.setText("LUMINAIRE ON");
                bluetoothComponent.light_status = "LUMINAIRE ON";


                buttonbackground.setBackgroundResource(R.drawable.lamp_on_button);
                onbutton.setVisibility(View.INVISIBLE);
                offbutton.setVisibility(View.VISIBLE);

               // Toast.makeText(this, "Light will be turned on after 15 secs", Toast.LENGTH_SHORT).show();

                alertDialogBuildernew.setMessage("Light will be turned on after 15 secs");
                alertDialog = alertDialogBuildernew.create();
                alertDialog.show();
                timervalue.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                        //    timer.cancel();
                    }
                },1500);

            }


            runOnUiThread(new Runnable() {

                @Override
                public void run() {


                    if (bluetoothComponent.gatt == null)
                        System.out.println("gatt null");
                    else
                        System.out.println("gatt not null");

                    System.out.println("gatt device  is  light" + bluetoothComponent.device);

                    bluetoothComponent.tx = bluetoothComponent.gatt.getService(bluetoothComponent.UART_UUID).getCharacteristic(bluetoothComponent.TX_UUID);

                    if (bluetoothComponent.tx != null) {
                        bluetoothComponent.tx.setValue(message);
                        System.out.println("tx is not null");
                        bluetoothComponent.write();
                        //  bluetoothComponent.gatt.writeCharacteristic(bluetoothComponent.tx);

                    } else
                        System.out.println("tx is null");


                }
            });
        }


    }


    public void Savelightcontrol(View view) {


        final TextView viewToAnimate = (TextView) findViewById(R.id.save_light_control_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);


        if (bluetoothComponent.gatt_connection_check == null) {

            alertDialogBuildernew.setMessage("Please check connection");
            alertDialog = alertDialogBuildernew.create();
            alertDialog.show();
            timervalue.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                 //   timervalue.cancel();
                }
            },1500);
        //    Toast.makeText(this, "Please check connection", Toast.LENGTH_SHORT).show();

        } else {
            lampname = Devicename.getText().toString().trim();

            System.out.println("came inside lampname "+lampname);

            if (lampname.equals("") || Wattvalue == "" || (bluetoothComponent.Maplocationlat == "" && bluetoothComponent.Maplocationlong == "")) {

                System.out.println("came inside");


                String lampcheck = "", wattcheck = "", locationcheck = "";
                if (lampname.equals("")){
                    if(Wattvalue==""||(bluetoothComponent.Maplocationlat == "" && bluetoothComponent.Maplocationlong == ""))
                    lampcheck = " Lamp Name,";
                else
                        lampcheck = " Lamp Name";
                }
                if (Wattvalue == ""){
                    if(bluetoothComponent.Maplocationlat == "" && bluetoothComponent.Maplocationlong == "")
                    wattcheck = " Watt Value,";
                else
                        wattcheck = " Watt Value";
                }
                if (bluetoothComponent.Maplocationlat == "" && bluetoothComponent.Maplocationlong == "")
                    locationcheck = " Location";

                alertDialogBuilder.setMessage("Please Enter" + lampcheck + wattcheck + locationcheck);
                 alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else{

            lampname = Devicename.getText().toString().trim();
            final String messagedevicename = "";
            final String messagelocationname = "";
            final String messagetime;
            final String messagebright = Brightvalue;
            final String messagewatt;

                String messagetimetemp ="";


            messagewatt = "setwat " + Wattvalue + ":";

            if (Hourvalue.length() == 1)
                messagetimetemp =  "dimdurn 0"+Hourvalue+"h";
            else
                messagetimetemp = "dimdurn "+Hourvalue+"h";

                if(Minvalue.length()==1)
                    messagetimetemp=messagetimetemp+"0"+Minvalue+":";
                else
                    messagetimetemp=messagetimetemp+Minvalue+":";

                messagetime=messagetimetemp;

                System.out.println("time is "+messagetime);


            if (ON_OFF_State == "ON") {


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        if (bluetoothComponent.gatt == null)
                            System.out.println("gatt null");
                        else
                            System.out.println("gatt not null");

                        System.out.println("gatt device  is  light" + bluetoothComponent.device);

                        bluetoothComponent.tx = bluetoothComponent.gatt.getService(bluetoothComponent.UART_UUID).getCharacteristic(bluetoothComponent.TX_UUID);

                        if (bluetoothComponent.tx != null) {
                            bluetoothComponent.tx.setValue(messagebright);
                            System.out.println("tx is not null" + messagebright);
                            bluetoothComponent.write();
                            //  bluetoothComponent.gatt.writeCharacteristic(bluetoothComponent.tx);

                        } else
                            System.out.println("tx is null");

                    }
                });

                final Handler tempHandler = new Handler();


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        if (bluetoothComponent.gatt == null)
                            System.out.println("gatt null");
                        else
                            System.out.println("gatt not null");

                        System.out.println("gatt device  is  light" + bluetoothComponent.device);

                        tempHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bluetoothComponent.tx = bluetoothComponent.gatt.getService(bluetoothComponent.UART_UUID).getCharacteristic(bluetoothComponent.TX_UUID);

                                if (bluetoothComponent.tx != null) {
                                    bluetoothComponent.tx.setValue(messagetime);
                                    System.out.println("tx is not null" + messagetime);
                                    bluetoothComponent.write();
                                    //  bluetoothComponent.gatt.writeCharacteristic(bluetoothComponent.tx);

                                } else
                                    System.out.println("tx is null");
                            }
                        }, 2000);

                    }
                });
            }

            final Handler tempHandler1 = new Handler();
            if (Wattvalue != "") {


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        if (bluetoothComponent.gatt == null)
                            System.out.println("gatt null");
                        else
                            System.out.println("gatt not null");

                        System.out.println("gatt device  is  light" + bluetoothComponent.device);

                        tempHandler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bluetoothComponent.tx = bluetoothComponent.gatt.getService(bluetoothComponent.UART_UUID).getCharacteristic(bluetoothComponent.TX_UUID);

                                if (bluetoothComponent.tx != null) {
                                    bluetoothComponent.tx.setValue(messagewatt);
                                    System.out.println("tx is not null" + messagewatt);
                                    bluetoothComponent.write();
                                    //  bluetoothComponent.gatt.writeCharacteristic(bluetoothComponent.tx);

                                } else
                                    System.out.println("tx is null");
                            }
                        }, 4000);

                    }
                });
//System.out.println("messagehere is "+messagetime+" "+messagebright+" "+messagewatt+" "+lampname);

            }


/*

     if(lampname!=""){
         runOnUiThread(new Runnable() {

             @Override
             public void run() {


                 bluetoothComponent.tx = bluetoothComponent.gatt.getService(bluetoothComponent.UART_UUID).getCharacteristic(bluetoothComponent.TX_UUID);

                 if (bluetoothComponent.tx != null) {
                     bluetoothComponent.tx.setValue(messagedevicename);

                     bluetoothComponent.write();


                 } else
                     System.out.println("tx is null");


             }
         });
     }


     if(bluetoothComponent.Maplocationlong!=""&&bluetoothComponent.Maplocationlat!=""){
         runOnUiThread(new Runnable() {

             @Override
             public void run() {


                 bluetoothComponent.tx = bluetoothComponent.gatt.getService(bluetoothComponent.UART_UUID).getCharacteristic(bluetoothComponent.TX_UUID);

                 if (bluetoothComponent.tx != null) {
                     bluetoothComponent.tx.setValue(messagelocationname;

                     bluetoothComponent.write();


                 } else
                     System.out.println("tx is null");


             }
         });
     }


*/


            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            //   editor.putString("Lampname_key", lampname);
            editor.putString("Hourvalue_key", Hourvalue);
            editor.putString("Minvalue_key", Minvalue);
            editor.putString("Wattvalue_key", Wattvalue);
            editor.putString("Longitudevalue_key", bluetoothComponent.Maplocationlong);
            editor.putString("Latitudevalue_key", bluetoothComponent.Maplocationlat);
            editor.putString("Brightalue_key", Brightvalue);

            editor.commit();


      //      Toast.makeText(this, "Configuration Saved", Toast.LENGTH_SHORT).show();

                alertDialogBuilder.setMessage("Configuration Saved");

                alertDialogBuilder.setNeutralButton(
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //  Toast.makeText(light_control.this,"You clicked OK Button",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(light_control.this, loggedIn.class);
                                startActivity(intent);
                            }
                        }
                );

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();




        }

        }


    }

    public void MapLocation(View view) {


        if (!isNetworkAvailable()) {
            devicenamestring = Devicename.getText().toString();
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();


        } else {
            System.out.println("Inside map location");
            devicenamestring = Devicename.getText().toString();
            Intent intent = new Intent(light_control.this, MapsActivity.class);
            startActivity(intent);
        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void Load_default(View view) {


        final TextView viewToAnimate = (TextView) findViewById(R.id.load_default_light_control_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);


        //   lampname= sharedPref.getString("Lampname_key", "") ;
        Hourvalue = sharedPref.getString("Hourvalue_key", "");
        Minvalue = sharedPref.getString("Minvalue_key", "");
        Wattvalue = sharedPref.getString("Wattvalue_key", "");
        bluetoothComponent.Maplocationlong = sharedPref.getString("Longitudevalue_key", "");
        bluetoothComponent.Maplocationlat = sharedPref.getString("Latitudevalue_key", "");
        Brightvalue = sharedPref.getString("Brightalue_key", "");


        if (Hourvalue != "") {
            ON_OFF_State = "ON";
        } else {
            ON_OFF_State = "OFF";
        }
        if (Brightvalue != "") {
            String tempbrightmessage = Brightvalue;
            tempbrightmessage = tempbrightmessage.replace("bright ", "");
            tempbrightmessage = tempbrightmessage.replace(":", "");
           // Toast.makeText(this, "Brightness is set to " + tempbrightmessage + "%", Toast.LENGTH_SHORT).show();

            alertDialogBuildernew.setMessage("Brightness is set to " + tempbrightmessage + "%");
            alertDialog = alertDialogBuildernew.create();
            alertDialog.show();
            timervalue.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    //    timer.cancel();
                }
            },1500);
        }

        refreshUI_load();


        // System.out.println("hour and min "+Hourvalue+" "+Minvalue);

    }

    public void refreshUI_load() {
        Timervalue_text = (TextView) findViewById(R.id.light_control_timer_value_id);
        if (Hourvalue != "" && Minvalue != "")
            Timervalue_text.setText(Hourvalue + ":" + Minvalue);
        else {
            Timervalue_text.setText("");
        }


        Power_value_text = (TextView) findViewById(R.id.luminaire_power_text_id);
        if (Wattvalue != "")
            Power_value_text.setText(Wattvalue + "W");
        else {
            Power_value_text.setText("");
        }

        Map_location_text = (TextView) findViewById(R.id.geolocation_text_id);
        if (bluetoothComponent.Maplocationlat != "" && bluetoothComponent.Maplocationlong != "")
            Map_location_text.setText(bluetoothComponent.Maplocationlong + "," + bluetoothComponent.Maplocationlat);
        else {
            Map_location_text.setText("");
        }

        //   Devicename.setText(lampname);

        Timer_on_off_text = (TextView) findViewById(R.id.light_control_timer_on_off_id);


        Timer_on_off_text.setText(ON_OFF_State);


    }



}


