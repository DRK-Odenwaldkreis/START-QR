package de.drk_odenwaldkreis.qrscanner_test;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import me.dm7.barcodescanner.zxing.ZXingScannerView;



public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;
    public String IP = "empty";
    public static final String PREF_NAME = "MyIPsave";

    public String qr;
    String[] properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "START:<b>QR</b>" + "</black>"));
        setContentView(R.layout.activity_main);

        SharedPreferences ip_pref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String IP_ohne= ip_pref.getString("ip", "no IP");
        if (IP_ohne.equals("no IP")) {
            Toast.makeText(getApplicationContext(), "IP Adresse nicht gesetzt", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), IP_ohne, Toast.LENGTH_SHORT).show();
            IP=IP_ohne+":8051";
        }


    }

    public void scan (View view){
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

   protected void onPause(){
        super.onPause();
       try {
           zXingScannerView.stopCamera();
       }
       catch (Exception e)
       {}
    }

    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_main);
    }

    public void handleResult(Result result){
        //Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        //zXingScannerView.resumeCameraPreview(this);
        new SendFirstRequest().execute(result.getText());

        /*Intent toForm = new Intent(MainActivity.this,FormActivity.class);
        toForm.putExtra("qr_content", result.getText());
        toForm.putExtra("IP_adress",IP);
        startActivity(toForm);*/
    }

    public void setIP(View view){
        Intent toIP = new Intent(MainActivity.this, IPActivity.class);
        startActivity(toIP);
    }

    public class SendFirstRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){

        }

        protected String doInBackground(String... arg0) {
            String status;
            status = "init";
            try {
                URL url = new URL("http://"+IP);


                qr = arg0[0];
                properties = qr.split("\\|");
                // hier müssen jetzt if abfragen nach der orga rein, dann werden die
                // entsprechende infos in postDataParams geschrieben

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("f_name", properties[0]);
                postDataParams.put("orga", properties[1]);
                postDataParams.put("führung", properties[2]);
                postDataParams.put("typ", properties[3]);
                postDataParams.put("sonderbeladung", properties[4]);


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(1000);
                conn.setConnectTimeout(1000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();


                status=Integer.toString(conn.getResponseCode());

                os.close();
                conn.disconnect();


                //return Integer.toString(conn.getResponseCode());

            } catch (Exception e) {


                status="Serverantwort fehlerhaft!";
            }
            return status;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Intent toFinal = new Intent(MainActivity.this, Success_NoConn_After_First.class);
            //Toast.makeText(getApplicationContext(), IP, Toast.LENGTH_LONG).show();
            if (result.equals("200")){
                Toast.makeText(getApplicationContext(), "Übertragung erfolgreich", Toast.LENGTH_LONG).show();

                toFinal.putExtra("result", result);
                toFinal.putExtra("qr_content", qr);
                toFinal.putExtra("IP_adress", IP);
                startActivity(toFinal);
             }
            else if (result.equals("201")){
                Toast.makeText(getApplicationContext(), "Informationen ergänzen", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), properties[1], Toast.LENGTH_LONG).show();
                if (properties[1].equals("RD")){
                    Intent toRD = new Intent(MainActivity.this, RDInputActivity.class);
                    toRD.putExtra("qr_content", qr);
                    toRD.putExtra("IP_adress", IP);
                    startActivity(toRD);
                }
                else if(properties[1].equals("FW")){
                    Intent toFW = new Intent(MainActivity.this, FWInputActivity.class);
                    toFW.putExtra("qr_content", qr);
                    toFW.putExtra("IP_adress", IP);
                    startActivity(toFW);
                }
                else{
                    Intent toSonst = new Intent(MainActivity.this, SonstInputActivity.class);
                    toSonst.putExtra("qr_content", qr);
                    toSonst.putExtra("IP_adress", IP);
                    startActivity(toSonst);
                }
            }
            else if (result.equals("202")){
                Toast.makeText(getApplicationContext(), "Fahrzeug vorhanden", Toast.LENGTH_LONG).show();

                toFinal.putExtra("result", result);
                toFinal.putExtra("qr_content", qr);
                toFinal.putExtra("IP_adress", IP);
                startActivity(toFinal);
            }
            else {
                Toast.makeText(getApplicationContext(), result , Toast.LENGTH_LONG).show();

                toFinal.putExtra("result", result);
                toFinal.putExtra("qr_content", qr);
                toFinal.putExtra("IP_adress", IP);
                startActivity(toFinal);
            }



        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}

