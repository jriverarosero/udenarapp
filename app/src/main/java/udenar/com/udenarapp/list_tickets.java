package udenar.com.udenarapp;

import android.app.ProgressDialog;
import android.content.Context;
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


public class list_tickets extends ActionBarActivity {

    HttpClient httpclient;
    HttpGet httppost;
    ProgressDialog dialog;
    String get_subject_url = "http://192.168.9.136:90/academica/getTickets/";
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Context con;
    ListView lv;
    EditText date1;
    EditText date2;
    Button btn;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tickets);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Obteniendo Respuesta");
        dialog.setCancelable(false);

        date1 = (EditText) findViewById(R.id.date1);
        date2 = (EditText) findViewById(R.id.date2);
        btn = (Button) findViewById(R.id.tickets);

        con = this;

        httpclient = new DefaultHttpClient();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");

        lv = (ListView) findViewById(R.id.lvt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Fecha",date1.getText().toString());
                //httppost = new HttpGet("http://192.168.9.136:90/academica/getTickets/123456/2015-04-04/2015-05-05");
                httppost = new HttpGet(get_subject_url + id + "/" + date1.getText() + "/" + date2.getText());
                Log.e("Fecha",get_subject_url + id + "/" + date1.getText() + "/" + date2.getText());
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
                list = new ArrayList<String>();

                String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

                Log.e("si","result"+httppost.getURI().toString());


                JSONObject jsonMainNode = new JSONObject(jsonResult);
                JSONArray jsonArray = jsonMainNode.getJSONArray("desprendibles");

                 Log.e("si","sdf 2");
                String nom = jsonMainNode.optString("nombre") + "   " + jsonMainNode.optString("apellido");
                list.add(nom);
                list.add("Desprendibles");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject ch = jsonArray.getJSONObject(i);
                    String cad = ch.optString("fecha");
                    String cad1 = ch.optString("seccion");
                    String cad2 = ch.optString("banco");
                    String cad3 = "Concepto:\n";
                    String cad4 = ch.optString("concepto");
                    String cad5 = ch.optString("nombre");
                    String cad6 = ch.optString("tipo");

                    String totCad = "" + cad + " " + cad1 + " " + cad2 + " " + cad3 + " "+ cad4 + " "+ cad5+" "+ cad6+" ";
/*                    if(cad6.compareTo("D")==0){
                        totCad = "" + cad + " " + cad1 + " " + cad2 + " " + cad3 + " "+ cad4 + " "+ cad5 + "Descuento";
                    } else if(cad6.compareTo("B")==0){
                        totCad = "" + cad + " " + cad1 + " " + cad2 + " " + cad3 + " "+ cad4 + " "+ cad5 + "Pago";
                    }*/

                    list.add(totCad);
                }


            } catch (Exception e) {
                Log.v("err", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(Float... valores) {
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

        protected void onPostExecute(ListView bytes) {

            try {
                adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1, list);
                lv.setAdapter(adapter);

            } catch (Exception e) {

            } finally {
                dialog.dismiss();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_tickets, menu);
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
