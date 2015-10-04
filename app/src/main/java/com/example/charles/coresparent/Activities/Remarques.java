package com.example.charles.coresparent.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.charles.coresparent.Adapters.MyArrayAdapterAbs;
import com.example.charles.coresparent.Adapters.MyArrayAdapterRem;
import com.example.charles.coresparent.DBInteraction.connectDB;
import com.example.charles.coresparent.Listener.onSwipe;
import com.example.charles.coresparent.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Remarques extends Activity implements onSwipe.onSwipeEvent{
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public InputStream is;
    public String result;
    private ArrayList<String> id_com_;
    private ArrayList<String> content_;
    private ArrayList<String> auteur_;
    private ArrayList<String> id_et_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarques);
        result = null;
        is = null;
        id_com_ = new ArrayList<String>();
        content_ = new ArrayList<String>();
        auteur_ = new ArrayList<String>();
        id_et_ = new ArrayList<String>();
        new Thread(new Runnable() {
            public void run() {
                ArrayList<String> colName1 = new ArrayList<String>(6);
                colName1.add("id_commentaire");
                colName1.add("contenu");
                colName1.add("auteur");
                colName1.add("id_etudiant");
                makeRequest("http://10.0.3.2/access.php?id=commentaires", colName1);
            }
        }).start();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if(id == 0)
                    intent = new Intent(getBaseContext(), Absences.class);
                else if(id == 1)
                    intent = new Intent(getBaseContext(), Remarques.class);
                else if(id == 2)
                    intent = new Intent(getBaseContext(), Eleves.class);
                startActivity(intent);
            }
        });
    }
    private void addDrawerItems() {
        String[] osArray = { "Absences", "Remarques", "Eleves", "Parametres" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }
    @Override
    public void SwipeEventDetected(View v, int SwipeType) {}

    public void makeRequest(String url, final ArrayList<String> colName){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        result = makeHttpRequest(url, "POST", nameValuePairs);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTabler(result, colName);
            }
        });
    }
    //Display in the selected table Row
    public void setTabler(String jarray, ArrayList<String> colName){
        try {
            JSONArray jArray = new JSONArray(jarray);
            for (int i = 0; i < jArray.length() - 1; i++) {
                JSONObject json_data = null;
                try {
                    json_data = jArray.getJSONObject(i);
                    for (String eachname : colName) {
                        if(eachname == "id_commentaire")
                            id_com_.add(json_data.getString(eachname));
                        if(eachname == "contenu")
                            content_.add(json_data.getString(eachname));
                        if(eachname == "auteur")
                            auteur_.add(json_data.getString(eachname));
                        if(eachname == "id_etudiant")
                            id_et_.add(json_data.getString(eachname));
                        //String stime = json_data.getString(eachname);//chaques colones
                        //Log.e("Stack trace", "Col name : " + stime);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        ListView l = (ListView) findViewById(R.id.listview);
        final MyArrayAdapterRem adapter = new MyArrayAdapterRem(Remarques.this, id_com_, content_, auteur_, id_et_);
        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        id_com_.remove(item);
                        content_.remove(item);
                        auteur_.remove(item);
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
            }
        });
    }
    public String makeHttpRequest(String url, String method, ArrayList<NameValuePair> params) {
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            if (method == "POST") {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method == "GET") {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return sb.toString();
    }
}
