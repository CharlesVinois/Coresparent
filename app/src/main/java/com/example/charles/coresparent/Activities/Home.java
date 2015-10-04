package com.example.charles.coresparent.Activities;
import com.example.charles.coresparent.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class Home extends Activity {
    public InputStream is;
    public String result;
    public String log, pas;
    public JSONArray jArray;
    public JSONObject json_data;
    public Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        send=(Button) findViewById(R.id.connect_send);
        is = null;
        jArray = null;
        json_data = null;
        log =null;
        pas =null;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        boolean ret = checkConnect(((EditText) findViewById(R.id.editText)).getText().toString(), ((EditText) findViewById(R.id.editTextpass)).getText().toString());
                        if (ret) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Home.this, Absences.class);
                                    Home.this.startActivity(intent);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Home.this, "Error Login or Password is wrong", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
    public boolean checkConnect(String dstl, String dstp){
        String refl, refp;
        final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {
            result = makeHttpRequest("http://10.0.3.2/access.php?id=access", "POST", nameValuePairs);
            jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length()-1; i++) {
                json_data = jArray.getJSONObject(i);
                log = json_data.getString("login");
                pas = json_data.getString("mot_de_passe");
                if(log.equals(dstl) && pas.equals(dstp))return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String makeHttpRequest(String url, String method, ArrayList<NameValuePair> params) {
        String line, ret = null;
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            ret = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return ret;
    }
}