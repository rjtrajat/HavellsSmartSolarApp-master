package in.chipmonk.havellsolarapp;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import  android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import  java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rajat on 27/07/16.
 */
public class battery_status extends AppCompatActivity {


    public TextView battery_status_voltage_inside ;
    public TextView battery_status_current_inside;
    public TextView battery_per_status;
    public TextView battery_charging_dishcharging ;
    public TextView battery_capacity_text;
    public RelativeLayout background_charging;
    public double value ;
    public double finalans;
    public DecimalFormat df;
    public Handler Handlerrefresh = null;

    public  AlertDialog.Builder alertDialogBuilder ;
    public Timer timer;
    public AlertDialog alertDialog;

protected  void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.battery_status);
//    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//    getSupportActionBar().setCustomView(R.layout.actionbarbattery_status);

    DisplayMetrics displaymetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

    int screenHeight = displaymetrics.heightPixels;
    int screenWidth = displaymetrics.widthPixels;
    View view = findViewById(R.id.battery_voltage_id);
   int viewHeight=(int)(screenHeight*17)/100;
    int viewWidth=(int)(screenWidth*90)/100;
    view.getLayoutParams().width=viewWidth;
    view.getLayoutParams().height=viewHeight;
    view=findViewById(R.id.battery_charging_id);
    view.getLayoutParams().width=viewWidth;
    view.getLayoutParams().height=viewHeight;
    view=findViewById(R.id.battery_status_id);
    view.getLayoutParams().width=viewWidth;
    view.getLayoutParams().height=viewHeight;
    view=findViewById(R.id.battery_capacity_id);
    view.getLayoutParams().width=viewWidth;
    view.getLayoutParams().height=viewHeight;



    background_charging = (RelativeLayout)findViewById(R.id.battery_charging_id);

   battery_status_voltage_inside = (TextView)findViewById(R.id.battery_status_voltage_inside_id);
    battery_status_voltage_inside.setText("Voltage = "+bluetoothComponent.Battery_status_voltage+"V");

    battery_status_current_inside = (TextView)findViewById(R.id.battery_status_current_inside_id);
    battery_status_current_inside.setText("Current = "+bluetoothComponent.Battery_status_current+"A");


    battery_per_status=(TextView)findViewById(R.id.battery_status_per_id);

    battery_charging_dishcharging=(TextView)findViewById(R.id.charging_discharging_id);



     value = Float.parseFloat(bluetoothComponent.Battery_status_voltage.trim());
    finalans = (value *10.0 - 110.0 )*2.5;

    df = new DecimalFormat("#.##");

    battery_per_status.setText(df.format(finalans)+"%");
    System.out.println("final ans is "+df.format(finalans));

    if(bluetoothComponent.Solar_panel_staus_current.contains("0.0")){
battery_charging_dishcharging.setText("DISCHARGING");
        background_charging.setBackgroundResource(R.drawable.solar_discharging);
    }
    else{
        battery_charging_dishcharging.setText("CHARGING");
        background_charging.setBackgroundResource(R.drawable.solar_charging);
    }


    battery_capacity_text = (TextView)findViewById(R.id.Battery_capacity_text_id);
    battery_capacity_text.setText(bluetoothComponent.Battery_capacity);

    /*

    if(bluetoothComponent.refreshcheck==false){
        TextView dataloadingbatterystatusoutside = (TextView)findViewById(R.id.data_loading_battery_status_id) ;
        dataloadingbatterystatusoutside.setText("Data Loading...");
    }
    else{
        TextView dataloadingbatterystatusoutside = (TextView)findViewById(R.id.data_loading_battery_status_id) ;
        dataloadingbatterystatusoutside.setText("");
    }

*/
Handlerrefresh = new Handler();

    alertDialogBuilder = new AlertDialog.Builder(this);
    timer = new Timer();


}

    @Override
    protected void onResume() {
        super.onResume();

        /*
        if(bluetoothComponent.refreshcheck==false){
            TextView dataloadingbatterystatusoutside = (TextView)findViewById(R.id.data_loading_battery_status_id) ;
            dataloadingbatterystatusoutside.setText("Data Loading...");
        }
        else{
            TextView dataloadingbatterystatusoutside = (TextView)findViewById(R.id.data_loading_battery_status_id) ;
            dataloadingbatterystatusoutside.setText("");
        }
        */
    }

    public void onBackBattery_status(View view){
        final TextView viewToAnimate = (TextView) findViewById(R.id.back_arrow_battery_status_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        if(bluetoothComponent.refreshcheck) {




            Intent intent = new Intent(battery_status.this, loggedIn.class);
            startActivity(intent);
        }
        else{
            alertDialogBuilder.setMessage("Please wait Data Loading...");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                   // timer.cancel();
                }
            },1500);

          //  Toast.makeText(this,"Please wait Data Loading...",Toast.LENGTH_SHORT).show();
        }

    }

    public void refreshUI()
    {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                battery_status_voltage_inside = (TextView)findViewById(R.id.battery_status_voltage_inside_id);
                battery_status_voltage_inside.setText("Voltage = "+bluetoothComponent.Battery_status_voltage+"V");

                battery_status_current_inside = (TextView)findViewById(R.id.battery_status_current_inside_id);
                battery_status_current_inside.setText("Current = "+bluetoothComponent.Battery_status_current+"A");


                battery_per_status=(TextView)findViewById(R.id.battery_status_per_id);

                battery_charging_dishcharging=(TextView)findViewById(R.id.charging_discharging_id);

                value = Float.parseFloat(bluetoothComponent.Battery_status_voltage.trim());
                finalans = (value *10.0 - 110.0 )*2.5;
                 df = new DecimalFormat("#.##");
                df.format(finalans);
                battery_per_status.setText(df.format(finalans)+"%");
                System.out.println("final ans is "+df.format(finalans));

                if(bluetoothComponent.Solar_panel_staus_current.contains("0.0")){
                    battery_charging_dishcharging.setText("DISCHARGING");

                    background_charging.setBackgroundResource(R.drawable.solar_discharging);
                }
                else{
                    battery_charging_dishcharging.setText("CHARGING");
                    background_charging.setBackgroundResource(R.drawable.solar_charging);
                }

                battery_capacity_text.setText(bluetoothComponent.Battery_capacity);

            }
        });

    }


    public void refreshscan_battery_status(View view){
        final ImageView viewToAnimate = (ImageView) findViewById(R.id.refreshbutton_battery_status_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        if(bluetoothComponent.gatt_connection_check==null) {
            alertDialogBuilder.setMessage("Please check connection");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                  //  timer.cancel();
                }
            },1500);

      //      Toast.makeText(this, "Please check connection", Toast.LENGTH_SHORT).show();

        }
        else {
        if(bluetoothComponent.refreshcheck){
            bluetoothComponent.refreshcheck=false;



            TextView dataloadingbatterystatusoutside = (TextView)findViewById(R.id.data_loading_battery_status_id) ;
            dataloadingbatterystatusoutside.setText("Data Loading...");

            Handlerrefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothComponent.refreshcheck=true;
                    TextView dataloadingbatterystatusinside = (TextView)findViewById(R.id.data_loading_battery_status_id) ;
                    dataloadingbatterystatusinside.setText("");
                }
            },5000);



        bluetoothComponent.currentActivity = battery_status.this;
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
                 //   timer.cancel();
                }
            },1500);

            //Toast.makeText(this,"Please wait Data Loading...",Toast.LENGTH_SHORT).show();
        }}
//        finish();
//        startActivity(getIntent());
    }
}
