package udenar.com.udenarapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class list_contact_layout extends ActionBarActivity {

    HttpClient httpclient;
    HttpGet httppost;
    ProgressDialog dialog;
    String get_subject_url = "http://190.254.4.125:90/academica/getSubject/";
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Context con;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact_layout);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Obteniendo Materias");
        dialog.setCancelable(false);

        httpclient = new DefaultHttpClient();
        Bundle bundle = getIntent().getExtras();
        httppost = new HttpGet(get_subject_url+bundle.getString("code"));
        con = this;

        lv=(ListView)findViewById(R.id.lv);
        new MiTarea().execute();
    }


    public class MiTarea extends AsyncTask<String, Float, ListView> {

        protected void onPreExecute() {
            dialog.show();
        }

        protected ListView doInBackground(String... urls) {
            try {
                HttpResponse response = httpclient.execute(httppost);
                String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

                //Log.e("si","result"+httppost.getURI().toString());

                JSONObject jsonMainNode = new JSONObject(jsonResult);


                list = new ArrayList<String>();
               // Log.e("si","sdf 2");



                    JSONArray materias = jsonMainNode.getJSONArray("materias");
                    for(int i=0;i<materias.length();i++){
                        JSONObject ch= materias.getJSONObject(i);
                        String cad=ch.optString("materia");
                        list.add(cad);
                    }




            } catch (Exception e) {
                Log.v("err",e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(Float... valores) {
        }

        protected void onPostExecute(ListView bytes) {

            try {
                adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1, list);
                lv.setAdapter(adapter);

            }catch (Exception e){

            }finally {
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
        getMenuInflater().inflate(R.menu.menu_list_contact_layout, menu);
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
