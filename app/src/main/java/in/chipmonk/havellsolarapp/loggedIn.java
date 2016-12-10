package in.chipmonk.havellsolarapp;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.graphics.drawable.ColorDrawable;
import android.util.*;
import android.widget.*;
import in.chipmonk.havellsolarapp.bluetoothComponent;
import org.w3c.dom.Text;
import android.view.animation.*;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class loggedIn extends AppCompatActivity{



    public TextView battery_status_voltage ;
    public TextView battery_status_current;
    public TextView panel_status_voltage ;
    public TextView panel_status_current;
    public TextView load_status_voltage ;
    public TextView loggedin_on_off;
    public TextView load_status_current;
    public Handler Handlerrefresh = null;
    public  AlertDialog.Builder alertDialogBuilder ;
    public Timer timer;
    public AlertDialog alertDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.logged_in);




        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
View view = findViewById(R.id.battery_voltage_id);
        int viewHeight=(int)(screenHeight*30)/100;
        int viewWidth=(int)(screenWidth*44)/100;
        view.getLayoutParams().width=viewWidth;
        view.getLayoutParams().height=viewHeight;
        view=findViewById(R.id.solar_panel_voltage_id);
        view.getLayoutParams().width=viewWidth;
        view.getLayoutParams().height=viewHeight;
        view=findViewById(R.id.lamp_on_off_id);
        view.getLayoutParams().width=viewWidth;
        view.getLayoutParams().height=viewHeight;

        battery_status_voltage = (TextView)findViewById(R.id.battery_status_voltage_id);
        panel_status_voltage = (TextView)findViewById(R.id.panel_status_voltage_id);
        load_status_voltage = (TextView)findViewById(R.id.lamp_control_voltage_id);
        battery_status_current = (TextView)findViewById(R.id.battery_status_current_id);
        panel_status_current = (TextView)findViewById(R.id.panel_status_current_id);
        load_status_current = (TextView)findViewById(R.id.lamp_control_current_id);
        loggedin_on_off = (TextView)findViewById(R.id.loggedin_on_off_id);

        battery_status_voltage.setText("Voltage = "+bluetoothComponent.Battery_status_voltage+"V");
        System.out.println("UI REFRESH is "+bluetoothComponent.Battery_status_voltage);
        panel_status_voltage.setText("Voltage = "+bluetoothComponent.Solar_panel_staus_voltage+"V");
        load_status_voltage.setText("Voltage = "+bluetoothComponent.load_status_voltage+"V");
        battery_status_current.setText("Current = "+bluetoothComponent.Battery_status_current+"A");
        panel_status_current.setText("Current = "+bluetoothComponent.Solar_panel_staus_current+"A");
        load_status_current.setText("Current = "+bluetoothComponent.load_status_current+"A");

        if(bluetoothComponent.light_status=="LUMINAIRE ON")
        loggedin_on_off.setText("ON");
        else
            loggedin_on_off.setText("OFF");

Handlerrefresh = new Handler();

