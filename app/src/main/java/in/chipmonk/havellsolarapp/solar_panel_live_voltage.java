package in.chipmonk.havellsolarapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.View;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.*;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.nearby.messages.internal.HandleClientLifecycleEventRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import in.chipmonk.havellsolarapp.bluetoothComponent;

/**
 * Created by Rajat on 27/07/16.
 */
public class solar_panel_live_voltage  extends AppCompatActivity{


    public TextView panel_status_voltage_inside ;
    public TextView DateTime;
    public TextView panel_status_current_inside;
    public Handler Handlerrefresh = null;
    public  DateFormat df;
    public String date;
    public GraphView graph;
    static public Handler Handletime;
    public Handler handler;
    public  AlertDialog.Builder alertDialogBuilder ;
    public Timer timer;
    public AlertDialog alertDialog;
    private LineGraphSeries<DataPoint> seriessolar;
    private LineGraphSeries<DataPoint> serieslamp;
    private int lastX = 0;

    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.solar_panel_live_voltage);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbarsolar_panel_live_voltage);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
        View view = findViewById(R.id.solar_power_circle_id);
        int viewHeight=(int)(screenHeight*43)/100;
        int viewWidth=(int)(screenWidth*80)/100;
        view.getLayoutParams().width=viewWidth;
        view.getLayoutParams().height=viewHeight;



                panel_status_voltage_inside = (TextView)findViewById(R.id.solar_panel_voltage_inside_id);
        panel_status_voltage_inside.setText("Voltage = "+bluetoothComponent.Solar_panel_staus_voltage+"V");

        panel_status_current_inside = (TextView)findViewById(R.id.solar_panel_current_inside_id);
        panel_status_current_inside.setText("Current = "+bluetoothComponent.Solar_panel_staus_current+"A");




        Handletime = new Handler();


        DateTime = (TextView)findViewById(R.id.date_time_id);
         df = new SimpleDateFormat("MMM d, yyyy, HH:mm:ss");
         date = df.format(Calendar.getInstance().getTime());

        DateTime.setText(date);

        //System.out.println("Time is "+DateTime.getText());

       Datetime();

        Handlerrefresh = new Handler();

        handler = new Handler();

        //Graph view instance
         graph  = (GraphView)findViewById(R.id.graph);
         viewHeight=(int)(screenHeight*30)/100;
        graph.getLayoutParams().height=viewHeight;


        //data
//seriessolar  = new LineGraphSeries<DataPoint>();
//        serieslamp = new LineGraphSeries<DataPoint>();
//        graph.addSeries(seriessolar);
//        graph.addSeries(serieslamp);
//        //customize
//        Viewport viewport  = graph.getViewport();
//        viewport.setYAxisBoundsManual(true);
//        viewport.setXAxisBoundsManual(true);
//
//        viewport.setMinY(0);
//        viewport.setMaxY(250);
//        viewport.setMinX(0);
//        viewport.setMaxX(3);
//        viewport.setScrollable(true );
//serieslamp.setColor(Color.WHITE);
//        serieslamp.setDrawDataPoints(true);
//        serieslamp.setDataPointsRadius(10);
//        seriessolar.setDrawDataPoints(true);
//        seriessolar.setDataPointsRadius(10);
        //serieslamp.setDrawBackground(true);
        //seriessolar.setDrawBackground(true);
        //serieslamp.setBackgroundColor(Color.parseColor("#A61c89b0"));
       // seriessolar.setBackgroundColor(Color.parseColor("#A61c89b0"));

/*

        if(bluetoothComponent.refreshcheck==false){
            TextView dataloadingsolaroutside = (TextView)findViewById(R.id.data_loading_solar_id) ;
            dataloadingsolaroutside.setText("Data Loading...");
        }
        else{
            TextView dataloadingsolaroutside = (TextView)findViewById(R.id.data_loading_solar_id) ;
            dataloadingsolaroutside.setText("");
        }

*/
        alertDialogBuilder = new AlertDialog.Builder(this);
        timer = new Timer();

