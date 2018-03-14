package com.example.ws719547997.gooooo;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    MqttAndroidClient client;
    TextView adrText;
    TextView tText;
    TextView hText;
    TextView pmText;
    Vibrator vibrator;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ringtone=RingtoneManager.getRingtone(getApplicationContext(),uri);

        adrText= (TextView) findViewById(R.id.adr);
        hText= (TextView) findViewById(R.id.h);
        tText= (TextView) findViewById(R.id.t);
        pmText= (TextView) findViewById(R.id.pm);

        Button button1= (Button) findViewById(R.id.button);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MarterialActivity.class);
                startActivity(intent);
            }
        });

        client = new MqttAndroidClient(this.getApplicationContext(),"tcp://1.1.1.1:1883",
                        "android mqtt client");


        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                adrText.setText(new String(mqttMessage.getPayload()));
//                parseWithJson(new String(mqttMessage.getPayload()));

               // vibrator.vibrate(200);
                // ringtone.play();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

//    public void pub(View view){
//        String topic = "wsn/android";
//        String msg = "我成功了";
//
//        try {
//
//            client.publish(topic, msg.getBytes(),0,false);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//
//    }

//    public void setsub(){
//        try{
//            client.subscribe("wsn/#",0);
//        }catch (MqttException e){
//            e.printStackTrace();
//        }
//    }

    public  void conn(View view){
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(MainActivity.this,"连接成功~",Toast.LENGTH_LONG).show();
//                    setsub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this,"连接失败 ...",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void discon(View view){
        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(MainActivity.this,"断开成功~",Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this,"断开失败 ...",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

//    public void parseWithJson(String jsonData) {
//
//        try {
//                JSONObject jsonObject=new JSONObject(jsonData);
//                String wsn_address = jsonObject.getString("adr");
//                String wsn_humidity = jsonObject.getString("h");
//                String wsn_temperature = jsonObject.getString("t");
//                String wsn_pm25 = jsonObject.getString("pm");
//                tText.setText(wsn_humidity);
//                hText.setText(wsn_temperature);
//                pmText.setText(wsn_pm25);
//            } catch (JSONException e1) {
//            e1.printStackTrace();
//        }}



}



