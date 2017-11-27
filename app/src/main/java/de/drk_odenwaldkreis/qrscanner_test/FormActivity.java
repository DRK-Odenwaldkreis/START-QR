package de.drk_odenwaldkreis.qrscanner_test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class FormActivity extends AppCompatActivity {

    private String qr_content;

    private String IP;


    //private HttpURLConnection conn = null;


    private String[] properties;


    public FormActivity() throws MalformedURLException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "START:<b>QR</b>" + "</black>"));
        setContentView(R.layout.activity_form);
        Bundle b = getIntent().getExtras();
        qr_content = b.getString("qr_content");
        properties = qr_content.split("\\|");

        TextView name_view = (TextView) findViewById(R.id.name_dat);
        name_view.setText(properties[0]);

        TextView typ_view = (TextView) findViewById(R.id.typ_dat);
        typ_view.setText(properties[3]);

        IP=b.getString("IP_adress");

        new SendPostRequest().execute();


    }
        public class SendPostRequest extends AsyncTask<String, Void, String> {

            protected void onPreExecute(){}

            protected String doInBackground(String... arg0) {
                String status;
                try {
                    URL url = new URL("http://"+IP);
                    // hier müssen jetzt if abfragen nach der orga rein, dann werden die
                    // entsprechende infos in postDataParams geschrieben

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("f_name", properties[0]);
                    postDataParams.put("orga", properties[1]);
                    postDataParams.put("führung", properties[2]);
                    postDataParams.put("typ", properties[3]);
                    postDataParams.put("sonderbeladung", properties[4]);

                    postDataParams.put("liegend", properties[5]);
                    postDataParams.put("tragestuhl", properties[6]);
                    postDataParams.put("sitzend", properties[7]);


                    postDataParams.put("loeschwasser", properties[8]);
                    postDataParams.put("schaummittel", properties[9]);

                    postDataParams.put("telefon", properties[10]);
                    postDataParams.put("fax", properties[11]);
                    postDataParams.put("email", properties[12]);
                    postDataParams.put("mrt", properties[13]);
                    postDataParams.put("hrt", properties[14]);

                    if (properties[1].equals("RD")){
                        postDataParams.put("zf", properties[15]);
                        postDataParams.put("gf", properties[16]);
                        postDataParams.put("helfer", properties[17]);

                        postDataParams.put("notarzt", properties[18]);
                        postDataParams.put("arzt", properties[19]);
                        postDataParams.put("notsan", properties[20]);
                        postDataParams.put("rs", properties[21]);

                        postDataParams.put("bemerkung", properties[22]);

                        postDataParams.put("atemschutz","");
                        postDataParams.put("csa", "");
                        postDataParams.put("sanitaeter", "");
                    }

                    else if (properties[1].equals("FW")){
                        postDataParams.put("zf", properties[15]);
                        postDataParams.put("gf", properties[16]);
                        postDataParams.put("helfer", properties[17]);

                        postDataParams.put("bemerkung", properties[21]);

                        postDataParams.put("notarzt", "");
                        postDataParams.put("arzt", "");
                        postDataParams.put("notsan", "");
                        postDataParams.put("rs", "");
                        postDataParams.put("atemschutz",properties[18]);
                        postDataParams.put("csa", properties[19]);
                        postDataParams.put("sanitaeter", properties[20]);
                    }

                    else{
                        postDataParams.put("zf", properties[15]);
                        postDataParams.put("gf", properties[16]);
                        postDataParams.put("helfer", properties[17]);
                        postDataParams.put("bemerkung", properties[18]);
                        postDataParams.put("notarzt", "");
                        postDataParams.put("arzt", "");
                        postDataParams.put("notsan", "");
                        postDataParams.put("rs", "");
                        postDataParams.put("atemschutz","");
                        postDataParams.put("csa", "");
                        postDataParams.put("sanitaeter", "");
                    }



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

                    status= e.getMessage();
                }
                return status;
            }
            @Override
            protected void onPostExecute(String result) {

                TextView status_view = (TextView) findViewById(R.id.status_dat);
                if (result.equals("200")){
                    Toast.makeText(getApplicationContext(), "Übertragung erfolgreich", Toast.LENGTH_LONG).show();
                    status_view.setTextColor(Color.GREEN);
                    status_view.setText("Erfolgreich");
                }
                else if (result.equals("201")){
                    Toast.makeText(getApplicationContext(), "QR Code unvollständig", Toast.LENGTH_LONG).show();
                    status_view.setTextColor(Color.RED);
                    status_view.setText("QR Code unvollständig");
                }
                else if (result.equals("202")){
                    Toast.makeText(getApplicationContext(), "Fahrzeug vorhanden", Toast.LENGTH_LONG).show();
                    status_view.setTextColor(Color.rgb(255,215,0));
                    status_view.setText("Fahrzeug bereits Vorhanden / Kein Auftrag");
                }
                else {
                    Toast.makeText(getApplicationContext(), result , Toast.LENGTH_LONG).show();
                    status_view.setTextColor(Color.RED);
                    status_view.setText("Fehler / Keine Antwort von ELW");
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

    public void main_screen(View view){
        Intent toMain = new Intent(FormActivity.this, MainActivity.class);
        toMain.putExtra("IP_adress", IP);
        startActivity(toMain);

    }

}

