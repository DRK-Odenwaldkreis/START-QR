package de.drk_odenwaldkreis.qrscanner_test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Success_NoConn_After_First extends AppCompatActivity {

    String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "START:<b>QR</b>" + "</black>"));
        setContentView(R.layout.activity_success__no_conn__after__first);

        Bundle b = getIntent().getExtras();
        String qr_content = b.getString("qr_content");
        String status = b.getString("result");
        IP = b.getString("IP_adress");
        String [] properties;
        properties = qr_content.split("\\|");

        //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();

        TextView name_view = (TextView) findViewById(R.id.name_);
        name_view.setText(properties[0]);

        TextView typ_view = (TextView) findViewById(R.id.typ_);
        typ_view.setText(properties[3]);

        TextView status_view = (TextView) findViewById(R.id.status_);


        if (status.equals("200")){
            status_view.setTextColor(Color.GREEN);
            status_view.setText("Erfolgreich");
        }
        else if (status.equals("202")){
            status_view.setTextColor(Color.rgb(255,215,0));
            status_view.setText("Fahreug bereits Vorhanden / Kein Auftrag");
        }
        else {status_view.setTextColor(Color.RED);
            status_view.setText("Verbindungsfehler");}

    }

    public void main_return(View view){
        Intent toMain = new Intent(Success_NoConn_After_First.this, MainActivity.class);
        toMain.putExtra("IP_adress", IP);
        startActivity(toMain);

    }
}
