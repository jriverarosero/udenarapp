package udenar.com.udenarapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ConsultaDesprendible extends ActionBarActivity {

    HttpClient httpclient;
    HttpGet httppost;
    ProgressDialog dialog;
    String get_subject_url = "http://192.168.9.136:90/academica/login/";
    //String get_subject_url = "http://172.16.132.13:90/academica/login/";
    EditText name;
    EditText pwd;
    Button btn;
    Context cntx = this;
    String is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_desprendible);

        httpclient = new DefaultHttpClient();

        name = (EditText) findViewById(R.id.ced);
        pwd = (EditText) findViewById(R.id.pwd);
        btn = (Button) findViewById(R.id.login);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Obteniendo Respuesta");
        dialog.setCancelable(false);

        httpclient = new DefaultHttpClient();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httppost = new HttpGet(get_subject_url + name.getText() + "/" + pwd.getText());
                new MiTarea().execute();
            }
        });

    }


    public class MiTarea extends AsyncTask<String, Float, ListView> {

        protected void onPreExecute() {
            dialog.show();
        }

        protected ListView doInBackground(String... urls) {
            try {
                HttpResponse response = httpclient.execute(httppost);


                String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

                JSONObject jsonMainNode = new JSONObject(jsonResult);
                is = jsonMainNode.optString("logeo");
                Log.e("DEB", is);


            } catch (Exception e) {
                Log.v("err", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(Float... valores) {
        }

        protected void onPostExecute(ListView bytes) {

            try {

                if (is.compareTo("no") == 0) {
                    Toast.makeText(cntx, "Usuario No Encontrado", Toast.LENGTH_LONG).show();
                } else if (is.compareTo("si") == 0) {
                    Intent i = new Intent(cntx, list_tickets.class);
                    i.putExtra("id",name.getText().toString());
                    startActivity(i);
                }

            } catch (Exception e) {

            } finally {
                dialog.dismiss();
            }

        }
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null)
                answer.append(rLine);
        } catch (IOException e) {
        }

        return answer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_consulta_desprendible, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