callreadgraph();

    }


    public void callreadgraph(){

        if(bluetoothComponent.gatt_connection_check==null) {

            alertDialogBuilder.setMessage("Please check connection");
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
              //      timer.cancel();
                }
            },1500);
         //   Toast.makeText(this, "Please check connection", Toast.LENGTH_SHORT).show();

        }
        else{


            if(bluetoothComponent.graphcanload&&bluetoothComponent.refreshcheck){
                bluetoothComponent.graphcanload=false;
                bluetoothComponent.refreshcheck=false;

                TextView dataloadingsolaroutside = (TextView)findViewById(R.id.data_loading_solar_id) ;
                dataloadingsolaroutside.setText("Data Loading...");


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothComponent.graphcanload=true;
                        bluetoothComponent.refreshcheck=true;
                        TextView dataloadingsolarinside = (TextView)findViewById(R.id.data_loading_solar_id) ;
                        dataloadingsolarinside.setText("");



                    }
                },4000);


        bluetoothComponent.currentActivity = solar_panel_live_voltage.this;

        final String message;


        message = "read graph:";






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
                    //    timer.cancel();
                    }
                },1500);
               // Toast.makeText(this,"Please wait Data Loading...",Toast.LENGTH_SHORT).show();
            }
        }



    }


    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end

//        bluetoothComponent.sday1="";
//        bluetoothComponent.sday2="";
//        bluetoothComponent.sday3="";
//        bluetoothComponent.lday1="";
//        bluetoothComponent.lday2="";
//        bluetoothComponent.lday3="";

        seriessolar.appendData(new DataPoint(0,0), true, 4);
        seriessolar.appendData(new DataPoint(1,20), true, 4);
        seriessolar.appendData(new DataPoint(2,150), true, 4);
        seriessolar.appendData(new DataPoint(3,110), true,4);
       serieslamp.appendData(new DataPoint(0,0), true, 4);
        serieslamp.appendData(new DataPoint(1,100), true, 4);
        serieslamp.appendData(new DataPoint(2,20), true, 4);
        serieslamp.appendData(new DataPoint(3,210), true,4);
    }

    
