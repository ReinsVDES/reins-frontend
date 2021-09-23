package com.example.myapplication;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class LoginActivity extends AppCompatActivity {
    private EditText nameText;
    private EditText pwdText;
    private Button button;
    public static String token;
    public static String refreshToken;
    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        nameText = (EditText) findViewById(R.id.editTextTextPersonName7);
        pwdText = (EditText) findViewById(R.id.editTextTextPassword);
        button = (Button) findViewById(R.id.button8);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 用户输入用户名密码， 然后通过Get方法发送给本地服务器
                String name = nameText.getText().toString();
                String pwd = pwdText.getText().toString();
                username=name;
                //使用POST方法向服务器发送数据
                PostThread postThread = new PostThread(name, pwd);
                postThread.start();
            }
        });
    }

    public class Word{
        private String token;
        private String refreshToken;

        public void setToken(String token){
            this.token=token;
        }

        public void setRefreshToken(String refreshToken){
            this.refreshToken=refreshToken;
        }

        public String getToken(){
            return token;
        }

        public String getRefreshToken(){
            return refreshToken;
        }
    }

    public class Message{
        private double code;
        private Word data;
        private String message;

        public void setCode(double code){
            this.code=code;
        }

        public void setData(Word data){
            this.data=data;
        }

        public void setMessage(String message){
            this.message=message;
        }

        public double getCode(){
            return code;
        }

        public Word getData(){
            return data;
        }

        public String getMessage(){
            return message;
        }
    }

    class PostThread extends Thread {

        String name;
        String pwd;

        public PostThread(String name, String pwd) {
            this.name = name;
            this.pwd = pwd;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://10.0.0.203:8080/Login";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对
            NameValuePair pair1 = new BasicNameValuePair("username", name);
            NameValuePair pair2 = new BasicNameValuePair("password", pwd);
            //将准备好的键值对对象放置在一个List当中
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(pair1);
            pairs.add(pair2);
            try {
                //创建代表请求体的对象（注意，是请求体）
                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                //将请求体放置在请求对象当中
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
                        Gson gson = new Gson();
                        Message message=gson.fromJson(result,Message.class);
                        token=message.getData().getToken();
                        Log.d("HTTP", "POST:"+token);
                        refreshToken=message.getData().getRefreshToken();
                        Log.d("HTTP", "POST:"+refreshToken);
                        Intent i = new Intent(LoginActivity.this , MainActivity.class);
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

    public void register1(View view){
        Intent i = new Intent(LoginActivity.this , RegisterActivity.class);
        startActivity(i);
    }
}
