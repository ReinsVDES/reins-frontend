package com.example.myapplication;

import static java.lang.Thread.sleep;

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
import java.util.List;

public class SharedActivity extends AppCompatActivity {
    public static String shareduser;
    public ArrayList<String> userlist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_activity);
        PostThread postThread=new PostThread();
        postThread.start();
//        while(userlist.size()<=0) {
//        }
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < userlist.size(); i++) {
            TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tablelayout4);
            TableRow tablerow = new TableRow(getApplicationContext());
            TextView user = new TextView(getApplicationContext());
            user.setText(userlist.get(i));
            tablerow.addView(user);
            tableLayout1.addView(tablerow);
            Log.d("view", "v");
        }
    }

    public class getedData{
        private double code;
        private ArrayList<String> data;
        private String message;

        public void setCode(double code){
            this.code=code;
        }

        public void setData(ArrayList<String> data){
            this.data=data;
        }

        public void setMessage(String message){
            this.message=message;
        }

        public ArrayList<String> getData(){
            return data;
        }

        public double getCode(){
            return code;
        }

        public String getMessage(){
            return message;
        }
    }

    class PostThread extends Thread {
        @Override
        public void run() {
            HttpClient httpClient = new MyHttpClient(getApplicationContext());
            String url = "https://10.0.0.203:443/data/receivedUserList";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对
            httpPost.addHeader("token",LoginActivity.token);
            Gson gson = new Gson();
            String json = new String();
            try {
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
                        getedData message=gson.fromJson(result, getedData.class);
                        Log.d("HTTP", "PPPPP:" +message.getData().get(0));
                        userlist= message.getData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shared2(View view){
        EditText editText=(EditText)findViewById(R.id.editTextTextPersonName14);
        String inputText=editText.getText().toString();
        shareduser=inputText;
        Intent i = new Intent(SharedActivity.this , Shared2Activity.class);
        startActivity(i);
    }

    public void return3(View view){
        Intent i = new Intent(SharedActivity.this , MainActivity.class);
        startActivity(i);
    }
}
