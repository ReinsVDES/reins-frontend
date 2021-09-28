package com.example.myapplication;

import static java.lang.Thread.*;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Handler;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;


public class MainActivity extends AppCompatActivity {
    public static String setn=new String();

    public static int I=1;
    public ArrayList<String> name=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PostThread1 postThread1=new PostThread1();
        postThread1.start();
        Log.d("length",String.valueOf(name.size()));
        try {
            sleep(300);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("sleep","over");
        for (int i = 0; i < name.size(); i++) {
            TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tablelayout1);
            TableRow tablerow = new TableRow(getApplicationContext());
            Button setname = new Button(getApplicationContext());
            setname.setText(name.get(i));
            tablerow.addView(setname);
            tableLayout1.addView(tablerow);
            setname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setn=setname.getText().toString();
                    Log.d("tiao","tiao");
                    Intent i = new Intent(MainActivity.this , SecondActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    @Data
    public class kv{
        private String name;
        private String type;

        public void setName(String name){
            this.name=name;
        }

        public void setType(String type){
            this.type=type;
        }

        public String getName(){
            return name;
        }

        public String getType(){
            return type;
        }
    }

    public class Set{
        private String setName;
        private List<kv> metadata=new ArrayList<>();
        private String type="key-value";
        private Boolean visible;

        public void setSetName(String setName){
            this.setName=setName;
        }

        public void setMetadata(kv data){
            this.metadata.add(data);
        }

        public void setVisible(){
            visible=false;
        }

        public String getSetName(){
            return setName;
        }

        public List<kv> getMetadata(){
            return metadata;
        }

        public String getType(){
            return type;
        }

        public Boolean getVisible(){
            return visible;
        }
    }

    class PostThread extends Thread {
        private Set Message=new Set();
        private kv kv1=new kv();
        private kv kv2=new kv();
        private kv kv3=new kv();
        private String setName=new String();
        public PostThread(String setName){
            Message.setSetName(setName);
            kv1.setName("key1");
            kv1.setType("Integer");
            kv2.setName("date");
            kv2.setType("Date");
            kv3.setName("location");
            kv3.setType("String");
            Message.setMetadata(kv1);
            Message.setMetadata(kv2);
            Message.setMetadata(kv3);
            Message.setVisible();
        }
        @Override
        public void run() {
            MyHttpClient httpClient = new MyHttpClient(getApplicationContext());
            String url = "https://10.0.0.203:443/data/createDataSet";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("token",LoginActivity.token);
            httpPost.addHeader("Content-Type", "application/json");

            Gson gson = new Gson();
            String json = gson.toJson(Message);
            try {
                StringEntity requestEntity = new StringEntity(json,HTTP.UTF_8);
                httpPost.setEntity(requestEntity);
                //执行请求对象
                try {
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    HttpResponse response = httpClient.execute(httpPost);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        Log.d("HTTP", "POST:" + result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class getedData{
        private double code;
        private Object data;
        private String message;

        public void setCode(double code){
            this.code=code;
        }

        public void setData(Object data){
            this.data=data;
        }

        public void setMessage(String message){
            this.message=message;
        }

        public Object getData(){
            return data;
        }

        public double getCode(){
            return code;
        }

        public String getMessage(){
            return message;
        }
    }


    public class data1{
        private ArrayList<Object> datasets;

        public void setDatasets(Object name){
            this.datasets.add(name);
        }

        public ArrayList<Object> getDatasets(){
            return datasets;
        }
    }

    public class data2{
        private String name;
        private ArrayList<Object> metaDataList;
        private ArrayList<Object> authorityList;
        private Boolean visibleMark;
        private String type;

        public void setName(String name){
            this.name=name;
        }

        public void setMetaDataList(Object metaDataList){
            this.metaDataList.add(metaDataList);
        }

        public void setAuthorityList(Object authorityList){
            this.authorityList.add(authorityList);
        }

        public void setVisibleMark(Boolean visibleMark){
            this.visibleMark=visibleMark;
        }

        public void setType(String type){
            this.type=type;
        }

        public String getName(){
            return name;
        }

        public ArrayList<Object> getMetaDataList(){
            return metaDataList;
        }

        public ArrayList<Object> getAuthorityList(){
            return authorityList;
        }

        public Boolean getVisibleMark(){
            return visibleMark;
        }

        public String getType(){
            return type;
        }
    }

    class PostThread1 extends Thread {
        @Override
        public void run() {
            HttpClient httpClient = new MyHttpClient(getApplicationContext());
            String url = "https://10.0.0.203:443/data/ownedDataSetList";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("token",LoginActivity.token);

            try {
                //执行请求对象
                try {
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    HttpResponse response = httpClient.execute(httpPost);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        Log.d("sleep","over1");
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        Log.d("HTTP", "POST1:" + result);
                        Gson gson = new Gson();
                        getedData message=gson.fromJson(result, getedData.class);

                        String json = gson.toJson(message.getData());
                        data1 message1=gson.fromJson(json,data1.class);
                        Log.d("HTTP", "POST1:" + message1.getDatasets());
                        ArrayList<Object> datasets=message1.getDatasets();
                        for(int i=0;i<datasets.size();i++){
                            json=gson.toJson(datasets.get(i));
                            data2 message2=gson.fromJson(json,data2.class);
                            name.add(message2.getName());
                            Log.d("HTTP", "POSTT:" + message2.getName());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("HTTP", "POST3:"+e);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("HTTP", "POST4:"+e);
            }
        }
    }

    @SuppressLint("ResourceType")
    public void createset(View view){
        EditText editText=(EditText)findViewById(R.id.editTextTextPersonName2);
        TableLayout tableLayout1=(TableLayout)findViewById(R.id.tablelayout1) ;
        String inputText=editText.getText().toString();
        TableRow tablerow = new TableRow(getApplicationContext());
        Button setname= new Button(getApplicationContext());
        setname.setText(inputText);
        tablerow.setId(I);
        tablerow.addView(setname);
        setname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setn=setname.getText().toString();
                Log.d("tiao","tiao");
                Intent i = new Intent(MainActivity.this , SecondActivity.class);
                startActivity(i);
            }
        });
        tableLayout1.addView(tablerow);
        PostThread postThread = new PostThread(inputText);
        postThread.start();
    }

    public void search1(View view){
        Intent i = new Intent(MainActivity.this , SearchActivity.class);
        startActivity(i);
    }

    public  void share1(View view){
        Intent i = new Intent(MainActivity.this , ThirdActivity.class);
        startActivity(i);
    }

    public  void share2(View view){
        Intent i = new Intent(MainActivity.this , SharedActivity.class);
        startActivity(i);
    }
}