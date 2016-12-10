package in.chipmonk.havellsolarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.graphics.Paint;
import android.graphics.Color;


import android.view.View;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);


        getSupportActionBar().hide();


        setContentView(R.layout.activity_main);

     //   requestPermissions();
    }
    public void LoggedIn(View view){
        Intent intent = new Intent(MainActivity.this,lamps.class);
        startActivity(intent);
    }
    public void operatorButton(View view){

    View tempview = findViewById(R.id.operatorLine);
        tempview.setBackgroundColor(Color.parseColor("#048EC0"));
        tempview.invalidate();
        tempview = findViewById(R.id.adminLine);
        tempview.setBackgroundColor(Color.WHITE);
        tempview.invalidate();
    }
    public void admitButton(View view){

        View tempview = findViewById(R.id.adminLine);
        tempview.setBackgroundColor(Color.parseColor("#048EC0"));
        tempview.invalidate();
        tempview = findViewById(R.id.operatorLine);
        tempview.setBackgroundColor(Color.WHITE);
        tempview.invalidate();
    }

}
