package com.example.myapplication;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.List;

public class SearchActivity extends AppCompatActivity{
    ArrayList<Object> blocks=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
    }

    public class kv{
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

    public class Set{
        private String userName;
        private String setName;
        private kv filter=new kv();

        public void setUserName(String setName){
            this.userName=setName;
        }

        public void setSetName(String data){
            this.setName=data;
        }

        public void setFilter(kv data){
            this.filter=data;
        }

        public String getSetName(){
            return setName;
        }

        public String getUserName(){
            return userName;
        }

        public kv getFilter(){
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
        private SecondActivity.data3 metaData;

        public void setContent(String content) {
            this.content = content;
        }

        public void setMetaData(SecondActivity.data3 metaData) {
            this.metaData = metaData;
        }

        public String getContent() {
            return content;
        }

        public SecondActivity.data3 getMetaData() {
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

    class PostThread extends Thread {
        EditText username=(EditText) findViewById(R.id.editTextTextPersonName);
        String user=username.getText().toString();
        EditText setname=(EditText) findViewById(R.id.editTextTextPersonName6);
        String set=setname.getText().toString();
        EditText lefttime=(EditText) findViewById(R.id.editTextTextPersonName9);
        String left=lefttime.getText().toString();
        EditText righttime=(EditText) findViewById(R.id.editTextTextPersonName10);
        String right=righttime.getText().toString();

        Set set1=new Set();
        kv kv1=new kv();

        public PostThread() {
            kv1.setLeftInterval(left);
            kv1.setRightInterval(right);
            kv1.setTagName("date");
            set1.setUserName(user);
            set1.setSetName(set);
            set1.setFilter(kv1);
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://10.0.0.203:8080/data/searchRows";
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
                        SecondActivity.getedData message=gson.fromJson(result, SecondActivity.getedData.class);
                        json = gson.toJson(message.getData());
                        SecondActivity.data1 message1=gson.fromJson(json, SecondActivity.data1.class);
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

    public void return2(View view){
            Intent i = new Intent(SearchActivity.this , MainActivity.class);
            startActivity(i);
    }

    public void search3(View view){
        PostThread postThread=new PostThread();
        postThread.start();
//        while(data3.size()<=0) {
//        }
        try {
            sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String json;
        Gson gson = new Gson();
        for (int i = 0; i < blocks.size(); i++) {
            json = gson.toJson(blocks.get(i));
            SecondActivity.data2 message2=gson.fromJson(json, SecondActivity.data2.class);
            TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tablelayout3);
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
        }
    }
}
