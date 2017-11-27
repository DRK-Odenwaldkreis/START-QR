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

public class FWInputActivity extends AppCompatActivity {

    private String qr_content;
    private EditText[] infoedit = new EditText[7];
    private String [] info = new String[7];
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "START:<b>QR</b>" + "</black>"));
        setContentView(R.layout.activity_fwinput);
        Bundle b = getIntent().getExtras();
        qr_content = b.getString("qr_content");
        IP = b.getString("IP_adress");

    }



    public void addInfo(View view){


        infoedit[0] = (EditText)findViewById(R.id.edit_zf_fw);
        infoedit[1] = (EditText)findViewById(R.id.edit_gf_fw);
        infoedit[2] = (EditText)findViewById(R.id.edit_helfer_fw);
        infoedit[3] = (EditText)findViewById(R.id.edit_atemschutz);
        infoedit[4] = (EditText)findViewById(R.id.edit_CSA);
        infoedit[5] = (EditText)findViewById(R.id.edit_San);
        infoedit[6] = (EditText)findViewById(R.id.edit_Bem_fw);



        //Toast.makeText(getApplicationContext(), "inaddInfo", Toast.LENGTH_LONG).show();
        for (int i = 0; i <7; i++){
            info[i]=infoedit[i].getText().toString();

            qr_content=qr_content+"|"+info[i];
        }

        Intent toForm = new Intent(FWInputActivity.this, FormActivity.class);
        toForm.putExtra("qr_content", qr_content);
        toForm.putExtra("IP_adress", IP);
        startActivity(toForm);



    }
}
