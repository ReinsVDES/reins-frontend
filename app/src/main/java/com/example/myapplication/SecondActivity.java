package com.example.myapplication;

import static java.lang.Thread.sleep;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    ArrayList<Object> blocks=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        PostThread1 postThread1=new PostThread1();
        postThread1.start();
//        while(data3.size()<=0) {
//        }
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String json;
        Gson gson = new Gson();
        for (int i = 0; i < blocks.size(); i++) {
            json = gson.toJson(blocks.get(i));
            data2 message2=gson.fromJson(json,data2.class);
            TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tablelaout2);
            TableRow tablerow = new TableRow(getApplicationContext());
            TextView date = new TextView(getApplicationContext());
            TextView location = new TextView(getApplicationContext());
            TextView content = new TextView(getApplicationContext());
            date.setText(message2.getMetaData().getDate());
            location.setText(message2.getMetaData().getLocation());
            content.setText(message2.getContent());
            tablerow.addView(date);
            tablerow.addView(location);
            tablerow.addView(content);
            tableLayout1.addView(tablerow);
            Log.d("view",json );
        }
    }

    public class kvv{
        private String tagName;
        private String leftInterval;
        private String rightInterval;

        public void setTagName(String name){
            this.tagName=name;
        }

        public void setLeftInterval(String type){
            this.leftInterval=type;
        }

        public void setRightInterval(String type){
            this.rightInterval=type;
        }

        public String getTagName(){
            return tagName;
        }

        public String getLeftInterval(){
            return leftInterval;
        }

        public String getRightInterval(){
            return rightInterval;
        }
    }

    public class Sett{
        private String userName;
        private String setName;
        private kvv filter=new kvv();

        public void setUserName(String setName){
            this.userName=setName;
        }

        public void setSetName(String data){
            this.setName=data;
        }

        public void setFilter(kvv data){
            this.filter=data;
        }

        public String getSetName(){
            return setName;
        }

        public String getUserName(){
            return userName;
        }

        public kvv getFilter(){
            return filter;
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
        private ArrayList<Object> blocks;

        public void setDatasets(Object name){
            this.blocks.add(name);
        }

        public ArrayList<Object> getDatasets(){
            return blocks;
        }
    }

    public class data2{
        private String content;
        private data3 metaData;

        public void setContent(String content) {
            this.content = content;
        }

        public void setMetaData(data3 metaData) {
            this.metaData = metaData;
        }

        public String getContent() {
            return content;
        }

        public data3 getMetaData() {
            return metaData;
        }
    }

    public class data3{
        private String key1;
        private String date;
        private String location;

        public void setDate(String date) {
            this.date = date;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setKey1(String key1) {
            this.key1 = key1;
        }

        public String getDate() {
            return date;
        }

        public String getKey1() {
            return key1;
        }

        public String getLocation() {
            return location;
        }
    }

    class PostThread1 extends Thread {
        Sett set1=new Sett();
        kvv kv1=new kvv();


        public PostThread1() {
            kv1.setLeftInterval("2000-01-01");
            kv1.setRightInterval(null);
            kv1.setTagName("date");
            set1.setUserName(LoginActivity.username);
            set1.setSetName(MainActivity.setn);
            set1.setFilter(kv1);
        }

        @Override
        public void run() {
            HttpClient httpClient = new MyHttpClient(getApplicationContext());
            String url = "https://10.0.0.203:443/data/searchRows";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对
            httpPost.addHeader("token",LoginActivity.token);
            httpPost.addHeader("Content-Type", "application/json");

            Gson gson = new Gson();
            String json = gson.toJson(set1);
            try {
                StringEntity requestEntity = new StringEntity(json, HTTP.UTF_8);
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
                        Log.d("HTTP", "PPPPP:" + result);
                        getedData message=gson.fromJson(result, getedData.class);
                        json = gson.toJson(message.getData());
                        data1 message1=gson.fromJson(json, data1.class);
                        Log.d("HTTP", "PPPPP:" + message1.getDatasets());
                        blocks=message1.getDatasets();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class kv{
        private String name;
        private String value;

        public void setName(String name){
            this.name=name;
        }

        public void setValue(String type){
            this.value=type;
        }

        public String getName(){
            return name;
        }

        public String getType(){
            return value;
        }
    }

    public class Set{
        private String dataBlock;
        private String setName;
        private ArrayList<kv> metadata=new ArrayList<>();

        public void setDataBlock(String setName){
            this.dataBlock=setName;
        }

        public void setSetName(String data){
            this.setName=data;
        }

        public void setMetadata(kv data){
            this.metadata.add(data);
        }

        public String getSetName(){
            return setName;
        }

        public String getDataBlock(){
            return dataBlock;
        }

        public ArrayList<kv> getMetadata(){
            return metadata;
        }
    }

    class PostThread extends Thread {
        EditText date=(EditText) findViewById(R.id.editTextTextPersonName11);
        String date1=date.getText().toString();
        EditText locat=(EditText) findViewById(R.id.editTextTextPersonName12);
        String locat1=locat.getText().toString();
        EditText data=(EditText) findViewById(R.id.editTextTextPersonName13);
        String data1=data.getText().toString();

        Set set1=new Set();
        kv kv1=new kv();
        kv kv2=new kv();
        kv kv3=new kv();

        public PostThread() {
            kv1.setName("key1");
            kv1.setValue("1");
            kv2.setName("date");
            kv2.setValue(date1);
            kv3.setName("location");
            kv3.setValue(locat1);
            set1.setSetName(MainActivity.setn);
            set1.setDataBlock(data1);
            set1.setMetadata(kv1);
            set1.setMetadata(kv2);
            set1.setMetadata(kv3);
        }

        @Override
        public void run() {
            HttpClient httpClient = new MyHttpClient(getApplicationContext());
            String url = "https://10.0.0.203:443/dataBlock/addDataRow";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("token",LoginActivity.token);
            httpPost.addHeader("Content-Type", "application/json");

            Gson gson = new Gson();
            String json = gson.toJson(set1);
            try {
                StringEntity requestEntity = new StringEntity(json, HTTP.UTF_8);
                httpPost.setEntity(requestEntity);
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
                        Log.d("SSSSS",result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addline(View view){
        EditText editText=(EditText)findViewById(R.id.editTextTextPersonName11);
        EditText editText1=(EditText)findViewById(R.id.editTextTextPersonName12);
        EditText editText2=(EditText)findViewById(R.id.editTextTextPersonName13);
        TableLayout tableLayout1=(TableLayout)findViewById(R.id.tablelaout2) ;
        String inputText=editText.getText().toString();
        String inputText1=editText1.getText().toString();
        String inputText2=editText2.getText().toString();
        TableRow tablerow = new TableRow(getApplicationContext());
        TextView textView = new TextView(getApplicationContext());
        TextView textView1 = new TextView(getApplicationContext());
        TextView textView2 = new TextView(getApplicationContext());
        textView.setText(inputText);
        textView1.setText(inputText1);
        textView2.setText(inputText2);
        tablerow.addView(textView);
        tablerow.addView(textView1);
        tablerow.addView(textView2);
        tableLayout1.addView(tablerow);
        PostThread postThread = new PostThread();
        postThread.start();
    }

    public void return1(View view){
        Intent i = new Intent(SecondActivity.this , MainActivity.class);
        startActivity(i);
    }
}