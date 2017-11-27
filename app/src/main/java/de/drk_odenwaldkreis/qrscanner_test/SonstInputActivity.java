package de.drk_odenwaldkreis.qrscanner_test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;

public class SonstInputActivity extends AppCompatActivity {
    private String qr_content;
    private EditText[] infoedit = new EditText[4];
    private String [] info = new String[4];
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "START:<b>QR</b>" + "</black>"));
        setContentView(R.layout.activity_sonst_input);
        Bundle b = getIntent().getExtras();
        qr_content = b.getString("qr_content");
        IP = b.getString("IP_adress");

    }



    public void addInfoS(View view){


        infoedit[0] = (EditText)findViewById(R.id.edit_zf_s);
        infoedit[1] = (EditText)findViewById(R.id.edit_gf_s);
        infoedit[2] = (EditText)findViewById(R.id.edit_helfer_s);
        infoedit[3] = (EditText)findViewById(R.id.edit_Bem_s);



        //Toast.makeText(getApplicationContext(), "inaddInfo", Toast.LENGTH_LONG).show();
        for (int i = 0; i <4; i++){
            info[i]=infoedit[i].getText().toString();

            qr_content=qr_content+"|"+info[i];
        }

        Intent toForm = new Intent(SonstInputActivity.this, FormActivity.class);
        toForm.putExtra("qr_content", qr_content);
        toForm.putExtra("IP_adress", IP);
        startActivity(toForm);



    }
}