/*
        if(bluetoothComponent.refreshcheck==false){
            TextView dataloadingloggedinoutside = (TextView)findViewById(R.id.data_loading_loggedin_id) ;
            dataloadingloggedinoutside.setText("Data Loading...");
        }
        else{
            TextView dataloadingloggedinoutside = (TextView)findViewById(R.id.data_loading_loggedin_id) ;
            dataloadingloggedinoutside.setText("");
        }
*/

        // refreshUI();



        alertDialogBuilder = new AlertDialog.Builder(this);
            timer = new Timer();
    }

    @Override
    protected void onResume() {

        super.onResume();
        /*
        if(bluetoothComponent.refreshcheck==false){
            TextView dataloadingloggedinoutside = (TextView)findViewById(R.id.data_loading_loggedin_id) ;
            dataloadingloggedinoutside.setText("Data Loading...");
        }
        else{
            TextView dataloadingloggedinoutside = (TextView)findViewById(R.id.data_loading_loggedin_id) ;
            dataloadingloggedinoutside.setText("");
        }*/
    }

    public void refreshUI()
    {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                battery_status_voltage.setText(" ");
                panel_status_voltage.setText(" ");
                load_status_voltage.setText(" ");
                battery_status_current.setText(" ");
                panel_status_current.setText(" ");
                load_status_current.setText(" ");

                battery_status_voltage.setText("Voltage = "+bluetoothComponent.Battery_status_voltage+"V");
                System.out.println("UI REFRESH is "+bluetoothComponent.Battery_status_voltage);
                panel_status_voltage.setText("Voltage = "+bluetoothComponent.Solar_panel_staus_voltage+"V");
                load_status_voltage.setText("Voltage = "+bluetoothComponent.load_status_voltage+"V");
                battery_status_current.setText("Current = "+bluetoothComponent.Battery_status_current+"A");
                panel_status_current.setText("Current = "+bluetoothComponent.Solar_panel_staus_current+"A");
                load_status_current.setText("Current = "+bluetoothComponent.load_status_current+"A");
            }
        });

    }

    public void onBackEnviro(View view){



        Intent intent = new Intent(loggedIn.this,in.chipmonk.havellsolarapp.MainActivity.class);
        startActivity(intent);
    }

    public void gotobatterystatus(View view){




        final ImageView viewToAnimate = (ImageView)findViewById(R.id.forward_arrow_battery_status_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);




        if(bluetoothComponent.gatt_connection_check==null){

            alertDialogBuilder.setMessage("Please check connection");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                //    timer.cancel();
                }
            },1500);
           // Toast.makeText(this,"Please check connection",Toast.LENGTH_SHORT).show();
        }
        else {
            if(bluetoothComponent.refreshcheck){

            bluetoothComponent.currentActivity = null;
        Intent intent = new Intent(loggedIn.this,in.chipmonk.havellsolarapp.battery_status.class);
        startActivity(intent);}
            else{

                alertDialogBuilder.setMessage("Please wait Data Loading...");
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    //    timer.cancel();
                    }
                },1500);
             //   Toast.makeText(this,"Please wait Data Loading...",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void gotosolarpanellivevoltage(View view){

        final ImageView viewToAnimate = (ImageView)findViewById(R.id.forward_arrow_solar_panel_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);


        if(bluetoothComponent.gatt_connection_check==null) {
          //  Toast.makeText(this, "Please check connection", Toast.LENGTH_SHORT).show();
            alertDialogBuilder.setMessage("Please check connection");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                   // timer.cancel();
                }
            },1500);
        }
        else {

            if(bluetoothComponent.refreshcheck){

        bluetoothComponent.currentActivity = null;




        Intent intent = new Intent(loggedIn.this,in.chipmonk.havellsolarapp.solar_panel_live_voltage.class);
        startActivity(intent);}
            else{
                alertDialogBuilder.setMessage("Please wait Data Loading...");
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                      //  timer.cancel();
                    }
                },1500);

              //  Toast.makeText(this,"Please wait Data Loading...",Toast.LENGTH_SHORT).show();
            }

        }

    }
    public void gotolamp(View view){

        final ImageView viewToAnimate = (ImageView)findViewById(R.id.forward_arrow_lamp_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);
        bluetoothComponent.currentActivity = null;

        if(bluetoothComponent.gatt_connection_check==null){
            alertDialogBuilder.setMessage("Please check connection");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                 //   timer.cancel();
                }
            },1500);

        }
        //    Toast.makeText(this,"Please check connection",Toast.LENGTH_SHORT).show();}
        else {


            if(bluetoothComponent.refreshcheck){


        final String message;


        if(bluetoothComponent.light_status=="LUMINAIRE ON"){
        message="read sloff:";
            bluetoothComponent.light_status="LUMINAIRE OFF";
            loggedin_on_off.setText("OFF");
        }
        else {
            message = "read slonn:";
            bluetoothComponent.light_status = "LUMINAIRE ON";
            loggedin_on_off.setText("ON");

            alertDialogBuilder.setMessage("Light will be turned on after 15 secs");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    //    timer.cancel();
                }
            },1500);

          //  Toast.makeText(this,"Light will be turned on after 15 secs",Toast.LENGTH_SHORT).show();
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
            else{
                alertDialogBuilder.setMessage("Please wait Data Loading...");
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    //    timer.cancel();
                    }
                },1500);

             //   Toast.makeText(this,"Please wait Data Loading...",Toast.LENGTH_SHORT).show();
            }

        }
//  Intent intent = new Intent(loggedIn.this,light_control.class);
//        startActivity(intent);




    }
    public void refreshscan_loggedin(View view){
        final ImageView viewToAnimate = (ImageView)findViewById(R.id.refreshbutton_logged_in_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        if(bluetoothComponent.gatt_connection_check==null){
            alertDialogBuilder.setMessage("Please check connection");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                 //   timer.cancel();
                }
            },1500);
         //   Toast.makeText(this,"Please check connection",Toast.LENGTH_SHORT).show();
        }
        else {
        if(bluetoothComponent.refreshcheck){
            bluetoothComponent.refreshcheck=false;

            TextView dataloadingloggedinoutside = (TextView)findViewById(R.id.data_loading_loggedin_id) ;
            dataloadingloggedinoutside.setText("Data Loading...");

            Handlerrefresh.postDelayed(new Runnable() {
                @Override
                public void run() {

                    TextView dataloadingloggedininside = (TextView)findViewById(R.id.data_loading_loggedin_id) ;
                    dataloadingloggedininside.setText("");

                    bluetoothComponent.refreshcheck=true;
                }
            },5000);

        bluetoothComponent.currentActivity = loggedIn.this;

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

        }
        else{

            alertDialogBuilder.setMessage("Please wait Data Loading...");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                  //  timer.cancel();
                }
            },1500);
            //Toast.makeText(this,"Please wait Data Loading...",Toast.LENGTH_SHORT).show();
        }}
       // refreshUI();
//        finish();
//        startActivity(getIntent());
    }

    public void Disconnect(View view){



        final TextView viewToAnimate = (TextView)findViewById(R.id.disconnect_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

            if(bluetoothComponent.gatt!=null)
        bluetoothComponent.gatt.disconnect();
      //  bluetoothComponent.gatt_connection_check.disconnect();
        Intent intent = new Intent(loggedIn.this,lamps.class);
        startActivity(intent);
    }
}



