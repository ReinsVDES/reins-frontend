package com.example.myapplication;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText nameText;
    private EditText pwdText;
    private Button button;
    private EditText area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        nameText = (EditText) findViewById(R.id.editTextTextPersonName5);
        pwdText = (EditText) findViewById(R.id.editTextTextPassword2);
        area = (EditText)  findViewById(R.id.editTextTextPersonName8);
        button = (Button) findViewById(R.id.button10);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 用户输入用户名密码， 然后通过Get方法发送给本地服务器
                String name = nameText.getText().toString();
                String pwd = pwdText.getText().toString();
                String ar = area.getText().toString();
                // 使用GET方法向本地服务器发送数据
                //GetThread getThread = new GetThread(name, pwd);
                //getThread.start();

                //使用POST方法向服务器发送数据
                PostThread postThread = new PostThread(name, pwd,ar);
                postThread.start();
            }
        });
    }

    class PostThread extends Thread {

        String name;
        String pwd;
        String ar;

        public PostThread(String name, String pwd ,String ar) {
            this.name = name;
            this.pwd = pwd;
            this.ar = ar;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://10.0.0.203:8080/register";
            //第二步：生成使用POST方法的请求对象
            HttpPost httpPost = new HttpPost(url);
            //NameValuePair对象代表了一个需要发往服务器的键值对
            NameValuePair pair1 = new BasicNameValuePair("username", name);
            NameValuePair pair2 = new BasicNameValuePair("password", pwd);
            NameValuePair pair3 = new BasicNameValuePair("userRegion", ar);
            //将准备好的键值对对象放置在一个List当中
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(pair1);
            pairs.add(pair2);
            pairs.add(pair3);
            try {
                //创建代表请求体的对象（注意，是请求体）
                HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                //将请求体放置在请求对象当中
                httpPost.setEntity(requestEntity);
                //执行请求对象
                try {
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    Log.d("HTTP", "POST1:");
                    HttpResponse response = httpClient.execute(httpPost);
                    Log.d("HTTP", "POST5:");
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        Log.d("HTTP", "POST2:");
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        Log.d("HTTP", "POST:" + result);
                        Intent i = new Intent(RegisterActivity.this , LoginActivity.class);
                        startActivity(i);
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


}
