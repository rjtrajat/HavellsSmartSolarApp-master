package in.chipmonk.havellsolarapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import in.chipmonk.havellsolarapp.light_control;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * Created by Rajat on 18/08/16.
 */
public class watt_setting extends AppCompatActivity {
    public EditText manuallwatt;
    public NumberPicker wattpicker ;
    public DecimalFormat dff;

    protected void onCreate(Bundle BundleInstance){
        super.onCreate(BundleInstance);
        getSupportActionBar().hide();
        setContentView(R.layout.watt_setting);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
        View view = findViewById(R.id.hour_circle_id);
        int viewHeight=(int)(screenHeight*40)/100;
        int viewWidth=(int)(screenWidth*70)/100;
        view.getLayoutParams().width=viewWidth;
        view.getLayoutParams().height=viewHeight;


        wattpicker = (NumberPicker) findViewById(R.id.watt_picker);

        final String[] arrayPicker  = new String []{"5 WATT","10 WATT","15 WATT"};

        wattpicker.setMinValue(0);
        //set max value from length array string reduced 1
        wattpicker.setMaxValue(arrayPicker.length - 1);
        //implement array string to number picker
        wattpicker.setDisplayedValues(arrayPicker);
        //disable soft keyboard
        wattpicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //set wrap true or false, try it you will know the difference
        wattpicker.setWrapSelectorWheel(false);


        int count = wattpicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = wattpicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = wattpicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(wattpicker)).setColor(Color.WHITE );
                    ((EditText)child).setTextColor(Color.WHITE);
                    wattpicker.invalidate();

                }
                catch(Exception e){
                    //Log.w("setNumberPickerTextColor", e);
                }

            }
        }


        manuallwatt = (EditText)findViewById(R.id.SetWatt_manually_id);

    }
    public void SetWatt(View view){

        final TextView viewToAnimate = (TextView) findViewById(R.id.setwatt_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        final String message;


        int hourind,brightind,minind;
        hourind  = wattpicker.getValue()+1;


        boolean check=true;

        if(manuallwatt.getText().toString().trim().equals("")){

            if(hourind==1){
                light_control.Wattvalue="0"+Integer.toString(hourind*5)+".0";
            }
            else
        light_control.Wattvalue=Integer.toString(hourind*5)+".0";}
        else {

            try {
                double valueone = Double.parseDouble(manuallwatt.getText().toString().trim());
                int valuetwo = (int)valueone;


                if(valueone!=valuetwo){
                    if(valueone<=10.0){
                        light_control.Wattvalue = "0"+valueone;
                    }
                    else
                        light_control.Wattvalue = ""+valueone;

                }
                else {
                    if(valueone<10.0){
                        light_control.Wattvalue = "0"+valuetwo+ ".0";
                    }
                    else
                        light_control.Wattvalue = valuetwo + ".0";
                }
            }
            catch (Exception e)
            {
                check=false;
                Toast.makeText(watt_setting.this, "Please enter correct number", Toast.LENGTH_SHORT).show();

            }


        }

        if(check==true) {

            double finalvalue = Double.parseDouble(light_control.Wattvalue.toString().trim());

            System.out.println("light control value is " + light_control.Wattvalue);


            if (finalvalue >= 1.0 && finalvalue <= 60.0) {
                Intent intent = new Intent(watt_setting.this, light_control.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please Enter Watt between 1 and 60 ", Toast.LENGTH_SHORT).show();
            }
        }


    }
    public void cancelwatt(View view){

        final TextView viewToAnimate = (TextView) findViewById(R.id.cancel_watt_id);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        viewToAnimate.startAnimation(in);
        viewToAnimate.setVisibility(View.VISIBLE);

        light_control.Wattvalue="";

        Intent intent = new Intent(watt_setting.this,light_control.class);
        startActivity(intent);

    }

}
