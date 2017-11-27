package de.drk_odenwaldkreis.qrscanner_test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RDInputActivity extends AppCompatActivity {

    private String qr_content;
    private EditText [] infoedit = new EditText[8];
    private String [] info = new String[8];
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "START:<b>QR</b>" + "</black>"));
        setContentView(R.layout.activity_rdinput);
        Bundle b = getIntent().getExtras();
        qr_content = b.getString("qr_content");
        IP = b.getString("IP_adress");

    }



    public void addInfo(View view){


        infoedit[0] = (EditText)findViewById(R.id.edit_zf_rd);
        infoedit[1] = (EditText)findViewById(R.id.edit_gf_rd);
        infoedit[2] = (EditText)findViewById(R.id.edit_helfer_rd);
        infoedit[3] = (EditText)findViewById(R.id.edit_NA);
        infoedit[4] = (EditText)findViewById(R.id.edit_A);
        infoedit[5] = (EditText)findViewById(R.id.edit_NS);
        infoedit[6] = (EditText)findViewById(R.id.edit_RS);
        infoedit[7] = (EditText)findViewById(R.id.edit_Bem_rd);



        //Toast.makeText(getApplicationContext(), "inaddInfo", Toast.LENGTH_LONG).show();
        for (int i = 0; i <8; i++){
            info[i]=infoedit[i].getText().toString();

            qr_content=qr_content+"|"+info[i];
        }

        Intent toForm = new Intent(RDInputActivity.this, FormActivity.class);
        toForm.putExtra("qr_content", qr_content);
        toForm.putExtra("IP_adress", IP);
        startActivity(toForm);



    }
}
