package de.drk_odenwaldkreis.qrscanner_test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IPActivity extends AppCompatActivity {

    private String IP;
    public static final String PREF_NAME = "MyIPsave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "START:<b>QR</b>" + "</black>"));
        setContentView(R.layout.activity_ip);
        SharedPreferences ip_pref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        IP= ip_pref.getString("ip", "IP Adresse eingeben");
        EditText et = (EditText) findViewById(R.id.editIP);
        et.setText(IP);
    }

    public void do_set(View view){
        EditText IP_element=(EditText)findViewById(R.id.editIP);
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("ip", IP_element.getText().toString());
        editor.apply();
        Toast.makeText(getApplicationContext(), IP , Toast.LENGTH_LONG).show();
        Intent toMain = new Intent(IPActivity.this, MainActivity.class);
        toMain.putExtra("IP_adress", IP);
        startActivity(toMain);
    }
}
