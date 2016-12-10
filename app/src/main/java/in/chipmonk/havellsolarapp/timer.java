package in.chipmonk.havellsolarapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.EditText;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Paint;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Rajat on 27/07/16.
 */
public class timer extends AppCompatActivity {
    static public int hour,minute;
    public NumberPicker hourpicker ;
    public TextView textupdate_per;
    static public light_control light_control_activity=null;

    public TextView textupdate_hour;
    public TextView textupdate_min;
    public  NumberPicker perpicker;
    public NumberPicker minpicker;
    public  AlertDialog.Builder alertDialogBuilder ;
    public Timer timer;
    public AlertDialog alertDialog;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        getSupportActionBar().hide();
        setContentView(R.layout.timer);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbartimer);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
        View view = findViewById(R.id.hour_circle_id);
        int viewHeight=(int)(screenHeight*40)/100;
        int viewWidth=(int)(screenWidth*70)/100;
        view.getLayoutParams().width=viewWidth;
        view.getLayoutParams().height=viewHeight;

        textupdate_per = (TextView)findViewById(R.id.Timer_update_per_id);
        textupdate_hour = (TextView)findViewById(R.id.Timer_update_hour_id);

        textupdate_min = (TextView)findViewById(R.id.Timer_update_min_id);


        hourpicker = (NumberPicker)findViewById(R.id.Hour_picker);

           final String[] arrayPicker  = new String []{"0 HOUR","1 HOUR","2 HOUR","3 HOUR","4 HOUR","5 HOUR","6 HOUR","7 HOUR","8 HOUR","9 HOUR","10 HOUR","11 HOUR","12 HOUR"};
           final String [] perPicker = new String []{"0%","10%","20%","30%","40%","50%","60%","70%","80%","90%","100%"};
       final String [] minPicker = new String []{"0 MINS","1 MINS","2 MINS","3 MINS","4 MINS","5 MINS","6 MINS","7 MINS","8 MINS","9 MINS","10 MINS",
               "11 MINS","12 MINS","13 MINS","14 MINS","15 MINS","16 MINS","17 MINS","18 MINS","19 MINS","20 MINS",
               "21 MINS","22 MINS","23 MINS","24 MINS","25 MINS","26 MINS","27 MINS","28 MINS","29 MINS","30 MINS",
               "31 MINS","32 MINS","33 MINS","34 MINS","35 MINS","36 MINS","37 MINS","38 MINS","39 MINS","40 MINS",
               "41 MINS","42 MINS","43 MINS","44 MINS","45 MINS","46 MINS","47 MINS","48 MINS","49 MINS","50 MINS",
               "51 MINS","52 MINS","53 MINS","54 MINS","55 MINS","56 MINS","57 MINS","58 MINS","59 MINS"};
        hourpicker.setMinValue(0);
        //set max value from length array string reduced 1
        hourpicker.setMaxValue(arrayPicker.length - 1);
        //implement array string to number picker
        hourpicker.setDisplayedValues(arrayPicker);
        //disable soft keyboard
        hourpicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //set wrap true or false, try it you will know the difference
        hourpicker.setWrapSelectorWheel(false);

        perpicker = (NumberPicker)findViewById(R.id.per_picker);

        perpicker.setMinValue(0);
        //set max value from length array string reduced 1
        perpicker.setMaxValue(perPicker.length - 1);
        //implement array string to number picker
        perpicker.setDisplayedValues(perPicker);
        //disable soft keyboard
        perpicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //set wrap true or false, try it you will know the difference
        perpicker.setWrapSelectorWheel(false);





        minpicker = (NumberPicker)findViewById(R.id.Min_picker);

        minpicker.setMinValue(0);
        //set max value from length array string reduced 1
        minpicker.setMaxValue(minPicker.length - 1);
        //implement array string to number picker
        minpicker.setDisplayedValues(minPicker);
        //disable soft keyboard
        minpicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //set wrap true or false, try it you will know the difference
        minpicker.setWrapSelectorWheel(false);




         int count = hourpicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = hourpicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = hourpicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(hourpicker)).setColor(Color.WHITE );
                    ((EditText)child).setTextColor(Color.WHITE);
                    hourpicker.invalidate();

                }
                catch(Exception e){
                    //Log.w("setNumberPickerTextColor", e);
                }

            }
        }


        count = perpicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = perpicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = perpicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(perpicker)).setColor(Color.WHITE );
                    ((EditText)child).setTextColor(Color.WHITE);
                    perpicker.invalidate();

                }
                catch(Exception e){
                    //Log.w("setNumberPickerTextColor", e);
                }

            }
        }


        count = minpicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = minpicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = minpicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(minpicker)).setColor(Color.WHITE );
                    ((EditText)child).setTextColor(Color.WHITE);
                    minpicker.invalidate();

                }
                catch(Exception e){
                    //Log.w("setNumberPickerTextColor", e);
                }

            }
        }




                hourpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i2) {
textupdate_hour.setText(" after "+arrayPicker[hourpicker.getValue()]);



                    }
                });

        perpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                textupdate_per.setText("Light after switched ON will be dimmed "+perPicker[perpicker.getValue()]);



            }
        });

        minpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                textupdate_min.setText(" "+minPicker[minpicker.getValue()]);



            }
        });


        alertDialogBuilder = new AlertDialog.Builder(this);
        timer = new Timer();


    }
    public void SetTimer(View view){

        final String message;
        String messagetimetemp="";


        final TextView viewToAnimate = (TextView) findViewById(R.id.settimer_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        int hourind,brightind,minind;
        hourind  = hourpicker.getValue();
        brightind = perpicker.getValue()*10;
        minind = minpicker.getValue();
        hour = hourpicker.getValue() ;
        minute = minind;



         if(hourind<10)
             messagetimetemp = "dimdurn 0"+Integer.toString(hourind)+"h";
        else
             messagetimetemp = "dimdurn "+Integer.toString(hourind)+"h";

        if(minind<10)
            messagetimetemp=messagetimetemp+"0"+minind+":";
        else
            messagetimetemp=messagetimetemp+minind+":";

        message=messagetimetemp;





        light_control_activity.Hourvalue=Integer.toString(hourind);
        light_control_activity.Minvalue=Integer.toString(minute);
        light_control_activity.ON_OFF_State="ON";



       /* runOnUiThread(new Runnable() {

            @Override
            public void run() {



                bluetoothComponent.tx = bluetoothComponent.gatt.getService(bluetoothComponent.UART_UUID).getCharacteristic(bluetoothComponent.TX_UUID);

                if(bluetoothComponent.tx!=null){
                    bluetoothComponent.tx.setValue(message);

                    bluetoothComponent.write();


                }
                else
                    System.out.println("tx is null");





            }
        });
*/
        final String newmessage;

        if(brightind==0)
            newmessage = "bright 00"+Integer.toString(brightind)+":";
        else if(brightind!=100)
            newmessage = "bright 0"+Integer.toString(brightind)+":";
        else
            newmessage = "bright "+Integer.toString(brightind)+":";

        light_control_activity.Brightvalue=newmessage;


/*


        runOnUiThread(new Runnable() {

            @Override
            public void run() {



                bluetoothComponent.tx = bluetoothComponent.gatt.getService(bluetoothComponent.UART_UUID).getCharacteristic(bluetoothComponent.TX_UUID);

                if(bluetoothComponent.tx!=null){
                    bluetoothComponent.tx.setValue(newmessage);

                    bluetoothComponent.write();


                }
                else
                    System.out.println("tx is null");





            }
        });
*/


        if(light_control_activity.Brightvalue!="") {
            String tempbrightmessage = light_control_activity.Brightvalue;
            tempbrightmessage = tempbrightmessage.replace("bright ","");
            tempbrightmessage = tempbrightmessage.replace(":","");
           // Toast.makeText(this,"Brightness is set to "+tempbrightmessage+"%",Toast.LENGTH_SHORT).show();


            alertDialogBuilder.setMessage("Brightness is set to "+tempbrightmessage+"%");

            alertDialogBuilder.setNeutralButton(
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //  Toast.makeText(light_control.this,"You clicked OK Button",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(timer.this,light_control.class);
                            startActivity(intent);
                        }
                    }
            );

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }







    }

    public void Cancelbtn(View view){

        final TextView viewToAnimate = (TextView) findViewById(R.id.cancel_timer_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);
//
//        light_control_activity.Hourvalue="";
//        light_control_activity.Minvalue="";
//        light_control_activity.ON_OFF_State="ON";
//
//        light_control_activity.Brightvalue = "bright 050:";
//
//        light_control_activity.Hourvalue = "1";
//        light_control_activity.Minvalue="0";




        light_control_activity.Hourvalue="";
        light_control_activity.Minvalue="";
        light_control_activity.ON_OFF_State="OFF";

        light_control_activity.Brightvalue = "";

        light_control_activity.Hourvalue = "";
        light_control_activity.Minvalue="";


        Intent intent = new Intent(timer.this,light_control.class);
        startActivity(intent);
    }


}
