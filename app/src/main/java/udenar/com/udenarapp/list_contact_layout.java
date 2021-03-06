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
    String get_subject_url = "http://ocara.udenar.edu.co/academicaws/getSubject/";
    ArrayList<String> list,list1,list2,list3;
    ArrayList<ArrayList> totList;
    ArrayAdapter<String> adapter;
    Context con;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact_layout);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Obteniendo Respuesta");
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
                list = new ArrayList<String>();
                list1 = new ArrayList<String>();
                list2 = new ArrayList<String>();
                list3 = new ArrayList<String>();
                totList = new ArrayList<ArrayList>();

                String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

                //Log.e("si","result"+httppost.getURI().toString());



                JSONObject jsonMainNode = new JSONObject(jsonResult);






               // Log.e("si","sdf 2");
                String nom=jsonMainNode.optString("nombre")+"   "+jsonMainNode.optString("apellido");
                String car=jsonMainNode.optString("programa")+"   "+jsonMainNode.optString("semestre");
                list.add(nom);
                list.add(car);
                JSONArray materias = jsonMainNode.getJSONArray("materias");
                 for(int i=0;i<materias.length();i++){
                        JSONObject ch= materias.getJSONObject(i);
                        String cad=ch.optString("codigo");
                        String cad1=ch.optString("materia");
                        String cad2=ch.optString("veces_cursada");
                        String cad3=ch.optString("observacion");
                        String totCad =""+cad+" "+cad1+" "+cad2+" "+cad3+"";
                        list.add(totCad);
                 }
                 totList.add(list);


            } catch (Exception e) {
                Log.v("err",e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(Float... valores) {
        }

        protected void onPostExecute(ListView bytes) {

            try {
                adapter = new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1,list);
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
