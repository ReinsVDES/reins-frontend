package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

public class ThirdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_activity);
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
        private String queryUser;
        private String setName;
        private ArrayList<kv> rangeAllow=new ArrayList<>();

        public void setQueryUser(String setName){
            this.queryUser=setName;
        }

        public void setSetName(String data){
            this.setName=data;
        }

        public void setRangeAllow(kv data){
            this.rangeAllow.add(data);
        }

        public String getSetName(){
            return setName;
        }

        public String getQueryUser(){
            return queryUser;
        }

        public ArrayList<kv> getMetadata(){
            return rangeAllow;
        }
    }

    class PostThread extends Thread {
        EditText username=(EditText) findViewById(R.id.editTextTextPersonName3);
        String user=username.getText().toString();
        EditText setname=(EditText) findViewById(R.id.editTextTextPersonName4);
        String set=setname.getText().toString();
        EditText lefttime=(EditText) findViewById(R.id.editTextDate);
        String left=lefttime.getText().toString();
        EditText righttime=(EditText) findViewById(R.id.editTextDate2);
        String right=righttime.getText().toString();

        Set set1=new Set();
        kv kv1=new kv();

        public PostThread() {
            kv1.setLeftInterval(left);
            kv1.setRightInterval(right);
            kv1.setTagName("date");
            set1.setQueryUser(user);
            set1.setSetName(set);
            set1.setRangeAllow(kv1);
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://10.0.0.203:8080/data/authAdd";
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
                        Log.d("HTTP", "00000:"+result);
                        Intent i = new Intent(ThirdActivity.this , MainActivity.class);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void return1(View view){
        PostThread postThread=new PostThread();
        postThread.start();
    }
}
