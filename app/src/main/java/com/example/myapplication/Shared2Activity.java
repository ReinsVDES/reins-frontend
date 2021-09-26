package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import static java.lang.Thread.sleep;
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

import lombok.Data;

public class Shared2Activity extends AppCompatActivity {
    private String sharedu=SharedActivity.shareduser;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared2_activity);
        PostThread postThread=new PostThread();
        postThread.start();
        try{
            sleep(200);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    public class kv{
        private String username;

        public void setTagName(String name){
            this.username=name;
        }

        public String getTagName(){
            return username;
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
    @Data
    public class AuthInfo{
        private DataSet dataSet;
        private String tagName;
        private String leftInterval;
        private String rightInterval;
    }
    @Data
    public class DataSet{
        private String name;
    }
    @Data
    public class data1{
        private ArrayList<AuthInfo> authorities;
    }
    class PostThread extends Thread {
        kv kv1=new kv();
        public PostThread() {
            kv1.setTagName(sharedu);
        }
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            HttpClient httpClient = new MyHttpClient(getApplicationContext());
            String url = "https://10.0.0.203:443/data/receivedDataSet";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对
            httpPost.addHeader("token",LoginActivity.token);
            httpPost.addHeader("Content-Type", "application/json");
            Gson gson = new Gson();
            String json = gson.toJson(kv1);
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
                        result = reader.readLine();

                        SecondActivity.getedData message=gson.fromJson(result, SecondActivity.getedData.class);
                        json = gson.toJson(message.getData());
                        Shared2Activity.data1 message1=gson.fromJson(json, Shared2Activity.data1.class);
                        for (AuthInfo authInfo :message1.getAuthorities()){
                            Log.d("HTTP", "00000:"+authInfo.getDataSet().getName());
                            TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tablelayout5);
                            TableRow tablerow = new TableRow(getApplicationContext());
                            TextView date = new TextView(getApplicationContext());
                            String txt = "数据集名称："+authInfo.getDataSet().getName();
                            if(authInfo.getTagName()!=null){
                                txt+= "查询范围：" + authInfo.getTagName();
                                if(authInfo.getLeftInterval()!=null || authInfo.getRightInterval()!= null){
                                    txt+="区间"+
                                            (authInfo.getLeftInterval()!=null?authInfo.getLeftInterval():"无限") +
                                            "至"+
                                            (authInfo.getRightInterval()!=null?authInfo.getRightInterval():"无限");
                                }
                            }
                            date.setText(txt);
                            tablerow.addView(date);
                            tableLayout1.addView(tablerow);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void return4(View view){
        Intent i = new Intent(Shared2Activity.this , SharedActivity.class);
        startActivity(i);
    }
}
