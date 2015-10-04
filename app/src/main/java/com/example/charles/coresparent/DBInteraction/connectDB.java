package com.example.charles.coresparent.DBInteraction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import com.example.charles.coresparent.R;
public class connectDB extends Activity {
    public InputStream is;
    public String result;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_db);
        result = null;
        is = null;
        new Thread(new Runnable() {
            public void run() {
                ArrayList<String> colName = new ArrayList<String>(3);
                colName.add("login");
                colName.add("mot_de_passe");
                colName.add("administrateur");
                TableLayout tv = (TableLayout) findViewById(R.id.table);
                makeRequest("http://10.0.3.2/access.php?id=access", colName, tv);
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                ArrayList<String> colName1 = new ArrayList<String>(6);
                colName1.add("id_absence");
                colName1.add("date_debut");
                colName1.add("date_fin");
                colName1.add("nombre_heure");
                colName1.add("justifié");
                colName1.add("id_etudiant");
                TableLayout tv1 = (TableLayout) findViewById(R.id.table1);
                makeRequest("http://10.0.3.2/access.php?id=absences", colName1, tv1);
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                ArrayList<String> colName2 = new ArrayList<String>(4);
                colName2.add("id_commentaire");
                colName2.add("contenu");
                colName2.add("auteur");
                colName2.add("id_etudiant");
                TableLayout tv2 = (TableLayout) findViewById(R.id.table2);
                makeRequest("http://10.0.3.2/access.php?id=commentaires", colName2, tv2);
            }
        }).start();
    }
    public void makeRequest(String url, final ArrayList<String> colName, final TableLayout tv){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        /*nameValuePairs.add(new BasicNameValuePair("user_name", "george"));
        nameValuePairs.add(new BasicNameValuePair("user_passwd", "georges"));*/
        result = makeHttpRequest(url, "POST", nameValuePairs);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTabler(result, colName, tv);
            }
        });
    }
    //Display in the selected table Row
    public void setTabler(String jarray, ArrayList<String> colName, TableLayout tv){
        try {
            JSONArray jArray = new JSONArray(jarray);
            tv.removeAllViewsInLayout();
            int flg = 1;
            for (int i = -1; i < jArray.length() - 1; i++) {
                TableRow tr = new TableRow(connectDB.this);
                tr.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                flg=setTable(tv, tr, jArray, colName, i, flg);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }
    public int setTable(TableLayout tv,TableRow tr, JSONArray jArray, ArrayList<String> colName,
                        int i, int flg){
        int flag = flg;
        if (flag == 1) {
            for (int j = 0; j < colName.size(); j++)
            {
                TextView b6 = new TextView(connectDB.this);
                b6.setText(colName.get(j));
                b6.setTextColor(Color.BLUE);
                //b6.setTextSize(15);
                b6.setPadding(10, 0, 0, 0);
                tr.addView(b6);
            }
            flag = 0;
            tv.addView(tr);
            final View vline = new View(connectDB.this);
            vline.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 2));
            vline.setBackgroundColor(Color.BLUE);
            tv.addView(vline);
        } else {
            JSONObject json_data = null;
            try {
                json_data = jArray.getJSONObject(i);
                for (int j = 0; j < colName.size(); j++) {
                    TextView b = new TextView(connectDB.this);
                    String stime = json_data.getString(colName.get(j));
                    b.setText(stime);
                    b.setTextColor(Color.RED);
                    b.setPadding(10, 0, 0, 0);
                    b.setTextSize(15);
                    tr.addView(b);
                }
            tv.addView(tr);
            final View vline1 = new View(connectDB.this);
            vline1.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 1));
            vline1.setBackgroundColor(Color.WHITE);
            tv.addView(vline1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return flag;
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