@Override
protected  void onResume(){
    super.onResume();
/*
    if(bluetoothComponent.refreshcheck==false){
        TextView dataloadingsolaroutside = (TextView)findViewById(R.id.data_loading_solar_id) ;
        dataloadingsolaroutside.setText("Data Loading...");
    }
    else{
        TextView dataloadingsolaroutside = (TextView)findViewById(R.id.data_loading_solar_id) ;
        dataloadingsolaroutside.setText("");
    }
*/
    //addEntry();
}
    public void Datetime(){


Handletime.postDelayed(new Runnable() {
    @Override
    public void run() {
        df = new SimpleDateFormat("MMM d, yyyy, HH:mm:ss");
        date = df.format(Calendar.getInstance().getTime());

        DateTime.setText(date);
        //System.out.println("Time is "+DateTime.getText());
        Datetime();
    }
},1000);


    }

    public void onBacksolar_panel_live_voltage (View view){
        final TextView viewToAnimate = (TextView) findViewById(R.id. back_arrow_solar_panel_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        if(bluetoothComponent.refreshcheck) {


Intent intent = new Intent(solar_panel_live_voltage.this,loggedIn.class);
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

          //  Toast.makeText(this,"Please wait Data Loading...",Toast.LENGTH_SHORT).show();
        }

    }

    public void refreshUI()
    {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                panel_status_voltage_inside = (TextView)findViewById(R.id.solar_panel_voltage_inside_id);
                panel_status_voltage_inside.setText("Voltage = "+bluetoothComponent.Solar_panel_staus_voltage+"V");

                panel_status_current_inside = (TextView)findViewById(R.id.solar_panel_current_inside_id);
                panel_status_current_inside.setText("Current = "+bluetoothComponent.Solar_panel_staus_current+"A");


                if(bluetoothComponent.graphdrawcheck)
                {
                    bluetoothComponent.graphdrawcheck=false;
                    drawgraph();

                }



//        bluetoothComponent.sday1="20";
//        bluetoothComponent.sday2="100";
//        bluetoothComponent.sday3="250";
//        bluetoothComponent.lday1="250";
//        bluetoothComponent.lday2="100";
//        bluetoothComponent.lday3="210";
//
//                seriessolar  = new LineGraphSeries<DataPoint>();
//                serieslamp = new LineGraphSeries<DataPoint>();
//                graph.addSeries(seriessolar);
//                graph.addSeries(serieslamp);
//                //customize
//                Viewport viewport  = graph.getViewport();
//                viewport.setYAxisBoundsManual(true);
//                viewport.setXAxisBoundsManual(true);
//
//                viewport.setMinY(0);
//                viewport.setMaxY(250);
//                viewport.setMinX(0);
//                viewport.setMaxX(3);
//                viewport.setScrollable(true );
//                serieslamp.setColor(Color.WHITE);
//                serieslamp.setDrawDataPoints(true);
//                serieslamp.setDataPointsRadius(10);
//                seriessolar.setDrawDataPoints(true);
//                seriessolar.setDataPointsRadius(10);
//
//
//
//                seriessolar.appendData(new DataPoint(0,0), true, 4);
//                double value = Double.parseDouble(bluetoothComponent.sday1.toString().trim());
//
//                seriessolar.appendData(new DataPoint(1,value), true, 4);
//                 value = Double.parseDouble(bluetoothComponent.sday2.toString().trim());
//                seriessolar.appendData(new DataPoint(2,value), true, 4);
//                 value = Double.parseDouble(bluetoothComponent.sday3.toString().trim());
//                seriessolar.appendData(new DataPoint(3,value), true,4);
//
//                serieslamp.appendData(new DataPoint(0,0), true, 4);
//                value = Double.parseDouble(bluetoothComponent.lday1.toString().trim());
//                serieslamp.appendData(new DataPoint(1,value), true, 4);
//                 value = Double.parseDouble(bluetoothComponent.lday2.toString().trim());
//                serieslamp.appendData(new DataPoint(2,value), true, 4);
//                 value = Double.parseDouble(bluetoothComponent.lday3.toString().trim());
//                serieslamp.appendData(new DataPoint(3,value), true,4);
//
//

            }
        });


    }

   public void drawgraph (){

       runOnUiThread(new Runnable() {

           @Override
           public void run() {

               bluetoothComponent.sday1 = "20";
               bluetoothComponent.sday2 = "100";
               bluetoothComponent.sday3 = "250";
               bluetoothComponent.lday1 = "250";
               bluetoothComponent.lday2 = "100";
               bluetoothComponent.lday3 = "210";

               seriessolar = new LineGraphSeries<DataPoint>();
               serieslamp = new LineGraphSeries<DataPoint>();
               graph.addSeries(seriessolar);
               graph.addSeries(serieslamp);
               //customize
               Viewport viewport = graph.getViewport();
               viewport.setYAxisBoundsManual(true);
               viewport.setXAxisBoundsManual(true);

               viewport.setMinY(0);
               viewport.setMaxY(250);
               viewport.setMinX(0);
               viewport.setMaxX(3);
               viewport.setScrollable(true);
               serieslamp.setColor(Color.WHITE);
               serieslamp.setDrawDataPoints(true);
               serieslamp.setDataPointsRadius(10);
               seriessolar.setDrawDataPoints(true);
               seriessolar.setDataPointsRadius(10);


               seriessolar.appendData(new DataPoint(0, 0), true, 4);
               double value = Double.parseDouble(bluetoothComponent.sday1.toString().trim());

               seriessolar.appendData(new DataPoint(1, value), true, 4);
               value = Double.parseDouble(bluetoothComponent.sday2.toString().trim());
               seriessolar.appendData(new DataPoint(2, value), true, 4);
               value = Double.parseDouble(bluetoothComponent.sday3.toString().trim());
               seriessolar.appendData(new DataPoint(3, value), true, 4);

               serieslamp.appendData(new DataPoint(0, 0), true, 4);
               value = Double.parseDouble(bluetoothComponent.lday1.toString().trim());
               serieslamp.appendData(new DataPoint(1, value), true, 4);
               value = Double.parseDouble(bluetoothComponent.lday2.toString().trim());
               serieslamp.appendData(new DataPoint(2, value), true, 4);
               value = Double.parseDouble(bluetoothComponent.lday3.toString().trim());
               serieslamp.appendData(new DataPoint(3, value), true, 4);
           }
           });

    }

    public void refreshscan_solar_status(View view){
        final ImageView viewToAnimate = (ImageView) findViewById(R.id.refreshbutton_solar_panel_id);

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
                //    timer.cancel();
                }
            },1500);
          //  Toast.makeText(this, "Please check connection", Toast.LENGTH_SHORT).show();

        }
        else {

        if(bluetoothComponent.refreshcheck){
            bluetoothComponent.refreshcheck=false;

            TextView dataloadingsolaroutside = (TextView)findViewById(R.id.data_loading_solar_id) ;
            dataloadingsolaroutside.setText("Data Loading...");

            Handlerrefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothComponent.refreshcheck=true;
                    TextView dataloadingsolarinside = (TextView)findViewById(R.id.data_loading_solar_id) ;
                    dataloadingsolarinside.setText("");
                }
            },5000);



        bluetoothComponent.currentActivity = solar_panel_live_voltage.this;






        final String message;

            //temporary

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
                    //    timer.cancel();
                }
            },1500);
          //  Toast.makeText(this,"Please wait Data Loading...",Toast.LENGTH_SHORT).show();
        }}

    }

}
