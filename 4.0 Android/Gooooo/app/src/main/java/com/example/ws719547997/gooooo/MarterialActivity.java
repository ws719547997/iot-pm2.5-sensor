package com.example.ws719547997.gooooo;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;


public class MarterialActivity extends AppCompatActivity implements View.OnClickListener {


    public String adr;
    public String t1;
    public String h1;
    public String p1;

    MqttAndroidClient client;

    public TextView t1_textview;
    public TextView h1_textview;
    public TextView p1_textview;
    public TextView t2_textview;
    public TextView h2_textview;
    public TextView p2_textview;
    public TextView status_textview;

    public FloatingActionButton fab1;

    boolean isMqtt=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marterial);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.ctoolbar);

        t1_textview = (TextView) findViewById(R.id.t1_textview);
        t2_textview = (TextView) findViewById(R.id.t2_textview);
        h1_textview = (TextView) findViewById(R.id.h1_textview);
        h2_textview = (TextView) findViewById(R.id.h2_textview);
        p1_textview = (TextView) findViewById(R.id.p1_textview);
        p2_textview = (TextView) findViewById(R.id.p2_textview);
        status_textview= (TextView) findViewById(R.id.text_status);

        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        setSupportActionBar(toolbar);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://140.143.222.251:1883",
                clientId);

        collapsingToolbar.setTitle("环境数据");


        fab1.setOnClickListener(this);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                parseWithJson(new String(mqttMessage.getPayload()));
                setTextWithJson();

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

    }

    public void parseWithJson(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            adr = jsonObject.getString("adr");
            t1=jsonObject.getString("t");
            h1=jsonObject.getString("h");
            p1=jsonObject.getString("pm");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    public void setTextWithJson(){
        int intAdr=Integer.parseInt(adr);
        if(intAdr==2){
            t2_textview.setText(t1);
            h2_textview.setText(h1);
            p2_textview.setText(p1);
        }else{
            t1_textview.setText(t1);
            h1_textview.setText(h1);
            p1_textview.setText(p1);
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.fab1:
                if(isMqtt==false){
                    try {
                        MqttConnectOptions Mqttoptions=new MqttConnectOptions();
                        Mqttoptions.setUserName("root");
                        Mqttoptions.setPassword("ws123456".toCharArray());
                        Mqttoptions.setCleanSession(true);
                        IMqttToken token = client.connect(Mqttoptions);
                        token.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Snackbar.make(v, "连接成功~", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                setsub();
                                isMqtt=true;
                                status_textview.setText("on");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                                Snackbar.make(v, "连接失败...", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }else{
                    try {
                        IMqttToken token = client.disconnect();
                        token.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // We are connected
                                Snackbar.make(v, "断开成功~", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                isMqtt=false;
                                status_textview.setText("off");
                            }
                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                // Something went wrong e.g. connection timeout or firewall problems
                                Snackbar.make(v, "断开失败...", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public void setsub(){
        try{
            client.subscribe("wsn/#",0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            MqttConnectOptions Mqttoptions=new MqttConnectOptions();
            Mqttoptions.setUserName("root");
            Mqttoptions.setPassword("ws123456".toCharArray());
            Mqttoptions.setCleanSession(true);
            IMqttToken token = client.connect(Mqttoptions);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    setsub();
                    isMqtt=true;
                    status_textview.setText("on");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    isMqtt=false;
                    status_textview.setText("off");
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
